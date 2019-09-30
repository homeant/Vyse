package fun.vyse.cloud.test;

import fun.vyse.cloud.test.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.dsl.Http;
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
        log.debug("message:{}", tom);
        log.debug("userInfo:{}",tempConverter.userInfo("homeant"));
    }



    @Configuration
    @EnableIntegration
    @IntegrationComponentScan
    @EnableAutoConfiguration
    public static class Config {
        @MessagingGateway
        public interface TempConverter {

            @Gateway(requestChannel = "convert.input")
            String hello(String name);

            @Gateway(requestChannel = "userInfo.input")
            UserInfo userInfo(String name);

        }

        @Bean
        public IntegrationFlow convert() {
            return f->f.handle(Http.outboundGateway("https://api.github.com/users/homeant")
                    .httpMethod(HttpMethod.GET).expectedResponseType(String.class)).log(LoggingHandler.Level.DEBUG).bridge();
        }

		@Bean
        public IntegrationFlow userInfo(){
        	return f->f.handle(Http.outboundGateway("https://api.github.com/users/{name}").uriVariable("name",r->r.getPayload())
					.httpMethod(HttpMethod.GET).expectedResponseType(UserInfo.class))
					.log(LoggingHandler.Level.DEBUG).bridge();
		}
    }
}
