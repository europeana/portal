package eu.europeana.portal2.web.presentation.model;

/**
 * Object for displaying active filters
 * 
 * @author peter.kiraly@kb.nl
 */
public class SearchFilter {

	private SearchLabel label;

	/**
	 * Link for the remove icon. It will remove the current search from the list of searches.
	 */
	private String removeLinkUrl = null;

	/**
	 * Breadbrumb link contains all previous and current search, but excludes the searches after the current one.
	 */
	private String breadcrumbLinkUrl = null;

	public SearchFilter(SearchLabel label, String removeLinkUrl, String breadcrumbLinkUrl) {
		super();
		this.label = label;
		this.removeLinkUrl = removeLinkUrl;
		this.breadcrumbLinkUrl = breadcrumbLinkUrl;
	}

	public SearchLabel getLabelObject() {
		return label;
	}

	public void setLabel(SearchLabel label) {
		this.label = label;
	}

	public String getRemoveLinkUrl() {
		return removeLinkUrl;
	}

	public void setRemoveLinkUrl(String url) {
		this.removeLinkUrl = url;
	}

	public String getBreadcrumbLinkUrl() {
		return breadcrumbLinkUrl;
	}

	public void setBreadcrumbLinkUrl(String urlFull) {
		this.breadcrumbLinkUrl = urlFull;
	}

	@Override
	public String toString() {
		return "SearchFilter [label=" + label + ", removeLinkUrl=" + removeLinkUrl + ", breadcrumbLinkUrl="
				+ breadcrumbLinkUrl + "]";
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
