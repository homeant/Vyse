/*
 *
 * Copyright (c) 2011-2014, junchen (junchen1314@foxmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *
 */

package fun.vyse.cloud.design.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fun.vyse.cloud.design.domain.DefineEntity;
import fun.vyse.cloud.design.domain.DefineModel;
import fun.vyse.cloud.design.domain.MetaDefinition;
import fun.vyse.cloud.design.entity.ConnectionEO;
import fun.vyse.cloud.design.entity.ModelDataEO;
import fun.vyse.cloud.design.entity.ModelEO;
import fun.vyse.cloud.design.entity.PropertyEO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * fun.vyse.cloud.design.test.DefineModelTest
 *
 * @Author junchen homeanter@163.com
 * @Date 2019-10-14 11:39
 */
@Slf4j
public class DefineEntityTest {

    private static ModelDataEO dataEO = new ModelDataEO();
    private static MetaDefinition<Long> metaDefinition = new MetaDefinition<>();
    private static DefineModel defineModel = null;

    private static ObjectMapper mapper = new ObjectMapper();

    @Before
    public void init() {
        List<ModelEO> modelEOS = Lists.newArrayList();
        List<PropertyEO> propertyEOS = Lists.newArrayList();
        List<ConnectionEO> connectionEOS = Lists.newArrayList();

        ModelEO modelEO = new ModelEO();
        modelEO.setId(1L);
        modelEO.setName("测试模型");
        modelEO.setCode("main");
        modelEOS.add(modelEO);
        ModelEO modelEO2 = new ModelEO();
        modelEO2.setId(2L);
        modelEO2.setName("测试模型2");
        modelEOS.add(modelEO2);

        PropertyEO propertyEO = new PropertyEO();
        propertyEO.setId(3L);
        propertyEO.setCode("name");
        propertyEO.setName("名称");
        propertyEOS.add(propertyEO);
        PropertyEO propertyEO2 = new PropertyEO();
        propertyEO2.setId(4L);
        propertyEO2.setCode("city");
        propertyEO2.setName("城市");
        propertyEOS.add(propertyEO2);

        ConnectionEO connectionEO = new ConnectionEO();
        connectionEO.setId(5L);
        connectionEO.setParentId(1L);
        connectionEO.setSubId(2L);
        connectionEO.setParentType("Model");
        connectionEO.setSubType("Model");
        connectionEOS.add(connectionEO);


        ConnectionEO connectionEO1 = new ConnectionEO();
        connectionEO1.setId(6L);
        connectionEO1.setParentId(1L);
        connectionEO1.setSubId(3L);
        connectionEO1.setParentType("Model");
        connectionEO1.setSubType("Property");
        connectionEOS.add(connectionEO1);

        ConnectionEO connectionEO2 = new ConnectionEO();
        connectionEO2.setId(7L);
        connectionEO2.setParentId(2L);
        connectionEO2.setSubId(4L);
        connectionEO2.setParentType("Model");
        connectionEO2.setSubType("Property");
        connectionEOS.add(connectionEO2);

        modelEOS.forEach(r -> metaDefinition.addModelMap(r));
        propertyEOS.forEach(r -> metaDefinition.addPropertyMap(r));
        connectionEOS.forEach(r -> metaDefinition.addConcurrentMap(r));
        metaDefinition.init();

        dataEO.setId(8L);
        dataEO.setDefineId(1L);
        dataEO.setDefineCode("main");

        defineModel = new DefineModel(dataEO, metaDefinition);

    }

    @Test
    public void test() throws JsonProcessingException {
        DefineEntity defineEntity = new DefineEntity(defineModel);
        log.debug("md:{}", metaDefinition);
        DefineModel model = defineEntity.getModel();
        log.debug("model:{}", model);
        Map<String,Object> data = Maps.newHashMap();

        data.put("id",8L);
        data.put("defineId",1L);
        data.put("defineCode","main");
        data.put("name","tom");
        model.setData(data);
        log.debug("role:{}",mapper.writeValueAsString(model.getEntity()));
    }
}
