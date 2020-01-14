package fun.vyse.cloud.core.util;

import lombok.NonNull;

import java.lang.reflect.ParameterizedType;

/**
 * ClassUtils
 *
 * @author junchen
 * @date 2019-11-11 13:01
 */
public class ClassUtils {

	public static Class getGenericsType(@NonNull Class clz){
		return ClassUtils.getGenericsType(clz,0);
	}

	public static Class getGenericsType(@NonNull Class clz,int index){
		// 获取当前new的对象的泛型的父类类型
		ParameterizedType pt = (ParameterizedType) clz.getGenericSuperclass();
		// 获取第一个类型参数的真实类型
		return (Class) pt.getActualTypeArguments()[index];
	}
}
