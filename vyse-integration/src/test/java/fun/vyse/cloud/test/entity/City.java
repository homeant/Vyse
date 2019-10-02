package fun.vyse.cloud.test.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class City {
	private String name;
	private String code;
}
