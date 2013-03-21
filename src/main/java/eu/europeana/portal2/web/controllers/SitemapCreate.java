package eu.europeana.portal2.web.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

class SitemapCreate {

	private static Map<String[],String> siteMapCache = new HashMap<String[],String> ();

	void createSitemap(String[] args, OutputStream outputWrite) {
		PerReqSitemap req = new PerReqSitemap(args, outputWrite);
		req.setOutputStream(outputWrite);
		req.setCache(siteMapCache);

		if(siteMapCache.containsKey(args)){
			//you can get it from a file if you do not want to read it from mem
			try {
				outputWrite.write(siteMapCache.get(args).toString().getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		Thread t = new Thread(req);
		t.start();

	}

	private class PerReqSitemap implements Runnable {
		
		Map<String[],String> siteMapCache = new HashMap<String[],String> ();
		String[] args;
		OutputStream out;
		
		PerReqSitemap(String[] args, OutputStream out) {
			this.args = args;
			this.out = out;
		}

		public void setCache(Map<String[],String> siteMapCache){
			this.siteMapCache = siteMapCache;
		}

		public void setOutputStream(OutputStream out){
			this.out = out;
		}

		public void createSitemap(){
			StringBuilder s = new StringBuilder();
			//create the sitemap here
			// out.write(s.toString().getBytes());
			//or you can put the file as well here
			siteMapCache.put(args, s.toString());
		}

		public void run(){
			createSitemap();
		}
	}
}

