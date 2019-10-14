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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fun.vyse.cloud.core.domain.IFixedEntity;
import fun.vyse.cloud.core.domain.InternalFixedModelEO;
import fun.vyse.cloud.design.entity.ConnectionEO;
import fun.vyse.cloud.design.entity.FixedModeEO;
import fun.vyse.cloud.design.entity.ModelEO;
import fun.vyse.cloud.design.entity.PropertyEO;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * fun.vyse.cloud.design.domain.MetaDefinition
 *
 * @Author junchen homeanter@163.com
 * @Date 2019-10-12 14:55
 */
@Slf4j
public class MetaDefinition<T> implements Serializable {

    /**
     * 模型定义
     */
    private Map<T, ModelEO> modelMap = Maps.newConcurrentMap();

    /**
     * 属性定义
     */
    private Map<T, PropertyEO> propertyMap = Maps.newConcurrentMap();

    /**
     * 连接关系
     */
    private Map<T, Map<T, List<ConnectionEO>>> concurrentsMap = Maps.newConcurrentMap();

    private Map<T, FixedModeEO> fixedModeMap = Maps.newConcurrentMap();

    private Map<Long, DualHashBidiMap<String, String>> fixedPropertyMap;

    private Map<String, List<ConnectionEO>> connectionMap;


    public void addModelMap(ModelEO modelEO) {
        this.modelMap.put((T) modelEO.getId(), modelEO);
    }

    public void addPropertyMap(PropertyEO propertyEO) {
        this.propertyMap.put((T) propertyEO.getId(), propertyEO);
    }

    public void addConcurrentMap(ConnectionEO connectionEO) {
        final T parentId = (T) connectionEO.getParentId();
        final T subId = (T) connectionEO.getSubId();
        Map<T, List<ConnectionEO>> children = null;
        if (concurrentsMap.containsKey(parentId)) {
            children = concurrentsMap.get(parentId);
        } else {
            children = Maps.newConcurrentMap();
            concurrentsMap.put(parentId, children);
        }
        List<ConnectionEO> connectionEOS = null;
        if (children.containsKey(subId)) {
            connectionEOS = children.get(subId);
        } else {
            connectionEOS = Lists.newArrayList();
            children.put(subId, connectionEOS);
        }
        connectionEOS.add(connectionEO);
    }

    public void init() {
        this.initModel();
        this.initConnection();
        this.initFixedProperty();
    }

    private void initModel() {

    }

    private void initConnection() {
        if (this.connectionMap == null) {
            this.connectionMap = Maps.newConcurrentMap();
            // Map<T, Map<T, List<ConnectionEO>>>
            concurrentsMap.values().stream().map(r -> r.values()).forEach(r -> r.forEach(y -> y.forEach(x -> this.initConnection(x))));
        }
    }


    private void initConnection(ConnectionEO connectionEO) {
        if (connectionEO != null) {
            Long parentId = connectionEO.getParentId();
            String key = "parentId:" + parentId + "|type:" + connectionEO.getSubType();
            List<ConnectionEO> connectionEOS = null;
            if (this.connectionMap.containsKey(key)) {
                connectionEOS = this.connectionMap.get(key);
            } else {
                connectionEOS = Lists.newArrayList();
                this.connectionMap.put(key, connectionEOS);
            }
            connectionEOS.add(connectionEO);
        }
    }


    private PropertyEO getProperty(Long id) {
        if (id == null) {
            return null;
        }
        return this.propertyMap.get(id);
    }

    private List<PropertyEO> findChildrenProperty(Long id) {
        List<ConnectionEO> connections = this.findChildrenConnection(id, "Property");
        return connections.stream().map(r -> this.getProperty(r.getSubId())).filter(r -> r != null).collect(Collectors.toList());
    }

    private List<ConnectionEO> findChildrenConnection(Long id, String type) {
        List<ConnectionEO> connectionEOS = Lists.newArrayList();
        String key = "parentId:" + id + "|type:" + type;
        if (connectionMap.containsKey(key)) {
            connectionEOS = connectionMap.get(key);
        }
        return connectionEOS;
    }

    private void initFixedProperty() {
        if (fixedPropertyMap == null) {
            this.fixedPropertyMap = Maps.newConcurrentMap();
            BeanMap basePropMap = BeanMap.create(new InternalFixedModelEO());
            Map<Long, BeanMap> propMaps = Maps.newHashMap();
            this.fixedModeMap.values().stream().forEach(r -> {
                try {
                    Class<?> clazz = Class.forName(r.getClassName());
                    Object bean = clazz.newInstance();
                    BeanMap beanMap = BeanMap.create(bean);
                    propMaps.put(r.getId(), beanMap);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            });
            IFixedEntity entity = null;
            List<PropertyEO> childrenProperty = null;
            BeanMap propMap = null;
            Long fixedId = null;
            List<ModelEO> modelEOS = modelMap.values().stream().collect(Collectors.toList());
            for (ModelEO modelEO : modelEOS) {
                fixedId = modelEO.getFixedId();
                if (fixedId != null) {
                    propMap = propMaps.get(fixedId);
                    if (propMap != null) {
                        entity = (InternalFixedModelEO) propMap.getBean();
                        childrenProperty = this.findChildrenProperty(fixedId);
                        continue;
                    }
                }
            }
            final BeanMap map = propMap;
            final Long fId = fixedId;
            final IFixedEntity fixedEntity = entity;
            ListUtils.emptyIfNull(childrenProperty).stream().forEach(r -> {
                String code = r.getCode();
                String alias = r.getAlias();
                if (StringUtils.isBlank(alias)) {
                    alias = code.substring(0, 1).toLowerCase() + code.substring(1);
                }
                if (map != null && basePropMap != null && !basePropMap.containsKey(alias)) {
                    this.init(fId, code, alias);
                }
            });
        }
    }

    private void init(Long id, String code, String alias) {
        DualHashBidiMap<String, String> dualHashBidiMap = this.fixedPropertyMap.get(id);
        if (dualHashBidiMap == null) {
            dualHashBidiMap = new DualHashBidiMap();
            this.fixedPropertyMap.put(id, dualHashBidiMap);
        }
        dualHashBidiMap.put(code, alias);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        builder.append("{");
        try {
            builder.append("model:");
            builder.append(objectMapper.writeValueAsString(modelMap));
            builder.append(",");
            builder.append("property:");
            builder.append(objectMapper.writeValueAsString(propertyMap));
            builder.append(",");
            builder.append("concurrent:");
            builder.append(objectMapper.writeValueAsString(concurrentsMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
        builder.append("}");
        return builder.toString();
    }

    public Map<String, String> getFixedAttributes(T defineId) {
        this.init();
        return this.fixedPropertyMap.get(defineId);
    }
}
