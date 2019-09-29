package fun.vyse.cloud.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private Config.TempConverter tempConverter;

    @Test
    public void test() {
        final String tom = tempConverter.hello("tom");
        log.debug("message:{}",tom);
    }


    @Configuration
    @EnableAutoConfiguration
    public static class Config {
        @MessagingGateway
        public interface TempConverter {

            @Gateway(requestChannel = "convert.input")
            String hello(String name);

        }

        @Bean
        public IntegrationFlow convert() {
            return f -> f.transform(r -> "hello " + r);
        }
    }
}
