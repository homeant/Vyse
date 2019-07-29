package fun.vyse.colud.extension.aop;

import fun.vyse.colud.extension.annotation.Extension;
import fun.vyse.colud.extension.annotation.ExtensionService;
import fun.vyse.colud.extension.script.SpringELParser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>fun.vyse.colud.extension.aop.ExtensionInterceptor</p>
 * <p>aop 实现</p>
 *
 * @author huangtianhui
 */
@Data
@Slf4j
public class ExtensionInterceptor implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private SpringELParser springELParser = new SpringELParser();

    /**
     * 前置通知
     *
     * @param joinPoint
     */
    public void beforMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        List<Object> args = Arrays.asList(joinPoint.getArgs());
    }

    /**
     * 后置通知（无论方法是否发生异常都会执行,所以访问不到方法的返回值）
     *
     * @param joinPoint
     */
    @SuppressWarnings("unchecked")
	public void afterMethod(JoinPoint joinPoint) {
    	//后置方法名称
    	String methodName = "afterMethod";
    	MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        Method method = sign.getMethod();
        ExtensionService extensionService = method.getAnnotation(ExtensionService.class);
        try {
	        Class clazz = extensionService.className();
	        Map<String,Object> beansOfType = applicationContext.getBeansOfType(clazz);
	        Class[] array = new Class[joinPoint.getArgs().length];
	        List<String> list = Arrays.asList(joinPoint.getArgs()).stream().map(r -> r.getClass().getName()).collect(Collectors.toList());
	        for (int i = 0; i < list.size(); i++) {
	            array[i] = Class.forName(list.get(i));
	        }
	        LinkedHashMap <String,Object> dataLinkMap=(LinkedHashMap<String,Object>) sortHashMap(beansOfType,methodName);
	        System.out.println(dataLinkMap);
	        for (Map.Entry<String, Object> entry : dataLinkMap.entrySet()) {
	            Object bean = entry.getValue();

	            Method me = bean.getClass().getDeclaredMethod(methodName,Object[].class);
	            Extension extension = me.getAnnotation(Extension.class);
	            boolean elValue = true;
	            if(extension!=null){
	                elValue = springELParser.getElValue(extension.condition(), joinPoint.getTarget(), joinPoint.getArgs());
	            }
	            if(elValue){
	                Object invoke = me.invoke(bean, new Object[]{joinPoint.getArgs()});
	                log.debug("result:{}",invoke);
	                if(invoke!=null){
	                    //return invoke;
	                }
	            }
	         }
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * 对拓展的实现方法排序
     *
     * @param map 
     */
    public static HashMap<String,Object> sortHashMap(Map<String, Object> map,String MethodName) {
        // 首先拿到 map 的键值对集合
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        // 将 set 集合转为 List 集合，为什么，为了使用工具类的排序方法
        List<Map.Entry<String, Object>> list = new ArrayList<Map.Entry<String, Object>>(entrySet);
        // 使用 Collections 集合工具类对 list 进行排序，排序规则使用匿名内部类来实现
        Collections.sort(list, new Comparator<Map.Entry<String, Object>>() {
            @Override
            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                //按照要求根据 User 的 age 的升序进行排,如果是倒序就是o2-o1
            	try {
	            Method me1 = o1.getValue().getClass().getDeclaredMethod(MethodName,Object[].class);
	            Extension extension1 = me1.getAnnotation(Extension.class);
	            Method me2 = o2.getValue().getClass().getDeclaredMethod(MethodName,Object[].class);
	            Extension extension2 = me2.getAnnotation(Extension.class);
                return  extension1.order()-extension2.order();
            	} catch (NoSuchMethodException e) {
					e.printStackTrace();
					return -1;
				} catch (SecurityException e) {
					e.printStackTrace();
					return -1;
				}
            }
        });
        //创建一个新的有序的 HashMap 子类的集合
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        //将 List 中的数据存储在 LinkedHashMap 中
        for (Map.Entry<String, Object> entry : list) {
            linkedHashMap.put(entry.getKey(), entry.getValue());
        }
        //返回结果
        return linkedHashMap;
    }
    /**
     * 返回通知（在方法正常结束执行的代码）
     * 返回通知可以访问到方法的返回值！
     *
     * @param joinPoint
     */
    public void afterReturnMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
    }

    /**
     * 异常通知（方法发生异常执行的代码）
     * 可以访问到异常对象；且可以指定在出现特定异常时执行的代码
     *
     * @param joinPoint
     * @param ex
     */
    public void afterThrowingMethod(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
    }

    /**
     * 环绕通知(需要携带类型为ProceedingJoinPoint类型的参数)
     * 环绕通知包含前置、后置、返回、异常通知；ProceedingJoinPoin 类型的参数可以决定是否执行目标方法
     * 且环绕通知必须有返回值，返回值即目标方法的返回值
     *
     * @param point
     */
    public Object aroundMethod(ProceedingJoinPoint point) {
        ExtensionService annotation = getMethodAnnotation(point, ExtensionService.class);
        Object result = null;
        String methodName ="beforeMethod";
        try {
            Class clazz = annotation.className();
            Map<String,Object> beansOfType = applicationContext.getBeansOfType(clazz);
            Class[] array = new Class[point.getArgs().length];
            List<String> list = Arrays.asList(point.getArgs()).stream().map(r -> r.getClass().getName()).collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                array[i] = Class.forName(list.get(i));
            }
            for (Map.Entry<String, Object> entry : beansOfType.entrySet()){
                Object bean = entry.getValue();
                Method me = bean.getClass().getDeclaredMethod(methodName, array);
                Extension extension = me.getAnnotation(Extension.class);
                boolean elValue = true;
                if(extension!=null){
                    elValue = springELParser.getElValue(extension.condition(), point.getTarget(), point.getArgs());
                }
                if(elValue){
                    Object invoke = me.invoke(bean, point.getArgs());
                    log.debug("result:{}",invoke);
                    if(invoke!=null){
                        return invoke;
                    }
                }
            }
            result = point.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private <T extends Annotation> T getMethodAnnotation(ProceedingJoinPoint joinPoint, Class<T> clazz) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getAnnotation(clazz);
    }
}
