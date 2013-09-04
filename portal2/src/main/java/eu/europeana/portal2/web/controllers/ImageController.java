package eu.europeana.portal2.web.controllers;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.europeana.corelib.db.entity.nosql.Image;
import eu.europeana.corelib.db.entity.nosql.ImageCache;
import eu.europeana.corelib.db.exception.DatabaseException;
import eu.europeana.corelib.db.service.ThumbnailService;
import eu.europeana.corelib.definitions.model.ThumbSize;

@Controller
public class ImageController {

	@Resource
	private ThumbnailService thumbnailService;

	@RequestMapping("/image")
	public byte[] getImage(@RequestParam(value = "url", required = false, defaultValue = "*:*") String url) {
		/*
		 * see ThumbnailServiceTest, MongoImageViewServlet
		 */
		ImageCache cache;
		try {
			cache = thumbnailService.findByOriginalUrl(url);
			Image image = cache.getImages().get(ThumbSize.TINY.toString());
			image.getHeight();
			image.getWidth();
			return image.getImage();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
