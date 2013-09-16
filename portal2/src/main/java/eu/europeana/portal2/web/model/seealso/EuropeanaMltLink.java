package eu.europeana.portal2.web.model.seealso;

public class EuropeanaMltLink {

	/**
	 * The Id of a record
	 */
	private String id;

	/**
	 * The title of the record
	 */
	private String title;

	public EuropeanaMltLink(String id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
