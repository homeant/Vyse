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


import fun.vyse.cloud.core.constant.EntityState;

/**
 * fun.vyse.cloud.core.domain.IStateEntity
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-16 14:39
 */
public interface IStateEntity {
    EntityState getState$();

    void setState$(EntityState state);

	void updateState$(EntityState var1);
}
