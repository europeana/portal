package eu.europeana.portal2.services.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.utils.ImageUtils;
import eu.europeana.portal2.services.ResponsiveImageService;

public class ResponsiveImageServiceImpl implements ResponsiveImageService {

	final private static String CACHEDIR = "/sp/rss-blog-cache/";

	@Log
	private Logger log;

	@Value("#{europeanaProperties['static.page.path']}")
	private String staticPagePath;

	@Value("#{europeanaProperties['portal.responsive.widths']}")
	private String responsiveImageWidthString;

	@Value("#{europeanaProperties['portal.responsive.labels']}")
	private String responsiveImageLabelString;

	@Value("#{europeanaProperties['portal.responsive.carousel.widths']}")
	private String responsiveCarouselImageWidthString;

	@Value("#{europeanaProperties['portal.responsive.carousel.labels']}")
	private String responsiveCarouselImageLabelString;

	@Value("#{europeanaProperties['portal.responsive.cache']}")
	private String responsiveCache;

	private Integer[] responsiveCarouselImageWidths;
	private String[] responsiveCarouselImageLabels;
	private String[] responsiveImageLabels;
	private Integer[] responsiveImageWidths;

	private static String directory;
	private static File dir;

	public ResponsiveImageServiceImpl(){}

	/* (non-Javadoc)
	 * @see eu.europeana.portal2.services.impl.ResponsiveImageService#createResponsiveImage(java.lang.String)
	 */
	@Override
	public Map<String, String> createResponsiveImage(String location) {
		return createResponsiveImage(location, true, false);
	}

	/* (non-Javadoc)
	 * @see eu.europeana.portal2.services.impl.ResponsiveImageService#createResponsiveImage(java.lang.String, boolean, boolean)
	 */
	@Override
	public Map<String, String> createResponsiveImage(String location, boolean isURL, boolean useCarousel) {
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
			widths = getResponsiveCarouselImageWidths();
			labels = getResponsiveCarouselImageLabels();
		} else {
			widths = getResponsiveImageWidths();
			labels = getResponsiveImageLabels();
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
					int height = (widths[i] * originalImage.getHeight()) / originalImage.getWidth();
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

	private BufferedImage readOriginalImage(String location, boolean isURL) {
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
		} catch (IllegalArgumentException e) {
			log.error(String.format("IllegalArgumentException during reading in location %s (is url? %b): %s", location, isURL, e.getLocalizedMessage()), e);
		}
		return originalImage;
	}

	private Integer[] getResponsiveCarouselImageWidths() {
		if (responsiveCarouselImageWidths == null) {
			String[] imageWidths = responsiveCarouselImageWidthString.split(",");
			responsiveCarouselImageWidths = new Integer[imageWidths.length];

			for (int i = 0, len = imageWidths.length; i < len; i++) {
				responsiveCarouselImageWidths[i] = Integer.parseInt(imageWidths[i]);
			}
		}

		return responsiveCarouselImageWidths;
	}

	private String[] getResponsiveCarouselImageLabels() {
		if (responsiveCarouselImageLabels == null) {
			responsiveCarouselImageLabels = responsiveCarouselImageLabelString.split(",");
		}
		return responsiveCarouselImageLabels;
	}

	private String[] getResponsiveImageLabels() {
		if (responsiveImageLabels == null) {
			responsiveImageLabels = responsiveImageLabelString.split(",");
		}
		return responsiveImageLabels;
	}

	private Integer[] getResponsiveImageWidths() {
		if (responsiveImageWidths == null) {
			String[] imageWidths = responsiveImageWidthString.split(",");
			responsiveImageWidths = new Integer[imageWidths.length];

			for (int i = 0, len = imageWidths.length; i < len; i++) {
				responsiveImageWidths[i] = Integer.parseInt(imageWidths[i]);
			}
		}
		return responsiveImageWidths;
	}
}
