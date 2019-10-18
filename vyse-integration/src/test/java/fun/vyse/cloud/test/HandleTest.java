package fun.vyse.cloud.test;

import fun.vyse.cloud.integration.handle.IIntegrationHandle;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ServiceLoader;

/**
 * @author junchen
 * @Description 处理测试类
 * @date 2019-10-07 13:13
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class HandleTest {
	@Test
	public void test(){
		ServiceLoader<IIntegrationHandle> operations = ServiceLoader.load(IIntegrationHandle.class);
		operations.forEach(r->{
			log.debug("class:{}",r.getClass().getName());
		});
	}

	@Configuration
	@EnableAutoConfiguration
	public static class ContextConfiguration {

	}
}
