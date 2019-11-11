package fun.vyse.cloud.base.service.impl;

import fun.vyse.cloud.base.entity.GeneratorEO;
import fun.vyse.cloud.base.repository.IGeneratorRepository;
import fun.vyse.cloud.base.service.IGeneratorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * GeneratorServiceImpl
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-30 11:35
 */
public class GeneratorServiceImpl extends BaseServiceImpl<GeneratorEO, Long, IGeneratorRepository> implements IGeneratorService {

	/**
	 * 获取值
	 *
	 * @param seqName
	 * @param tenantId
	 * @return
	 */
	@Transactional
	@Override
	public Object get(String seqName, String tenantId) {
		GeneratorEO generatorEO = baseRepository.findOne((root, criteriaQuery, criteriaBuilder) ->
				criteriaBuilder.equal(root.get(GeneratorEO.SEQ_NAME), seqName)).orElse(null);
		Object value = null;
		if (generatorEO != null) {
			ExpressionParser parser = new SpelExpressionParser();
			EvaluationContext context = new StandardEvaluationContext();
			Expression exp;
			Long oldValue = generatorEO.getValue();
			String expression = generatorEO.getExpression();
			context.setVariable("tenantId", generatorEO.getTenantId());
			if (generatorEO.getLength() > 0) {
				context.setVariable("value", String.format("%0" + generatorEO.getLength() + "d", oldValue));
			} else {
				context.setVariable("value", oldValue);
			}
			if (StringUtils.isNotBlank(expression)) {
				exp = parser.parseExpression(expression);
			} else {
				exp = parser.parseExpression("#value");
			}
			value = exp.getValue(context);
			exp = parser.parseExpression("#value + #increment");
			context.setVariable("value", oldValue);
			context.setVariable("increment", generatorEO.getIncrement());
			Long newValue = (Long) exp.getValue(context);
			generatorEO.setValue(newValue);
			baseRepository.flush();
		}
		return value;
	}
}
