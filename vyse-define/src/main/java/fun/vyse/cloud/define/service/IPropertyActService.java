package fun.vyse.cloud.define.service;

import fun.vyse.cloud.core.service.IBaseService;
import fun.vyse.cloud.define.entity.actual.PropertyActEO;

/**
 * IModelPropertyService
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 19:00
 */
public interface IPropertyActService extends IBaseService<PropertyActEO,Long> {

	PropertyActEO createProperty(Long id);
}
