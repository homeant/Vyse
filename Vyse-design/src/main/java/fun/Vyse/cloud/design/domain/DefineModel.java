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

import com.fasterxml.jackson.annotation.JsonIgnore;
import fun.vyse.cloud.core.common.ApplicationContextHelper;
import fun.vyse.cloud.core.domain.IFixedEntity;
import fun.vyse.cloud.core.domain.IModel;
import fun.vyse.cloud.core.domain.ITenantEntity;
import fun.vyse.cloud.design.entity.ModelDataEO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * fun.vyse.cloud.design.domain.DefineModel
 *
 * @Author junchen homeanter@163.com
 * @Date 2019-10-12 14:34
 */
@ToString
public class DefineModel implements IModel, ITenantEntity {

    private ModelDataEO entity;
    @JsonIgnore
    private transient MetaDefinition<Long> md = null;

    private IFixedEntity fixedModel = null;

    private Map<String, String> fixedPropertyMap = null;
    @JsonIgnore
    private transient BeanMap fixedModelMap = null;

    @Getter
    @Setter
    private String tenantId;

    @Getter
    @Setter
    private DefineModel parentModel;


    @Override
    public ModelDataEO getEntity() {
        return entity;
    }

    public DefineModel() {
        this.entity = null;
    }

    public DefineModel(ModelDataEO entity, MetaDefinition md) {
        this.entity = entity;
        this.md = md;
    }

    public Long getId() {
        return this.entity.getId();
    }

    public void setId(Long id) {
        this.entity.setId(id);
    }

    public void setPropertyValue(String code, Object value) {
        if (StringUtils.isNotBlank(code)) {
            if(this.isFixedProperty(code)){
                if(isFixedProperty(code)){
                    this.setFixedModelValue(code, value);
                }
            }
        }
    }

    private void setFixedModelMap(){
        if(fixedModelMap == null){
            if (this.fixedModel != null) {
                this.fixedModelMap = BeanMap.create(this.fixedModel);
            }else if (this.isFixed()) {

            }
        }
    }

    private void setFixedModelValue(String code, Object value) {
        if(this.fixedPropertyMap != null){
            String alias = this.fixedPropertyMap.get(code);
            if (alias != null) {
                this.setFixedModelMap();
                if (this.fixedModelMap != null) {
                    if (value == null) {
                        this.setFixedValue(this.fixedModelMap, code, alias, value);
                        return;
                    }
                    Class<?> targetType = this.fixedModelMap.getPropertyType(alias);
                    Class<?> srcType = value.getClass();
                    if (targetType == srcType) {
                        this.setFixedValue(this.fixedModelMap, code, alias, value);
                    }
                }
            }
        }
    }

    private void setFixedValue(BeanMap fixedModelMap, String code, String alias, Object value){
        if (fixedModelMap.containsKey(alias)) {
            Object old = fixedModelMap.get(alias);
            if (!Objects.equals(value, old)) {
                fixedModelMap.put(alias, value);
            }
        }
    }


    private void initMetaDefinition() {
        if (md == null) {
            MetaDefinitionFactory factory = (MetaDefinitionFactory) ApplicationContextHelper.getBean(MetaDefinitionFactory.class);
            factory.getMetaDefinition(this.tenantId);
        }
    }

    private void setFixedPropertyMap() {
        if (fixedPropertyMap == null) {
            Long defineId = this.entity.getDefineId();
            if (defineId != null) {
                this.initMetaDefinition();
                this.fixedPropertyMap = this.md.getFixedAttributes(defineId);
            }
        }
    }

    private boolean isFixedProperty(String code) {
        this.setFixedPropertyMap();
        return this.fixedPropertyMap != null ? this.fixedPropertyMap.containsKey(code) : false;
    }

    public void setData(Map<String, Object> data) {
        Long defineId = MapUtils.getLong(data, "defineId");
        String defineCode = MapUtils.getString(data, "defineCode");
        if (entity != null) {
            if (Objects.equals(this.entity.getDefineId(), defineId)) {
                String entityCode = this.entity.getDefineCode();
                if (StringUtils.equals(defineCode, entityCode)) {
                    data.keySet().stream().forEach(key -> {
                        Object value = MapUtils.getObject(data, key);
                        if (!(value instanceof Map) && !(value instanceof List)) {
                            this.setPropertyValue(key, value);
                        }else if(value instanceof Map){
                            //this.setData((Map)value);
                        }
                    });
                }
            }
        }
    }


    @JsonIgnore
    public Boolean isFixed() {
        Long defineId = this.getEntity().getDefineId();
        return defineId != null && defineId > 0L ? true : false;
    }
}
