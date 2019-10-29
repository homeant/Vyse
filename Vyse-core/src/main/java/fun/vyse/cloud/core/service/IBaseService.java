package fun.vyse.cloud.core.service;

import fun.vyse.cloud.core.domain.IFixedEntity;
import fun.vyse.cloud.core.domain.ITenantEntity;
import org.springframework.data.domain.Example;

import java.util.List;

/**
 * IService
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 12:21
 */
public interface IBaseService<T extends IFixedEntity,ID> {
	T findOne(ID id);

	<S extends T> List<S> findList(Example<S> example);

	<S extends T> S saveAndFlush(S entity);

	<T extends ITenantEntity> List<T> findListByTenantId(String tenantId);

	/**
	 * 创建新的模型对象
	 * @param id id
	 * @return 模型对象
	 */
	T newActual(ID id);
}
