js.utils.registerNamespace( 'eu.europeana.fulldoc' );

eu.europeana.fulldoc = {
	

	// provides priority order for which tab to open when no hash is given
	// provides a list of accepted hash values for validation
	tab_priority : [ '#related-items','#similar-content','#map-view' ],

	init : function() {

		// this is a copy/paste from index.js: TODO: make this a common script
		var initCarousels = function(){
			if(typeof Galleria == "undefined") {
		        window.setTimeout(initCarousels, 100);
		    }
		    else{
				Galleria.loadTheme(eu.europeana.vars.branding + '/js/galleria/themes/europeana/galleria.europeana.js');
				Galleria.configure({
						transition:		'fadeslide',		/* fade, slide, flash, fadeslide, pulse */
						carousel:		true,
						carouselSpeed:	1200,				/* transition speed */
						carouselSteps:	2,
						easing:			'galleriaOut',
						imageCrop:		false,				/* if true, make pan true */
						imagePan:		false,
						lightbox:		true,
						responsive:		true,
						thumbnails: false
				});
				
				Galleria.ready(function(options) {					
					this.$( 'container' ).css("border-radius", "10px 10px 0px 0px");

				});
				jQuery('#carousel-1').galleria({dataSource:carouselData});
		    }		
		};
		// end of paste
		
		
		this.loadComponents();
		this.addAutoTagHandler();

		jQuery('#item-save-tag').bind('submit', this.handleSaveTagSubmit );
		jQuery('#item-save').bind('click', this.handleSaveItemClick );
		jQuery('#item-embed').bind('click', this.handleEmbedClick );
		
		jQuery('#urlRefIsShownBy').bind('click', this.handleRedirectIsShownByClick );
		
		jQuery('#urlRefIsShownAt').bind('click', this.handleRedirectIsShownAtClick );
		
		jQuery('#urlRefIsShownByImg').bind('click', this.handleRedirectIsShownByImgClick );
		
		jQuery('#urlRefIsShownAtImg').bind('click', this.handleRedirectIsShownAtImgClick );
		
		jQuery('#urlRefIsShownByPlay').bind('click', this.handleRedirectIsShownByPlayClick );
		
		jQuery('#lightbox_href').bind('click', this.handleRedirectIsShownByImgClick );
		
		
		// Andy: conditional load test
		jQuery("#query-input").focus(function(){
			europeana_bootstrap.common.loadResultSizer(
				function(){ console.log("in callback for fulldoc.js loadResultSizer"); }
			);
		});
		
		initCarousels();

	},

	
	loadComponents : function() {
		
		var self = eu.europeana.fulldoc;
		
		// dependency group - external search services
			
		js.loader.loadScripts([{
			name : 'external-search-services',
			file : 'external-search-services' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			callback : function() { eu.europeana.ess.init(); }
		}]);
		
		
		// dependency group - embed click functionality
		
		js.loader.loadScripts([{
			file: 'window-open' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/js/' + js.min_directory
		}]);
			
			
			
		// move this to bootstrap
			
		js.loader.loadScripts([{
			name: 'jquery-tools',
			file : 'jquery.tools.min.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/jquery/',
			dependencies : [ 'jquery-1.4.4.min.js' ]
		}]);
			
		js.loader.loadScripts([{
			name : 'jwplayer',
			file : 'jwplayer.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/jwplayer/mediaplayer-5.8/'					
		}]);
			
		js.loader.loadScripts([{
			file : 'fulldoc-lightbox' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'jquery-tools', 'jwplayer'],
				
			callback: function(){
				
				(function ($) {
					$.event.special.imageload = {
						add: function (hollaback) {
							if ( this.nodeType === 1 && this.tagName.toLowerCase() === 'img' && this.src !== '' ) {
								// Image is already complete, fire the hollaback (fixes browser issues were cached
								// images isn't triggering the load event)
								if ( this.complete || this.readyState === 4 ) {
									
									$(this).live('load', hollaback.handler);
									hollaback.handler.apply(this);
								}

								// Check if data URI images is supported, fire 'error' event if not
								else if ( this.readyState === 'uninitialized' && this.src.indexOf('data:') === 0 ) {

									$(this).trigger('error');
								}
								else {
									$(this).bind('load', hollaback.handler);
								}
							}
						}
					};
				}(jQuery));
				

				/* this callback ensures that the trigger image overlay is correctly sized */						
				//var imgOnLoad = function(img, msg){
				var imgOnLoad = function(){

					var lightboxImg		= jQuery(".content-image");
					
					/* function to size and show the lightbox 'toolbar' and initialise lightbox (should only be called when main lightbox image has loaded)  */
					var setUpTrigger = function(type){

						var doSetUpTrigger = function(initLightbox){
							var triggerDiv = jQuery("div.trigger");
							var triggerImg = jQuery("img.trigger");
							var realWidth = parseInt(triggerImg.width());
							if(realWidth == 0){
								realWidth = parseInt(triggerImg.width);
							}
							triggerDiv.css("display", "block");
							triggerDiv.css("width", realWidth + "px");
															
							if(initLightbox){
								//$("#lightbox_href").removeAttr("href");
								//$("#lightbox_href").attr("rel", "#lightbox");
								eu.europeana.lightbox.init();
							}
						}; // end doSetUpTrigger
						
						
						
						if(type == "img"){ // for images we check that hot-linking is allowed before enabling the lightbox trigger (i.e. we wait for the image to load and check the dimensions)
						
							lightboxImg.bind('imageload', function(){
								var lightboxImgW = eu.europeana.lightbox.getImgDimensionsFromSrc(lightboxImg.attr("src")).w;
								if(lightboxImgW > 0){
									doSetUpTrigger(true);
								}
							});
						}
						else if(type == "other"){
							doSetUpTrigger(false);
						}
					}
					if(typeof lightboxImg != 'undefined' && typeof lightboxImg.attr("src") != 'undefined' && lightboxImg.attr("src").length>0){
						setUpTrigger("img");
					}
					//else if(jQuery("div.playerDiv a ") && jQuery("div.playerDiv a ").attr("href").length > 0){
					//	setUpTrigger("other");								
					//}
					else{
						setUpTrigger("other");								
					}

				}; // end imageOnLoad
				
				var triggerImage = $("img.trigger");
				triggerImage.bind('imageload', function(){
					imgOnLoad();
				});
			}
		}]);

		
	
		// dependency group - carousel, tabs and truncate content functionality (+citation)
	
		js.loader.loadScripts([{
			name : 'carousel',
			file : 'carousel' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/com/gmtplusone/' + js.min_directory
		}]);
	
		js.loader.loadScripts([{
			name : 'tabs',
			file : 'tabs' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/com/gmtplusone/' + js.min_directory,
			dependencies : [ 'carousel' ],
			callback : function() {self.addTabs();}
		}]);
		
		js.loader.loadScripts([{
			file : 'citation' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'tabs' ],
			callback: function(){eu.europeana.citation.init();}
		}]);
		
		js.loader.loadScripts([{
			name : 'truncate-content',
			file : 'truncate-content' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/com/gmtplusone/' + js.min_directory,
			callback: function() { self.adjustDescription(); },
			dependencies : ['tabs']
		}]);
			
		// dependency group - addthis functionality
			
		js.loader.loadScripts([{
			file: 'addthis' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/com/addthis/' + js.min_directory,
			callback : function() {
				self.addThis();
			}
		}]);
	


		js.loader.loadScripts([{
			name : 'translation-services',
			file: 'translation-services' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			callback : function() {eu.europeana.translation_services.init(
			
					// Andy: this callback within a callback expands the link to the service and triggers the loading of the microsoft translate scripts
					// comment out this line to save 300 - 385 milliseconds of initial load time
					// leave this line in place to have the translator automatically opened 
					
					// function(){jQuery("#translate-item").trigger('click');}
					
			);}
		}]);
		
		
	},

	
	handleRedirectIsShownByClick: function ( e ) {
		com.google.analytics.europeanaEventTrack('IsShownBy', 'Europeana Redirects');
	},

	handleRedirectIsShownAtClick: function ( e ) {
		com.google.analytics.europeanaEventTrack('IsShownAt', 'Europeana Redirects');
	},
	
	handleRedirectIsShownByImgClick: function ( e ) {
		com.google.analytics.europeanaEventTrack('IsShownBy Img', 'Europeana Redirects');
	},
	
	handleRedirectIsShownAtImgClick: function ( e ) {
		com.google.analytics.europeanaEventTrack('IsShownAt Img', 'Europeana Redirects');
	},
	
	handleRedirectIsShownByPlayClick: function ( e ) {
		com.google.analytics.europeanaEventTrack('IsShownBy Play', 'Europeana Redirects');
	},
	
	handleEmbedClick : function ( e ) {
		e.preventDefault();
		
		js.open.openWindow({
			url : jQuery(this).attr('href'),
			specs : {
				width : 960,
				height : 600,				
				left : ( window.screen.width - 960 ) / 2,
				top : ( window.screen.height - 600 ) / 2,
				scrollbars:"yes"
			}
		});
		
	},
	
	
	addTabs : function() {
		eu.europeana.tabs = {};
		eu.europeana.tabs.explore = new com.gmtplusone.tabs(
			'#explore-further',
			{ callbacks : { opened : eu.europeana.fulldoc.tabFeedback }	}
		);
		
		eu.europeana.tabs.explore.init( eu.europeana.fulldoc.addCarousels );		
	},
	
	
	openTab : function() {
		var	tab_priority = eu.europeana.fulldoc.tab_priority,
			tab_to_open = '',
			menu_ids = eu.europeana.tabs.explore.options.menu_ids,
			hash = window.location.hash,	
			priority_found = false,
			i,
			ii = menu_ids.length,
			x,
			xx = tab_priority.length;
		
	
		// find the prioritized tab that exists on the page
			for ( x = 0; x < xx; x += 1 ) {
				for ( i = 0; i < ii; i += 1 ) {
					if ( menu_ids[i] === tab_priority[x] ) {
						tab_to_open = tab_priority[x];
						priority_found = true;
						break;
					}
				}
				if ( priority_found ) { break; }
			}
		
		// if hash is present see if its valid, in the tab_priority array
			if ( hash ) {
				for ( i = 0; i < ii; i += 1 ) {
					if ( menu_ids[i] === hash ) {
						tab_to_open = hash;
						break;
					}
				}
			}
		eu.europeana.tabs.explore.toggleTab( tab_to_open );
	},
	
	
	tabFeedback : function( id ) {
		
		if ( !eu.europeana.fulldoc.similar && '#similar-content' === id ) {
			if ( eu.europeana.carousel.similar ) {
				eu.europeana.carousel.similar.init();
				eu.europeana.fulldoc.similar = true;
			}
		}
		
		if ( !eu.europeana.fulldoc.related && '#related-content' === id ) {
			if ( eu.europeana.carousel.related ) {
				eu.europeana.carousel.related.init();
				eu.europeana.fulldoc.related = true;
			}
		}
		
	},
	
	
	addCarousels : function() {

		eu.europeana.carousel = {};
		
		if ( jQuery('#similar-content').length > 0 ) {
			
			eu.europeana.carousel.similar = eu.europeana.fulldoc.createCarousel('#similar-content');
			
		}
		
		if ( jQuery('#related-content').length > 0 ) {
			
			eu.europeana.carousel.related = eu.europeana.fulldoc.createCarousel('#related-content');
			
		}

	},
	
	createCarousel : function( id ) {
		
		return new com.gmtplusone.carousel(
			id,
			{ animation :
				{
					arrow_keys_active : false,
					hover_title : false 
				}
			}
		);
		
	},
	
	
	adjustDescription : function() {
		
		
		var description_truncate = new com.gmtplusone.truncate(
			'#item-description',
			{
				toggle_html : {
					more : eu.europeana.vars.msg.more,
					less : eu.europeana.vars.msg.less,
					more_class : 'more toggle-menu-icon',
					less_class : 'less toggle-menu-icon active'
				}
			}
		);
		
		description_truncate.init();
		
		var subject_truncate = new com.gmtplusone.truncate(
				'#item-subject',
				{
					toggle_html : {
						more : eu.europeana.vars.msg.more,
						less : eu.europeana.vars.msg.less,
						more_class : 'more toggle-menu-icon',
						less_class : 'less toggle-menu-icon active'
					}
				}
			);
		
		subject_truncate.init();

		
		var rights_truncate = new com.gmtplusone.truncate(
				'.item-moreless',
				{
					toggle_html : {
						more : eu.europeana.vars.msg.more,
						less : eu.europeana.vars.msg.less,
						more_class : 'more toggle-menu-icon',
						less_class : 'less toggle-menu-icon active'
					}
				}
		);
		
		// callback sent to truncate content to be run after truncation is complete
		rights_truncate.init(eu.europeana.fulldoc.openTab);
	},
	
	
	handleSaveTagSubmit : function( e ) {
		e.preventDefault();
		if ( jQuery('#item-tag').val() < 1 ) { return; }
		var ajax_feedback = {
			saved_tags_count : 0,
			$saved_tags : jQuery('#saved-tags-count'),
			success : function() {
				var html =
					'<span id="save-tag-feedback">' +
						eu.europeana.vars.msg.saved_tag +
					'</span>';
				eu.europeana.ajax.methods.addFeedbackContent( html );
				eu.europeana.ajax.methods.showFeedbackContainer();
				ajax_feedback.saved_tags_count = parseInt( ajax_feedback.$saved_tags.html(), 10 );
				ajax_feedback.$saved_tags.html( ajax_feedback.saved_tags_count + 1 );
			},
			failure : function() {
				var html =
					'<span id="save-tag-feedback" class="error">' +
						eu.europeana.vars.msg.save_tag_failed +
					'</span>';
				eu.europeana.ajax.methods.addFeedbackContent( html );
				eu.europeana.ajax.methods.showFeedbackContainer();
			}
		},
		ajax_data = {
			className : "SocialTag",
			tag : encodeURIComponent( jQuery('#item-tag').val() ),
			europeanaUri : eu.europeana.vars.item.uri
		};
		eu.europeana.ajax.methods.user_panel( 'save', ajax_data, ajax_feedback );
	},
	
	handleSaveItemClick : function( e ) {
		e.preventDefault();
		var ajax_feedback = {
			saved_items_count : 0,
			$saved_items : jQuery('#saved-items-count'),
			success : function() {
				var html =
					'<span id="save-item-feedback">' +
						eu.europeana.vars.msg.saved_item +
					'</span>';
				eu.europeana.ajax.methods.addFeedbackContent( html );
				eu.europeana.ajax.methods.showFeedbackContainer();
				ajax_feedback.saved_items_count = parseInt( ajax_feedback.$saved_items.html(), 10 );
				ajax_feedback.$saved_items.html( ajax_feedback.saved_items_count + 1 );
			},
			failure : function() {
				var html =
					'<span id="save-item-feedback" class="error">' +
						eu.europeana.vars.msg.save_item_failed +
					'</span>';
				eu.europeana.ajax.methods.addFeedbackContent( html );
				eu.europeana.ajax.methods.showFeedbackContainer();
			}
		},
		ajax_data = {
			className : "SavedItem",
			europeanaUri : eu.europeana.vars.item.uri
		};
		eu.europeana.ajax.methods.user_panel( 'save', ajax_data, ajax_feedback );
	},
	
	
	addAutoTagHandler : function() {
		
		var self = this;
		
		jQuery('#fields-enrichment h3 a, #fields-enrichment h4 a').each(function( key, value ) {
			
			jQuery(value).bind('click', self.handleAutoTagClick );
			
		});
		
	},
	
	handleAutoTagClick : function( e ) {
		
		e.preventDefault();
		var $elm = jQuery(this);
		$elm.parent().next().slideToggle();
		
		if ( $elm.hasClass('active') ) {
			
			$elm.removeClass('active');
			
		} else {
			
			$elm.addClass('active');
			
		}
		
	},
	
	addThis : function() {
		var url = jQuery('head link[rel="canonical"]').attr('href'),
			title = jQuery('head title').html(),
			description = jQuery('head meta[name="description"]').attr('content');
		window.addthis_config = com.addthis.createConfigObject({
			pubid : eu.europeana.vars.addthis_pubid,
			ui_language: 'en', // eu.europeana.vars.locale,
			data_ga_property: eu.europeana.vars.gaId,
			data_ga_social : true,
			data_track_clickback: true,
			ui_use_css : true
		});
		
		// nb: tweet does not accept twitter templates, it only accepts html attributes
		// @see /js/com/addthis/addthis.js for those attributes		
		//jQuery('#header-strip').append(
		jQuery('#additional-actions-addthis').append(
				com.addthis.getToolboxHtml({
				html_class : 'addthis',
				url : url,
				title : title,
				description : description,
				services : {
					compact : {},
					tweet : { count : 'vertical' },
					google_plusone : { count : 'true', size: 'tall' },
					facebook_like : { layout : 'box_count' }
				}
			})
		);

		jQuery('#lightbox-addthis').append(
				com.addthis.getToolboxHtml({

					html_class : 'addthis',
					url : url,
					title : title,
					description : description,
					services : {
						compact : {},
						twitter : {},
						google_plusone : { count : 'false' },
						facebook_like : {}
					}
				})
		);

		jQuery('.addthis').hide();
		com.addthis.init( null, true, false );
		setTimeout( function() { jQuery('.addthis').fadeIn(); }, 600 );	
	}
};

eu.europeana.fulldoc.init();
