package fun.vyse.cloud.produce;

import fun.vyse.cloud.domain.Id;
import fun.vyse.cloud.domain.IdMeta;
import fun.vyse.cloud.enums.IdType;
import fun.vyse.cloud.util.TimeUtils;

/**
 * BaseProduce
 *
 * @author junchen
 * @date 2019-12-14 13:01
 */
public abstract class AbstractProduce implements IdProduce,ResetProduce {

	protected long sequence = 0;
	protected long lastTimestamp = -1;

	@Override
	public void produce(Id id, IdMeta meta) {
		long timestamp = TimeUtils.genTime(IdType.parse(id.getType()));
		TimeUtils.validateTimestamp(lastTimestamp, timestamp);

		if (timestamp == lastTimestamp) {
			sequence++;
			sequence &= meta.getSeqBitsMask();
			if (sequence == 0) {
				timestamp = TimeUtils.tillNextTimeUnit(lastTimestamp, IdType.parse(id.getType()));
			}
		} else {
			lastTimestamp = timestamp;
			sequence = 0;
		}

		id.setSeq(sequence);
		id.setTime(timestamp);
	}

	@Override
	public void reset() {
		this.sequence = 0;
		this.lastTimestamp = -1;
	}
}
