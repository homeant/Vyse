package fun.vyse.cloud.base.service;

import fun.vyse.cloud.core.domain.IEntity;
import fun.vyse.cloud.core.domain.IFixedEntity;
import fun.vyse.cloud.core.domain.ITenantEntity;
import org.springframework.data.domain.Example;

import java.io.Serializable;
import java.util.List;

/**
 * IService
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 12:21
 */
public interface IBaseService<T extends IEntity,ID> {

	ID get();

	T findOne(ID id);

	<S extends T> List<S> findList(Example<S> example);

	<S extends T> S saveAndFlush(S entity);

	<T extends ITenantEntity> List<T> findListByTenantId(String tenantId);

	/**
	 * 创建新的模型对象
	 * @return 模型对象
	 */
	<T extends IFixedEntity> T newActual();
}
