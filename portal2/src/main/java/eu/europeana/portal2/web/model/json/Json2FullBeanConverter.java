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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import eu.europeana.corelib.definitions.edm.beans.BriefBean;
import eu.europeana.corelib.definitions.edm.beans.FullBean;
import eu.europeana.corelib.definitions.edm.entity.Agent;
import eu.europeana.corelib.definitions.edm.entity.Aggregation;
import eu.europeana.corelib.definitions.edm.entity.Concept;
import eu.europeana.corelib.definitions.edm.entity.EuropeanaAggregation;
import eu.europeana.corelib.definitions.edm.entity.Place;
import eu.europeana.corelib.definitions.edm.entity.ProvidedCHO;
import eu.europeana.corelib.definitions.edm.entity.Proxy;
import eu.europeana.corelib.definitions.edm.entity.Timespan;
import eu.europeana.corelib.definitions.edm.entity.WebResource;
import eu.europeana.corelib.definitions.solr.DocType;
import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.solr.bean.impl.FullBeanImpl;
import eu.europeana.corelib.solr.entity.AgentImpl;
import eu.europeana.corelib.solr.entity.AggregationImpl;
import eu.europeana.corelib.solr.entity.ConceptImpl;
import eu.europeana.corelib.solr.entity.EuropeanaAggregationImpl;
import eu.europeana.corelib.solr.entity.PlaceImpl;
import eu.europeana.corelib.solr.entity.ProvidedCHOImpl;
import eu.europeana.corelib.solr.entity.ProxyImpl;
import eu.europeana.corelib.solr.entity.TimespanImpl;
import eu.europeana.corelib.solr.entity.WebResourceImpl;
import eu.europeana.corelib.utils.DateUtils;
import eu.europeana.portal2.web.model.FullBean4Json;

public class Json2FullBeanConverter {

	private final Logger log = Logger.getLogger(Json2FullBeanConverter.class);

	private static final String AGGREGATIONS = "aggregations";
	private static final String PROXIES = "proxies";
	private static final String RELATED_ITEMS = "relatedItems";
	private static final String PROVIDED_CHOS = "providedCHOs";
	private static final String PLACES = "places";
	private static final String AGENTS = "agents";
	private static final String TIMESPANS = "timespans";
	private static final String CONCEPTS = "concepts";
	private static final String EUROPEANA_AGGREGATION = "europeanaAggregation";
	private static final String WEBRESOURCES = "webResources";

	private static ObjectMapper mapper = new ObjectMapper();
	// setters.get(prefix).get(field) -> Method
	private static Map<String, Map<String, Method>> setters = null;
	private static Map<String, Map<String, Field>> fields = null;

	private File file;
	private boolean isFileSouce = true;
	private String content = null;
	// handled fields
	private List<String> handledFields = new ArrayList<String>(Arrays.asList(new String[]{
			AGGREGATIONS, PROXIES, RELATED_ITEMS, PROVIDED_CHOS, EUROPEANA_AGGREGATION,
			PLACES, AGENTS, TIMESPANS, CONCEPTS, // HashMap -> Object
			"what", "where", "when", "dctermsIsPartOf", // List -> String[]
			"year", "title", "europeanaCollectionName", "language", // List -> String[]
			"europeanaCompleteness", // Integer -> int
			"type", // String -> DocType
			"timestamp_update", "timestamp_created" // String -> Date
	}));

	public Json2FullBeanConverter(File file) {
		isFileSouce = true;
		this.file = file;
	}

	public Json2FullBeanConverter(String content) {
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

	private void initialize() {
		initializeSetters();
		initializeFields();
	}

	private void initializeFields() {
		fields = new HashMap<String, Map<String, Field>>();
		Map<String, Class<?>> objects4fields = new HashMap<String, Class<?>>();
		objects4fields.put(RELATED_ITEMS, BriefBeanImpl.class);

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

	private void initializeSetters() {
		setters = new HashMap<String, Map<String, Method>>();
		Map<String, Class<?>> objects4setters = new HashMap<String, Class<?>>();
		objects4setters.put("", FullBean4Json.class);
		objects4setters.put(PROXIES, Proxy.class);
		objects4setters.put(AGGREGATIONS, Aggregation.class);
		objects4setters.put(PROVIDED_CHOS, ProvidedCHO.class);
		objects4setters.put(RELATED_ITEMS, BriefBeanImpl.class);
		objects4setters.put(PLACES, Place.class);
		objects4setters.put(AGENTS, Agent.class);
		objects4setters.put(TIMESPANS, Timespan.class);
		objects4setters.put(CONCEPTS, Concept.class);
		objects4setters.put(EUROPEANA_AGGREGATION, EuropeanaAggregation.class);
		objects4setters.put(WEBRESOURCES, WebResource.class);

		for (String prefix : objects4setters.keySet()) {
			Map<String, Method> localSetters = new HashMap<String, Method>();
			Method[] methods = objects4setters.get(prefix).getMethods();
			for (Method method : methods) {
				String name = method.getName();
				if (name.startsWith("set")) {
					String field = name.substring(3, 4).toLowerCase() + name.substring(4);
					if (prefix.equals("")) {
						if (StringUtils.equals(field, "timestampCreated")) {
							field = "timestamp_created";
						} else if (StringUtils.equals(field, "timestampUpdated")) {
							field = "timestamp_update";
						}
					}
					localSetters.put(field, method);
				}
			}
			setters.put(prefix, localSetters);
		}
	}

	@SuppressWarnings("unchecked")
	public FullBean extractFullBean() throws JsonParseException, JsonMappingException, IOException {
		if (setters == null) {
			initialize();
		}

		Map<String, Object> object = readObjectMap();

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
				value = convertObjects(value, field);
			} else if (field.equals(EUROPEANA_AGGREGATION)) {
				value = convertObject(EUROPEANA_AGGREGATION, (LinkedHashMap<String,Object>)value);
			} else if (field.equals("type")) {
				value = DocType.safeValueOf((String)value);
			} else if (field.equals("europeanaCompleteness")) {
				value = (int) ((Integer)value).intValue();
			} else if (field.equals("year") 
					|| field.equals("title") 
					|| field.equals("europeanaCollectionName")
					|| field.equals("language")) {
				value = ((List<String>)value).toArray(new String[((List<String>)value).size()]);
			} else if (field.equals("timestamp_update") || field.equals("timestamp_created")) {
				value = DateUtils.parse((String)value);
			}

			if (isListToArray(value, setter)) {
				value = listToArray(value);
			}

			if (!value.getClass().getName().equals(setter.getParameterTypes()[0].getCanonicalName())) {
				if (!handledFields.contains(field)) {
					String message = String.format("'%s': %s  is not fit for %s", 
						field, value.getClass().getName(), setter.getParameterTypes()[0].getCanonicalName());
				//	System.out.println(message);
					log.warn(message);
				}
			}

			set(fullBean, setter, value);
		}

		content = null;
		file = null;

		return fullBean;
	}

