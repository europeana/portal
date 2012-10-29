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

package eu.europeana.portal2.querymodel.query;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Convert license URI's to shorter and more readable notations.
 * 
 * @author Serkan Demirel, <serkan@blackbuilt.nl>
 */
public final class EuropeanaRightsConverter {

	private static final String EUROPEANA = "^.*europeana\\.eu/rights/([\\w-]+)[/\\w.]*$";
	private static final String CREATIVE_COMMONS = "(^.*creativecommons\\.org/licenses/)([a-z\\-]+)/([0-9\\.]+)/?([a-z\\.]+?)?/?$";
	private static final String CREATIVE_COMMONS_PD = "^.*creativecommons\\.org/publicdomain/mark/([0-9\\.]+)/.*$";
	private static final Map<String, String> EUROPEANA_RIGHTS_MAP = new HashMap<String, String>();

	static {
		EUROPEANA_RIGHTS_MAP.put("rr-p", "Paid Access - rights reserved");
		EUROPEANA_RIGHTS_MAP.put("rr-f", "Free Access - rights reserved");
		EUROPEANA_RIGHTS_MAP.put("rr-r", "Restricted Access - rights reserved");
		EUROPEANA_RIGHTS_MAP.put("pd", "Public Domain");
		EUROPEANA_RIGHTS_MAP.put("unknown", "Unknown copyright status");
	}

	/**
	 * Convert the provided URI to any of the configured license types.
	 * 
	 * @param uri
	 *         The provided license URI.
	 * 
	 * @return The extracted information about the URI or the original URI if
	 *         not matched.
	 */
	public static License convert(String uri) {
		Matcher matcher;
		if ((matcher = Pattern.compile(EUROPEANA).matcher(uri)).matches()) {
			return tryEuropeanaLicenses(matcher);
		}
		if ((matcher = Pattern.compile(CREATIVE_COMMONS).matcher(uri)).matches()) {
			return tryCcLicenses(matcher);
		}
		if ((matcher = Pattern.compile(CREATIVE_COMMONS_PD).matcher(uri)).matches()) {
			return tryCcPublicDomain(matcher);
		}
		return new License(uri);
	}

	public static String convertCc(String original) {
		Matcher matcher;
		License license;
		if ((matcher = Pattern.compile(CREATIVE_COMMONS).matcher(original)).matches()) {
			license = tryCcLicenses(matcher);
			return license.getModifiedURI();
		}
		return "";
	}

	/**
	 * Tries to convert a URI with the format
	 * http://www.europeana.eu/rights/TYPE/ to (Europeana TYPE).
	 * 
	 * @param matcher
	 *            The matcher containing the URI.
	 * 
	 * @return The extracted information about the URI or null if not matched.
	 */
	private static License tryEuropeanaLicenses(Matcher matcher) {
		StringBuilder result = new StringBuilder();
		for (int walk = 1; walk < matcher.groupCount(); walk++) {
			result.append(matcher.group(walk).toUpperCase()).append(" ");
		}
		result.append(matcher.group(matcher.groupCount()));
		return new License(matcher.group(), String.format("%s", EUROPEANA_RIGHTS_MAP.get(result.toString())));
	}

	/**
	 * Tries to convert a URI with the format
	 * http://creativecommons.org/licenses/TYPE/VERSION/COUNTRY/ to (CC TYPE
	 * VESRSION).
	 * 
	 * @param matcher
	 *            The matcher containing the URI.
	 * 
	 * @return The extracted information about the URI or null if not matched.
	 */
	private static License tryCcLicenses(Matcher matcher) {
		return new License(matcher.group(), 
			String.format("CC %s", matcher.group(2).toUpperCase().trim()), 
			String.format("%s%s/*", matcher.group(1), matcher.group(2))
		);
	}

	/**
	 * Tries to convert a URI with the format
	 * http://creativecommons.org/publicdomain/mark/VERSION/ to (CC Public
	 * Domain VERSION).
	 * 
	 * @param matcher
	 *            The matcher containing the URI.
	 * 
	 * @return The extracted information about the URI or null if not matched.
	 */
	private static License tryCcPublicDomain(Matcher matcher) {
		return new License(matcher.group(), "Public Domain");
	}

	/**
	 * License containing the original URI, the meaningful name and the modified
	 * URI.
	 */
	public static class License {

		private String originalURI;
		private String label;

		private String modifiedURI;

		public License(String originalURI) {
			this.originalURI = originalURI;
		}

		public License(String originalURI, String label) {
			this.originalURI = originalURI;
			this.label = label;
			this.modifiedURI = originalURI;
		}

		public License(String originalURI, String label, String modifiedURI) {
			this.originalURI = originalURI;
			this.label = label;
			this.modifiedURI = modifiedURI;
		}

		public String getOriginalURI() {
			return originalURI;
		}

		public String getLabel() {
			return label;
		}

		public String getModifiedURI() {
			return modifiedURI;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			License license = (License) o;
			return label.equals(license.label);
		}

		@Override
		public int hashCode() {
			return label.hashCode();
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("License");
			sb.append("{originalURI='").append(originalURI).append('\'');
			sb.append(", label='").append(label).append('\'');
			sb.append(", modifiedURI='").append(modifiedURI).append('\'');
			sb.append('}');
			return sb.toString();
		}
	}
}
