package fun.vyse.cloud.dozer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * @author junchen
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "dozer.enabled", havingValue  = "true")
@EnableConfigurationProperties(DozerProperties.class)
public class DozerAutoConfiguration {

    private final DozerProperties dozerProperties;

    @Bean
    public DozerBeanMapperFactoryBean mapperFactoryBean(){
        DozerBeanMapperFactoryBean factoryBean = new DozerBeanMapperFactoryBean();
        factoryBean.setMappingFiles(this.dozerProperties.resolveMappingLocations());
        return factoryBean;
    }
}
