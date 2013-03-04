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

	private static final Map<String, String> EUROPEANA_RIGHTS_MAP = new HashMap<String, String>();
	private static final Pattern EUROPEANA_PATTERN = Pattern.compile(
			"^.*europeana\\.eu/rights/([\\w-]+)[/\\w.]*$");
	private static final Pattern CREATIVE_COMMONS_PATTERN = Pattern.compile(
			"(^.*creativecommons\\.org/licenses/)([a-z\\-]+)/([0-9\\.]+)/?([a-z\\.]+?)?/?$");
	private static final Pattern CREATIVE_COMMONS_PD_PATTERN = Pattern.compile(
			"^.*creativecommons\\.org/publicdomain/mark/([0-9\\.]+)/.*$");

	static {
		EUROPEANA_RIGHTS_MAP.put("rr-p", "Europeana - Rights Reserved - Paid Access");
		EUROPEANA_RIGHTS_MAP.put("rr-f", "Europeana - Rights reserved - Free access");
		EUROPEANA_RIGHTS_MAP.put("rr-r", "Europeana - Rights Reserved - Restricted Access");
		EUROPEANA_RIGHTS_MAP.put("pd", "Public Domain");
		EUROPEANA_RIGHTS_MAP.put("unknown", "Europeana - Unknown copyright status");
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
		if ((matcher = EUROPEANA_PATTERN.matcher(uri)).matches()) {
			return tryEuropeanaLicenses(matcher);
		}
		if ((matcher = CREATIVE_COMMONS_PATTERN.matcher(uri)).matches()) {
			return tryCcLicenses(matcher);
		}
		if ((matcher = CREATIVE_COMMONS_PD_PATTERN.matcher(uri)).matches()) {
			return tryCcPublicDomain(matcher);
		}
		return new License(uri);
	}

	public static String convertCc(String original) {
		Matcher matcher;
		License license;
		if ((matcher = CREATIVE_COMMONS_PATTERN.matcher(original)).matches()) {
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
		for (int i = 1, max = matcher.groupCount(); i < max; i++) {
			result.append(matcher.group(i).toUpperCase()).append(" ");
		}
		result.append(matcher.group(matcher.groupCount()));
		return new License(matcher.group(), String.format("%s", EUROPEANA_RIGHTS_MAP.get(result.toString())));
	}

	/**
	 * Tries to convert a URI in the format
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
	 * Tries to convert a URI in the format
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
		private boolean modified = false;

		public License(String originalURI) {
			this.originalURI = originalURI;
		}

		public License(String originalURI, String label) {
			this.originalURI = originalURI;
			this.label = label;
			this.modifiedURI = originalURI;
		}

		/**
		 * Construct new license
		 * 
		 * @param originalURI The original URI
		 * @param label Label to use
		 * @param modifiedURI The modified URI
		 */
		public License(String originalURI, String label, String modifiedURI) {
			this.originalURI = originalURI;
			this.label = label;
			this.modifiedURI = modifiedURI;
			modified = true;
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

		public boolean isModified() {
			return modified;
		}
	}
}
