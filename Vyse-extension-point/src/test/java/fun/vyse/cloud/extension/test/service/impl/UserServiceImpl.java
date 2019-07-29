package fun.vyse.cloud.extension.test.service.impl;

import fun.vyse.cloud.extension.test.IUserExt;
import fun.vyse.cloud.extension.test.entity.User;
import fun.vyse.cloud.extension.test.service.IUserService;
import fun.vyse.cloud.extension.test.service.UserExtensionService;
import fun.vyse.colud.extension.IExtensionPoint;
import fun.vyse.colud.extension.annotation.ExtensionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>fun.vyse.cloud.extension.test.service.impl.UserServiceImpl</p>
 * <p></p>
 *
 * @author huangtianhui
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    @Override
    @ExtensionService(className = IExtensionPoint.class,name="")
    public User getUser(User user) {
        log.debug("执行前-user:{}",user);
        user.setName("1");
        log.debug("执行后-user:{}",user);
        return user;
    }
}
