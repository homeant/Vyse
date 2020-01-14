package fun.vyse.cloud.service.impl;

import fun.vyse.cloud.converter.IdConverter;
import fun.vyse.cloud.converter.impl.IdConverterImpl;
import fun.vyse.cloud.domain.Id;
import fun.vyse.cloud.domain.IdMeta;
import fun.vyse.cloud.enums.IdType;
import fun.vyse.cloud.factory.IdMetaFactory;
import fun.vyse.cloud.provider.MachineIdProvider;
import fun.vyse.cloud.service.IdService;
import fun.vyse.cloud.util.TimeUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * AbstractIdServiceImpl
 *
 * @author junchen
 * @date 2019-12-14 11:53
 */
@Slf4j
public abstract class AbstractIdServiceImpl implements IdService {
	@Setter
	protected long machineId = -1;
	protected long genMethod = 0;
	@Setter
	protected long type = 0;
	@Setter
	protected long version = 0;

	protected IdType idType;
	@Setter
	protected IdMeta idMeta;
	@Setter
	protected IdConverter idConverter;

	@Setter
	protected MachineIdProvider machineIdProvider;

	public AbstractIdServiceImpl() {
		idType = IdType.MAX_PEAK;
	}

	public AbstractIdServiceImpl(String type) {
		idType = IdType.parse(type);
	}

	public AbstractIdServiceImpl(IdType type) {
		idType = type;
	}

	@Override
	public void init() {
		this.machineId = machineIdProvider.getMachineId();

		if (machineId < 0) {
			log.error("The machine ID is not configured properly so that Vesta Service refuses to start.");

			throw new IllegalStateException(
					"The machine ID is not configured properly so that Vesta Service refuses to start.");

		}
		if(this.idMeta == null){
			setIdMeta(IdMetaFactory.getIdMeta(idType));
			setType(idType.value());
		} else {
			if(this.idMeta.getTimeBits() == 30){
				setType(0);
			} else if(this.idMeta.getTimeBits() == 40){
				setType(1);
			} else {
				throw new RuntimeException("Init Error. The time bits in IdMeta should be set to 30 or 40!");
			}
		}
		setIdConverter(new IdConverterImpl(this.idMeta));
	}

	@Override
	public long genId() {
		Id id = new Id();

		id.setMachine(machineId);
		id.setGenMethod(genMethod);
		id.setType(type);
		id.setVersion(version);

		produceId(id);

		long ret = idConverter.convert(id);

		// Use trace because it cause low performance
		if (log.isTraceEnabled()) {
			log.trace(String.format("Id: %s => %d", id, ret));
		}
		return ret;
	}

	/**
	 * 生成ID
	 * @param id
	 */
	protected abstract void produceId(Id id);

	@Override
	public Date transTime(final long time) {
		if (idType == IdType.MAX_PEAK) {
			return new Date(time * 1000 + TimeUtils.EPOCH);
		} else if (idType == IdType.MIN_GRANULARITY) {
			return new Date(time + TimeUtils.EPOCH);
		}

		return null;
	}


	@Override
	public Id expId(long id) {
		return idConverter.convert(id);
	}

	@Override
	public long makeId(long time, long seq) {
		return makeId(time, seq, machineId);
	}

	@Override
	public long makeId(long time, long seq, long machine) {
		return makeId(genMethod, time, seq, machine);
	}

	@Override
	public long makeId(long genMethod, long time, long seq, long machine) {
		return makeId(type, genMethod, time, seq, machine);
	}

	@Override
	public long makeId(long type, long genMethod, long time,
					   long seq, long machine) {
		return makeId(version, type, genMethod, time, seq, machine);
	}

	@Override
	public long makeId(long version, long type, long genMethod,
					   long time, long seq, long machine) {
		IdType idType = IdType.parse(type);

		Id id = new Id(machine, seq, time, genMethod, type, version);
		IdConverter idConverter = new IdConverterImpl(idType);

		return idConverter.convert(id);
	}
}
