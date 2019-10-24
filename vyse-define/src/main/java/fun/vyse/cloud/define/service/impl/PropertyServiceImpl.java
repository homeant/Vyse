package fun.vyse.cloud.define.service.impl;

import fun.vyse.cloud.core.service.impl.BaseServiceImpl;
import fun.vyse.cloud.define.entity.PropertyEO;
import fun.vyse.cloud.define.repository.IPropertyRepository;
import fun.vyse.cloud.define.service.IPropertyService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PropertyServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 12:40
 */
@Service
public class PropertyServiceImpl extends BaseServiceImpl<PropertyEO,Long,IPropertyRepository> implements IPropertyService {

}
