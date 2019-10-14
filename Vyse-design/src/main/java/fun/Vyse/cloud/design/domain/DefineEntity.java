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

package fun.vyse.cloud.design.domain;

import fun.vyse.cloud.core.domain.IEntity;
import lombok.Data;

import java.util.Map;

/**
 * fun.vyse.cloud.design.domain.DefineEntity
 * 操作的entity
 * @Author junchen homeanter@163.com
 * @Date 2019-10-12 14:34
 */
@Data
public class DefineEntity implements IEntity<Long> {

    private DefineModel model;

    private Map<String,Object> extend;

    public DefineEntity(){
        this.model = null;
    }

    public DefineEntity(DefineModel model){
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
}
