package fun.vyse.cloud.model.entity;

import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;

/**
 * PropertySpec
 *
 * @author junchen
 * @date 2020-01-17 23:07
 */
@Data
@Entity(name = "spec_property")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class PropertySpec extends AbstractBaseEntity {
	private String name;

	private String code;
}
