package fun.vyse.cloud.base.service;

import fun.vyse.cloud.core.domain.IEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * IService
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 12:21
 */
public interface IBaseService<T extends IEntity> {
	/**
	 * 获取一条数据
	 * @param id
	 * @return
	 */
	T getOne(Integer id);

	/**
	 * 分页
	 * @param pageable
	 * @return
	 */
	Page<T> getPage(Pageable pageable);

	/**
	 * 分页
	 * @param pageable
	 * @param example
	 * @param <S>
	 * @return
	 */
	<S extends T> Page<S> getPage(Pageable pageable,Example<S> example);

	/**
	 * 获取所有
	 * @param example
	 * @param sort
	 * @param <S>
	 * @return
	 */
	<S extends T> List<S> getAll(Example<S> example,Sort sort);

	/**
	 * 保存
	 * @param bean
	 * @param <S>
	 * @return
	 */
	<S extends T> S save(S bean);

	/**
	 * 保存并里面更新到DB
	 * @param bean
	 * @param <S>
	 * @return
	 */
	<S extends T> S saveAndFlush(S bean);

	/**
	 * 获取条数
	 * @return
	 */
	Long count();

	/**
	 * 获取条数
	 * @param example
	 * @param <S>
	 * @return
	 */
	<S extends T> Long count(Example<S> example);

	/**
	 * 删除
	 * @param bean
	 */
	void remove(T bean);

	/**
	 * 删除
	 * @param id
	 */
	void remove(Integer id);

}
