/*
 *  Copyright (c) 2011-2014, junchen (junchen1314@foxmail.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

package fun.vyse.cloud.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import fun.vyse.cloud.core.domain.IFixedEntity;
import fun.vyse.cloud.define.domain.DomainEntity;
import fun.vyse.cloud.define.domain.DomainModel;
import fun.vyse.cloud.define.domain.MetaDefinition;
import fun.vyse.cloud.define.domain.MetaDefinitionFactory;
import fun.vyse.cloud.define.entity.*;
import fun.vyse.cloud.define.service.IMetaDefinitionService;
import fun.vyse.cloud.test.impl.MetaDefinitionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * com.ifa.cloud.channel.test.DefineModelTest
 *
 * @Author junchen homeanter@163.com
 * @Date 2019-10-14 11:39
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"logging.level.fun.vyse.cloud=debug"})
public class DefineEntityTest {

	private static ModelDataEO dataEO = new ModelDataEO();
	private static DomainModel domainModel = null;

	private static ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private IMetaDefinitionService metaDefinitionService;

	@Before
	public void init() {
		dataEO.setId(8L);
		dataEO.setDomainId(1L);
		dataEO.setDomainCode("main");
		dataEO.setFixedId(1L);
		MetaDefinition<Long> metaDefinition = metaDefinitionService.getMetaDefinition("");
		domainModel = new DomainModel(dataEO, metaDefinition);
		domainModel.setTenantId("");
		FixedModeEO fixedModelEO = metaDefinition.getFixedModelEO(1L);
		Object fixedInstance = fixedModelEO.createFixedInstance();
		domainModel.setFixedModel((IFixedEntity) fixedInstance);

		ModelEO modelEO = new ModelEO();
		PropertyEO propertyEO = new PropertyEO();
		propertyEO.setId(4L);
		propertyEO.setCode("city");
		Assert.assertFalse(metaDefinition.isFixed(modelEO, propertyEO));
		/**
		 * 子级模型
		 */
//		ModelPropertyEO modelPropertyEO = new ModelPropertyEO();
//		modelPropertyEO.setId(10L);
//		modelPropertyEO.setDomainId(1L);
//		modelPropertyEO.setParentId(2L);
//		modelPropertyEO.setPropertyId(3L);
//		modelPropertyEO.setTopId(8L);
//		modelPropertyEO.setCode("city");
//		modelPropertyEO.setValue("北京");
//		domainModel.put(modelPropertyEO);
	}

	@Test
	public void test() throws JsonProcessingException {
		DomainEntity domainEntity = new DomainEntity(domainModel);
		DomainModel model = domainEntity.getModel();
		Map<String, Object> data = Maps.newHashMap();

		//data.put("id", 8L);
		data.put("domainId", 1L);
		data.put("domainCode", "main");
		data.put("name", "tom");
		model.setData(data);
		log.debug("role:{}", mapper.writeValueAsString(model));
	}

	@Configuration
	@EnableAutoConfiguration
	public static class Config {

		@Bean
		public IMetaDefinitionService metaDefinitionService() {
			return new MetaDefinitionServiceImpl();
		}

		@Bean
		MetaDefinitionFactory factory(IMetaDefinitionService metaDefinitionService) {
			return new MetaDefinitionFactory(metaDefinitionService);
		}
	}
}
