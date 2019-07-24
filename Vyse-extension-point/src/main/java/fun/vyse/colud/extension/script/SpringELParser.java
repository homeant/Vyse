package fun.vyse.colud.extension.script;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

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
    private static final String APOSTROPHE = "'";

    private static final String IS_EMPTY = "isEmpty";

    private final ExpressionParser parser = new SpelExpressionParser();

    private final ConcurrentHashMap<String, Expression> expCache = new ConcurrentHashMap<String, Expression>();

    private final ConcurrentHashMap<String, Method> funcs = new ConcurrentHashMap<String, Method>(8);

    private static Method isEmpty = null;

    static {
        try{
            //isEmpty = StringUtils.class.getMethod("isEmpty",new Class[]{String.class});
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void addFunction(String name, Method method) {
        funcs.put(name, method);
    }

    @Override
    public boolean getElValue(String spEL, Object target, Object[] arguments) throws Exception {
        // 如果不是表达式，直接返回false
        if (spEL.indexOf(POUND) == -1 && spEL.indexOf(APOSTROPHE) == -1) {
            return false;
        }
        StandardEvaluationContext context = new StandardEvaluationContext();
        //context.registerFunction(IS_EMPTY,isEmpty);
        Iterator<Map.Entry<String, Method>> it = funcs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Method> entry = it.next();
            context.registerFunction(entry.getKey(), entry.getValue());
        }
        context.setVariable(TARGET, target);
        context.setVariable(ARGS, arguments);
        Expression expression = expCache.get(spEL);
        if (null == expression) {
            expression = parser.parseExpression(spEL);
            expCache.put(spEL, expression);
        }
        return expression.getValue(context,boolean.class);
    }
}
