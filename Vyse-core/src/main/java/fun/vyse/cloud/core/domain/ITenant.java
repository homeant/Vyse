package fun.vyse.cloud.core.domain;

/**
 * @author junchen
 * @Description 租户接口类
 * @date 2019-10-13 09:49
 */
public interface ITenant extends IEntity{
	String getTenantId();

	void setTenantId(String id);
}
