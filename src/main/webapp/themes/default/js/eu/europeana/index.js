(function() {
	
	'use strict';

	var setupAnalytics = false;
	
	var initResponsiveImages = function(){

		var setup = function(){
			var initialSuffix = '_1'; // smallest by default
			if($.browser.msie  && ( parseInt($.browser.version, 10) === 7 || parseInt($.browser.version, 10) === 8 )  ){
				initialSuffix = '_3'; // largest by default
			}
			
			new euResponsive({
				"galleryName"	:	"euresponsive",
				"selector"		:	"#section-blog .responsive_half",
				"initialSuffix"	:	initialSuffix,
				"suffixes"		:	["_1", "_2", "_3", "_2"]
			});
			
			new euResponsive({
				"galleryName"	:	"euresponsive",
				"selector"		:	"#section-featured-content .responsive_half",
				"initialSuffix"	:	initialSuffix,
				"suffixes"		:	["_1", "_2", "_3", "_2"]
			});
			

		};
		setup();
	};
	
	var initCarousels = function(){
		Galleria.loadTheme(eu.europeana.vars.branding + '/js/galleria/themes/europeanax/' + js.min_directory + 'galleria.europeanax'  + js.min_suffix + '.js');
		Galleria.configure({
				transition:		'fadeslide',		/* fade, slide, flash, fadeslide, pulse */
				carousel:		true,
				carouselSpeed:	1200,				/* transition speed */
				carouselSteps:	2,
				easing:			'galleriaOut',
				
				imageCrop:		false,				/* if true, make pan true (custom logic in galleria theme overrides this setting) */
				imagePan:		false,
				
				lightbox:		false,
				responsive:		true,
				idleMode:		true,
				popupLinks:		true,
				fullscreenDoubleTap:	false,
				debug:			js.debug
		});

		if(carouselData && carouselData.length>0){
			$('<img src="' + carouselData[0].image + '" style="visibility:hidden"/>').appendTo("#carousel-1");			
		}
		
		var carouselInitialSuffix	= '_1';
		
    	
		var carouselSelector		= '#carousel-1 img';
		var carosuelEuResponsive = new euResponsive({
			"galleryName"	:	"euresponsive",
			"selector"		:	carouselSelector,
			"initialSuffix"	:	carouselInitialSuffix,
			"oneOff"		:	true
		});
		
		var src = $($(carouselSelector)[0]).attr("src");
		if( src.indexOf( carouselInitialSuffix + "." ) == -1 || $("html").hasClass('ie8') ){
			
			// we're bigger than a mobile: update all image urls in the carousel data to appropriate size
			var lastSuffix = carosuelEuResponsive.getLastSuffix();
			
			if($("html").hasClass('ie8')){
				lastSuffix = '_4';
			}

			if(carouselData && carouselData.length>0){
				$(carouselData).each(function(i, ob){
					ob.image = ob.image.replace(carouselInitialSuffix + ".", lastSuffix + ".");
					$($("#carousel-1 img")[i]).attr("src", ob.fullSize);
				});
			}			
		}
		 
		
		$("#carousel-1").imagesLoaded(
			function($images, $proper, $broken) {
				
				var imgW			= $(this).find("img").width();
				var imgH			= $(this).find("img").height();
				
				var msgFailed = "(" + $broken.length + " broke, " + $proper.length + " succeeded)";
				for(var i=0; i<$broken.length; i++){
					msgFailed += "\n  broke: " + $($broken[0]).attr("src")
				}
				for(var i=0; i<$proper.length; i++){
					msgFailed += "\n  proper: " + $($proper[0]).attr("src")
				}
				
				$("#carousel-1 img").remove();

				var carousel		= $("#carousel-1");
				var parentWidth		= carousel.width();
				

				var ratio			= imgW / imgH;
				var thumb			= $('<div class="galleria-thumbnails-container"></div>').appendTo(carousel);
				
				carousel.css("height",  (parentWidth/ratio) + "px");
				carousel.css("width",	"100%");
				
				thumb.remove();
				
				$('#carousel-1').galleria({
					dataSource:carouselData,
					autoplay:17000,
					debug:js.debug,
					extend: function(e){
						
						var thisGallery = this;
						
						// Info update
						this.bind("image", function(e) {
							var gallery = this;
							$("#carousel-1 .linkButton").html(carouselData[e.index].linkDescription);
							$("#carousel-1-external-info").html( $("#carousel-1 .galleria-info-title").html() );

						});

						// Google Analytics
						
						this.bind("loadfinish", function(e) {
							
							if(!setupAnalytics){
								
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

								var dataSource		= this._options.dataSource;

								setTimeout(function(){
									$('#carousel-1 .galleria-thumbnails img').add('#carousel-1 .galleria-image-nav-right').add('#carousel-1 .galleria-image-nav-left').click(function(e){
										clicked({
											"open" : false,
											"ga" : {
												"action"	: "Navigate",
												"category"	: "Index-Carousel",
												"url"		: ""
											}	
										});
									});
									
									$('#carousel-1 .galleria-stage .galleria-images').add('#carousel-1 .galleria-info').add('#carousel-1 .galleria-info button').click(function(e){
										clicked({
											"open" : dataSource[thisGallery.getIndex()].europeanaLink,
											"ga" : {
												"action"	: "Click-Through (link index " + thisGallery.getIndex() + ")",
												"category"	: "Index-Carousel",
												"url"		: dataSource[thisGallery.getIndex()].europeanaLink
											}	
										});
										e.stopPropagation();
									});
								}, 200);
								
								setupAnalytics = true;

							}
						});
					}			
				});
			
			}).each(function() {
				if(this.complete){
					$(this).load();
				}
		});

	};
	
	/*
	var initAddThis = function(){
		var url = $('head link[rel="canonical"]').attr('href'),
			title = $('head title').html(),
			description = $('head meta[name="description"]').attr('content');
			window.addthis_config = com.addthis.createConfigObject({
				pubid : eu.europeana.vars.addthis_pubid,
				ui_language: eu.europeana.vars.locale,
				data_ga_property: eu.europeana.vars.gaId,
				data_ga_social : true,
				data_track_clickback: true,
				ui_click: true,		// disable hover
				ui_use_css : true});
		
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
			link_html : $('#shares-link').html()
		});

		var addThisHtml = com.addthis.getToolboxHtml({
			html_class : '',
			url : url,
			title : title,
			description : description,
			services : {
				compact			:	{},
				tweet			:	{},
				google_plusone	:	{},
				facebook_like	:	{},
				email			:	{},
				pinterest		:	{}

			}
			//, link_html : $('#shares-link').html()
		});

		$("#addthis_new").html(addThisHtml);
		js.console.log("done: " + $("#addthis_new").length + "    \n\n\n" + addThisHtml   );
		
		$('#addthis_new .addthis_button')					.html('+')
		$('#addthis_new .addthis_button_tweet')				.html('T')
		$('#addthis_new .addthis_button_google_plusone')	.html('G')
		$('#addthis_new .addthis_button_facebook_like')		.html('F')
		
		//addthis.addEventListener('addthis.ready' );
		
		com.addthis.init( null, true, false,
			function(){
				//				$('#addthis_new .addthis_button_facebook_like')		.html('<span class="icon-facebook"></span>');
			}
		);
		
		//js.console.log("addThisHtml = " + addThisHtml);
		
		$('#shares-link').html(
			addThisHtml
		);
		
		$('#shares-link').hide();
		com.addthis.init( null, true, false );
		
		setTimeout( function() {
			$('#shares-link').fadeIn(function(){
				$(this).css("display", "inline-block");
			}); },
			600 );
	};
	 */
	
	var init = function() {
		
		js.loader.loadScripts([{
			file : 'jquery.imagesloaded.min.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/jquery/' + js.min_directory,
			callback : function() {
				initCarousels();
			}
		}]);
		
		initResponsiveImages();
		
		// Make sections collapsible
		$("#section-blog").Collapsible({
			headingSelector:	"#collapse-header-1",
			iconSelector:		".collapse-icon",
			bodySelector:		".collapse-content",
			expandedClass:		'icon-arrow',
			collapsedClass:		'icon-arrow-3',
			executeDefaultClick: true,
			toggleFn: function(){return $("#mobile-menu").is(":visible");},
			fireFirstOpen:		function(){
    			eu.europeana.vars.suppresResize = true;
    			
    			//var url = window.location.href.indexOf('/portal') > 0 ? 'index.html?fragment=blog' : '/portal/index.html?fragment=blog';
    			
    			//alert("href: " + window.location.href + "\n\nurl: " + url + "\n\nindexOf: " + window.location.href.indexOf);
    			
				$.ajax({
					url: '/portal/index.html?fragment=blog',
					dataType: 'json',
					success: function(data){
						$("#section-blog .collapse-content").html(data.markup);
		    			eu.europeana.vars.suppresResize = false;
		    			window.euResponsiveTriggerRespond();
					},
					error: function(x, status, e){
						js.console.log("error = " + JSON.stringify(e));
					}
				});
			}
		});
		

		$("#section-featured-content").Collapsible({
			headingSelector:	"#collapse-header-2",
			iconSelector:		".collapse-icon",
			bodySelector:		".collapse-content",
			followerSelector:	"#section-featured-partner",
			expandedClass:		'icon-arrow',
			collapsedClass:		'icon-arrow-3',
			fireFirstOpen:		function(){
				eu.europeana.vars.suppresResize = true;
				$.ajax({
					url: '/portal/index.html?fragment=featuredContent',
					dataType: 'json',
					success: function(data){
						$("#section-featured-content .collapse-content").html(data.markup);
						$("#collapse-header-2").parent().after(data.markup2);
		    			eu.europeana.vars.suppresResize = false;
		    			window.euResponsiveTriggerRespond();
					},
					error: function(x, status, e){
						js.console.log("error = " + JSON.stringify(e));
					}
				});				
			},
			toggleFn: function(){return $("#mobile-menu").is(":visible");}
		});

		
		$("#section-pinterest").Collapsible({
			headingSelector:		"#collapse-header-3",
			iconSelector:			".collapse-icon",
			bodySelector:			".collapse-content",
			executeDefaultClick:	true,
			expandedClass:			'icon-arrow',
			collapsedClass:			'icon-arrow-3',
			fireFirstOpen:			function(){
				eu.europeana.vars.suppresResize = false;
				$.ajax({
						url: '/portal/index.html?fragment=pinterest',
						dataType: 'json',
						success: function(data){
							$("#section-pinterest .collapse-content").html(data.markup);
							var carousel3Data = data.data.carousel3Data;
							
							$('#carousel-3').galleria({
								dataSource:carousel3Data,
								extend: function(e){
									
							    	var thisGallery		= this;
									
									// add ellipsis
									var doEllipsis = function(){
										var ellipsisObjects = [];
										$('.europeana-carousel-info').each(
											function(i, ob){
												ellipsisObjects[ellipsisObjects.length] = new Ellipsis($(ob));					
											}
										);
										
										setTimeout(function(){
											for(var i=0; i<ellipsisObjects.length; i++ ){
												ellipsisObjects[i].respond();
											}
										}, 1000);
										
									};

									var infoSelector = '#carousel-3 .galleria-thumbnails .galleria-image .europeana-carousel-info';
									var imgSelector = '#carousel-3 .galleria-thumbnails .galleria-image img';
									
									$(infoSelector).live('click', 
										function(){
											var clicked	= this;
											$(infoSelector).each(function(i, ob){
												if(ob == clicked){
													com.google.analytics.europeanaEventTrack("Pinterest Activity", "pinterest item", thisGallery._options.dataSource[i].link);
												}
											});
										}
									);
									
									$(imgSelector).live('click', 
										function(){
											var clicked	= this;
											$(imgSelector).each(function(i, ob){
												if(ob == clicked){
													com.google.analytics.europeanaEventTrack("Pinterest Activity", "pinterest item", thisGallery._options.dataSource[i].link);
												}
											});
										}
									);
									
									$(this).ready(function(e) {
										eu.europeana.vars.suppresResize = false;
										setTimeout(doEllipsis, 100);
									});
								} 
							});
							
							
						},
						error: function(x, status, e){
							js.console.log("error = " + JSON.stringify(e));
						}
				});
			},
			
			toggleFn: function(){return $("#mobile-menu").is(":visible");}
		});
		
		
		// iphone now fires resize events on scroll, closing all opened content: this fixes
		$(window).bind("touchmove", function(){
			eu.europeana.vars.suppresResize = true;
		});
		$(window).bind("scroll", function(){
			setTimeout(function(){
				eu.europeana.vars.suppresResize = false;
			}, 20)
		});

		
		/*
		js.loader.loadScripts([{
			file: 'addthis' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/com/addthis/' + js.min_directory,
			callback : function() {
				initAddThis();
			}
		}]);
		*/
	};
	
	$(document).ready(function(){
		init();
	});
	
	
}());