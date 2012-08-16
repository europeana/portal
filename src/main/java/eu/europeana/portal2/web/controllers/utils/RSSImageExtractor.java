package eu.europeana.portal2.web.controllers.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RSSImageExtractor {

	static enum Param {SRC, TITLE};
	static final Pattern IMG_PATTERN = Pattern.compile("<img [^<>]+/>");
	static final Pattern SRC_PATTERN = Pattern.compile("src=\"([^\"]+)\"");
	static final Pattern TITLE_PATTERN = Pattern.compile("title=\"([^\"]+)\"");

	public static List<RSSImage> extractImages(String text) {
		List<RSSImage> images = new ArrayList<RSSImage>();

		Matcher imgMatcher = IMG_PATTERN.matcher(text);
		Map<Param, Pattern> patterns = new HashMap<Param, Pattern>(){
			private static final long serialVersionUID = 1L;
			{
				put(Param.SRC, SRC_PATTERN);
				put(Param.TITLE, TITLE_PATTERN);
			}
		};

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
			images.add(image);
		}
		return images;
	}
}
