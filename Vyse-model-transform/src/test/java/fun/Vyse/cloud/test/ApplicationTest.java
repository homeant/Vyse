package fun.Vyse.cloud.test;

import fun.Vyse.cloud.test.domain.User;
import fun.Vyse.cloud.test.domain.UserDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.Date;


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
        UserDto userDto = mapperFacade.map(user, UserDto.class);
        log.debug("dto:{}",userDto);
    }


    @Configuration
    @EnableAutoConfiguration
    public static class Config {

    }
}
