package fun.vyse.cloud.define.service;


import fun.vyse.cloud.define.domain.DomainModel;
import fun.vyse.cloud.define.domain.MetaDefinition;
import fun.vyse.cloud.define.domain.Model;

import java.util.Map;

/**
 * IDomainModelService
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-25 11:36
 */
public interface IDomainModelService {

	/**
	 * 创建领域模型
	 * @param modelId
	 * @return
	 */
	DomainModel createDomainModel(Long modelId, Map<String,Object> entity);

	/**
	 * 创建领域模型
	 * @param md
	 * @param parent
	 * @param model
	 * @param entity
	 * @return
	 */
	DomainModel createDomainModel(MetaDefinition<Long> md, DomainModel parent, Model model,Long id,Map<String,Object> entity);


	/**
	 * 更新领域模型
	 * @param model
	 * @return
	 */
	DomainModel updateDomainModel(DomainModel model);

	/**
	 * 删除领域模型
	 * @param model
	 * @return
	 */
	boolean removeDomainModel(DomainModel model);
}
