package eu.europeana.portal2.web.presentation.model.data.submodel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class FieldCleaner {

	private static Map<Pattern, Method> complexReplacements = new LinkedHashMap<Pattern, Method>();
	private static Map<String, String> simpleReplacements = new LinkedHashMap<String, String>();
	static {
		registerComplexReplacement("&lt;(BR|br)&gt;", "cleanBr");
		registerComplexReplacement("&lt;(/)?(I|i|P|p)&gt;", "cleanTags");
		registerComplexReplacement("&lt;(/)?(LI|li)&gt;", "removeTags");
		simpleReplacements.put(">>", "&rarr;");
		simpleReplacements.put("&amp;quot;", "\"");
	}

	private static void registerComplexReplacement(String regex, String methodName) {
		try {
			complexReplacements.put(Pattern.compile(regex),
				FieldCleaner.class.getDeclaredMethod(methodName, Matcher.class));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	public static String clean(String text) {
		if (StringUtils.isBlank(text)) {
			return text;
		}
		for (Pattern pattern : complexReplacements.keySet()) {
			Method method = complexReplacements.get(pattern);
			text = cleanElements(pattern.matcher(text), method);
		}
		for (String pattern : simpleReplacements.keySet()) {
			text = text.replace(pattern, simpleReplacements.get(pattern));
		}
		return text;
	}

	private static String cleanElements(Matcher matcher, Method method) {
		StringBuffer result = new StringBuffer();
		while (matcher.find()) {
			try {
				matcher.appendReplacement(result, (String)method.invoke(FieldCleaner.class, matcher));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		matcher.appendTail(result);
		return result.toString();
	}

	@SuppressWarnings("unused")
	private static String cleanBr(Matcher matcher) {
		return "<br/>";
	}

	@SuppressWarnings("unused")
	private static String cleanTags(Matcher matcher) {
		return "<" + (matcher.group(1) == null ? "" : matcher.group(1)) + matcher.group(2).toLowerCase() + ">";
	}

	@SuppressWarnings("unused")
	private static String removeTags(Matcher matcher) {
		return "";
	}
}
