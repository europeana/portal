package eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.europeana.corelib.definitions.solr.entity.Timespan;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.FullBeanLinker;

public class TimespanDecorator extends ContextualItemDecorator implements Timespan {

	private Timespan timespan;
	Map<String, String> isPartOfLinks;

	public TimespanDecorator(FullBeanLinker fullBeanLinked, Timespan timespan,
			String userLanguage, String edmLanguage) {
		super(fullBeanLinked, timespan, userLanguage, edmLanguage);
		this.timespan = timespan;
	}

	@Override
	public Map<String, List<String>> getBegin() {
		return timespan.getBegin();
	}

	public List<String> getBeginLang() {
		return getLanguageVersion(timespan.getBegin());
	}

	@Override
	public Map<String, List<String>> getEnd() {
		return timespan.getEnd();
	}

	public List<String> getEndLang() {
		return getLanguageVersion(timespan.getEnd());
	}

	@Override
	public Map<String, List<String>> getIsPartOf() {
		return timespan.getIsPartOf();
	}

	@Override
	public Map<String, List<String>> getDctermsHasPart() {
		return timespan.getDctermsHasPart();
	}

	@Override
	public String[] getOwlSameAs() {
		return timespan.getOwlSameAs();
	}

	@Override
	public void setBegin(Map<String, List<String>> begin) {}

	@Override
	public void setEnd(Map<String, List<String>> end) {}

	@Override
	public void setIsPartOf(Map<String, List<String>> isPartOf) {}

	@Override
	public void setDctermsHasPart(Map<String, List<String>> hasPart) {}

	@Override
	public void setOwlSameAs(String[] owlSameAs) {}

	public void makeLinks(Map<String, String> ids) {
		isPartOfLinks = new LinkedHashMap<String, String>();
		if (getIsPartOf() != null) {
			for (String term : getLanguageVersion(getIsPartOf())) {
				isPartOfLinks.put(term, (ids.containsKey(term) ? ids.get(term) : ""));
			}
		}
	}

	public Map<String, String> getIsPartOfLinks() {
		return isPartOfLinks;
	}

	@Override
	public List<ContextualItemDecorator> getRelatedContextualItem() {
		List<ContextualItemDecorator> items = new ArrayList<ContextualItemDecorator>();
		return items;
	}
}
