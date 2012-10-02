js.utils.registerNamespace( 'eu.europeana.fulldoc' );

eu.europeana.fulldoc = {

	// provides priority order for which tab to open when no hash is given
	// provides a list of accepted hash values for validation
	tab_priority : [ '#related-items','#similar-content','#map-view' ],
	
	more_icon_class:"icon-arrow-6-right",
	
	less_icon_class:"icon-arrow-7-right",

	init : function() {

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
				function(){
					if(! $("#mobile-menu").is(":visible") ){
						jQuery("#translate-item").trigger('click');
						jQuery("#translate-item").unbind('click');
						jQuery("#translate-item").bind('click', function(e){e.preventDefault();});
						jQuery("#translate-item").addClass('disabled');
						jQuery("#translate-item span")
						.removeClass(eu.europeana.translation_services.more_icon_class)
						.removeClass(eu.europeana.translation_services.less_icon_class)
						.removeClass(eu.europeana.translation_services.more_icon_class_phone);
					}
				}
			);}
		}]);
		
		js.loader.loadScripts([{
			file : 'jquery.imagesloaded.min.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/jquery/' + js.min_directory,
			callback : function() {
				self.initCarousels();
			}
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
		if($('#explore-further').length==0){
			return;
		}
		eu.europeana.tabs = {};
		eu.europeana.tabs.explore = new com.gmtplusone.tabs(
			'#explore-further',
			{ callbacks : { opened : eu.europeana.fulldoc.tabFeedback }	}
		);
		eu.europeana.tabs.explore.init( eu.europeana.fulldoc.addCarousels );
	},
	
	openTab : function() {
		if(typeof eu.europeana.tabs == "undefined"){
			return;
		}
		
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
		/*
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
		*/
	},
	
	/*
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
	*/
	
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
			jQuery(value).addClass(eu.europeana.fulldoc.more_icon_class);
		});
		
	},
	
	handleAutoTagClick : function( e ) {
		
		e.preventDefault();
		var $elm = jQuery(this);
		
		$elm.parent().next().slideToggle();
		//$elm = $elm.find('.icon');
		
		if ( $elm.hasClass(eu.europeana.fulldoc.more_icon_class) ) {
			
			$elm.removeClass(eu.europeana.fulldoc.more_icon_class);
			$elm.addClass(eu.europeana.fulldoc.less_icon_class);
		} else {
			
			$elm.removeClass(eu.europeana.fulldoc.less_icon_class);
			$elm.addClass(eu.europeana.fulldoc.more_icon_class);
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
				ui_use_css : true});
		
		// nb: tweet does not accept twitter templates, it only accepts html attributes
		// @see /js/com/addthis/addthis.js for those attributes
		
		var addThisHtml = com.addthis.getToolboxHtml_ANDY({
			html_class : '',
			url : url,
			title : title,
			description : description,
			services : {
				compact : {}
			},
			link_html : $('#shares-link').html()
		
		});

		jQuery('#shares-link').html(
			addThisHtml
		);
		
		
		jQuery('#shares-link').hide();
		com.addthis.init( null, true, false );
		
		setTimeout( function() {
			jQuery('#shares-link').fadeIn(function(){
				$(this).css("display", "inline-block");
			}); },
			600 );
		
		/*

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
		 */
		
	},
	
	
	initTopCarousel : function(){
		
		// mock some lightbox data
		//
		var lightboxImages = [
		    'http://godsofart.com/wp-content/uploads/2012/01/Star_Wars_Tales_by_UdonCrew.jpg',
			'http://garytymon.files.wordpress.com/2011/05/starwars_art_vader-thumb-500x368-16957.jpg',
			'http://0.tqn.com/d/animatedtv/1/0/T/x/fGuy_BlueHarvest_sc459_0034f.jpg',
			'http://garytymon.files.wordpress.com/2011/05/02.jpg',
			'http://3.bp.blogspot.com/-BwELLLI2HuQ/TjzWsDBc-EI/AAAAAAAAAeM/p5_LAtteZws/s1600/millenium-falcon.jpg',
			'http://1.bp.blogspot.com/-Em5hSysjkFo/TsVKcZ-uHZI/AAAAAAAAETQ/YcRPI26OiLo/s1600/111711d.jpg'
		];

		var lightboxImgCount = 0;

		for(var i=0; i<carouselData.length; i++){
			if(i%2==0){   	   					
				carouselData[i].lightboxable = {
						type	: 'image',
						url		: lightboxImages[lightboxImgCount]
				};
				lightboxImgCount ++;
			}
		}
		
		// end mock lightbox data

		jQuery('#carousel-1').css("height", eu.europeana.fulldoc.getCarousel1Height() + "px");	// set height to max height that will be needed
			
		thisGallery = Galleria.run('#carousel-1', {
			transition:			'fadeslide',
			carousel:			true,
			carouselSpeed:		1200,
			carouselSteps:		1,
			easing:				'galleriaOut',
			imageCrop:			false,
			imagePan:			false,
			lightbox:			false,
			responsive:			true,
			dataSource:			carouselData,
			thumbnails: 		carouselData.length>1,
			max_scale_ratio:	1,					// prevent stretching (does this work?  no reference to this variable in galleria that I can find) 
			extend: function(e){
			
				//var triggerHeight	= 40;
				
				var doEllipsis = function(){
					var ellipsisObjects = [];
					jQuery('.europeana-carousel-info').each(
						function(i, ob){
							ellipsisObjects[ellipsisObjects.length] = new Ellipsis($(ob));					
						}
					);
					$(window).bind('resize', function(){
						if(eu.europeana.vars.suppresResize){
							return;
						}
						for(var i=0; i<ellipsisObjects.length; i++ ){
							ellipsisObjects[i].respond();
						}
					});
				};
	
				$(this).ready(function(e) {
					setTimeout(doEllipsis, 1000);
				});
				
	
				var jsLoaded		= false;
				var triggerPanel	= null;
	
				
				var initLightbox = function(url, gallery){
					$('#lightbox_image').one('load', function(){

						var navOb = function(){
							
							var nav = function(direction){
								var currentUrl = $("#lightbox_image").attr("src");
								var submodel = [];
								var submodelActive = 0;
								
								for(var i=0; i<carouselData.length; i++){
									if(carouselData[i].lightboxable){
										if(carouselData[i].lightboxable.url == currentUrl){
											submodelActive = submodel.length;
										}
										submodel[submodel.length] = carouselData[i].lightboxable;
									}
								}
								
								var newActive = submodelActive + direction;
								if(newActive<0){
									newActive = submodel.length -1;
								}
								else if(newActive >= submodel.length){
									newActive = 0;						
								}
								
								$("#hidden_img").unbind( '.imagesLoaded' );
								$("#hidden_img").remove();
								$('<div id="hidden_img" style="visibility:hidden;"><img src="' + submodel[newActive].url + '" /></div>').appendTo('#lightbox_image').imagesLoaded(
									function(){
										$("#lightbox_image").attr("src", submodel[newActive].url);
										eu.europeana.lightbox.layout();
									}
								);
							}
							this.prev = function(){
								nav(-1);
							}
							this.next = function(){
								nav(1);
							}
						};
						
						var navOb = new navOb();
						
						eu.europeana.lightbox.init(url, navOb);
						showTrigger(true, gallery);
					});
					
					$('#lightbox_image').attr('src', url);
				};
				
				// centre thumbnails
				var verticallyAlignImageNav = function(gallery){
					
					var imageNav	= gallery.$('container').find('.galleria-image-nav');
					
					imageNav.css("margin-top", "0px");
					imageNav.css("top", ((heightDiff/2) + stageTopMargin) + "px");
					
					var stage		= gallery.$('container').find('.galleria-stage');
					var imageNavR	= gallery.$('container').find('.galleria-image-nav-right');
					var heightDiff	= stage.height() - imageNavR.height();
					var stageTopMargin	= parseInt(gallery.$('container').find('.galleria-stage').css("margin-top")  );
					
					imageNav.css("top", ((heightDiff/2) + stageTopMargin) + "px");
				};		
				
				// Show or hide the trigger - resizing relevant elements
				var showTrigger = function(show, gallery){
					if(show){
						$('.lb-trigger').fadeIn(500);
					}
					else{
						$('.lb-trigger').css('display', 'none');
					}
					verticallyAlignImageNav(gallery);
				};
	
				this.bind("image", function(e) {	// lightbox trigger
					var gallery = this;
					var lightboxable = gallery._options.dataSource[e.index].lightboxable;
					if(lightboxable && lightboxable.url.length>0){
						if(!triggerPanel){
							this.$( 'container' ).find('.galleria-images').after(
									'<div class="lb-trigger" >'
									+ '<span rel="#lightbox" title="' + triggerLabels[lightboxable.type] + '" class="icon-magplus">' + triggerLabels[lightboxable.type] + '</span>'
									+ '</div>'
							);
							triggerPanel = this.$( 'container' ).find('.lb-trigger');
							triggerPanel.hide();
							
							var loadJS = function(){
								
								js.loader.loadScripts([{
									name: 'jquery-tools',
									file : 'jquery.tools.min.js' + js.cache_helper,
									path : eu.europeana.vars.branding + '/js/jquery/',
									dependencies : [ 'jquery' ]
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
										initLightbox(lightboxable.url, gallery);
										//initLightbox(lightboxable.url);
										jsLoaded = true;
										
										$(window).on("resize", function(){
											if(eu.europeana.lightbox.layout){
												eu.europeana.lightbox.layout();												
											}
										});
										
										w.addEventListener( "orientationchange",
											function(){
												if(eu.europeana.lightbox.layout){
													eu.europeana.lightbox.layout();												
												}
											},
										false);


									}
								}]);
							};
							loadJS();
						}
						else{
							initLightbox(lightboxable.url, gallery);
							//showTrigger(true, gallery);
						}
					}
					else{
						showTrigger(false, gallery);
					}
	
				}); // end bind image
			
			} // end extend
		
		}); // end Galleria.run
	},
	
	initBottomCarousel : function(){
		if(typeof carousel2Data != 'undefined'){
			
			// 150 too small for iphone: make min height 200
   			jQuery('#carousel-2').css("height", Math.max(200, eu.europeana.fulldoc.getCarousel2Dimensions().h) + "px");
   			
   			Galleria.run('#carousel-2', {
					transition:		'fadeslide',
					carousel:		true,
					carouselSpeed:	1200,
					carouselSteps:	1,
					easing:			'galleriaOut',
					imageCrop:		false,
					imagePan:		false,
					lightbox:		true,
					responsive:		true,
					dataSource:		carousel2Data,
					thumbnails: 	true
	   		});
	   		   				
		}
	},
	
	
	initCarousels: function(){

		Galleria.loadTheme(eu.europeana.vars.branding + '/js/galleria/themes/europeanax/galleria.europeanax.js');

		$("#carousel-1-img-measure img").imagesLoaded( function(){
			
			eu.europeana.fulldoc.getCarousel1Height = function(){

  				var tallestImageH = $("#carousel-1-img-measure").height();

  				var galleriaOffsetY	= 70;	// thumbnail + thumbnail margin bottom (NOTE: linked to .galleria-stage in galleria theme)
  				if( window.showingPhone() ){
  					galleriaOffsetY	= 120;	
  				}
  				
  				return tallestImageH + galleriaOffsetY;
			};
			
			eu.europeana.fulldoc.initTopCarousel();
		});
		
		
		$("#carousel-2-img-measure img").imagesLoaded( function(){
			
			eu.europeana.fulldoc.getCarousel2Dimensions = function(){

				$("#carousel-2-img-measure img").css("display", "inline-block");
  				var tallestImageH = $("#carousel-2-img-measure").height();
  				
  				$("#carousel-2-img-measure img").css("display", "block");  				
  				var widestImageW = $("#carousel-2-img-measure").width();
  				
  				console.log("getCarousel2Dimensions returns {w:" + widestImageW + ", h:" + tallestImageH + "}");
  				
  				return {w:widestImageW, h:tallestImageH};
			};
			
			eu.europeana.fulldoc.initBottomCarousel();
		});
		
		
	},
	
};

eu.europeana.fulldoc.init();
