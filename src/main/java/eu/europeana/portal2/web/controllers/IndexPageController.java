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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;

import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.corelib.web.model.PageInfo;
import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.controllers.utils.RSSFeedParser;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.IndexPage;
import eu.europeana.portal2.web.presentation.model.data.submodel.CarouselItem;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeaturedItem;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeaturedPartner;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeedEntry;
import eu.europeana.portal2.web.util.ClickStreamLogger;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.Injector;

/**
 * Where people arrive.
 * 
 * @author Gerald de Jong <geralddejong@gmail.com>
 * @author Eric van der Meulen <eric.meulen@gmail.com>
 */

@Controller
public class IndexPageController {

	// @Resource private ProposedSearchTermSampler proposedSearchTermSampler;
	@Resource private ClickStreamLogger clickStreamLogger;

	@Resource private ReloadableResourceBundleMessageSource messageSource;

	@Resource(name="configurationService") private Configuration config;

	private static final List<String> freaments = Arrays.asList("blog", "featuredContent", "pinterest");

	private final Logger log = Logger.getLogger(getClass().getName());

	private static List<FeedEntry> feedEntries;
	private static Calendar feedAge;
	private List<CarouselItem> carouselItems;

	private static List<FeedEntry> pinterestEntries;
	private static Calendar pinterestAge;

