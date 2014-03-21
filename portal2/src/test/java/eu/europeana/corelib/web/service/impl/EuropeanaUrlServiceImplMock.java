package eu.europeana.corelib.web.service.impl;

import java.io.UnsupportedEncodingException;

import eu.europeana.corelib.definitions.solr.DocType;
import eu.europeana.corelib.web.service.EuropeanaUrlService;
import eu.europeana.corelib.web.utils.UrlBuilder;

public class EuropeanaUrlServiceImplMock implements EuropeanaUrlService {

	@Override
	public UrlBuilder getApi1Home(String apikey) {
		return null;
	}

	@Override
	public UrlBuilder getApi2Home(String apikey) {
		return null;
	}

	@Override
	public UrlBuilder getApi1SearchJson(String apikey, String query, int start)
			throws UnsupportedEncodingException {
		return null;
	}

	@Override
	public UrlBuilder getApi2SearchJson(String apikey, String query, String rows)
			throws UnsupportedEncodingException {
		return null;
	}

	@Override
	public UrlBuilder getApi2RecordJson(String apikey, String collectionid,
			String objectid) {
		return null;
	}

	@Override
	public UrlBuilder getApi2RecordJson(String apikey, String europeanaId) {
		return null;
	}

	@Override
	public UrlBuilder getApi1Record(String apikey, String europeanaId,
			String extention) {
		return null;
	}

	@Override
	public UrlBuilder getApi2Record(String apikey, String europeanaId,
			String extention) {
		return null;
	}

	@Override
	public UrlBuilder getApi2Redirect(long uid, String showAt, String provider,
			String europeanaId, String profile) {
		return null;
	}

	@Override
	public UrlBuilder getPortalHome(boolean relative) {
		return null;
	}

	@Override
	public String getPortalResolve(String europeanaId) {
		return null;
	}

	@Override
	public String getPortalResolve(String collectionid, String objectid) {
		return null;
	}

	@Override
	public UrlBuilder getPortalSearch() throws UnsupportedEncodingException {
		return new UrlBuilder("/portal/search.html");
	}

	@Override
	public UrlBuilder getPortalSearch(boolean relative, String query,
			String rows) throws UnsupportedEncodingException {
		return null;
	}

	@Override
	public UrlBuilder getPortalSearch(boolean relative, String searchpage,
			String query, String rows) throws UnsupportedEncodingException {
		return null;
	}

	@Override
	public UrlBuilder getPortalRecord(boolean relative, String collectionid,
			String objectid) {
		return null;
	}

	@Override
	public UrlBuilder getPortalRecord(boolean relative, String europeanaId) {
		return null;
	}

	@Override
	public UrlBuilder getCanonicalPortalRecord(String europeanaId) {
		return null;
	}

	@Override
	public UrlBuilder getThumbnailUrl(String thumbnail, DocType type) {
		return null;
	}

	@Override
	public String extractEuropeanaId(String url) {
		return null;
	}

}
