package eu.europeana.portal2.web.model.json;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import eu.europeana.corelib.definitions.solr.DocType;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.corelib.definitions.solr.entity.Agent;
import eu.europeana.corelib.definitions.solr.entity.Aggregation;
import eu.europeana.corelib.definitions.solr.entity.Concept;
import eu.europeana.corelib.definitions.solr.entity.Place;
import eu.europeana.corelib.definitions.solr.entity.ProvidedCHO;
import eu.europeana.corelib.definitions.solr.entity.Proxy;
import eu.europeana.corelib.definitions.solr.entity.Timespan;
import eu.europeana.corelib.solr.bean.impl.FullBeanImpl;
import eu.europeana.corelib.solr.entity.AgentImpl;
import eu.europeana.corelib.solr.entity.AggregationImpl;
import eu.europeana.corelib.solr.entity.ConceptImpl;
import eu.europeana.corelib.solr.entity.PlaceImpl;
import eu.europeana.corelib.solr.entity.ProvidedCHOImpl;
import eu.europeana.corelib.solr.entity.ProxyImpl;
import eu.europeana.corelib.solr.entity.TimespanImpl;
import eu.europeana.portal2.web.model.FullBean4Json;

public class Json2FullBean {

	private static final String AGGREGATIONS = "aggregations";
	private static final String PROXIES = "proxies";
	private static final String RELATED_ITEMS = "relatedItems";
	private static final String PROVIDED_CHOS = "providedCHOs";
	private static final String PLACES = "places";
	private static final String AGENTS = "agents";
	private static final String TIMESPANS = "timespans";
	private static final String CONCEPTS = "concepts";
	
	private static final Logger log = Logger.getLogger(Json2FullBean.class.getName());

	private static ObjectMapper mapper = new ObjectMapper();
	// setters.get(prefix).get(field) -> Method
	private static Map<String, Map<String, Method>> setters = null;
	private static Map<String, Map<String, Field>> fields = null;
	private File file;
	private boolean isFileSouce = true;
	private String content = null;
	// handled fields
	private List<String> handledFields = new ArrayList<String>(Arrays.asList(new String[]{
			AGGREGATIONS, PROXIES, RELATED_ITEMS, PROVIDED_CHOS, PLACES, AGENTS, TIMESPANS, CONCEPTS, // HashMap -> Object
			"what", "where", "when", "dctermsIsPartOf", // List -> String[]
			"europeanaCompleteness" // Integer -> int
	}));
	
	public Json2FullBean(File file) {
		isFileSouce = true;
		this.file = file;
	}

	public Json2FullBean(String content) {
		setContent(content);
	}

	public void setFile(String fileName) {
		isFileSouce = true;
		this.file = new File(fileName);
	}

	public void setContent(String content) {
		isFileSouce = false;
		this.content = content.replaceAll(",\"[^\"]+\":null", "");
	}

	private void initializeSetters() {
		if (setters == null) {
			setters = new HashMap<String, Map<String, Method>>();
		}
		if (fields == null) {
			fields = new HashMap<String, Map<String, Field>>();
		}

		Map<String, Class> objects4setters = Collections.unmodifiableMap(new HashMap<String, Class>() {{
			put("", FullBean4Json.class);
			put(PROXIES, Proxy.class);
			put(AGGREGATIONS, Aggregation.class);
			put(PROVIDED_CHOS, ProvidedCHO.class);
			put(RELATED_ITEMS, BriefBeanImpl.class);
			put(PLACES, Place.class);
			put(AGENTS, Agent.class);
			put(TIMESPANS, Timespan.class);
			put(CONCEPTS, Concept.class);
		}});

		Map<String, Class> objects4fields = Collections.unmodifiableMap(new HashMap<String, Class>() {{
			put(RELATED_ITEMS, BriefBeanImpl.class);
		}});

		for (String prefix : objects4setters.keySet()) {
			Map<String, Method> localSetters = new HashMap<String, Method>();
			Method[] methods = objects4setters.get(prefix).getMethods();
			for (Method method : methods) {
				String name = method.getName();
				if (name.startsWith("set")) {
					String field = name.substring(3, 4).toLowerCase() + name.substring(4);
					localSetters.put(field, method);
				}
			}
			setters.put(prefix, localSetters);
		}

		for (String prefix : objects4fields.keySet()) {
			Map<String, Field> localFields = new HashMap<String, Field>();
			List<Field> allFields = extractFields(objects4fields.get(prefix));
			for (Field field : allFields) {
				String name = field.getName();
				localFields.put(name, field);
			}
			fields.put(prefix, localFields);
		}
	}
	
	public FullBean extractFullBean() throws JsonParseException, JsonMappingException, IOException {
		if (setters == null) {
			initializeSetters();
		}
		Map<String,Object> result = (isFileSouce) 
			? mapper.readValue(file, Map.class)
			: mapper.readValue(content, Map.class);
		Map<String,Object> object = (Map<String, Object>) result.get("object");

		FullBeanImpl fullBean = new FullBean4Json();
		for (String field : object.keySet()) {
			Object value = object.get(field);
			if (value == null) {
				continue;
			}
			Method setter = setters.get("").get(field);
			if (setter == null) {
				continue;
			}

			// type conversions
			if (field.equals(PROXIES)
				|| field.equals(AGGREGATIONS)
				|| field.equals(PROVIDED_CHOS)
				|| field.equals(RELATED_ITEMS)
				|| field.equals(PLACES)
				|| field.equals(AGENTS)
				|| field.equals(TIMESPANS)
				|| field.equals(CONCEPTS)) {
				value = convertObject(value, field);
			} else if (field.equals("europeanaCompleteness")) {
				value = (int) ((Integer)value).intValue();
			}

			if (isListToArray(value, setter)) {
				value = listToArray(value);
			}

			if (!value.getClass().getName().equals(setter.getParameterTypes()[0].getCanonicalName())) {
				if (!handledFields.contains(field)) {
					log.severe(field + ": " + value.getClass().getName() + " is not fit for "+ setter.getParameterTypes()[0].getCanonicalName());
				}
			}
			
			set(fullBean, setter, value);
		}

		result = null;
		content = null;
		file = null;

		return fullBean;
	}
	
