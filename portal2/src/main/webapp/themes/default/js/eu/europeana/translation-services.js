js.utils.registerNamespace( 'eu.europeana.translation_services' );


eu.europeana.translation_services = {
	more_icon_class : 'icon-arrow-6',
	less_icon_class : 'icon-arrow-7',

	links : {

		$show_services : jQuery('#translate-item'),
		$return_to_original : jQuery( '<a href="" style="display:none;">' + eu.europeana.vars.msg.return_to_language.toLowerCase() + '</a>' ),
		$return_to_original_inline : jQuery( '<a href="" style="display:none;">' + eu.europeana.vars.msg.return_to_language + '</a>' ),
		$iconP: jQuery('#translate-item').find('.iconP')

	},

	containers : {

		$translation_services : jQuery( '<div id="translation-services"></div>' )

	},

	translator_selector_html :
		'<div id="microsoft-translate-element">' +
			'<select>' +
				'<option value="">' + eu.europeana.vars.msg.select_language + '</option>' +
			'</select>' +
			'<span class="small">Powered by <span class="bold">Microsoft<sup>&reg;</sup></span> Translator</span>' +
		'</div>',


	translators : {

		microsoft : {

			callbacks: [],
			translations : {}

		}

	},

	text_nodes : [],
	source_text : [],
	to_locale : undefined,


	init : function(callback) {

		this.links.$show_services.bind( 'click', { self : this }, this.toggleShowServices );
		this.links.$show_services.after( this.containers.$translation_services );

		this.links.$iconP.addClass(eu.europeana.translation_services.more_icon_class);

		this.captureOriginalTextNodes();
		this.setUpCallbacks('microsoft');

		this.returnedCount = 0;

		// Andy: we now dynamically load the ms translate javascript se we can't initialise it here.
		//this.addTranslatorMicrosoft();
		//this.containers.$translation_services.append(this.translator_selector_html);

		var self = this;

	   	jQuery("#translate-item").click(function(){
			js.loader.loadScripts([{
				name : 'microsoft-translator',
				file: 'translator' + js.min_suffix + '.js' + js.cache_helper,
				path: eu.europeana.vars.branding + '/js/com/microsoft/' + js.min_directory,
				callback: function(){
					self.addTranslatorMicrosoft();
				}
			}]);
	    });

	   	if(typeof callback != "undefined"){
	   		callback();
	   	}
	},


	captureOriginalTextNodes : function() {

		var self = this;

		jQuery('#item-details .translate, #additional-info .translate').each( function( index, value ) {
			self.text_nodes.push( jQuery( value ) );
		});

	},

	
	completion : function(){
		this.returnedCount ++;
		
		if(this.returnedCount == this.text_nodes.length){
			
			// reset returned count
			
			this.returnedCount		= 0;
			var incompleteTranslate	= false;
			
			for(var i=0; i<this.text_nodes.length; i++){
				
				if( jQuery.data(this.text_nodes[i]).waiting ){
					incompleteTranslate	= true;
				}
				
				// reset waiting data on text nodes for next time
				jQuery.data(this.text_nodes[i], 'waiting', true);
			}
			if(incompleteTranslate){
				eu.europeana.translation_services.renewToken();
			}
		}
			

	},

	/**
	 *
	 *	each translation should be placed into an array that matches up with the callback for the request per translator
	 *	the array entry contains the original text if no translation came thru then it will not be replaced
	 *	original nodes are kept and a click on the return to language link brings them back
	 *
	 * @param translator
	 */
	setUpCallbacks : function( translator ) {

		var self = this,
			i,
			ii = this.text_nodes.length;
				
		for ( i = 0; i < ii; i += 1 ) {

			(function() {

				var x = i;
				self.source_text[x] = jQuery.trim( self.text_nodes[x].html() );
				jQuery.data(self.text_nodes[x], 'waiting', true);
				
				self.translators[translator].callbacks[x] =
					function( response  ) {
						self.translators[translator].translations[self.to_locale][x] = response;
						self.applyTranslation( self.text_nodes[x], response );
						jQuery.data(self.text_nodes[x], 'waiting', false);
					};

			})();

		}


	},


	toggleShowServices : function( e ) {

		var self = e.data.self;
		e.preventDefault();

		if ( self.containers.$translation_services.is(':hidden') ) {
			self.links.$iconP.removeClass(eu.europeana.translation_services.more_icon_class);
			self.links.$iconP.addClass(eu.europeana.translation_services.less_icon_class);
			self.containers.$translation_services.slideDown();

		} else {
			self.links.$iconP.removeClass(eu.europeana.translation_services.less_icon_class);
			self.links.$iconP.addClass(eu.europeana.translation_services.more_icon_class);
			self.containers.$translation_services.slideUp();
		};

	},

	renewToken : function(){
		
		var value = jQuery('#microsoft-translate-element select').val();
		jQuery('#microsoft-translate-element select').val(null);
		jQuery('#microsoft-translate-element select').trigger('change');
		
		var js_src = '/' + eu.europeana.vars.portal_name + '/token.json';
		$.ajax({
			url: js_src,
			dataType: "json"
		})
		.done(function( data, textStatus, jqXHR ) {
			
			var newToken = "Bearer " + encodeURIComponent(data.access_token);
			
			eu.europeana.vars.bing_translate_key			= newToken;
			com.microsoft.translator.options.BING_API_KEY	= newToken;
			
			js.console.log('renewed token');
			
			jQuery('#microsoft-translate-element select').val(value);
			jQuery('#microsoft-translate-element select').trigger('change');
			
		})
		.fail(function( jqXHR, textStatus ) {
			js.console.log('failed to renew token: ' + textStatus);
		});
		
	},

	addTranslatorMicrosoft : function() {

		var self = this;

		eu.europeana.translation_services.microsoft = new com.microsoft.translator({

			BING_API_KEY : eu.europeana.vars.bing_translate_key,
			callback : 'eu.europeana.translation_services.microsoft.callback',
			browser_locale : eu.europeana.vars.locale,

			$container_for_selector : this.containers.$translation_services,
			"translator_selector_html" : this.translator_selector_html,
			
			"detect_nodes" : self.text_nodes

		});

		eu.europeana.translation_services.microsoft.init(function() {
			jQuery('#microsoft-translate-element select').on(
				'change',
				{ self : self, translator : 'microsoft' },
				self.handleTranslateRequest
			);
		});
	},


	handleTranslateRequest : function( e ) {

		var self = e.data.self,
			translator = e.data.translator,
			i,
			ii = self.text_nodes.length;

		// set the translator's to locale value
		self.to_locale = jQuery(this).val();
		if ( self.to_locale.length < 1 ) { return; }

		com.google.analytics.europeanaEventTrack('Europeana Translation', self.to_locale, jQuery('head link[rel="canonical"]').attr('href'));

		eu.europeana.translation_services[translator].options.to_locale = self.to_locale;

		self.addReturnToOriginal();

		for ( i = 0; i < ii; i = i + 1 ) {

			self.setTranslatorTranslationLocale( translator );
			self.setTranslatorTranslationDefault( translator, i );

			if ( self.translators[translator].translations[self.to_locale] && self.translators[translator].translations[self.to_locale][i] ) {
				self.applyTranslation( self.text_nodes[i], self.translators[translator].translations[self.to_locale][i] );
			}
			else {

				eu.europeana.translation_services[translator].options.text_to_translate = encodeURIComponent( self.source_text[i] );
				eu.europeana.translation_services[translator].options.callback = 'eu.europeana.translation_services.translators.' + translator + '.callbacks[' + i + ']';
				
				
				eu.europeana.translation_services.microsoft.translate();
				
			}

		}

	},


	/**
	 *	make sure the translator's translations array exists for the selected locale
	 *
	 * @param translator
	 */
	setTranslatorTranslationLocale : function( translator ) {

		if ( this.translators[translator].translations[this.to_locale] ) { return; }
		this.translators[translator].translations[this.to_locale] = [];

	},


	/**
	 *	make sure the translator's translations arrays have default values for each source text node
	 *
	 * @param translator
	 * @param i
	 */
	setTranslatorTranslationDefault : function( translator, i ) {

		if ( this.translators[translator].translations[this.to_locale][i] ) { return; }
		this.translators[translator].translations[this.to_locale][i] = null;

	},


	applyTranslation : function( $text_node, translation ) {

		$text_node.html( translation );
		js.utils.flashHighlight( $text_node, '#ffff00', '#ffffff', 1500);

	},

	addReturnToOriginal : function() {

		if ( this.links.$return_to_original.is(':hidden') ) {
		
			var originalAvailable = (typeof com.microsoft.translator.originalLanguage != 'undefined') && com.microsoft.translator.originalLanguage;
			
			if(originalAvailable){
				this.links.$translated_from_language = jQuery( '<span class="translatedFromLabel">' + eu.europeana.vars.msg.translated_from_language + ' ' + com.microsoft.translator.originalLanguage + ' -&nbsp;</span>');
			}
			else{
				var html = this.links.$return_to_original.html()
				html = html.charAt(0).toUpperCase() + html.slice(1);
				this.links.$return_to_original.html(html);
				this.links.$return_to_original.addClass('translatedFromLabel');
			}
			
			
			this.links.$return_to_original.css('display', 'inline-block');
			this.links.$return_to_original_inline.css('display', 'inline-block');
			
			this.links.$return_to_original.bind('click', { self : this }, this.handleReturnToOriginal )
			this.links.$return_to_original_inline.bind('click', { self : this }, this.handleReturnToOriginal )
			
			$('#main-fulldoc-area').prepend(
				this.links.$return_to_original.fadeIn()
			);
			
			if(originalAvailable){
				$('#main-fulldoc-area').prepend(
						this.links.$translated_from_language
				);
			}
			
			this.containers.$translation_services.append(
					this.links.$return_to_original_inline.fadeIn()
			);

		}
	},


	handleReturnToOriginal : function( e ) {

		var self = e.data.self,
			i,
			ii = self.text_nodes.length;

		e.preventDefault();

		for ( i = 0; i < ii; i = i + 1 ) {

			self.applyTranslation( self.text_nodes[i], self.source_text[i] );

		};

		jQuery('#microsoft-translate-element select').val('');
		self.links.$return_to_original_inline.fadeOut();
		self.links.$return_to_original.fadeOut();
		
		if(typeof self.links.$translated_from_language != 'undefined'){
			self.links.$translated_from_language.fadeOut();			
		}
	}

};
