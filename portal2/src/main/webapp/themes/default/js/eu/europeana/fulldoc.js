js.utils.registerNamespace( 'eu.europeana.fulldoc' );

eu.europeana.fulldoc = {

	lightboxOb :  null,
	vimeoDetect : 'vimeo.com/video',
	permittedLbSoundCollections : eu.europeana.vars.soundCloudAwareCollections,
	
	
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
		
		// "View item at" link
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
		
		//js.console.log(JSON.stringify(carouselData));
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
			name : 'accordion-tabs',
			file : 'EuAccordionTabs' + js.min_suffix + '.js' + js.cache_helper,
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
					if(! js.utils.phoneTest() ){
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
		
		$('#fields-enrichment h3 a, #fields-enrichment h4 a, .concept .item-context-data a.more-info').each(function( key, value ) {
			
			$(value).bind('click', self.handleAutoTagClick );
			$(value).addClass(eu.europeana.fulldoc.more_icon_class);
		});
		
	},
	
	handleAutoTagClick : function( e ) {
		
		e.preventDefault();
		var $elm = $(this);
		
		var event = $elm.data('event');
		if(event){
    	   	$(window).trigger(event, $elm.parent().next() );
		}
		
		console.log('toggle ' + $elm.parent().next()[0].nodeName )
		$elm.parent().next().slideToggle();
		
		if ( $elm.hasClass(eu.europeana.fulldoc.more_icon_class) ) {			
			$elm.removeClass(eu.europeana.fulldoc.more_icon_class);
			$elm.addClass(eu.europeana.fulldoc.less_icon_class);
		}
		else {			
			$elm.removeClass(eu.europeana.fulldoc.less_icon_class);
			$elm.addClass(eu.europeana.fulldoc.more_icon_class);
		}
		if ( $elm.html() == eu.europeana.vars.show.more) {			
			$elm.html(eu.europeana.vars.show.less);
		}
		else {
			$elm.html(eu.europeana.vars.show.more);
		}
		
	},
	
	
	addThis : function() {
		
		$(document).on('click', '#lightbox_info .shares-link, #additional-info .shares-link', function(e){
		
			// e.preventDefault();
			
			
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
		
		
		if(  (navigator.userAgent.match(/OS 5(_\d)+ like Mac OS X/i) || navigator.userAgent.match(/OS 6(_\d)+ like Mac OS X/i)  ) && ! navigator.userAgent.match(/CriOS/i) ){
			$('.shares-link').click();			
		}

		//if( navigator.userAgent.match(/iPhone/i) && ! navigator.userAgent.match(/CriOS/i) ){
		//	$('.shares-link').click();			
		//}
	},
	

	/**
	 * Makes the one and only call to eu.europeana.lightbox.init
	 * */
	initLightbox : function(url){

		if(js.debug){
			js.console.log("initLightbox");			
		}

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
			var ocValue = $('.original-context #urlRefIsShownAt').length > 0 ? $('.original-context #urlRefIsShownAt') : $('.original-context #urlRefIsShownBy'); 
			
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
					"onNav"	: function(url){
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
		
		$(document).off('click',
			'#carousel-1-img-measure img, '
		+ 	'#carousel-1-img-measure .lb-trigger, '
		+	'#carousel-1 .galleria-stage .galleria-image img')		
		
		.on('click',
			'#carousel-1-img-measure img, '
		+ 	'#carousel-1-img-measure .lb-trigger, '
		+	'#carousel-1 .galleria-stage .galleria-image img',
		
		eu.europeana.fulldoc.triggerClick);
		
		
		js.console.log("bound all triggers");
	},
	
	triggerClick : function(e){
		
		e = $(e.target);
		
		var target = "image";
		var openLB = 	carouselData[eu.europeana.fulldoc.getCarouselIndex()].external
						&&
						(
							(
								carouselData[eu.europeana.fulldoc.getCarouselIndex()].external.type == 'image' 
								&& 
								!eu.europeana.fulldoc.lightboxTestFailed
							)
							|| 
							(
								carouselData[eu.europeana.fulldoc.getCarouselIndex()].external.type == 'video'
								&&
								carouselData[eu.europeana.fulldoc.getCarouselIndex()].external.url.indexOf(eu.europeana.fulldoc.vimeoDetect) > -1
							)
							|| 
							(
								carouselData[eu.europeana.fulldoc.getCarouselIndex()].external.type == 'sound'
								&&
								($.inArray(eu.europeana.vars.collectionId, eu.europeana.fulldoc.permittedLbSoundCollections) > -1) 
							)
						)
						&&
						!js.utils.phoneTest();
		
		
		if(e.hasClass('label') || e.hasClass('lb-trigger') || e == eu.europeana.fulldoc.triggerPanel){
			target = "magnify";
		}
		else if(e[0].nodeName.toUpperCase()=='IMG'){
			target = carouselData[eu.europeana.fulldoc.getCarouselIndex()].external ? (eu.europeana.fulldoc.lightboxTestFailed ? 'broken-img' : 'image') : 'link';
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
		
		if(eu.europeana.fulldoc.carousel1){
			var index = eu.europeana.fulldoc.getCarouselIndex();
			window.open(carouselData[index ? index : 0].external.url, '_new');
			
		}
		else{
			window.open(isShownBy.length ? isShownBy : isShownAt, '_new');
		}	
		
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
		
		var imgIndex = index ? index : 0;
		
		if(!carouselData[imgIndex].external){
			/* #1039 - "Clicking the thumbnail of an object interaction"
			 * uncomment this line if we want to show the trigger:
			 * 
			 * eu.europeana.fulldoc.showExternalTrigger(true, carouselData[0].dataType, null);  
			 */
			$('#carousel-1-img-measure img').css('cursor', 'pointer');			
		}
		else{
			if(!eu.europeana.fulldoc.lightboxOb && carouselData[imgIndex].external.type == 'image'){

				/*	if the image is wider than 200 px initialise the lightbox and show the trigger panel,
				 	if not set the cursoe icon for the image and the eu.europeana.fulldoc.lightboxTestFailed variable to false	*/
				
				var loadLightbox = function(){
					$('<img src="'+ carouselData[imgIndex].external.url + '" style="visibility:hidden"/>')
						.appendTo('body').imagesLoaded(
									
						function($images, $proper, $broken){
		
							if($proper.length==1 && $proper.width() > 200){
		
								// need to store the real widths here, because by the time fulldoc.js loads the tmp img element may have been removed.
								
								//var properW = $proper.width();
								//var properH = $proper.height();
								
								// Add the markup
		
								eu.europeana.fulldoc.loadLightboxJS(
									function(){
										//eu.europeana.fulldoc.initLightbox(carouselData[imgIndex].external.url, properW, properH);
										eu.europeana.fulldoc.initLightbox(carouselData[imgIndex].external.url);
										eu.europeana.fulldoc.showExternalTrigger(true, carouselData[imgIndex].external.type, gallery);
									}
								);
							}
							else{
								js.console.log("lightbox test failed: " + ($proper.length==1 ? "image was too small (" + $proper.width() + ")" : "image didn't load (url: " + carouselData[imgIndex].external.url + ")"));
								eu.europeana.fulldoc.lightboxTestFailed = true;
								
								// if the lightbox test fails then triggerBind (below) will attach a click handler - make sure it has a pointer cursor here
								$('#carousel-1-img-measure img').css('cursor', 'pointer');
							}
							$(this).remove();
						}
						// end image load test
					);
				};
				loadLightbox();
			}
			else{ // NON IMAGE
				if(
						(
							carouselData[eu.europeana.fulldoc.getCarouselIndex()].external.type == 'video' 
							&&
							carouselData[eu.europeana.fulldoc.getCarouselIndex()].external.url.indexOf(eu.europeana.fulldoc.vimeoDetect) > -1
						)				
						||
						(
							carouselData[eu.europeana.fulldoc.getCarouselIndex()].external.type == 'sound'
							&&
							($.inArray(eu.europeana.vars.collectionId, eu.europeana.fulldoc.permittedLbSoundCollections) > -1) 
						)				
					
				){
					
					eu.europeana.fulldoc.loadLightboxJS(
						function(){
							//var videoW = 582;
							//var videoH = 315;						
							//eu.europeana.fulldoc.initLightbox(carouselData[imgIndex].external.url, videoW, videoH);
							eu.europeana.fulldoc.initLightbox(carouselData[imgIndex].external.url);
							eu.europeana.fulldoc.showExternalTrigger(true, carouselData[imgIndex].external.type, gallery);
						}
					);
				}
				else{
					eu.europeana.fulldoc.showExternalTrigger(true, carouselData[imgIndex].external.type, gallery);
				}
				
			}			
		}
		
		eu.europeana.fulldoc.triggerBind();
	},
	
	
	/**
	 * 
	 * @gallery galleria instance
	 * @gallery true / false - false if we're changing the trigger from within a carousel
	 * 
	 * */
	showExternalTrigger : function(show, type, gallery){
		
		if(js.utils.phoneTest()){
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
		}]);
		
		/*
		js.loader.loadScripts([{
			name : 'fitvids',
			file : 'jquery.fitvids.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/com/davatron5000/'
		}]);		  
		*/
		
		if(!js.utils.phoneTest()){
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
				
				/* 
				dependencies :  $("html").hasClass('ie8') ? ['fitvids'] : ['touchswipe', 'fitvids'],
				*/
				
				dependencies :  $("html").hasClass('ie8') ? [] : ['touchswipe'],
				
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
	

	getLightboxableCount:function(){
		var lightboxableCount = 0;
		for(var i=0; i<carouselData.length; i++){
			if(carouselData[i].external && carouselData[i].external.type == 'image'){
				lightboxableCount++;
			}
			else if(carouselData[i].external && carouselData[i].external.type == 'video' && carouselData[i].external.url.indexOf(eu.europeana.fulldoc.vimeoDetect) > -1 ){
				lightboxableCount++;				
			}
			else if(carouselData[eu.europeana.fulldoc.getCarouselIndex()].external.type == 'sound' && ($.inArray(eu.europeana.vars.collectionId, eu.europeana.fulldoc.permittedLbSoundCollections) > -1) ){
				lightboxableCount++;								
			}			
		}
		return lightboxableCount;
	},
	
	
	initCarousels: function(){
		
		Galleria.loadTheme(eu.europeana.vars.branding + '/js/galleria/themes/europeanax/' + js.min_directory + 'galleria.europeanax'  + js.min_suffix + '.js');
		
		$("#carousel-1-img-measure img").imagesLoaded( function($images, $proper, $broken){

			// this is where we go when there is no carosuel data or when the carousel images didn't load
			
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
							eu.europeana.fulldoc.initTriggerPanel( carouselData[0].external.type);
						}
					}
					else{
						// init trigger panel here for non-lightboxable stuff
						eu.europeana.fulldoc.initTriggerPanel( carouselData[0].dataType);
					}
				}
				else{
					
					// if thumbnail img doesn't load and alt text show, it can break layout
					$('#carousel-1-img-measure').css('white-space',		'normal');
					$('#carousel-1-img-measure').css('word-break',		'break-all');
				}
			};
			// end initNoCarousel
			
			
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
								if( js.utils.phoneTest() ){
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
				// not carouselTest object
				if(js.debug){
					js.console.log("no carousel test to run");					
				}			
				initNoCarousel();
			}
		});

		
		/**
		 * 
		 * MLT 
		 * 
		 * */
				
		if(typeof(mlt) != 'undefined'){

			// structure to track what's loaded
			
			var loadData = {};

			var getActiveSection = function(){
				return $('#mlt .section.active');
			}
			
			var showSpinner = function(){
				var section = $('#mlt-carousel');
				if(section.html().length == 0){
					section.addClass('loading');		
				}
		    	section.append('<div class="ajax-overlay">');
		    	$('.ajax-overlay').css('top', $('.ajax-overlay').parent().scrollTop() + 'px');
		    	section.find('.galleria-thumbnails').css('top', '1000px');
		    };
		    
			var hideSpinner = function(thisGallery){
				return;
				/*
				var section = $('#mlt-carousel');//$('#more-like-this');
				section.find('.ajax-overlay').remove();				
				section.removeClass('loading');	
				section.find('.galleria-thumbnails-container .galleria-image').first().find('img').focus();
		    	section.find('.galleria-thumbnail').css('top', '0px');
		    	*/
			};
		
			var getQuery = function(){
				
				var querySuffix = window.location.href;

				querySuffix = querySuffix.substring(querySuffix.indexOf('/record')+7, querySuffix.indexOf('.html') ); 
				querySuffix = '+NOT+europeana_id:&quot;' + querySuffix  + '&quot;';
				console.log("querySuffix = " + querySuffix )
				
				return mltQuery + querySuffix;
			}


            var fnInView = function(){
            	var cw      = $('#more-like-this .galleria-thumbnails-container').width();
            	var first   = $('#more-like-this .galleria-thumbnails-container .galleria-image:first');            	
            	var firstW  = parseInt(first.css('width'));
				var firstM  = parseInt(first.css('margin-left'));
				var res     = Math.ceil(cw / ( firstW + 2*firstM ));
				var max     = 4;
				
				//console.log('fnInView returns ' + res)
				
				return Math.floor(max, res);
            }
			
			var loadMore = function(){
				
				//console.log('loadMore start');
				
				var start	= loadData.loaded + 1;
				var qty		= fnInView();
				var query   = getQuery();
				
				loadData.expectedThumbCount = ((loadData.loaded + qty) -1)  >= loadData.total ?  loadData.total -1 : ((loadData.loaded + qty) -1);

				console.log('set loadData.expectedThumbCount to ' + loadData.expectedThumbCount + ', total is ' + loadData.total);
				
				loadMltData(query, processResult, start, qty)
				
				//console.log('loadMore end');				
			};
            
			/////////////////////////////////////////////////////
			
			var mltGalleriaOpTemplate = {
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
				
				XXXidleMode: false,
				isIncrementalLoad: true,
				suppressIdle:   true,

				thumbnails: 	true,
					
				extend: function(e){
					var thisGallery = this;
					var doEllipsis = function(){
						
						var ellipsisObjects = [];
						//var index = eu.europeana.fulldoc.mltTabs.getOpenTabIndex();
						var carousel = $('#more-like-this');// $('#mlt-carousel-' + index);  // TODO can't I just access the object element directly?
						
						$(carousel.find('.europeana-carousel-info')).each(
							function(i, ob){
								ellipsisObjects[ellipsisObjects.length] = new Ellipsis($(ob));					
							}
						);
												
						$(window).euRsz(function(){
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
					
					
					//this.bind("loadfinish", function(e) {
					this.bind("thumbnail", function(e) {
					
						//var index = eu.europeana.fulldoc.mltTabs.getOpenTabIndex();
												
						// we only execute on the last thumbnail....
						if( e.index != loadData.expectedThumbCount){
							return;
						}
							
						
						// bind swipe events
						if( ! $("html").hasClass('ie8') ){
							
							thisGallery.$( 'container' ).find( '.galleria-thumbnails-container .galleria-image').swipe({
									swipeStatus:function(event, phase, direction, distance, fingerCount) {
										if(phase=="end"){
											if(direction == "left"){
												thisGallery.$( 'container' ).find('.galleria-thumb-nav-right').click();
											}
											if(direction == "right"){
												thisGallery.$( 'container' ).find('.galleria-thumb-nav-left').click();
											}
										}
									},
									triggerOnTouchEnd:false,
									threshold:100
								}
							);
						}

						// bind key events
							
						thisGallery.$( 'container' ).find('.galleria-thumbnails-container  .galleria-image'  )
						.keypress(function(e){
							if(e.keyCode == 39){
								var right = thisGallery.$( 'container' ).find('.galleria-thumb-nav-right');
								if(!right.hasClass('europeana-disabled')){
									right.click();									
								}
							}
							else if(e.keyCode == 37){
								var left = thisGallery.$( 'container' ).find('.galleria-thumb-nav-left');
								if(!left.hasClass('europeana-disabled')){
									left.click();									
								}
							}
					   	});
						
						//var dataSource = this._options.dataSource;

						thisGallery.$( 'container' ).find('.galleria-thumb-nav-left').attr('tabindex', 0);
						thisGallery.$( 'container' ).find('.galleria-thumb-nav-right').attr('tabindex', 1);
						thisGallery.$( 'container' ).find('.galleria-thumbnails-list').attr('tabindex', 2);


//						$('#mlt .section.active .galleria-thumbnails .galleria-image').each(function(i, ob){
						$('#more-like-this .galleria-thumbnails .galleria-image').each(function(i, ob){
							$(ob).find('img').attr({
								'alt'	:	loadData.carouselMltData[i].title,
								'title'	: loadData.carouselMltData[i].title,
								'tabindex' : i+3
							});
						});
							
						if(!loadData.setup){
							hideSpinner(thisGallery);
							loadData.setup = true;
						}

						
						// rebind right arrow / extra handler for left arrow
						$('#more-like-this .carousel').find('.galleria-thumb-nav-left').bind('click', function(e){
							rightArrow.removeClass('europeana-disabled');							
							rightArrow.removeClass('disabled');							
						});
						
						$('#more-like-this .carousel').find('.galleria-thumb-nav-right').unbind('click').bind('click', function(e){
			                e.preventDefault();
			                
			                //var index = eu.europeana.fulldoc.mltTabs.getOpenTabIndex();
							var x     = thisGallery;
			                var xOps  = x._options;
							var xCar  = x._carousel;

							//console.log('update current to: ' + (xCar.current + xOps.carouselSteps) );


							loadData.current = (xCar.current + xOps.carouselSteps);  // write next val
							//console.log('set loadData.tabs[index].current to ' + loadData.tabs[index].current);
							
							inView = fnInView();
			                
			                var loadedCount = xCar.hooks.length -1;
			                var current     = xCar.current + 1;

			                if( (current + inView) >= loadedCount){
			                	//console.log('load more then navigate (tabIndex[' + index + '])');
			                	loadMore();
			                }
			                else{
			                	xCar.set(loadData.current);
			                	
								if(loadData.loaded < loadData.total){
									//console.log('normal nav and show the arrow  (' + $('#mlt .section.active .carousel').find('.galleria-thumb-nav-right').length + ')');
									$('#more-like-this .carousel').find('.galleria-thumb-nav-right').show();
								}
								else{
									$('#more-like-this .carousel').find('.galleria-thumb-nav-right').addClass('europeana-disabled');
									$('#more-like-this .carousel').find('.galleria-thumb-nav-right').css('display', 'none');
								}
			                }
						});

						
						// Show or hide the "Next" button....
						// ...Galleria shows the arrows on idle_exit, so "europeana-disabled" class is used here 
						var showArrows = function(){
							var rightArrow = $('#more-like-this .carousel .galleria-thumb-nav-right');
							
							if((loadData.current + fnInView()) < loadData.total){
								if(rightArrow.hasClass('europeana-disabled') ){
									//alert('re-enable here, undoes the fix....');
								}
								rightArrow.show();
								rightArrow.removeClass('disabled');
								rightArrow.removeClass('europeana-disabled');
							}
							else{
								rightArrow.hide();
								rightArrow.addClass('disabled');
								rightArrow.addClass('europeana-disabled');
							}
							
							setTimeout(function(){
								thisGallery.trigger( Galleria.RESCALE );
							}, 150);
						};
						
						
						var x     = thisGallery;
		                var xOps  = x._options;

						if(typeof loadData.current == 'undefined'){
							//console.log("loadData.tabs[index].current == 'undefined' so set to 0");
							loadData.current = 0;			
						}
						else {
							
							var xCar  = x._carousel;

							//console.log('post process [' + index + '] nav kicking in now.... curr1 (internal): ' + xCar.current + ',  curr2 (tracked): ' + loadData.tabs[index].current);
							
							var prevCurrent = loadData.current - loadData.loadSize;
							
//							alert('prevCurrent ' + prevCurrent);
							
							var oldSpeed = thisGallery._options.carouselSpeed;

							thisGallery.setOptions( 'carouselSpeed', 0 );
							xCar.set(prevCurrent, true);
							thisGallery.setOptions( 'carouselSpeed', oldSpeed);

							hideSpinner(thisGallery);

							setTimeout(function(){
								xCar.set(loadData.current);
								showArrows();

							//	thisGallery.trigger( Galleria.RESCALE );

							}, 1);

	//						alert('current ' + loadData.current);

						}
						

						showArrows();
					}); // end loadFinish
					

					thisGallery.bind("idle_enter",function(e) {

						//console.log('current = ' + thisGallery._carousel.current)
						//console.log('inview = ' + fnInView())
						//console.log('loadData.total = ' + loadData.total)

		                if( (thisGallery._carousel.current + fnInView() ) <= loadData.total){
		                	var rightArrow = $('#more-like-this .carousel .galleria-thumb-nav-right');
		                	rightArrow.removeClass('disabled');
		                	rightArrow.show();
		                }
		                else{
		                	//alert('end of dataset - do not show right arrow...')
		                }
					});
					
				} // end extend
			};

			
			var carouselInit = function(){
								
				// Defines the ops and calls run to init carousel
				
				
				// 150 too small for iphone: make min height 200
				var maxHeight = Math.max(200, loadData.fnGetCarouselDimensions().h) + "px";
				
				$('#mlt-carousel').css("height", maxHeight);
							
				
				var mltGalleriaOps = $.extend(true, {}, mltGalleriaOpTemplate);
				mltGalleriaOps.dataSource = loadData.carouselMltData;


				Galleria.run('#mlt-carousel', mltGalleriaOps);
				var allGalleries = Galleria.get();
				loadData.carousel = allGalleries[allGalleries.length-1];

			} // end carouselInit()

			
			// load all link
			var getLoadAllLink = function(){
				return '<a class="load-all" href="/portal/search.html?query=' + mltQuery + '&rows=' + eu.europeana.vars.rows + '">' + labelLoadAll + '</a>';
			}


			// function executed on ajax return
			
			var processResult = function(data){

				var section       = $('#more-like-this');
				var sectionImages = $('#more-like-this .images');

				// Append image data to measure div and build carousel data structure
				
				if(typeof loadData.carouselMltData == 'undefined'){
					loadData.carouselMltData = [];
				}
				
				$.each(data.items, function(i, ob){
					
					var imgUrl = ((typeof ob.edmPreview != 'undefined' && ob.edmPreview.length) ? ob.edmPreview[0] : 'http://europeanastatic.eu/api/image?size=FULL_DOC&type=' + ob.type.toUpperCase()).replace('size=LARGE', 'size=MEDIUM');
					
					sectionImages.append('<img src="' + imgUrl + '">');
	
					loadData.carouselMltData[loadData.carouselMltData.length] = {
						image:	imgUrl,
						title:	ob.title[0],
						link:	ob.link.substr(0, ob.link.indexOf('.json') ).replace('/api/v2/', '/portal/') + '.html'
						/* set linkTarget here to open in new window */
					};
				});
				

				// First load initialises the carousel for this tab
				
				if(typeof(loadData.carousel) == 'undefined'){

					sectionImages.imagesLoaded( function($images, $proper, $broken){
						
						loadData.fnGetCarouselDimensions = function(){
							
							sectionImages.css("display", "inline-block");
							
			  				var tallestImageH	= sectionImages.height();
			  				
			  				sectionImages.css("display", "block");
			  				
			  				var widestImageW	= sectionImages.width();
			  				
			  				sectionImages.css("display", "none");
			  				
			  				return {w:widestImageW, h:tallestImageH};
						};
						
						sectionImages.css("display", "none");
						carouselInit();
					});
				}
				else{
					// carousel is defined
					
					// use in relaod or get rid
					var storedCarousel		= loadData.carousel;
					var storedCarouselData	= loadData.carouselMltData;
					
					// extra hide 
					sectionImages.css("display", "none");
					
					var mltGalleriaOps        = $.extend(true, {}, mltGalleriaOpTemplate);
					mltGalleriaOps.dataSource = loadData.carouselMltData;
					loadData.leftOffset       = $('#more-like-this .galleria-thumbnails').css('left');
					
					loadData.carousel.destroy();
					
					Galleria.run('#mlt-carousel', mltGalleriaOps );
					var allGalleries = Galleria.get();
					loadData.carousel = allGalleries[allGalleries.length-1];
				}
				
				// load all link
				if(data.totalResults > fnInView() ){
					if(typeof loadData.loaded == 'undefined'){
						if( $('.load-all').length ==0 ){
							section.append(getLoadAllLink());							
						}
					}
				}

				
				// Update data tracking model

				loadData.loaded	= typeof loadData.loaded == 'undefined' ? data.itemsCount : loadData.loaded + data.itemsCount;
				loadData.total	= data.totalResults;
				
			}; // end process result

			
			
			// ajax calls for mlt - invokes processResult on success
			
			var loadMltData = function(query, fn, start, qty){
				
				try{
					showSpinner();

					var url = window.location.href.split('/portal')[0] + '/api/v2/search.json?wskey=api2demo';
					if(url.indexOf('localhost')>-1 || url.indexOf('http://192')>-1){
						url = "http://test.portal2.eanadev.org/api/v2/search.json?wskey=api2demo";
					}

					url += '&query='	+ query;
					url += '&start='	+ (start ? start : 1);
					url += '&rows='		+ (qty ? qty : mltTotal > fnInView() ? fnInView() : mltTotal);
					url += '&profile=minimal';
					
					if(typeof loadData.expectedThumbCount == 'undefined'){
						loadData.expectedThumbCount = (qty ? qty-1 : mltTotal > fnInView() ? fnInView()-1 : mltTotal -1 ); 
					}
					
					loadData.loadSize = (qty ? qty : fnInView());
					
					$.ajax({
						"url":				url.replace(/&quot;/g, '"'),
				        "dataType":			"json", 
				        "crossDomain":		true,
				        "type":				"POST",
				        "fail":function(e){
				    		console.log("Ajax error: " + e);
				    		hideSpinner();
				        },
				        "success":function(data){
			        		fn(data);				        		
				        	return 0;
						}
					});
				}
				catch(e){
					hideSpinner(thisGallery);
					console.log("Ajax error: " + e);
				}
			}

			var initIfBigEnough = function(){
				var mobile = js.utils.phoneTest();
				
				if(mobile){
					var sel = '.mlt-title';
					var mltEllipsis = new Ellipsis($(sel));

					mltEllipsis.respond();

					if(mltTotal > 1){						
						if( $('.load-all').length ==0 ){
							$('#more-like-this').append(getLoadAllLink());
						}
					}
				}
				else{
					if(  $('#more-like-this').find('#mlt-carousel').length == 0 ){
						
						$('#more-like-this').empty();
						$('<div class="carousel europeana-carousel" id="mlt-carousel">').appendTo($('#more-like-this'));
						loadMltData(getQuery(), processResult);

					}
				}				
			};
			
			$(window).euRsz(function(){
				initIfBigEnough();
			});
			initIfBigEnough();
			
		} // end if mlt

		
		/**
		 * 
		 * geo-location
		 * 
		 * */
	   	$(window).bind('init-map', function(e, tgtDiv){
	   		var mapId     = 'geo-map';
	   		var mapInfoId = 'geo-map-info';
	   		
	   		if($('#' + mapId).length){
	   			return;
	   		}
			var longitude = $('#longitude');
			var latitude  = $('#latitude');
			
			if(longitude.length && latitude.length){
				
				longitude = longitude.val();
				latitude =  latitude.val();
	
				if( ! [latitude, longitude].join(',').match(/^\s*-?\d+\.\d+\,\s?-?\d+\.\d+\s*$/) ){
					console.log('invalid coordinates (' + latitude + ', ' + longitude + ') - exit map');
					return;
				}
				
				longitude = parseFloat(longitude);
				latitude =  parseFloat(latitude);
	
				var path = eu.europeana.vars.branding + '/js/com/leaflet/';
	
				$.each(
					['leaflet.min.css',
				     'leaflet.ie.css',
				     'Leaflet-MiniMap-master/src/Control.MiniMap.css'],
				    function(i, stylesheet){
						$('head').append('<link rel="stylesheet" href="' + path + stylesheet + '" type="text/css"/>');				
					}
				);
				
				var initMap = function(){
					$(tgtDiv).append('<li><div id="' + mapId + '"></div><div id="' + mapInfoId + '"></div></li>');
	    			var mqTilesAttr = 'Tiles &copy; <a href="http://www.mapquest.com/" target="_blank">MapQuest</a> <img src="http://developer.mapquest.com/content/osm/mq_logo.png" />';
	    			
	    			// map quest
	    			var mq = new L.TileLayer(
	    				'http://otile{s}.mqcdn.com/tiles/1.0.0/{type}/{z}/{x}/{y}.png',
	    				{
        					minZoom: 4,
        					maxZoom: 18,
        					attribution: mqTilesAttr,
        					subdomains: '1234',
        					type: 'osm'
	    				}
	    			);
	    			var map = L.map(mapId, {
	    			    center: new L.LatLng(latitude, longitude),
	    			    zoomControl: true,
	    			    zoom: 8
	    			});
	    			map.addLayer(mq);
	    			map.invalidateSize(); 			
	    			
	    			L.marker([latitude, longitude]).addTo(map);
	    			
	    			var placeName = '';
	    			switch($('input[name="placename"]').length)
	    			{
		    			case 1:
		    				placeName = $('input[name="placename"]').val();
		    				break;
		    			case 2: {
		    				var names = $('input[name="placename"]').map(function() { return this.value; }).get();
		    				var uniqueNames = [];
			    			$.each(names, function(i, el){
			    			    if($.inArray(el, uniqueNames) === -1){
			    			    	uniqueNames.push(el);
			    			    } 
			    			});
		    				placeName = uniqueNames.join(', ');
		    				break;		    				
		    			}
	    			} 

	    			if(!$('input[name="placename"]').length || !$('input[name="placename"]').val().length){
	    				$('#' + mapInfoId).addClass('uppercase');
	    			}
	    			$('#' + mapInfoId).html(placeName + ' ' + eu.europeana.vars.map.coordinates + ': ' +  latitude + '&deg; ' +  (latitude > 0 ? eu.europeana.vars.map.north : eu.europeana.vars.map.south)  + ', ' +  longitude + '&deg; ' + (longitude > 0 ? eu.europeana.vars.map.east : eu.europeana.vars.map.west ) 	);
				};
	
				js.loader.loadScripts([
	   			    {
						"file" : 'leaflet.js',
						"path" : path,
						"name" : "leaflet"
					},
				    {
						"file" : 'Leaflet-Pan/L.Control.Pan.js',
						"path" : path,
						"name" : "pan",
						dependencies : ["leaflet"]
					},
					{
						"path" : path,
						"file" : "Leaflet-MiniMap-master/src/Control.MiniMap.js",
						dependencies : ["leaflet", "pan"],
						callback : function() {
							initMap();
						}
					}
				]);
	
			}
		});
		
	}
	
};

eu.europeana.fulldoc.init();

