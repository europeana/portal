package eu.europeana.portal2.web.model.spellcheck;

import java.util.ArrayList;
import java.util.List;

import eu.europeana.portal2.web.model.facets.LabelFrequency;

public class SpellCheck {

	public boolean correctlySpelled;

	public List<LabelFrequency> suggestions = new ArrayList<LabelFrequency>();

	public boolean isCorrectlySpelled() {
		return correctlySpelled;
	}

	public List<LabelFrequency> getSuggestions() {
		return suggestions;
	}
}
