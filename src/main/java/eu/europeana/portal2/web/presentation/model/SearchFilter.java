package eu.europeana.portal2.web.presentation.model;

import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

/**
 * Object for displaying active filters
 * 
 * @author peter.kiraly@kb.nl
 */
public class SearchFilter {

	private final Logger log = Logger.getLogger(getClass().getName());

	private SearchLabel label;

	private String url = null;

	private String urlFull = null; // Andy

	public SearchFilter(SearchLabel label, String url, String urlFull) {
		super();
		this.label = label;
		this.url = url;
		this.urlFull = urlFull;
	}

	public SearchLabel getLabelObject() {
		return label;
	}

	public void setLabel(SearchLabel label) {
		this.label = label;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlFull() {
		return urlFull;
	}

	public void setUrlFull(String urlFull) {
		this.urlFull = urlFull;
	}

	@Override
	public String toString() {
		return "SearchFilter [label=" + label + ", url=" + url + "]";
	}

	// Wrapper methods for label object
	public String getField() {
		return label.getField();
	}

	public String getFieldCode() {
		return label.getFieldCode();
	}

	public String getValue() {
		return label.getValue();
	}

	public String getLabel() {
		return label.getLabel();
	}
}
