package eu.europeana.portal2.web.presentation.model.data.submodel.clickstream;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_EMPTY)
public class LanguageChangeDAO extends AffixDAO {

	private String action;
	private String oldLanguage;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getOldLanguage() {
		return oldLanguage;
	}

	public void setOldLanguage(String oldLanguage) {
		this.oldLanguage = oldLanguage;
	}
}
