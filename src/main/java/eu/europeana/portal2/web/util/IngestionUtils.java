/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europeana.portal2.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.response.FacetField;

import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.definitions.solr.model.Query;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.model.ResultSet;
import eu.europeana.corelib.solr.service.SearchService;

/**
 * Helper methods for working with collections
 * 
 * @author Borys Omelayenko
 */

public class IngestionUtils {

	@Resource private SearchService searchService;

	public static List<FacetField.Count> getCollectionsFromSolr(SearchService searchService, String facetFieldName, 
			String queryString, String[] fq) 
			throws SolrTypeException {
		Query query = new Query(queryString)
							.setParameter("rows", "0")
							.setParameter("facet", "true")
							.setRefinements(fq)
							// .setParameter("addFacetField", facetFieldName)
							.setParameter("facet.mincount", "1")
							.setParameter("facet.limit", "750")
							.setAllowSpellcheck(false);
		final ResultSet<BriefBean> response = searchService.search(BriefBean.class, query);
		List<FacetField.Count> facetFieldCount = null;
		for (FacetField facetField : response.getFacetFields()) {
			if (facetField.getName().equalsIgnoreCase(facetFieldName)) {
				facetFieldCount = facetField.getValues();
			}
		}
		if (facetFieldCount == null || facetFieldCount.size() == 0) {
			facetFieldCount = new ArrayList<FacetField.Count>();
		}
		return facetFieldCount;
	}

	public static String getCachedImageUrl(String imageCacheUrl, String imageUrl)
			throws UnsupportedEncodingException {
		return imageCacheUrl + "size=BRIEF_DOC&uri=" + URLEncoder.encode(imageUrl, "UTF-8");
	}

	/*
	public static boolean isImageCached(String imageCacheUrl, String imageUrl)
			throws Exception {
		InputStream is = null;
		try {
			is = new URL(getCachedImageUrl(imageCacheUrl, imageUrl) + "&show=info").openStream();
			String dimensions = IOUtils.toString(is);
			return new ImageInfo().deserialize(dimensions);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
	*/

	// TODO: remove when switched from "xml"
	@Deprecated
	public static String makeRecordHash(String resolveUri) {
		if (resolveUri.contains("/")) {
			resolveUri = StringUtils.substringAfterLast(resolveUri, "/");
		}
		return StringUtils.substringBeforeLast(resolveUri, ".");
	}

}
