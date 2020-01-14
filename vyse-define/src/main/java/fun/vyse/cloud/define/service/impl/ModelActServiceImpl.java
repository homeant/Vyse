package fun.vyse.cloud.define.service.impl;

import fun.vyse.cloud.core.constant.EntityState;
import fun.vyse.cloud.base.service.impl.BaseServiceImpl;
import fun.vyse.cloud.define.entity.actual.ModelActEO;
import fun.vyse.cloud.define.repository.IModelActRepository;
import fun.vyse.cloud.define.service.IModelActService;

/**
 * ModelDataServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 19:01
 */
public class ModelActServiceImpl extends BaseServiceImpl<ModelActEO,Long, IModelActRepository> implements IModelActService {
	/**
	 * 创建业务模型数据对象
	 *
	 * @param id
	 * @return
	 */
	@Override
	public ModelActEO createModel(Long id) {
		ModelActEO modelActEO = new ModelActEO();
		modelActEO.setDirtyFlag(EntityState.New);
		modelActEO.setState$(EntityState.New);
		return modelActEO;
	}
}
