package eu.europeana.portal2.web.presentation;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.web.model.FragmentInfo;

public enum PortalFragmentInfo implements FragmentInfo {

	INDEX_BLOG("blog", "index/fragment/blog"),
	INDEX_FEATUREDCONTENT("featuredContent", "index/fragment/featuredContent"),
	INDEX_PINTEREST("pinterest", "index/fragment/pinterest");
	
	private String fragmentName;
	private String template;

	private PortalFragmentInfo(String fragmentName, String template) {
		this.fragmentName = fragmentName;
		this.template = template;
	}
	
	@Override
	public String getTemplate() {
		return template;
	}
	
	public static PortalFragmentInfo getByFragmentName(String name) {
		for (PortalFragmentInfo info : PortalFragmentInfo.values()) {
			if (StringUtils.equalsIgnoreCase(name, info.fragmentName)) {
				return info;
			}
		}
		return null;
	}
	
}
