package fun.vyse.cloud.orika;

import fun.vyse.cloud.orika.converter.LocalDateConverter;
import fun.vyse.cloud.orika.converter.LocalDateTimeConverter;
import fun.vyse.cloud.orika.converter.LocalTimeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@ConditionalOnClass(MapperFactory.class)
@Configuration
@ConditionalOnProperty(name = "orika.enabled", matchIfMissing = true)
@RequiredArgsConstructor
@EnableConfigurationProperties(OrikaProperties.class)
public class OrikaAutoConfiguration {

    /**
     * The configuration properties for Orika.
     */
    private final OrikaProperties orikaProperties;

    /**
     * The configurers for {@link DefaultMapperFactory.MapperFactoryBuilder}.
     */
    private final Optional<List<OrikaMapperFactoryBuilderConfigurer>> orikaMapperFactoryBuilderConfigurers;

    /**
     * The configurers for {@link MapperFactory}.
     */
    private final Optional<List<OrikaMapperFactoryConfigurer>> orikaMapperFactoryConfigurers;

    @ConditionalOnMissingBean
    @Bean
    public DefaultMapperFactory.MapperFactoryBuilder<?, ?> orikaMapperFactoryBuilder() {
        DefaultMapperFactory.Builder orikaMapperFactoryBuilder = new DefaultMapperFactory.Builder();
        if (orikaProperties.getUseBuiltinConverters() != null) {
            orikaMapperFactoryBuilder.useBuiltinConverters(orikaProperties.getUseBuiltinConverters());
        }
        if (orikaProperties.getUseAutoMapping() != null) {
            orikaMapperFactoryBuilder.useAutoMapping(orikaProperties.getUseAutoMapping());
        }
        if (orikaProperties.getMapNulls() != null) {
            orikaMapperFactoryBuilder.mapNulls(orikaProperties.getMapNulls());
        }
        if (orikaProperties.getDumpStateOnException() != null) {
            orikaMapperFactoryBuilder.dumpStateOnException(orikaProperties.getDumpStateOnException());
        }
        if (orikaProperties.getFavorExtension() != null) {
            orikaMapperFactoryBuilder.favorExtension(orikaProperties.getFavorExtension());
        }
        if (orikaProperties.getCaptureFieldContext() != null) {
            orikaMapperFactoryBuilder.captureFieldContext(orikaProperties.getCaptureFieldContext());
        }
        orikaMapperFactoryBuilderConfigurers
                .orElseGet(Collections::emptyList)
                .forEach(configurer -> configurer.configure(orikaMapperFactoryBuilder));
        return orikaMapperFactoryBuilder;
    }

    /**
     * Creates a {@link MapperFactory}.
     *
     * @param mapperFactoryBuilder the {@link DefaultMapperFactory.MapperFactoryBuilder}.
     * @return a {@link MapperFactory}.
     */
    @ConditionalOnMissingBean
    @Bean
    public MapperFactory orikaMapperFactory(DefaultMapperFactory.MapperFactoryBuilder<?, ?> mapperFactoryBuilder) {
        MapperFactory orikaMapperFactory = mapperFactoryBuilder.build();
        orikaMapperFactoryConfigurers
                .orElseGet(Collections::emptyList)
                .forEach(configurer -> configurer.configure(orikaMapperFactory));
        ConverterFactory factory = orikaMapperFactory.getConverterFactory();
        factory.registerConverter(new LocalDateTimeConverter());
        factory.registerConverter(new LocalDateConverter());
        factory.registerConverter(new LocalTimeConverter());
        return orikaMapperFactory;
    }

    /**
     * Creates a {@link MapperFacade}.
     *
     * @param mapperFactory the {@link MapperFactory}.
     * @return a {@link MapperFacade}.
     */
    @ConditionalOnMissingBean
    @Bean
    public MapperFacade orikaMapperFacade(MapperFactory mapperFactory) {
        MapperFacade orikaMapperFacade = mapperFactory.getMapperFacade();
        return orikaMapperFacade;
    }

}
