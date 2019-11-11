package fun.vyse.cloud.base.service;

import fun.vyse.cloud.base.entity.GeneratorEO;

/**
 * IGeneratorService
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-30 11:34
 */
public interface IGeneratorService extends IBaseService<GeneratorEO,Long> {
	/**
	 * 获取值
	 * @param seqName
	 * @param tenantId
	 * @return
	 */
	Object get(String seqName,String tenantId);
}
