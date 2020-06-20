package fun.vyse.cloud.model.service.impl;

import fun.vyse.cloud.base.service.impl.BaseServiceImpl;
import fun.vyse.cloud.model.constant.ConnectionType;
import fun.vyse.cloud.model.entity.ConnectionSpec;
import fun.vyse.cloud.model.repository.ConnectionSpecRepository;
import fun.vyse.cloud.model.service.ConnectionSpecService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * ConnectionSpecServiceImpl
 *
 * @author junchen
 * @date 2020-01-17 23:59
 */
public class ConnectionSpecServiceImpl extends BaseServiceImpl<ConnectionSpec, ConnectionSpecRepository> implements ConnectionSpecService {
	/**
	 * 获取连接
	 *
	 * @param parentId
	 * @param connectionType
	 * @return
	 */
	@Override
	public List<ConnectionSpec> getConnection(Integer parentId, ConnectionType connectionType) {
		return baseRepository.findAll((root, criteriaQuery, cb) -> {
			Predicate predicate = cb.equal(root.get("parentId"), parentId);
			Predicate parentType = cb.equal(root.get("parentType"), connectionType.name());
			return cb.and(predicate, parentType);
		});
	}
}

