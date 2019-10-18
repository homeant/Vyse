package fun.vyse.cloud.integration.config;

import lombok.Data;
import org.springframework.expression.Expression;
import org.springframework.http.HttpMethod;

import java.util.Map;

/**
 * @author junchen
 * @Description http配置类
 * @date 2019-10-07 08:59
 */
@Data
public class HttpIntegrationConfig extends AbstractIntegrationConfig {

	private HttpMethod method;

	private String Url;

	private Map<String, Object> headers;

	private String charset;

	private boolean encodeUri;

	private Map<String, Expression> uriVariable;

}
