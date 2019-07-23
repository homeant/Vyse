package fun.vyse.colud.extension.script;

import java.lang.reflect.Method;

/**
 * <p>fun.vyse.colud.extension.script.AbstractScriptParser</p>
 * <p></p>
 *
 * @author huangtianhui
 */
public abstract class AbstractScriptParser {

    protected static final String TARGET = "target";

    protected static final String ARGS = "args";

    /**
     * 为了简化表达式，方便调用Java static 函数，在这里注入表达式自定义函数
     *
     * @param name   自定义函数名
     * @param method 调用的方法
     */
    public abstract void addFunction(String name, Method method);

    /**
     * 将表达式转换期望的值
     *
     * @param elStr
     * @param target    AOP 拦截到的实例
     * @param arguments 参数
     * @param valueType 值类型
     * @param <T>       泛型
     * @return T Value 返回值
     * @throws Exception 异常
     */
    public abstract  <T> T getElValue(String elStr, Object target, Object[] arguments, Class<T> valueType) throws Exception;
}
