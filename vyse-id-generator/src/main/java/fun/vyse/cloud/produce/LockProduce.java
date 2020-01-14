package fun.vyse.cloud.produce;

import fun.vyse.cloud.domain.Id;
import fun.vyse.cloud.domain.IdMeta;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * LockProduce
 *
 * @author junchen
 * @date 2019-12-14 13:05
 */
public class LockProduce extends AbstractProduce {
	private Lock lock = new ReentrantLock();

	@Override
	public void produce(Id id, IdMeta meta) {
		lock.lock();
		try {
			super.produce(id, meta);
		} finally {
			lock.unlock();
		}
	}
}
