package fun.vyse.cloud.define.service;

import fun.vyse.cloud.core.service.IBaseService;
import fun.vyse.cloud.define.entity.ModelDataEO;

/**
 * IModelDataService
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 18:59
 */
public interface IModelDataService extends IBaseService<ModelDataEO,Long> {
	/**
	 * 创建业务模型数据对象
	 * @param id
	 * @return
	 */
	ModelDataEO createModelData(Long id);
}
