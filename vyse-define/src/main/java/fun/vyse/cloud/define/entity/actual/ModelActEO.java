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

package fun.vyse.cloud.define.entity.actual;

import fun.vyse.cloud.core.domain.DomainEO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * com.ifa.cloud.channel.model.entity.ModelDataEO
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-12 14:48
 */
@Entity
@Table(name = "act_model")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class ModelActEO extends DomainEO<Long> {

}
