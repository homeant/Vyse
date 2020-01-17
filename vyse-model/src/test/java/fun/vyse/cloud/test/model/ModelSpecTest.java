package fun.vyse.cloud.test.model;

import fun.vyse.cloud.model.entity.ModelSpec;
import fun.vyse.cloud.model.service.ModelSpecService;
import fun.vyse.cloud.test.ApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

/**
 * ModelTest
 *
 * @author junchen
 * @date 2020-01-15 22:43
 */
@Slf4j
public class ModelSpecTest extends ApplicationTest {

	@Autowired
	private ModelSpecService modelSpecService;

	@Test
	@Transactional
	public void test(){
		ModelSpec modelSpec = modelSpecService.getOne(1);
		log.info("model:{}",modelSpec);
	}

	@Configuration
	@EnableAutoConfiguration
	public static class Config{

	}
}
