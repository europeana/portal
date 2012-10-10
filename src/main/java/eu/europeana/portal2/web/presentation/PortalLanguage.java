package eu.europeana.portal2.web.presentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * Hold the languages used by portal. (Subset of the Language enum in definitions project)
 * 
 * @author wjboogerd
 *
 */
public enum PortalLanguage {
	
	EN(true, "English (eng)","english_t"), 

	EU(true, "Basque (eus)", "basque_t"),
	
	BG(true, "&#x0411;&#x044a;&#x043b;&#x0433;&#x0430;&#x0440;&#x0441;&#x043a;&#x0438; (bul)", "bulgarian_t"),
	
	CA(true, "Catal&#224; (ca)", null),

	CS(true, "&#268;e&#353;tina (cze/cse)", "czechlanguage_t"),
	
	DA(true, "Dansk (dan)", "danish_t"),
	
	DE(true, "Deutsch (deu)", "german_t"), 

	EL(true, "&#917;&#955;&#955;&#951;&#957;&#953;&#954;&#940; (ell/gre)", "greek_t"),	

	ES(true, "Espa&#241;ol (esp)", "spanish_t"), 

	ET(true, "Eesti (est)", "estonian_t"),
	
	FIN(true, "Suomi (fin)", "finnish_t"),  // added
	
	FR(true, "Fran&#231;ais (fre)", "french_t"), 

	GA(true, "Irish (gle)", "irish_t"),
	
	HU(true, "Magyar (hun)", "hungarian_t", false),

	IS(true, "&#205;slenska (ice)", "icelandic_t"),
	
	IT(true, "Italiano (ita)","italian_t"),

	LT(true, "Lietuvi&#371; (lit)", "lithuanian_t"),
	
	LV(true, "Latvie&#353;u (lav)","latvian_t"),
	
	MT(true, "Malti (mlt)", "maltese_t", false),
	
	NL(true, "Nederlands (dut)", "dutch_t"),
	
	NO(true, "Norsk (nor)", "norwegian_t"),
	
	PL(true, "Polski (pol)", "polish_t"),

	PT(true, "Portugu&#234;s (por)", "portuguese_t"),
	
	RO(true, "Rom&#226;n&#259; (rom)", "romanian_t", false),
	
	RU(true, "Russian (rus)", "russian_t"),
	
	SK(true, "Slovensk&#253; (slo)", "slovak_t"),
	
	SL(true, "Sloven&#353;&#269;ina (slv)", "slovenian_t"),

	SV(true, "Svenska (sve/swe)", "swedish_t"),
	
	UK(true, "Ukrainian (ukr)", "ukrainian_t", false);
	
	// UNSUPPORTED LANGUAGES (BUT OCCURS IN FACETS):
	/*
	MUL(false, "Multilingue (mul)",null, false),
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
	YI(false, "Yiddish (yi)", "yiddish_t", false);
	*/
/*
croatian_t=Croatian (hrv)
luxemburgish_t=Luxemburgish (ltz)

 */
	// TODO: la, lat = latin, de-DE, en-GB, EN-GB, nn, 

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
