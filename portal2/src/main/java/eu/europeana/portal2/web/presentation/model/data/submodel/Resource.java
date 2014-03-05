package eu.europeana.portal2.web.presentation.model.data.submodel;

public class Resource {

	private String value;
	private String uri;

	public Resource(String value, String uri) {
		super();
		this.value = value;
		this.uri = uri;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}