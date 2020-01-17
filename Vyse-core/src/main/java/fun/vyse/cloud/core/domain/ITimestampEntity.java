package fun.vyse.cloud.core.domain;

/**
 * ITimestampEntity
 *
 * @author junchen
 * @date 2020-01-17 22:41
 */
public interface ITimestampEntity {
	Long getUpdatedTime();

	void setUpdatedTime(Long updatedTime);

	Long getCreatedTime();

	void setCreatedTime(Long createdTime);

	Long getDeletedTime();

	void setDeletedTime(Long deletedTime);
}
