package fun.vyse.cloud.base.entity;

import fun.vyse.cloud.core.domain.AbstractBaseEntity;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * GeneratorEO
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-30 10:52
 */
@Entity
@Table(name = "t_generator")
@Data
@ToString(callSuper = true)
public class GeneratorEO extends AbstractBaseEntity<Long> {

	private Long id;

	/**
	 * 序列名称
	 */
	private String seqName;

	/**
	 * 执行表达式
	 */
	private String expression;

	/**
	 * 当前值
	 */
	private Long value;

	/**
	 * 自增值
	 */
	private Long increment;

	/**
	 * 序号的长度(位数)
	 */
	private int length;

	public static final String SEQ_NAME = "seqName";

	public static final String EXPRESSION = "expression";

	public static final String VALUE = "value";

	public static final String INCREMENT = "increment";
}
