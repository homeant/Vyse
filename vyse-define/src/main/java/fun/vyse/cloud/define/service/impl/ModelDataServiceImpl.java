package fun.vyse.cloud.define.service.impl;

import fun.vyse.cloud.core.constant.EntityState;
import fun.vyse.cloud.core.service.impl.BaseServiceImpl;
import fun.vyse.cloud.define.entity.ModelDataEO;
import fun.vyse.cloud.define.repository.IModelDataRepository;
import fun.vyse.cloud.define.service.IModelDataService;

/**
 * ModelDataServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 19:01
 */
public class ModelDataServiceImpl extends BaseServiceImpl<ModelDataEO,Long, IModelDataRepository> implements IModelDataService {
	/**
	 * 创建业务模型数据对象
	 *
	 * @param id
	 * @return
	 */
	@Override
	public ModelDataEO createModelData(Long id) {
		ModelDataEO modelDataEO = new ModelDataEO();
		modelDataEO.setDirtyFlag(EntityState.New);
		modelDataEO.setState$(EntityState.New);
		return modelDataEO;
	}
}