	public static String fileToString(String file) {
		String result = null;
		DataInputStream in = null;

		try {
			File f = new File(file);
			byte[] buffer = new byte[(int) f.length()];
			in = new DataInputStream(new FileInputStream(f));
			in.readFully(buffer);
			result = new String(buffer);
		} catch (IOException e) {
			throw new RuntimeException("IO problem in fileToString", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) { /* ignore it */
			}
		}
		return result;
	}

	/**
	 * Transform an ArrayList to String[]
	 * 
	 * @param value
	 * @return
	 */
	private static String[] listToArray(Object value) {
		ArrayList<String> myList = (ArrayList<String>)value;
		String[] myArray = new String[myList.size()];
		myList.toArray(myArray);
		return myArray;
	}
	
	private boolean isListToArray(Object value, Method setter) {
		return (value.getClass().getName().equals("java.util.ArrayList") 
				&& setter.getParameterTypes()[0].getCanonicalName().equals("java.lang.String[]"));
	}

	private boolean isListToArray(Object value, Field field) {
		return (value.getClass().getName().equals("java.util.ArrayList") 
				&& field.getClass().getCanonicalName().equals("java.lang.String[]"));
	}

	private void set(Object bean, Method setter, Object value) {
		try {
			setter.invoke(bean, value);
		} catch (IllegalArgumentException e) {
			log.severe(e.getMessage() + ". Object: " + bean + ", Setter: " + setter + " Value: " + value);
			log.severe(e.getMessage() + ": " + setter.getParameterTypes()[0].getCanonicalName());
			log.severe(e.getMessage() + ": " + value.getClass().getName());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.severe(e.getMessage() + ". Object: " + bean + ", Setter: " + setter + " Value: " + value);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.severe(e.getMessage() + ". Object: " + bean + ", Setter: " + setter + " Value: " + value);
			e.printStackTrace();
		}
	}

	private Object convertObject(Object valueObject, String prefix) {
		List items = getList(prefix);
		for (LinkedHashMap<String, Object> rawItem : (ArrayList<LinkedHashMap<String, Object>>)valueObject) {
			Object item = getInstance(prefix);
			populateItem(item, rawItem, prefix);
			items.add(item);
		}
		return items;
	}

	private List getList(String type) {
		if (type.equals(PROXIES)) {
			return new ArrayList<Proxy>();
		} else if (type.equals(AGGREGATIONS)) {
			return new ArrayList<Aggregation>();
		} else if (type.equals(RELATED_ITEMS)) {
			return new ArrayList<BriefBean>();
		} else if (type.equals(PROVIDED_CHOS)) {
			return new ArrayList<ProvidedCHO>();
		} else if (type.equals(PLACES)) {
			return new ArrayList<Place>();
		} else if (type.equals(AGENTS)) {
			return new ArrayList<Agent>();
		} else if (type.equals(TIMESPANS)) {
			return new ArrayList<Timespan>();
		} else if (type.equals(CONCEPTS)) {
			return new ArrayList<Concept>();
		}
		return null;
	}

	private Object getInstance(String type) {
		if (type.equals(PROXIES)) {
			return new ProxyImpl();
		} else if (type.equals(AGGREGATIONS)) {
			return new AggregationImpl();
		} else if (type.equals(RELATED_ITEMS)) {
			return new BriefBeanImpl();
		} else if (type.equals(PROVIDED_CHOS)) {
			return new ProvidedCHOImpl();
		} else if (type.equals(PLACES)) {
			return new PlaceImpl();
		} else if (type.equals(AGENTS)) {
			return new AgentImpl();
		} else if (type.equals(TIMESPANS)) {
			return new TimespanImpl();
		} else if (type.equals(CONCEPTS)) {
			return new ConceptImpl();
		}
		return null;
	}

	private void populateItem(Object item, LinkedHashMap<String, Object> rawItem, String prefix) {
		for (String field : rawItem.keySet()) {
			Object value = rawItem.get(field);
			if (value == null) {
				continue;
			}
			Method setter = setters.get(prefix).get(field);
			if (setter == null) {
				continue;
			}
			if (isListToArray(value, setter)) {
				value = listToArray(value);
			}
			if (prefix.equals(RELATED_ITEMS) && field.equals("timestamp")) {
				value = new Date((Long)value);
			}
			if ((prefix.equals(PROXIES) && field.equals("edmType")) 
				|| (prefix.equals(RELATED_ITEMS) && field.equals("type"))) {
				value = DocType.get((String)value);
			}
			set(item, setter, value);
		}
	}

	private List<Field> extractFields(Class clazz) {
		Class superClazz = clazz;
		List<Field> members = new ArrayList<Field>();
		while (superClazz != null && superClazz != Object.class) {
			members.addAll(Arrays.asList(superClazz.getDeclaredFields()));
			superClazz = superClazz.getSuperclass();
		}
		return members;
	}
}
