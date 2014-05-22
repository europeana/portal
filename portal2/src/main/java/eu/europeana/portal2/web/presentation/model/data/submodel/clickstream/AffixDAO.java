package eu.europeana.portal2.web.presentation.model.data.submodel.clickstream;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.joda.time.DateTime;

import eu.europeana.portal2.services.impl.ClickStreamLogServiceImpl;

@JsonSerialize(include = Inclusion.NON_EMPTY)
public class AffixDAO {

	private String userId;
	private String sessionId;
	private String language;
	private DateTime date = new DateTime();
	private String ip;
	private String reqUrl;
	private String userAgent;
	private String referer;
	private String version = ClickStreamLogServiceImpl.VERSION;
	private String utma;
	private String utmb;
	private String utmc;
	private String tracSession;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	@JsonProperty("lang")
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDate() {
		return date.toString();
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@JsonProperty("req")
	public String getReqUrl() {
		return reqUrl;
	}

	public void setReqUrl(String reqUrl) {
		this.reqUrl = reqUrl;
	}

	@JsonProperty("user-agent")
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	@JsonProperty("v")
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUtma() {
		return utma;
	}

	public void setUtma(String utma) {
		this.utma = utma;
	}

	public String getUtmb() {
		return utmb;
	}

	public void setUtmb(String utmb) {
		this.utmb = utmb;
	}

	public String getUtmc() {
		return utmc;
	}

	public void setUtmc(String utmc) {
		this.utmc = utmc;
	}

	@JsonProperty("trac_session")
	public String getTracSession() {
		return tracSession;
	}

	public void setTracSession(String tracSession) {
		this.tracSession = tracSession;
	}
}
