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
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.web.controllers.utils.RSSFeedParser;
import eu.europeana.portal2.web.presentation.PortalFragmentInfo;
import eu.europeana.portal2.web.presentation.model.IndexPage;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeaturedItem;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeaturedPartner;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeedEntry;
import eu.europeana.portal2.web.util.ControllerUtil;
import eu.europeana.portal2.web.util.Injector;

@Controller
public class FragmentController {

	@Resource
	private AbstractMessageSource messageSource;

	@Resource(name = "configurationService")
	private Configuration config;
	
	private static List<FeedEntry> feedEntries;
	private static Calendar feedAge;

	private static List<FeedEntry> pinterestEntries;
	private static Calendar pinterestAge;

	private static Calendar featuredItemAge;
	private ArrayList<FeaturedItem> featuredItems;

	private static Calendar featuredPartnersAge;
	private ArrayList<FeaturedPartner> featuredPartners;

	@RequestMapping("/indexFragment.json")
	public ModelAndView indexFragment(@RequestParam(value = "fragment", required = false) String fragmentName,
			HttpServletRequest request, HttpServletResponse response,
			Locale locale) {
		Injector injector = new Injector(request, response, locale);

		IndexPage model = new IndexPage();
		injector.injectProperties(model);

		PortalFragmentInfo fragment = PortalFragmentInfo.getByFragmentName(fragmentName);
		if (fragment != null) {
			switch (fragment) {
			case INDEX_BLOG:
				updateFeedIfNeeded(model);
				break;

			case INDEX_FEATUREDCONTENT:
				updateFeaturedItem(model, locale);
				updateFeaturedPartner(model, locale);
				break;

			case INDEX_PINTEREST:
				updatePinterest(model);
				break;

			default:
				break;
			}

		}
		ModelAndView page = ControllerUtil.createModelAndViewFragment(model, fragment, locale);
		injector.postHandle(this, page);
		return page;
	}

	/**
	 * Sets the featured items list and the highlighted parter
	 */
	private synchronized void updateFeaturedItem(IndexPage model, Locale locale) {
		Calendar timeout = DateUtils.toCalendar(DateUtils.addMinutes(new Date(),
				-config.getResponsiveCacheCheckFrequencyInMinute()));
		if ((featuredItemAge == null) || featuredItemAge.before(timeout)) {
			featuredItems = new ArrayList<FeaturedItem>();
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
		}
		model.setFeaturedItems(featuredItems);
		if (featuredItems.size() > 0) {
			int index = 0;
			if (featuredItems.size() > 1) {
				index = RandomUtils.nextInt(featuredItems.size());
			}
			model.setFeaturedItem(featuredItems.get(index));
		}
	}

	/**
	 * Sets the featured partner list and the highlighted parter
	 */
	private synchronized void updateFeaturedPartner(IndexPage model, Locale locale) {
		Calendar timeout = DateUtils.toCalendar(DateUtils.addMinutes(new Date(),
				-config.getResponsiveCacheCheckFrequencyInMinute()));
		if ((featuredPartnersAge == null) || featuredPartnersAge.before(timeout)) {
			featuredPartners = new ArrayList<FeaturedPartner>();
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
		}
		model.setFeaturedPartners(featuredPartners);
		if (featuredPartners.size() > 0) {
			int index = 0;
			if (featuredPartners.size() > 1) {
				index = RandomUtils.nextInt(featuredPartners.size());
			}
			model.setFeaturedPartner(featuredPartners.get(index));
		}
	}

	private synchronized void updateFeedIfNeeded(IndexPage model) {
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

	private synchronized void updatePinterest(IndexPage model) {
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
			model.setPinterestItem(pinterestEntries.get(RandomUtils.nextInt(pinterestEntries.size())));
		} else {
			model.setPinterestItem(null);
		}
		model.setPinterestUrl(config.getPintUrl());
	}

}
