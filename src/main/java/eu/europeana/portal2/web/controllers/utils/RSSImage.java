package eu.europeana.portal2.web.controllers.utils;

/**
 * Container of images extracted from blog posts.
 * Now it contains the following fields:
 * - src: the URL of the image
 * - title: the caption of the image (if any)
 * 
 * @author peter.kiraly@kb.nl
 */
public class RSSImage {

	private String src;
	private String title;

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
