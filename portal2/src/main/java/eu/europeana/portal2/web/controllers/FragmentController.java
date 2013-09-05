package eu.europeana.portal2.web.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import eu.europeana.portal2.services.Configuration;
import eu.europeana.portal2.services.FeedService;
import eu.europeana.portal2.services.ResponsiveImageService;
import eu.europeana.portal2.web.presentation.PortalFragmentInfo;
import eu.europeana.portal2.web.presentation.model.IndexPage;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeaturedItem;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeaturedPartner;
import eu.europeana.portal2.web.util.ControllerUtil;

@Controller
public class FragmentController {

	@Resource
	private AbstractMessageSource messageSource;

	@Resource
	private Configuration config;
	
	@Resource
	private FeedService feedService;

	@Resource
	private ResponsiveImageService responsiveImageService;

	private static Calendar featuredItemAge;
	private List<FeaturedItem> featuredItems;

	private static Calendar featuredPartnersAge;
	private List<FeaturedPartner> featuredPartners;

	@RequestMapping(value = "/indexFragment.json", params = "fragment=blog")
	public ModelAndView indexFragmentBlog() {
		IndexPage model = new IndexPage();
		model.setFeedEntries(feedService.getFeedEntries());
		return ControllerUtil.createModelAndViewFragment(model, PortalFragmentInfo.INDEX_BLOG);
	}

	@RequestMapping(value = "/indexFragment.json", params = "fragment=featuredContent")
	public ModelAndView indexFragmentFeaturedContent(Locale locale) {
		IndexPage model = new IndexPage();
		updateFeaturedItem(model, locale);
		updateFeaturedPartner(model, locale);
		return ControllerUtil.createModelAndViewFragment(model, PortalFragmentInfo.INDEX_FEATUREDCONTENT);
	}

	@RequestMapping(value = "/indexFragment.json", params = "fragment=pinterest")
	public ModelAndView indexFragmentPinterest() {
		IndexPage model = new IndexPage();
		model.setPinterestItems(feedService.getPinterestEntries());
		model.setPinterestUrl(config.getPintUrl());
		return ControllerUtil.createModelAndViewFragment(model, PortalFragmentInfo.INDEX_PINTEREST);
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
						String imgUrl = messageSource.getMessage(item.getImgUrl(), null, locale);
						item.setResponsiveImages(responsiveImageService.createResponsiveImage(
								imgUrl.replace("//", "/"), false, false));
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
						String imgUrl = messageSource.getMessage(item.getImgUrl(), null, locale);
						item.setResponsiveImages(responsiveImageService.createResponsiveImage(
								imgUrl.replace("//", "/"), false, false));
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
		if (featuredPartners.size() > 0) {
			int index = 0;
			if (featuredPartners.size() > 1) {
				index = RandomUtils.nextInt(featuredPartners.size());
			}
			model.setFeaturedPartner(featuredPartners.get(index));
		}
	}

}
