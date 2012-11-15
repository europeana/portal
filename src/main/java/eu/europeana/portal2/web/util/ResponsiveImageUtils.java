package eu.europeana.portal2.web.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import eu.europeana.corelib.utils.ImageUtils;

public class ResponsiveImageUtils {

	// @Resource(name="configurationService") private static Configuration config;

	private static final Logger log = Logger.getLogger("ResponsiveImageUtils");

	private static String staticPagePath = Beans.getConfig().getStaticPagePath();

	private static Integer[] responsiveWidths = Beans.getConfig().getResponsiveImageWidths();

	private static String[] responsiveLabels = Beans.getConfig().getResponsiveImageLabels();

	final private static String CACHEDIR = "/sp/rss-blog-cache/";

	private static String directory;
	private static File dir;

	public ResponsiveImageUtils(){}

	public static Map<String, String> createResponsiveImage(String location) {
		return createResponsiveImage(location, true);
	}

	public static Map<String, String> createResponsiveImage(String location, boolean isURL) {
		Map<String, String> responsiveImages = new HashMap<String, String>();

		if (staticPagePath.endsWith("/")) {
			staticPagePath = staticPagePath.substring(0, staticPagePath.length() - 1);
		}

		if (dir == null) {
			directory = staticPagePath + CACHEDIR;
			File dir = new File(directory);
			if (!dir.exists()) {
				dir.mkdir();
			}
		}

		String extension = location.substring(location.lastIndexOf(".") + 1);
		String nameWithoutExt = location.substring(0, location.lastIndexOf(".") - 1);
		String toFS = nameWithoutExt.replace("/", "-").replace(":", "-").replace(".", "-");

		BufferedImage orig = null;
		try {
			if (isURL) {
				orig = ImageIO.read(new URL(location));
			} else {
				orig = ImageIO.read(new File(staticPagePath, location));
			}
		} catch (MalformedURLException e) {
			log.severe("MalformedURLException during reading in location: " + e.getLocalizedMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.severe("IOException during reading in location: " + e.getLocalizedMessage());
			e.printStackTrace();
		}

		if (orig == null) {
			return responsiveImages;
		}

		for (int i = 0, l = responsiveWidths.length; i<l; i++){
			String fileName = toFS + responsiveLabels[i] + "." + extension;

			// work out new image name 
			String fileUrl = "/sp/rss-blog-cache/" + fileName;
			String filePath = directory + fileName; 

			File outputfile = new File(filePath);
			BufferedImage responsive = null;
			if (!outputfile.exists()) {
				log.info(String.format("new file is %s, url is %s, old file is %s, ", filePath, fileUrl, location));
				try {
					int height = (int)Math.ceil((responsiveWidths[i] * orig.getHeight()) / orig.getWidth());
					responsive = ImageUtils.scale(orig, responsiveWidths[i], height);
				} catch (IOException e) {
					log.severe("IOException during scaling image: " + e.getLocalizedMessage());
					e.printStackTrace();
				}
				if (responsive == null) {
					continue;
				}
			}
			responsiveImages.put(responsiveLabels[i], fileUrl);

			if (!outputfile.exists()) {
				boolean created = false;
				try {
					created = outputfile.createNewFile();
				} catch (IOException e) {
					log.severe("IOException during create new file: " + e.getLocalizedMessage());
					e.printStackTrace();
				}

				if (created) {
					try {
						ImageIO.write(responsive, extension, outputfile);
					} catch (IOException e) {
						log.severe("IOException during writing new file: " + e.getLocalizedMessage());
						e.printStackTrace();
					}
					log.info("created " + outputfile);
				}
			}
		}
		return responsiveImages;
	}
}
