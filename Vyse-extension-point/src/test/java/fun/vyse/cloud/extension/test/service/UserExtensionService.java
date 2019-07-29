package fun.vyse.cloud.extension.test.service;

import fun.vyse.cloud.extension.test.IUserExt;
import fun.vyse.cloud.extension.test.entity.User;
import fun.vyse.cloud.extension.test.service.impl.UserServiceImpl;
import fun.vyse.colud.extension.annotation.Extension;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * <p>fun.vyse.cloud.extension.test.service.UserExtensionService</p>
 * <p></p>
 *
 * @author huangtianhui
 */
@Slf4j
@Component
public class UserExtensionService implements IUserExt {
    @Extension(condition = "#args[0].name eq '1'")
    public User getUser(User user){
        user.setName("112232");
        return null;
    }

	@Override
	@Extension(condition = "#args[0].name eq '1'",order = 1)
	public void beforeMethod(Object [] args) {
		log.debug("beforeMethod:{}","123");
	}

	@Override
	@Extension(condition = "#args[0].name eq '1'",order = 2)
	public void afterMethod(Object [] args) {
		log.debug("beforeMethod:{}","456");
	}

}
