package fun.vyse.cloud.integration.config;

import lombok.Data;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.List;

/**
 * <p>title：</p>
 * <p>descript：配置基类</p>
 *
 * @author junchen
 * @date 2019/10/7
 */
@Data
public abstract class AbstractIntegrationConfig implements InergrationConfig{

	private boolean useTemplate;

	private String templatePath;

	private Class<?> responseType;

	private Class<?> requestType;

	private List<ChannelInterceptor> interceptors;

}
