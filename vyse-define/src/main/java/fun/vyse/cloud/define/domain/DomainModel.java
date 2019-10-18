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

package fun.vyse.cloud.define.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fun.vyse.cloud.core.common.ApplicationContextHelper;
import fun.vyse.cloud.core.domain.*;
import fun.vyse.cloud.define.entity.ModelDataEO;
import fun.vyse.cloud.define.entity.ModelPropertyEO;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * fun.vyse.cloud.core.domain.DefineModel
 *
 * @Author junchen homeanter@163.com
 * @Date 2019-10-12 14:34
 */
@ToString
public class DomainModel extends AbstractBaseEntity<Long> implements IModel, ITenantEntity {

	private ModelDataEO entity;
	@JsonIgnore
	private transient MetaDefinition<Long> md = null;

	/**
	 * 静态类EO
	 */
	private IFixedEntity<Long> fixedModel = null;

	/**
	 * 静态属性映射
	 */
	private Map<String, String> fixedPropertyMap = null;

	/**
	 * 静态类的数据存储
	 */
	@JsonIgnore
	private transient BeanMap fixedModelMap = null;

	private Map<Long, ModelPropertyEO> propertyMap = Maps.newConcurrentMap();

	@Getter
	@Setter
	private String tenantId;

	@Getter
	@Setter
	private DomainModel parentModel;

	public DomainModel() {
		this.entity = null;
	}

	public DomainModel(ModelDataEO entity, MetaDefinition md) {
		this.entity = entity;
		this.md = md;
	}

	public void setFixedModel(IFixedEntity fixedModel) {
		this.fixedModel = fixedModel;
		this.fixedModelMap = BeanMap.create(fixedModel);
	}

	@Override
	public ModelDataEO getEntity() {
		return entity;
	}


	/**
	 * 初始化静态属性映射
	 */
	private void initFixedPropertyMap() {
		if (fixedPropertyMap == null) {
			Long fixedId = this.entity.getFixedId();
			if (fixedId != null && fixedId > 0L) {
				this.initMetaDefinition();
				this.fixedPropertyMap = this.md.getFixedProperty(fixedId);
			}
		}
	}

	private void initMetaDefinition() {
		if (md == null) {
			MetaDefinitionFactory factory = (MetaDefinitionFactory) ApplicationContextHelper.getBean(MetaDefinitionFactory.class);
			if (factory != null) {
				md = factory.getMetaDefinition(this.tenantId);
			}
		}
	}

	/**
	 * 属性赋值
	 *
	 * @param code  编码
	 * @param value 属性值
	 */
	public void setPropertyValue(String code, Object value) {
		this.initFixedPropertyMap();
		Set<String> basePropertys = Sets.newHashSet("domainId", "domainCode");
		if (!basePropertys.contains(code)) {
			this.setFixedValue(code, value);
		}
	}

	/**
	 * 静态属性赋值
	 *
	 * @param code  编码
	 * @param value 属性值
	 */
	private void setFixedValue(String code, Object value) {
		Object oldValue = this.getFixedValue(code);
		if (ObjectUtils.notEqual(oldValue, value)) {
			String alias = this.fixedPropertyMap.get(code);
			this.fixedModelMap.put(alias, value);
		}
	}

	/**
	 * 获取静态属性的值
	 *
	 * @param code 编码
	 * @return 属性值
	 */
	private Object getFixedValue(String code) {
		if (this.fixedPropertyMap != null) {
			String alias = this.fixedPropertyMap.get(code);
			return this.fixedModelMap.get(alias);
		}
		return null;
	}

	public void put(IEntity entity) {
		if (entity instanceof ModelPropertyEO) {
			this.put((ModelPropertyEO) entity);
		}
	}

	/**
	 * 添加动态字段
	 *
	 * @param propertyEO 属性对象
	 */
	private void put(ModelPropertyEO propertyEO) {
		Long id = propertyEO.getId();
		Long parentId = propertyEO.getParentId();
		if (!this.entity.getDomainId().equals(id) && this.entity.getDomainId().equals(parentId)) {
			this.propertyMap.put(id, propertyEO);
		}
	}

	public <T> List<T> findChildren(Class<T> classType) {
		return Collections.emptyList();
	}

	public <T> T findChildren(Class<T> classType, String code, Integer index) {
		return null;
	}

	public <T> List<T> findChildren(Class<T> classType, String code) {
		return Collections.emptyList();
	}

	public void setData(@NonNull Map<String, Object> map) {
		Long domainId = (Long) map.get("domainId");
		String domainCode = (String) map.get("domainCode");
		if (this.entity != null) {
			if (!ObjectUtils.notEqual(this.entity.getDomainId(), domainId)) {
				if (StringUtils.equals(this.entity.getDomainCode(), domainCode)) {
					for (String r : map.keySet()) {
						Object value = map.get(r);
						if (!(value instanceof Map) && !(value instanceof List)) {
							this.setPropertyValue(r, value);
						} else if (value instanceof Map) {
							DomainModel children = this.findChildren(DomainModel.class, r, 0);
							if (children != null) {
								children.setData((Map) value);
							}
						} else if (value instanceof List) {
							((List) value).stream().filter(this::isMap).forEach(x -> this.setData((Map) x));
						}
					}
				}
			}
		}
	}

	private Boolean isMap(Object object) {
		return object instanceof Map;
	}
}
