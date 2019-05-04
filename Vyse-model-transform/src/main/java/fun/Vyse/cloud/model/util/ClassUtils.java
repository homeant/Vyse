package fun.Vyse.cloud.model.util;

public class ClassUtils {
    public static Class getClass(String className) throws ClassNotFoundException{
        try {
            return Class.forName(className);
        }catch (ClassNotFoundException e){
            throw e;
        }
    }
}
