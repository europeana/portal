package eu.europeana.portal2.web.presentation.model.data.decorators.fullbean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultilangFieldValue {

	private Map<String, List<String>> langValueMap;

	public MultilangFieldValue(Map<String, List<String>> langValueMap) {
		this.langValueMap = langValueMap;
	}

	public boolean has(String lang) {
		return langValueMap.containsKey(lang);
	}

	public List<String> get(String lang) {
		return langValueMap.get(lang);
	}

	public Integer getSize(String lang) {
		return langValueMap.get(lang).size();
	}

	public List<String> getLangs() {
		List<String> langs = new ArrayList<String>();
		langs.addAll(langValueMap.keySet());
		return langs;
	}
}
