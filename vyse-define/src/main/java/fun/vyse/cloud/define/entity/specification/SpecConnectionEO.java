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
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * <p>
 * 
 * </p>
 *
 * @author junchen
 * @since 2019-10-12
 */
@Entity
@Table(name = "spec_connection")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SpecConnectionEO extends AbstractBaseEntity<Long>  {

    private static final long serialVersionUID=1L;

    /**
     * 最大数
     */
    private Long maxmium;

    /**
     * 最小数
     */
    private Long minmiun;

    /**
     * 构建个数
     */
    private Integer buildNumber;

    /**
     * 加载类型 0 即时加载 -1 按需加载
     */
    private Integer loadType;

    /**
     * 父级类型
     */
    private String parentType;

    /**
     * 父级id
     */
    private Long parentId;

    /**
     * 子级类型
     */
    private String subType;

    /**
     * 子级的id
     */
    private Long subId;

    /**
     * 排序
     */
    private String sort;


    public static final String MAXMIUM = "maxmium";

    public static final String MINMIUN = "minmiun";

    public static final String BUILD_NUMBER = "build_number";

    public static final String LOAD_TYPE = "load_type";

    public static final String PARENT_TYPE = "parent_type";

    public static final String PARENT_ID = "parent_id";

    public static final String SUB_TYPE = "sub_type";

    public static final String SUB_ID = "sub_id";

    public static final String SORT = "sort";

}
