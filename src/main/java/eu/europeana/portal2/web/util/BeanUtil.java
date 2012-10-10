package eu.europeana.portal2.web.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.definitions.solr.entity.Proxy;
import eu.europeana.corelib.solr.bean.impl.FullBeanImpl;
import eu.europeana.corelib.solr.entity.AggregationImpl;
import eu.europeana.corelib.utils.StringArrayUtils;

public class BeanUtil {

	public static String toString(Object object) {
		StringBuilder builder = new StringBuilder();
		String indent = "";
		extract(object, builder, indent);
		return object.getClass().getSimpleName() + " [\n" + builder.toString() + "]";
	}
	
	private static StringBuilder extract(Object parent, StringBuilder builder, String indent) {
		Method[] methods = parent.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("get")) {
				String field = method.getName().substring(3);
				String returnType = method.getReturnType().getSimpleName();
				try {
					Object o = method.invoke(parent);
					if (isEmptyValue(returnType, o)) {
						continue;
					}

					builder.append(indent).append(field).append(" (").append(returnType).append("): ");
					if (isEmptyValue(returnType, o)) {
						builder.append("[empty]");
					} else if (returnType.equals("String")) {
						builder.append(o);
					} else if (returnType.equals("Boolean")) {
						builder.append(o.toString());
					} else if (returnType.equals("String[]")) {
						String[] a = (String[]) o;
						builder.append(StringUtils.join(a, ", "));
					} else if (returnType.equals("Boolean[]")) {
						Boolean[] a = (Boolean[]) o;
						builder.append(StringUtils.join(a, ", "));
					} else if (returnType.equals("List")) {
						for(Object a : (List)o) {
							builder.append("\n").append(indent).append("  (").append(a.getClass().getSimpleName()).append(")\n");
							extract(a, builder, indent + "    ");
						}
					}
					else {
						builder.append(o.toString());
					}
					builder.append("\n");
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return builder;
	}
	
	private static boolean isEmptyValue(String returnType, Object o) {
		if (o == null) {
			return true;
		}
		if (returnType.equals("String") && o.equals("")) {
			return true;
		}
		if (returnType.equals("String[]") && StringUtils.join((String[]) o, ", ").equals("")) {
			return true;
		}
		return false;
	}
	
	private static Method getProxyMethod(String function) {
		final Method[] methods = Proxy.class.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(function)) {
				return method;
			}
		}
		return null;
	}

	private static Method getAggregationMethod(String function) {
		final Method[] methods = AggregationImpl.class.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(function)) {
				return method;
			}
		}
		return null;
	}

	public String[] getProxyValue(FullBeanImpl document, String function) {
		Method method = getProxyMethod(function);
		if (method == null) {
			return null;
		}
		if (document.getProxies() != null) {
			List<String> items = new ArrayList<String>();
			for (Proxy proxy : document.getProxies()) {
				try {
					StringArrayUtils.addToList(items, (String[])method.invoke(proxy));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			return StringArrayUtils.toArray(items);
		}
		return null;
	}

	public String[] getAggregationValue(FullBeanImpl document, String function) {
		Method method = getAggregationMethod(function);
		if (method == null) {
			return null;
		}
		if (document.getAggregations() != null) {
			List<String> items = new ArrayList<String>();
			for (Object _aggregation : document.getAggregations()) {
				AggregationImpl aggregation = (AggregationImpl)_aggregation;
				try {
					StringArrayUtils.addToList(items, (String[])method.invoke(aggregation));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			return StringArrayUtils.toArray(items);
		}
		return null;
	}


}
