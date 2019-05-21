package fun.Vyse.cloud.test;

import com.google.common.collect.Lists;
import fun.Vyse.cloud.test.domain.Address;
import fun.Vyse.cloud.test.domain.User;
import fun.Vyse.cloud.test.domain.UserDto;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ApplicationTest {

    @Autowired
    private MapperFacade mapperFacade;

    @Test
    public void test() {
        User user = new User();
        user.setName("hello");
        user.setDate(new Date());
        List<Address> addresses = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            Address address = new Address();
            address.setAddress("北京"+i);
            addresses.add(address);
        }
        user.setAddresses(addresses);
//        UserDto userDto = mapperFacade.map(user, UserDto.class);
//        log.debug("dto:{}",userDto);
        Map<String,Object> map = mapperFacade.map(user, Map.class);
        log.debug("map:{}",map);
        User user1 = mapperFacade.map(map, User.class);
        log.debug("user:{}",user1);
    }


    @Configuration
    @EnableAutoConfiguration
    public static class Config {

    }
}
