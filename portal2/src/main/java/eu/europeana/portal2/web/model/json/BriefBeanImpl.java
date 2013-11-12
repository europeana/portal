package eu.europeana.portal2.web.model.json;

import java.util.Date;
import java.util.List;
import java.util.Map;

import eu.europeana.corelib.definitions.solr.DocType;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;

public class BriefBeanImpl implements BriefBean {

	private String id;
	private String fullDocUrl;
	private Date timestamp;
	private String[] provider;
	private String[] edmDataProvider;
	private String[] edmObject;
	private int europeanaCompleteness;
	private String[] docType;
	private String[] language;
	private String[] year;
	private String[] rights;
	private String[] title;
	private String[] dcCreator;
	private String[] dcContributor;
	private String[] edmPlace;
	private List<Map<String, String>> edmPlacePrefLabel;
	private List<String> edmPlaceLatitude;
	private List<String> edmPlaceLongitude;
	private String[] edmTimespan;
	private List<Map<String, String>> edmTimespanLabel;
	private String[] edmTimespanBegin;
	private String[] edmTimespanEnd;
	private String[] edmAgentTerm;
	private List<Map<String, String>> edmAgentLabel;
	private String[] dctermsHasPart;
	private String[] dctermsSpatial;
//	private DocType type;
	private boolean isOptedOut;
	private String[] edmPreview;
	private float score;
	private Date timestampCreated;
	private Date timestampUpdated;
	
	@Override
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getFullDocUrl() {
		return fullDocUrl;
	}

	public void setFullDocUrl(String fullDocUrl) {
		this.fullDocUrl = fullDocUrl;
	}

	@Override
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String[] getProvider() {
		return provider;
	}

	public void setProvider(String[] provider) {
		this.provider = provider;
	}

	public String[] getEdmDataProvider() {
		return edmDataProvider;
	}

	public void setEdmDataProvider(String[] edmDataProvider) {
		this.edmDataProvider = edmDataProvider;
	}

	@Override
	public String[] getEdmObject() {
		return edmObject;
	}

	public void setEdmObject(String[] edmObject) {
		this.edmObject = edmObject;
	}

	@Override
	public int getEuropeanaCompleteness() {
		return europeanaCompleteness;
	}

	public void setEuropeanaCompleteness(int europeanaCompleteness) {
		this.europeanaCompleteness = europeanaCompleteness;
	}

	public String[] getDocType() {
		return docType;
	}

	public void setDocType(String[] docType) {
		this.docType = docType;
	}

	@Override
	public String[] getLanguage() {
		return language;
	}

	public void setLanguage(String[] language) {
		this.language = language;
	}

	@Override
	public String[] getYear() {
		return year;
	}

	public void setYear(String[] year) {
		this.year = year;
	}

	@Override
	public String[] getRights() {
		return rights;
	}

	public void setRights(String[] rights) {
		this.rights = rights;
	}

	@Override
	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	@Override
	public String[] getDcCreator() {
		return dcCreator;
	}

	public void setDcCreator(String[] dcCreator) {
		this.dcCreator = dcCreator;
	}

	@Override
	public String[] getDcContributor() {
		return dcContributor;
	}

	public void setDcContributor(String[] dcContributor) {
		this.dcContributor = dcContributor;
	}

	@Override
	public String[] getEdmPlace() {
		return edmPlace;
	}

	public void setEdmPlace(String[] edmPlace) {
		this.edmPlace = edmPlace;
	}

	public List<Map<String, String>> getEdmPlacePrefLabel() {
		return edmPlacePrefLabel;
	}

	public void setEdmPlacePrefLabel(List<Map<String, String>> edmPlacePrefLabel) {
		this.edmPlacePrefLabel = edmPlacePrefLabel;
	}

	@Override
	public List<String> getEdmPlaceLatitude() {
		return edmPlaceLatitude;
	}

	public void setEdmPlaceLatitude(List<String> edmPlaceLatitude) {
		this.edmPlaceLatitude = edmPlaceLatitude;
	}

	@Override
	public List<String> getEdmPlaceLongitude() {
		return edmPlaceLongitude;
	}

	public void setEdmPlaceLongitude(List<String> edmPlaceLongitude) {
		this.edmPlaceLongitude = edmPlaceLongitude;
	}

	@Override
	public String[] getEdmTimespan() {
		return edmTimespan;
	}

	public void setEdmTimespan(String[] edmTimespan) {
		this.edmTimespan = edmTimespan;
	}

	@Override
	public List<Map<String, String>> getEdmTimespanLabel() {
		return edmTimespanLabel;
	}

	public void setEdmTimespanLabel(List<Map<String, String>> edmTimespanLabel) {
		this.edmTimespanLabel = edmTimespanLabel;
	}

	@Override
	public String[] getEdmTimespanBegin() {
		return edmTimespanBegin;
	}

	public void setEdmTimespanBegin(String[] edmTimespanBegin) {
		this.edmTimespanBegin = edmTimespanBegin;
	}

	@Override
	public String[] getEdmTimespanEnd() {
		return edmTimespanEnd;
	}

	public void setEdmTimespanEnd(String[] edmTimespanEnd) {
		this.edmTimespanEnd = edmTimespanEnd;
	}

	public String[] getEdmAgentTerm() {
		return edmAgentTerm;
	}

	public void setEdmAgentTerm(String[] edmAgentTerm) {
		this.edmAgentTerm = edmAgentTerm;
	}

	@Override
	public List<Map<String, String>> getEdmAgentLabel() {
		return edmAgentLabel;
	}

	public void setEdmAgentLabel(List<Map<String, String>> edmAgentLabel) {
		this.edmAgentLabel = edmAgentLabel;
	}

	@Override
	public String[] getDctermsHasPart() {
		return dctermsHasPart;
	}

	public void setDctermsHasPart(String[] dctermsHasPart) {
		this.dctermsHasPart = dctermsHasPart;
	}

	@Override
	public String[] getDctermsSpatial() {
		return dctermsSpatial;
	}

	public void setDctermsSpatial(String[] dctermsSpatial) {
		this.dctermsSpatial = dctermsSpatial;
	}

	@Override
	public DocType getType() {
		return DocType.safeValueOf(docType);
	}

	public void setType(DocType type) {
//		this.type = type;
		this.docType = new String[]{type.name()};
	}

	@Override
	public String[] getEdmAgent() {
		return (this.edmAgentTerm != null ? this.edmAgentTerm.clone() : null);
	}

	@Override
	public String[] getDataProvider() {
		return (this.edmDataProvider != null ? this.edmDataProvider.clone() : null);
	}

	@Override
	public List<Map<String, String>> getEdmPlaceLabel() {
		return edmPlacePrefLabel;
	}

	@Override
	public Boolean isOptedOut() {
		return isOptedOut;
	}

	public void setOptedOut(boolean isOptedOut) {
		this.isOptedOut = isOptedOut;
	}

	@Override
	public String[] getEdmPreview() {
		return this.edmPreview;
	}
	
	@Override
	public float getScore() {
		return this.score;
	}
	@Override
	public Date getTimestampCreated() {
		return this.timestampCreated;
	}
	@Override
	public Date getTimestampUpdated() {
		return this.timestampUpdated;
	}
	
}
