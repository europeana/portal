/**
 *  translator.js
 *
 *  @package	com.google
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @created	2011-09-15 17:27 gmt +1
 *  @version	2012-01-14 12:06 gmt +1
 */

/**
 *  @package	com.google
 *  @author		dan entous <contact@gmtplusone.com>
 */
js.utils.registerNamespace( 'com.google.analytics' );


com.google.analytics = {

	
	gaId : 'UA-XXXXXXXX-1',
	
		
	/* Custom event tracking.
	 * 
	 * action: name of action to log
	 * category: optional, category to log event
	 * 
	 * */
	europeanaEventTrack : function(action, category, url) {
		
		if(js.debug){
			alert("Track Event:\n\naction =\t\t" + action + "\ncategory = \t\t" + category + "\nurl = \t\t" 
					+ (url || jQuery('head link[rel="canonical"]').attr('href'))
			);
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
	},
	

	/**
	 * Event Tracking is a method available in the ga.js tracking code that you
	 * can use to record user interaction with website elements
	 *
	 * @param {string} category (required)
	 * The name you supply for the group of objects you want to track.
	 * 
	 * @param {string} action (required)
	 * A string that is uniquely paired with each category, and commonly used to 
	 * define the type of user interaction for the web object.
	 *
	 * @param {string} label (optional)
	 * An optional string to provide additional dimensions to the event data.
	 * 
	 * @param {int} value (optional)
	 * An integer that you can use to provide numerical data about the user event.
	 * 
	 * @param {boolean} non-interaction (optional)
	 * A boolean that when set to true, indicates that the event hit will not be
	 * used in bounce-rate calculation.
	 *
	 * @see https://developers.google.com/analytics/devguides/collection/gajs/methods/gaJSApiEventTracking
	 * @see https://developers.google.com/analytics/devguides/collection/gajs/eventTrackerGuide
	 */
	trackEvent : function( category, action, opt_label, opt_value, opt_noninteraction ) {
		
		if ( js.debug ) {
			 js.console.log(
			 	"Track Event:\n\n" +
			 	"action =\t\t" + action + "\n" +
			 	"category = \t\t" + category + "\n" + 
			 	"opt_label = \t\t" + opt_label + "\n" +
			 	"opt_value = \t\t" + opt_value + "\n" +
			 	"opt_noninteraction = \t\t" + opt_noninteraction
			 )
			 
		}
		
		_gaq.push(['_trackEvent', category, action, opt_label, opt_value, opt_noninteraction ]);
		
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
	
	
	setAccountId : function( gaId ) {
		
		gaId = gaId || this.gaId;
		_gaq.push(['_setAccount', gaId]);
		
	},
	
	
	createAnalyticsArray : function() {
		
		if ( window._gaq ) {
			
			throw new Error( 'window._gaq already exists' );
			
		}
		
		window._gaq = [];
		
	},
	
	
	init : function() {
		
		this.createAnalyticsArray();
		this.setAccountId( eu.europeana.vars.gaId );
		this.trackPageView();
		this.loadApi();
		
	}


};

com.google.analytics.init();

