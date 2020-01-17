package fun.vyse.cloud.model.entity;

import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;

/**
 * Model
 *
 * @author junchen
 * @date 2020-01-17 22:28
 */
@Data
@Entity(name = "spec_model")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ModelSpec extends AbstractBaseEntity {
	private String name;

	private String code;
}
