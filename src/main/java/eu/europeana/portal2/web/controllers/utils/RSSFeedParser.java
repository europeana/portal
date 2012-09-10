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

import org.eclipse.jetty.util.log.Log;
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

	boolean useNormalImageFormat = true;
	private final URL url;

	String staticPagePath = "";
	
	private final Logger log = Logger.getLogger(getClass().getName());

	public void setStaticPagePath(String staticPagePath) {
		Log.info("setStaticPagePath " + staticPagePath);
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

	private Map<Integer, String> createResponsiveImage(String location) throws IOException{
		
		Map<Integer, String> result = new HashMap<Integer, String>();
		
		int[] widths = new int[]{200, 150, 90};// TODO: set in properties
		
		String[] fNames = new String[]{ "_1", "_2", "_3"};// TODO: set in properties
		

		String directory = staticPagePath;
		String toFS = location.replace("/", "-").replace(":", "-");
		String extension = toFS.substring(toFS.lastIndexOf("."));
		
		for(int i=0; i<widths.length; i++){
			
			//http://blog.europeana.eu/wp-content/uploads/2012/09/794px-Participating_Countries_WLM_2012.svg_.png
			//String directory	= "blog.europeana.eu";     // set in properties 
			String newFileName	= directory + "rss-blog-cache/" + toFS.substring( 0, toFS.lastIndexOf("."))  + fNames[i] + extension; 

			System.err.println("new filename is " + newFileName + ", old filename is " + location);
			
			//BufferedImage orig = ImageIO.read( getClass().getResourceAsStream("/images/GREATWAR.jpg") );
			BufferedImage orig = ImageIO.read( new URL( location )  );// getClass().getResourceAsStream(location ) );
			
			BufferedImage reponsive = ImageUtils.scale(orig, widths[i], 200);

			result.put(widths[i], newFileName);
		
		    java.io.File outputfile = new java.io.File(	newFileName);
		    
		    if(!outputfile.exists()){
		    	
				System.err.println("create: >>>" + outputfile.getAbsoluteFile()  + "<<<");
				try{					
					outputfile.createNewFile();
					javax.imageio.ImageIO.write(reponsive, extension, outputfile);
				}
				catch(Exception e){
					e.printStackTrace();
				}
				System.err.println("created");
		    }
		}
		return result;
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
				try{

					for (RSSImage image : message.getImages()){
						  Map<Integer,String> responsiveFileNames = createResponsiveImage(image.getSrc());
						  image.setResponsiveFileNames(responsiveFileNames);
					}

				}
				catch(IOException e){
					e.printStackTrace();
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
