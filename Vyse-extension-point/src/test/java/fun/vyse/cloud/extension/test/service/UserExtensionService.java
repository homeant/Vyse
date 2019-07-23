package fun.vyse.cloud.extension.test.service;

import fun.vyse.cloud.extension.test.IUserExt;
import fun.vyse.cloud.extension.test.entity.User;
import fun.vyse.colud.extension.annotation.Extension;
import org.springframework.stereotype.Component;

/**
 * <p>fun.vyse.cloud.extension.test.service.UserExtensionService</p>
 * <p></p>
 *
 * @author huangtianhui
 */
@Component
public class UserExtensionService implements IUserExt {
    @Extension(condition = "#args[0].name eq '1'")
    public User getUser(User user){
        user.setName("112232");
        return null;
    }
}
