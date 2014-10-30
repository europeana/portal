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
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.AutoCloseInputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.factory.AccountFactory;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
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
	private boolean objectStore;
	private static Thread t;

	@Value("#{europeanaProperties['static.page.path']}")
	public void setStaticPagePath(String staticPagePath) {
		this.staticPagePath = staticPagePath;
	}

	@Value("#{europeanaProperties['static.page.isobjectstore']}")
	public void setObjectStore(boolean objectStore) {
		this.objectStore = objectStore;
	}

	
	@Value("#{europeanaProperties['objectstore.username']}")
	public void setUsername(String username) {
		this.username = username;
	}
	@Value("#{europeanaProperties['objectstore.password']}")
	public void setPassword(String password) {
		this.password = password;
	}
	@Value("#{europeanaProperties['objectstore.authurl']}")
	public void setUrl(String url) {
		this.url = url;
	}
	@Value("#{europeanaProperties['objectstore.tenantid']}")
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	@Value("#{europeanaProperties['objectstore.tenantname']}")
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}


	@PostConstruct
	public void loadFiles(){
		if(t== null){
			t = new Thread(new ObjectStoreDownloader());
			t.start();
		}
	}

	private final Pattern fileNamePattern = Pattern
			.compile("[a-zA-Z0-9_/\\-]+\\.[a-z]+");

	private String username;

	private String password;

	private String url;

	private String tenantId;

	private String tenantName;

	public String getPage(String fileName, Locale language) {

		Page page = fetchPage(fileName, language);
		if (page == null) {
			return null;
		} else {
			return page.getContent();
		}
	}

	public void writeBinaryPage(String fileName, OutputStream out)
			throws IOException {
		writeBinaryPage(fileName, out, false);
	}

	public void writeBinaryPage(String fileName, OutputStream out,
			boolean isResponsiveImage) throws IOException {
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
		 * if (staticPageDir == null) { staticPageDir = new
		 * File(staticPagePath); } if (staticPageDir.isDirectory()) { File file
		 * = new File(staticPageDir, fileName); if (file.exists()) { return new
		 * Page(file); } }
		 */
		return page;
	}

	private Page fetchPage(String fileName, Locale language) {
		// check file name for a-z
		if (fileNamePattern.matcher(fileName).matches()) {
			// paranoid test
			if (checkForDot(fileName)) {
				String lingualFileName = fileName;
				if (language != null) {
					lingualFileName = fileName.replace(DOT,
							"_" + language.getLanguage() + DOT);
				}
				Page page = pageMap().get(lingualFileName);
				if (page == null) {
					String defautFileName = fileName.replace(DOT, "_"
							+ Locale.ENGLISH.getLanguage() + DOT);
					page = pageMap().get(defautFileName);
					if (page == null) {
						log.warn(String.format(
								"filename %s (%s,  %s) is not existing",
								fileName, lingualFileName, defautFileName));
						return null;
					}
				}
				return page;
			} else {
				log.warn("checkForDot: false for " + fileName);
			}
		} else {
			log.warn("fileName " + fileName + " does not match the pattern "
					+ fileNamePattern.pattern());
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
		Calendar timeout = DateUtils.toCalendar(DateUtils.addMinutes(
				new Date(), -getCheckFrequency()));
		if (pageMapCache.isEmpty() || getLastCheck() == null
				|| getLastCheck().before(timeout)) {
			if (staticPagePath == null) {
				log.error("staticPagePath is not set!");
				throw new RuntimeException(staticPagePath + " is not set!");
			}
			
			File root = new File(staticPagePath);
			if (!root.isDirectory()) {
				log.error("staticPagePath: " + staticPagePath
						+ " is not a directory!");
				throw new RuntimeException(staticPagePath
						+ " is not a directory!");
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
				String baseFileName = file.getPath()
						.substring(root.getPath().length()).replace('\\', '/');
				if (checkForDot(baseFileName)) {
					pageMapCache.put(baseFileName, new Page(file));
				} else {
					/*
					 * if (!baseFileName.endsWith(".svn-base") &&
					 * !baseFileName.endsWith(".js")) {
					 * log.warning(String.format
					 * ("Skip registering file to static cache: %s",
					 * file.getAbsolutePath())); }
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
					content = StringUtils.join(
							FileUtils.readLines(file, "UTF-8"), '\n');
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
			IOUtils.copy(new AutoCloseInputStream(new FileInputStream(file)),
					out);
		}
	}
	
	private class ObjectStoreDownloader implements Runnable{

		@Override
		public void run() {
			ClassLoader c=getClass().getClassLoader();
			URLClassLoader urlC = (URLClassLoader)c;
			URL[] urls = urlC.getURLs();
			
			if (objectStore && !new File(urls[0].getPath()+staticPagePath).exists()) {
				AccountConfig config = new AccountConfig();
						config.setUsername(username);
						config.setPassword(password);
						config.setAuthUrl(url);
						config.setTenantId(tenantId);
						config.setTenantName(tenantName);
				Account account = new AccountFactory(config).createAccount();
				Container container = account.getContainer("europeana");
				Collection<StoredObject> containers = container.list();
				long startDownloading = System.currentTimeMillis();
				for (StoredObject currentContainer : containers) {
					if (currentContainer.isObject()) {
						if (currentContainer.getName().contains("/")) {
							String[] path = currentContainer.getName().split("/");
							String basePath;
							
								basePath = new File(urls[0].getPath()).getAbsolutePath()+"/";
								
							for (int j = 0; j < path.length - 1; j++) {
								new File(basePath + path[j]).mkdir();
								basePath = basePath + path[j] + "/";
							}
							
						}
						currentContainer.getAsObject().downloadObject(
								new File(urls[0].getPath()+"/"+currentContainer.getName()));
					}

				}
				log.info("Finished dowloading in: " + (System.currentTimeMillis()- startDownloading ));

			}
			try {
				urlC.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
