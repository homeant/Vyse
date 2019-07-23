package fun.vyse.cloud.extension.test;

import fun.vyse.cloud.extension.test.entity.User;
import fun.vyse.cloud.extension.test.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>fun.vyse.cloud.extension.test.ExtensionApplication</p>
 * <p></p>
 *
 * @author huangtianhui
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"logging.level.fun=DEBUG"},webEnvironment= SpringBootTest.WebEnvironment.NONE)
public class ExtensionApplication {

    @Autowired
    private IUserService userService;

    @Test
    public void test1() {
        User arg = new User();
        arg.setName("1");
        User user = userService.getUser(arg);
        log.debug("user:{}",user);
    }

    @EnableAutoConfiguration
    @Configuration
    @ComponentScan(value = {"fun.vyse"})
    @ImportResource(locations= {"classpath:applicationContext.xml"})
    public static class ContextConfiguration {

    }
}
