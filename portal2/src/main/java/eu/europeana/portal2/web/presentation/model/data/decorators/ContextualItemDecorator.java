package eu.europeana.portal2.web.presentation.model.data.decorators;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import eu.europeana.corelib.definitions.solr.entity.ContextualClass;

public class ContextualItemDecorator implements ContextualClass {

	private ContextualClass item;
	protected String userLanguage;
	protected String edmLanguage;

	ContextualItemDecorator(ContextualClass item, String userLanguage, String edmLanguage) {
		this.item = item;
		this.userLanguage = userLanguage;
		this.edmLanguage = edmLanguage;
	}

	@Override
	public ObjectId getId() {
		return item.getId();
	}

	@Override
	public String getAbout() {
		return item.getAbout();
	}

	@Override
	public Map<String, List<String>> getPrefLabel() {
		return item.getPrefLabel();
	}

	@Override
	public Map<String, List<String>> getAltLabel() {
		return item.getAltLabel();
	}

	@Override
	public Map<String, List<String>> getHiddenLabel() {
		return item.getHiddenLabel();
	}

	@Override
	public Map<String, List<String>> getNote() {
		return item.getNote();
	}

	@Override
	public void setId(ObjectId id) {}

	@Override
	public void setAbout(String about) {}

	@Override
	public void setPrefLabel(Map<String, List<String>> prefLabel) {}

	@Override
	public void setAltLabel(Map<String, List<String>> altLabel) {}

	@Override
	public void setHiddenLabel(Map<String, List<String>> hiddenLabel) {}

	@Override
	public void setNote(Map<String, List<String>> note) {}

	public List<String> getLabels() {
		List<String> labels = getLanguageVersion(item.getPrefLabel());
		if (labels == null) {
			labels = getLanguageVersion(item.getAltLabel());
		}
		return labels;
	}

	public List<String> getNotes() {
		return getLanguageVersion(item.getNote());
	}

	public List<String> getLanguageVersion(Map<String, List<String>> map) {
		List<String> list = null;
		if (map == null) {
			return list;
		}
		if (map.containsKey(userLanguage)) {
			list = map.get(userLanguage);
		} else if (map.containsKey("def")) {
			list = map.get("def");
		} else if (map.containsKey(edmLanguage)) {
			list = map.get(edmLanguage);
		}
		return list;
	}
}
