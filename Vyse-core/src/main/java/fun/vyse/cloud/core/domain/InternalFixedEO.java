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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fun.vyse.cloud.core.constant.EntityState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * fun.vyse.cloud.core.domain.InternalFixedEO
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-14 14:37
 */
@Data
@ToString(callSuper = true)
@JsonTypeInfo(
		use = JsonTypeInfo.Id.CLASS,
		include = JsonTypeInfo.As.EXISTING_PROPERTY,
		property = "class$"
)
@EqualsAndHashCode(callSuper = true)
public class InternalFixedEO<T> extends AbstractBaseEntity<T> implements IFixedEntity<T> {
	/**
	 * 模型id
	 */
    private T domainId;
	/**
	 * 顶级模型数据id
	 */
	private T topId;

	private EntityState dirtyFlag;

    private String class$ = this.getClass().getName();

    public static final String DOMAIN_ID = "domainId";

	public static final String TOP_ID = "topId";

	public static final String DIRTY_FLAG = "dirtyFlag";

    public void setClass$(String value){}

	public void updateDirtyFlag(EntityState state){
		EntityState dirtyFlag = this.getDirtyFlag();
		if(EntityState.New != dirtyFlag || EntityState.Modify != state){
			this.setDirtyFlag(state);
		}
	}
}
