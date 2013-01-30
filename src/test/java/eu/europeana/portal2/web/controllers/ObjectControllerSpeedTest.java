package eu.europeana.portal2.web.controllers;

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

import org.junit.Test;

public class ObjectControllerSpeedTest {

	String solrUrl = "http://10.101.38.1:9595/solr/search/select?fl=europeana_id&q=";
	String portalUrl = "http://localhost:8080/portal/record";
	Pattern pattern = Pattern.compile("<str name=\"europeana_id\">(.*?)</str>");

	@Test
	public void run() {
		List<String> urls = getIds("philips");
		long t1 = new Date().getTime();
		for (String url : urls) {
			System.out.println(url);
			long t11 = new Date().getTime();
			getWebContent(url);
			System.out.println("took: " + (new Date().getTime() - t11));
		}
		long t2 = new Date().getTime();
		System.out.println("takes: " + ((t2 - t1) / urls.size()));
	}
	
	private List<String> getIds(String query) {
		List<String> ids = new ArrayList<String>();
		String content = getWebContent(solrUrl + URLEncoder.encode(query));
		Matcher matcher = pattern.matcher(content);
		int start = 1;
		while (matcher.find()) {
			ids.add(portalUrl + matcher.group(1) + ".html?query=" + URLEncoder.encode(query) + "&start=" + start);
			start++;
		}
		return ids;
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
				is.close();
			} catch (IOException ioe) {
				// nothing to see here
			}
		}
		return content.toString();
	}

}
