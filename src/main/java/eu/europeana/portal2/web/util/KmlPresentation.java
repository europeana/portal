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

package eu.europeana.portal2.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * KML Presentation helper class
 * 
 * @author wjboogerd
 * 
 */
public class KmlPresentation {

	public static String getKmlDescriptor(String url, String imageCacheUrl,
			String thumbnail, String name, String descr)
			throws UnsupportedEncodingException {

		StringBuilder sb = new StringBuilder();

		sb.append("<table border=\"0\" padding=\"1\">");
		sb.append("<tr>");
		sb.append("<td><a href=\"");
		sb.append(url);
		sb.append("?bt=kml");
		sb.append("\">");
		sb.append("<img src=\"");
		sb.append(imageCacheUrl);
		sb.append("uri=");
		sb.append(URLEncoder.encode(thumbnail, "UTF-8"));
		sb.append("&amp;size=BRIEF_DOC&amp;type=IMAGE\" height=\"110\"/>");
		sb.append("</a></td>");
		sb.append("<td><b>");
		sb.append(name);
		sb.append("</b><br/>");
		sb.append(descr);
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("<p align=right><font color=\"#008B8B\"><a href=\"http://www.europeana.eu\">Europeana.eu</a></font></p>");

		return StringEscapeUtils.escapeXml(sb.toString());
	}

	// override
	public static String getKmlDescriptor(String url, String imageCacheUrl,
			String thumbnail, String name, String descr, String date,
			String place) throws UnsupportedEncodingException {

		StringBuilder sb = new StringBuilder();

		sb.append("<div class=\"bubble-content\">");
		sb.append("<a href=\"");
		sb.append(url);
		sb.append("?bt=kml");
		sb.append("\">");
		sb.append("<img src=\"");
		sb.append(imageCacheUrl);
		sb.append("uri=");
		sb.append(URLEncoder.encode(thumbnail, "UTF-8"));
		sb.append("&amp;size=BRIEF_DOC&amp;type=IMAGE\" height=\"110\" ");
		sb.append("alt=\"");
		sb.append(name);
		sb.append("\"/>");
		sb.append(name);
		sb.append("</a>");
		sb.append("<ul>");
		sb.append("<li>");
		sb.append("<span class=\"field-name\">");
		sb.append("Date: "); // TODO localise
		sb.append("</span>");
		sb.append(date);
		sb.append("</li>");
		sb.append("<li>");
		sb.append("<span class=\"field-name\">");
		sb.append("Place: "); // TODO localise
		sb.append("</span>");
		sb.append(place);
		sb.append("</li>");
		/*
		 * sb.append("<li>"); sb.append("<span class=\"field-name\">");
		 * sb.append("Description: "); // TODO localise sb.append("</span>");
		 * sb.append(descr); sb.append("</li>");
		 */
		sb.append("</ul>");

		return StringEscapeUtils.escapeXml(sb.toString());
	}
}
