package fun.vyse.cloud.converter;

import fun.vyse.cloud.domain.Id;

/**
 * IdConverter
 *
 * @author junchen
 * @date 2019-12-14 11:54
 */
public interface IdConverter {
	public long convert(Id id);

	public Id convert(long id);
}
