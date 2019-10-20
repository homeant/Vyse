package fun.vyse.cloud.core.domain;

import fun.vyse.cloud.core.constant.EntityState;
import lombok.Data;

/**
 * AbstractStateEntity
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-18 15:01
 */
@Data
public abstract class AbstractStateEntity implements IStateEntity {

	private EntityState state$;
}
