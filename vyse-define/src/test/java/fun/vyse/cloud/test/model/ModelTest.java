package fun.vyse.cloud.test.model;

import fun.vyse.cloud.define.repository.IModelRepository;
import fun.vyse.cloud.define.service.IModelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * ModelTest
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 09:49
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ModelTest {

	@Autowired
	private IModelService modelService;

	public void install(){

	}

	@Test
	public void test(){
		log.debug("bean:{}", modelService.get(1L));
	}

	@Configuration
	@ComponentScan(basePackages = {"fun.vyse.cloud"})
	@EntityScan(basePackages = {"fun.vyse.cloud"})
	@EnableJpaRepositories(basePackages = {"fun.vyse.cloud"})
	@EnableAutoConfiguration
	@EnableTransactionManagement
	public static class Config {

	}
}
