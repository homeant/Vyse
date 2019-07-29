package fun.vyse.colud.extension;

import fun.vyse.cloud.context.Context;

/**
 *
 * @author junchen
 */
public interface IExtensionPoint {

    void before(Context context);

    void after(Context context);
}
