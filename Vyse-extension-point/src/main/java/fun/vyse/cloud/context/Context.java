package fun.vyse.cloud.context;

import lombok.Data;

/**
 * <p>fun.vyse.cloud.context.Context</p>
 * <p>扩展点上下文</p>
 *
 * @author huangtianhui
 */
@Data
public class Context {
    /**
     * 切入点方法入参
     * */
    private Object[] args;
    /**
     * 切入点方法返回值
     * */
    private Object result;
    /**
     * 切入点方法上条件注解
     * */
    private String condition;
    /**
     * 切入点方法上条件注解
     * */
    private Integer order;
}
