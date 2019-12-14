package fun.vyse.cloud.produce;

import fun.vyse.cloud.domain.Id;
import fun.vyse.cloud.domain.IdMeta;
import fun.vyse.cloud.enums.IdType;
import fun.vyse.cloud.util.TimeUtils;

import java.util.concurrent.atomic.AtomicReference;

/**
 * AtomicProduce
 *
 * @author junchen
 * @date 2019-12-14 13:07
 */
public class AtomicProduce implements IdProduce,ResetProduce {
	class Variant {

		private long sequence = 0;
		private long lastTimestamp = -1;

	}

	private AtomicReference<Variant> variant = new AtomicReference<Variant>(new Variant());

	@Override
	public void produce(Id id, IdMeta meta) {
		Variant varOld, varNew;
		long timestamp, sequence;

		while (true) {

			// Save the old variant
			varOld = variant.get();

			// populate the current variant
			timestamp = TimeUtils.genTime(IdType.parse(id.getType()));
			TimeUtils.validateTimestamp(varOld.lastTimestamp, timestamp);

			sequence = varOld.sequence;

			if (timestamp == varOld.lastTimestamp) {
				sequence++;
				sequence &= meta.getSeqBitsMask();
				if (sequence == 0) {
					timestamp = TimeUtils.tillNextTimeUnit(varOld.lastTimestamp, IdType.parse(id.getType()));
				}
			} else {
				sequence = 0;
			}

			// Assign the current variant by the atomic tools
			varNew = new Variant();
			varNew.sequence = sequence;
			varNew.lastTimestamp = timestamp;

			if (variant.compareAndSet(varOld, varNew)) {
				id.setSeq(sequence);
				id.setTime(timestamp);

				break;
			}

		}
	}

	@Override
	public void reset() {
		variant = new AtomicReference<Variant>(new Variant());
	}
}
