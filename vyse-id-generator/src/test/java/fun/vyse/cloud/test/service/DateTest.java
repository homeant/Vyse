package fun.vyse.cloud.test.service;

import fun.vyse.cloud.domain.Id;
import fun.vyse.cloud.domain.IdMeta;
import fun.vyse.cloud.service.IdService;
import fun.vyse.cloud.test.ApplicationTests;
import fun.vyse.cloud.test.config.AutoConfiguration;
import fun.vyse.cloud.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
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
	public void snowflakeIdTest() {
		SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(1, 1);
		long nextId = snowflakeIdWorker.nextId();
		log.debug("nextId:{}", nextId);
	}

	@Test
	public void dateTest() throws ParseException {
		long id = idService.genId();
		log.debug("id:{}", id);
		Date date = DateUtils.parseDate("2019-11-09 19:44:08", "yyyy-MM-dd HH:mm:ss");
		Long time = date.getTime() - 1420041600000L;
		log.debug("time:{}", time / 1000);
		long makeId = idService.makeId(1, time, 0);
		Id res = idService.expId(id);
		log.debug("id:{},res:{}", makeId, res);
	}

	@Test
	public void test2() throws ParseException {
		Date date = DateUtils.parseDate("2019-11-09 19:44:08", "yyyy-MM-dd HH:mm:ss");
		Date startDate = DateUtils.parseDate("2019-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		Long time = date.getTime() - startDate.getTime();
		time = time / 1000;
		for (int i = 1; i < 20; i++) {
			log.debug("time:{}", time << i);
		}
	}

	@Test
	public void test3() throws ParseException {
		Date nowDate = DateUtils.parseDate("2019-11-09 19:44:08", "yyyy-MM-dd HH:mm:ss");
		Date startDate = DateUtils.parseDate("2019-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		Long machine = 1L;
		Long endDate = nowDate.getTime() - startDate.getTime();
		Long nowTime = endDate / 1000;

		Long machineBits = 10L;
		Long machineBitsMask = -1L ^ -1L << machineBits;
		//看不懂算法，此处必须为30及以上
//		Long timeBits = 30L;
//		Long timeBitsStartPos = machineBits;
//		Long timeBitsMask = -1L ^ -1L << timeBits;
		Long seqBits = 10L;
		Long seqBitsStartPos = machineBits;
		Long seqBitsMask = -1L ^ -1L << seqBits;

		for (int i = 200; i < 250; i++) {
			Long value = 0L;
			log.debug("nowTime:{}", nowTime);
			value |= machine;
			// value |= nowTime << timeBitsStartPos;
			//value |= 1000000000L;
			Long index = Long.parseLong(i + "");
			value |= index << seqBitsStartPos;
			log.debug("result:{}", value);

			Long id = Long.parseLong(value + "");
			log.debug("machine:{}", id & machineBitsMask);
			//log.debug("time:{}", (id >>> timeBitsStartPos) & timeBitsMask);
			log.debug("index:{}", (id >>> seqBitsStartPos) & seqBitsMask);
		}
	}

	@Import({AutoConfiguration.class})
	@Configuration
	@EnableAutoConfiguration
	public static class Config {

	}
}
