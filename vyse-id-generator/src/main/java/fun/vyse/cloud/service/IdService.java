package fun.vyse.cloud.service;

import fun.vyse.cloud.domain.Id;

import java.util.Date;

/**
 * IdService
 *
 * @author junchen
 * @date 2019-12-13 22:17
 */
public interface IdService {
	/**
	 * 获取ID
	 * @return
	 */
	long genId();

	/**
	 * 解析ID
	 * @param id
	 * @return
	 */
	Id expId(long id);

	long makeId(long time, long seq);

	long makeId(long time, long seq, long machine);

	long makeId(long genMethod, long time, long seq, long machine);

	long makeId(long type, long genMethod, long time,
				long seq, long machine);

	long makeId(long version, long type, long genMethod,
				long time, long seq, long machine);

	Date transTime(long time);

	void init();
}
