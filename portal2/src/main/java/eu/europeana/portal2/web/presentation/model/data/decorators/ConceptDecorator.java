package eu.europeana.portal2.web.presentation.model.data.decorators;

import java.util.List;
import java.util.Map;

import eu.europeana.corelib.definitions.solr.entity.Concept;

public class ConceptDecorator extends ContextualItemDecorator implements Concept {

	private Concept concept;

	ConceptDecorator(Concept concept, String userLanguage, String edmLanguage) {
		super(concept, userLanguage, edmLanguage);
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
}
