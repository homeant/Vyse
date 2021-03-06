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

package fun.vyse.cloud.define.entity.specification;

import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * fun.vyse.cloud.define.entity.FixedModelEO
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-14 15:18
 */
@Entity
@Table(name = "spec_fixedModel")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class FixedModelSpecEO extends AbstractBaseEntity<Long>  {
    private String className;

    public Object createFixedInstance(){
        try {
            Class<?> clazz = Class.forName(this.className);
            Object o = clazz.newInstance();
            return o;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
