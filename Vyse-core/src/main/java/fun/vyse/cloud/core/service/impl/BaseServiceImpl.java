package fun.vyse.cloud.core.service.impl;

import fun.vyse.cloud.core.repository.IBaseRepository;
import fun.vyse.cloud.core.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * BaseServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 12:22
 */
@Service
public class BaseServiceImpl<T,ID> implements IBaseService<T,ID> {
	@Autowired
	public IBaseRepository<T,ID> baseRepository;
}
