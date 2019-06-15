package fun.vyse.cloud.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import fun.vyse.cloud.test.domain.Address;
import fun.vyse.cloud.test.domain.User;
import fun.vyse.cloud.test.domain.UserDto;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import org.dozer.Mapper;
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

    @Autowired
    private MapperFactory mapperFactory;

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
            address.setAddress("北京"+i);
            addresses.add(address);
        }
        user.setAddresses(addresses);
        for (int i = 0; i < 100; i++) {
            user.setName(user.getName()+i);
            long startTime = System.currentTimeMillis();
            mapperFacade.map(user,Map.class);
            long endTime = System.currentTimeMillis();
            log.debug("{}",endTime-startTime);
        }
    }

    @Test
    public void test2(){
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
        for (int i = 0; i < 100; i++) {
            user.setName(user.getName()+i);
            long startTime = System.currentTimeMillis();
            mapper.map(user, UserDto.class);
            long endTime = System.currentTimeMillis();
            log.debug("dozer:{}",endTime-startTime);
        }
    }


    @Configuration
    @EnableAutoConfiguration
    public static class Config {

    }
}
