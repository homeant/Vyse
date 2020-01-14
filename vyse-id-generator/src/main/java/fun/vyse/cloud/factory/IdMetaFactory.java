package fun.vyse.cloud.factory;

import fun.vyse.cloud.domain.IdMeta;
import fun.vyse.cloud.enums.IdType;

/**
 * IdMetaFactory
 *
 * @author junchen
 * @date 2019-12-14 12:02
 */
public class IdMetaFactory {
	/**
	 * machineBits seqBits timeBits genMethodBits typeBits versionBits
	 */
	private static IdMeta maxPeak = new IdMeta((byte) 10, (byte) 20, (byte) 30, (byte) 2, (byte) 1, (byte) 1);

	private static IdMeta minGranularity = new IdMeta((byte) 10, (byte) 10, (byte) 40, (byte) 2, (byte) 1, (byte) 1);

	public static IdMeta getIdMeta(IdType type) {
		if (IdType.MAX_PEAK.equals(type)) {
			return maxPeak;
		} else if (IdType.MIN_GRANULARITY.equals(type)) {
			return minGranularity;
		}
		return null;
	}
}
