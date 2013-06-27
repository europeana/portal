package eu.europeana.portal2.web.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europeana.corelib.utils.ImageUtils;

public class ResponsiveImageUtils {

	private static final Logger log = LoggerFactory.getLogger(ResponsiveImageUtils.class);

	private static String staticPagePath = Beans.getConfig().getStaticPagePath();

	private static Integer[] responsiveWidths = Beans.getConfig().getResponsiveImageWidths();

	private static String[] responsiveLabels = Beans.getConfig().getResponsiveImageLabels();

	private static Integer[] responsiveCarouselWidths = Beans.getConfig().getResponsiveCarouselImageWidths();

	private static String[] responsiveCarouselLabels = Beans.getConfig().getResponsiveCarouselImageLabels();

	private static String responsiveCache = Beans.getConfig().getResponsiveCache();

	final private static String CACHEDIR = "/sp/rss-blog-cache/";

	private static String directory;
	private static File dir;

	public ResponsiveImageUtils(){}

	public static Map<String, String> createResponsiveImage(String location) {
		return createResponsiveImage(location, true, false);
	}

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
	public static Map<String, String> createResponsiveImage(String location, boolean isURL, boolean useCarousel) {
		Map<String, String> responsiveImages = new HashMap<String, String>();

		if (staticPagePath.endsWith("/")) {
			staticPagePath = staticPagePath.substring(0, staticPagePath.length() - 1);
		}

		if (dir == null) {
			directory = (responsiveCache != null) ? responsiveCache : staticPagePath + CACHEDIR;
			if (!directory.endsWith("/")) {
				directory += "/";
			}
			dir = new File(directory);
			if (!dir.exists()) {
				dir.mkdir();
			}
		}

		String extension = location.substring(location.lastIndexOf(".") + 1);
		String nameWithoutExt = location.substring(0, location.lastIndexOf("."));
		String toFS = nameWithoutExt.replace("http://", "").replace("/", "-").replace(":", "-").replace(".", "-");
		if (toFS.startsWith("-")) {
			toFS = toFS.substring(1);
		}
		if (toFS.endsWith("-")) {
			toFS = toFS.substring(0, toFS.length()-1);
		}

		Integer[] widths;
		String[] labels;
		if (useCarousel) {
			widths = responsiveCarouselWidths;
			labels = responsiveCarouselLabels;
		} else {
			widths = responsiveWidths;
			labels = responsiveLabels;
		}

		BufferedImage originalImage = null;
		for (int i = 0, l = widths.length; i<l; i++){
			String fileName = toFS + labels[i] + "." + extension;

			// work out new image name 
			String fileUrl = "/sp/rss-blog-cache/" + fileName;
			String filePath = directory + fileName;

			File outputfile = new File(filePath);
			BufferedImage responsiveImage = null;
			if (!outputfile.exists()) {
				log.info(String.format("new file is %s, url: %s, location: %s, ", filePath, fileUrl, location));
				if (originalImage == null) {
					originalImage = readOriginalImage(location, isURL);
					if (originalImage == null) {
						log.warn(String.format("The original image (%s) is not readable", location));
						return responsiveImages;
					}
				}

				try {
					int height = (int)Math.ceil((widths[i] * originalImage.getHeight()) / originalImage.getWidth());
					responsiveImage = ImageUtils.scale(originalImage, widths[i], height);
					// responsive = ImageUtils.compress(responsive, 0.8f);
				} catch (IOException e) {
					log.error("IOException during scaling image: " + e.getLocalizedMessage(), e);
				}

				if (responsiveImage == null) {
					continue;
				}
			}
			responsiveImages.put(labels[i], fileUrl);

			if (!outputfile.exists()) {
				boolean created = false;
				try {
					created = outputfile.createNewFile();
				} catch (IOException e) {
					log.error("IOException during create new file: " + e.getLocalizedMessage(), e);
				}

				if (created) {
					try {
						// compressAndShow
						ImageIO.write(responsiveImage, extension, outputfile);
					} catch (IOException e) {
						log.error("IOException during writing new file: " + e.getLocalizedMessage(), e);
					}
					log.info("created " + outputfile);
				}
			}
		}
		return responsiveImages;
	}
	
	private static BufferedImage readOriginalImage(String location, boolean isURL) {
		BufferedImage originalImage = null;
		try {
			if (isURL) {
				originalImage = ImageIO.read(new URL(location));
			} else {
				originalImage = ImageIO.read(new File(staticPagePath, location));
			}
		} catch (MalformedURLException e) {
			log.error(String.format("MalformedURLException during reading in location %s (is url? %b): %s", location, isURL, e.getLocalizedMessage()), e);
		} catch (IOException e) {
			log.error(String.format("IOException during reading in location %s (is url? %b):  %s", location, isURL, e.getLocalizedMessage()), e);
		}
		return originalImage;
	}
}
