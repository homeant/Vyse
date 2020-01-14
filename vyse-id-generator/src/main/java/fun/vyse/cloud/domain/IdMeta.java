package fun.vyse.cloud.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * IdMeta
 *
 * @author junchen
 * @date 2019-12-14 11:38
 */
@Getter
@Setter
@AllArgsConstructor
public class IdMeta {
	private byte machineBits;

	private byte seqBits;

	private byte timeBits;

	private byte genMethodBits;

	private byte typeBits;

	private byte versionBits;

	public long getMachineBitsMask() {
		return -1L ^ -1L << machineBits;
	}

	public long getSeqBitsStartPos() {
		return machineBits;
	}

	public long getSeqBitsMask() {
		return -1L ^ -1L << seqBits;
	}

	public long getTimeBitsStartPos() {
		return machineBits + seqBits;
	}

	public long getTimeBitsMask() {
		return -1L ^ -1L << timeBits;
	}

	public long getGenMethodBitsStartPos() {
		return machineBits + seqBits + timeBits;
	}

	public long getGenMethodBitsMask() {
		return -1L ^ -1L << genMethodBits;
	}

	public long getTypeBitsStartPos() {
		return machineBits + seqBits + timeBits + genMethodBits;
	}

	public long getTypeBitsMask() {
		return -1L ^ -1L << typeBits;
	}

	public long getVersionBitsStartPos() {
		return machineBits + seqBits + timeBits + genMethodBits + typeBits;
	}

	public long getVersionBitsMask() {
		return -1L ^ -1L << versionBits;
	}
}
