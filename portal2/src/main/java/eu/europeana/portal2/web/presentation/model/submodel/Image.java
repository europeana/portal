package eu.europeana.portal2.web.presentation.model.submodel;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.web.model.mediaservice.MediaService;
import eu.europeana.portal2.web.presentation.model.data.submodel.RightsValue;

public class Image implements SortableImage {

	String thumbnail;
	String full;
	String escapedFull;
	String rights;
	RightsValue rightsValue;
	String about;
	String isNextInSequence;
	MediaService mediaService;

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

	public Image(String thumbnail, String full, String type, String edmField, String rights) {
		this(thumbnail, full, type, edmField);
		this.rights       = rights;
		this.rightsValue = RightsValue.safeValueByUrl(rights, "", null);
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

	public String getRights() {
		return rights;
	}
	
	public RightsValue getRightsValue() {
		return rightsValue;
	}

	public void setRights(String rights) {
		this.rights = rights;
		this.rightsValue = RightsValue.safeValueByUrl(rights, "",null);
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getIsNextInSequence() {
		return isNextInSequence;
	}

	public void setIsNextInSequence(String isNextInSequence) {
		this.isNextInSequence = isNextInSequence;
	}

	public MediaService getMediaService() {
		return mediaService;
	}

	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}

	@Override
	public String toString() {
		return "Image [thumbnail=" + thumbnail + ", full=" + full + ", type=" + type + "]";
	}
}
