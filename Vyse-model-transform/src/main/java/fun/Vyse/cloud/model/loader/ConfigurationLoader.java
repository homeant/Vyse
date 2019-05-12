package fun.Vyse.cloud.model.loader;


import fun.Vyse.cloud.model.domain.Configuration;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Iterator;

/**
 * 读取xml
 */
@Slf4j
public class ConfigurationLoader {

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    private MapperFactory factory;
    private final String MAPPINGS_TAG = "mappings";
    private final String MAPPING_TAG = "mapping";
    private final String CONFIGURATION_TAG = "configuration";
    private final String CUSTOM_CONVERTERS_TAG = "custom-converters";
    private final String TYPE_ATTR = "type";
    private final String CONVERTER_TAG = "converter";
    private final String CLASS_A_TAG = "class-a";
    private final String CLASS_B_TAG = "class-b";
    private final String FIELD_TAG = "field";
    private final String A_TAG = "a";
    private final String B_TAG = "b";


    public void load(String location) {
        Resource[] resources = getResources(location);
        for (int i = 0; i < resources.length; i++) {
            Resource resource = resources[i];
            if (resource.exists()) {
                try {
                    SAXReader reader = new SAXReader();
                    Document document = reader.read(resource.getFile());
                    Element mappingsEl = document.getRootElement();
                    if(mappingsEl!=null){
                        if(mappingsEl.getName().equals(MAPPINGS_TAG)){
                            Configuration configuration = new Configuration();
                            Element mappingEl = mappingsEl.element(MAPPING_TAG);
                            if(mappingEl!=null){
                                Iterator<Element> iterators = mappingEl.elementIterator(CUSTOM_CONVERTERS_TAG);
                                
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    private Resource[] getResources(String location) {
        try {
            return resourceResolver.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }

    public String getElementValue(@NonNull Element el,String key){
        Element element = el.element(key);
        if(element!=null){
            return element.getStringValue();
        }
        return null;
    }

    private String getElementValue(@NonNull Element el){
       return el.getStringValue();
    }

    public String getAttributeValue(@NonNull Element el,String key){
        Attribute attribute = el.attribute(key);
        if(attribute!=null){
            return attribute.getStringValue();
        }
        return null;
    }

    private String getAttributeValue(Attribute el){
        return el.getStringValue();
    }
}
