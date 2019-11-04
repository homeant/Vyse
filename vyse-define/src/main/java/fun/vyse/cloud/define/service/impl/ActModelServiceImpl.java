package fun.vyse.cloud.define.service.impl;

import fun.vyse.cloud.core.constant.EntityState;
import fun.vyse.cloud.core.service.impl.BaseServiceImpl;
import fun.vyse.cloud.define.entity.actual.ActModelEO;
import fun.vyse.cloud.define.repository.IActModelRepository;
import fun.vyse.cloud.define.service.IActModelService;

/**
 * ModelDataServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 19:01
 */
public class ActModelServiceImpl extends BaseServiceImpl<ActModelEO,Long, IActModelRepository> implements IActModelService {
	/**
	 * 创建业务模型数据对象
	 *
	 * @param id
	 * @return
	 */
	@Override
	public ActModelEO createModel(Long id) {
		ActModelEO modelDataEO = new ActModelEO();
		modelDataEO.setDirtyFlag(EntityState.New);
		modelDataEO.setState$(EntityState.New);
		return modelDataEO;
	}
}
