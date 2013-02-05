package eu.europeana.portal2.web.controllers.speed;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class SpeedTestUtils {

	public static void printStatistics(DescriptiveStatistics stat) {
		System.out.print(String.format("%5d ms (%2d occurences)", (int)stat.getSum(), stat.getN()));
		System.out.print(String.format(", mean: %4d (%4d-%5d)", (int)stat.getMean(), (int)stat.getMin(), (int)stat.getMax()));
		System.out.print(String.format(", median: %4d", (int)stat.getPercentile(50)));
		System.out.print(String.format(", deviation: %4d (%4d%%)", (int)stat.getStandardDeviation(),
				(int)(stat.getStandardDeviation()*100/stat.getMean())));

		System.out.print(", Percentiles: ");
		for (int i=1; i <= 20; i++) {
			System.out.print(String.format("%d%%: %d, ", i * 5, (int)(stat.getPercentile(i * 5))));
			// System.out.print(String.format("%3d", (int)(stat.getPercentile(i * 10) * 100 / stat.getMean())));
		}
		System.out.println();
	}

	public static void getWebContent(String _url) {
		URL url;
		InputStream is = null;
		DataInputStream dis;
		String line;

		try {
			url = new URL(_url);
			is = url.openStream();  // throws an IOException
			dis = new DataInputStream(new BufferedInputStream(is));

			while ((line = dis.readLine()) != null) {
				// System.out.println(line);
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
	}

}
