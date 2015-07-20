/*
 * Copyright 2007-2013 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved 
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 *  
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under 
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of 
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under 
 *  the Licence.
 */

package eu.europeana.portal2.web.controllers;

import eu.europeana.corelib.web.model.PageInfo;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.services.ClickStreamLogService;
import eu.europeana.portal2.services.ResponsiveImageService;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.IndexPage;
import eu.europeana.portal2.web.presentation.model.data.submodel.CarouselItem;
import eu.europeana.portal2.web.util.ControllerUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Where people arrive.
 * 
 * @author Gerald de Jong <geralddejong@gmail.com>
 * @author Eric van der Meulen <eric.meulen@gmail.com>
 */

@Controller
public class IndexPageController {

	
	@Resource
	private ClickStreamLogService clickStreamLogger;

	@Resource
	private MessageSource messageSource;

	@Resource
	private Configuration config;

	@Resource
	private ResponsiveImageService responsiveImageService;

	private List<CarouselItem> carouselItems;
	private static Calendar carouselAge;

	@RequestMapping("/index.html")
	public ModelAndView indexHandler(HttpServletRequest request, Locale locale) {

		IndexPage model = new IndexPage();
		// update dynamic items
		updateCarousel(model, locale);
		model.setAnnounceMsg(getAnnounceMessage(locale));
		model.setIsNofEnabled(config.isNofEnabled());

		// fill model
		// model.setRandomTerms(proposedSearchTermSampler.pickRandomItems(locale));
		PageInfo view = PortalPageInfo.INDEX;
		final ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, view);
		clickStreamLogger.logUserAction(request, ClickStreamLogService.UserAction.INDEXPAGE, page);
		return page;
	}

	private String getAnnounceMessage(Locale locale) {
		String message = null;
		try {
			message = messageSource.getMessage("announce_message_t", null, locale);
		} catch (NoSuchMessageException e) {
			if (locale != Locale.ENGLISH) {
				return getAnnounceMessage(Locale.ENGLISH);
			}
		}
		return StringUtils.trimToNull(message);
	}

	/**
	 * We assume that the English locale file has the same number of featured items / partners as all the other locale
	 * files
	 **/
	/*
	 * private int getMessageCount(String msg){ boolean keepFetching = true; int i = 1; while (keepFetching) { try {
	 * messageSource.getMessage(msg, null, Locale.ENGLISH); } catch (NoSuchMessageException e) { keepFetching = false; }
	 * } return i; }
	 */

	private synchronized void updateCarousel(IndexPage model, Locale locale) {
		Calendar timeout = DateUtils.toCalendar(DateUtils.addMinutes(new Date(),
				-config.getResponsiveCacheCheckFrequencyInMinute()));
		if ((carouselAge == null) || carouselAge.before(timeout)) {
			carouselItems = new ArrayList<CarouselItem>();
			boolean keepFetching = true;
			int i = 1;
			while (keepFetching) {
				try {
					String label = String.format("notranslate_carousel-item-%d_a_url_t", i);
					String url = messageSource.getMessage(label, null, locale);
					if (StringUtils.isNotEmpty(url) && !StringUtils.equals(label, url)) {
						CarouselItem item = new CarouselItem(model, i, url);
						String imgUrl = messageSource.getMessage(item.getImgUrl(), null, locale);
						item.setResponsiveImages(responsiveImageService.createResponsiveImage(
								imgUrl.replace("//", "/"), false, true));
						Map<String, String> translatableUrls = new HashMap<String, String>();
						boolean keepFetchingLanguages = true;
						int j = 1;
						while (keepFetchingLanguages) {
							String key = "";
							try {
								key = String.format("notranslate_carousel-item-%d_a_url_lang_%d_t", i, j);
								String langUrlRaw = messageSource.getMessage(key, null, null);
								keepFetchingLanguages = false;
								if (StringUtils.isNotBlank(langUrlRaw) && !key.equals(langUrlRaw)) {
									String[] langUrl = langUrlRaw.split(",");
									if (langUrl.length == 2) {
										translatableUrls.put(langUrl[0], langUrl[1]);
										keepFetchingLanguages = true;
									}
								}
							} catch (NoSuchMessageException e) {
								keepFetchingLanguages = false;
							} catch (ArrayIndexOutOfBoundsException e) {
								keepFetchingLanguages = false;
							}
							j++;
						}
						item.setTranslatableUrls(translatableUrls);
						carouselItems.add(item);
					} else {
						keepFetching = false;
					}
					i++;
				} catch (NoSuchMessageException e) {
					keepFetching = false;
				}
			}
			carouselAge = Calendar.getInstance();
		}
		model.setCarouselItems(carouselItems);
	}

}