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

package fun.vyse.cloud.design.domain;

import com.google.common.collect.Maps;
import fun.vyse.cloud.design.service.IMetaDefinitionService;

import java.util.Map;

/**
 * fun.vyse.cloud.design.domain.MetaDefinitionFactory
 *
 * @Author junchen homeanter@163.com
 * @Date 2019-10-14 10:31
 */
public class MetaDefinitionFactory {

    private static Map<String, MetaDefinition<Long>> metaDefinitionMap = Maps.newConcurrentMap();

    private final IMetaDefinitionService metaDefinitionService;

    public MetaDefinitionFactory(IMetaDefinitionService metaDefinitionService) {
        this.metaDefinitionService = metaDefinitionService;
    }

    public MetaDefinition getMetaDefinition(String tenantId) {
        if (metaDefinitionMap.containsKey(tenantId)) {
            return metaDefinitionMap.get(tenantId);
        } else {
            MetaDefinition<Long> metaDefinition = metaDefinitionService.getMetaDefinition(tenantId);
            metaDefinitionMap.put(tenantId, metaDefinition);
            return metaDefinition;
        }
    }

    public MetaDefinition getMetaDefinition(){

        return this.getMetaDefinition();
    }
}
