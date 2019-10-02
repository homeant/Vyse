package fun.vyse.cloud.core.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHelper implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHelper.applicationContext = applicationContext;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Object getBean(Class claz){
        return ApplicationContextHelper.applicationContext.getBean(claz);
    }

}
