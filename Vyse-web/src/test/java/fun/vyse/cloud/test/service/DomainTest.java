package fun.vyse.cloud.test.service;

import fun.vyse.cloud.test.ApplicationTest;
import fun.vyse.cloud.test.domain.User;
import fun.vyse.cloud.test.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class DomainTest extends ApplicationTest {



	@Test
	public void test(){
		User user = new User();
		user.setLoginName("12321");
		user.setPassword("123456");
		log.info("result:{}",UserMapper.INSTANCE.userToUserDto(user));
	}
}
