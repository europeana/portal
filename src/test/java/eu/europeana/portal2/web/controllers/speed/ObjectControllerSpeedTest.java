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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/servlet/portal2-mvc.xml", "/internal/portal2-development.xml"})
public class ObjectControllerSpeedTest {

	@Resource private SearchService searchService;

	private int totalRecordsToTest = 20;
	private int maxPerTerm = 10;
	private int rows = 20;
	private String solrUrl = "http://10.101.38.1:9595/solr/search/select?fl=europeana_id&rows=" + rows;
	private String portalUrl = "http://localhost:8080/portal/record";
	private Pattern pattern = Pattern.compile("<str name=\"europeana_id\">(.*?)</str>");

	@Test
	public void run() {
		System.out.println("run");
		System.out.println();
		List<String[]> recordIds = getIds();
		Map<String, DescriptiveStatistics> stats;

		for (int i = 1; i <= 5; i++) {
			stats = createNewStatistics();

			for (String[] recordId : recordIds) {
				testMlt(recordId, stats.get("mlt"));
			}
			for (String[] recordId : recordIds) {
				testMongo(recordId, stats.get("mongo"));
			}
			for (String[] recordId : recordIds) {
				testObjectPage(recordId, stats.get("page"));
			}

			System.out.println(String.format("--- %d.) mlt: %d%%, mongo: %d%%", i,
					(int)(stats.get("mlt").getSum() * 100 / stats.get("page").getSum()),
					(int)(stats.get("mongo").getSum() * 100 / stats.get("page").getSum()))
			);
			for (String type : stats.keySet()) {
				printStatistics(type, stats.get(type));
			}
		}
	}

	private Map<String, DescriptiveStatistics> createNewStatistics() {
		Map<String, DescriptiveStatistics> stats = new LinkedHashMap<String, DescriptiveStatistics>() {{
			put("page", new DescriptiveStatistics());
			put("mlt", new DescriptiveStatistics());
			put("mongo", new DescriptiveStatistics());
		}};
		return stats;
	}

	private void testObjectPage(String[] recordId, DescriptiveStatistics stat) {
		String url = String.format("http://localhost:8080/portal/record%s.html?query=%s&start=%s", recordId[0], recordId[1], recordId[2]);
		long t1 = new Date().getTime();
		getWebContent(url);
		long t2 = new Date().getTime();
		stat.addValue((t2 - t1));
	}

	private void testMlt(String[] recordId, DescriptiveStatistics stat) {
		long t1 = new Date().getTime();
		try {
			searchService.findMoreLikeThis(recordId[0]);
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		long t2 = new Date().getTime();
		stat.addValue((t2 - t1));
	}

	
	private void testMongo(String[] recordId, DescriptiveStatistics stat) {
		long t1 = new Date().getTime();
		try {
			searchService.findById(recordId[0], false);
		} catch (SolrTypeException e) {
			e.printStackTrace();
		}
		long t2 = new Date().getTime();
		stat.addValue((t2 - t1));
	}

	private void printStatistics(String type, DescriptiveStatistics stat) {
		System.out.print(String.format("[%5s] %5d ms", type, (int)stat.getSum()));
		System.out.print(String.format(", mean: %4d (%4d-%5d)", (int)stat.getMean(), (int)stat.getMin(), (int)stat.getMax()));
		System.out.print(String.format(", median: %4d", (int)stat.getPercentile(50)));
		System.out.print(String.format(", deviation: %4d (%4d%%)", (int)stat.getStandardDeviation(),
				(int)(stat.getStandardDeviation()*100/stat.getMean())));

		System.out.print(", Percentiles: ");
		for (int i=1; i <= 10; i++) {
			System.out.print(String.format("%2d", (int)(stat.getPercentile(i * 10) * 100 / stat.getMax())));
			if (i < 10) {
				System.out.print("-");
			} else {
				System.out.println();
			}
		}

		/*
		double[] values = stat.getSortedValues();
		int j = 10;
		for (values)
		*/
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
