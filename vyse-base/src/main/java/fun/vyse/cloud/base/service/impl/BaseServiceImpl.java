package fun.vyse.cloud.base.service.impl;

import fun.vyse.cloud.base.repository.IBaseRepository;
import fun.vyse.cloud.base.service.IBaseService;
import fun.vyse.cloud.core.domain.IEntity;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * BaseServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 12:22
 */
public class BaseServiceImpl<T extends IEntity, ID, M extends IBaseRepository<T, ID>> implements IBaseService<T,ID> {

	@Autowired
	public M baseRepository;

}
