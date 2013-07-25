package eu.europeana.portal2.web.presentation.model;

import org.apache.commons.lang.StringUtils;

public class Image {

	String thumbnail;
	String full;
	String escapedFull;

	/**
	 * The type of image (text, video, image, 3d)
	 */
	String type;

	/**
	 * The edm field where the image located
	 */
	String edmField;

	public Image(String thumbnail, String full, String type) {
		this.thumbnail = thumbnail;
		this.full = full;
		this.type = type;
		if (!StringUtils.isBlank(full)) {
			escapedFull = full.replace("\\", "\\\\");
		}
	}

	public Image(String thumbnail, String full, String type, String edmField) {
		this(thumbnail, full, type);
		this.edmField = edmField;
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

	public String getEscapedFull() {
		return escapedFull;
	}

	public void setEscapedFull(String escapedFull) {
		this.escapedFull = escapedFull;
	}

	public String getEdmField() {
		return edmField;
	}

	public void setEdmField(String edmField) {
		this.edmField = edmField;
	}

	@Override
	public String toString() {
		return "Image [thumbnail=" + thumbnail + ", full=" + full + ", type=" + type + "]";
	}
}
