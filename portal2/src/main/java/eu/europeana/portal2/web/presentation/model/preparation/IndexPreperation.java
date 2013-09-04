package eu.europeana.portal2.web.presentation.model.preparation;

import java.util.ArrayList;
import java.util.List;

import eu.europeana.portal2.web.presentation.model.data.IndexData;
import eu.europeana.portal2.web.presentation.model.data.submodel.CarouselItem;

public class IndexPreperation extends IndexData {

	public void setCarouselItems(List<CarouselItem> carouselItems) {
		List<CarouselItem> carouselItemsUnique = new ArrayList<CarouselItem>();
		for(CarouselItem carouselItem : carouselItems){
			if(!carouselItemsUnique.contains(carouselItem)){
				carouselItemsUnique.add(carouselItem);
			}
		}
		this.carouselItems = carouselItemsUnique;
	}

}
