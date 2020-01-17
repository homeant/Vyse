package fun.vyse.cloud.test.model;

import fun.vyse.cloud.test.ApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.testng.annotations.Test;

/**
 * ModelTest
 *
 * @author junchen
 * @date 2020-01-15 22:43
 */
@Slf4j
public class ModelSpecTest extends ApplicationTest {

	@Test
	public void test(){

	}

	@Configuration
	@EnableAutoConfiguration
	public static class Config{

	}
}
