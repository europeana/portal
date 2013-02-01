package eu.europeana.portal2.web.controllers.speed;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.corelib.definitions.solr.QueryType;
import eu.europeana.corelib.definitions.solr.beans.BriefBean;
import eu.europeana.corelib.solr.bean.impl.BriefBeanImpl;
import eu.europeana.corelib.solr.service.SearchService;
import eu.europeana.corelib.solr.service.query.MoreLikeThis;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/servlet/portal2-mvc.xml", "/internal/portal2-development.xml"})
public class MoreLikeThisSpeedTest {

	@Resource(name = "corelib_solr_solrServer") private SolrServer solrServer;

	@Resource private SearchService searchService;

	private int totalRecordsToTest = 20;
	private int maxPerTerm = 4;
	private int rows = 20;
	private String solrUrl = "http://10.101.38.1:9595/solr/search/select?fl=europeana_id&rows=" + rows;
	private Pattern pattern = Pattern.compile("<str name=\"europeana_id\">(.*?)</str>");
	private String mltFields;

	@Test
	public void test() {
		List<String[]> recordIds = getIds();
		List<BriefBean> result;
		// warming
		for (String[] recordId : recordIds) {
			try {
				findMoreLikeThis(recordId[0], 10, 1);
			} catch (SolrServerException e) {
				e.printStackTrace();
			}
		}
		for (int i=1; i<3; i++) {
			mltFields = null;
			long t = new Date().getTime();
			DescriptiveStatistics stat1 = new DescriptiveStatistics();
			DescriptiveStatistics stat2 = new DescriptiveStatistics();
			System.out.println("mlt.mindf=" + 5*i);
			for (String[] recordId : recordIds) {
				try {
					long t2 = new Date().getTime();
					result = findMoreLikeThis(recordId[0], 10, i);
					long diff = new Date().getTime() - t2;
					if (result.isEmpty()) {
						stat2.addValue(diff);
					} else {
						stat1.addValue(diff);
					}
				} catch (SolrServerException e) {
					e.printStackTrace();
				}
			}
			printStatistics(stat1);
			if (stat2.getN() > 0) {
				printStatistics(stat2);
			}

			double[] values = stat1.getSortedValues();
			DescriptiveStatistics stat3 = new DescriptiveStatistics();
			for (int j=1; j<values.length-1; j++) {
				stat3.addValue(values[j]);
			}
			printStatistics(stat3);
		}
	}

	private void printStatistics(DescriptiveStatistics stat) {
		System.out.print(String.format("%5d ms (%2d occurences)", (int)stat.getSum(), stat.getN()));
		System.out.print(String.format(", mean: %4d (%4d-%5d)", (int)stat.getMean(), (int)stat.getMin(), (int)stat.getMax()));
		System.out.print(String.format(", median: %4d", (int)stat.getPercentile(50)));
		System.out.print(String.format(", deviation: %4d (%4d%%)", (int)stat.getStandardDeviation(),
				(int)(stat.getStandardDeviation()*100/stat.getMean())));

		System.out.print(", Percentiles: ");
		for (int i=1; i <= 10; i++) {
			System.out.print(String.format("%3d", (int)(stat.getPercentile(i * 10) * 100 / stat.getMean())));
			if (i < 10) {
				System.out.print("-");
			} else {
				System.out.println();
			}
		}
	}

	private List<BriefBean> findMoreLikeThis(String europeanaObjectId, int count, int mintf)
			throws SolrServerException {

		SolrQuery solrQuery = new SolrQuery()
			.setQuery("europeana_id:\"" + europeanaObjectId + "\"");
		if (mintf == 2) {
			solrQuery.setQueryType(QueryType.ADVANCED.toString());
		}
		solrQuery.set("mlt", true);

		if (mltFields == null) {
			List<String> fields = new ArrayList<String>();
			for (MoreLikeThis mltField : MoreLikeThis.values()) {
				fields.add(mltField.toString());
			}
			mltFields = StringUtils.join(fields, ",");
			System.out.println("size: " + fields.size() + ", fields: " + mltFields + " count: " + count);
		}
		solrQuery.set("mlt.fl", mltFields);
		solrQuery.set("mlt.mintf", 1);
		solrQuery.set("mlt.match.include", "false");
		solrQuery.set("mlt.count", count);
		// solrQuery.set("mlt.mindf", mintf);
		solrQuery.set("rows", 1);

		List<SolrDocument> docs = getSimilarDocs(solrQuery);
		if (docs.isEmpty() && Integer.valueOf(solrQuery.get("mlt.mintf")) > 1) {
			solrQuery.set("mlt.mintf", Integer.valueOf(solrQuery.get("mlt.mintf")) - 1);
			docs = getSimilarDocs(solrQuery);
		}

		List<BriefBean> beans = new ArrayList<BriefBean>();
		for (SolrDocument doc : docs) {
			beans.add(solrServer.getBinder().getBean(BriefBeanImpl.class, doc));
		}
		return beans;
	}

	private List<SolrDocument> getSimilarDocs(SolrQuery solrQuery) throws SolrServerException {
		QueryResponse response = solrServer.query(solrQuery);
		// System.out.println("MoreLikeThis: " + response.getElapsedTime());

		@SuppressWarnings("unchecked")
		NamedList<Object> moreLikeThisList = (NamedList<Object>) response.getResponse().get("moreLikeThis");
		@SuppressWarnings("unchecked")
		List<SolrDocument> docs = (List<SolrDocument>) moreLikeThisList.getVal(0);
		return docs;
	}

	private List<String[]> getIdsForTerm(String query, List<String[]> ids, int start) {
		String content = getWebContent(solrUrl + "&q=" + URLEncoder.encode(query) + "&start=" + start);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			if (!matcher.group(1).contains("/_")) {
				ids.add(new String[]{matcher.group(1), URLEncoder.encode(query), String.valueOf(start)});
			}
			if (ids.size() == maxPerTerm) {
				break;
			}
			start++;
		}
		return ids;
	}

	private List<String[]> getIds() {
		// List<String> words = TermProvider.getRandomWords(10);
		List<String[]> recordIds = new ArrayList<String[]>();
		while (recordIds.size() < totalRecordsToTest) {
			String word = TermProvider.getRandomWords(1).get(0);
			System.out.print(word + " ");
			int start = 0;
			List<String[]> ids = new ArrayList<String[]>();
			boolean nextRound = true;
			while (nextRound) {
				int oldSize = ids.size();
				ids = getIdsForTerm(word, ids, ((start * rows) + 1));
				if (oldSize < ids.size() && ids.size() < maxPerTerm) {
					nextRound = true;
				} else {
					nextRound = false;
				}
				start++;
			}
			recordIds.addAll(ids);
		}
		System.out.println();
		System.out.println("Total number of records: " + recordIds.size());
		return recordIds;
	}

	private String getWebContent(String _url) {
		URL url;
		InputStream is = null;
		DataInputStream dis;
		String line;
		StringBuilder content = new StringBuilder();

		try {
			url = new URL(_url);
			is = url.openStream();  // throws an IOException
			dis = new DataInputStream(new BufferedInputStream(is));

			while ((line = dis.readLine()) != null) {
				// System.out.println(line);
				content.append(line);
			}
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ioe) {
				// nothing to see here
			}
		}
		return content.toString();
	}
}
