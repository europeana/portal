package eu.europeana.portal2.web.presentation.model;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	private String reusability;

	// record parameters
	private String collectionId;
	private String recordId;

	// suggestions parameters
	private boolean phrases;

	private List<String> supportedFunctions = Arrays.asList(new String[]{"search", "record", "suggestions"});
	// TODO: add back "spelling"
	private Map<String, Boolean> defaultSearchProfiles = new LinkedHashMap<String, Boolean>(){
		private static final long serialVersionUID = 1L;
		{
			put("standard", false);
			put("portal", false);
			put("facets", false);
			put("breadcrumb", false);
			put("minimal", false);
			put("params", false);
		}
	};

	private Map<String, Boolean> defaultObjectProfiles = new LinkedHashMap<String, Boolean>(){
		private static final long serialVersionUID = 1L;
		{
			put("full", false);
			put("similar", false);
			put("params", false);
		}
	};

	private List<String> defaultRows = Arrays.asList(new String[]{"12", "24", "48", "96"});

	private List<String> supportedReusabilityValues = Arrays.asList(new String[]{"free", "limited"});

	private List<String> profiles;

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

	public Map<String, Boolean> getDefaultSearchProfiles() {
		return defaultSearchProfiles;
	}

	public Map<String, Boolean> getDefaultObjectProfiles() {
		return defaultObjectProfiles;
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

	public String getReusability() {
		return reusability;
	}

	public void setReusability(String reusability) {
		this.reusability = reusability;
	}

	public List<String> getSupportedReusabilityValues() {
		return supportedReusabilityValues;
	}
}
