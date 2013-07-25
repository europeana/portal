package eu.europeana.portal2.web.util.apiconsole;

/**
 * Wrapper class for API result
 *
 * @author peter.kiraly@kb.nl
 */
public class ApiResult {

	/**
	 * The HTTP status code
	 */
	private int httpStatusCode;

	/**
	 * The content of the result
	 */
	private String content;

	public ApiResult(int httpStatusCode, String content) {
		super();
		this.httpStatusCode = httpStatusCode;
		this.content = content;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
