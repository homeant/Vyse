package fun.Vyse.cloud.model;

import com.google.common.collect.Lists;
import fun.Vyse.cloud.core.orika.OrikaAutoConfiguration;
import fun.Vyse.cloud.core.orika.OrikaMapperFactoryBuilderConfigurer;
import fun.Vyse.cloud.core.orika.OrikaMapperFactoryConfigurer;
import fun.Vyse.cloud.model.converter.DateConverter;
import fun.Vyse.cloud.model.domain.Configuration;
import fun.Vyse.cloud.model.domain.Field;
import fun.Vyse.cloud.model.domain.Mapping;
import fun.Vyse.cloud.model.loader.ConfigurationLoader;
import fun.Vyse.cloud.model.util.ClassUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;



@Data
@Slf4j
@AutoConfigureBefore(OrikaAutoConfiguration.class)
@ConditionalOnProperty(name = "vyse.model.enabled", matchIfMissing = true)
@EnableConfigurationProperties(ModelTransformProperties.class)
public class ModelTransformAutoConfiguration implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private final ModelTransformProperties transformProperties;

    public ModelTransformAutoConfiguration(ModelTransformProperties properties){
        this.transformProperties = properties;
        ConfigurationLoader loader = new ConfigurationLoader();
        loader.load(properties.resolveMappingLocations());
    }

    @Bean
    public OrikaMapperFactoryBuilderConfigurer orikaMapperFactoryBuilderConfigurer(){
        OrikaMapperFactoryBuilderConfigurer orikaMapperFactoryBuilderConfigurer = new OrikaMapperFactoryBuilderConfigurer(){
            /**
             * Configures the {@link DefaultMapperFactory.MapperFactoryBuilder}.
             *
             * @param mapperFactoryBuilder the {@link DefaultMapperFactory.MapperFactoryBuilder}.
             */
            @Override
            public void configure(DefaultMapperFactory.MapperFactoryBuilder<?, ?> mapperFactoryBuilder) {
                log.debug("config：{}",mapperFactoryBuilder);
                DateConverter dateConverter = new DateConverter("yyyy-MM-dd");
                mapperFactoryBuilder.build().getConverterFactory().registerConverter("dataToStr",dateConverter);
            }
        };
        return orikaMapperFactoryBuilderConfigurer;
    }

    @Bean
    public OrikaMapperFactoryConfigurer orikaMapperFactoryConfigurer(){
        OrikaMapperFactoryConfigurer factoryConfigurer = new OrikaMapperFactoryConfigurer(){
            /**
             * Configures the {@link MapperFactory}.
             *
             * @param factory the {@link MapperFactory}.
             */
            @Override
            public void configure(MapperFactory factory) {
//                mapperMap.getMappers().forEach(mapper -> {
//                    try {
//                        ClassMapBuilder builder = factory.classMap(ClassUtils.getClass(mapper.getClassA()), ClassUtils.getClass(mapper.getClassB()));
//                        mapper.getFields().forEach(field -> {
//                            if(StringUtils.isEmpty(field.getDateFormat())){
//                                builder.field(field.getA(),field.getB());
//                            }else{
//                                builder.fieldMap(field.getA(),field.getB()).converter("dataToStr").add();
//                            }
//                        });
//                        builder.byDefault().register();
//                    }catch (Exception e){
//                        throw new RuntimeException(e);
//                    }
//                });
            }
        };
        log.debug("factoryConfigurer:{}",factoryConfigurer);
        return  factoryConfigurer;
    }
}
