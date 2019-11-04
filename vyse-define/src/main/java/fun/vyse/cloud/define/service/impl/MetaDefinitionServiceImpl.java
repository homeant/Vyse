package fun.vyse.cloud.define.service.impl;

import fun.vyse.cloud.define.domain.MetaDefinition;
import fun.vyse.cloud.define.entity.specification.ConnectionSpecEO;
import fun.vyse.cloud.define.entity.specification.FixedModelSpecEO;
import fun.vyse.cloud.define.entity.specification.ModelSpecEO;
import fun.vyse.cloud.define.entity.specification.PropertySpecEO;
import fun.vyse.cloud.define.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * MetaDefinitionServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 16:20
 */
public class MetaDefinitionServiceImpl implements IMetaDefinitionService {

	@Autowired
	private IModelSpecService specModelService;

	@Autowired
	private IPropertySpecService specPropertyService;

	@Autowired
	private IConnectionSpecService specConnectionService;

	@Autowired
	private IFixedModelSpecService specFixedModelService;

	/**
	 * 根据租户id查询定义
	 *
	 * @param tenantId
	 * @return
	 */
	@Override
	public MetaDefinition<Long> getMetaDefinition(String tenantId) {
		List<ModelSpecEO> modelEOS = this.specModelService.findListByTenantId(tenantId);
		List<PropertySpecEO> propertyEOS = specPropertyService.findListByTenantId(tenantId);
		List<ConnectionSpecEO> connectionEOS = specConnectionService.findListByTenantId(tenantId);
		List<FixedModelSpecEO> FixedModelEOS = specFixedModelService.findListByTenantId(tenantId);
		MetaDefinition<Long> metaDefinition = new MetaDefinition<>();
		modelEOS.forEach(r -> metaDefinition.addModel(r));
		propertyEOS.forEach(r -> metaDefinition.addProperty(r));
		connectionEOS.forEach(r -> metaDefinition.addConcurrent(r));
		FixedModelEOS.forEach(r->metaDefinition.addFixedModel(r));
		metaDefinition.init();
		return metaDefinition;
	}
}
