package fun.vyse.colud.extension;

import fun.vyse.cloud.context.Context;

/**
 *
 * @author junchen
 */
public interface IExtensionPoint {
    /**
     * 拓展点前置方法
     * @param 参数上下文
     * */
    void before(Context context);
    /**
     * 拓展点后置方法
     * @param 参数上下文
     * */
    void after(Context context);
}
