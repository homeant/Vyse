package fun.vyse.cloud.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;


/**
 * IRepository 仓库接口层
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-23 13:14
 */
@NoRepositoryBean
public interface IBaseRepository<T> extends JpaRepository<T,Integer>, JpaSpecificationExecutor<T> {
}
