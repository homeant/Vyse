package fun.vyse.cloud.model;


import fun.vyse.cloud.model.loader.ConfigurationLoader;
import fun.vyse.cloud.orika.OrikaAutoConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;


@Data
@Slf4j
@AutoConfigureBefore(OrikaAutoConfiguration.class)
@ConditionalOnProperty(name = "vyse.entity.enabled", havingValue  = "true")
@EnableConfigurationProperties(ModelTransformProperties.class)
public class ModelTransformAutoConfiguration implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private final ModelTransformProperties transformProperties;
    private static final ConfigurationLoader configurationLoader = new ConfigurationLoader();

    public ModelTransformAutoConfiguration(ModelTransformProperties properties){
        this.transformProperties = properties;
        configurationLoader.load(properties.resolveMappingLocations());
    }

    @Bean
    public ConfigurationLoader configurationLoader(){
        return configurationLoader;
    }

    @Bean
    public ModelMapperConfigurer modelMapperConfigurer(){
        return new ModelMapperConfigurer(configurationLoader);
    }
}
