package fun.vyse.cloud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Id
 *
 * @author junchen
 * @date 2019-12-13 22:08
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Id implements Serializable {
	private long machine;
	private long seq;
	private long time;
	private long genMethod;
	private long type;
	private long version;
}
