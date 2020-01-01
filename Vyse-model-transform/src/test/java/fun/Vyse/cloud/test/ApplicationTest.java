package fun.vyse.cloud.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import fun.vyse.cloud.test.domain.Address;
import fun.vyse.cloud.test.domain.User;
import fun.vyse.cloud.test.domain.UserDto;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;


@Slf4j
@SpringBootTest
public class ApplicationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private Mapper mapper;

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Test
    public void test() {
        User user = new User();
        user.setName("hello");
        user.setDate(new Date());
        List<Address> addresses = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            Address address = new Address();
            address.setName("北京" + i);
            addresses.add(address);
        }
        user.setAddressList(addresses);
        Long time = 0L;
        for (int i = 0; i < 100; i++) {
            user.setName(user.getName() + i);
            long startTime = System.currentTimeMillis();
            UserDto map = mapperFacade.map(user, UserDto.class);
            long endTime = System.currentTimeMillis();
            try {
                log.debug("str:{}",objectMapper.writeValueAsString(map));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            time += endTime - startTime;
        }
        log.debug("mapperFacade:{}", time);
    }

    @Test
    public void test2() {
        User user = new User();
        user.setName("hello");
        user.setDate(new Date());
        List<Address> addresses = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            Address address = new Address();
            address.setName("北京" + i);
            addresses.add(address);
        }
        user.setAddressList(addresses);
        Long time = 0L;
        for (int i = 0; i < 100; i++) {
            user.setName(user.getName() + i);
            long startTime = System.currentTimeMillis();
            mapper.map(user, UserDto.class);
            long endTime = System.currentTimeMillis();
            time += endTime - startTime;
        }
        log.debug("dozer:{}", time);
    }


    @Configuration
    @EnableAutoConfiguration
    public static class Config {

    }
}
