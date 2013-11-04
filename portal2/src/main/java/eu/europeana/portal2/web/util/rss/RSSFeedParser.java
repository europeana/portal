/*
 * Copyright 2007-2013 The Europeana Foundation
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

package eu.europeana.portal2.web.util.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.europeana.portal2.services.ResponsiveImageService;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeedEntry;

public class RSSFeedParser {

	Logger log = Logger.getLogger(RSSFeedParser.class.getCanonicalName());

	static final String TITLE = "title";
	static final String DESCRIPTION = "description";
	static final String CHANNEL = "channel";
	static final String LANGUAGE = "language";
	static final String COPYRIGHT = "copyright";
	static final String LINK = "link";
	static final String AUTHOR = "author";
	static final String ITEM = "item";
	static final String PUB_DATE = "pubDate";
	static final String GUID = "guid";

	boolean useNormalImageFormat = true;
	private final URL url;

	String staticPagePath = "";

	public void setStaticPagePath(String staticPagePath) {
		this.staticPagePath = staticPagePath;
	}

	private int itemLimit;

	public RSSFeedParser(String feedUrl, int itemLimit) {
		try {
			this.url = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		this.itemLimit = itemLimit;
	}

	public RSSFeedParser(String feedUrl, int itemLimit, boolean useNormalImageFormat) {
		this(feedUrl, itemLimit);
		this.useNormalImageFormat = useNormalImageFormat;
	}

	public List<FeedEntry> readFeed(ResponsiveImageService responsiveImageService) {
		try {
			List<FeedEntry> feeds = new ArrayList<FeedEntry>();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(url.openStream());
			NodeList nodes = doc.getElementsByTagName("item");
			for (int i = 0; i < Math.min(nodes.getLength(), itemLimit); i++) {
				Element element = (Element) nodes.item(i);
				FeedEntry message = new FeedEntry();
				if (element.getElementsByTagName(AUTHOR).getLength() > 0) {
					message.setAuthor(getElementValue(element, "dc:creator"));
				}

				String description = getElementValue(element, DESCRIPTION);
				message.setGuid(getElementValue(element, GUID));
				message.setLink(getElementValue(element, LINK));
				message.setTitle(getElementValue(element, TITLE));
				message.setPubDate(getElementValue(element, PUB_DATE));
				message.setDescription(description);
				message.setImages(RSSImageExtractor.extractImages(description, useNormalImageFormat));
				// if no images, tries "content:encoded" element
				if (message.getImages().size() == 0) {
					message.setImages(
						RSSImageExtractor.extractImages(
							getElementValue(element, "content:encoded"), 
							useNormalImageFormat
						)
					);
				}
				// now we have the image URLs
				boolean hasImage = false;
				for (RSSImage image : message.getImages()){
					if (image.getSrc() == null) {
						continue;
					}
					try {
						Map<String, String> responsiveFileNames = responsiveImageService.createResponsiveImage(image.getSrc());
						image.setResponsiveFileNames(responsiveFileNames);
						hasImage = true;
					} catch (Exception e) {
						if (image.getSrc() != null && message.getLink() != null) {
							log.severe(String.format("Error extracting image (%s) for blog %s", image.getSrc(), message.getLink()));
						}
					}
				}
				if (!hasImage) {
					log.warning(String.format("There is no extracted image for blog %s.", message.getLink()));
				}
				feeds.add(message);
			}
			return feeds;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getCharacterDataFromElement(Element e) {
		try {
			Node child = e.getFirstChild();
			if (child instanceof CharacterData) {
				CharacterData cd = (CharacterData) child;
				return cd.getData();
			}
		} catch (Exception ex) {
		}
		return "";
	} // private String getCharacterDataFromElement

	protected float getFloat(String value) {
		if (value != null && !value.equals(""))
			return Float.parseFloat(value);
		else
			return 0;
	}

	protected String getElementValue(Element parent, String label) {
		return getCharacterDataFromElement((Element) parent.getElementsByTagName(label).item(0));
	}
}
