package fun.vyse.cloud.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fun.vyse.cloud.test.domain.Address;
import fun.vyse.cloud.test.domain.User;
import fun.vyse.cloud.test.domain.UserDto;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.map.MultiKeyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * BeanTest
 *
 * @author junchen
 * @date 2019-12-21 22:58
 */
@Slf4j
@SpringBootTest
public class BeanTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ScriptEngine scriptEngine;

	@Autowired
	private MapperFacade mapperFacade;

	@Test
	public void evalTest() throws ScriptException {
		//Bindings bindings = scriptEngine.createBindings();
		//bindings.putAll(beanMap);
		//Object value = scriptEngine.eval("addresses[0].name = 1", bindings);
		BeanMap beanMap = BeanMap.create(new User());
		Map<String, Object> filedMap = Maps.newHashMap();
		filedMap.put("name", "jie");
		filedMap.put("addressList.address", "123");
		filedMap.put("addressList.name", "name");
		filedMap.put("userInfo.name", "tom");
		for (Map.Entry<String, Object> entry : filedMap.entrySet()) {
			String path = entry.getKey();
			Object value = entry.getValue();
			if (path.indexOf(".") > -1) {
				String prefix = path.substring(0, path.indexOf("."));
				Class propertyType = beanMap.getPropertyType(prefix);
				Object result = beanMap.get(prefix);
				if (result == null && propertyType != null) {
					if (propertyType == List.class) {
						result = Lists.newArrayList();
						Object bean = beanMap.getBean();
						try {
							Field field = bean.getClass().getDeclaredField(prefix);
							Type genericType = field.getGenericType();
							if (genericType != null) {
								if (genericType instanceof ParameterizedType) {
									ParameterizedType parameterizedType = (ParameterizedType) genericType;
									Class actualType = (Class) parameterizedType.getActualTypeArguments()[0];
									Object actual = actualType.newInstance();
									List list = (List) result;
									list.add(actual);
								}
							}
						} catch (NoSuchFieldException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						}
						beanMap.put(prefix, result);
					} else {
						try {
							Object o = propertyType.newInstance();
							beanMap.put(prefix, o);
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				beanMap.put(path, value);
			}
		}
		log.debug("result:{}", beanMap.getBean());
	}

	@Configuration
	@EnableAutoConfiguration
	public static class Config {
		@Bean
		public ScriptEngine scriptEngine() {
			ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
			return scriptEngineManager.getEngineByName("groovy");
		}
	}
}
