package eu.europeana.portal2.querymodel.query;

import org.apache.commons.lang.StringUtils;

/**
 * Class represents a set of rights that can be attached to an item in the Europeana website.
 * 
 * @author kevinparkings
 */
public enum RightsOption {

	//CC_ZERO("http://creativecommons.org/publicdomain/zero", "CC0", "cc_zero.png"), 
	CC_ZERO("http://creativecommons.org/publicdomain/zero", "CC0", "icon-licensezero"), 

	CC_BY("http://creativecommons.org/licenses/by/", "CC BY", "cc_by.png"),
	CC_BY_SA("http://creativecommons.org/licenses/by-sa/", "CC BY-SA", "cc_by_sa.png"),
	CC_BY_NC_SA("http://creativecommons.org/licenses/by-nc-sa/", "CC BY-NC-SA", "cc_by_nc_sa.png"),
	CC_BY_ND("http://creativecommons.org/licenses/by-nd/", "CC BY-ND", "cc_by_nd.png"),
	CC_BY_NC("http://creativecommons.org/licenses/by-nc/", "CC BY-NC", "cc_by_nc.png"),
	CC_BY_NC_ND("http://creativecommons.org/licenses/by-nc-nd/", "CC BY-NC-ND", "cc_by_nc_nd.png"),

	NOC("http://creativecommons.org/publicdomain/mark/", "Public Domain marked", "noc.png"),

	//EU_PD("http://www.europeana.eu/rights/pd/", "Public Domain", "eu_public_domain.jpg", "eu_public_domain-transparent.png"), // +transparent
	EU_PD("http://www.europeana.eu/rights/pd/", "Public Domain", "icon-licensepd", "icon-licensepd"), // +transparent 

	//EU_RR_F("http://www.europeana.eu/rights/rr-f/", "Free Access - Rights Reserved", "eu_free_access.jpg", "eu_free_access.png"), // +transparent
	EU_RR_F("http://www.europeana.eu/rights/rr-f/", "Free Access - Rights Reserved", "icon-free", "icon-free"),

	//EU_RR_P("http://www.europeana.eu/rights/rr-p/", "Paid Access - Rights Reserved", "eu_paid_access.jpg", "eu_paid_access.png"), // +transparent
	EU_RR_P("http://www.europeana.eu/rights/rr-p/", "Paid Access - Rights Reserved", "icon-paid", "icon-paid"),

	//EU_RR_R("http://www.europeana.eu/rights/rr-r/", "Restricted Access - Rights Reserved", "eu_restricted_access.jpg", "cc-restrictedaccess.png"), // +transparent
	EU_RR_R("http://www.europeana.eu/rights/rr-r/", "Restricted Access - Rights Reserved", "icon-restricted", "icon-restricted"),

	//EU_U("http://www.europeana.eu/rights/unknown/", "Unknown copyright status", "eu_unknown.jpg", "cc-unknown.png"); // +transparent
	EU_U("http://www.europeana.eu/rights/unknown/", "Unknown copyright status", "icon-unknown", "icon-unknown");

	private String url = null;
	private String rightsText = null;
	private String rightsIcon = null;
	private String rightsIconLightbox = null;

	/**
	 * Constructor for method
	 * 
	 * @param url
	 *            Url associated with the rights for the object
	 * @param rightsText
	 *            Text associated with the rights
	 * @param rightsIcon
	 *            Icon associated with the rights
	 */
	private RightsOption(String url, String rightsText, String rightsIcon) {
		this.url = url;
		this.rightsText = rightsText;
		this.rightsIcon = rightsIcon;
	}

	/**
	 * Constructor for method
	 * 
	 * @param url
	 *            Url associated with the rights for the object
	 * @param rightsText
	 *            Text associated with the rights
	 * @param rightsIcon
	 *            Icon associated with the rights
	 * @param rightsIconLightbox
	 *            Icon associated with the rights for lightbox display
	 */
	private RightsOption(String url, String rightsText, String rightsIcon,
			String rightsIconLightbox) {
		this(url, rightsText, rightsIcon);
		this.rightsIconLightbox = rightsIconLightbox;
	}

	/**
	 * Returns the full Url associated with the rights
	 * 
	 * @return Full url associated with the rights
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Return text associated with the rights
	 * 
	 * @return text associated with the results
	 */
	public String getRightsText() {
		return rightsText;
	}

	/**
	 * Returns the url of the icon associated with the results
	 * 
	 * @return url of icon associated with the results
	 */
	public String getRightsIcon() {
		return rightsIcon;
	}

	/**
	 * Returns the url of the icon associated with the results
	 * 
	 * @return url of icon associated with the results
	 */
	public String getRightsIconLightbox() {
		if (rightsIconLightbox == null) {
			return rightsIcon;
		}
		return rightsIconLightbox;
	}

	public static RightsOption safeValueByUrl(String url) {
		if (StringUtils.isNotBlank(url)) {
			for (RightsOption option : RightsOption.values()) {
				if (url.contains(option.getUrl())) {
					return option;
				}
			}
		}
		return null;
	}
}
