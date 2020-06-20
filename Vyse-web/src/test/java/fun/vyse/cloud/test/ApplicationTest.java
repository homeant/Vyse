package fun.vyse.cloud.test;

import fun.vyse.cloud.test.config.ApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * ApplicationTest
 *
 * @author junchen
 * @date 2020-01-15 22:38
 */
@SpringBootTest(classes = {ApplicationConfiguration.class})
public class ApplicationTest extends AbstractTestNGSpringContextTests {
	
}
