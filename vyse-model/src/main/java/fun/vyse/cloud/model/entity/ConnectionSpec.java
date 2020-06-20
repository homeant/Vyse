package fun.vyse.cloud.model.entity;

import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.List;

/**
 * ConnectSpec
 *
 * @author junchen
 * @date 2020-01-17 23:12
 */
@Data
@Builder
@Entity(name = "spec_connection")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class ConnectionSpec extends AbstractBaseEntity {
	private Integer parentId;

	@Column(length = 32)
	private String parentType;

	private Integer subId;

	@Column(length = 32)
	private String subType;

	@Transient
	private List<ConnectionSpec> connections;
}
