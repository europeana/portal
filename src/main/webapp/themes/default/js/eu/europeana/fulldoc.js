js.utils.registerNamespace( 'eu.europeana.fulldoc' );

eu.europeana.fulldoc = {

	// provides priority order for which tab to open when no hash is given
	// provides a list of accepted hash values for validation
	tab_priority : [ '#related-items','#similar-content','#map-view' ],
	
	more_icon_class : "icon-arrow-6-right",
	
	less_icon_class : "icon-arrow-7-right",

	init : function() {

		this.loadComponents();
		this.addAutoTagHandler();
		
		$('#item-save-tag')	.bind('submit', this.handleSaveTagSubmit );
		$('#item-embed')	.bind('click', this.handleEmbedClick );
		
		$('#urlRefIsShownAt, #urlRefIsShownBy').bind('click',
			function(e){
				eu.europeana.fulldoc.triggerPanel.data['type'] = 'link';
				eu.europeana.fulldoc.trackedClick('link');
			}
		);

		if( $('#item-save').hasClass('icon-unsaveditem') ){
			$('#item-save').bind('click', this.handleSaveItemClick );			
		}
		else{
			$('#item-save').css('cursor', 'default');
		}
		
		js.console.log(JSON.stringify(carouselData));
	},

	trackedClick : function(src){		/* src = img, magnify or link; */
		if(src == 'img'){
			if(typeof eu.europeana.fulldoc.triggerPanel != 'undefined' && eu.europeana.fulldoc.triggerPanel.is(":visible")){
				eu.europeana.fulldoc.triggerPanel.find('.label').click();		/* will recurse back to here with src 'magnify' */				
			}
		}
		else if(src == 'broken-img'){
			com.google.analytics.trackEvent("Europeana Portal", 'Europeana Lightbox', 'External (' + src + ')');
			window.open(carouselData[0].external.url, '_new');
		}
		else{
			var clickSrc	= src == 'magnify' ? 'image' : eu.europeana.fulldoc.triggerPanel.data['type'];
			var action		= src == 'link' ? 'Europeana Redirect' : (eu.europeana.fulldoc.triggerPanel.data['type']  == 'image' ? 'Europeana Lightbox' : 'Europeana Redirect');
			com.google.analytics.trackEvent("Europeana Portal", action, 'External (' + clickSrc + ')');
		}
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
		
		js.loader.loadScripts([{
			file: 'window-open' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/js/' + js.min_directory
		}]);
		
		js.loader.loadScripts([{
			name : 'accordion-tabs',
			file : 'accordion-tabs' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory
		}]);
		
		js.loader.loadScripts([{
			file : 'citation' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'accordion-tabs' ],
			callback: function(){
				eu.europeana.citation.init();
			}
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
						$("#translate-item").trigger('click');
						$("#translate-item").unbind('click');
						$("#translate-item").bind('click', function(e){e.preventDefault();});
						$("#translate-item").addClass('disabled');
						$("#translate-item span")
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
			dependencies : [ 'accordion-tabs' ],
			callback : function() {
				self.initCarousels();	
			}
		}]);
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

	handleSaveTagSubmit : function( e ) {
		e.preventDefault();
		if ( jQuery('#item-tag').val() < 1 ){
			return;
		}

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
			$itemSave : $('#item-save'),
			$saved_items : $('#saved-items-count'),
			success : function() {
				var html =
					'<span id="save-item-feedback">' +
						eu.europeana.vars.msg.saved_item +
					'</span>';
				eu.europeana.ajax.methods.addFeedbackContent( html );
				eu.europeana.ajax.methods.showFeedbackContainer();
				ajax_feedback.saved_items_count = parseInt( ajax_feedback.$saved_items.html(), 10 );
				ajax_feedback.$saved_items.html( ajax_feedback.saved_items_count + 1 );
				ajax_feedback.$itemSave.removeClass("icon-unsaveditem");
				ajax_feedback.$itemSave.addClass("icon-saveditem");
				ajax_feedback.$itemSave.find('.action-title').html(eu.europeana.vars.msg.saved_item);
				ajax_feedback.$itemSave.css('cursor', 'default');
				ajax_feedback.$itemSave.unbind('click');
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
				ui_use_css : true,
				ui_click: true		// disable hover
			});
		
		// nb: tweet does not accept twitter templates, it only accepts html attributes
		// @see /js/com/addthis/addthis.js for those attributes
		
		var addThisHtml = com.addthis.getToolboxHtml({
			html_class : '',
			url : url,
			title : title,
			description : description,
			services : {
				compact : {}
			},
			link_html : $('.shares-link').html()
		
		});

		$('.shares-link').html(
			addThisHtml
		);
		
		$('.shares-link').hide();
		com.addthis.init( null, true, false );
		
		setTimeout( 
			function(){
				jQuery('.shares-link').fadeIn();
			},
			600
		);
		
	},


	initLightbox : function(url){
		js.console.log("initLightbox");

		var NavOb = function(){
			var nav = function(direction){
				var currentUrl = $("#lightbox_image").attr("src");
				var submodel = [];
				var submodelActive = 0;
				
				for(var i=0; i<carouselData.length; i++){
					if(carouselData[i].external && carouselData[i].external.type == "image"){
						if(carouselData[i].external.url == currentUrl){
							submodelActive = submodel.length;
							js.console.log("active for lightbox: " + i);
						}
						submodel[submodel.length] = carouselData[i].external;
					}
				}

				var newActive = submodelActive + direction;
				if(newActive<0){
					newActive = submodel.length -1;
				}
				else if(newActive >= submodel.length){
					newActive = 0;						
				}

				js.console.log("submodel is now " + JSON.stringify(submodel)  );
				
				$("#hidden_img").unbind( '.imagesLoaded' );
				$("#hidden_img").remove();
				
				$('<div id="hidden_img" style="visibility:hidden;"><img src="' + submodel[newActive].url + '" /></div>').appendTo('#lightbox_image').imagesLoaded(
					function(){
						var zoomed = false;
						if($("#zoomedImg").is(":visible")){
							eu.europeana.lightbox.closeZoom();
							zoomed = true;
						}
						
						$("#lightbox_image").attr("src", submodel[newActive].url);
						eu.europeana.lightbox.layout();
						if(zoomed){
							eu.europeana.lightbox.zoomImg();
						}
					}
				);
			};
			this.prev = function(){
				nav(-1);
			};
			this.next = function(){
				nav(1);
			};
		};
		
		eu.europeana.lightbox.init(url, eu.europeana.fulldoc.getLightboxableCount() > 1 ? new NavOb() : null);
	
		$('#lightbox_image').attr('src', url);
		
	},
	
	
	
	initTriggerPanel: function(type, index, gallery){
		
		
		js.console.log("initTriggerPanel type = " + type);
		
		if($("#mobile-menu").is(":visible") ){
			return;
		}
		
		js.console.log('initTriggerPanel type= ' + type + ", index = " + index);
		
		if(typeof(eu.europeana.fulldoc.triggerPanel)=="undefined"){
			eu.europeana.fulldoc.triggerPanel = $('<div class="lb-trigger" >'
					+ '<span rel="#lightbox" title="'
					+ '" class="icon-magplus label">'
					+ '</span>'
					+ '</div>'
			).appendTo($('#carousel-1-img-measure'));
			eu.europeana.fulldoc.triggerPanel.hide();
		}
		
		eu.europeana.fulldoc.triggerPanel.data['type'] = type; /* used for google analytics category */
		
		var triggerSpan = eu.europeana.fulldoc.triggerPanel.find('.label');
		triggerSpan.attr('title', eu.europeana.vars.external.triggers.labels[type]);
		triggerSpan.html(eu.europeana.vars.external.triggers.labels[type]);
		
		// action handling
		
		eu.europeana.fulldoc.triggerPanel.unbind('click');
		triggerSpan.unbind('click');
		triggerSpan.removeData('overlay');

		$(eu.europeana.fulldoc.triggerPanel).bind('click', function(){
			eu.europeana.fulldoc.trackedClick('magnify', type);
		});

		
		if(carouselData[index ? index : 0].external.type == 'image'){
			
			/* if the image is wider than 200 px initialise the lightbox and show the trigger panel,
			 * if not bind the image to open the mapped url in a seaparate window
			 */
			$('<img src="'+ carouselData[index ? index : 0].external.url + '" style="visibility:hidden"/>')
					.appendTo('body').imagesLoaded(
							
						function($images, $proper, $broken){
						
							if($proper.length==1 && $proper.width() > 200){
								eu.europeana.fulldoc.loadLightboxJS(
										function(){
											eu.europeana.fulldoc.initLightbox(carouselData[index ? index : 0].external.url);
											eu.europeana.fulldoc.showExternalTrigger(true, carouselData[index ? index : 0].external.type, gallery);
										}
								);
							}
							else{
								js.console.log("lightbox test failed: " + ($proper.length==1 ? "image was too small (" + $proper.width() + ")" : "image didn't load (url: " + carouselData[index ? index : 0].external.url + ")"));
								
								// if the lightbox test fails then attach a click handler to the image
								$('#carousel-1-img-measure img').css('cursor', 'pointer');
								$('#carousel-1-img-measure img').click(
//										{param:'broken-img'},
										function(){
											eu.europeana.fulldoc.triggerPanel.data['type'] = 'broken-img';
											eu.europeana.fulldoc.trackedClick('broken-img');
										}
								);

								
							}
							$(this).remove();
						}
						
			);
		}
		else{
			eu.europeana.fulldoc.triggerPanel.bind('click', function(){
				
				var type = carouselData[index ? index : 0].external.type; 
				if(type == 'pdf'){
        			window.open(carouselData[index ? index : 0].external.url, '_new');
        		}
				else if(type == 'sound'){
					window.open(carouselData[index ? index : 0].external.url, '_new');
				}
				else if(type == 'video'){
					window.open(carouselData[index ? index : 0].external.url, '_new');
				}
				else if(type == 'text'){
					window.open(carouselData[index ? index : 0].external.url, '_new');
				}
			});
			eu.europeana.fulldoc.showExternalTrigger(true, carouselData[index ? index : 0].external.type, gallery);
		}
	},
	
	
	/**
	 * 
	 * @gallery galleria instance
	 * @show true / false - false if we're changing the trigger from within a carousel
	 * 
	 * */
	showExternalTrigger : function(show, type, gallery){
		if(show){
			var marginTrigger = 0;
			
			if(gallery){				
				marginTrigger = ( $("#carousel-1").width() - $(gallery.getActiveImage()).width() ) / 2;
				
				eu.europeana.fulldoc.triggerPanel.css("bottom", "-0.75em"); 
			}
			else{
				marginTrigger = ( $("#carousel-1-img-measure").width() - $("#carousel-1-img-measure img").width() ) / 2;
			}
			eu.europeana.fulldoc.triggerPanel.css("margin-left", marginTrigger + "px");
			eu.europeana.fulldoc.triggerPanel.fadeIn(500);
			$('#carousel-1-img-measure img').css('cursor', 'pointer');
		}
		else{
			eu.europeana.fulldoc.triggerPanel.css('display', 'none');
			$('#carousel-1-img-measure img').css('cursor', 'default');
		}
	},
	
	loadLightboxJS : function(callbackLoad){

		js.loader.loadScripts([{
			name: 'jquery-tools',
			file : 'jquery.tools.min.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/jquery/min/',
			dependencies : [ 'jquery' ]
		}]);

		js.loader.loadScripts([{
				name : 'touchswipe',
				file : 'touch-swipe.min.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/jquery/',
				callback: function(){ alert('loaded touch swipe'); }
		}]);		  
		  
		if(!window.showingPhone()){
			
			var lightboxJsFile = 'fulldoc-lightbox' + js.min_suffix + '.js' + js.cache_helper;
			
			if( js.loader.loader_status[lightboxJsFile]){
				if(callbackLoad){
					callbackLoad();					
				}
				return;
			}
			
			js.loader.loadScripts([{
				file : lightboxJsFile,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies :  $("html").hasClass('ie8') ? [ 'jquery-tools'] : [ 'jquery-tools', 'touchswipe'],
				
				callback: function(){
					$(window).on("resize", function(){
						if(eu.europeana.lightbox.layout){
							eu.europeana.lightbox.layout();												
						}
					});
					if(callbackLoad){
						callbackLoad();
					}
				}
			}]);									
		}
	},
	
	
	initTopCarousel : function(){

		jQuery('#carousel-1').css("height", eu.europeana.fulldoc.getCarousel1Height() + "px");	// set height to max height that will be needed
			
		eu.europeana.fulldoc.carousel1 = Galleria.run('#carousel-1', {
			debug:				js.debug,
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
			fullscreenDoubleTap:	false,
			thumbnails: 		carouselData.length>1,
			max_scale_ratio:	1,					// prevent stretching (does this work?  no reference to this variable in galleria that I can find) 
			extend: function(e){
				
				var doEllipsis = function(){
					var ellipsisObjects = [];
					jQuery('#carousel-1 .europeana-carousel-info').each(
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
				
	
				this.bind("image", function(e) {	// lightbox trigger
					var gallery = this;
					js.console.log("galleria lightbox trigger updating disabled: do this in the navOb");
					
					// update trigger ( and show lightboxable )
					// OR hide trigger	
					
					var external = gallery._options.dataSource[e.index].external;
					
					eu.europeana.fulldoc.initTriggerPanel(external.type, e.index, gallery);
					
				}); // end bind image
			
			} // end extend
		
		}); // end Galleria.run
	},
	
	initBottomCarousel : function(){
		
		if(typeof carousel2Data != 'undefined'){
			
			var carousel2selector = $('#carousel-2-tabbed').is(":visible") ? '#carousel-2-tabbed' : '#carousel-2'; 
			
			// 150 too small for iphone: make min height 200
   			$(carousel2selector).css("height", Math.max(200, eu.europeana.fulldoc.getCarousel2Dimensions().h) + "px");
   	
   			var x = Galleria.run(carousel2selector, {
   				debug:			js.debug,
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
				thumbnails: 	true,
				extend: function(e){
					var doEllipsis = function(){
						var ellipsisObjects = [];
						$(carousel2selector + ' .europeana-carousel-info').each(
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
				}
	   		});
   			
		}
	},
	
	getLightboxableCount:function(){
		var lightboxableCount = 0;
		for(var i=0; i<carouselData.length; i++){
			if(carouselData[i].external && carouselData[i].external.type == 'image'){
				lightboxableCount++;
			}
		}
		return lightboxableCount;
	},
	
	
	initCarousels: function(){
		
		
		Galleria.loadTheme(eu.europeana.vars.branding + '/js/galleria/themes/europeanax/' + js.min_directory + 'galleria.europeanax'  + js.min_suffix + '.js');
			
		$("#carousel-1-img-measure img").imagesLoaded( function($images, $proper, $broken){

			// measurement broken if img doesn't load but alt text is present
			$("#carousel-1-img-measure img").each(function(i, ob){
				$(ob).data.alt = $(ob).attr("alt");
				$(ob).attr("alt", "");
			});
			
			js.console.log("measured carousel 1 images: div width is " + $("#carousel-1-img-measure").width() );
			
			// this is where we go when carousel test images don't load
			var initNoCarousel = function(){
				
				// show either the thumbnail or the alt text
				
				$("#carousel-1-img-measure img").removeClass("no-show");
				$("#carousel-1-img-measure").css("position",	"relative");
				$("#carousel-1-img-measure").css("text-align",	"center");
				$("#additional-info").css("padding-top", "1em");
				
				// if the thumbnail loaded then show it, otherwise restore the alt text (but prevent it from breaking the layout)
				if($("#carousel-1-img-measure").width()>0){
					if(carouselData[0].external){
						eu.europeana.fulldoc.lightboxable = carouselData[0].external;
						eu.europeana.fulldoc.initTriggerPanel( carouselData[0].external.type);
					}	
				}
				else{
					$('#carousel-1-img-measure').css('white-space',		'normal');
					$('#carousel-1-img-measure').css('word-break',		'break-all');
					$("#carousel-1-img-measure img").each(function(i, ob){
						$(ob).attr("alt", $(ob).data.alt);
					});
				}
				
				// img trigger bind
				$('#carousel-1-img-measure img').bind('click',
					function(){
						eu.europeana.fulldoc.trackedClick('img');
					}
				);
			};
			
			// Run carousel test and init if successful
			if(typeof carouselTest == 'object'){
				
				var testHtml = '';
				
				$(carouselTest).each(function(i, ob){
					testHtml += '<img src="' + carouselTest[i].src + '" style="display:none"/>';
				});
								
				$(testHtml).appendTo('body').imagesLoaded(
					function($images, $proper, $broken){
						if($proper.length==carouselTest.length){
							js.console.log("carousel test passed: src was " +  $($proper[0]).attr("src") );
							eu.europeana.fulldoc.getCarousel1Height = function(){
								var tallestImageH = $("#carousel-1-img-measure").height();
								var galleriaOffsetY	= 70;	// thumbnail + thumbnail margin bottom (NOTE: linked to .galleria-stage in galleria theme)
								if( window.showingPhone() ){
									galleriaOffsetY	= 120;	
								}
								return tallestImageH + galleriaOffsetY;
							};
							eu.europeana.fulldoc.initTopCarousel();
							$('#carousel-1 .galleria-stage .galleria-image img').live('click', 
								function(){
									eu.europeana.fulldoc.trackedClick('img');
								}
							);		
						}
						else{
							var msgFailed = "(" + $broken.length + " broke, " + $proper.length + " succeeded)";
							for(var i=0; i<$broken.length; i++){
								msgFailed += "\n  broke: " + $($broken[0]).attr("src")
							}
							for(var i=0; i<$proper.length; i++){
								msgFailed += "\n  proper: " + $($proper[0]).attr("src")
							}

							js.console.log("carousel test failed: " + msgFailed);
							initNoCarousel();
						}
					}
				);
			}
			else{
				js.console.log("no carousel test to run");
				initNoCarousel();
			}
		});

		
		$("#carousel-2-img-measure img").imagesLoaded( function(){

			eu.europeana.fulldoc.getCarousel2Dimensions = function(){

				$("#carousel-2-img-measure img").css("display", "inline-block");
  				var tallestImageH = $("#carousel-2-img-measure").height();
  				
  				$("#carousel-2-img-measure img").css("display", "block");  				
  				var widestImageW = $("#carousel-2-img-measure").width();
  				
  				$("#carousel-2-img-measure img").css("display", "none");
  				
  				return {w:widestImageW, h:tallestImageH};
			};
			$("#carousel-2-img-measure img").css("display", "none");
	
			eu.europeana.fulldoc.bottomTabs =  new AccordionTabs( $('#explore-further'),
				function(){
					eu.europeana.fulldoc.initBottomCarousel();
				}
			);

		});

	},
	
};

eu.europeana.fulldoc.init();
