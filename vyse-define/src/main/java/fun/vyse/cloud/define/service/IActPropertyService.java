package fun.vyse.cloud.define.service;

import fun.vyse.cloud.core.service.IBaseService;
import fun.vyse.cloud.define.entity.actual.ActPropertyEO;

/**
 * IModelPropertyService
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 19:00
 */
public interface IActPropertyService extends IBaseService<ActPropertyEO,Long> {

	ActPropertyEO createProperty(Long id);
}
