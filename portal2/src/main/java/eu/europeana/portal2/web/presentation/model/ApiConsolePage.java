package eu.europeana.portal2.web.presentation.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import eu.europeana.corelib.web.model.rights.RightReusabilityCategorizer;
import eu.europeana.portal2.web.presentation.model.data.ApiData;

/**
 * Model for API requests
 * 
 * @author peter.kiraly@kb.nl
 */
public class ApiConsolePage extends ApiData {

	private String function;
	private String jsonString;
	private String apiUrl;
	private Integer httpStatusCode;

	// search parameters
	private String query;
	private String[] refinements;
	private String profile;
	private int start;
	private int rows;
	private String sort;
	private String callback;
	private String latMin;
	private String latMax;
	private String longMin;
	private String longMax;
	private String yearMin;
	private String yearMax;
	private String[] reusability;
	private Map<String, Boolean> reusabilityMap;

	// record parameters
	private String collectionId;
	private String recordId;

	// suggestions parameters
	private boolean phrases = false;
	// is console embedded?
	private boolean isEmbeddedConsole = false;

	private static List<String> supportedFunctions = new ArrayList<String>();
	static {
		supportedFunctions.add("search");
		supportedFunctions.add("record");
		supportedFunctions.add("suggestions");
	}

	// TODO: add back "spelling"
	private static Map<String, Boolean> defaultSearchProfiles = new LinkedHashMap<String, Boolean>();
	static {
		defaultSearchProfiles.put("standard", false);
		defaultSearchProfiles.put("portal", false);
		defaultSearchProfiles.put("facets", false);
		defaultSearchProfiles.put("breadcrumb", false);
		defaultSearchProfiles.put("minimal", false);
		defaultSearchProfiles.put("params", false);
	}
	private Map<String, Boolean> searchProfiles = new LinkedHashMap<String, Boolean>();;

	private static final Map<String, Boolean> defaultObjectProfiles = new LinkedHashMap<String, Boolean>();
	static {
		defaultObjectProfiles.put("full", false);
		defaultObjectProfiles.put("similar", false);
		defaultObjectProfiles.put("params", false);
	}
	private Map<String, Boolean> objectProfiles = new LinkedHashMap<String, Boolean>();

	private static List<String> defaultRows = new ArrayList<String>();
	static {
		defaultRows.add("12");
		defaultRows.add("24");
		defaultRows.add("48");
		defaultRows.add("96");
	}

	private static List<String> supportedReusabilityValues = new ArrayList<String>();
	static {
		supportedReusabilityValues.add(RightReusabilityCategorizer.OPEN);
		supportedReusabilityValues.add(RightReusabilityCategorizer.RESTRICTED);
	}

	private List<String> profiles;
	private List<String> requestHeaders;
	private List<String> responseHeaders;

	public ApiConsolePage() {
		objectProfiles.putAll(defaultObjectProfiles);
		searchProfiles.putAll(defaultSearchProfiles);
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public Map<String, Boolean> getSearchProfiles() {
		return searchProfiles;
	}

	public Map<String, Boolean> getObjectProfiles() {
		return objectProfiles;
	}

	public List<String> getDefaultRows() {
		return defaultRows;
	}

	public List<String> getSupportedFunctions() {
		return supportedFunctions;
	}

	@Override
	public String getQuery() {
		return query;
	}

	@Override
	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public String[] getRefinements() {
		return refinements;
	}

	@Override
	public void setRefinements(String[] refinements) {
		this.refinements = refinements;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public void setStart(int start) {
		this.start = start;
	}

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public void setRows(int rows) {
		this.rows = rows;
	}

	@Override
	public String getSort() {
		return sort;
	}

	@Override
	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public boolean isPhrases() {
		return phrases;
	}

	public void setPhrases(boolean phrases) {
		this.phrases = phrases;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public Integer getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(Integer httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getLatMin() {
		return latMin;
	}

	public void setLatMin(String latMin) {
		this.latMin = latMin;
	}

	public String getLatMax() {
		return latMax;
	}

	public void setLatMax(String latMax) {
		this.latMax = latMax;
	}

	public String getLongMin() {
		return longMin;
	}

	public void setLongMin(String longMin) {
		this.longMin = longMin;
	}

	public String getLongMax() {
		return longMax;
	}

	public void setLongMax(String longMax) {
		this.longMax = longMax;
	}

	public String getYearMin() {
		return yearMin;
	}

	public void setYearMin(String yearMin) {
		this.yearMin = yearMin;
	}

	public String getYearMax() {
		return yearMax;
	}

	public void setYearMax(String yearMax) {
		this.yearMax = yearMax;
	}

	public List<String> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<String> profiles) {
		this.profiles = profiles;
	}

	public Map<String, Boolean> getReusability() {
		if (reusabilityMap == null) {
			reusabilityMap = new HashMap<String, Boolean>();
			for (String key : RightReusabilityCategorizer.getReusabilityValueMap().keySet()) {
				reusabilityMap.put(key, ArrayUtils.contains(reusability, key));
			}
		}
		return reusabilityMap;
	}

	public void setReusability(String[] reusability) {
		this.reusability = reusability;
	}

	public Map<String, String> getSupportedReusabilityValues() {
		return RightReusabilityCategorizer.getReusabilityValueMap();
	}

	public boolean isEmbeddedConsole() {
		return isEmbeddedConsole;
	}

	public void setEmbeddedConsole(boolean isEmbeddedConsole) {
		this.isEmbeddedConsole = isEmbeddedConsole;
	}

	public void setResponseHeaders(List<String> list) {
		this.responseHeaders = list;
	}

	public List<String> getResponseHeaders() {
		return responseHeaders;
	}

	public void setRequestHeaders(List<String> list) {
		this.requestHeaders = list;
	}

	public List<String> getRequestHeaders() {
		return requestHeaders;
	}
}
