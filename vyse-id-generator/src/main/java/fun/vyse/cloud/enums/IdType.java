package fun.vyse.cloud.enums;

/**
 * IdType
 *
 * @author junchen
 * @date 2019-12-14 11:42
 */
public enum IdType {
	/**
	 * 最大峰值
	 */
	MAX_PEAK("max-peak"),
	/**
	 * 最小粒度
	 */
	MIN_GRANULARITY("min-granularity");

	private String name;

	private IdType(String name) {
		this.name = name;
	}

	public long value() {
		switch (this) {
			case MIN_GRANULARITY:
				return 1;
			default:
				return 0;
		}
	}

	public static IdType parse(String name) {
		if (IdType.MIN_GRANULARITY.name.equals(name)) {
			return MIN_GRANULARITY;
		} else if (IdType.MAX_PEAK.name.equals(name)) {
			return MAX_PEAK;
		}
		return null;
	}

	public static IdType parse(long type) {
		if (type == 1) {
			return MIN_GRANULARITY;
		} else if (type == 0) {
			return MAX_PEAK;
		}
		return null;
	}
}
