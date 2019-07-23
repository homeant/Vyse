package fun.vyse.colud.extension.script;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>fun.vyse.colud.extension.script.SpringELParser</p>
 * <p></p>
 *
 * @author huangtianhui
 */
public class SpringELParser extends AbstractScriptParser  {

    /**
     * # 号
     */
    private static final String POUND = "#";

    /**
     * 撇号
     */
    private static final String apostrophe = "'";

    private final ExpressionParser parser = new SpelExpressionParser();

    private final ConcurrentHashMap<String, Expression> expCache = new ConcurrentHashMap<String, Expression>();

    private final ConcurrentHashMap<String, Method> funcs = new ConcurrentHashMap<String, Method>(8);

    @Override
    public void addFunction(String name, Method method) {
        funcs.put(name, method);
    }

    @Override
    public <T> T getElValue(String keySpEL, Object target, Object[] arguments, Class<T> valueType) throws Exception {
        if (valueType.equals(String.class)) {
            // 如果不是表达式，直接返回字符串
            if (keySpEL.indexOf(POUND) == -1 && keySpEL.indexOf(apostrophe) == -1) {
                return (T) keySpEL;
            }
        }
        StandardEvaluationContext context = new StandardEvaluationContext();

        //context.registerFunction(HASH, hash);
        //context.registerFunction(EMPTY, empty);
        Iterator<Map.Entry<String, Method>> it = funcs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Method> entry = it.next();
            context.registerFunction(entry.getKey(), entry.getValue());
        }
        context.setVariable(TARGET, target);
        context.setVariable(ARGS, arguments);
        Expression expression = expCache.get(keySpEL);
        if (null == expression) {
            expression = parser.parseExpression(keySpEL);
            expCache.put(keySpEL, expression);
        }
        return expression.getValue(context, valueType);
    }
}
