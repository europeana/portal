package eu.europeana.portal2.web.model.mlt;

import java.util.ArrayList;
import java.util.List;

public class EuropeanaMlt {

	private List<EuropeanaMltCategory> categories;

	public EuropeanaMlt() {
		this.categories = new ArrayList<EuropeanaMltCategory>();
	}

	public void addCategory(EuropeanaMltCategory category) {
		categories.add(category);
	}

	public List<EuropeanaMltCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<EuropeanaMltCategory> categories) {
		this.categories = categories;
	}
}
