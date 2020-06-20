package fun.vyse.cloud.test.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
	private String username;

	private String password;
}
