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

package eu.europeana.portal2.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.AutoCloseInputStream;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 * This class pays attention to a file system directory and delivers pages if
 * they are present.
 * 
 * @author Gerald de Jong <geralddejong@gmail.com>
 */

public class StaticPageCache {

	private static final String DOT = ".";

	private final Logger log = Logger.getLogger(getClass().getName());
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
		Page page = fetchPage(fileName, null);
		if (page != null) {
			page.writeBinaryContent(out);
		}
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
						log.severe(String.format("filename (%s,  %s) is not existing", fileName, lingualFileName, defautFileName));
						return null;
					}
				}
				return page;
			} else {
				log.severe("checkForDot: false for " + fileName);
			}
		} else {
			log.severe("fileName " + fileName + " does not match the pattern " + fileNamePattern.pattern());
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
		if (pageMapCache.isEmpty()) {
			if (staticPagePath == null) {
				log.severe("staticPagePath is not set!");
				throw new RuntimeException(staticPagePath + " is not set!");
			}
			File root = new File(staticPagePath);
			if (!root.isDirectory()) {
				log.severe("staticPagePath: " + staticPagePath + " is not a directory!");
				throw new RuntimeException(staticPagePath + " is not a directory!");
			}
			addToPageMap(root, root);
		}
		return pageMapCache;
	}

	private void addToPageMap(File root, File directory) {
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				addToPageMap(root, file);
			} else {
				String baseFileName = file.getPath().substring(root.getPath().length()).replace('\\', '/');
				if (checkForDot(baseFileName)) {
					pageMapCache.put(baseFileName, new Page(file));
				} else {
					if (!baseFileName.endsWith(".svn-base") && !baseFileName.endsWith(".js")) {
						log.warning(String.format("Skip registering file to static cache: %s", file.getAbsolutePath()));
					}
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
					log.severe("Unable to read static page " + file);
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
