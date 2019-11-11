package fun.vyse.cloud.core.domain;

import fun.vyse.cloud.core.constant.EntityState;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Transient;

/**
 * AbstractStateEntity
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-18 15:01
 */
@Data
@ToString
public abstract class AbstractStateEntity implements IStateEntity {
	@Transient
	private EntityState state$;

	public final static String STATE$ = "state$";

	public void setState$(EntityState state$) {
		if (state$ == null) {
			this.state$ = EntityState.None;
		} else {
			this.state$ = state$;
		}

	}

	public void updateState$(EntityState state$) {
		EntityState state = this.getState$();
		if (EntityState.New != state || EntityState.Modify != state$) {
			this.setState$(state$);
		}
	}
}
