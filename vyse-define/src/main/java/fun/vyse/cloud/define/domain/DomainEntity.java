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
import com.fasterxml.jackson.annotation.JsonInclude;
import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import fun.vyse.cloud.core.domain.IEntity;
import lombok.Data;

import java.util.Map;

/**
 * fun.vyse.cloud.core.domain.DefineEntity
 * 操作的entity
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-12 14:34
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DomainEntity extends AbstractBaseEntity<Long> implements IEntity<Long> {

	@JsonIgnore
	private DomainModel model;

	private Map<String, Object> extend;

	public DomainEntity() {
		this.model = null;
	}

	public DomainEntity(DomainModel model) {
		this.model = model;
	}

	@Override
	public Long getId() {
		return this.model.getId();
	}

	@Override
	public void setId(Long id) {
		this.model.setId(id);
	}

	@Override
	public String toString() {
		return this.model != null ? this.model.toString() : "";
	}
}
