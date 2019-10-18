package fun.vyse.cloud.core.domain;

import fun.vyse.cloud.core.constant.EntityState;
import lombok.Data;

/**
 * AbstractStateEntity
 *
 * @Author junchen homeanter@163.com
 * @Date 2019-10-18 15:01
 */
@Data
public abstract class AbstractStateEntity implements IStateEntity {

	private EntityState state$;
}
