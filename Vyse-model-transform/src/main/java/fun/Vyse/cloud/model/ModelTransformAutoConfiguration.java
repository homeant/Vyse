package fun.Vyse.cloud.model;

import com.google.common.collect.Lists;
import fun.Vyse.cloud.core.orika.OrikaAutoConfiguration;
import fun.Vyse.cloud.core.orika.OrikaMapperFactoryBuilderConfigurer;
import fun.Vyse.cloud.core.orika.OrikaMapperFactoryConfigurer;
import fun.Vyse.cloud.model.converter.DateConverter;
import fun.Vyse.cloud.model.domain.Configuration;
import fun.Vyse.cloud.model.domain.Field;
import fun.Vyse.cloud.model.domain.Mapper;
import fun.Vyse.cloud.model.domain.MapperMap;
import fun.Vyse.cloud.model.util.ClassUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.FieldMapBuilder;
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
    private ModelTransformProperties transformProperties;
    private MapperMap mapperMap;

    public ModelTransformAutoConfiguration(ModelTransformProperties properties){
        this.transformProperties = properties;
        Resource[] resources = transformProperties.resolveMappingLocations();
        mapperMap = new MapperMap();
        mapperMap.setMappers(Lists.newArrayList());
        for (int i = 0; i < resources.length; i++) {
            Resource resource = resources[i];
            MapperMap mapper = buiderMapper(resource);
            if(mapper.getConfiguration()!=null){
                mapperMap.setConfiguration(mapper.getConfiguration());
            }
            mapperMap.getMappers().addAll(mapper.getMappers());
        }
        log.debug("mapperMap:{}",mapperMap);
    }

    private MapperMap buiderMapper(Resource resource){
        MapperMap mapperMap = new MapperMap();
        try {
            if(resource.isFile()){
                SAXReader reader = new SAXReader();
                Document document = reader.read(resource.getFile());
                Element rootEl = document.getRootElement(); // 获取根节点
                Element configurationEl = rootEl.element("configuration");
                if(configurationEl!=null){
                    Element format = configurationEl.element("date-format");
                    if(format !=null){
                        Configuration configuration = new Configuration();
                        configuration.setDateFormat(format.getStringValue());
                        mapperMap.setConfiguration(configuration);
                    }
                }
                Iterator<Element> mappersEl = rootEl.elementIterator("mapper");
                List<Mapper> mappers = Lists.newArrayList();
                while (mappersEl.hasNext()) {
                    Element mapperEl = mappersEl.next();
                    Element classAEl = mapperEl.element("class-a");
                    Element classBEl = mapperEl.element("class-b");
                    Mapper mapper = new Mapper();
                    mapper.setClassA(classAEl.getStringValue());
                    mapper.setClassB(classBEl.getStringValue());
                    List<Field> fields = Lists.newArrayList();
                    Element fieldsEl = mapperEl.element("fields");
                    Iterator<Element> fieldEls = fieldsEl.elementIterator("field");
                    while (fieldEls.hasNext()){
                        Field field = new Field();
                        Element fieldEl = fieldEls.next();
                        Element aEl = fieldEl.element("a");
                        Element bEl = fieldEl.element("b");
                        field.setA(aEl.getStringValue());
                        field.setB(bEl.getStringValue());
                        Attribute aformatAttr = aEl.attribute("date-format");
                        Attribute bformatAttr = bEl.attribute("date-format");
                        if(aformatAttr!=null){
                            String format = aformatAttr.getValue();
                            if(!StringUtils.isEmpty(format)){
                                field.setDateFormat(format);
                            }
                        }else if(bformatAttr!=null){
                            String format = bformatAttr.getValue();
                            if(!StringUtils.isEmpty(format)){
                                field.setDateFormat(format);
                            }
                        }
                        fields.add(field);
                    }
                    mapper.setFields(fields);
                    mappers.add(mapper);
                }
                mapperMap.setMappers(mappers);
            }
        }catch (Exception e){
            log.error("{}",e);
            throw new RuntimeException(e);
        }
        return mapperMap;
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
                mapperMap.getMappers().forEach(mapper -> {
                    try {
                        ClassMapBuilder builder = factory.classMap(ClassUtils.getClass(mapper.getClassA()), ClassUtils.getClass(mapper.getClassB()));
                        mapper.getFields().forEach(field -> {
                            if(StringUtils.isEmpty(field.getDateFormat())){
                                builder.field(field.getA(),field.getB());
                            }else{
                                builder.fieldMap(field.getA(),field.getB()).converter("dataToStr").add();
                            }
                        });
                        builder.byDefault().register();
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                });
            }
        };
        log.debug("factoryConfigurer:{}",factoryConfigurer);
        return  factoryConfigurer;
    }
}
