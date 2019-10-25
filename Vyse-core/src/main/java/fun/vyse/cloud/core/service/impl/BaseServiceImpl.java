package fun.vyse.cloud.core.service.impl;

import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import fun.vyse.cloud.core.domain.ITenantEntity;
import fun.vyse.cloud.core.repository.IBaseRepository;
import fun.vyse.cloud.core.service.IBaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * BaseServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 12:22
 */
public class BaseServiceImpl<T,ID,M extends IBaseRepository<T,ID>> implements IBaseService<T,ID> {

	@Autowired
	public M baseRepository;

	@Override
	public T findOne(ID id) {
		return baseRepository.findById(id).orElse(null);
	}

	@Override
	public <S extends T> List<S> findList(Example<S> example) {
		return baseRepository.findAll(example);
	}


	@Transactional
	@Override
	public <S extends T> S saveAndFlush(S entity) {
		return baseRepository.saveAndFlush(entity);
	}

	@Override
	public <T extends ITenantEntity> List<T> findListByTenantId(String tenantId) {
		return (List<T>) this.baseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
						if(StringUtils.isNotBlank(tenantId)){
				return criteriaBuilder.equal(root.get(AbstractBaseEntity.TENANT_ID),tenantId);
			}
			return criteriaBuilder.isNull(root.get(AbstractBaseEntity.TENANT_ID));
		});
	}
}
