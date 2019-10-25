package fun.vyse.cloud.define.service;


import fun.vyse.cloud.define.domain.DomainModel;

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
	DomainModel createDomainModel(Long modelId);


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
