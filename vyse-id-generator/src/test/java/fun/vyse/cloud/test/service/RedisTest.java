package fun.vyse.cloud.test.service;

import fun.vyse.cloud.test.ApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * RedisTest
 *
 * @author junchen
 * @date 2020-01-01 18:07
 */
@Slf4j
public class RedisTest extends ApplicationTests {
	@Autowired
	private RedisTemplate redisTemplate;

	private RedisAtomicLong atomicLong;

	@BeforeMethod
	public void init(){
		atomicLong = new RedisAtomicLong("20200101", redisTemplate.getConnectionFactory());
	}

	@Test
	public void test() throws InterruptedException {
		log.debug("redis:{}",redisTemplate);
		for (int i = 0; i < 100; i++) {
			new Thread(() -> {
				long id = atomicLong.getAndIncrement();
				atomicLong.expire(10000, TimeUnit.SECONDS);
				log.debug("time:{},id:{}",System.currentTimeMillis(),id);
			}).start();
		}
		Thread.sleep(2000);
	}

	@Configuration
	@EnableAutoConfiguration
	public static class Config {

	}
}
