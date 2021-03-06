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

package eu.europeana.portal2.web.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * Hold the languages used by portal. (Subset of the Language enum in definitions project)
 * 
 * @author wjboogerd
 */
public enum PortalLanguage {

	EN(true, "English","english_t"), 

	EU(true, "Basque", "basque_t"),

	BG(true, "&#x0411;&#x044a;&#x043b;&#x0433;&#x0430;&#x0440;&#x0441;&#x043a;&#x0438;", "bulgarian_t"),

	CA(true, "Catal&#224;", null),

	CS(true, "&#268;e&#353;tina", "czechlanguage_t"),

	DA(true, "Dansk", "danish_t"),

	DE(true, "Deutsch", "german_t"), 

	EL(true, "&#917;&#955;&#955;&#951;&#957;&#953;&#954;&#940;", "greek_t"),

	ES(true, "Espa&#241;ol", "spanish_t"), 

	ET(true, "Eesti", "estonian_t"),

	FR(true, "Fran&#231;ais", "french_t"), 

	GA(true, "Gaeilge", "irish_t"),

	HR(true, "Hrvatski", "croatian_t"),

	IS(true, "&#205;slenska", "icelandic_t"),

	IT(true, "Italiano","italian_t"),

	LT(true, "Lietuvi&#371;", "lithuanian_t"),

	LV(true, "Latvie&#353;u","latvian_t"),

	HU(true, "Magyar", "hungarian_t", false),

	MT(true, "Malti", "maltese_t", false),

	NL(true, "Nederlands", "dutch_t"),

	NO(true, "Norsk", "norwegian_t"),

	PL(true, "Polski", "polish_t"),

	PT(true, "Portugu&#234;s", "portuguese_t"),

	RO(true, "Rom&#226;n&#259;", "romanian_t", false),

	RU(true, "&#1056;&#1091;&#1089;&#1089;&#1082;&#1080;&#1081;", "russian_t"),

	SL(true, "Sloven&#353;&#269;ina", "slovenian_t"),

	SK(true, "Slovensk&#253;", "slovak_t"),

	FI(true, "Suomi", "finnish_t"), 

	SV(true, "Svenska", "swedish_t"),

	UK(true, "&#1059;&#1082;&#1088;&#1072;&#1111;&#1085;&#1089;&#1100;&#1082;&#1072;", "ukrainian_t", false),

	// UNSUPPORTED LANGUAGES (BUT OCCUR IN FACETS / FullDoc metadata):

	MUL(false, "Multilingue (mul)", "mul_t", false),

	AR(false, "Arabic", "arabic_t", false),
	FA(false, "Persian", "persian_t", false),
	HE(false, "Hebrew", "hebrew_t", false),
	CY(false, "Welsh", "welsh_t", false),
	LA(false, "Latin", "latin_t", false),

	AUS(false, "Australian (aus)", null, false),
	BE(false, "Belarusian (bel)", null, false),
	CHK(false, "Chuukese (chk)", null, false),
	EE(false, "Ewe (ewe)", null, false),
	ML(false, "Malayalam (mal)", null, false),
	SE(false, "Northern Sami (sme)", null, false),
	SI(false, "Sinhalese (sin)", null, false),
	SR(false, "Serbian (srp)", "serbian_t", false),
	SWE(false, "Swedish (swe)","swedish_t", false),
	TR(false, "Turkish (tr)", "turkish_t", false),
	YI(false, "Yiddish (yi)", "yiddish_t", false),
	JA(false, "Japanese (ja)", "japanese_t", false),
	ZH(false, "Chinese (zh)", "chinese_t", false),
	HI(false, "Hindi (hi)", "hindi_t", false),
	KO(false, "Korean (ko)", "korean_t", false),
	CU(false, "Old Church Slavonic (cu)", "old_church_slavonic_t", false),
	SA(false, "Sanskrit (sa)", "sanskrit_t", false),
	BO(false, "Tibetan (bo)", "tibetan_t", false),
	KM(false, "Khmer (km)", "khmer_t", false),
	KA(false, "Georgian (ka)", "georgian_t", false),
	HY(false, "Armenian (hy)", "armenian_t", false),
	SQ(false, "Albanian (sq)", "albanian_t", false),
	AM(false, "Amharic (am)", "amharic_t", false),
	GEZ(false, "Ge'ez (gez)", "geez_t", false),
	BR(false, "Breton (br)", "breton_t", false),
	OTA(false, "Ottoman Turkish (ota)", "ottoman_turkish_t", false),
	AKK(false, "Akkadian (akk)", "akkadian_t", false),
	ARC(false, "Aramaic (arc)", "aramaic_t", false),
	SYC(false, "Syriac (syc)", "syriac_t", false),
	EGY(false, "Egyptian (egy)", "egyptian_t", false),
	COP(false, "Coptic (cop)", "coptic_t", false),
	OC(false, "Occitan (oc)", "occitan_t", false),
	TH(false, "Thai (th)", "thai_t", false),
	UR(false, "Urdu (ur)", "urdu_t", false),
	VI(false, "Vietnamese (vi)", "vietnamese_t", false);

	/*
		luxemburgish_t=Luxemburgish (ltz)
		TODO: la, lat = latin, de-DE, en-GB, EN-GB, nn,
	 */

	private String languageName;
	private boolean portalSupport;
	private boolean imageSupport = true;
	private String label;

	private PortalLanguage(boolean portalSupport, String name, String label) {
		this.portalSupport = portalSupport;
		this.languageName = name;
		this.label = label;
	}

	private PortalLanguage(boolean portalSupport, String name, String label, boolean imageSupport) {
		this(portalSupport, name, label);
		this.imageSupport = imageSupport;
	}

	public String getLanguageCode() {
		return this.toString().toLowerCase();
	}

	public String getLanguageName() {
		return languageName;
	}

	public String getLabel() {
		return label;
	}

	public boolean hasImageSupport() {
		return imageSupport;
	}

	public boolean hasPortalSupport() {
		return portalSupport;
	}

	public static PortalLanguage safeValueOf(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		try {
			return PortalLanguage.valueOf(value.toUpperCase());
		} catch (Exception e) {
			return null;
		}
	}

	public static PortalLanguage safeValueOf(Locale value) {
		if (value == null) {
			return null;
		}
		return safeValueOf(value.getLanguage());
	}

	private static List<PortalLanguage> supported = null;

	public static List<PortalLanguage> getSupported() {
		if (supported == null) {
			supported = new ArrayList<PortalLanguage>();
			for (PortalLanguage lang : PortalLanguage.values()) {
				if (lang.portalSupport) {
					supported.add(lang);
				}
			}
		}
		return supported;
	}

	// cache this list as it is more or less static
	private static List<String> languageCodes = null;

	public static List<String> getLanguageCodes() {
		if (languageCodes == null) {
			languageCodes = new ArrayList<String>();
			for (PortalLanguage lang : PortalLanguage.getSupported()) {
				languageCodes.add(lang.getLanguageCode());
			}
		}
		return languageCodes;
	}
}
