package fun.vyse.cloud.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import fun.vyse.cloud.test.entity.City;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.ws.SimpleWebServiceOutboundGateway;
import org.springframework.integration.ws.WebServiceHeaders;
import org.springframework.integration.xml.transformer.XPathTransformer;
import org.springframework.integration.xml.xpath.XPathEvaluationType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class WebServiceTest {

	@Autowired
	@Qualifier("webService.input")
	private MessageChannel messageChannel;

	@Autowired
	private PollableChannel resultChannel;


	@Test
	public void test() {
		Message<?> message = new GenericMessage("");
		messageChannel.send(message);
		Message<?> result = resultChannel.receive(1000);
		log.debug("result:{}", result.getPayload());
	}

	@Test
	public void transformTest() {
		XPathTransformer transformer = new XPathTransformer("/parent/child");
		transformer.setEvaluationType(XPathEvaluationType.NODE_LIST_RESULT);
		Message<?> message = MessageBuilder.withPayload(
				"<parent><child name='foo'/><child name='bar'/></parent>").build();
		Object result = transformer.transform(message);
		log.debug("result:{}", result);
	}

	@Configuration
	@EnableIntegration
	@EnableAutoConfiguration
	public static class ContextConfiguration {

		@Autowired
		private freemarker.template.Configuration freemarkerConfig;

		@Bean
		public IntegrationFlow webService() {
			return f -> f
					.transform(r -> {
						try {
							Template template = freemarkerConfig.getTemplate("getRegionCountry_request.ftl");
							String result = FreeMarkerTemplateUtils.processTemplateIntoString(template, Maps.newHashMap());
							return result;
						} catch (IOException e) {
							e.printStackTrace();
						} catch (TemplateException e) {
							e.printStackTrace();
						}
						return null;
					})
					.enrichHeaders(h -> h
							.header(WebServiceHeaders.SOAP_ACTION, "http://WebXml.com.cn/getRegionCountry"))
					.handle(new SimpleWebServiceOutboundGateway("http://ws.webxml.com.cn/WebServices/WeatherWS.asmx"))
					.transform(r -> {
						log.debug("result:{}", r);
						XPathTransformer transformer = new XPathTransformer("/*[local-name()=\"getRegionCountryResponse\"]/*[local-name()=\"getRegionCountryResult\"]/*[local-name()=\"string\"]");
						transformer.setEvaluationType(XPathEvaluationType.NODE_LIST_RESULT);
						return transformer.transform(MessageBuilder.withPayload(r).build());
					})
					.transform(r -> {
						List<Node> nodeList = (List<Node>) r;
						List<City> result = Lists.newArrayList();
						nodeList.forEach(y -> {
							String str = y.getTextContent();
							String[] city = str.split(",");
							result.add(City.builder().name(city[0]).code(city[1]).build());
						});
						return result;
					})
					.log(LoggingHandler.Level.DEBUG).channel(c -> c.queue("resultChannel"));
		}
	}
}
