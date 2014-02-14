package eu.europeana.portal2.web.model.mlt;

public class EuropeanaMltLink {

	/**
	 * The Id of a record
	 */
	private String id;

	/**
	 * The title of the record
	 */
	private String title;

	/**
	 * The fullDocUrl
	 */
	private String fullDocUrl;

	/**
	 * The thumbnail of record
	 */
	private String thumbnail;

	public EuropeanaMltLink(String id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public EuropeanaMltLink(String id, String title, String fullDocUrl, String thumbnail) {
		this(id, title);
		this.fullDocUrl = fullDocUrl;
		this.thumbnail = thumbnail;
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

	public String getFullDocUrl() {
		return fullDocUrl;
	}

	public void setFullDocUrl(String fullDocUrl) {
		this.fullDocUrl = fullDocUrl;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
}