	private Map<String, Object> readObjectMap() throws IOException,
			JsonParseException, JsonMappingException {
		@SuppressWarnings("unchecked")
		Map<String,Map<String, Object>> result = (isFileSouce) 
				? mapper.readValue(file, Map.class) 
				: mapper.readValue(content, Map.class);

		Map<String,Object> object = result.get("object");
		return object;
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
				if (in != null) {
					in.close();
				}
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
		@SuppressWarnings("unchecked")
		ArrayList<String> myList = (ArrayList<String>) value;
		String[] myArray = new String[myList.size()];
		myList.toArray(myArray);
		return myArray;
	}

	private boolean isListToArray(Object value, Method setter) {
		return (value.getClass().getName().equals("java.util.ArrayList") 
				&& setter.getParameterTypes()[0].getCanonicalName().equals("java.lang.String[]"));
	}

	private void set(Object bean, Method setter, Object value) {
		try {
			setter.invoke(bean, value);
		} catch (IllegalArgumentException e) {
	//		System.out.println("ERROR: " + e.getMessage());
			log.error(e.getMessage());
			// log.error("Object: " + bean);
		//	System.out.println("Setter: " + setter);
		//	log.error("Setter: " + setter);
		//	System.out.println("Value: " + value + " " + value.getClass().getName());
		//	log.error("Value: " + value);
			//log.error(e.getMessage() + ". Object: " + bean + ", Setter: " + setter + " Value: " + value);
		//	log.error(e.getMessage() + ": " + setter.getParameterTypes()[0].getCanonicalName());
			//log.error(e.getMessage() + ": " + value.getClass().getName());
		} catch (IllegalAccessException e) {
			log.error(e.getMessage() + ". Object: " + bean + ", Setter: " + setter + " Value: " + value);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage() + ". Object: " + bean + ", Setter: " + setter + " Value: " + value);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object convertObjects(Object valueObject, String prefix) {
		List items = getList(prefix);
		for (LinkedHashMap<String, Object> rawItem : (ArrayList<LinkedHashMap<String, Object>>)valueObject) {
			items.add(convertObject(prefix, rawItem));
		}
		return items;
	}

	private Object convertObject(String javaType, LinkedHashMap<String, Object> rawItem) {
		Object item = getInstance(javaType);
		populateItem(item, rawItem, javaType);
		return item;
	}

	private List<?> getList(String type) {
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
		} else if (type.equals(CONCEPTS)) {
			return new ArrayList<Concept>();
		} else if (type.equals(WEBRESOURCES)) {
			return new ArrayList<WebResource>();
		}
		return null;
	}

	private Object getInstance(String type) {
		if (type.equals(PROXIES)) {
			return new ProxyImpl();
		} else if (type.equals(AGGREGATIONS)) {
			return new AggregationImpl();
		} else if (type.equals(EUROPEANA_AGGREGATION)) {
			return new EuropeanaAggregationImpl();
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
		} else if (type.equals(WEBRESOURCES)) {
			return new WebResourceImpl();
		}
		return null;
	}

	private void populateItem(Object item, LinkedHashMap<String, Object> rawItem, String javaType) {
		for (String field : rawItem.keySet()) {
			Object value = rawItem.get(field);
			if (value == null) {
				continue;
			}
			Method setter = setters.get(javaType).get(field);
			if (setter == null) {
				continue;
			}
			if (isListToArray(value, setter)) {
				value = listToArray(value);
			}
			if (javaType.equals(RELATED_ITEMS) && field.equals("timestamp")) {
				value = new Date((Long)value);
			} else if (javaType.equals(PLACES) && (field.equals("latitude") || field.equals("longitude"))) {
				value = Float.parseFloat(Double.toString((Double)value));
			} else if (javaType.equals(AGGREGATIONS) && field.equals(WEBRESOURCES)) {
				value = convertObjects(value, field);
			} else if ((javaType.equals(PROXIES) && field.equals("edmType")) 
				|| (javaType.equals(RELATED_ITEMS) && field.equals("type"))) {
				value = DocType.safeValueOf((String)value);
			}
			set(item, setter, value);
		}
	}

	private List<Field> extractFields(Class<?> clazz) {
		Class<?> superClazz = clazz;
		List<Field> members = new ArrayList<Field>();
		while (superClazz != null && superClazz != Object.class) {
			members.addAll(Arrays.asList(superClazz.getDeclaredFields()));
			superClazz = superClazz.getSuperclass();
		}
		return members;
	}
}
