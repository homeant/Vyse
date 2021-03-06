package fun.vyse.cloud.base.boot;

import fun.vyse.cloud.base.service.IGeneratorService;
import fun.vyse.cloud.base.service.impl.GeneratorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * BaseAutoConfiguration
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-30 11:43
 */
@Slf4j
@Configuration
@EntityScan(basePackages = {"fun.vyse.cloud.core.domain","fun.vyse.cloud.base.entity"})
@EnableJpaRepositories(basePackages = {"fun.vyse.cloud.core.repository","fun.vyse.cloud.base.repository"})
public class BaseAutoConfiguration {

	public BaseAutoConfiguration(){
		log.info("BaseAutoConfiguration init ...");
	}

	@Bean
	public IGeneratorService generatorService(){
		return new GeneratorServiceImpl();
	}

}
