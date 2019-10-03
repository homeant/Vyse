package fun.vyse.cloud.test;

import fun.vyse.cloud.test.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.scripting.dsl.Scripts;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.*;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

	@Autowired
	private ContextConfiguration.TempConverter tempConverter;

	@Autowired
	private PollableChannel discardChannel;

	@Autowired
	private PollableChannel results;

	@Autowired
	private PollableChannel groovyResult;

	@Autowired
	private ExecutorChannel executorChannel;

	@Autowired
	@Qualifier("scriptFilter.input")
	private MessageChannel filterInput;

	@Autowired
	@Qualifier("scriptTransform.input")
	private MessageChannel scriptTransform;

	@Autowired
	@Qualifier("executorFolw.input")
	private MessageChannel executorFolw;

	@Test
	public void test() {
		//字符串方式
		final String info = tempConverter.hello("homeant");
		log.debug("info:{}", info);
		//实体对象方式
		log.debug("userInfo:{}", tempConverter.userInfo("homeant"));
	}

	@Test
	public void channel() {
		Message<String> message = new GenericMessage<>("homeant");
		discardChannel.send(message);
	}

	@Test
	public void route() {
		UserInfo userInfo = new UserInfo();
		userInfo.setName("homeant");
		userInfo = tempConverter.route(userInfo);
		log.debug("userInfo:{}", userInfo);
	}

	@Test
	public void filterTest() {
		Message<?> message = MessageBuilder.withPayload("homeant").setHeader("type", "good").build();
		Message<?> message1 = MessageBuilder.withPayload("homeant").setHeader("type", "bad").build();
		filterInput.send(message);
		filterInput.send(message1);
		Message<?> receive = results.receive(10000);
		log.debug("result:{}", receive.getPayload());
		assertThat(results.receive(0)).isNull();

		Message<?> receive1 = discardChannel.receive(10000);
		log.debug("result1:{}", receive1.getPayload());
		assertThat(discardChannel.receive(0)).isNull();
	}

	@Test
	public void script() {
		Message<?> message = new GenericMessage<>("good");
		scriptTransform.send(message);
		Message<?> receive = groovyResult.receive(10000);
		log.debug("result:{}", receive);
	}

	@Test
	public void executorFlow() throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			final Integer num = i+1;
			Runnable runnable = () -> {
				QueueChannel replyChannel = new QueueChannel();
				Message<?> testMessage =
						MessageBuilder.withPayload("test_"+num)
								.setReplyChannel(replyChannel)
								.build();
				executorFolw.send(testMessage);
				Message<?> receive = replyChannel.receive(1000);
				log.debug("result:{}", receive);
			};
			runnable.run();
		}
	}


	@Configuration
	@EnableIntegration
	@EnableAutoConfiguration
	public static class ContextConfiguration {
		@MessagingGateway
		public interface TempConverter {

			@Gateway(requestChannel = "convert.input")
			String hello(String name);

			@Gateway(requestChannel = "userInfo.input")
			UserInfo userInfo(String name);

			@Gateway(requestChannel = "route.input")
			UserInfo route(UserInfo userInfo);

		}

		@Bean
		public IntegrationFlow convert() {
			return f -> f
					.handle(Http.outboundGateway("https://api.github.com/users/{name}")
							.httpMethod(HttpMethod.GET)
							.expectedResponseType(String.class)
							.uriVariable("name", r -> r.getPayload()))
					.log(LoggingHandler.Level.DEBUG).bridge();
		}

		@Bean
		public IntegrationFlow userInfo() {
			return f -> f
					.handle(Http.outboundGateway("https://api.github.com/users/{name}")
							.uriVariable("name", r -> r.getPayload())
							.httpMethod(HttpMethod.GET)
							.expectedResponseType(UserInfo.class))
					.log(LoggingHandler.Level.DEBUG).bridge();
		}

		@Bean
		public PollableChannel discardChannel() {
			QueueChannel queueChannel = new QueueChannel();
			queueChannel.addInterceptor(new ChannelInterceptor() {
				@Override
				public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
					log.debug("Interceptor body:{}", message.getPayload());
				}
			});
			return queueChannel;
		}


		@Bean
		public IntegrationFlow route() {
			return f -> f.<UserInfo, String>transform(r -> r.getName()).route(r -> "userInfo.input");
		}

		@Bean
		public Resource scriptResource() {
			return new ByteArrayResource("headers.type == 'good'".getBytes());
		}


		@Bean
		public IntegrationFlow scriptFilter() {
			return f -> f
					.filter(Scripts.processor(scriptResource())
									.lang("groovy"),
							e -> e.discardChannel("discardChannel"))
					.channel(c -> c.queue("results"));
		}

		@Value("scripts/GroovyTransformerTests.groovy")
		private Resource scriptResource;

		@Bean
		public IntegrationFlow scriptTransform() {
			return f -> f
					.transform(Scripts.processor(scriptResource)
							.lang("groovy"))
					.channel(c -> c.queue("groovyResult"));
		}

		@Bean
		public ExecutorChannel executorChannel() {
			ExecutorChannel channel = new ExecutorChannel(executor());
			channel.addInterceptor(new ChannelInterceptor(){
				@Override
				public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
					log.debug("time:{},{}",message.getPayload(),System.currentTimeMillis());
				}
			});
			return channel;
		}

		@Bean
		public Executor executor() {
			ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
			taskExecutor.setCorePoolSize(5);
			taskExecutor.setMaxPoolSize(10);
			taskExecutor.setQueueCapacity(25);
			taskExecutor.initialize();
			return taskExecutor;
		}

		@Bean
		public IntegrationFlow executorFolw() {
			return f -> f
					.gateway("executorChannel");
		}

		@Bean
		public IntegrationFlow integrationFlow() {
			return IntegrationFlows.from("executorChannel")
					.handle(Http.outboundGateway("https://api.github.com/users/homeant")
							.httpMethod(HttpMethod.GET)
							.expectedResponseType(String.class)).transform(r->r)
					.get();
		}
	}
}
