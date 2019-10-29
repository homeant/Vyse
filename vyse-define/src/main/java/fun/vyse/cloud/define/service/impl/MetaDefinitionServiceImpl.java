package fun.vyse.cloud.define.service.impl;

import fun.vyse.cloud.define.domain.MetaDefinition;
import fun.vyse.cloud.define.entity.ConnectionEO;
import fun.vyse.cloud.define.entity.FixedModelEO;
import fun.vyse.cloud.define.entity.ModelEO;
import fun.vyse.cloud.define.entity.PropertyEO;
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
	private IModelService modelService;

	@Autowired
	private IPropertyService propertyService;

	@Autowired
	private IConnectionService connectionService;

	@Autowired
	private IFixedModelService fixedModelService;

	/**
	 * 根据租户id查询定义
	 *
	 * @param tenantId
	 * @return
	 */
	@Override
	public MetaDefinition<Long> getMetaDefinition(String tenantId) {
		List<ModelEO> modelEOS = this.modelService.findListByTenantId(tenantId);
		List<PropertyEO> propertyEOS = propertyService.findListByTenantId(tenantId);
		List<ConnectionEO> connectionEOS = connectionService.findListByTenantId(tenantId);
		List<FixedModelEO> FixedModelEOS = fixedModelService.findListByTenantId(tenantId);
		MetaDefinition<Long> metaDefinition = new MetaDefinition<>();
		modelEOS.forEach(r -> metaDefinition.addModel(r));
		propertyEOS.forEach(r -> metaDefinition.addProperty(r));
		connectionEOS.forEach(r -> metaDefinition.addConcurrent(r));
		FixedModelEOS.forEach(r->metaDefinition.addFixedModel(r));
		metaDefinition.init();
		return metaDefinition;
	}
}
