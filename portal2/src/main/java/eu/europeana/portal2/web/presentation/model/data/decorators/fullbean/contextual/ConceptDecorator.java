package eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.contextual;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import eu.europeana.corelib.definitions.solr.entity.Concept;
import eu.europeana.portal2.web.presentation.model.data.decorators.fullbean.FullBeanLinked;

public class ConceptDecorator extends ContextualItemDecorator implements Concept {

	private Concept concept;
	private Map<String, String> broaderLinks;
	private Map<String, String> narrowerLinks;
	private Map<String, String> relatedLinks;
	private Map<String, String> broadMatchLinks;
	private Map<String, String> narrowMatchLinks;
	private Map<String, String> relatedMatchLinks;
	private Map<String, String> closeMatchLinks;
	private Map<String, String> exactMatchLinks;
	List<ContextualItemDecorator> relatedItems = new ArrayList<ContextualItemDecorator>();

	public ConceptDecorator(FullBeanLinked fullBeanLinked, Concept concept,
			String userLanguage, String edmLanguage) {
		super(fullBeanLinked, concept, userLanguage, edmLanguage);
		this.concept = concept;
	}

	@Override
	public String[] getBroader() {
		return concept.getBroader();
	}

	@Override
	public void setBroader(String[] broader) {
		//
	}

	@Override
	public String[] getNarrower() {
		return concept.getNarrower();
	}

	@Override
	public void setNarrower(String[] narrower) {
		//
	}

	@Override
	public String[] getRelated() {
		return concept.getRelated();
	}

	@Override
	public void setRelated(String[] related) {
		//
	}

	@Override
	public String[] getBroadMatch() {
		return concept.getBroadMatch();
	}

	@Override
	public void setBroadMatch(String[] broadMatch) {
		//
	}

	@Override
	public String[] getNarrowMatch() {
		return concept.getNarrowMatch();
	}

	@Override
	public void setNarrowMatch(String[] narrowMatch) {
		//
	}

	@Override
	public String[] getRelatedMatch() {
		return concept.getRelatedMatch();
	}

	@Override
	public void setRelatedMatch(String[] relatedMatch) {
		//
	}

	@Override
	public String[] getExactMatch() {
		return concept.getExactMatch();
	}

	@Override
	public void setExactMatch(String[] exactMatch) {
		//
	}

	@Override
	public String[] getCloseMatch() {
		return concept.getCloseMatch();
	}

	@Override
	public void setCloseMatch(String[] closeMatch) {
		//
	}

	@Override
	public Map<String, List<String>> getNotation() {
		return concept.getNotation();
	}

	public List<String> getNotationLang() {
		return getLanguageVersion(getNotation());
	}

	@Override
	public void setNotation(Map<String, List<String>> notation) {
		//
	}

	@Override
	public String[] getInScheme() {
		return concept.getInScheme();
	}

	@Override
	public void setInScheme(String[] inScheme) {
		//
	}

	public void makeLinks(Map<String, String> ids) {
		broaderLinks = new LinkedHashMap<String, String>();
		if (getBroader() != null) {
			for (String term : getBroader()) {
				addRelation(term);
				broaderLinks.put(term, (ids.containsKey(term) ? ids.get(term) : ""));
			}
		}

		narrowerLinks = new LinkedHashMap<String, String>();
		if (getNarrower() != null) {
			for (String term : getNarrower()) {
				addRelation(term);
				narrowerLinks.put(term, (ids.containsKey(term) ? ids.get(term) : ""));
			}
		}

		relatedLinks = new LinkedHashMap<String, String>();
		if (getRelated() != null) {
			for (String term : getRelated()) {
				addRelation(term);
				relatedLinks.put(term, (ids.containsKey(term) ? ids.get(term) : ""));
			}
		}

		broadMatchLinks = new LinkedHashMap<String, String>();
		if (getBroadMatch() != null) {
			for (String term : getBroadMatch()) {
				addRelation(term);
				ConceptDecorator other = (ConceptDecorator)fullBeanLinked.getConceptByURI(term);
				broadMatchLinks.put(term, (other != null && other.getLabel() != null ? other.getLabel() : ""));
			}
		}

		narrowMatchLinks = new LinkedHashMap<String, String>();
		if (getNarrowMatch() != null) {
			for (String term : getNarrowMatch()) {
				addRelation(term);
				narrowMatchLinks.put(term, (ids.containsKey(term) ? ids.get(term) : ""));
			}
		}

		relatedMatchLinks = new LinkedHashMap<String, String>();
		if (getRelatedMatch() != null) {
			for (String term : getRelatedMatch()) {
				addRelation(term);
				relatedMatchLinks.put(term, (ids.containsKey(term) ? ids.get(term) : ""));
			}
		}

		closeMatchLinks = new LinkedHashMap<String, String>();
		if (getCloseMatch() != null) {
			for (String term : getCloseMatch()) {
				addRelation(term);
				closeMatchLinks.put(term, (ids.containsKey(term) ? ids.get(term) : ""));
			}
		}

		exactMatchLinks = new LinkedHashMap<String, String>();
		if (getExactMatch() != null) {
			for (String term : getExactMatch()) {
				addRelation(term);
				exactMatchLinks.put(term, (ids.containsKey(term) ? ids.get(term) : ""));
			}
		}
	}

	public Map<String, String> getBroaderLinks() {
		return broaderLinks;
	}

	public Map<String, String> getNarrowerLinks() {
		return narrowerLinks;
	}

	public Map<String, String> getRelatedLinks() {
		return relatedLinks;
	}

	public Map<String, String> getBroadMatchLinks() {
		return broadMatchLinks;
	}

	public Map<String, String> getNarrowMatchLinks() {
		return narrowMatchLinks;
	}

	public Map<String, String> getRelatedMatchLinks() {
		return relatedMatchLinks;
	}

	public Map<String, String> getCloseMatchLinks() {
		return closeMatchLinks;
	}

	public Map<String, String> getExactMatchLinks() {
		return exactMatchLinks;
	}

	@Override
	public List<ContextualItemDecorator> getRelatedContextualItem() {
		return relatedItems;
	}

	private void addRelation(String uri) {
		ConceptDecorator relation = (ConceptDecorator)fullBeanLinked.getConceptByURI(uri);
		if (relation != null && !relatedItems.contains(relation)) {
			relatedItems.add(relation);
		}
	}
}
