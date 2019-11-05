package fun.vyse.cloud.define.service.impl;

import fun.vyse.cloud.core.constant.EntityState;
import fun.vyse.cloud.core.domain.IFixedEntity;
import fun.vyse.cloud.define.domain.DomainModel;
import fun.vyse.cloud.define.domain.MetaDefinition;
import fun.vyse.cloud.define.domain.Specification;
import fun.vyse.cloud.define.entity.actual.ConnectionActEO;
import fun.vyse.cloud.define.entity.actual.ModelActEO;
import fun.vyse.cloud.define.entity.actual.PropertyActEO;
import fun.vyse.cloud.define.entity.specification.ConnectionSpecEO;
import fun.vyse.cloud.define.entity.specification.FixedModelSpecEO;
import fun.vyse.cloud.define.entity.specification.ModelSpecEO;
import fun.vyse.cloud.define.entity.specification.PropertySpecEO;
import fun.vyse.cloud.define.service.IDomainModelService;
import fun.vyse.cloud.define.service.IMetaDefinitionService;
import fun.vyse.cloud.define.service.IModelActService;
import fun.vyse.cloud.define.service.IPropertyActService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import static fun.vyse.cloud.define.entity.actual.PropertyActEO.PropertyType;


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
	private IModelActService modelDataService;

	@Autowired
	private IPropertyActService actPropertyService;

	/**
	 * 创建领域模型
	 *
	 * @param modelId
	 * @return
	 */
	@Override
	public DomainModel createDomainModel(Long modelId, Map<String, Object> entity) {
		MetaDefinition<Long> md = metaDefinitionService.getMetaDefinition("");
		Specification model = md.buildSpec(null, modelId);
		return this.createDomainModel(md, null, model, modelId, entity);
	}

	/**
	 * 创建领域模型
	 *
	 * @param md
	 * @param parent
	 * @param spec
	 * @param entity
	 * @return
	 */
	private DomainModel createDomainModel(MetaDefinition<Long> md, DomainModel parent, Specification spec, Long id, Map<String, Object> entity) {
		ModelSpecEO modelEO = md.getModel(id);
		if (modelEO != null) {
			ModelActEO modelActEO = new ModelActEO();
			modelActEO.setDomainId(id);
			modelActEO.setCode(modelEO.getCode());
			modelActEO.updateDirtyFlag(EntityState.New);
			Long fixedId = modelEO.getFixedId();
			DomainModel domainModel = new DomainModel(modelActEO, md);
			domainModel.updateState$(EntityState.New);
			Long topId;
			String parentPath = "";
			Integer loadType = 0;
			if (parent == null) {
				topId = id;
				modelActEO.setTopId(topId);
			} else {
				topId = parent.getEntity().getTopId();
				parent.updateState$(EntityState.Modify);
				Long parentId = parent.getId();
				modelActEO.setParentId(parentId);
				ConnectionSpecEO connection = md.getConnection(parentId, id, null);
				if (connection != null) {
					loadType = connection.getLoadType();
				}
				if (loadType == 0) {//即时加载
					parent.put(domainModel);
					domainModel.setParentModel(parent);
					parentPath = parent.getEntity().getPath();
				} else if (loadType == -1) {
					ConnectionActEO connectionActEO = new ConnectionActEO();
					connectionActEO.setDomainId(modelActEO.getId());
				}

			}
			modelActEO.setTopId(topId);

			if (ObjectUtils.isNotEmpty(fixedId)) {
				modelActEO.setFixedId(modelEO.getFixedId());
				FixedModelSpecEO fixedModelEO = md.getFixedModelEO(1L);
				Object fixedInstance = fixedModelEO.createFixedInstance();
				domainModel.setFixedModel((IFixedEntity) fixedInstance);
			}
			//设置属性
			List<PropertySpecEO> propertyEOS = spec.findChildren(PropertySpecEO.class);
			if (CollectionUtils.isNotEmpty(propertyEOS)) {
				PropertyActEO property = null;
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
							PropertySpecEO propertyEO = (PropertySpecEO) iterator.next();
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
					} while (index < PropertyActEO.PROPERTY_NUMBER && i < propertyEOS.size());
					property.setCurrentIndex(index);
					domainModel.put(property);
					property = null;
				}
			}
			//添加action对象
			//添加子模型
			List<Specification> children = spec.findChildren(Specification.class);
			if (CollectionUtils.isNotEmpty(children)) {
				children.forEach(childSpec -> {
					List<ConnectionSpecEO> connections = spec.getConnection(childSpec.getId());
					if (CollectionUtils.isNotEmpty(connections)) {
						ConnectionSpecEO connectionSpecEO = connections.get(0);
						Integer loadType1 = connectionSpecEO.getLoadType();
						int buiderCount;
						if (loadType1 == null) {
							loadType1 = 0;
						}
						if (loadType1 == 0) {
							Integer buildNumber = connectionSpecEO.getBuildNumber();
							Integer minmiun = connectionSpecEO.getMinmiun();
							if (buildNumber == null) {
								buildNumber = 0;
							}
							if (minmiun == null) {
								minmiun = 0;
							}
							buiderCount = buildNumber >= minmiun ? buildNumber : minmiun;
							String code = domainModel.getEntity().getCode();
							List<Map<String, Object>> values = null;
							if (entity != null && entity.containsKey(code)) {
								Object value = entity.get(code);
								if (value instanceof Map) {
									values.add((Map) value);
								}
								if (value instanceof List) {
									values = (List) value;
								}
							}
							for (int i = 0; i < buiderCount; i++) {
								Map<String, Object> value = null;
								if (CollectionUtils.isNotEmpty(values) && values.size() > i) {
									value = values.get(i);
								}
								this.createDomainModel(md, domainModel, childSpec, null, value);
							}
						}
					}
				});
			}
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
