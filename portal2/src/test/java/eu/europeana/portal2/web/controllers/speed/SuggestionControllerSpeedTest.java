package eu.europeana.portal2.web.controllers.speed;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Collation;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Correction;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.Test;

import eu.europeana.corelib.definitions.solr.model.Term;

public class SuggestionControllerSpeedTest {

	private String baseUrl = "http://10.101.38.1:9595/solr/";

	private HttpSolrServer solrServer = null;

	private int cents = 3;
	private int iterations = 100 * cents;

	private List<String> words;

	@Test
	public void allSpeedTest() {
		words = TermProvider.getRandomWords(iterations);
		solrJSpeedTest();
		restSpeedTest();
	}

	// @Test
	public void restSpeedTest() {
		String baseUrl = "http://localhost:8080/portal/suggestions.json?term=%s";

		long t = new Date().getTime();
		long min = 0, max = 0, t1, t3;
		String minw = "", maxw = "";
		int i;
		DescriptiveStatistics stat = new DescriptiveStatistics();
		List<String> slowQueries = new ArrayList<String>();
		for (i = 0; i<iterations; i++) {
			if (i > words.size()-1) {
				break;
			}
			String url = String.format(baseUrl, words.get(i));
			// System.out.println(url);
			t1 = new Date().getTime();
			SpeedTestUtils.getWebContent(url);
			t3 = (new Date().getTime() - t1);
			stat.addValue(t3);
			if (t3 > 1000) {
				slowQueries.add(words.get(i) + " (" + t3 + ")");
			}
			if (min == 0) {min = max = t3;}
			if (t3 < min) {min = t3; minw = words.get(i);}
			if (t3 > max) {max = t3; maxw = words.get(i);}
		}
		long time = (new Date().getTime() - t);
		System.out.println("[suggestion] took " + (time/cents) + " (" + min + "-" + max + ") " + maxw
				+ ", slow queries: " + slowQueries);
		SpeedTestUtils.printStatistics(stat);
	}

	// @Test
	public void solrJSpeedTest() {

		long t = new Date().getTime();
		int i;
		long total = 0;
		long min = 0, max = 0, t1, t3, time, tmax = 0, tmin = 0;
		String maxw = "", tmaxw = "";
		DescriptiveStatistics stat = new DescriptiveStatistics();
		List<String> slowQueries = new ArrayList<String>();
		Map<String, String> fields = new HashMap<String, String>(){{
			put("who", "suggestWho");
			put("title", "suggestTitle");
			put("what", "suggestWhat");
			put("where", "suggestWhere");
			put("when", "suggestWhen");
		}};
		for (i = 0; i<iterations; i++) {
			if (i > words.size()-1) {
				break;
			}
			t1 = new Date().getTime();
			for (Map.Entry<String, String> field : fields.entrySet()) {
				long t2 = new Date().getTime();
				getSuggestions(words.get(i), field.getKey(), field.getValue(), true);
				time = (new Date().getTime() - t2);
				total += time;
				if (time > 1000) {
					slowQueries.add(field.getKey() + ":" + words.get(i) + " (" + time + ")");
				}
				if (tmax == 0) {tmax = tmin = time;}
				if (time > tmax) {tmax = time; tmaxw = field.getKey() + ":" + words.get(i);}
				if (time < tmin) {tmin = time;}
			}
			t3 = (new Date().getTime() - t1);
			stat.addValue(t3);
			if (min == 0) {min = max = t3;}
			if (t3 < min) {min = t3;}
			if (t3 > max) {max = t3; maxw = words.get(i) + " (" + t3 + ")";}
		}
		System.out.println("[solrJ] took " + ((new Date().getTime() - t)/cents) 
				+ " -- internal part: " + (total/cents) 
				+ " (" + min + "-" + max + ") " + maxw
				+ " (" + tmin + "-" + tmax + ") " + tmaxw
				+ ", slow queries: " + slowQueries
		);
		SpeedTestUtils.printStatistics(stat);
	}

	private long getSuggestions(String query, String field, String rHandler, boolean collate) {
		List<Term> results = new ArrayList<Term>();
		boolean doPrint = false;
		if (solrServer == null) {
			solrServer = new HttpSolrServer(baseUrl + "search");
			solrServer.setFollowRedirects(false);
			solrServer.setConnectionTimeout(60000);
			solrServer.setDefaultMaxConnectionsPerHost(64);
			solrServer.setMaxTotalConnections(125);
			doPrint = true;
		}

		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set("qt", "/" + rHandler);
		params.set("q", field + ":" + query);
		params.set("rows", 0);
		if (collate) {
			params.set("spellcheck.collate", "true");
		} else {
			params.set("spellcheck.collate", "false");
		}

		try {
			if (solrServer != null) {
				if (doPrint) {
					System.out.println(params.toString());
				}
				QueryResponse qResp = solrServer.query(params);
				// return (Integer)qResp.getHeader().get("QTime");
				SpellCheckResponse spResponse = qResp.getSpellCheckResponse();
				//if the suggestions are not empty and there are collated results
				if (!spResponse.getSuggestions().isEmpty()) {
					if (collate) {
						if (spResponse.getCollatedResults() != null) {
							// log.fine("Number of collated results received " + spResponse.getCollatedResults().size());
							for (Collation collation : spResponse.getCollatedResults()) {
								StringBuilder termResult = new StringBuilder();
								// NOTE: should we concatenate and change corrections?
								for (Correction cor : collation.getMisspellingsAndCorrections()) {
									//							String corStr = cor.getCorrection().replaceAll(
									//									"[-+.^:(),]", "");
									//pickup the corrections, remove duplicates
									String[] terms = cor.getCorrection().trim().replaceAll("  ", " ").split(" ");
									for (String term : terms) {
										if (StringUtils.isBlank(term)) {
											continue;
										}
										// termResult.
										if (!StringUtils.contains(termResult.toString(), term)) {
											termResult.append(term + " ");
										}
									}
								}
								//return the term, the number of hits for each collation and the field that it should be mapped to
								Term term = new Term(termResult.toString().trim(), collation.getNumberOfHits(), field, null);
								results.add(term);
							}
						}
					} else {
						for (Suggestion suggestion : spResponse.getSuggestions()) {
							for (String term : suggestion.getSuggestions()) {
								results.add(new Term(term, 1, field, null));
							}
						}
					}
				}
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
