package fun.vyse.cloud.test.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
	private String loginName;

	private String password;
}
