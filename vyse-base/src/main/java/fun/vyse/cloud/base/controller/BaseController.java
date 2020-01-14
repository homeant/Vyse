package fun.vyse.cloud.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 基本控制器
 */
public class BaseController {

	/*@Autowired
	public HttpServletRequest request;

	@Autowired
	public HttpServletResponse response;*/

    @Autowired
    private MessageSource messageSource;

    /**
     * <p>
     * getMessage
     * </p>
     * <p>
     * 获取国际化
     * </p>
     *
     * @param code
     * @return message
     * @author guaika junchen1314@foxmail.com
     * @Data 2017年12月9日 下午1:28:42
     */
    public String getMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, null, "", locale);
    }

    public String getMessage(String code, Object args) {
        Locale locale = LocaleContextHolder.getLocale();
        if (args == null) {
            args = "";
        }
        if (args.getClass().isArray()) {
            return messageSource.getMessage(code, (Object[]) args, "", locale);
        } else {
            return messageSource.getMessage(code, new Object[]{args}, "", locale);
        }
    }


    /**
     * <p>
     * getMessage
     * </p>
     * <p>
     * 获取国际化
     * </p>
     *
     * @param code
     * @param args
     * @return
     * @author guaika junchen1314@foxmail.com
     * @Data 2017年12月9日 下午1:29:39
     */
    public String getMessage(String code, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, locale);
    }

}
