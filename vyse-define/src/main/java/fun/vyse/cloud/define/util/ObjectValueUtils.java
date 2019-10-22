package fun.vyse.cloud.define.util;

import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.time.DateFormatUtils.format;
import static org.apache.commons.lang3.time.DateUtils.parseDate;

import java.text.ParseException;
import java.util.Date;

/**
 * ObjectValueUtils
 *
 * @author junchen homeanter@163.com
 * @date 2019-10-22 11:13
 */
public class ObjectValueUtils {

	private static final ObjectValueUtils instance = new ObjectValueUtils();

	public static String toStringValue(Object value, String pattern) {
		if (value == null) {
			return null;
		}
		if(value.getClass()==String.class){
			return String.valueOf(value);
		}
		if (value instanceof Date) {
			return format((Date) value, pattern);
		}else if(value.getClass().isArray()){
			if(value instanceof byte[]){
				return new String((byte[])value);
			}else if(value instanceof char[]){
				return new String((char[])value);
			}
			return "";
		}
		return null;
	}

	public static Object toObjectValue(String dataType, String value, String pattern) {
		if(StringUtils.isBlank(value)){
			return null;
		}
		Class clazz = instance.getClass(dataType);
		if (clazz == String.class) {
			return value;
		} else if (clazz == Date.class) {
			return instance.toDate(value, pattern);
		}
		return null;
	}

	private Class getClass(String classType) {
		try {
			Class clazz = Class.forName(classType);
			return clazz;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			return String.class;
		}
	}

	private Date toDate(String date, String pattern) {
		try {
			return parseDate(date, pattern);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
