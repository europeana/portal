package eu.europeana.portal2.web.presentation.model.submodel;

import java.util.List;

import eu.europeana.corelib.definitions.model.web.BreadCrumb;

/**
 * @author Sjoerd Siebinga <sjoerd.siebinga@gmail.com>
 * @since Jan 8, 2010 10:51:42 AM todo: documentation
 */
public interface ResultPagination {
	boolean isPrevious();

	boolean isNext();

	boolean isFirst();

	boolean isLast();

	int getFirstPage();

	int getLastPage();

	int getPreviousPage();

	int getNumberOfPages();

	int getNextPage();

	int getLastViewableRecord();

	int getNumFound();

	int getRows();

	int getStart();

	List<BreadCrumb> getBreadcrumbs();

	PresentationQuery getPresentationQuery();

	int getPageNumber();
}
