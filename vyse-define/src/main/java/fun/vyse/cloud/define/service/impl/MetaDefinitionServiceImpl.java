package fun.vyse.cloud.define.service.impl;

import fun.vyse.cloud.define.domain.MetaDefinition;
import fun.vyse.cloud.define.entity.specification.SpecConnectionEO;
import fun.vyse.cloud.define.entity.specification.SpecFixedModelEO;
import fun.vyse.cloud.define.entity.specification.SpecModelEO;
import fun.vyse.cloud.define.entity.specification.SpecPropertyEO;
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
	private ISpecModelService specModelService;

	@Autowired
	private ISpecPropertyService specPropertyService;

	@Autowired
	private ISpecConnectionService specConnectionService;

	@Autowired
	private ISpecFixedModelService specFixedModelService;

	/**
	 * 根据租户id查询定义
	 *
	 * @param tenantId
	 * @return
	 */
	@Override
	public MetaDefinition<Long> getMetaDefinition(String tenantId) {
		List<SpecModelEO> modelEOS = this.specModelService.findListByTenantId(tenantId);
		List<SpecPropertyEO> propertyEOS = specPropertyService.findListByTenantId(tenantId);
		List<SpecConnectionEO> connectionEOS = specConnectionService.findListByTenantId(tenantId);
		List<SpecFixedModelEO> FixedModelEOS = specFixedModelService.findListByTenantId(tenantId);
		MetaDefinition<Long> metaDefinition = new MetaDefinition<>();
		modelEOS.forEach(r -> metaDefinition.addModel(r));
		propertyEOS.forEach(r -> metaDefinition.addProperty(r));
		connectionEOS.forEach(r -> metaDefinition.addConcurrent(r));
		FixedModelEOS.forEach(r->metaDefinition.addFixedModel(r));
		metaDefinition.init();
		return metaDefinition;
	}
}
