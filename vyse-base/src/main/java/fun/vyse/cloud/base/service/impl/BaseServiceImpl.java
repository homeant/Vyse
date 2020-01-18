package fun.vyse.cloud.base.service.impl;

import fun.vyse.cloud.base.repository.IBaseRepository;
import fun.vyse.cloud.base.service.IBaseService;
import fun.vyse.cloud.core.domain.IEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * BaseServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 12:22
 */
public class BaseServiceImpl<T extends IEntity, M extends IBaseRepository<T>> implements IBaseService<T> {

	@Autowired
	public M baseRepository;

	@Override
	public T getOne(Integer id) {
		return baseRepository.findById(id).orElse(null);
	}

	@Override
	public Page<T> getPage(Pageable pageable) {
		return baseRepository.findAll(pageable);
	}

	@Override
	public <S extends T> Page<S> getPage(Pageable pageable, Example<S> example) {
		return baseRepository.findAll(example, pageable);
	}

	@Override
	public <S extends T> List<S> getAll(Example<S> example, Sort sort) {
		return baseRepository.findAll(example, sort);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public <S extends T> S save(S bean) {
		return baseRepository.save(bean);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public <S extends T> S saveAndFlush(S bean) {
		return baseRepository.saveAndFlush(bean);
	}

	@Override
	public Long count() {
		return baseRepository.count();
	}

	@Override
	public <S extends T> Long count(Example<S> example) {
		return baseRepository.count(example);
	}

	@Override
	public void remove(T bean) {
		baseRepository.delete(bean);
	}

	@Override
	public void remove(Integer id) {
		baseRepository.deleteById(id);
	}
}
