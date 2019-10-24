package fun.vyse.cloud.define.service.impl;

import fun.vyse.cloud.core.service.impl.BaseServiceImpl;
import fun.vyse.cloud.define.entity.ModelPropertyEO;
import fun.vyse.cloud.define.repository.IModelPropertyRepository;
import fun.vyse.cloud.define.service.IModelPropertyService;
import org.springframework.stereotype.Service;

/**
 * ModelPropertyServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 19:02
 */
@Service
public class ModelPropertyServiceImpl extends BaseServiceImpl<ModelPropertyEO,Long, IModelPropertyRepository> implements IModelPropertyService {
}
