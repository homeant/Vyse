package fun.vyse.cloud.model.boot;

import fun.vyse.cloud.model.service.ConnectionSpecService;
import fun.vyse.cloud.model.service.ModelSpecService;
import fun.vyse.cloud.model.service.PropertySpecService;
import fun.vyse.cloud.model.service.impl.ConnectionSpecServiceImpl;
import fun.vyse.cloud.model.service.impl.ModelSpecServiceImpl;
import fun.vyse.cloud.model.service.impl.PropertySpecServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * ModelAutoConfiguration
 *
 * @author junchen
 * @date 2020-01-15 22:50
 */
@Slf4j
@Configuration
@EntityScan(basePackages = {"fun.vyse.cloud.core.domain","fun.vyse.cloud.model.entity"})
@EnableJpaRepositories(basePackages = {"fun.vyse.cloud.model.repository"})
public class ModelAutoConfiguration {
	@Bean
	public ModelSpecService modelSpecService(){
		return new ModelSpecServiceImpl();
	}

	@Bean
	public PropertySpecService propertySpecService(){
		return new PropertySpecServiceImpl();
	}

	@Bean
	public ConnectionSpecService connectionSpecService(){
		return new ConnectionSpecServiceImpl();
	}
}
