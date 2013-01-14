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

package eu.europeana.portal2.web.presentation.model;

import java.util.List;

import eu.europeana.corelib.definitions.db.entity.relational.SavedItem;
import eu.europeana.corelib.definitions.db.entity.relational.SavedSearch;
import eu.europeana.corelib.definitions.db.entity.relational.SocialTag;
import eu.europeana.portal2.web.presentation.model.abstracts.SearchPageData;

public class MyEuropeanaPage extends SearchPageData {

	List<SavedItem> savedItems;
	List<SavedSearch> savedSearches;
	List<SocialTag> socialTags;

	public List<SavedItem> getSavedItems() {
		return savedItems;
	}

	public void setSavedItems(List<SavedItem> savedItems) {
		this.savedItems = savedItems;
	}

	public List<SavedSearch> getSavedSearches() {
		return savedSearches;
	}

	public void setSavedSearches(List<SavedSearch> savedSearches) {
		this.savedSearches = savedSearches;
	}

	public List<SocialTag> getSocialTags() {
		return socialTags;
	}

	public void setSocialTags(List<SocialTag> socialTags) {
		this.socialTags = socialTags;
	}
}
