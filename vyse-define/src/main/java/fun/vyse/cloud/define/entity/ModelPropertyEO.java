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
import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import fun.vyse.cloud.core.domain.IFixedEntity;
import lombok.Data;
import net.sf.cglib.beans.BeanMap;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * fun.vyse.cloud.define.entity.ModelProperty
 *
 * @Author junchen homeanter@163.com
 * @Date 2019-10-15 12:04
 */
@Data
public class ModelPropertyEO extends AbstractBaseEntity<Long> implements IFixedEntity<Long> {

	private static final Long PROPERTY_NUMBER = 1L;

	@JsonIgnore
	private transient BeanMap bean;

	private Long currentIndex;

	/**
	 * 属性id
	 */
	private Long domainId1;

	private String code1;

	private String value1;

	private String dateType1;

	private String path1;

	/**
	 * 父级模型id
	 */
	private Long parentId;

	/**
	 * 数据id
	 */
	private Long topId;

	public Object get(Long index) {
		Long domainId = (Long) this.get(PropertyType.domainId, index);
		if (domainId != null) {
			String dataType = (String) this.get(PropertyType.dateType, index);
			String value = this.getValue(index);
			if (StringUtils.isNotBlank(dataType)) {
				try {
					Class clazz = Class.forName(dataType);
					if (clazz == String.class) {
						return value;
					} else if (clazz == Date.class) {
						return new Date();
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			return value;
		}
		return null;
	}

	public Object get(PropertyType type, Long id) {
		if (this.bean == null) {
			this.bean = BeanMap.create(this);
		}
		return this.bean.get(this.getProperName(type, id));
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

	public Long getCurrentIndex(){
		if (this.currentIndex == null) {
			for(Long i = PROPERTY_NUMBER; i >= 1; --i) {
				Long domainId = (Long)this.get(PropertyType.domainId, i);
				if (domainId != null) {
					this.currentIndex = i;
					break;
				}
			}
		}
		return this.currentIndex;
	}

	public enum PropertyType {
		code,
		value,
		domainId,
		path,
		dateType
	}
}
