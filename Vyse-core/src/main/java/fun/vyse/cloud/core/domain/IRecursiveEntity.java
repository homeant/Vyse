package fun.vyse.cloud.core.domain;

/**
 * IRecursiveEntity
 *	递归接口
 * @author junchen homeanter@163.com
 * @date 2019-10-19 11:53
 */
public interface IRecursiveEntity<T> {
	T getParentId();

	void setParentId(T parentId);
}
