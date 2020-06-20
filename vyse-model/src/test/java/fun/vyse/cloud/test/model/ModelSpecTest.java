package fun.vyse.cloud.test.model;

import fun.vyse.cloud.model.constant.DataType;
import fun.vyse.cloud.model.constant.ConnectionType;
import fun.vyse.cloud.model.entity.*;
import fun.vyse.cloud.model.service.*;
import fun.vyse.cloud.test.ApplicationTest;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.testng.annotations.Test;

import java.util.List;

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

	@Autowired
	private ModelActService modelActService;

	@Autowired
	private PropertyActService propertyActService;

	@Autowired
	private ConnectionActService connectionActService;

	@Test
	public void test(){
		ModelSpec customerModel = ModelSpec.builder().code("customer").name("客户信息").build();
		customerModel = modelSpecService.save(customerModel);

		PropertySpec nameProperty = PropertySpec.builder().code("name").name("姓名").dataType(DataType.String.name()).build();
		nameProperty = propertySpecService.save(nameProperty);

		ConnectionSpec connectionSpec = ConnectionSpec.builder()
				.parentId(customerModel.getId()).parentType(ConnectionType.Model.name())
				.subId(nameProperty.getId()).subType(ConnectionType.Property.name())
				.build();
		connectionSpec = connectionSpecService.save(connectionSpec);
		log.info("model:{}",connectionSpec);

		ModelAct modelAct = ModelAct.builder().code(customerModel.getCode()).name(customerModel.getName()).build();
		modelAct = modelActService.save(modelAct);
		PropertyAct propertyAct = PropertyAct.builder().id1(nameProperty.getId()).value1("托尼").build();
		propertyAct = propertyActService.save(propertyAct);

		ConnectionAct connectionAct = ConnectionAct.builder()
				.parentId(modelAct.getId()).parentType(ConnectionType.Model.name())
				.subId(propertyAct.getId()).subType(ConnectionType.Property.name())
				.build();
		connectionActService.save(connectionAct);
	}

	@Test
	public void query(){
		List<ConnectionSpec> connection = connectionSpecService.getConnection(2, ConnectionType.Model);
		log.info("connection:{}", JSONValue.toJSONString(connection));
	}



	@Configuration
	@EnableAutoConfiguration
	public static class Config{

	}
}
