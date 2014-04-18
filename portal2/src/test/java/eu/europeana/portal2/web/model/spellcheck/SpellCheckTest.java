package eu.europeana.portal2.web.model.spellcheck;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import eu.europeana.portal2.web.model.facets.LabelFrequency;

public class SpellCheckTest {

	@Test
	public void testCorrectlySpelled() {
		SpellCheck spellCheck = new SpellCheck();
		assertFalse(spellCheck.isCorrectlySpelled());

		spellCheck.correctlySpelled = false;
		assertFalse(spellCheck.isCorrectlySpelled());

		spellCheck.correctlySpelled = true;
		assertTrue(spellCheck.isCorrectlySpelled());
	}

	@Test
	public void testSuggestionsIndirect() {
		SpellCheck spellCheck = new SpellCheck();

		assertNotNull(spellCheck.getSuggestions());
		assertEquals(0, spellCheck.getSuggestions().size());

		List<LabelFrequency> suggestions = new ArrayList<LabelFrequency>();
		suggestions.add(new LabelFrequency("Den Haag", 10));
		suggestions.add(new LabelFrequency("Amsterdam", 5));
		spellCheck.suggestions = suggestions;

		suggestionAsserts(spellCheck);
	}

	@Test
	public void testSuggestionsDirect() {
		SpellCheck spellCheck = new SpellCheck();

		assertNotNull(spellCheck.getSuggestions());
		assertEquals(0, spellCheck.getSuggestions().size());

		spellCheck.suggestions.add(new LabelFrequency("Den Haag", 10));
		spellCheck.suggestions.add(new LabelFrequency("Amsterdam", 5));

		suggestionAsserts(spellCheck);
	}

	private void suggestionAsserts(SpellCheck spellCheck) {
		assertNotNull(spellCheck.getSuggestions());
		assertEquals(2, spellCheck.getSuggestions().size());

		assertEquals("Den Haag", spellCheck.getSuggestions().get(0).getLabel());
		assertEquals(10, spellCheck.getSuggestions().get(0).getCount());

		assertEquals("Amsterdam", spellCheck.getSuggestions().get(1).getLabel());
		assertEquals(5, spellCheck.getSuggestions().get(1).getCount());
	}
}
