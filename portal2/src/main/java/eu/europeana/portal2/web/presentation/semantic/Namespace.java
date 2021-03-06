package eu.europeana.portal2.web.presentation.semantic;

public class Namespace {
	private String prefix;
	private String uri;

	public Namespace(String prefix, String uri) {
		this.prefix = prefix;
		this.uri = uri;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public String toString() {
		return "Namespace [prefix=" + prefix + ", uri=" + uri + "]";
	}
}
