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
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
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
import org.apache.commons.collections4.CollectionUtils;
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

import java.util.List;
import java.util.Map;

/**
 * com.ifa.cloud.channel.test.DefineModelTest
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-14 11:39
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"logging.level.fun.vyse.cloud=debug"})
public class DefineEntityTest {

	private static ModelDataEO dataEO = new ModelDataEO();
	private static DomainModel domainModel = null;

	private static ObjectMapper mapper = null;

	private static MetaDefinition<Long> metaDefinition = null;

	@Autowired
	private IMetaDefinitionService metaDefinitionService;

	@Before
	public void init() {
		mapper = new ObjectMapper();
		mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY,true);
		dataEO.setId(8L);
		dataEO.setDomainId(1L);
		dataEO.setCode("main");
		dataEO.setFixedId(1L);
		dataEO.setTopId(8L);
		metaDefinition = metaDefinitionService.getMetaDefinition("");
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
	}

	@Test
	public void test() throws JsonProcessingException {
		DomainEntity domainEntity = new DomainEntity(domainModel);
		DomainModel model = domainEntity.getModel();
		Map<String, Object> data = Maps.newHashMap();

		data.put("id", 9L);
		data.put("domainId", 1L);
		data.put("code", "main");
		data.put("name", "tom");
		model.setData(data);
		log.debug("data:{}", mapper.writeValueAsString(model.getData()));

	}

	@Test
	public void childrenModelTest() throws JsonProcessingException {
		DomainEntity domainEntity = new DomainEntity();
		domainEntity.setModel(domainModel);
		log.debug("model:{}", mapper.writeValueAsString(domainModel));


		ModelDataEO childrenData = new ModelDataEO();
		childrenData.setId(9L);
		childrenData.setDomainId(2L);
		childrenData.setParentId(8L);
		childrenData.setTopId(8L);
		childrenData.setCode("test");

		DomainModel children = new DomainModel(childrenData, metaDefinition);
		children.setParentModel(domainModel);

		List<ConnectionEO> connections = metaDefinition.getConnection(2L, 4L);
		if (CollectionUtils.isNotEmpty(connections)) {
			Long id = 10L;
			for (ConnectionEO connection : connections) {
				PropertyEO property = metaDefinition.getProperty(connection.getSubId());
				//属性放置
				ModelPropertyEO modelPropertyEO = new ModelPropertyEO();
				modelPropertyEO.setId(id);
				modelPropertyEO.setDomainId1(property.getId());
				modelPropertyEO.setParentId(9L);
				modelPropertyEO.setTopId(8L);
				modelPropertyEO.setCode1(property.getCode());
				modelPropertyEO.setValue1(property.getDefValue());
				modelPropertyEO.setDateType1(property.getDataType());
				id++;
				children.put(modelPropertyEO);
			}
		}
		domainModel.put(children);

		Map<String, Object> data = Maps.newHashMap();

		data.put("id", 9L);
		data.put("domainId", 1L);
		data.put("code", "main");
		data.put("name", "tom");

		Map<String,Object> chData = Maps.newHashMap();
		data.put("test",chData);
		chData.put("id",10L);
		chData.put("domainId",2L);
		chData.put("code","test");
		chData.put("city","上海");

		domainModel.setData(data);

		DomainModel childrenModel = DefineEntityTest.domainModel.findChildren(DomainModel.class, "test", 0);
		log.debug("children:{}", childrenModel);
		log.debug("data:{}", mapper.writeValueAsString(domainModel.getData()));
	}

	@Configuration
	@EnableAutoConfiguration
	public static class Config {

		@Bean
		public IMetaDefinitionService metaDefinitionService() {
			return new MetaDefinitionServiceImpl();
		}

		@Bean
		public MetaDefinitionFactory factory(IMetaDefinitionService metaDefinitionService) {
			return new MetaDefinitionFactory(metaDefinitionService);
		}
	}
}
