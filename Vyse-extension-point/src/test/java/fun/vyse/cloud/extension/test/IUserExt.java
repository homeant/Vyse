package fun.vyse.cloud.extension.test;

import fun.vyse.cloud.extension.test.entity.User;

/**
 * <p>fun.vyse.cloud.extension.test.IUserExt</p>
 * <p></p>
 *
 * @author huangtianhui
 */
public interface IUserExt {
	
    User getUser(User user);
    
    /**
     * 前置方法
     * */
    void beforeMethod(Object [] args);
   /**
    * 后置方法
    * */
    void afterMethod(Object [] args);
}
