package eu.europeana.portal2.web.util.rss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Image extractor does simple job: extract images from a HTML text.
 * 
 * @author peter.kiraly@kb.nl
 */
public class RSSImageExtractor {

	/**
	 * List of supported parameters
	 */
	static enum Param {SRC, TITLE};

	/**
	 * Regex pattern for images
	 */
	static final Pattern IMG_PATTERN = Pattern.compile("<img [^<>]+>");

	/**
	 * Regex pattern for HTML escaped images
	 */
	static final Pattern ESCAPED_IMG_PATTERN = Pattern.compile("&lt;img [^<>]+&gt;");

	/**
	 * Regex pattern for the src attribute of an image
	 */
	static final Pattern SRC_PATTERN = Pattern.compile("src=\"([^\"]+)\"");

	/**
	 * Regex pattern for the title attribute of an image
	 */
	static final Pattern TITLE_PATTERN = Pattern.compile("title=\"([^\"]+)\"");

	/**
	 * Extracts images from a HTML source
	 *
	 * @param text
	 *   The HTML text which might contain image elements.
	 *
	 * @return
	 *   The list of images
	 */
	public static List<RSSImage> extractImages(String text) {
		return extractImages(text, true);
	}

	/**
	 * Extracts images from a HTML source
	 *
	 * @param text
	 *   The HTML text which might contain image elements.
	 *
	 * @return
	 *   The list of images
	 */
	public static List<RSSImage> extractImages(String text, boolean useNormalImageFormat) {
		List<RSSImage> images = new ArrayList<RSSImage>();

		Matcher imgMatcher = (useNormalImageFormat)
							? IMG_PATTERN.matcher(text)
							: ESCAPED_IMG_PATTERN.matcher(text);
		Map<Param, Pattern> patterns = new HashMap<Param, Pattern>(){
			private static final long serialVersionUID = 1L; {
			put(Param.SRC, SRC_PATTERN);
			put(Param.TITLE, TITLE_PATTERN);
		}};

		while (imgMatcher.find()) {
			String imageHTML = imgMatcher.group();
			RSSImage image = new RSSImage();
			for (Entry<Param, Pattern> pattern : patterns.entrySet()) {
				Matcher matcher = pattern.getValue().matcher(imageHTML);
				if (matcher.find()) {
					switch (pattern.getKey()) {
						case SRC: image.setSrc(matcher.group(1)); break;
						case TITLE: image.setTitle(matcher.group(1)); break;
					}
				}
			}
			if (image.getSrc() != null) {
				images.add(image);
			}
		}
		return images;
	}
}
