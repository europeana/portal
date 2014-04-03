package eu.europeana.portal2.web.presentation.model.data.decorators.fullbean;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.definitions.solr.beans.FullBean;
import eu.europeana.corelib.utils.StringArrayUtils;

/**
 * Wrapper functions aound shortcut calls.
 * Shortcut calls makes easily to access field content under sub-object of the full bean.
 * 
 * @author Peter.Kiraly@europeana.eu
 */
public class ShortcutWrapper extends FullBeanLinked {

	protected FullBeanShortcut shortcut;

	public ShortcutWrapper(FullBean fullBean) {
		super(fullBean);
		shortcut = new FullBeanShortcut(this);
	}

	public ShortcutWrapper(FullBean fullBean, String userLanguage) {
		super(fullBean, userLanguage);
		shortcut = new FullBeanShortcut(this);
	}

	/**
	 * Returns the posts author
	 * 
	 * @return posts author
	 */
	public String getPostAuthor() {
		if (StringUtils.isBlank(shortcut.get("DcCreator")[0])) {
			return "none";
		} else {
			return shortcut.get("DcCreator")[0];
		}
	}

	public String getUrlKml() {
		if (isPositionAvailable()) {
			// return "record" + getAbout() + ".kml";
			return "";
		}
		return null;
	}

	public boolean isPositionAvailable() {
		return (shortcut.getEdmPlaceLatitude() != null && shortcut.getEdmPlaceLatitude().length > 0
				&& shortcut.getEdmPlaceLatitude()[0] != null && shortcut.getEdmPlaceLatitude()[0] != 0)
				|| (shortcut.getEdmPlaceLongitude() != null && shortcut.getEdmPlaceLongitude().length > 0
						&& shortcut.getEdmPlaceLongitude()[0] != null && shortcut.getEdmPlaceLongitude()[0] != 0);
	}

	public Float[] getEdmPlaceLatitude() {
		return shortcut.getEdmPlaceLatitude();
	}

	public Float[] getEdmPlaceLongitude() {
		return shortcut.getEdmPlaceLongitude();
	}

	public String[] getDataProvider() {
		return shortcut.get("DataProvider");
	}

	public String[] getEdmCountry() {
		return shortcut.get("EdmCountry");
	}

	public String[] getEdmLanguage() {
		return shortcut.get("EdmLanguage");
	}

	public String getCheckedEdmLandingPage() {
		if (ArrayUtils.isEmpty(shortcut.get("EdmLandingPage"))) {
			return null;
		}
		String landingPage = shortcut.get("EdmLandingPage")[0];
		if (!landingPage.endsWith(".html")) {
			landingPage += ".html";
		}
		return landingPage;
	}

	public String[] getEdmDataProvider() {
		return shortcut.get("EdmDataProvider");
	}

	public boolean isUserGeneratedContent() {
		if (StringArrayUtils.isNotBlank(shortcut.get("EdmUGC"))) {
			return StringUtils.equalsIgnoreCase(shortcut.get("EdmUGC")[0], "true");
		}
		return false;
	}

	public String[] getDctermsHasVersion() {
		return shortcut.get("DctermsHasVersion");
	}

	public String[] getDctermsIsFormatOf() {
		return shortcut.get("DctermsIsFormatOf");
	}

	public String[] getDcDate() {
		return shortcut.get("DcDate");
	}

	public String[] getDcDescription() {
		if (shortcut.get("DcDescription") == null) {
			return null;
		}

		List<String> descriptions = Arrays.asList(shortcut.get("DcDescription"));
		for (int i = 0, l = descriptions.size(); i < l; i++) {
			descriptions.set(i, descriptions.get(i).replace("\n", "<br/>\n"));
		}
		return StringArrayUtils.toArray(descriptions);
	}

	public String getDcDescriptionCombined() {
		return StringEscapeUtils.escapeXml(StringUtils.join(getDcDescription(), ";"));
	}

	public String[] getDcFormat() {
		return shortcut.get("DcFormat");
	}

	public String[] getDcLanguage() {
		return shortcut.get("DcLanguage");
	}

	public String[] getDcRights() {
		return shortcut.get("DcRights");
	}

	public String[] getDcSubject() {
		return shortcut.get("DcSubject");
	}

	public String[] getDcTitle() {
		return shortcut.get("DcTitle");
	}

	public String getDcTitleCombined() {
		return StringEscapeUtils.escapeXml(StringUtils.join(getDcTitle(), ";"));
	}

	public String[] getDcType() {
		return shortcut.get("DcType");
	}

	public FullBeanShortcut getShortcut() {
		return shortcut;
	}

	public List<String> getEdmRights() {
		return shortcut.getList("EdmRights");
	}
}
