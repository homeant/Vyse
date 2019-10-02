package fun.vyse.colud.extension.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>fun.vyse.colud.extension.annotation.Extension</p>
 * <p></p>
 *
 * @author huangtianhui
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Extension {
    /**
     * 条件
     * @return
     */
    String condition();

    /**
     * 执行顺序
     * @return
     */
    int order() default 0;
}
