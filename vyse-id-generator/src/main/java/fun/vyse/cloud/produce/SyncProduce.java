package fun.vyse.cloud.produce;

import fun.vyse.cloud.domain.Id;
import fun.vyse.cloud.domain.IdMeta;

/**
 * SyncProduce
 *
 * @author junchen
 * @date 2019-12-14 13:05
 */
public class SyncProduce extends AbstractProduce {
	@Override
	public synchronized void produce(Id id, IdMeta meta) {
		super.produce(id, meta);
	}
}
