/**
 *  api.js
 *
 *  @package	com.google
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @created	2011-08-24 08:51 GMT +1
 *  @version	2011-08-24 08:51 GMT +1
 */

/**
 *  @package	com.google
 *  @author		dan entous <contact@gmtplusone.com>
 *  @link		http://code.google.com/apis/language/translate/v2/getting_started.html
 *  @link		http://code.google.com/apis/language/translate/v2/using_rest.html
 */
js.utils.registerNamespace( 'com.google.translate' );


com.google.translate = function( options ) {
	
	
	var default_options = {
		
		api_key : null,
		source : null,
		target : null,
		callback : this.callback,
		uri : 'https://www.googleapis.com/language/translate/v2'
	
	};
	
	this.options = jQuery.extend( true, {}, default_options, options );
	
	
	/**
	 *	Translates source text from source language to target language
	 */
	Function.method( 'com.google.translate.translate', function(){
		
		var uri =	this.options.uri +
					'?key=' + this.options.api_key +
					'&source=' + this.options.source_language +
					'&target=' + this.options.target +
					'&q=' + this.options.source_text +
					'';
		
	});
	
	
	/**
	 *	List the source and target languages supported by the translate methods
	 */
	Function.method( 'com.google.translate.languages', function(){
		
		var uri =	this.options.uri +
					'/languages' +
					'?key=' + this.options.api_key +
					//'&target=' + this.options.target +
					'';
		
	});

	
	/**
	 *	Detect language of source text
	 */
	Function.method( 'com.google.translate.detect', function(){
		
		var uri =	this.options.uri +
					'/detect' +
					'?key=' + this.options.api_key +
					'&q=' + this.options.source_text +
					'';
		
	});
	
	
	/**
	 *	Callback for api translate results
	 */
	Function.method( 'com.google.translate.detect', function( response ){
		
		js.log( response );
		
	});


};