package fun.vyse.cloud.service.impl;

import fun.vyse.cloud.domain.Id;
import fun.vyse.cloud.enums.IdType;
import fun.vyse.cloud.produce.AtomicProduce;
import fun.vyse.cloud.produce.IdProduce;
import fun.vyse.cloud.produce.LockProduce;
import fun.vyse.cloud.produce.SyncProduce;
import fun.vyse.cloud.util.CommonUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * IdServiceImpl
 *
 * @author junchen
 * @date 2019-12-14 10:57
 */
@Slf4j
public class IdServiceImpl extends AbstractIdServiceImpl {

	private static final String SYNC_LOCK_IMPL_KEY = "vesta.sync.lock.impl.key";

	private static final String ATOMIC_IMPL_KEY = "vesta.atomic.impl.key";

	@Setter
	protected IdProduce idProduce;

	public IdServiceImpl() {
		super();
		initProduce();
	}

	public IdServiceImpl(String type) {
		super(type);

		initProduce();
	}

	public IdServiceImpl(IdType type) {
		super(type);

		initProduce();
	}

	@Override
	protected void produceId(Id id) {
		this.idProduce.produce(id,this.idMeta);
	}

	public void initProduce() {
		if(idProduce != null){
			log.info("The " + idProduce.getClass().getCanonicalName() + " is used.");
		} else if (CommonUtils.isPropKeyOn(SYNC_LOCK_IMPL_KEY)) {
			log.info("The SyncProduce is used.");
			idProduce = new SyncProduce();
		} else if (CommonUtils.isPropKeyOn(ATOMIC_IMPL_KEY)) {
			log.info("The AtomicProduce is used.");
			idProduce = new AtomicProduce();
		} else {
			log.info("The default LockProduce is used.");
			idProduce = new LockProduce();
		}
	}
}
