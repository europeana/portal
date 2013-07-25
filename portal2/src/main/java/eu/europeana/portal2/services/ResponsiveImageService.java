package eu.europeana.portal2.services;

import java.util.Map;

public interface ResponsiveImageService {

	Map<String, String> createResponsiveImage(String location);

	/**
	 * Creates responsive images
	 *
	 * @param location
	 *   The file's URL or path
	 * @param isURL
	 *   Whether the file is an URL or not
	 * @param useCarousel
	 *   Flag if use carousel's with and suffixes or the standard ones
	 *
	 * @return
	 */
	Map<String, String> createResponsiveImage(String location, boolean isURL, boolean useCarousel);

}