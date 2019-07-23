package fun.vyse.colud.extension.annotation;

import java.lang.annotation.*;

/**
 * <p>fun.vyse.colud.extension.annotation.Extension</p>
 * <p></p>
 *
 * @author huangtianhui
 */
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Extension {
    String condition();
}
