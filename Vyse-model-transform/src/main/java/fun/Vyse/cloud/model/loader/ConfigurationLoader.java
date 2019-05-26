package fun.Vyse.cloud.model.loader;


import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fun.Vyse.cloud.model.domain.Configuration;
import fun.Vyse.cloud.model.domain.Converter;
import fun.Vyse.cloud.model.domain.Field;
import fun.Vyse.cloud.model.domain.Mapping;
import fun.Vyse.cloud.model.util.ClassUtils;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 读取xml
 */
@Slf4j
@Data
public class ConfigurationLoader {

    private static final Map<String,Mapping> MAPPING_MAP = Maps.newConcurrentMap();
    private static final Map<String,Converter> CONVERTER_MAP = Maps.newConcurrentMap();
    private static final String MAPPINGS_TAG = "mappings";
    private static final String MAPPING_TAG = "mapping";
    private static final String CONFIGURATION_TAG = "configuration";
    private static final String CUSTOM_CONVERTERS_TAG = "custom-converters";
    private static final String TYPE_ATTR = "type";
    private static final String CONVERTER_TAG = "converter";
    private static final String CLASS_A_TAG = "class-a";
    private static final String CLASS_B_TAG = "class-b";
    private static final String FIELD_TAG = "field";
    private static final String A_TAG = "a";
    private static final String B_TAG = "b";



    public void load(Resource[] resource) {
        for (int i = 0; i < resource.length; i++) {
            if (resource[i].exists()) {
                try {
                    SAXReader reader = new SAXReader();
                    Document document = reader.read(resource[i].getFile());
                    Element mappingsEl = document.getRootElement();
                    if(mappingsEl!=null){
                        if(mappingsEl.getName().equals(MAPPINGS_TAG)){
                            Configuration configuration = new Configuration();
                            Element configurationEl = mappingsEl.element(CONFIGURATION_TAG);
                            if(configurationEl!=null){
                                Element custom_convertersEl = configurationEl.element(CUSTOM_CONVERTERS_TAG);
                                List<Converter> converters = Lists.newArrayList();
                                Iterator<Element> iterators = custom_convertersEl.elementIterator(CONVERTER_TAG);
                                while (iterators.hasNext()){
                                    Element converterEl = iterators.next();
                                    Converter converter = new Converter();
                                    String type = getAttributeValue(converterEl,TYPE_ATTR);
                                    converter.setType(type);
                                    converter.setClassA(getElementValue(converterEl,CLASS_A_TAG));
                                    converter.setClassB(getElementValue(converterEl,CLASS_B_TAG));
                                    converters.add(converter);
                                    Class A = ClassUtils.getClass(converter.getClassA());
                                    Class B = ClassUtils.getClass(converter.getClassB());
                                    CONVERTER_MAP.put(A.getTypeName()+"_"+B.getTypeName(),converter);
                                }
                                configuration.setCustomConverters(converters);
                                log.debug("converter:{}",CONVERTER_MAP);
                            }
                            Element mappingEl = mappingsEl.element(MAPPING_TAG);
                            if(mappingEl!=null){
                                Mapping mapping = new Mapping();
                                mapping.setClassA(getElementValue(mappingEl,CLASS_A_TAG));
                                mapping.setClassB(getElementValue(mappingEl,CLASS_B_TAG));
                                Iterator<Element> fieldsEl = mappingEl.elementIterator(FIELD_TAG);
                                List<Field> fields = Lists.newArrayList();
                                while (fieldsEl.hasNext()){
                                    Field field = new Field();
                                    Element fieldEl = fieldsEl.next();
                                    field.setA(getElementValue(fieldEl,A_TAG));
                                    field.setB(getElementValue(fieldEl,B_TAG));
                                    fields.add(field);
                                }
                                mapping.setFields(fields);
                                Class A = ClassUtils.getClass(mapping.getClassA());
                                Class B = ClassUtils.getClass(mapping.getClassB());
                                MAPPING_MAP.put(A.getTypeName()+"_"+B.getTypeName(),mapping);
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }
        log.debug("{}",MAPPING_MAP);
    }


    private String getElementValue(@NonNull Element el,String key){
        Element element = el.element(key);
        if(element!=null){
            return element.getStringValue();
        }
        return null;
    }

    private String getElementValue(@NonNull Element el){
       return el.getStringValue();
    }

    private String getAttributeValue(@NonNull Element el,String key){
        Attribute attribute = el.attribute(key);
        if(attribute!=null){
            return attribute.getStringValue();
        }
        return null;
    }

    private String getAttributeValue(Attribute el){
        return el.getStringValue();
    }

    public Mapping getMapping(String key){return MAPPING_MAP.get(key);}

    public Converter getConverter(String key){return CONVERTER_MAP.get(key);}

    public List<Mapping> getMappings(){
        List<String> keys = MAPPING_MAP.keySet().stream().collect(Collectors.toList());
        List<Mapping> mappings = Lists.newArrayList();
        for (int i = 0; i < keys.size(); i++) {
            mappings.add(MAPPING_MAP.get(keys.get(i)));
        }
        return mappings;
    }

}
