/**
 *  translator.js
 *
 *  @package	com.google
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @created	2011-09-15 17:27 GMT +1
 *  @version	2011-09-15 17:27 GMT +1
 */

/**
 *  @package	com.google
 *  @author		dan entous <contact@gmtplusone.com>
 */
js.utils.registerNamespace( 'com.google.analytics' );


com.google.analytics = {

	init : function() {
		
		this.createAnalyticsArray();
		this.setAccountId();
		this.trackPageView();
		this.loadApi();
		
	},
	
	
	createAnalyticsArray : function() {
		
		if ( window._gaq ) {
			
			throw new Error( 'window._gaq already exists' );
			
		}
		
		window._gaq = [];
		
	},
	
	
	setAccountId : function() {
		
		_gaq.push(['_setAccount', eu.europeana.vars.gaId]);
		
	},
	
	
	trackPageView : function() {
		
		_gaq.push(['_trackPageview']);
		
	},
	
	
	loadApi : function() {
		
		js.loader.loadScripts([{
			file : 'ga.js',
			path : ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/'
		}]);
		
	},
	
	/* Custom event tracking.
	 * 
	 * action: name of action to log
	 * category: optional, category to log event
	 * 
	 * */
	europeanaEventTrack : function(action, category, url) {
		
		
		if(js.debug){
			alert("Track Event:\n\naction =\t\t" + action + "\ncategory = \t" + category);
		}
		
		category = category || 'Europeana Portal';
	    _gaq.push(['_trackEvent',
	               category,							     			// category of activity
			       action,											    // action
			       url || jQuery('head link[rel="canonical"]').attr('href')	// label
	    ]);			
	},
	
	/* Custom event tracking.
	 * 
	 * @languageCode: one of:
	 * 		a language code
	 * 
	 * */
	translationEventTrack : function(languageCode) {
		this.europeanaEventTrack(languageCode, 'Europeana Translation');
	}
	
};

com.google.analytics.init();