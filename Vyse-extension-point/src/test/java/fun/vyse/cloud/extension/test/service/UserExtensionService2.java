package fun.vyse.cloud.extension.test.service;

import fun.vyse.cloud.extension.test.IUserExt;
import fun.vyse.cloud.extension.test.entity.User;
import org.springframework.stereotype.Component;

/**
 * <p>fun.vyse.cloud.extension.test.service.UserExtensionService</p>
 * <p></p>
 *
 * @author huangtianhui
 */
@Component
public class UserExtensionService2 implements IUserExt {
    public User getUser(User user){
        return null;
    }
}
