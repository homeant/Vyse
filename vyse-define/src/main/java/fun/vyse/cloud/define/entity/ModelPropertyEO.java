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

import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import fun.vyse.cloud.core.domain.IFixedEntity;
import lombok.Data;

/**
 * fun.vyse.cloud.define.entity.ModelProperty
 *
 * @Author junchen homeanter@163.com
 * @Date 2019-10-15 12:04
 */
@Data
public class ModelPropertyEO extends AbstractBaseEntity<Long> implements IFixedEntity<Long> {
	/**
	 * 属性id
	 */
	private Long propertyId;

	/**
	 *
	 */
    private String value;

    private String code;

	/**
	 * 主模型id
	 */
	private Long domainId;

	/**
	 * 父级模型id
	 */
	private Long parentId;

	/**
	 * 数据id
	 */
	private Long topId;
}
