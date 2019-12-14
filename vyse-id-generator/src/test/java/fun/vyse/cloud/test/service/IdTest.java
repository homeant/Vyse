package fun.vyse.cloud.test.service;

import fun.vyse.cloud.converter.IdConverter;
import fun.vyse.cloud.converter.impl.IdConverterImpl;
import fun.vyse.cloud.domain.Id;
import fun.vyse.cloud.enums.IdType;
import fun.vyse.cloud.provider.MachineIdProvider;
import fun.vyse.cloud.provider.PropertyMachineIdProvider;
import fun.vyse.cloud.service.IdService;
import fun.vyse.cloud.service.impl.IdServiceImpl;
import fun.vyse.cloud.test.ApplicationTests;
import fun.vyse.cloud.test.config.AutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * IdTest
 *
 * @author junchen
 * @date 2019-12-14 13:17
 */
@Slf4j
public class IdTest extends ApplicationTests {

	@Autowired
	private IdService idService;

	@Test(groups = {"idService"})
	public void test() {
		for (int i = 0; i < 50; i++) {

			Long id = idService.genId();
			Id res = idService.expId(id);
			Date date = idService.transTime(res.getTime());
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			Instant instant = date.toInstant();
			ZoneId zoneId = ZoneId.systemDefault();
			LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
			String dateValue = localDateTime.format(dateTimeFormatter);

			log.debug("id:{}，date:{}, res:{}", id, dateValue, res);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}



	@Import({AutoConfiguration.class})
	@Configuration
	@EnableAutoConfiguration
	public static class Config {

	}
}
