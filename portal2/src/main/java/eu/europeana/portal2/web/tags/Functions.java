package eu.europeana.portal2.web.tags;

import eu.europeana.portal2.web.presentation.model.data.submodel.FieldCleaner;

public class Functions {

	public static String cleanField(String text) {
		return FieldCleaner.clean(text);
	}

	public static String encode(String text) {
		return eu.europeana.corelib.utils.EuropeanaUriUtils.encode(text);
	}
}
