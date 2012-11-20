package eu.europeana.portal2.web.presentation.model;

public class Image {

	String thumbnail;
	String full;
	String type;

	public Image(String thumbnail, String full, String type) {
		this.thumbnail = thumbnail;
		this.full = full;
		this.type = type;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getFull() {
		return full;
	}

	public void setFull(String full) {
		this.full = full;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Image [thumbnail=" + thumbnail + ", full=" + full + ", type=" + type + "]";
	}
}
