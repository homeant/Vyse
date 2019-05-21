package fun.Vyse.cloud.model;

import fun.Vyse.cloud.model.domain.Mapping;
import fun.Vyse.cloud.model.loader.ConfigurationLoader;
import fun.Vyse.cloud.orika.OrikaMapperFactoryBuilderConfigurer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

import java.util.List;

/**
 * @author tianhui
 * @className ModelMapperConfigurer
 * @description TODO
 * @date 2019/5/21 14:42
 */
@Data
@Slf4j
public class ModelMapperConfigurer implements OrikaMapperFactoryBuilderConfigurer {

    private final ConfigurationLoader configurationLoader;

    /**
     * Configures the {@link DefaultMapperFactory.MapperFactoryBuilder}.
     *
     * @param orikaMapperFactoryBuilder the {@link DefaultMapperFactory.MapperFactoryBuilder}.
     */
    @Override
    public void configure(DefaultMapperFactory.MapperFactoryBuilder<?, ?> orikaMapperFactoryBuilder) {
        DefaultMapperFactory mapperFactory = orikaMapperFactoryBuilder.build();
        ConverterFactory converterFactory = mapperFactory.getConverterFactory();
        List<Mapping> mappings = configurationLoader.getMappings();
        mappings.stream().forEach(r->{
            try {
                Class A = Class.forName(r.getClassA());
                Class B = Class.forName(r.getClassB());
                ClassMapBuilder classMapBuilder = mapperFactory.classMap(A, B);
                r.getFields().stream().forEach(x->{
                    log.debug("field:{}",x);
                    classMapBuilder.field(x.getA(), x.getB());
                });
                classMapBuilder.byDefault().register();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        //defaultMapperFactory.getConverterFactory().registerConverter();
    }
}
