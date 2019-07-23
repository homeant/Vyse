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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    public void afterMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
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
        String methodName = point.getSignature().getName();
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
                Boolean elValue = true;
                if(extension!=null){
                    elValue = springELParser.getElValue(extension.condition(), point.getTarget(), point.getArgs(), Boolean.class);
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
