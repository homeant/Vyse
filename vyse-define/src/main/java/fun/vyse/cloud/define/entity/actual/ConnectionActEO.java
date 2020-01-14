package fun.vyse.cloud.define.entity.actual;

import fun.vyse.cloud.core.domain.IRecursiveEntity;
import fun.vyse.cloud.core.domain.InternalFixedEO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * ConnectionActEO
 *
 * @author junchen homeanter@163.com
 * @date 2019-11-04 20:09
 */
@Entity
@Table(name = "act_connection")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ConnectionActEO extends InternalFixedEO<Long> implements IRecursiveEntity<Long> {
	private Long parentId;
}
