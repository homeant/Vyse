package fun.vyse.cloud.test.model;

import fun.vyse.cloud.define.domain.DomainEntity;
import fun.vyse.cloud.define.domain.DomainModel;
import fun.vyse.cloud.define.domain.MetaDefinition;
import fun.vyse.cloud.define.domain.MetaDefinitionFactory;
import fun.vyse.cloud.define.entity.ConnectionEO;
import fun.vyse.cloud.define.entity.FixedModelEO;
import fun.vyse.cloud.define.entity.ModelEO;
import fun.vyse.cloud.define.entity.PropertyEO;
import fun.vyse.cloud.define.repository.IModelPropertyRepository;
import fun.vyse.cloud.define.repository.IModelRepository;
import fun.vyse.cloud.define.service.*;
import fun.vyse.cloud.define.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.framework.qual.RequiresQualifier;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * ModelTest
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 09:49
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ModelTest {

	@Autowired
	private IModelService modelService;

	@Autowired
	private IPropertyService propertyService;

	@Autowired
	private IConnectionService connectionService;

	@Autowired
	private IFixedModelService fixedModelService;

	@Autowired
	private IMetaDefinitionService metaDefinitionService;

	@Ignore
	@Test
	public void insert() {
		ModelEO modelEO = new ModelEO();
		modelEO.setId(1L);
		modelEO.setName("测试模型");
		modelEO.setCode("main");
		modelEO.setType("Product");
		modelEO.setFixedId(1L);
		modelEO.setVersion(0L);
		modelService.saveAndFlush(modelEO);

		ModelEO modelEO2 = new ModelEO();
		modelEO2.setId(2L);
		modelEO2.setName("测试模型2");
		modelEO2.setCode("test");
		modelEO2.setType("Model");
		modelEO2.setVersion(0L);
		modelService.saveAndFlush(modelEO2);


		PropertyEO propertyEO = new PropertyEO();
		propertyEO.setId(3L);
		propertyEO.setCode("name");
		propertyEO.setName("名称");
		propertyEO.setDataType("java.lang.String");
		propertyEO.setVersion(0L);
		propertyService.saveAndFlush(propertyEO);

		PropertyEO propertyEO2 = new PropertyEO();
		propertyEO2.setId(4L);
		propertyEO2.setCode("city");
		propertyEO2.setName("城市");
		propertyEO2.setDefValue("北京");
		propertyEO2.setDataType("java.lang.String");
		propertyEO2.setVersion(0L);
		propertyService.saveAndFlush(propertyEO2);

		ConnectionEO connectionEO = new ConnectionEO();
		connectionEO.setId(5L);
		connectionEO.setParentId(1L);
		connectionEO.setSubId(2L);
		connectionEO.setParentType("Model");
		connectionEO.setSubType("Model");
		connectionEO.setMaxmium(1L);
		connectionEO.setVersion(0L);
		connectionService.saveAndFlush(connectionEO);


		ConnectionEO connectionEO1 = new ConnectionEO();
		connectionEO1.setId(6L);
		connectionEO1.setParentId(1L);
		connectionEO1.setSubId(3L);
		connectionEO1.setParentType("Model");
		connectionEO1.setSubType("Property");
		connectionEO1.setVersion(0L);
		connectionService.saveAndFlush(connectionEO1);

		ConnectionEO connectionEO2 = new ConnectionEO();
		connectionEO2.setId(7L);
		connectionEO2.setParentId(2L);
		connectionEO2.setSubId(4L);
		connectionEO2.setParentType("Model");
		connectionEO2.setSubType("Property");
		connectionEO2.setVersion(0L);
		connectionService.saveAndFlush(connectionEO2);

		FixedModelEO fixedModelEO = new FixedModelEO();
		fixedModelEO.setClassName("fun.vyse.cloud.test.entity.UserEO");
		fixedModelEO.setId(1L);
		fixedModelEO.setVersion(0L);
		fixedModelService.saveAndFlush(fixedModelEO);
	}

	@Test
	public void test() {
		log.debug("bean:{}", modelService.findOne(1L));
		MetaDefinition<Long> metaDefinition = metaDefinitionService.getMetaDefinition(null);
		DomainEntity entity = new DomainEntity();
		DomainModel model = new DomainModel(null,metaDefinition);
		entity.setModel(model);
		log.debug("model:{}",model);
	}

	@Configuration
	//@ComponentScan(basePackages = {"fun.vyse.cloud.define.test"})
	@EnableAutoConfiguration
	@EnableTransactionManagement
	public static class Config {

	}
}
