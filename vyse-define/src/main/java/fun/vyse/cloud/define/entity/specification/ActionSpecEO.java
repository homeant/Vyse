package fun.vyse.cloud.define.entity.specification;

import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * ActionSpecEO
 *
 * @author junchen homeanter@163.com
 * @date 2019-11-05 14:14
 */
@Entity
@Table(name = "spec_action")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ActionSpecEO extends AbstractBaseEntity<Long>  {
	/**
	 * 编码
	 */
	private String code;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 别名
	 */
	private String alias;

	/**
	 * 数据类型
	 */
	private String dataType;
	/**
	 * 包名
	 */
	private String packageName;

	/**
	 * 类名
	 */
	private String className;

	/**
	 * 方法
	 */
	private String method;
}
