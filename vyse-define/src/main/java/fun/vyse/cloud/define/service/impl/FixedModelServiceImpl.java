package fun.vyse.cloud.define.service.impl;

import fun.vyse.cloud.core.service.impl.BaseServiceImpl;
import fun.vyse.cloud.define.entity.FixedModelEO;
import fun.vyse.cloud.define.repository.IFixedModelRepository;
import fun.vyse.cloud.define.service.IFixedModelService;
import org.springframework.stereotype.Service;

/**
 * FixedModeServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 17:09
 */
@Service
public class FixedModelServiceImpl extends BaseServiceImpl<FixedModelEO,Long, IFixedModelRepository> implements IFixedModelService {
}
