/*
 * Copyright 2007-2012 The Europeana Foundation
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.web.interceptor.ConfigInterceptor;
import eu.europeana.portal2.web.controllers.utils.RSSFeedParser;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.ThemeChecker;
import eu.europeana.portal2.web.presentation.model.IndexPage;
import eu.europeana.portal2.web.presentation.model.data.submodel.CarouselItem;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeaturedItem;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeedEntry;
import eu.europeana.portal2.web.util.ControllerUtil;

/**
 * Where people arrive.
 * 
 * @author Gerald de Jong <geralddejong@gmail.com>
 * @author Eric van der Meulen <eric.meulen@gmail.com>
 */

@Controller
public class IndexPageController {

	private static List<FeedEntry> feedEntries;
	private static Calendar feedAge;
	private List<CarouselItem> carouselItems;

	private static List<FeedEntry> pinterestEntries;
	private static Calendar pinterestAge;

	// @Autowired
	// private ProposedSearchTermSampler proposedSearchTermSampler;

	// @Autowired
	// private ClickStreamLogger clickStreamLogger;

	@Resource
	// private ReloadableResourceBundleMessageSource messageSource;
	private ResourceBundleMessageSource messageSource;

	@Resource
	private ConfigInterceptor corelib_web_configInterceptor;

	@Value("#{europeanaProperties['portal.blog.url']}")
	private String blogUrl;

	@Value("#{europeanaProperties['portal.blog.timeout']}")
	private Integer blogTimeout;

	@Value("#{europeanaProperties['portal.pinterest.url']}")
	private String pintUrl;

	@Value("#{europeanaProperties['portal.pinterest.feedurl']}")
	private String pintFeedUrl;

	@Value("#{europeanaProperties['portal.pinterest.timeout']}")
	private Integer pintTimeout;

	@Value("#{europeanaProperties['portal.pinterest.itemslimit']}")
	private Integer pintItemLimit;

	@RequestMapping("/index.html")
	public ModelAndView indexHandler(
			@RequestParam(value = "theme", required = false, defaultValue="default") String theme,
			HttpServletRequest request, HttpServletResponse response, Locale locale)
			throws Exception {
		IndexPage model = new IndexPage();
		// update dynamic items
		updateFeedIfNeeded(model);
		updateCarousel(model, locale);
		updateFeaturedItem(model, locale);
		model.setAnnounceMsg(getAnnounceMessage(locale));
		model.setTheme(ThemeChecker.check(theme));
		// fill model
		// model.setRandomTerms(proposedSearchTermSampler.pickRandomItems(locale));
		final ModelAndView page = ControllerUtil.createModelAndViewPage(model,
				locale, PortalPageInfo.INDEX);
		// clickStreamLogger.logUserAction(request,
		// ClickStreamLogger.UserAction.INDEXPAGE, page);
		try {
			corelib_web_configInterceptor.postHandle(request, response, this, page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return page;
	}

	private String getAnnounceMessage(Locale locale) {
		String message = null;
		try {
			message = messageSource.getMessage("announce_message_t", null,
					locale);
		} catch (NoSuchMessageException e) {
			if (locale != Locale.ENGLISH) {
				return getAnnounceMessage(Locale.ENGLISH);
			}
		}
		return StringUtils.trimToNull(message);
	}

	private void updateFeaturedItem(IndexPage model, Locale locale) {
		boolean keepFetching = true;
		int i = 1;
		while (keepFetching) {
			try {
				String label = String.format("notranslate_featured-item-%d_a_url_t", i);
				String url = messageSource.getMessage(label, null, locale);
				if (StringUtils.isNotEmpty(url) && !StringUtils.equals(label, url)) {
					i++;
				} else {
					keepFetching = false;
				}
			} catch (NoSuchMessageException e) {
				keepFetching = false;
			}
		}
		model.setFeaturedItem(new FeaturedItem(RandomUtils.nextInt(i - 1) + 1));
	}

	private void updateCarousel(IndexPage model, Locale locale) {
		carouselItems = new ArrayList<CarouselItem>();
		boolean keepFetching = true;
		int i = 1;
		while (keepFetching) {
			try {
				String label = String.format(
						"notranslate_carousel-item-%d_a_url_t", i);
				String url = messageSource.getMessage(label, null, locale);
				if (StringUtils.isNotEmpty(url)
						&& !StringUtils.equals(label, url)) {
					carouselItems.add(new CarouselItem(model, i, url));
				} else {
					keepFetching = false;
				}
				i++;
			} catch (NoSuchMessageException e) {
				keepFetching = false;
			}
		}
		model.setCarouselItems(carouselItems);
	}

	private void updateFeedIfNeeded(IndexPage model) {
		Calendar timeout = DateUtils.toCalendar(DateUtils.addHours(new Date(),
				-blogTimeout.intValue()));
		// read feed only once every few hours
		if ((feedAge == null) || feedAge.before(timeout)) {
			RSSFeedParser parser = new RSSFeedParser(blogUrl, 3);
			List<FeedEntry> newEntries = parser.readFeed();
			if ((newEntries != null) && (newEntries.size() > 0)) {
				feedEntries = newEntries;
				feedAge = Calendar.getInstance();
			}
		}
		model.setFeedEntries(feedEntries);

		timeout = DateUtils.toCalendar(DateUtils.addHours(new Date(), -pintTimeout.intValue()));
		if ((pinterestAge == null) || pinterestAge.before(timeout)) {
			RSSFeedParser parser = new RSSFeedParser(pintFeedUrl, pintItemLimit.intValue());
			List<FeedEntry> newEntries = parser.readFeed();
			if ((newEntries != null) && (newEntries.size() > 0)) {
				pinterestEntries = newEntries;
				pinterestAge = Calendar.getInstance();

			}
		}
		if ((pinterestEntries != null) && !pinterestEntries.isEmpty()) {
			model.setPinterestItem(pinterestEntries.get(RandomUtils.nextInt(pinterestEntries.size() - 1)));
		} else {
			model.setPinterestItem(null);
		}
		model.setPinterestUrl(pintUrl);
	}
}