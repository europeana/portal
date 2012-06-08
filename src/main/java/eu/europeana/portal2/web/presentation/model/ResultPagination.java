package eu.europeana.portal2.web.presentation.model;

import java.util.List;

import eu.europeana.corelib.definitions.model.web.BreadCrumb;

/**
 * @author Sjoerd Siebinga <sjoerd.siebinga@gmail.com>
 * @since Jan 8, 2010 10:51:42 AM todo: documentation
 */
public interface ResultPagination {
	boolean isPrevious();

	boolean isNext();

	int getPreviousPage();

	int getNextPage();

	int getLastViewableRecord();

	int getNumFound();

	int getRows();

	int getStart();

	List<PageLink> getPageLinks();

	List<BreadCrumb> getBreadcrumbs();

	PresentationQuery getPresentationQuery();

	int getPageNumber();
}
