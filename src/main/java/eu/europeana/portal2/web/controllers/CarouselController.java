package eu.europeana.portal2.web.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.data.CarouselPage;
import eu.europeana.portal2.web.presentation.model.data.submodel.CarouselItem;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.Injector;

@Controller
public class CarouselController {

	private final Logger log = Logger.getLogger(getClass().getName());
	
	@Resource private ReloadableResourceBundleMessageSource messageSource;

	@Resource(name="configurationService") private Configuration config;

	@RequestMapping(value="/carousel.json", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView carouselHandler(
			@RequestParam(value = "start", required = false, defaultValue="1") int start,
			@RequestParam(value = "rows", required = false, defaultValue="1") int rows,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale)
					throws Exception {
		Injector injector = new Injector(request, response, locale);

		CarouselPage model = new CarouselPage();
		model.setLocale(locale);
		if (start < 1) {
			start = 1;
		}
		model.setStart(start);
		injector.injectProperties(model);

		List<CarouselItem> carouselItems = new ArrayList<CarouselItem>();
		boolean keepFetching = true;
		int i = 1;
		int total = 0;
		while (keepFetching) {
			try {
				String label = String.format("notranslate_carousel-item-%d_a_url_t", i);
				String url = messageSource.getMessage(label, null, locale);
				if (StringUtils.isNotEmpty(url)
					&& !StringUtils.equals(label, url)) {
					total = i;
					if (i >= start && carouselItems.size() < rows) {
						CarouselItem item = new CarouselItem(model, i, url);
						item.setResponsiveImages(messageSource.getMessage(item.getImgUrl(), null, locale));
						carouselItems.add(item);
					}
				} else {
					keepFetching = false;
				}
				i++;
			} catch (NoSuchMessageException e) {
				keepFetching = false;
			}
		}
		model.setCarouselItems(carouselItems);
		model.setRows(carouselItems.size());
		model.setTotal(total);

		ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, PortalPageInfo.CAROUSEL);
		injector.postHandle(this, page);

		return page;
	}
}
