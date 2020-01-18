package fun.vyse.cloud.test.model;

import fun.vyse.cloud.model.constant.DataType;
import fun.vyse.cloud.model.constant.ModelType;
import fun.vyse.cloud.model.entity.ConnectionSpec;
import fun.vyse.cloud.model.entity.ModelSpec;
import fun.vyse.cloud.model.entity.PropertySpec;
import fun.vyse.cloud.model.service.ConnectionSpecService;
import fun.vyse.cloud.model.service.ModelSpecService;
import fun.vyse.cloud.model.service.PropertySpecService;
import fun.vyse.cloud.test.ApplicationTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.testng.annotations.Test;

/**
 * ModelTest
 *
 * @author junchen
 * @date 2020-01-15 22:43
 */
@Slf4j
public class ModelSpecTest extends ApplicationTest {

	@Autowired
	private ModelSpecService modelSpecService;

	@Autowired
	private PropertySpecService propertySpecService;

	@Autowired
	private ConnectionSpecService connectionSpecService;

	@Test
	public void test(){
		ModelSpec customerModel = ModelSpec.builder().code("customer").name("客户信息").build();
		customerModel = modelSpecService.save(customerModel);

		PropertySpec nameProperty = PropertySpec.builder().code("name").name("姓名").dataType(DataType.String.name()).build();
		nameProperty = propertySpecService.save(nameProperty);

		ConnectionSpec connectionSpec = ConnectionSpec.builder()
				.parentId(customerModel.getId()).parentType(ModelType.Model.name())
				.subId(nameProperty.getId()).subType(ModelType.Property.name())
				.build();
		connectionSpec = connectionSpecService.save(connectionSpec);
		log.info("model:{}",connectionSpec);
	}

	@Configuration
	@EnableAutoConfiguration
	public static class Config{

	}
}
