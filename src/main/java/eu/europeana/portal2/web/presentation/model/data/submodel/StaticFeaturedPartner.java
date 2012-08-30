package eu.europeana.portal2.web.presentation.model.data.submodel;

/**
 * View more latest content from our partners...
 * - featured-partner-view_text_t (JSP: viewText)
 * - notranslate_featured-partner-view_link_t (JSP: viewLink)
 * - notranslate_featured-partner-view_target_t (JSP: viewTarget)
 * 
 * Browse the content of all our partners...
 * - featured-partner-browse_text_t (JSP: browseText)
 * - notranslate_featured-partner-browse_link_t (JSP: browseLink)
 * - notranslate_featured-partner-browse_target_t (JSP: browseTarget)
 * 
 * Visit partners website...
 * - featured-partner-visit_text_t (JSP: visitText)
 * - notranslate_featured-partner-visit_link_t (JSP: visitLink)
 * - notranslate_featured-partner-visit_target_t (JSP: visitTarget)
 *
 * @author peter.kiraly@kb.nl
 */
public class StaticFeaturedPartner {

	protected int i;

	public StaticFeaturedPartner(int i) {
		this.i = i;
	}

	// View more latest content from our partners…
	public String getViewText() {
		return "featured-partner-view_text_t";
	}

	public String getViewLink() {
		return "notranslate_featured-partner-view_link_t";
	}

	public String getViewTarget() {
		return "notranslate_featured-partner-view_target_t";
	}

	// Browse the content of all our partners…
	public String getBrowseText() {
		return "featured-partner-browse_text_t";
	}

	public String getBrowseLink() {
		return "notranslate_featured-partner-browse_link_t";
	}

	public String getBrowseTarget() {
		return "notranslate_featured-partner-browse_target_t";
	}

	// Visit partners website…
	public String getVisitText() {
		return "featured-partner-visit_text_t";
	}

	public String getVisitLink() {
		return "notranslate_featured-partner-visit_link_t";
	}

	public String getVisitTarget() {
		return "notranslate_featured-partner-visit_target_t";
	}
}
