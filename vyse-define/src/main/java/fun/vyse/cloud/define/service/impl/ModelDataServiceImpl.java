package fun.vyse.cloud.define.service.impl;

import fun.vyse.cloud.core.service.impl.BaseServiceImpl;
import fun.vyse.cloud.define.entity.ModelDataEO;
import fun.vyse.cloud.define.repository.IModelDataRepository;
import fun.vyse.cloud.define.service.IModelDataService;
import org.springframework.stereotype.Service;

/**
 * ModelDataServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 19:01
 */
@Service
public class ModelDataServiceImpl extends BaseServiceImpl<ModelDataEO,Long, IModelDataRepository> implements IModelDataService {
}
