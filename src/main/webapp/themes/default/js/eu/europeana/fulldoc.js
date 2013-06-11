


js.utils.registerNamespace( 'eu.europeana.fulldoc' );

eu.europeana.fulldoc = {

	lightboxOb :	null,
/*	
	// provides priority order for which tab to open when no hash is given
	// provides a list of accepted hash values for validation
	//tab_priority : [ '#related-items','#similar-content','#map-view' ],
*/	
	more_icon_class : "icon-arrow-6-right",
	
	less_icon_class : "icon-arrow-7-right",

	setupAnalytics : false,
	
	init : function() {

		this.loadComponents();
		this.addAutoTagHandler();
		
		$('.sidebar-right a').each(function(i, ob){			
			js.utils.fixSearchRowLinks($(ob));
		});
		
		js.utils.fixSearchRowLinks($('#navigation a').first());
		
		
		$('#item-save-tag')	.bind('submit', this.handleSaveTagSubmit );
		$('#item-embed')	.bind('click', this.handleEmbedClick );
		
		$('#urlRefIsShownAt, #urlRefIsShownBy').bind('click',
			function(e){
				com.google.analytics.europeanaEventTrack("Europeana Portal", "Europeana Redirect", "External (link)");
			}
		);

		if( $('#item-save').hasClass('icon-unsaveditem') ){
			$('#item-save').bind('click', this.handleSaveItemClick );			
		}
		else{
			$('#item-save').css('cursor', 'default');
		}
		
		// analytics
		
		$('.sidebar-right a').click(function(e){
			com.google.analytics.europeanaEventTrack("Click-link " + $(e.target).attr('href'), "Search-Also-For");
		});
		
		js.console.log(JSON.stringify(carouselData));
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
		self.addThis();
		
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
			url : $(this).attr('href'),
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
		if ( $('#item-tag').val() < 1 ){
			return;
		}

		var ajax_feedback = {
			saved_tags_count : 0,
			$saved_tags : $('#saved-tags-count'),
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
			tag : encodeURIComponent( $('#item-tag').val() ),
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
		
		$('#fields-enrichment h3 a, #fields-enrichment h4 a').each(function( key, value ) {
			
			$(value).bind('click', self.handleAutoTagClick );
			$(value).addClass(eu.europeana.fulldoc.more_icon_class);
		});
		
	},
	
	handleAutoTagClick : function( e ) {
		
		e.preventDefault();
		var $elm = $(this);
		
		$elm.parent().next().slideToggle();
		
		if ( $elm.hasClass(eu.europeana.fulldoc.more_icon_class) ) {
			
			$elm.removeClass(eu.europeana.fulldoc.more_icon_class);
			$elm.addClass(eu.europeana.fulldoc.less_icon_class);
		} else {
			
			$elm.removeClass(eu.europeana.fulldoc.less_icon_class);
			$elm.addClass(eu.europeana.fulldoc.more_icon_class);
		}
		
	},
	
	
	addThis : function() {
		
		
		$('#lightbox_info .shares-link, #additional-info .shares-link').live('click', function(e){
			
			js.loader.loadScripts([{
				
				file: 'addthis' + js.min_suffix + '.js' + '?' + 'domready=1', //&async=1',
				
				path: eu.europeana.vars.branding + '/js/com/addthis/' + js.min_directory,
				callback : function() {

					// remove live handlers
					$('#lightbox_info .shares-link, #additional-info .shares-link').die('click');
						
					var url = $('head link[rel="canonical"]').attr('href'),
					title = $('head title').html(),
					description = $('head meta[name="description"]').attr('content');
					window.addthis_config = com.addthis.createConfigObject({
						pubid : eu.europeana.vars.addthis_pubid,
						ui_language: 'en', // eu.europeana.vars.locale,
						data_ga_property: eu.europeana.vars.gaId,
						data_ga_social : true,
						data_track_clickback: true,
						ui_use_css : true,
						ui_click: true		// disable hover
					});
					
					url = $('head meta[property="og:url"]').attr('content');
					window.addthis_share = com.addthis.createShareObject({
						// nb: twitter templates will soon be deprecated, no date is given
						// @link http://www.addthis.com/help/client-api#configuration-sharing-templates
						templates: { twitter: title + ': ' + url + ' #europeana' }
					});
					
					window.addthis_html = com.addthis.getToolboxHtml({
						html_class : '',
						url : url,
						title : title,
						description : description,
						services : {
							compact : {}
						},
						link_html : $('#additional-info .shares-link').html()
					});

					$('.shares-link').each(function(){
						$(this).html(window.addthis_html);
					});

					function addthisReady(evt) {
				        var oEvent = document.createEvent('HTMLEvents');
				        oEvent.initEvent('click', true, true);
				        
				        if($('#lightbox').is(':visible')){
				        	$('#lightbox .addthis_button')[0].dispatchEvent(oEvent);
				        }
				        else{
				        	$('#additional-info .addthis_button')[0].dispatchEvent(oEvent);
				        }
					}

					com.addthis.init( null, true, false,
						function(){
							addthis.addEventListener('addthis.ready', addthisReady);
						}		
					);
				}
			}]);
		});
		
		if( navigator.userAgent.match(/iPhone/i) ){
			$('.shares-link').click();			
		}
	},
	

	/**
	 * Makes the one and only call to eu.europeana.lightbox.init
	 * */
	initLightbox : function(url){
		
		js.console.log("initLightbox");
		

		if(!eu.europeana.fulldoc.lightboxOb){
			var cmp = $('<div id="lightbox">'	+ $('#lightbox-proxy').html() + '</div>');
			$(".iframe-wrap").empty().append(cmp);
			
			// copy title, meta and original context to the info panel
			
			// title
			cmp.find('#lightbox_info li.title').append(
				'&nbsp;' + $('#item-details h1').html()
			);
			
			// meta
			$('#item-details .lbN').each(function(i, ob){
				var next	 	= $(ob).next('.lbV');
				if(!next.length){
					next = $(ob).next('h2').find('.lbV').length ? $(ob).next('h2') : '';
				}
				var metaValue	= next.length ? next.html() : null;
				
				if(metaValue){
					var metaLabel = $(ob).html();
					cmp.find('#lightbox_info ul li.bottom').before(
						'<li><strong>' + metaLabel + '</strong>&nbsp;' + metaValue + '</li>'
					);
				}
			});

			// original context
			var ocLabel = $('.original-context div:not(:empty)');
			ocLabel = ocLabel.length ? ocLabel.html() : '';
			var ocValue = $('.original-context #urlRefIsShownAt');
			ocValue = ocValue.length ? ocValue.clone().wrap('<p>').parent().html() : '';
			
			cmp.find('#lightbox_info ul li.bottom').before(
				'<li><strong>' + ocLabel + ':</strong>&nbsp;'
				+ ocValue + '</li>'
			);

			// rights
			var rightsVal = $('.original-context .rights-badge').clone().wrap('<p>').parent().html();

			/*
			cmp.find('#lightbox_info ul li.bottom').append(
				 (rightsVal ? rightsVal : '') 
			);
			*/				

			cmp.find('#lightbox_info ul li.bottom').before(
				'<li>' + 	(rightsVal ? rightsVal : '') + '</li>' 
			);
			
			
			eu.europeana.fulldoc.lightboxOb = new eu.europeana.lightbox();
			
			eu.europeana.fulldoc.lightboxOb.init(
				{	"cmp"	:	cmp,
					"src"	:	url,
					"data"	:	eu.europeana.fulldoc.getLightboxableCount() > 1 ?  carouselData : null,
					onNav	: function(url){
						eu.europeana.fulldoc.setCarouselIndexByUrl(url);
					}
				}
			);
			
			$(".iframe-wrap").empty();
		}
	},
	
	
	/* Called once:
	 * All img / magnify clicks routed through here 
	 * */
	
	/**
	 * Test URLS
	 * 
	 * 
	 * simple lightbox					= http://localhost:8081/portal/record/09102/_SMS_MM_M777.html?start=1&query=paris&startPage=1&rows=24
	 * broken img lightbox				= 
	 * carousel lightbox				= http://localhost:8081/portal/record/09102/_CM_0159044.html?start=23&query=paris&startPage=1&rows=24
	 * carousel lightbox mixed media	= http://localhost:8081/portal/record/09102/_SMS_MM_M1383.html
	 * 
	 * 
	 * */
	
	triggerBind : function(){

		$(		'#carousel-1-img-measure img, '
			+ 	'#carousel-1-img-measure .lb-trigger, '
			+	'#carousel-1 .galleria-stage .galleria-image img'
			
		).die().live('click', eu.europeana.fulldoc.triggerClick);
		
		js.console.log("bound all triggers");
	},
	
	triggerClick : function(e){
		e = $(e.target);
		
		var target = "image";
		var openLB = carouselData[eu.europeana.fulldoc.getCarouselIndex()].external.type == 'image' && !eu.europeana.fulldoc.lightboxTestFailed && !$("#mobile-menu").is(":visible");
		
		if(e.hasClass('label') || e.hasClass('lb-trigger') || e == eu.europeana.fulldoc.triggerPanel){
			target = "magnify";
		}
		else if(e[0].nodeName.toUpperCase()=='IMG'){
			target = eu.europeana.fulldoc.lightboxTestFailed ? 'broken-img' : 'image';
		}
		
		// category, action, label
		var gaCategory	= 'Europeana Portal';
		var gaAction	= openLB ? 'Europeana Lightbox' : 'Europeana Redirect';
		var gaLabel		= 'External (' + target + ')';
		
		com.google.analytics.europeanaEventTrack(gaCategory, gaAction, gaLabel);
		
		var targetInfo		= e[0].nodeName + ' #' + e.attr('id') + ', .' + e.attr('class'); 
		var carouselInfo	= "carousel = " + ( typeof eu.europeana.fulldoc.carousel1 != 'undefined' );
		var gaData			= "[" + gaCategory + ", " + gaAction + ", " + gaLabel + "]"

		if(openLB){
			eu.europeana.fulldoc.showLightbox();
		}
		else{
			eu.europeana.fulldoc.winOpen();			
		}
		
	},
	
	winOpen : function(){
		var index = eu.europeana.fulldoc.getCarouselIndex();
		window.open(carouselData[index ? index : 0].external.url, '_new');
	},
	
	
	showLightbox : function(){
		
		$(".iframe-wrap").empty().append(eu.europeana.fulldoc.lightboxOb.getCmp() );
		
		$(".iframe-wrap, .close").unbind("click").each(function(i, ob){
			$(ob).click(function(e){
				if(e.target == ob){
					if(typeof addthis_close != 'undefined'){
						addthis_close();
					}
					$("#lightbox").remove();	// this is needed to stop ie8 making a black screen following closing of the lightbox.
					$(".overlaid-content").css('visibility', 'hidden');
				}
			});
		});
		
		eu.europeana.fulldoc.lightboxOb.showLightbox(function(){
			$(".overlaid-content").css('visibility', 'visible');	
		});											
	},
	
	initTriggerPanel: function(type, index, gallery){

		if(typeof(eu.europeana.fulldoc.triggerPanel)=="undefined"){
			// instantiate and hide
			eu.europeana.fulldoc.triggerPanel = $('<div class="lb-trigger" >'
					+ '<span rel="#lightbox" title="'
					+ '" class="icon-magplus label">'
					+ '</span>'
					+ '</div>'
			).appendTo($('#carousel-1-img-measure'));
			eu.europeana.fulldoc.triggerPanel.hide();
		}
		
		var triggerPanel	= eu.europeana.fulldoc.triggerPanel;
		var triggerSpan		= triggerPanel.find('.label');
		triggerSpan.attr('title', eu.europeana.vars.external.triggers.labels[type]);
		triggerSpan.html(eu.europeana.vars.external.triggers.labels[type]);
		
		if(!eu.europeana.fulldoc.lightboxOb && carouselData[index ? index : 0].external.type == 'image'){

			/*	if the image is wider than 200 px initialise the lightbox and show the trigger panel,
			 	if not set the cursoe icon for the image and the eu.europeana.fulldoc.lightboxTestFailed variable to false	*/
			
			$('<img src="'+ carouselData[index ? index : 0].external.url + '" style="visibility:hidden"/>')
				.appendTo('body').imagesLoaded(
							
				function($images, $proper, $broken){

					if($proper.length==1 && $proper.width() > 200){

						// need to store the real widths here, because by the time fulldoc.js loads the tmp img element may have been removed.
						
						var properW = $proper.width();
						var properH = $proper.height();
						
						// Add the markup

						eu.europeana.fulldoc.loadLightboxJS(
							function(){
								eu.europeana.fulldoc.initLightbox(carouselData[index ? index : 0].external.url, properW, properH);
								eu.europeana.fulldoc.showExternalTrigger(true, carouselData[index ? index : 0].external.type, gallery);
							}
						);
					}
					else{
						js.console.log("lightbox test failed: " + ($proper.length==1 ? "image was too small (" + $proper.width() + ")" : "image didn't load (url: " + carouselData[index ? index : 0].external.url + ")"));
						eu.europeana.fulldoc.lightboxTestFailed = true;
						
						// if the lightbox test fails then attach a click handler to the image
						$('#carousel-1-img-measure img').css('cursor', 'pointer');
					}
					$(this).remove();
				}
				// end image load test
			);
		}
		else{ // NON IMAGE
			eu.europeana.fulldoc.showExternalTrigger(true, carouselData[index ? index : 0].external.type, gallery);
		}

		eu.europeana.fulldoc.triggerBind();
	},
	
	
	/**
	 * 
	 * @gallery galleria instance
	 * @show true / false - false if we're changing the trigger from within a carousel
	 * 
	 * */
	showExternalTrigger : function(show, type, gallery){
		
		if($("#mobile-menu").is(":visible") ){
			return;
		}
		
		if(show){
			var marginTrigger = 0;
			
			if(gallery){				
				marginTrigger = ( $("#carousel-1").width() - $(gallery.getActiveImage()).width() ) / 2;
				var top = $('#carousel-1').height() - (2 * $('#carousel-1 .galleria-thumbnails-container').height());
				eu.europeana.fulldoc.triggerPanel.css("top", top + "px"); 
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

	
	/**
	 * @callbackLoad = fn handling overlay click binding / lighbtox init call / trigger update
	 * */
	loadLightboxJS : function(callbackLoad){

		js.loader.loadScripts([{
				name : 'touchswipe',
				file : 'touch-swipe.min.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/jquery/'
				/* ,callback: function(){ js.console.log('loaded touch swipe'); } */
		}]);		  
		  
		if(!window.showingPhone()){
			var lightboxJsFile = 'lightbox' + js.min_suffix + '.js' + js.cache_helper;
			
			if( js.loader.loader_status[lightboxJsFile]){
				if(callbackLoad){
					callbackLoad();					
				}
				return;
			}
			
			js.loader.loadScripts([{
				file : lightboxJsFile,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies :  $("html").hasClass('ie8') ? [ ] : ['touchswipe'],
				
				callback: function(){
					$(window).euRsz(function(){
						if(eu.europeana.fulldoc.lightboxOb){
							if( $('.overlaid-content').css('visibility') == 'hidden' ){
								return;
							}
							eu.europeana.fulldoc.lightboxOb.layout();							
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

		$('#carousel-1').css("height", eu.europeana.fulldoc.getCarousel1Height() + "px");	// set height to max height that will be needed
			
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
			autoplay:			0,
			fullscreenDoubleTap:	false,
			thumbnails: 		carouselData.length>1,
			max_scale_ratio:	1,					// prevent stretching (does this work?  no reference to this variable in galleria that I can find) 
			extend: function(e){
					
				$(window).add('.iframe-wrap').bind('keydown', function(e){
					var key	= window.event ? e.keyCode : e.which;
					if(key==39){
						if( ($('.overlaid-content').css('visibility') == 'hidden') ){
							if(eu.europeana.fulldoc.carousel1){
								$('#carousel-1 .galleria-image-nav-right').click();
							}
						}
						else{
							if(eu.europeana.fulldoc.lightboxOb){
								eu.europeana.fulldoc.lightboxOb.next();								
							}
						}
					}
					else if(key==37){
						if( ($('.overlaid-content').css('visibility') == 'hidden') ){
							if(eu.europeana.fulldoc.carousel1){
								$('#carousel-1 .galleria-image-nav-left').click();
							}
						}
						else{
							if(eu.europeana.fulldoc.lightboxOb){
								eu.europeana.fulldoc.lightboxOb.prev();								
							}
						}
					}
				});
				

				this.bind("image", function(e) {	// lightbox trigger
					
					if($('html').hasClass('ie8')){
						this.pause();
						if(e.index == currIndex){
							return;
						}
					}
					var currIndex = e.index;
					var gallery = this;
					var external = gallery._options.dataSource[e.index].external;
					eu.europeana.fulldoc.initTriggerPanel(external.type, e.index, gallery);
					
					if(eu.europeana.fulldoc.lightboxOb){
						eu.europeana.fulldoc.lightboxOb.goTo(e.index);
					}					
					
					$('#carousel-1 .galleria-stage .galleria-image:visible img').attr('title', carouselData[e.index].title);
					
				}); // end bind image
				
				eu.europeana.fulldoc.getCarouselIndex = function(){
					return $('#carousel-1').data('galleria').getIndex();
				};
				
				eu.europeana.fulldoc.setCarouselIndexByUrl = function(url){
					
					eu.europeana.fulldoc.suppressLightboxUpdate = true;
					
					for(var i=0; i<carouselData.length; i++){
						if(carouselData[i].external && carouselData[i].external.url == url){
							$('#carousel-1').data('galleria').show(i);
							break;
						}
					}
				};

				
			} // end extend
		}); // end Galleria.run
	},
	
	initBottomCarousel : function(){
		
		if(!eu.europeana.vars.isShowSimilarItems){
			return;
		}
		
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
				lightbox:		false,
				responsive:		true,
				dataSource:		carousel2Data,
				thumbnails: 	true,
				extend: function(e){
					
					var thisGallery = this;
					
					var doEllipsis = function(){
						var ellipsisObjects = [];
						$(carousel2selector + ' .europeana-carousel-info').each(
							function(i, ob){
								ellipsisObjects[ellipsisObjects.length] = new Ellipsis($(ob));					
							}
						);

						/*
						$(window).euRsz(function(){
							if(eu.europeana.vars.suppresResize){
								return;
							}
							for(var i=0; i<ellipsisObjects.length; i++ ){
								ellipsisObjects[i].respond();
							}							
						});
						*/
					};
					
					$(this).ready(function(e) {
						setTimeout(doEllipsis, 1000);
					});
	
					this.bind("loadfinish", function(e) {
						// Google Analytics
						if(!eu.europeana.fulldoc.setupAnalytics){
							
							var clicked = function(clickData){
								com.google.analytics.europeanaEventTrack(
									clickData.ga.action,
									clickData.ga.category,
									clickData.ga.url
								);
								if(clickData.open){
									window.location = clickData.open;
								}									
							};
							
							$('#explore-further .galleria-thumb-nav-right,  #explore-further .galleria-thumb-nav-left').click(function(e){
								clicked({
									"open" : false,
									"ga" : {
										"action"	: "Navigate",
										"category"	: "Similar-Items-Carousel",
										"url"		: ""
									}	
								});
							});
							
							var dataSource		= this._options.dataSource;
			
							$('#explore-further .galleria-thumbnails .galleria-image').each(function(i, ob){
								$(ob).unbind('click');
								$(ob).click(function(e){
									clicked({
										//"open" : dataSource[i].europeanaLink,
										"open" : js.utils.fixSearchRowLinks(dataSource[i].europeanaLink),
										"ga" : {
											"action"	: "Click-Through (link index " + i + ")",
											"category"	: "Similar-Items-Carousel",
											"url"		: dataSource[i].europeanaLink
										}	
									});
									e.stopPropagation();
								});
								$(ob).find('img').attr('alt', carousel2Data[i].title);
								$(ob).find('img').attr('title', carousel2Data[i].title);
							});
							eu.europeana.fulldoc.setupAnalytics = true;
						}
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

			// this is where we go when carousel test images don't load
			var initNoCarousel = function(){
				
				eu.europeana.fulldoc.getCarouselIndex = function(){
					return 0;
				};
				
				// show either the thumbnail or the alt text
				
				$("#carousel-1-img-measure img").removeClass("no-show");
				$("#carousel-1-img-measure").css("position",	"relative");
				$("#carousel-1-img-measure").css("text-align",	"center");
				$("#additional-info").css("padding-top", "1em");
				
				// if the thumbnail loaded then show it, otherwise restore the alt text (but prevent it from breaking the layout)
				if($("#carousel-1-img-measure").width()>0){
					if(carouselData[0].external){
						if(carouselData[0].external.type == '3d'){
							$('#carousel-1-img-measure img').css('cursor', 'pointer');
						}
						else{
							eu.europeana.fulldoc.lightboxable = carouselData[0].external;
							eu.europeana.fulldoc.initTriggerPanel( carouselData[0].external.type);
						}
					}
				}
				else{
					// if img doesn't load and alt text show, it can break layout
					$('#carousel-1-img-measure').css('white-space',		'normal');
					$('#carousel-1-img-measure').css('word-break',		'break-all');
				}
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
								
								var res = (tallestImageH < 80 ? 80 : tallestImageH) + galleriaOffsetY;
								return res;
								
							};
							eu.europeana.fulldoc.initTopCarousel();
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