	@RequestMapping("/index.html")
	public ModelAndView indexHandler(
			@RequestParam(value = "theme", required = false, defaultValue="") String theme,
			@RequestParam(value = "embeddedlang", required = false) String embeddedLang,
			@RequestParam(value = "fragment", required = false) String fragment,
			HttpServletRequest request,
			HttpServletResponse response,
			Locale locale) {
		Injector injector = new Injector(request, response, locale);

		log.info("===== index ====");

		IndexPage model = new IndexPage();
		// update dynamic items
		updateFeedIfNeeded(model);
		updatePinterest(model);
		updateCarousel(model, locale);
		updateFeaturedItem(model, locale);
		updateFeaturedPartner(model, locale);
		model.setAnnounceMsg(getAnnounceMessage(locale));
		injector.injectProperties(model);

		// fill model
		// model.setRandomTerms(proposedSearchTermSampler.pickRandomItems(locale));
		PageInfo view = PortalPageInfo.INDEX;
		if (freaments.contains(fragment)) {
			if (fragment.equals("blog")) {
				view = PortalPageInfo.INDEX_BLOG;
			} else if (fragment.equals("featuredContent")) {
				view = PortalPageInfo.INDEX_FEATUREDCONTENT;
			} else if (fragment.equals("pinterest")) {
				view = PortalPageInfo.INDEX_PINTEREST;
			}
		}
		final ModelAndView page = ControllerUtil.createModelAndViewPage(model, locale, view);
		injector.postHandle(this, page);
		clickStreamLogger.logUserAction(request, ClickStreamLogger.UserAction.INDEXPAGE, page);

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
	 * We assume that the English locale file has the same number of featured items / partners as all the other locale files 
	 **/
	/*
	private int getMessageCount(String msg){
		boolean keepFetching = true;
		int i = 1;
		while (keepFetching) {
			try {
				messageSource.getMessage(msg, null, Locale.ENGLISH);
			} catch (NoSuchMessageException e) {
				keepFetching = false;
			}
		}
		return i;
	}
	*/

	/**
	 * We assume that the English locale file has the same number of featured items / partners as all the other locale files 
	 **/
	/*
	private int getMessageCount(String msg){
		boolean keepFetching = true;
		int i = 1;
		while (keepFetching) {
			try {
				messageSource.getMessage(msg, null, Locale.ENGLISH);
			} catch (NoSuchMessageException e) {
				keepFetching = false;
			}
		}
		return i;
	}
	*/

	/**
	 * Sets the featured items list and the highlighted parter
	 */
	private void updateFeaturedItem(IndexPage model, Locale locale) {
		ArrayList<FeaturedItem> featuredItems = new ArrayList<FeaturedItem>();
		boolean keepFetching = true;
		int i = 1;
		while (keepFetching) {
			try {
				String label = String.format("notranslate_featured-item-%d_a_url_t", i);
				String url = messageSource.getMessage(label, null, locale);
				if (StringUtils.isNotEmpty(url) && !StringUtils.equals(label, url)) {
					FeaturedItem item = new FeaturedItem(i);
					item.setResponsiveImages(messageSource.getMessage(item.getImgUrl(), null, locale));
					featuredItems.add(item);
					i++;
				} else {
					keepFetching = false;
				}
			} catch (NoSuchMessageException e) {
				keepFetching = false;
			}
		}
		model.setFeaturedItems(featuredItems);
		if (i > 1) {
			int index = 0;
			if (featuredItems.size() > 1) {
				int max = featuredItems.size() - 1;
				index = RandomUtils.nextInt(max);
			}
			model.setFeaturedItem(featuredItems.get(index));
		}
	}

	/**
	 * Sets the featured partner list and the highlighted parter
	 */
	private void updateFeaturedPartner(IndexPage model, Locale locale) {
		ArrayList<FeaturedPartner> featuredPartners = new ArrayList<FeaturedPartner>();
		boolean keepFetching = true;
		int i = 1;
		while (keepFetching) {
			try {
				String label = String.format("notranslate_featured-partner-%d_a_url_t", i);
				String url = messageSource.getMessage(label, null, locale);
				if (StringUtils.isNotEmpty(url) && !StringUtils.equals(label, url)) {
					FeaturedPartner item = new FeaturedPartner(i);
					item.setResponsiveImages(messageSource.getMessage(item.getImgUrl(), null, locale));
					featuredPartners.add(item);
					i++;
				} else {
					keepFetching = false;
				}
			} catch (NoSuchMessageException e) {
				keepFetching = false;
			}
		}
		model.setFeaturedPartners(featuredPartners);
		if (featuredPartners.size() > 0) {
			int index = 0;
			if (featuredPartners.size() > 1) {
				int max = featuredPartners.size() - 1;
				index = RandomUtils.nextInt(max);
			}
			model.setFeaturedPartner(featuredPartners.get(index));
		}
	}

	private void updateCarousel(IndexPage model, Locale locale) {
		carouselItems = new ArrayList<CarouselItem>();
		boolean keepFetching = true;
		int i = 1;
		while (keepFetching) {
			try {
				String label = String.format("notranslate_carousel-item-%d_a_url_t", i);
				String url = messageSource.getMessage(label, null, locale);
				if (StringUtils.isNotEmpty(url)
						&& !StringUtils.equals(label, url)) {
					CarouselItem item = new CarouselItem(model, i, url);
					item.setResponsiveImages(messageSource.getMessage(item.getImgUrl(), null, locale));
					carouselItems.add(item);
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
		Calendar timeout = DateUtils.toCalendar(DateUtils.addHours(new Date(), -config.getBlogTimeout()));
		// read feed only once every few hours
		if ((feedAge == null) || feedAge.before(timeout)) {
			RSSFeedParser parser = new RSSFeedParser(config.getBlogUrl(), 3);
			parser.setStaticPagePath(config.getStaticPagePath());
			List<FeedEntry> newEntries = parser.readFeed();
			if ((newEntries != null) && (newEntries.size() > 0)) {
				feedEntries = newEntries;
				feedAge = Calendar.getInstance();
			}
		}
		model.setFeedEntries(feedEntries);
	}

	private void updatePinterest(IndexPage model) {
		Calendar timeout = DateUtils.toCalendar(DateUtils.addHours(new Date(), -config.getPintTimeout()));
		if ((pinterestAge == null) || pinterestAge.before(timeout)) {
			RSSFeedParser parser = new RSSFeedParser(config.getPintFeedUrl(), config.getPintItemLimit());
			parser.setStaticPagePath(config.getStaticPagePath());
			List<FeedEntry> newEntries = parser.readFeed();
			if ((newEntries != null) && (newEntries.size() > 0)) {
				pinterestEntries = newEntries;
				pinterestAge = Calendar.getInstance();
			}
		}
		if ((pinterestEntries != null) && !pinterestEntries.isEmpty()) {
			model.setPinterestItems(pinterestEntries);
			model.setPinterestItem(pinterestEntries.get(RandomUtils.nextInt(pinterestEntries.size() - 1)));
		} else {
			model.setPinterestItem(null);
		}
		model.setPinterestUrl(config.getPintUrl());
	}
}