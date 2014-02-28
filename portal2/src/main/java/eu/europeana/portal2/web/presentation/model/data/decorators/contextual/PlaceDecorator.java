package eu.europeana.portal2.web.presentation.model.data.decorators.contextual;

import java.util.List;
import java.util.Map;

import eu.europeana.corelib.definitions.solr.entity.Place;

public class PlaceDecorator extends ContextualItemDecorator implements Place {

	private Place place;

	public PlaceDecorator(Place place, String userLanguage, String edmLanguage) {
		super(place, userLanguage, edmLanguage);
		this.place = place;
	}

	@Override
	public Map<String, List<String>> getIsPartOf() {
		return place.getIsPartOf();
	}

	public List<String> getIsPartOfLang() {
		return getLanguageVersion(place.getIsPartOf());
	}

	@Override
	public Map<String, List<String>> getDcTermsHasPart() {
		return place.getDcTermsHasPart();
	}

	public List<String> getDcTermsHasPartLang() {
		return getLanguageVersion(place.getDcTermsHasPart());
	}

	@Override
	public Float getLatitude() {
		return place.getLatitude();
	}

	@Override
	public Float getLongitude() {
		return place.getLongitude();
	}

	@Override
	public Float getAltitude() {
		return place.getAltitude();
	}

	@Override
	public Map<String, Float> getPosition() {
		return place.getPosition();
	}

	@Override
	public String[] getOwlSameAs() {
		return place.getOwlSameAs();
	}

	///////////// Setters
	@Override
	public void setIsPartOf(Map<String, List<String>> isPartOf) {}

	@Override
	public void setLatitude(Float latitude) {}

	@Override
	public void setLongitude(Float longitude) {}

	@Override
	public void setAltitude(Float altitude) {}

	@Override
	public void setPosition(Map<String, Float> position) {}

	@Override
	public void setDcTermsHasPart(Map<String, List<String>> dcTermsHasPart) {}

	@Override
	public void setOwlSameAs(String[] owlSameAs) {}
}
