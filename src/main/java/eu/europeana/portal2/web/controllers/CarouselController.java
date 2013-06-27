package eu.europeana.portal2.web.controllers;

import org.springframework.stereotype.Controller;

@Controller
public class CarouselController {
/*
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

						Map<String, String> translatableUrls = new HashMap<String, String>();
						boolean keepFetchingLanguages = true;
						int j = 1;
						while (keepFetchingLanguages) {
							String key = "";
							try{
								key = String.format("notranslate_carousel-item-%d_a_url_lang_%d", i, j);
								String[] langUrl =  messageSource.getMessage( key, null, null ).split(",");
								translatableUrls.put(langUrl[0], langUrl[1]);
							}
							catch (NoSuchMessageException e) {
								System.err.println( "Couldn't read language " + key);
								keepFetchingLanguages = false;
							}
							j++;
						}
						item.setTranslatableUrls(translatableUrls);
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
*/
}
