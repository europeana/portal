/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved 
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 *  
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under 
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of 
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under 
 *  the Licence.
 */

package eu.europeana.portal2.web.presentation.model.data;

import java.util.List;

import javax.mail.search.SearchTerm;

import eu.europeana.portal2.web.presentation.model.abstracts.SearchPageData;
import eu.europeana.portal2.web.presentation.model.data.submodel.CarouselItem;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeaturedItem;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeedEntry;

public abstract class IndexData extends SearchPageData {

	private List<SearchTerm> randomTerms;

	protected List<FeedEntry> feedEntries;

	private List<CarouselItem> carouselItems;

	protected FeaturedItem featuredItem;

	private FeedEntry pinterestItem;

	private String pinterestUrl;

	public void setRandomTerms(List<SearchTerm> randomTerms) {
		this.randomTerms = randomTerms;
	}

	public List<SearchTerm> getRandomTerms() {
		return randomTerms;
	}

	public void setFeedEntries(List<FeedEntry> feedEntries) {
		this.feedEntries = feedEntries;
	}

	public List<FeedEntry> getFeedEntries() {
		return feedEntries;
	}

	public void setCarouselItems(List<CarouselItem> carouselItems) {
		this.carouselItems = carouselItems;
	}

	public List<CarouselItem> getCarouselItems() {
		return carouselItems;
	}

	public void setFeaturedItem(FeaturedItem featuredItem) {
		this.featuredItem = featuredItem;
	}

	public FeaturedItem getFeaturedItem() {
		return featuredItem;
	}

	public void setPinterestItem(FeedEntry pinterestItem) {
		this.pinterestItem = pinterestItem;
	}

	public FeedEntry getPinterestItem() {
		return pinterestItem;
	}

	public void setPinterestUrl(String pinterestUrl) {
		this.pinterestUrl = pinterestUrl;
	}

	public String getPinterestUrl() {
		return pinterestUrl;
	}
}
