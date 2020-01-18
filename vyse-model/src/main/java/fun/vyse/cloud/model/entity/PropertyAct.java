package fun.vyse.cloud.model.entity;

import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * PropertySpec
 *
 * @author junchen
 * @date 2020-01-17 23:07
 */
@Data
@Builder
@Entity(name = "act_property")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class PropertyAct extends AbstractBaseEntity {
	@Column(name = "id_1")
	private Integer id1;

	@Column(name = "value_1",length = 100)
	private String value1;

	@Column(name = "id_2")
	private Integer id2;

	@Column(name = "value_2",length = 100)
	private String value2;
}
