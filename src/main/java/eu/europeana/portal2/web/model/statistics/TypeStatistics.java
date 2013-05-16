package eu.europeana.portal2.web.model.statistics;

/**
 * Interface for API "by type" statistics
 *
 * @author peter.kiraly@kb.nl
 */
public interface TypeStatistics {

	/**
	 * The statistics record type (such as OBJECT, SEARCH..)
	 * @return
	 */
	String getRecordType();

	/**
	 * The profile within a given record type (such as full, standard)
	 * @return
	 */
	String getProfile();

	/**
	 * The number belongs to this profile
	 * @return
	 */
	long getCount();
}
