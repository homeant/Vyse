package fun.vyse.cloud.test.model;

import com.google.common.collect.Maps;
import fun.vyse.cloud.base.service.IGeneratorService;
import fun.vyse.cloud.define.domain.DomainEntity;
import fun.vyse.cloud.define.domain.DomainModel;
import fun.vyse.cloud.define.domain.MetaDefinition;
import fun.vyse.cloud.define.entity.specification.SpecConnectionEO;
import fun.vyse.cloud.define.entity.specification.SpecFixedModelEO;
import fun.vyse.cloud.define.entity.specification.SpecModelEO;
import fun.vyse.cloud.define.entity.specification.SpecPropertyEO;
import fun.vyse.cloud.define.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
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
	private ISpecModelService specModelService;

	@Autowired
	private ISpecPropertyService specPropertyService;

	@Autowired
	private ISpecConnectionService specConnectionService;

	@Autowired
	private ISpecFixedModelService specFixedModelService;

	@Autowired
	private IMetaDefinitionService metaDefinitionService;

	@Autowired
	private IDomainEntityService domainEntityService;

	@Autowired
	private IDomainModelService domainModelService;

	@Test
	public void insert() {
		SpecModelEO modelEO = new SpecModelEO();
		modelEO.setId(1L);
		modelEO.setName("测试模型");
		modelEO.setCode("main");
		modelEO.setType("Product");
		modelEO.setFixedId(1L);
		modelEO.setVersion_(0L);
		specModelService.saveAndFlush(modelEO);

		SpecModelEO modelEO2 = new SpecModelEO();
		modelEO2.setId(2L);
		modelEO2.setName("测试模型2");
		modelEO2.setCode("test");
		modelEO2.setType("Model");
		modelEO2.setVersion_(0L);
		specModelService.saveAndFlush(modelEO2);


		SpecPropertyEO propertyEO = new SpecPropertyEO();
		propertyEO.setId(3L);
		propertyEO.setCode("name");
		propertyEO.setName("名称");
		propertyEO.setDataType("java.lang.String");
		propertyEO.setVersion_(0L);
		specPropertyService.saveAndFlush(propertyEO);

		SpecPropertyEO propertyEO2 = new SpecPropertyEO();
		propertyEO2.setId(4L);
		propertyEO2.setCode("city");
		propertyEO2.setName("城市");
		propertyEO2.setDefValue("北京");
		propertyEO2.setDataType("java.lang.String");
		propertyEO2.setVersion_(0L);
		specPropertyService.saveAndFlush(propertyEO2);

		SpecConnectionEO connectionEO = new SpecConnectionEO();
		connectionEO.setId(5L);
		connectionEO.setParentId(1L);
		connectionEO.setSubId(2L);
		connectionEO.setParentType("Model");
		connectionEO.setSubType("Model");
		connectionEO.setMaxmium(1L);
		connectionEO.setVersion_(0L);
		specConnectionService.saveAndFlush(connectionEO);


		SpecConnectionEO connectionEO1 = new SpecConnectionEO();
		connectionEO1.setId(6L);
		connectionEO1.setParentId(1L);
		connectionEO1.setSubId(3L);
		connectionEO1.setParentType("Model");
		connectionEO1.setSubType("Property");
		connectionEO1.setVersion_(0L);
		specConnectionService.saveAndFlush(connectionEO1);

		SpecConnectionEO connectionEO2 = new SpecConnectionEO();
		connectionEO2.setId(7L);
		connectionEO2.setParentId(2L);
		connectionEO2.setSubId(4L);
		connectionEO2.setParentType("Model");
		connectionEO2.setSubType("Property");
		connectionEO2.setVersion_(0L);
		specConnectionService.saveAndFlush(connectionEO2);

		SpecFixedModelEO fixedModelEO = new SpecFixedModelEO();
		fixedModelEO.setClassName("fun.vyse.cloud.test.entity.UserEO");
		fixedModelEO.setId(1L);
		fixedModelEO.setVersion_(0L);
		specFixedModelService.saveAndFlush(fixedModelEO);
	}

	@Test
	public void test() {
		log.debug("bean:{}", specModelService.findOne(1L));
		MetaDefinition<Long> metaDefinition = metaDefinitionService.getMetaDefinition(null);
		DomainEntity entity = new DomainEntity();
		DomainModel model = new DomainModel(null,metaDefinition);
		entity.setModel(model);
		log.debug("model:{}",model);
	}

	@Test
	public void domainEntityTest(){
		DomainModel domainModel = domainModelService.createDomainModel(1L, Maps.newHashMap());
		log.info("model:{}",domainModel);
	}

	@Autowired
	private IGeneratorService generatorService;

	@Test
	public void generatorTest(){
		for (int i = 0; i < 10; i++) {
			Object value = generatorService.get("test", null);
			log.info("value{}:{}",i+1,value);
		}
		for (int i = 0; i < 2; i++) {
			Long value = (Long) generatorService.get("pkId", null);
			log.info("pkId{}:{}",i+1,value);
		}
	}

	@Configuration
	//@ComponentScan(basePackages = {"fun.vyse.cloud.define.test"})
	@EnableAutoConfiguration
	@EnableTransactionManagement
	public static class Config {

	}
}
