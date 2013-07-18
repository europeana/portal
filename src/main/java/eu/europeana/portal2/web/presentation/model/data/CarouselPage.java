package eu.europeana.portal2.web.presentation.model.data;

import java.util.List;

import eu.europeana.portal2.web.presentation.model.abstracts.SearchPageData;
import eu.europeana.portal2.web.presentation.model.data.submodel.CarouselItem;

/**
 * The model for carousel controller
 * 
 * @author peter.kiraly@kb.nl
 */
public class CarouselPage extends SearchPageData {

	/**
	 * The collection of elements
	 */
	private List<CarouselItem> carouselItems;

	/**
	 * The total number of elements
	 */
	private int total;

	/**
	 * The number of elements to retrieve
	 */
	private int rows = 1;

	/**
	 * The first element to retrieve
	 */
	private int start = 0;

	public void setCarouselItems(List<CarouselItem> carouselItems) {
		this.carouselItems = carouselItems;
	}

	public List<CarouselItem> getCarouselItems() {
		return carouselItems;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public void setRows(int rows) {
		this.rows = rows;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public void setStart(int start) {
		this.start = start;
	}

}
