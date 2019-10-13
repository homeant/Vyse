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

package fun.vyse.cloud.core.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * fun.vyse.cloud.core.domain.FixedEntity
 * 静态表实体
 * @Author junchen
 * @Date 2019-10-13 10:01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FixedEntity<T> extends AbstractEntity<T> implements ITenant, IStructure {

	private String tenantId;

	private Date createTime;

	private Date updateTime;

	private String creator;

	private String mender;

	public final String TENANT_ID = "tenantId";
	public final String CREATE_TIME = "createTime";
	public final String UPDATE_TIME = "updateTime";
	public final String CREATOR = "creator";
	public final String MENDER = "mender";
}
