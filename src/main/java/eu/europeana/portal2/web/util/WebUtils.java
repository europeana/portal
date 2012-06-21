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

import org.apache.commons.lang.StringUtils;

public final class WebUtils {

	public static String clean(String s) {
		s = StringUtils.trim(s);
		if (StringUtils.isNotEmpty(s)) {
			String regex = "\\s{2,}";
			s = s.replaceAll(regex, " ");
		}
		return s;
	}

	public static <T> T returnFirst(T[] list, T empty) {
		if (list == null || list.length == 0) {
			return empty;
		}
		return list[0];
	}

	public static String checkMimeType(String ref) {
		if (StringUtils.isBlank(ref)) {
			return null;
		}
		String[] supported = new String[] {
			// images
			"jpg", "jpeg", "gif", "png"
			// movies
			// "avi", "wmv", "flv", "mp4",
			// audio
			// "mp3", "flac",
			// text
			// "txt", "pdf"
		};
		for (String extention : supported) {
			if (StringUtils.endsWithIgnoreCase(ref, extention)) {
				return ref;
			}
		}
		return null;
	}
}
