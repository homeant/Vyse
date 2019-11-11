package fun.vyse.cloud.base.service.impl;

import fun.vyse.cloud.base.entity.GeneratorEO;
import fun.vyse.cloud.base.repository.IGeneratorRepository;
import fun.vyse.cloud.core.constant.EntityState;
import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import fun.vyse.cloud.core.domain.IEntity;
import fun.vyse.cloud.core.domain.IFixedEntity;
import fun.vyse.cloud.core.domain.ITenantEntity;
import fun.vyse.cloud.base.repository.IBaseRepository;
import fun.vyse.cloud.base.service.IBaseService;
import fun.vyse.cloud.core.util.ClassUtils;
import net.sf.cglib.core.ReflectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * BaseServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 12:22
 */
public class BaseServiceImpl<T extends IEntity, ID, M extends IBaseRepository<T, ID>> implements IBaseService<T, ID> {

	@Autowired
	public M baseRepository;

	@Autowired
	private IGeneratorRepository generatorRepository;

	@Override
	@Transactional
	public ID get() {
		Class clz = ClassUtils.getGenericsType(this.getClass());
		String className = clz.getSimpleName();
		GeneratorEO generatorEO = generatorRepository.findOne((root, criteriaQuery, criteriaBuilder) ->
				criteriaBuilder.equal(root.get(GeneratorEO.SEQ_NAME), className)).orElse(null);
		ID value = null;
		if (generatorEO != null) {
			value = (ID) generatorEO.getValue();
			long newValue = generatorEO.getValue() + generatorEO.getIncrement();
			generatorEO.setValue(newValue);
			generatorRepository.flush();
		}
		return value;
	}

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
		if(entity.getId()== null){
			BaseServiceImpl baseService = (BaseServiceImpl)AopContext.currentProxy();
			ID id = (ID)baseService.get();
			entity.setId(id);
		}
		return baseRepository.saveAndFlush(entity);
	}

	@Override
	public <T extends ITenantEntity> List<T> findListByTenantId(String tenantId) {
		return (List<T>) this.baseRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
			if (StringUtils.isNotBlank(tenantId)) {
				return criteriaBuilder.equal(root.get(AbstractBaseEntity.TENANT_ID), tenantId);
			}
			return criteriaBuilder.isNull(root.get(AbstractBaseEntity.TENANT_ID));
		});
	}

	/**
	 * 创建新的模型对象
	 *
	 * @return 模型对象
	 */
	@Override
	@Transactional
	public <T extends IFixedEntity> T newActual() {
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();
			IFixedEntity actual = (IFixedEntity) ReflectUtils.newInstance((Class) actualTypes[0]);
			actual.setState$(EntityState.New);
			actual.setDirtyFlag(EntityState.New);
			BaseServiceImpl baseService = (BaseServiceImpl)AopContext.currentProxy();
			ID id = (ID)baseService.get();
			actual.setId(id);
			int a = 1/0;
			return (T) actual;
		}
		return null;
	}
}
