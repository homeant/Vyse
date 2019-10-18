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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author junchen
 * @since 2019-10-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PropertyEO extends AbstractBaseEntity<Long> {

    private static final long serialVersionUID=1L;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    private String alias;

    /**
     * 默认值
     */
    private String defValue;

    /**
     * 字段类型
     */
    private String dataType;

    /**
     * 排序
     */
    private String sort;


    public static final String CODE = "code";

    public static final String NAME = "name";

    public static final String DEF_VALUE = "def_value";

    public static final String DATA_TYPE = "data_type";

    public static final String SORT = "sort";

}
