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

package fun.vyse.cloud.define.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import fun.vyse.cloud.core.constant.EntityState;
import fun.vyse.cloud.core.constant.SetPropertyType;
import fun.vyse.cloud.core.domain.IRecursiveEntity;
import fun.vyse.cloud.core.domain.InternalFixedEO;
import fun.vyse.cloud.define.domain.DomainModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static fun.vyse.cloud.define.util.ObjectValueUtils.toObjectValue;
import static fun.vyse.cloud.define.util.ObjectValueUtils.toStringValue;

/**
 * fun.vyse.cloud.define.entity.ModelProperty
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-15 12:04
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class ModelPropertyEO extends InternalFixedEO<Long> implements IRecursiveEntity<Long> {

	private static final Long PROPERTY_NUMBER = 3L;

	@JsonIgnore
	private transient BeanMap bean;

	@JsonIgnore
	private transient Map<String, Long> codeMap;

	private EntityState dirtyFlag;

	private Long currentIndex;

	/**
	 * 属性id
	 */
	private Long domainId1;

	/**
	 * 属性编码
	 */
	private String code1;

	/**
	 * 属性值
	 */
	private String value1;

	/**
	 * 属性的数据类型
	 */
	private String dateType1;

	/**
	 * 时间类型转换格式
	 */
	private String pattern1;

	/**
	 * 属性的路径
	 */
	private String path1;

	private Long domainId2;
	private String code2;
	private String value2;
	private String dateType2;
	private String pattern2;
	private String path2;

	private Long domainId3;
	private String code3;
	private String value3;
	private String dateType3;
	private String pattern3;
	private String path3;

	/**
	 * 父级数据id
	 */
	private Long parentId;

	public Object get(Long index) {
		Long domainId = (Long) this.get(PropertyType.domainId, index);
		if (domainId != null) {
			String dataType = (String) this.get(PropertyType.dateType, index);
			String pattern = (String) this.get(PropertyType.pattern, index);
			String value = this.getValue(index);
			if (StringUtils.isNotBlank(dataType)) {
				return toObjectValue(dataType, value, pattern);
			}
			return value;
		}
		return null;
	}

	public Object get(PropertyType type, Long index) {
		if (this.bean == null) {
			this.bean = BeanMap.create(this);
		}
		return this.bean.get(this.getProperName(type, index));
	}

	public String getValue(Long index) {
		Long domainId = (Long) this.get(PropertyType.domainId, index);
		if (domainId != null) {
			return (String) this.get(PropertyType.value, index);
		}
		return null;
	}

	private String getProperName(PropertyType type, Long index) {
		return type.toString() + index.toString();
	}

	public Long getCurrentIndex() {
		if (this.currentIndex == null) {
			for (Long i = PROPERTY_NUMBER; i >= 1; --i) {
				Long domainId = (Long) this.get(PropertyType.domainId, i);
				if (domainId != null) {
					this.currentIndex = i;
					break;
				}
			}
		}
		return this.currentIndex;
	}

	public boolean containsKey(String code) {
		this.init();
		return this.codeMap.containsKey(code);
	}

	private void init() {
		if (this.codeMap == null) {
			this.codeMap = Maps.newConcurrentMap();
			if (this.bean == null) {
				this.bean = BeanMap.create(this);
			}
			for (Long i = 1L; i <= this.getCurrentIndex(); i++) {
				String key = (String) this.get(PropertyType.code, i);
				this.codeMap.put(key, i);
			}
		}

	}

	public SetPropertyType set(String code, Object value) {
		this.init();
		Long index = this.codeMap.get(code);
		if (index != null) {
			return this.set(index, value);
		}
		return SetPropertyType.Failure;
	}

	public SetPropertyType set(Long index, Object value) {
		Long domainId = (Long) this.get(PropertyType.domainId, index);
		if (domainId != null) {
			String pattern = (String) this.get(PropertyType.pattern, index);
			String nowValue = toStringValue(value, pattern);
			String oldValue = this.getValue(index);
			if (!StringUtils.equals(nowValue, oldValue)) {
				this.setValue(index, nowValue);
				this.updateDirtyFlag(EntityState.Modify);
				this.updateState$(EntityState.Modify);
				return SetPropertyType.Success;
			}
			return SetPropertyType.Null;
		}
		return SetPropertyType.Failure;
	}

	private void setValue(Long index, String value) {
		Long domainId = (Long) this.get(PropertyType.domainId, index);
		if (domainId != null) {
			this.set(PropertyType.value, index, value);
		}
	}

	private void set(PropertyType type, Long index, Object value) {
		if (this.bean == null) {
			this.bean = BeanMap.create(this);
		}
		this.bean.put(this.getProperName(type, index), value);
	}

	public enum PropertyType {
		code,
		value,
		domainId,
		pattern,
		path,
		dateType
	}
}
