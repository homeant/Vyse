package fun.vyse.cloud.define.service.impl;

import fun.vyse.cloud.core.constant.EntityState;
import fun.vyse.cloud.core.domain.IFixedEntity;
import fun.vyse.cloud.define.domain.DomainModel;
import fun.vyse.cloud.define.domain.MetaDefinition;
import fun.vyse.cloud.define.domain.Model;
import fun.vyse.cloud.define.entity.actual.ActModelEO;
import fun.vyse.cloud.define.entity.actual.ActPropertyEO;
import fun.vyse.cloud.define.entity.specification.SpecConnectionEO;
import fun.vyse.cloud.define.entity.specification.SpecFixedModelEO;
import fun.vyse.cloud.define.entity.specification.SpecModelEO;
import fun.vyse.cloud.define.entity.specification.SpecPropertyEO;
import fun.vyse.cloud.define.service.IActModelService;
import fun.vyse.cloud.define.service.IActPropertyService;
import fun.vyse.cloud.define.service.IDomainModelService;
import fun.vyse.cloud.define.service.IMetaDefinitionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static fun.vyse.cloud.define.entity.actual.ActPropertyEO.PropertyType;


import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * DomainModelServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-25 11:37
 */
public class DomainModelServiceImpl implements IDomainModelService {

	@Autowired
	private IMetaDefinitionService metaDefinitionService;

	@Autowired
	private IActModelService modelDataService;

	@Autowired
	private IActPropertyService actPropertyService;

	/**
	 * 创建领域模型
	 *
	 * @param modelId
	 * @return
	 */
	@Override
	public DomainModel createDomainModel(Long modelId, Map<String, Object> entity) {
		MetaDefinition<Long> md = metaDefinitionService.getMetaDefinition("");
		Model model = md.buildModel(null, modelId);
		return this.createDomainModel(md, null, model, modelId, entity);
	}

	/**
	 * 创建领域模型
	 *
	 * @param md
	 * @param parent
	 * @param model
	 * @param entity
	 * @return
	 */
	@Override
	public DomainModel createDomainModel(MetaDefinition<Long> md, DomainModel parent, Model model, Long id, Map<String, Object> entity) {
		SpecModelEO modelEO = md.getModel(id);
		if (modelEO != null) {
			ActModelEO dataEO = new ActModelEO();
			dataEO.setDomainId(id);
			dataEO.setCode(modelEO.getCode());
			dataEO.updateDirtyFlag(EntityState.New);
			Long fixedId = modelEO.getFixedId();
			DomainModel domainModel = new DomainModel(dataEO, md);
			Long topId;
			String parentPath = "";
			Integer loadType = 0;
			if (parent == null) {
				topId = id;
				dataEO.setTopId(topId);
			} else {
				topId = parent.getEntity().getTopId();
				parent.updateState$(EntityState.Modify);
				Long parentId = parent.getId();
				dataEO.setParentId(parentId);
				SpecConnectionEO connection = md.getConnection(parentId, id, null);
				if (connection != null) {
					loadType = connection.getLoadType();
				}
				if (loadType == 0) {//即时加载
					parent.put(domainModel);
					domainModel.setParentModel(parent);
					parentPath = parent.getEntity().getPath();
				} else if (loadType == -1) {
					// TODO 需要开发懒加载功能
				}

			}
			dataEO.setTopId(topId);

			if (ObjectUtils.isNotEmpty(fixedId)) {
				dataEO.setFixedId(modelEO.getFixedId());
				SpecFixedModelEO fixedModelEO = md.getFixedModelEO(1L);
				Object fixedInstance = fixedModelEO.createFixedInstance();
				domainModel.setFixedModel((IFixedEntity) fixedInstance);
			}
			//设置属性
			List<SpecPropertyEO> propertyEOS = model.findChildren(SpecPropertyEO.class);
			if (CollectionUtils.isNotEmpty(propertyEOS)) {
				ActPropertyEO property = null;
				Iterator iterator = propertyEOS.iterator();
				Integer index = 0;
				int i = 0;
				while1:
				while (true) {
					do {
						do {
							if (iterator.hasNext()) {
								break while1;
							}
							SpecPropertyEO propertyEO = (SpecPropertyEO) iterator.next();
							Object defVal = propertyEO.getDefValue();
							String code = propertyEO.getCode();
							if (code != null && entity.containsKey(code)) {
								defVal = entity.get(code);
							}
							Boolean fixed = md.isFixed(modelEO, propertyEO);
							if (!fixed) {//不是静态表
								if (property == null) {
									property = actPropertyService.newActual(null);
									property.setSerialIndex(index);
									property.setParentId(modelEO.getId());
									property.setTopId(topId);
									++index;
								}
								property.set(PropertyType.code, index, propertyEO.getCode());
								property.set(PropertyType.domainId, index, propertyEO.getId());
								property.set(PropertyType.domainId, index, propertyEO.getId());
								property.set(PropertyType.dateType, index, propertyEO.getDataType());
								property.set(PropertyType.pattern, index, propertyEO.getPattern());
								String path;
								if (StringUtils.isBlank(parentPath)) {
									path = propertyEO.getCode();
								} else {
									path = parentPath + "." + propertyEO.getCode();
								}
								property.set(PropertyType.path, index, path);
								if (defVal != null) {
									property.set(index, defVal);
								}
								++index;
							} else {
								domainModel.setFixedValue(code, defVal);
							}
						} while (property == null);
					} while (index < ActPropertyEO.PROPERTY_NUMBER && i < propertyEOS.size());
					property.setCurrentIndex(index);
					domainModel.put(property);
					property = null;
				}
			}
			//添加action对象
			//添加子模型
			domainModel.updateState$(EntityState.New);
			return domainModel;
		}
		return null;
	}


	/**
	 * 更新领域模型
	 *
	 * @param model
	 * @return
	 */
	@Override
	public DomainModel updateDomainModel(DomainModel model) {
		return null;
	}

	/**
	 * 删除领域模型
	 *
	 * @param model
	 * @return
	 */
	@Override
	public boolean removeDomainModel(DomainModel model) {
		return false;
	}
}
