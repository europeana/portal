package eu.europeana.portal2.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

import eu.europeana.portal2.web.controllers.utils.ApiFulldocParser;

public class WebUtil {

	private static final Logger log = Logger.getLogger(WebUtil.class.getName());

	public static String requestApiSession(String apiUrl, String api2key, String api2secret) {
		log.info("get API session " + api2key + ", " + api2secret);
		String apiSession = null;
		HttpURLConnection urlConn = null;
		try {
			String params = "api2key=" + URLEncoder.encode(api2key, "UTF-8") 
					+ "&secret=" + URLEncoder.encode(api2secret, "UTF-8");

			URL url = new URL(apiUrl + "/login");
			urlConn = (HttpURLConnection)url.openConnection();
			urlConn.setRequestMethod("POST");
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConn.setRequestProperty("Content-Length", "" + Integer.toString(params.getBytes().length));
			urlConn.setUseCaches(false);
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setInstanceFollowRedirects(false);
			urlConn.setFixedLengthStreamingMode(params.getBytes().length);
			urlConn.getOutputStream().write(params.getBytes());

			String cookie = urlConn.getHeaderField("Set-Cookie");
			if (cookie != null) {
				apiSession = cookie.substring(0, cookie.indexOf(";"));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(urlConn != null) {
				urlConn.disconnect();
			}
		}
		log.info("resulted apiSession: " + apiSession);
		return apiSession;
	}
	
	public static String getApiSession(String apiUrl, String api2key, String api2secret) {
		String apiSession = null;
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(apiUrl + "/login");
		method.addParameter("api2key", api2key);
		method.addParameter("secret", api2secret);
		method.setFollowRedirects(false);
		try {
			client.executeMethod(method);
			apiSession = method.getResponseHeader("Set-Cookie").getValue();
		} catch (HttpException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		log.info("resulted apiSession: " + apiSession);
		return apiSession;
	}
	
	private Response getOrPost(Request request) {
		
		String mErrorMessage = null;
		HttpURLConnection conn = null;
		Response response = null;
		try {
			conn = (HttpURLConnection) request.uri.openConnection();
			if (request.headers != null) {
				for (String header : request.headers.keySet()) {
					for (String value : request.headers.get(header)) {
						conn.addRequestProperty(header, value);
					}
				}
			}
			if (request instanceof POST) {
				byte[] payload = ((POST) request).body;
				conn.setDoOutput(true);
				conn.setFixedLengthStreamingMode(payload.length);
				conn.getOutputStream().write(payload);
				int status = conn.getResponseCode();
				if (status / 100 != 2)
					response = new Response(status,
							new Hashtable<String, List<String>>(), conn.getResponseMessage().getBytes());
			}
			if (response == null) {
				BufferedInputStream in = new BufferedInputStream(
						conn.getInputStream());
				byte[] body = readStream(in);
				response = new Response(conn.getResponseCode(),
						conn.getHeaderFields(), body);
			}
		} catch (IOException e) {
			e.printStackTrace(System.err);
			mErrorMessage = ((request instanceof POST) ? "POST " : "GET ")
					+ e.getLocalizedMessage();
		} finally {
			if (conn != null)
				conn.disconnect();
		}
		return response;
	}
	
	private static byte[] readStream(InputStream in) throws IOException {
		byte[] buf = new byte[1024];
		int count = 0;
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		while ((count = in.read(buf)) != -1)
			out.write(buf, 0, count);
		return out.toByteArray();
	}

	private class Request {
		public URL uri;
		public Map<String, List<String>> headers;

		public Request(URL uri, Map<String, List<String>> headers) {
			this.uri = uri;
			this.headers = headers;
		}
	}

	private class POST extends Request {
		public byte[] body;

		public POST(URL uri, Map<String, List<String>> headers, byte[] body) {
			super(uri, headers);
			this.body = body;
		}
	}

	public class Response {

		/** The HTTP status code */
		public int status;

		/** The HTTP headers received in the response */
		public Map<String, List<String>> headers;

		/** The response body, if any */
		public byte[] body;

		protected Response(int status, Map<String, List<String>> headers, byte[] body) {
			this.status = status;
			this.headers = headers;
			this.body = body;
		}
	}


}
