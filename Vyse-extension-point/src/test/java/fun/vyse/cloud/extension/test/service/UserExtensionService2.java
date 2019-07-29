package fun.vyse.cloud.extension.test.service;

import fun.vyse.cloud.extension.test.IUserExt;
import fun.vyse.cloud.extension.test.entity.User;
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
public class UserExtensionService2 implements IUserExt {
	
    public User getUser(User user){
        return null;
    }

	@Override
	@Extension(condition = "#args[0].name eq '1'",order = 2)
	public void beforeMethod(Object [] args) {
		log.debug("beforeMethod:{}","991");
	}

	@Override
	@Extension(condition = "#args[0].name eq '1'",order = 1)
	public void afterMethod(Object [] args) {
		log.debug("beforeMethod:{}","992");
	}

}
