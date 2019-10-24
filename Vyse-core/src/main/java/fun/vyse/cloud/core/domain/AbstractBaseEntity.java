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

package fun.vyse.cloud.core.domain;

import lombok.Data;
import lombok.ToString;
//import org.hibernate.annotations.ColumnMeta;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * fun.vyse.cloud.core.domain.AbstractBaseEntity
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-12 13:25
 */
@MappedSuperclass
@Data
@ToString(callSuper = true)
public abstract class AbstractBaseEntity<T> extends AbstractStateEntity implements IEntity<T>, IVersionEntity {
	@Id
	//@ColumnMeta(index = Integer.MIN_VALUE)
	private T id;

	//@ColumnMeta(index = Integer.MAX_VALUE - 1)
	private String tenantId;

	//@ColumnMeta(index = Integer.MAX_VALUE)
	@Version
	private Long version;

	public static final String ID = "id";

	public static final String TENANT_ID = "tenantId";

}
