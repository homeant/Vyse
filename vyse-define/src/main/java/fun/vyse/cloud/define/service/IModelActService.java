package fun.vyse.cloud.define.service;

import fun.vyse.cloud.base.service.IBaseService;
import fun.vyse.cloud.define.entity.actual.ModelActEO;

/**
 * IModelDataService
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 18:59
 */
public interface IModelActService extends IBaseService<ModelActEO,Long> {
	/**
	 * 创建业务模型数据对象
	 * @param id
	 * @return
	 */
	ModelActEO createModel(Long id);
}
