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

package eu.europeana.portal2.web.presentation.enums;

public enum ExternalService {

    IMDB("IMDB", "http://www.imdb.com/find?s=all&#38;q="),
    GOOGLE("Google", "http://www.google.com/search?q="),
    WIKIPEDIA("Wikipedia", "http://en.wikipedia.org/wiki/Special:Search?go=Go&#38;search="),
    BING("Bing", "http://www.bing.com/search?q="),
    GOOGLEBOOKS("Google Books", "http://www.google.com/search?btnG=Boeken+zoeken&tbs=bks%3A1&tbo=1&q="),
    WORLDCAT("Worldcat", "http://www.worldcat.org/search?qt=worldcat_org_all&q="),
    FLICKR("Flickr", "http://www.flickr.com/search/?q="),
    AMAZON("Amazon", "http://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Daps&x=0&y=0&field-keywords="),
    YOUTUBE("YouTube", "http://www.youtube.com/results?aq=f&search_query="),
    GOOGLEMAPS("Google Maps", "http://maps.google.com/maps?q="),
    INTERNETARCHIVE("Internet Archive", "http://www.archive.org/search.php?query="),
    SMITHSONIAN("Smithsonian", "http://collections.si.edu/search/results.jsp?q=");
    
    private String serviceName;
    private String searchString;
    
    private ExternalService(String serviceName, String searchString){
        this.serviceName = serviceName;
        this.searchString = searchString;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getSearchString() {
        return formatSiwSearchTerm(searchString);
    }
    
    private String formatSiwSearchTerm(String searchTerm) {
        return searchTerm
            .replace("%26nbsp", "")
            .replace("&nbsp", "");
    }
    
    public static ExternalService[] none() {
        return new ExternalService[0];
    }

}
