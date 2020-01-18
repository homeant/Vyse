package fun.vyse.cloud.core.hibernate;

import fun.vyse.cloud.core.domain.ITimestampEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * AuditingEntityListener
 *
 * @author junchen
 * @date 2020-01-18 16:57
 */
public class AuditingEntityListener {
	@PrePersist
	public void prePersist(Object bean) {
		if (bean instanceof ITimestampEntity) {
			((ITimestampEntity) bean).setCreatedTime(System.currentTimeMillis());
		}
	}

	@PreUpdate
	public void preUpdate(Object bean) {
		if (bean instanceof ITimestampEntity) {
			((ITimestampEntity) bean).setUpdatedTime(System.currentTimeMillis());
		}
	}
}
