package fun.vyse.cloud.produce;

import fun.vyse.cloud.domain.Id;
import fun.vyse.cloud.domain.IdMeta;

/**
 * IdProduce
 *
 * @author junchen
 * @date 2019-12-14 12:57
 */
public interface IdProduce {
	void produce(Id id, IdMeta meta);
}
