package eu.europeana.portal2.web.model.statistics;

/**
 * Interface for API "by user" statistics
 *
 * @author peter.kiraly@kb.nl
 */
public interface UserStatistics {

	/**
	 * Returns the name of the user
	 * @return
	 */
	String getName();

	/**
	 * Returns the API key (wskey)
	 * @return
	 */
	String getApiKey();

	/**
	 * Returns the number of calls within a time period
	 * @return
	 */
	long getCount();
}
