package eu.europeana.portal2.web.presentation.model;

import java.util.List;

import eu.europeana.corelib.definitions.model.web.BreadCrumb;

/**
 * @author Sjoerd Siebinga <sjoerd.siebinga@gmail.com>
 * @since Jan 9, 2010 12:47:37 PM
 */
public interface DocIdWindowPager {
	DocIdWindow getDocIdWindow();

	boolean isNext();

	boolean isPrevious();

	String getQueryStringForPaging();

	String getFullDocUri();

	String getNextFullDocUrl();

	String getPreviousFullDocUrl();

	String getNextUri();

	int getNextInt();

	String getPreviousUri();

	int getPreviousInt();

	String getQuery();

	String getReturnToResults();

	String getPageId();

	String toString();

	String getStartPage();

	List<BreadCrumb> getBreadcrumbs();

	int getFullDocUriInt();
}
