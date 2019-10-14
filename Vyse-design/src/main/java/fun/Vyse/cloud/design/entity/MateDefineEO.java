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

package fun.vyse.cloud.design.entity;

import fun.vyse.cloud.core.domain.AbstractEntity;
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
public class MateDefineEO extends AbstractEntity {

    private static final long serialVersionUID=1L;

    /**
     * 属性的id
     */
    private Long propertyId;

    /**
     * 类型
     */
    private String type;

    /**
     * 默认值
     */
    private String defValue;

    /**
     * 选择项
     */
    private String optValue;

    /**
     * 文档信息
     */
    private String doc;

    /**
     * 是否显示
     */
    private String isDisplay;

    /**
     * 是否必填
     */
    private String isRequired;

    /**
     * 是否只读
     */
    private String isReadonly;

    /**
     * 栅格偏移
     */
    private Integer colOffset;

    /**
     * 栅格比例
     */
    private Integer colSize;

    private String clazz;

    private String style;

    private String attrs;

    private String props;

    /**
     * 排序
     */
    private String sort;


    public static final String PROPERTY_ID = "property_id";

    public static final String TYPE = "type";

    public static final String DEF_VALUE = "def_value";

    public static final String OPT_VALUE = "opt_value";

    public static final String DOC = "doc";

    public static final String IS_DISPLAY = "is_display";

    public static final String IS_REQUIRED = "is_required";

    public static final String IS_READONLY = "is_readonly";

    public static final String COL_OFFSET = "col_offset";

    public static final String COL_SIZE = "col_size";

    public static final String CLASS = "class";

    public static final String STYLE = "style";

    public static final String ATTRS = "attrs";

    public static final String PROPS = "props";

    public static final String SORT = "sort";

}
