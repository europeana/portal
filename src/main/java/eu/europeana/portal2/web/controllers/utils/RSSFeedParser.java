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

package eu.europeana.portal2.web.controllers.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.europeana.corelib.utils.ImageUtils;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeedEntry;

public class RSSFeedParser {

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

	int[] responsiveWidths;
	String[] fNames;
	
	boolean useNormalImageFormat = true;
	private final URL url;

	String staticPagePath = "";

	private final Logger log = Logger.getLogger(getClass().getName());

	public void setStaticPagePath(String staticPagePath) {
		this.staticPagePath = staticPagePath;
	}

	private int itemLimit;

	public RSSFeedParser(String feedUrl, int itemLimit, String[] fNames, int[] responsiveWidths) {
		try {
			this.url = new URL(feedUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		this.itemLimit			= itemLimit;
		this.fNames				= fNames;
		this.responsiveWidths	= responsiveWidths;
	}

	public RSSFeedParser(String feedUrl, int itemLimit, boolean useNormalImageFormat, String[] fNames, int[] responsiveWidths) {
		this(feedUrl, itemLimit, fNames, responsiveWidths);
		this.useNormalImageFormat = useNormalImageFormat;
	}

	
	private Map<String, String> createResponsiveImage(String location) {

		Map<String, String> responsiveImages = new HashMap<String, String>();

		String directory = staticPagePath + "/sp/rss-blog-cache/";
		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}

		String extension = location.substring(location.lastIndexOf(".") + 1);
		String nameWithoutExt = location.substring(0, location.lastIndexOf(".") - 1);
		String toFS = nameWithoutExt.replace("/", "-").replace(":", "-").replace(".", "-");
		BufferedImage orig = null;
		try {
			orig = ImageIO.read(new URL(location));
		} catch (MalformedURLException e) {
			log.severe("MalformedURLException during reading in location: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.severe("IOException during reading in location: " + e.getLocalizedMessage());
			e.printStackTrace();
		}

		if (orig == null) {
			return responsiveImages;
		}

		for (int i = 0, l = responsiveWidths.length; i<l; i++){
			String fileName = toFS + fNames[i] + "." + extension;
			
			// work out new image name 
			String fileUrl = "/sp/rss-blog-cache/" + fileName;
			String filePath = directory + fileName; 

			log.info(String.format("new file is %s, url is %s, old file is %s, ", filePath, fileUrl, location));
			BufferedImage responsive = null;
			try {
				responsive = ImageUtils.scale(orig, responsiveWidths[i], 0);	// zero-height to auto-calculate
			} catch (IOException e) {
				log.severe("IOException during scaling image: " + e.getLocalizedMessage());
				e.printStackTrace();
			}
			if (responsive == null) {
				continue;
			}
			responsiveImages.put(fNames[i], fileUrl);

			File outputfile = new File(filePath);
			if (!outputfile.exists()) {
				boolean created = false;
				try {
					created = outputfile.createNewFile();
				} catch (IOException e) {
					log.severe("IOException during create new file: " + e.getLocalizedMessage());
					e.printStackTrace();
				}

				if (created) {
					try {
						ImageIO.write(responsive, extension, outputfile);
					} catch (IOException e) {
						log.severe("IOException during writing new file: " + e.getLocalizedMessage());
						e.printStackTrace();
					}
					log.info("created");
				}
			}
		}
		return responsiveImages;
	}

	public List<FeedEntry> readFeed() {
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
				for (RSSImage image : message.getImages()){
					Map<String, String> responsiveFileNames = createResponsiveImage(image.getSrc());
					image.setResponsiveFileNames(responsiveFileNames);
				}
				message.setGuid(getElementValue(element, GUID));
				message.setLink(getElementValue(element, LINK));
				message.setTitle(getElementValue(element, TITLE));
				message.setPubDate(getElementValue(element, PUB_DATE));
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
