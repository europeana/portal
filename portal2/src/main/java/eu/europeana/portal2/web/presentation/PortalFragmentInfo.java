package eu.europeana.portal2.web.presentation;

import eu.europeana.corelib.web.model.FragmentInfo;

public enum PortalFragmentInfo implements FragmentInfo {

	INDEX_BLOG("index/fragment/blog"),
	INDEX_FEATUREDCONTENT("index/fragment/featuredContent"),
	INDEX_PINTEREST("index/fragment/pinterest");

	private String template;

	private PortalFragmentInfo(String template) {
		this.template = template;
	}

	@Override
	public String getTemplate() {
		return template;
	}
}
