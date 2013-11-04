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

package eu.europeana.portal2.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;

import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.services.FeedService;
import eu.europeana.portal2.services.ResponsiveImageService;
import eu.europeana.portal2.web.presentation.model.data.submodel.FeedEntry;
import eu.europeana.portal2.web.util.rss.RSSFeedParser;

public class FeedServiceImpl implements FeedService {

	@Log
	private Logger log;

	@Resource
	private Configuration config;
	
	@Resource(name="portal2_taskScheduler")
	private  TaskScheduler scheduler;

	@Resource(name="portal2_taskExecutor")
	private  TaskExecutor executor;

	private ScheduledFuture<?> firstRunTask;
	private ScheduledFuture<?> updateFeedTask;
	private ScheduledFuture<?> updatePinterestTask;

	@Resource
	private ResponsiveImageService responsiveImageService;

	private static List<FeedEntry> feedEntries = new ArrayList<FeedEntry>();
	private static List<FeedEntry> pinterestEntries = new ArrayList<FeedEntry>();

	@PostConstruct
	public void scheduleFirstRun() {
		firstRunTask = scheduler.scheduleAtFixedRate(new FirstRunTask(), millisecondsMinutes(10));
	}

	@Override
	public List<FeedEntry> getFeedEntries() {
		return feedEntries;
	}

	@Override
	public List<FeedEntry> getPinterestEntries() {
		return pinterestEntries;
	}

	private int millisecondsMinutes(int minutes) {
		return (minutes*60*1000);
	}

	private int millisecondsHours(int hours) {
		return (hours*millisecondsMinutes(60));
	}

	/**
	 * FirstRunTask will execute tasks every 10 minutes to first successfully run... 
	 * And then schedule them on the configurated intervals.
	 *
	 */
	private class FirstRunTask implements Runnable {
		@Override
		public void run() {
			if (log.isTraceEnabled()) {
				log.trace("FIRST RUN SCHEDULER TASK: running...");
			}
			if (updateFeedTask == null) {
				if (feedEntries.isEmpty()) {
					executor.execute(new UpdateFeedTask());
				} else {
					updateFeedTask = scheduler.scheduleWithFixedDelay(new UpdateFeedTask(), millisecondsHours(config.getBlogTimeout()));
				}
			}
			if (updatePinterestTask == null) {
				if (pinterestEntries.isEmpty()) {
					executor.execute(new UpdatePinterestTask());
				} else {
					updatePinterestTask = scheduler.scheduleWithFixedDelay(new UpdatePinterestTask(), millisecondsHours(config.getPintTimeout()));
				}
			}
			if ((updateFeedTask != null) && (updatePinterestTask != null)) {
				if (firstRunTask.cancel(true)) {
					if (log.isTraceEnabled()) {
						log.trace("FIRST RUN SCHEDULER TASK: my job is done, terminating myself...");
					}
				} else {
					log.warn("FIRST RUN SCHEDULER TASK: self-termination failed...");
				}
			}
			if (log.isTraceEnabled()) {
				log.trace("FIRST RUN SCHEDULER TASK: finished...");
			}
		}
	}
	
	private class UpdateFeedTask implements Runnable {
		@Override
		public void run() {
			if (log.isTraceEnabled()) {
				log.trace("BLOG RSS FEED: updating...");
			}
			RSSFeedParser parser = new RSSFeedParser(config.getBlogUrl(), 3);
			parser.setStaticPagePath(config.getStaticPagePath());
			List<FeedEntry> newEntries = parser.readFeed(responsiveImageService);
			if ((newEntries != null) && (newEntries.size() > 0)) {
				feedEntries.clear();
				feedEntries.addAll(newEntries);
			}
			if (log.isTraceEnabled()) {
				log.trace("BLOG RSS FEED: finished...");
			}
		}
	}

	private class UpdatePinterestTask implements Runnable {
		@Override
		public void run() {
			if (log.isTraceEnabled()) {
				log.trace("PINTEREST: updating...");
			}
			RSSFeedParser parser = new RSSFeedParser(config.getPintFeedUrl(), config.getPintItemLimit());
			parser.setStaticPagePath(config.getStaticPagePath());
			List<FeedEntry> newEntries = parser.readFeed(responsiveImageService);
			if ((newEntries != null) && (newEntries.size() > 0)) {
				pinterestEntries.clear();
				pinterestEntries.addAll(newEntries);
			}
			if (log.isTraceEnabled()) {
				log.trace("PINTEREST: finished...");
			}
		}
	}
	
}
