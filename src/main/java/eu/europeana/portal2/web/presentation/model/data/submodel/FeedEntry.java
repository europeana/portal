/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved 
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 *  
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under 
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of 
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under 
 *  the Licence.
 */

package eu.europeana.portal2.web.presentation.model.data.submodel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import eu.europeana.portal2.web.controllers.utils.RSSImage;

public class FeedEntry {

	private String title;
	private String description;
	private String link;
	private String author;
	private String guid;
	private Date pubDate;
	
	/**
	 * List of images extracted from the description. Each image has an src and a title field.
	 */
	private List<RSSImage> images;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		if (StringUtils.length(description) > 160) {
			return StringUtils.abbreviate(description, 160);
		}
		return description;
	}

	public String getDescriptionFull() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public void setPubDate(String str) {
		if (StringUtils.isNotBlank(str)) {
			DateFormat formatter = new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
			try {
				pubDate = formatter.parse(str);
			} catch (ParseException e) {
			}
		}
	}

	public Date getPubDate() {
		return pubDate;
	}

	public List<RSSImage> getImages() {
		return images;
	}

	public void setImages(List<RSSImage> images) {
		this.images = images;
	}
}
