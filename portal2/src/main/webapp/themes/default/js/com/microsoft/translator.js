/**
 *  translator.js
 *
 *  @package	com.microsoft
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @created	2011-07-26 12:10 GMT +1
 *  @version	2011-07-29 09:13 GMT +1
 */

/**
 *  @package	com.microsoft
 *  @author		dan entous <contact@gmtplusone.com>
 *  @link		http://sdk.microsofttranslator.com/AJAX/GettingStarted.aspx
 *  @link		http://msdn.microsoft.com/en-us/library/ff512404.aspx
 */
js.utils.registerNamespace( 'com.microsoft.translator' );


com.microsoft.translator = function( options ) {
	
	
	var default_options = {
			
			BING_API_KEY : null,
			
			// standard callback method
			callback : 'com.microsoft.translator.callback',
			
			// locale to translate to
			to_locale : undefined,
			
			// selector container and html
			$container_for_selector : undefined,			
			$translator_selector : undefined,
			translator_selector_html :
				'<div id="microsoft-translate-element">' +
					'<select>' +
						'<option>Select Language</option>' +
					'</select>' +
					'<span class="small">Powered by <span class="bold">Microsoft<sup>&reg;</sup> Translator</span>' +
				'</div>',
			
			// language info for the selector
			language_codes : {
				
				/**
				 *	cc for determining the language of the selector language list
				 */ 
				browser_locale : 'en',
				
				/**
				 *	string
				 *	raw response from microsoft
				 */
				available : undefined,
				
				/**
				 *	string
				 *	formated for the api's getLanguageNames call, e.g., ["cc","cc"]
				 */
				formated : undefined
				
			}
			
		};
		
	
	this.options = jQuery.extend( true, {}, default_options, options );
	
	com.microsoft.translator.options = this.options;	// I can't use 'this' in a function called from JSONP
	
	this.init = function( callback ) {
		this.addTranslatorToContainer();
		if ( callback ) { callback.call(); }
	};
	
	
	/**
	 *	helper function used to dynamically set the callback function
	 */
	com.microsoft.translator.prototype.setCallback = function( callback ) {
		
		this.callback = callback;
		
	};
	
	
	/**
	 *	a default callback method which should be replaced by a more specific callback method based on
	 *	the api call initiated
	 */
	com.microsoft.translator.prototype.callback = function( response ) {
		
		js.console.log( response );
	
	};
	
	
	/**
	 *	add translation selector to the container specified
	 *	add language possibilities to the selector
	 */
	com.microsoft.translator.prototype.addTranslatorToContainer = function() {
		this.options.$translator_selector = jQuery( this.options.translator_selector_html );
		this.options.$container_for_selector.append( this.options.$translator_selector );
		this.getLanguagesForTranslate();
	};
	
	
	/**
	 *	Returns a tokenized AppID which can be used as the AppID parameter in any method (except GetAppIdToken()).
	 *	A tokenized AppID should be used when the appID must be kept secret.
	 *	
	 *	@link http://msdn.microsoft.com/en-us/library/ff512398.aspx
	 *	
	 *
	 *	@param appId string
	 *	A string containing the Bing AppId.	
	 *	
	 *	@param minRatingRead int
	 *	An int to define the minimum rating translations require to be returned. The recommended minRatingRead is 5
	 *	as this will only include automatic translations and authority approved translations.
	 *
	 *	@param maxRatingRead int
	 *	An int to define the maximum rating that a user can write with using this token. The recommended maxRatingWrite
	 *	is from 1 to 4 for anonymous users, and from 6 to 10 for authoritative users whom you trust. Translations with
	 *	ratings of 5 or higher will replace automatic translations when calling Translate() or TranslateArray() with the
	 *	same appId. Translations with ratings lower then 5 will only be returned when calling GetTranslations() or
	 *	GetTranslationsArray().
	 *	
	 *	@param expireSeconds int
	 *	An int that defines the duration in seconds from now that the token is valid. The value can be between 
	 *	1 and 86400 (24 hours).
	 *
	 *	@return void
	 */
	/*
	com.microsoft.translator.prototype.getAppIdToken = function() {
		
        var js_src = 	'http://api.microsofttranslator.com/V2/Ajax.svc/GetAppIdToken' +
        				'?oncomplete=' + callback +
        				'&appId=' + options.BING_API_KEY +
        				'&minRatingRead=5' +
        				'&maxRatingWrite=1' +
        				'&expireSeconds=60';
        
        js.loader.loadScripts([{
			file : js_src,
			path : ''
		}]);
        
    },
    */
	
	
    /**
	 *	The GetLanguagesForTranslate method returns the list of language codes available
	 *	for all Microsoft Translator services besides text to speak. See which languages
	 *	are available for yourself by using the demo below.
	 *
	 *	@link http://sdk.microsofttranslator.com/AJAX/GetLanguagesForTranslate.aspx
	 *	@link http://msdn.microsoft.com/en-us/library/ff512401.aspx
	 *	@uri http://api.microsofttranslator.com/V2/Ajax.svc/GetLanguagesForTranslate
	 *
	 *	@param options object
	 *	
	 *		@var callback string
	 *		reference to callback function
	 *
	 *		@var appId string
	 *		bing api key
	 */
    com.microsoft.translator.prototype.getLanguagesForTranslate = function() {
		
    	var options =	this.options,
    		self    =	this,
    		js_src  =	'http://api.microsofttranslator.com/V2/Ajax.svc/GetLanguagesForTranslate' +
						'?oncomplete=' + options.callback +
						'&appId=' + options.BING_API_KEY;
    	
    	this.setCallback( function( response ) {
			
			var i,
				ii = response.length,
				language_codes = '[';
			
			options.language_codes.available = response;
			
			for ( i = 0; i < ii; i += 1 ) {
				
				language_codes += '"' + response[i] + '",';
				
			}
			
			language_codes += ']';
			
			options.language_codes.formated = language_codes;
			self.getLanguageNames( options );
		});
		
		
		js.loader.loadScripts([{
			file : js_src,
			path : ''
		}]);
		
    };
    
    
    /**
     *	The GetLanguageNames method retrieves the friendly names for language codes passed to it, and then localizes 
     *	them the passed locale language. This is helpful when you want to display which languages are available for 
     *	translation purposes, as most users won't know every culture code. Try the demo below to return some language 
     *	names based on the culture codes.
     *
     *	@link http://sdk.microsofttranslator.com/AJAX/GetLanguageNames.aspx
     */
    com.microsoft.translator.prototype.languageNamesCallback = function(response) {
		var i = 0,
		html = '',
		ii = response.length;

		var options = com.microsoft.translator.options;
		
		for ( i = 0; i < ii; i += 1 ) {
			
			if(options.language_codes.available[i].length>2){
				continue;
			}
			html += '<option value="' + options.language_codes.available[i] + '">' + response[i] + '</option>';
				
		}
		
		options.$translator_selector.find('select').append( html );
		
		
		// get orig language here
		
		var arraysText = '';
		for(var i=0; i<options.detect_nodes.length; i++){
			
			var html = options.detect_nodes[i].html();
			
			if(isNaN(parseInt(html))){		// ignore numeric data
				
				html = html.replace(/\'/g, '');
				html = html.replace(/\"/g, '');
	
				if(arraysText.length + 3 + html.length < 99998){
					if(arraysText.length > 0){
						arraysText += ',';
					} 
					arraysText += '"' + html + '"' 
				}					
			}
		}


		if(arraysText.length>0){
			
			arraysText = '[' + arraysText + ']'
			js_src_detect  =	'http://api.microsofttranslator.com/V2/Ajax.svc/DetectArray' +
			'?oncomplete=' + 'com.microsoft.translator.prototype.doneDetect' +
			'&appId=' + options.BING_API_KEY +
			'&texts=' + arraysText;
			
	    	try {	    		
	    		js.loader.loadScripts([{
	    			file : js_src_detect,
	    			path : ''
	    		}]);
	    	}
	    	catch(e) {
	    		js.console.log(e);
				console.log('trigger translator-ready 1')
	    		$(window).trigger('translator-ready');
	    	}				
		} 
		else{
			console.log('trigger translator-ready 2')
			$(window).trigger('translator-ready');
		}
    }

    com.microsoft.translator.prototype.getLanguageNames = function() {
    	
				
//    	this.setCallback( function( response ) {
			

//		});

    	var	options =	this.options,
		js_src  = 	'http://api.microsofttranslator.com/V2/Ajax.svc/GetLanguageNames' +
					'?oncomplete=com.microsoft.translator.prototype.languageNamesCallback'  +
				//	'?oncomplete=' + options.callback +
					'&appId=' + options.BING_API_KEY +
					'&locale=' + options.language_codes.browser_locale +
					'&languageCodes=' + options.language_codes.formated;
    	
		js.loader.loadScripts([{
			file : js_src,
			path : ''
		}]);
		
    };
    
    
	/**
	 *	The GetTranslations method retrieves an array of translations from one language
	 *	to another from the translation memory. GetTranslations differs from Translate as
	 *	it returns all available translations including those submitted by other users.
	 *
	 *	You must specify the maximum number of translations you want returned along with
	 *	the text, original language and translated text language. Try the demo below to
	 *	return a number of translations.
	 *
	 *	@link http://sdk.microsofttranslator.com/AJAX/GetTranslations.aspx
	 */
    /*
    com.microsoft.translator.prototype.getTranslations = function() {
    	
        window.mycallback = function(response) {
            var array = response.Translations;
            var translations = "";
            for (var i = 0; i < array.length; i++) {
                translations = translations + array[i].TranslatedText + "\n";
            }
            document.getElementById("resultMessage").innerHTML = translations;
        }

        var text = encodeURIComponent(document.getElementById("inputText").value);
        var languageFrom = document.getElementById("langFrom").default_options[document.getElementById("langFrom").selectedIndex].value;
        var languageTo = document.getElementById("langTo").default_options[document.getElementById("langTo").selectedIndex].value;
        var maxTranslations = document.getElementById("numberTranslations").value;
        var default_options = "{\"State\":\"someUniqueStateId\"}";
        var s = document.createElement("script");
        s.src = "http://api.microsofttranslator.com/V2/Ajax.svc/GetTranslations?oncomplete=mycallback&appId=<%= appIdToken %>&text=" + text + "&from=" + languageFrom +
		            "&to=" + languageTo + "&maxTranslations=" + maxTranslations + "&default_options=" + default_options;
        document.getElementsByTagName("head")[0].appendChild(s);
        
    };
    */
    
    
    /**
     *	The GetTranslationsArray method retrieves an array of translations from multiple translation candidates for multiple source texts. 
     *	GetTranslationsArray differs from TranslateArray as it returns all available 
     *	translations including those submitted by other users. You must specify the maximum number of 
     *	translations you want returned along with an array of the texts (all should be the same langauge), 
     *	original langauge and translated text langauge.
     *
     *	@link http://sdk.microsofttranslator.com/AJAX/GetTranslationsArray.aspx
     */
    /*
    com.microsoft.translator.prototype.getTranslationsArray = function() {
    
        window.mycallback = function(response) {
            var array = response;
            var translations = "";
            for (var i = 0; i < array.length; i++) {
                for (var count = 0; count < array[i].Translations.length; count++) {

                    translations = translations + (i+1) + "." + (count + 1) + ": " + array[i].Translations[count].TranslatedText + "<br />";
                }
            }
            document.getElementById("resultMessage").innerHTML = translations;
        }

        var languageFrom = document.getElementById("langFrom").default_options[document.getElementById("langFrom").selectedIndex].value;
        var languageTo = document.getElementById("langTo").default_options[document.getElementById("langTo").selectedIndex].value;
        var texts = "[\"" + encodeURIComponent(document.getElementById("element1").value) + "\", \"" + 
                            encodeURIComponent(document.getElementById("element2").value) + "\", \"" +
                            encodeURIComponent(document.getElementById("element3").value) + "\"]";
        var maxTranslations = document.getElementById("numberTranslations").value;
        var default_options = "{\"State\":\"someUniqueStateId\"}";
    	var s = document.createElement("script");
    	s.src = "http://api.microsofttranslator.com/V2/Ajax.svc/GetTranslationsArray?oncomplete=mycallback&appId=<%= appIdToken %>&texts=" + texts + "&from=" + languageFrom +
    				"&to=" + languageTo + "&maxTranslations=" + maxTranslations + "&default_options=" + default_options;
    	document.getElementsByTagName("head")[0].appendChild(s);
    	
    };
    */
    
    
    /**
     *	The translate method translates text from one language to another. The language to 
     *	translate to and from need to be included as method parameters along with the text. In 
     *	the example below you will see in the code-behind that the to and from languages have been 
     *	hard coded, this can be set dynamically through your own culture code parameters.  
     *	Enter some text in the box above and click the 'Translate' button, and the translated text 
     *	will be displayed. Click the aspx tab above to see exactly how this is done.
     *
     *	@link http://sdk.microsofttranslator.com/AJAX/Translate.aspx
     *	@link http://msdn.microsoft.com/en-us/library/ff512406.aspx
     *	@uri http://api.microsofttranslator.com/V2/Ajax.svc/Translate
     *
     *	@param appId string
     *	A string containing the Bing AppID.
     *	
     *	@param text string
     *	A string representing the text to translate.
     *
     * 	@param from string
     * 	A string representing the language code of the translation text. If left empty the response will include the result of language auto-detection.
     * 
     *	@param to string
     *	A string representing the language code to translate the text into.
     *
     *	@param contentType string
     *	The format of the text being translated. The supported formats are "text/plain" and "text/html". Any HTML needs to be well-formed.
     *
     *	@param category string
     *	The category of the text to translate. The only supported category is "general".
     *
     *	@return string
     *	A string representing the translated text.
     */
    com.microsoft.translator.prototype.doneDetect = function(data) {
    	
    	
    	var langs = {};
    	if(typeof data.length != 'undefined' && data.length){
    		for(var i=0; i<data.length; i++){
    			if(langs[data[i]]){
    				langs[data[i]] += 1;
    			}
    			else{
    				langs[data[i]] = 1;
    			}
    		}
    	}
    	var max = 0;
    	var top = '';
    	for (var key in langs) {
		  if (langs[key]>max){
			  max = langs[key];
			  top = key;
		  }
		}
    	
    	com.microsoft.translator.originalLanguageCode = top;
    	com.microsoft.translator.originalLanguage     = $('#microsoft-translate-element select option[value=' + top + ']').html();
    	
		$(window).trigger('translator-ready');

    }
    
    com.microsoft.translator.prototype.translate = function() {
    	
    	//var	options = this.options,
    	var	options = com.microsoft.translator.options,
    		js_src  =	'http://api.microsofttranslator.com/V2/Ajax.svc/Translate' +
    					'?oncomplete=' + options.callback +
						'&appId=' + options.BING_API_KEY +
						//'&from=' +
						'&to=' + options.to_locale + 
						'&text=' + options.text_to_translate +
						'';
    	
		$.ajax({
			url: js_src,
			dataType: "script",
			crossDomain: true,
		})
		.done(function( data, textStatus, jqXHR ) {
			eu.europeana.translation_services.completion();
		})
		.fail(function( jqXHR, textStatus ) {
			js.console.log('fail: text= ' + textStatus + '\n\njqXHR: ' + JSON.stringify(jqXHR) );
		});
    		
		
    	/*
        window.mycallback = function(response) { document.getElementById("resultMessage").innerHTML = response; }
		
		var text = encodeURIComponent(document.getElementById("inputText").value);
        var languageFrom = document.getElementById("langFrom").default_options[document.getElementById("langFrom").selectedIndex].value;
        var languageTo = document.getElementById("langTo").default_options[document.getElementById("langTo").selectedIndex].value;
        var s = document.createElement("script");
        s.src = "http://api.microsofttranslator.com/V2/Ajax.svc/Translate?oncomplete=mycallback&appId=<%= appIdToken %>&from=" + languageFrom + "&to=" + languageTo + "&text=" + text;
        document.getElementsByTagName("head")[0].appendChild(s);
        */
    };
    
    
    /**
     *	The TranslateArray method translates an array of text from one language to another.
     *	The language to translate to and from need to be included as method parameters along
     *	with the text. In the example below you will see in the aspx that the to and from
     *	languages have been hard coded, this can be set dynamically through your own culture
     *	code parameters. Enter some text in the box above and click the 'Translate' button,
     *	and the translated text will be displayed. Click the aspx tab above to see exactly
     *	how this is done.
     *    
     *	@link http://sdk.microsofttranslator.com/AJAX/TranslateArray.aspx
     *
     *	@var appId string
     *	A string containing the Bing AppID.
     *
     *	@var texts string
     *	An array containing the texts for translation.
     *
     *	@var from string
     *	A string representing the language code of the translation text.
     *	If left empty the response will include the result of language auto-detection.
     *
     *	@var to string
     *	A string representing the language code to translate the text to.
     *
     *	@var options string
     *	A TranslateOptions element containing the values below. They are all optional and default to the most common settings.
     *	
     *		Category:		A string containing the category (domain) of the translation. Defaults to "general".
     *		ContentType:	The format of the text being translated. The supported formats are "text/plain" and "text/html".
     *						Any HTML needs to be well-formed.
     *		Uri:			A string containing the content location of this translation.
     *		User:			A string used to track the originator of the submission.
     *		State:			User state to help correlate request and response. The same contents will be returned in the response.
     *
     *	@return TranslateArrayResponse
     *	Returns a TranslateArrayResponse array. Each TranslateArrayResponse has the following elements:
     *
     *		Error:						Indicates an error if one has occurred. Otherwise set to null.
     *		OriginalSentenceLengths:	An array of integers indicating the length of each sentence in the original source text. The length of the array indicates the number of sentences.
     *		TranslatedText:				The translated text.
     *		TranslatedSentenceLengths:	An array of integers indicating the length of each sentence in the translated text. The length of the array indicates the number of sentences.
     *		State:						User state to help correlate request and response. Returns the same content as in the request.
     */
    /*
    com.microsoft.translator.prototype.translateArray = function( e ) {
    
    	var self = e.data.self,
    		//options = e.data.options,
    		js_src = 	'http://api.microsofttranslator.com/V2/Ajax.svc/TranslateArray' +
						'?oncomplete=' + options.callback +
						'&appId=' + options.BING_API_KEY +
						//'&from=' +
						'&to=' + jQuery(this).val() + 
						'&texts=' + options.texts_to_translate +
						//'&options=' +
						'';			
		
		self.callback = function( response ) {
			
			var i,
				ii = options.original_text_nodes.length;
		
			for ( i = 0; i < ii; i += 1 ) {
				
				jQuery( options.original_text_nodes[i] ).html( response[i].TranslatedText );
				js.utils.flashHighlight( jQuery(options.original_text_nodes[i] ), '#ffff00', '#f3f3f3', 1500);
				
			}
		
		};
		
		
		js.loader.loadScripts([{
			file : js_src,
			path : ''
		}]);
		
		
        window.mycallback = function(response) {
            var array = response;
            var translations = "";
            for (var i = 0; i < array.length; i++) {
                translations = translations + (i + 1) + ": " + array[i].TranslatedText + "<br />";
            }
            document.getElementById("resultMessage").innerHTML = translations;
        }

        var languageFrom = document.getElementById("langFrom").default_options[document.getElementById("langFrom").selectedIndex].value;
        var languageTo = document.getElementById("langTo").default_options[document.getElementById("langTo").selectedIndex].value;
        var texts = "[\"" + encodeURIComponent(document.getElementById("element1").value) + "\", \"" +
                    encodeURIComponent(document.getElementById("element2").value) + "\", \"" +
                    encodeURIComponent(document.getElementById("element3").value) + "\"]";
        var default_options = "{\"State\":\"someUniqueStateId\"}";
        var s = document.createElement("script");
        s.src = "http://api.microsofttranslator.com/V2/Ajax.svc/TranslateArray?oncomplete=mycallback&appId=<%= appIdToken %>&from=" + languageFrom +
			"&to=" + languageTo + "&texts=" + texts + "&default_options=" + default_options;
        document.getElementsByTagName("head")[0].appendChild(s);
        
    };
    */
    
};
