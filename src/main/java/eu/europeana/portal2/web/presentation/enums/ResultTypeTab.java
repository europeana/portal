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

package eu.europeana.portal2.web.presentation.enums;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class ResultTypeTab {

	public static final String TAB_ID_ALL = "all";
	public static final String TAB_ID_TEXT = "text";
	public static final String TAB_ID_IMAGE = "image";
	public static final String TAB_ID_SOUND = "sound";
	public static final String TAB_ID_VIDEO = "video";
	// public static final String TAB_ID_WIKIPEDIA = "wikipedia";
	public static final String TAB_ID_BLANK = "";

	public static final String TAB_TAG_ID_ALL = "All_t";
	public static final String TAB_TAG_ID_TEXT = "Texts_t";
	public static final String TAB_TAG_ID_IMAGE = "Images_t";
	public static final String TAB_TAG_ID_VIDEO = "Videos_t";
	public static final String TAB_TAG_ID_SOUND = "Sounds_t";
	// public static final String TAB_TAG_ID_WIKIPEDIA = "Wikipedia_t";
	public static final String TAB_TAG_ID_BLANK = "Blank_t";

	String tabType = null;
	long tabCount = 0;
	String selectedText = "";
	String url = "";

	boolean showCountAlways = false;

	/**
	 * Returns a collection of all the available media types available in
	 * Europeana
	 * 
	 * @return list of media types
	 */
	public static List<String> getResultTabTypes() {

		List<String> mediaResultTypes = new ArrayList<String>();

		mediaResultTypes.add(TAB_ID_ALL);
		mediaResultTypes.add(TAB_ID_TEXT);
		mediaResultTypes.add(TAB_ID_IMAGE);
		mediaResultTypes.add(TAB_ID_SOUND);
		mediaResultTypes.add(TAB_ID_VIDEO);
		// mediaResultTypes.add(TAB_ID_WIKIPEDIA);

		return mediaResultTypes;
	}

	public ResultTypeTab(String tabType, String selectedText, String url,
			boolean showCountAlways) {
		this.tabType = tabType;
		this.selectedText = selectedText;
		this.url = url;
		this.showCountAlways = showCountAlways;
	}

	public String getTabType() {
		return tabType;
	}

	public long getTabCount() {
		return tabCount;
	}

	public void setTabCount(long tabCount) {
		this.tabCount = tabCount;
	}

	public void setSelectedText(String selectedText) {
		this.selectedText = selectedText;
	}

	public String getSelectedText() {
		return selectedText;
	}

	public String getUrl() {
		return url;
	}

	/**
	 * Reutrns the a number formatted for use in the view.
	 * 
	 * @return formatted number of results
	 */
	public String getFormattedCount() {

		final char NUMBER_FORMAT_GROUP_SEPERATOR = ',';
		final String NUMBER_FORMAT = "###,###.##";

		if ("All_t".equals(tabType)) {
			return "";
		}

		if ((tabCount > 0 && !"".equals(selectedText)) || showCountAlways) {
			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
			symbols.setGroupingSeparator(NUMBER_FORMAT_GROUP_SEPERATOR);
			NumberFormat formatter = new DecimalFormat(NUMBER_FORMAT, symbols);
			return "(" + formatter.format(tabCount) + ")";
		} else {
			return "";
		}
	}
}
