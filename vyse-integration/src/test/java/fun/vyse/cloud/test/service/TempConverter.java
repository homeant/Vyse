package fun.vyse.cloud.test.service;

import fun.vyse.cloud.test.entity.UserInfo;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

/**
 * TempConverter
 *
 * @author junchen
 * @date 2020-01-16 22:42
 */
@MessagingGateway
public interface TempConverter {

	@Gateway(requestChannel = "convert.input")
	String hello(String name);

	@Gateway(requestChannel = "userInfo.input")
	UserInfo userInfo(String name);

	@Gateway(requestChannel = "route.input")
	UserInfo route(UserInfo userInfo);

}
