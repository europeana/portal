package eu.europeana.portal2.web.tags;

import org.apache.commons.lang.StringUtils;

import eu.europeana.corelib.web.service.MicrosoftTranslatorService;
import eu.europeana.corelib.web.service.impl.MicrosoftTranslatorServiceImpl;
import eu.europeana.portal2.web.presentation.model.data.submodel.FieldCleaner;

public class Functions {

	private static MicrosoftTranslatorService translationUrlservice = MicrosoftTranslatorServiceImpl.getBeanInstance();

	public static String cleanField(String text) {
		return FieldCleaner.clean(text);
	}

	public static String encode(String text) {
		return eu.europeana.corelib.utils.EuropeanaUriUtils.encode(text);
	}

	public static String abbreviate(String str, int maxWidth) {
		return StringUtils.abbreviate(str, maxWidth);
	}

	public static String translate(String valueToTranslate, String langCode) {
		return translationUrlservice.translate(valueToTranslate, langCode);
	}
}
