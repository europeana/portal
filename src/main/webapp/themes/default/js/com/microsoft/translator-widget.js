/**
 *  microsoft.js
 *
 *  @package	eu.europeana.translation-services
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @created	2011-07-07 15:27 GMT +1
 *  @version	2011-07-07 16:10 GMT +1
 */

/**
 *  @package	eu.europeana.translation-services
 *  @author		dan entous <contact@gmtplusone.com>
 *  @link		http://translate.google.com/translate_tools
 */
js.utils.registerNamespace( 'eu.europeana.translation_services.microsoft' );

eu.europeana.translation_services.microsoft = {

	options : {
		container : '#MicrosoftTranslatorWidget',
		html : '<div id="MicrosoftTranslatorWidget"></div>',
		js_src : 'http://www.microsofttranslator.com/ajax/v2/widget.aspx?mode=manual&from=' + eu.europeana.vars.locale + '&layout=ts'
	},
	
	BING_API_KEY : "28A86FF39138EFE1BC1D9E9E90B8AF5FC9A950E6",

	
	addToContainer : function( $container ) {
		
		if ( jQuery( this.options.container ).length < 1 ) {
			
			$container.append( this.options.html );
			js.loader.loadScripts([{
				file : this.options.js_src,
				path : ''
			}]);
			
		}
		
	}
	
	
};


/*
 *	<div id="MicrosoftTranslatorWidget" style="width: 200px; min-height: 83px; border-color: #3A5770; background-color: #78ADD0;"><noscript><a href="http://www.microsofttranslator.com/bv.aspx?a=http%3a%2f%2feuropeana.eu%2fportal%2f">Translate this page</a><br />Powered by <a href="http://www.microsofttranslator.com">Microsoft� Translator</a></noscript></div>
 *	<script>setTimeout(function() { var s = document.createElement("script"); s.type = "text/javascript"; s.charset = "UTF-8"; s.src = ((location && location.href && location.href.indexOf('https') == 0) ? "https://ssl.microsofttranslator.com" : "http://www.microsofttranslator.com" ) + "/ajax/v2/widget.aspx?mode=manual&from=en&layout=ts"; var p = document.getElementsByTagName('head')[0] || document.documentElement; p.insertBefore(s, p.firstChild); }, 0);</script>
 */