package fun.vyse.cloud.model.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
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

}
