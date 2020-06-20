package fun.vyse.cloud.model.service;

import fun.vyse.cloud.base.service.IBaseService;
import fun.vyse.cloud.model.constant.ConnectionType;
import fun.vyse.cloud.model.entity.ConnectionSpec;

import java.util.List;

/**
 * ConnectionSpecService
 *
 * @author junchen
 * @date 2020-01-17 23:58
 */
public interface ConnectionSpecService extends IBaseService<ConnectionSpec> {
	/**
	 * 获取连接
	 * @param parentId
	 * @param connectionType
	 * @return
	 */
	List<ConnectionSpec> getConnection(Integer parentId, ConnectionType connectionType);
}
