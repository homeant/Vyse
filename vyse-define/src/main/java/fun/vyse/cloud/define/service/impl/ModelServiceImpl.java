package fun.vyse.cloud.define.service.impl;

import fun.vyse.cloud.core.service.impl.BaseServiceImpl;
import fun.vyse.cloud.define.entity.ModelEO;
import fun.vyse.cloud.define.service.IModelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ModelServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 11:09
 */
@Service
@Transactional
public class ModelServiceImpl extends BaseServiceImpl<ModelEO,Long> implements IModelService {

	@Override
	public ModelEO get(Long id) {
		return this.baseRepository.findById(id).orElse(null);
	}
}