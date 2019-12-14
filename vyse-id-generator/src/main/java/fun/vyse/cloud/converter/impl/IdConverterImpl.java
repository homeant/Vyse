package fun.vyse.cloud.converter.impl;

import fun.vyse.cloud.converter.IdConverter;
import fun.vyse.cloud.domain.Id;
import fun.vyse.cloud.domain.IdMeta;
import fun.vyse.cloud.enums.IdType;
import fun.vyse.cloud.factory.IdMetaFactory;
import lombok.Data;

/**
 * IdConverterImpl
 *
 * @author junchen
 * @date 2019-12-14 12:03
 */
@Data
public class IdConverterImpl implements IdConverter {
	private IdMeta idMeta;

	public IdConverterImpl(IdType idType) {
		this(IdMetaFactory.getIdMeta(idType));
	}

	public IdConverterImpl(IdMeta idMeta) {
		this.idMeta = idMeta;
	}

	@Override
	public long convert(Id id) {
		return doConvert(id, idMeta);
	}

	protected long doConvert(Id id, IdMeta meta) {
		long ret = 0;

		ret |= id.getMachine();

		ret |= id.getSeq() << meta.getSeqBitsStartPos();

		ret |= id.getTime() << meta.getTimeBitsStartPos();

		ret |= id.getGenMethod() << meta.getGenMethodBitsStartPos();

		ret |= id.getType() << meta.getTypeBitsStartPos();

		ret |= id.getVersion() << meta.getVersionBitsStartPos();

		return ret;
	}

	@Override
	public Id convert(long id) {
		return doConvert(id, idMeta);
	}

	protected Id doConvert(long id, IdMeta idMeta) {
		Id ret = new Id();

		ret.setMachine(id & idMeta.getMachineBitsMask());

		ret.setSeq((id >>> idMeta.getSeqBitsStartPos()) & idMeta.getSeqBitsMask());

		ret.setTime((id >>> idMeta.getTimeBitsStartPos()) & idMeta.getTimeBitsMask());

		ret.setGenMethod((id >>> idMeta.getGenMethodBitsStartPos()) & idMeta.getGenMethodBitsMask());

		ret.setType((id >>> idMeta.getTypeBitsStartPos()) & idMeta.getTypeBitsMask());

		ret.setVersion((id >>> idMeta.getVersionBitsStartPos()) & idMeta.getVersionBitsMask());

		return ret;
	}
}
