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

package fun.vyse.cloud.test.impl;

import com.google.common.collect.Lists;
import fun.vyse.cloud.define.domain.MetaDefinition;
import fun.vyse.cloud.define.entity.ConnectionEO;
import fun.vyse.cloud.define.entity.FixedModeEO;
import fun.vyse.cloud.define.entity.ModelEO;
import fun.vyse.cloud.define.entity.PropertyEO;
import fun.vyse.cloud.define.service.IMetaDefinitionService;

import java.util.List;

/**
 * fun.vyse.cloud.define.service.impl.MetaDefinitionServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-14 10:34
 */
public class MetaDefinitionServiceImpl implements IMetaDefinitionService {

    @Override
    public MetaDefinition<Long> getMetaDefinition(String tenantId) {
        List<ModelEO> modelEOS = Lists.newArrayList();
        List<PropertyEO> propertyEOS = Lists.newArrayList();
        List<ConnectionEO> connectionEOS = Lists.newArrayList();
        List<FixedModeEO> fixedModeEOS = Lists.newArrayList();

        ModelEO modelEO = new ModelEO();
        modelEO.setId(1L);
        modelEO.setName("测试模型");
        modelEO.setCode("main");
        modelEO.setType("Product");
        modelEO.setFixedId(1L);
        modelEOS.add(modelEO);

        ModelEO modelEO2 = new ModelEO();
        modelEO2.setId(2L);
        modelEO2.setName("测试模型2");
        modelEO2.setCode("test");
        modelEO2.setType("Model");
        modelEOS.add(modelEO2);

        PropertyEO propertyEO = new PropertyEO();
        propertyEO.setId(3L);
        propertyEO.setCode("name");
        propertyEO.setName("名称");
		propertyEO.setDataType("java.lang.String");
        propertyEOS.add(propertyEO);

        PropertyEO propertyEO2 = new PropertyEO();
        propertyEO2.setId(4L);
        propertyEO2.setCode("city");
        propertyEO2.setName("城市");
        propertyEO2.setDefValue("北京");
        propertyEO2.setDataType("java.lang.String");
        propertyEOS.add(propertyEO2);

        ConnectionEO connectionEO = new ConnectionEO();
        connectionEO.setId(5L);
        connectionEO.setParentId(1L);
        connectionEO.setSubId(2L);
        connectionEO.setParentType("Model");
        connectionEO.setSubType("Model");
        connectionEO.setMaxmium(1L);
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

        FixedModeEO fixedModeEO = new FixedModeEO();
        fixedModeEO.setClassName("fun.vyse.cloud.test.entity.UserEO");
        fixedModeEO.setId(1L);
        fixedModeEOS.add(fixedModeEO);

        MetaDefinition<Long> metaDefinition = new MetaDefinition<>();
        modelEOS.forEach(r -> metaDefinition.addModel(r));
        propertyEOS.forEach(r -> metaDefinition.addProperty(r));
        connectionEOS.forEach(r -> metaDefinition.addConcurrent(r));
        fixedModeEOS.forEach(r->metaDefinition.addFixedModel(r));
        metaDefinition.init();
        return metaDefinition;
    }
}
