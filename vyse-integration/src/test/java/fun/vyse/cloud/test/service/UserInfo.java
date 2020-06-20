package fun.vyse.cloud.test.service;

import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowDefinition;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.http.dsl.Http;
import org.springframework.stereotype.Component;

/**
 * UserInfo
 *
 * @author junchen
 * @date 2020-01-16 22:43
 */
public class UserInfo implements IntegrationFlow {
	@Override
	public void configure(IntegrationFlowDefinition<?> integrationFlowDefinition) {
		integrationFlowDefinition.handle(Http.outboundGateway("https://api.github.com/users/{name}")
				.httpMethod(HttpMethod.GET)
				.expectedResponseType(String.class)
				.uriVariable("name", r -> r.getPayload()))
				.log(LoggingHandler.Level.DEBUG).bridge();
	}
}
