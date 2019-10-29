package org.hibernate.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ColumMeta
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-24 13:51
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnMeta {
	int index() default 0;
}
