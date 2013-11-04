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

package eu.europeana.portal2.services.impl.abstracts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.AutoCloseInputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;

import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.web.support.Configuration;

/**
 * This class pays attention to a file system directory and delivers pages if
 * they are present.
 * 
 * @author Gerald de Jong <geralddejong@gmail.com>
 */

public abstract class StaticCache {
	
	@Log
	protected Logger log;

	private static final String DOT = ".";

	@Resource
	protected Configuration config;

	private Map<String, Page> pageMapCache = new ConcurrentHashMap<String, Page>();
	private String staticPagePath;

	@Value("#{europeanaProperties['static.page.path']}")
	public void setStaticPagePath(String staticPagePath) {
		this.staticPagePath = staticPagePath;
	}

	private final Pattern fileNamePattern = Pattern.compile("[a-zA-Z0-9_/\\-]+\\.[a-z]+");

	public String getPage(String fileName, Locale language) {

		Page page = fetchPage(fileName, language);
		if (page == null) {
			return null;
		} else {
			return page.getContent();
		}
	}

	public void writeBinaryPage(String fileName, OutputStream out) throws IOException {
		writeBinaryPage(fileName, out, false);
	}

	public void writeBinaryPage(String fileName, OutputStream out, boolean isResponsiveImage) throws IOException {
		Page page = null;
		if (isResponsiveImage) {
			page = fetchImage(fileName);
		} else {
			page = fetchPage(fileName, null);
		}
		if (page != null) {
			page.writeBinaryContent(out);
		}
	}

	private Page fetchImage(String fileName) {
		Page page = pageMap().get(fileName);
		/*
		if (staticPageDir == null) {
			staticPageDir = new File(staticPagePath);
		}
		if (staticPageDir.isDirectory()) {
			File file = new File(staticPageDir, fileName);
			if (file.exists()) {
				return new Page(file);
			}
		}*/
		return page;
	}

	private Page fetchPage(String fileName, Locale language) {
		// check file name for a-z
		if (fileNamePattern.matcher(fileName).matches()) {
			// paranoid test
			if (checkForDot(fileName)) {
				String lingualFileName = fileName;
				if (language != null) {
					lingualFileName = fileName.replace(DOT, "_" + language.getLanguage() + DOT);
				}
				Page page = pageMap().get(lingualFileName);
				if (page == null) {
					String defautFileName = fileName.replace(DOT, "_" + Locale.ENGLISH.getLanguage() + DOT);
					page = pageMap().get(defautFileName);
					if (page == null) {
						log.warn(String.format("filename %s (%s,  %s) is not existing", fileName, lingualFileName, defautFileName));
						return null;
					}
				}
				return page;
			} else {
				log.warn("checkForDot: false for " + fileName);
			}
		} else {
			log.warn("fileName " + fileName + " does not match the pattern " + fileNamePattern.pattern());
		}
		return null;
	}

	private boolean checkForDot(String fileName) {
		return fileName.indexOf(DOT) == fileName.lastIndexOf(DOT);
	}

	public void invalidate() {
		pageMapCache.clear();
	}

	private Map<String, Page> pageMap() {
		Calendar timeout = DateUtils.toCalendar(DateUtils.addMinutes(new Date(), -getCheckFrequency()));
		if (pageMapCache.isEmpty()
			|| getLastCheck() == null
			|| getLastCheck().before(timeout)) {
			if (staticPagePath == null) {
				log.error("staticPagePath is not set!");
				throw new RuntimeException(staticPagePath + " is not set!");
			}
			File root = new File(staticPagePath);
			if (!root.isDirectory()) {
				log.error("staticPagePath: " + staticPagePath + " is not a directory!");
				throw new RuntimeException(staticPagePath + " is not a directory!");
			}
			if (!pageMapCache.isEmpty()) {
				pageMapCache.clear();
			}
			addToPageMap(root, root);
			setLastCheck(Calendar.getInstance());
		}
		return pageMapCache;
	}

	protected abstract Integer getCheckFrequency();

	protected abstract Calendar getLastCheck();

	protected abstract void setLastCheck(Calendar lastCheck);

	private void addToPageMap(File root, File directory) {
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				addToPageMap(root, file);
			} else {
				String baseFileName = file.getPath().substring(root.getPath().length()).replace('\\', '/');
				if (checkForDot(baseFileName)) {
					pageMapCache.put(baseFileName, new Page(file));
				} else {
					/*
					if (!baseFileName.endsWith(".svn-base") && !baseFileName.endsWith(".js")) {
						log.warning(String.format("Skip registering file to static cache: %s", file.getAbsolutePath()));
					}
					*/
				}
			}
		}
	}

	private class Page {
		private File file;
		private String content;
		private boolean fetched;

		private Page(File file) {
			this.file = file;
		}

		private synchronized String getContent() {
			if (!fetched) {
				try {
					content = StringUtils.join(FileUtils.readLines(file, "UTF-8"), '\n');
				} catch (Exception e) {
					log.error("Unable to read static page " + file);
				} finally {
					fetched = true;
				}
			}
			return content;
		}

		private synchronized void writeBinaryContent(OutputStream out)
				throws IOException {
			IOUtils.copy(new AutoCloseInputStream(new FileInputStream(file)), out);
		}
	}
}
