package fun.vyse.cloud.define.service;

import fun.vyse.cloud.define.domain.DomainEntity;

/**
 * IDomainEntityService
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-25 11:54
 */
public interface IDomainEntityService {
	/**
	 * 创建领域对象
	 * @param id
	 * @return
	 */
	DomainEntity createEntity(Long id);

	/**
	 * 保存领域对象
	 * @param entity
	 * @return
	 */
	DomainEntity saveEntity(DomainEntity entity);

	/**
	 * 删除领域对象
	 * @param entity
	 * @return
	 */
	boolean removeEntity(DomainEntity entity);
}
