/**
 *  translator.js
 *
 *  @package	com.google
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @created	2011-07-07 15:27 GMT +1
 *  @version	2011-07-07 16:10 GMT +1
 */

/**
 *  @package	com.google
 *  @author		dan entous <contact@gmtplusone.com>
 *  @link		http://translate.google.com/translate_tools
 */
js.utils.registerNamespace( 'com.google.translator' );


com.google.translator = function( options ) {
	
	
	var default_options = {
		
		$container_for_translator : undefined,
		js_src : 'http://translate.google.com/translate_a/element.js?cb=',
		locale : 'en',
		gaId : undefined,
		
		translator : {
		
			html : '<div id="google-translate-element"></div>',
			container_id : 'google-translate-element',
			$container : undefined,
			callback : 'com.google.translator.callback'
		
		}
		
	},
	
	options = jQuery.extend( true, {}, default_options, options );
	
	
	this.init = function( callback ) {
		
		this.setUpTranslator();
		if ( callback ) { callback.call(); }
		
	};
	
	
	com.google.translator.prototype.setUpTranslator = function() {
		
		if ( options.$container_for_translator.length > 0 ) { this.addTranslatorToContainer(); }
		else { js.console.error( 'no container for translator specified' ); }
		
	};
	
	
	com.google.translator.prototype.addTranslatorToContainer = function() {
			
		options.$container_for_translator.append( options.translator.html );
		options.translator.$container = jQuery( '' + options.translator.container_id );
		
		js.loader.loadScripts([{
			name : 'google-translator-api',
			file : options.js_src + options.translator.callback,
			path : ''
		}]);
		
	};
	
	
	com.google.translator.prototype.callback = function() {
		
		new google.translate.TranslateElement(
				
			{
				pageLanguage : options.locale,
				autoDisplay: false,
				multilanguagePage : true,
				gaTrack : true,
				gaId : options.gaId
			},
			
			options.translator.container_id
			
		);
		
	};

};