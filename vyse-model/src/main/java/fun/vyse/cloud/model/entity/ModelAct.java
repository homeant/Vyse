package fun.vyse.cloud.model.entity;

import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import lombok.*;

import javax.persistence.Entity;

/**
 * ModelAct
 *
 * @author junchen
 * @date 2020-01-18 18:18
 */

@Data
@Builder
@Entity(name="act_model")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ModelAct extends AbstractBaseEntity {
	private String code;

	private String name;
}
