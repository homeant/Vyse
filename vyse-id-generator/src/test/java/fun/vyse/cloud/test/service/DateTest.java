package fun.vyse.cloud.test.service;

import fun.vyse.cloud.domain.Id;
import fun.vyse.cloud.service.IdService;
import fun.vyse.cloud.test.ApplicationTests;
import fun.vyse.cloud.test.config.AutoConfiguration;
import fun.vyse.cloud.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.util.Date;

/**
 * DateTest
 *
 * @author junchen
 * @date 2019-12-14 16:24
 */
@Slf4j
@Configuration
@Import({IdTest.Config.class})
public class DateTest extends ApplicationTests {

	@Autowired
	private IdService idService;

	@Test
	public void dateTest() throws ParseException {
		long id = idService.genId();
		log.debug("id:{}", id);
		Date date = DateUtils.parseDate("2019-11-09 19:44:08", "yyyy-MM-dd HH:mm:ss");
		Long time = date.getTime() - 1420041600000L;
		log.debug("time:{}", time / 1000);
		long makeId = idService.makeId(time, 0,1);
		Id res = idService.expId(id);
		log.debug("id:{},res:{}", makeId, res);
		SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1, 1);
		long nextId = snowflakeIdWorker.nextId();
		log.debug("nextId:{}", nextId);
	}

	@Import({AutoConfiguration.class})
	@Configuration
	@EnableAutoConfiguration
	public static class Config {

	}
}
