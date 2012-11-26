package eu.europeana;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class Portal2Starter {

	public static String getEuropeanaPath() {
		return getEuropeanaRoot().getAbsolutePath();
	}

	private static File getEuropeanaRoot() {
		try {
			return new File(".").getCanonicalFile();
		} catch (IOException e) {
			throw new RuntimeException("Couldn't get canonical file", e);
		}
	}

	public static void main(String... args) throws Exception {
		System.setProperty("org.eclipse.jetty.util.log.DEBUG", "true");

		Server server = new Server(8081);
		server.setStopAtShutdown(true);

		String webapp = getEuropeanaPath() + "/src/main/webapp";
		System.err.println("Webapp path = " + webapp);

		try {
			WebAppContext wac = new WebAppContext(webapp, "/portal");
			server.setHandler(wac);
		} catch (Exception e) {
			e.printStackTrace();
		}

		server.start();
	}
}
