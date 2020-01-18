package fun.vyse.cloud.model.entity;

import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import lombok.*;

import javax.persistence.Entity;

/**
 * PropertySpec
 *
 * @author junchen
 * @date 2020-01-17 23:07
 */
@Data
@Builder
@Entity(name = "spec_property")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class PropertySpec extends AbstractBaseEntity {
	private String name;

	private String code;

	private String dataType;
}
