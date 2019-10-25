package fun.vyse.cloud.define.service.impl;

import fun.vyse.cloud.core.constant.EntityState;
import fun.vyse.cloud.core.domain.IFixedEntity;
import fun.vyse.cloud.define.domain.DomainModel;
import fun.vyse.cloud.define.domain.MetaDefinition;
import fun.vyse.cloud.define.entity.FixedModelEO;
import fun.vyse.cloud.define.entity.ModelDataEO;
import fun.vyse.cloud.define.entity.ModelEO;
import fun.vyse.cloud.define.service.IDomainModelService;
import fun.vyse.cloud.define.service.IMetaDefinitionService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DomainModelServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-25 11:37
 */
public class DomainModelServiceImpl implements IDomainModelService {

	@Autowired
	private IMetaDefinitionService metaDefinitionService;

	/**
	 * 创建领域模型
	 *
	 * @param modelId
	 * @return
	 */
	@Override
	public DomainModel createDomainModel(Long modelId) {
		MetaDefinition<Long> md = metaDefinitionService.getMetaDefinition("");
		ModelEO modelEO = md.getModel(modelId);
		if(modelEO!=null){
			ModelDataEO dataEO = new ModelDataEO();
			dataEO.setDomainId(modelId);
			dataEO.setCode(modelEO.getCode());
			dataEO.updateDirtyFlag(EntityState.New);
			Long fixedId = modelEO.getFixedId();
			DomainModel domainModel = new DomainModel(dataEO, md);
			if(ObjectUtils.isNotEmpty(fixedId)){
				dataEO.setFixedId(modelEO.getFixedId());
				FixedModelEO fixedModelEO = md.getFixedModelEO(1L);
				Object fixedInstance = fixedModelEO.createFixedInstance();
				domainModel.setFixedModel((IFixedEntity) fixedInstance);
			}
			//添加proptyer默认值
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
