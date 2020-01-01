package fun.vyse.cloud.test.service;

import fun.vyse.cloud.converter.IdConverter;
import fun.vyse.cloud.converter.impl.IdConverterImpl;
import fun.vyse.cloud.domain.Id;
import fun.vyse.cloud.domain.IdMeta;
import fun.vyse.cloud.enums.IdType;
import fun.vyse.cloud.provider.MachineIdProvider;
import fun.vyse.cloud.provider.PropertyMachineIdProvider;
import fun.vyse.cloud.service.IdService;
import fun.vyse.cloud.service.impl.IdServiceImpl;
import fun.vyse.cloud.test.ApplicationTests;
import fun.vyse.cloud.test.config.AutoConfiguration;
import fun.vyse.cloud.util.TimeUtils;
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
import java.time.ZoneOffset;
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

	@Test
	public void seqTest() throws InterruptedException {
		Produce produce = new Produce();
		for (int i = 0; i < 100; i++) {
			new Thread(produce::produce).start();
		}
		Thread.sleep(5000);
	}

	public class Produce {

		private long sequence = 0;
		private long lastTimestamp = -1;

		private final long epoch = 1577836800000L;

		private final long seqBits = 8L;
		private final long seqBitsMask = -1L ^ -1L << this.seqBits;
		private final long timeBits = seqBits;
		private final long timeBitsMask = -1L ^ -1L << this.timeBits;

		public synchronized void produce() {
			long timestamp = timeGen();
			// 如果上一个timestamp与新产生的相等，则sequence加一(0-4095循环); 对新的timestamp，sequence从0开始
			if (timestamp == lastTimestamp) {
				sequence++;
				sequence &= seqBitsMask;
				if (sequence == 0) {
					// 重新生成timestamp
					timestamp = timeGen();
				}
			} else {
				sequence = 0;
			}
			lastTimestamp = timestamp;

			long ret = 0;
			ret |= sequence;
			ret |= (timestamp - epoch) << timeBits;

			log.debug("time:{},seq:{}", timestamp - epoch, sequence);

			Long seq = (ret &  (timeBitsMask));
			Long time = (ret >>> timeBits);

			log.debug("ret:{},time:{},seq:{}", ret, time, seq);
		}

		/**
		 * 获得系统当前毫秒数
		 */
		private long timeGen() {
			return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
		}
	}


	@Import({AutoConfiguration.class})
	@Configuration
	@EnableAutoConfiguration
	public static class Config {

	}
}
