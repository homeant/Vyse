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

    private Object[] args;

    private Object result;

    private String condition;
}
