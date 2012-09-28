(function() {
	
	'use strict';

	var initResponsiveImages = function(){

		var setup = function(){
			var initialSuffix = '_1.'; // smallest by default
			if(jQuery.browser.msie  && ( parseInt(jQuery.browser.version, 10) === 7 || parseInt(jQuery.browser.version, 10) === 8 )  ){
				initialSuffix = '_4.'; // largest by default
			}
			new responsiveGallery({
				imgSelector		: '.responsive',
				initialSuffix	: initialSuffix,
				suffixes: {
					'1': '_1.',
					'2': '_2.',
					'3': '_3.',
					'4': '_4.'
				}
			});
		};
		setup();
	};
	
	var initCarousels = function(){

		Galleria.loadTheme(eu.europeana.vars.branding + '/js/galleria/themes/europeanax/galleria.europeanax.js');
		//Galleria.loadTheme(eu.europeana.vars.branding + '/js/galleria/themes/europeana/galleria.europeana.js');
		//Galleria.loadTheme(eu.europeana.vars.branding + '/js/galleria/themes/classic/galleria.classic.js');
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
				idleMode:		true,
				popupLinks:		true,
				debug:			false
		});
			

		
		$('<img src="' + carouselData[0].image + '" style="visibility:hidden"/>').appendTo("#carousel-1").one('load',
			function() {
				var imgW			= $(this).width();
				var imgH			= $(this).height();
				$(this).remove();

				var carousel		= jQuery("#carousel-1");
				var parentWidth		= carousel.width();
				
				var ratio			= imgW / imgH;
				var thumb			= jQuery('<div class="galleria-thumbnails-container"></div>').appendTo(carousel);
				
				carousel.css("height",  (parentWidth/ratio) + thumb.height() + 5 + "px");
				
				carousel.css("width",	"100%");
				thumb.remove();
				
				carouselData[0].themeRef = eu.europeana.vars.branding + '/js/galleria/themes/europeanax/galleria.europeanax.js';
				jQuery('#carousel-1').galleria({
					dataSource:carouselData,
					autoplay:5000
				});
			
			}).each(function() {
				if(this.complete){
					$(this).load();
				}
		});
		
	
		// Make sections collapsible
		jQuery("#section-blog").Collapsible({
			headingSelector:	"#collapse-header-1",
			iconSelector:		".collapse-icon",
			bodySelector:		".collapse-content",
			
			expandedClass:		'icon-arrow',
			collapsedClass:		'icon-arrow-3',
			
			toggleFn: 			window.showingPhone
		});
		
		jQuery("#section-featured-content").Collapsible({
			headingSelector:	"#collapse-header-2",
			iconSelector:		".collapse-icon",
			bodySelector:		".collapse-content",
			followerSelector:	"#section-featured-partner",
			expandedClass:		'icon-arrow',
			collapsedClass:		'icon-arrow-3',
			fireFirstOpen:		function(){
				if(typeof carousel2Data != 'undefined'){
					jQuery('#carousel-2').galleria({dataSource:carousel2Data});										
				}
			},
			
			toggleFn: function(){return $("#mobile-menu").is(":visible");}
		});

		jQuery("#section-pinterest").Collapsible({
			headingSelector:	"#collapse-header-3",
			iconSelector:		".collapse-icon",
			bodySelector:		".collapse-content",

			expandedClass:		'icon-arrow',
			collapsedClass:		'icon-arrow-3',
			fireFirstOpen:		function(){
				
				jQuery('#carousel-3').galleria({
					dataSource:carousel3Data,
					extend: function(e){
						// add ellipsis
						var doEllipsis = function(){
							var ellipsisObjects = [];
							jQuery('.europeana-carousel-info').each(
								function(i, ob){
									ellipsisObjects[ellipsisObjects.length] = new Ellipsis($(ob));					
								}
							);
							$(window).bind('resize', function(){
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
			},
			
			toggleFn: function(){return $("#mobile-menu").is(":visible");}
		});

	};
	
	var initAddThis = function(){
		var url = jQuery('head link[rel="canonical"]').attr('href'),
			title = jQuery('head title').html(),
			description = jQuery('head meta[name="description"]').attr('content');
			window.addthis_config = com.addthis.createConfigObject({
				pubid : eu.europeana.vars.addthis_pubid,
				ui_language: 'en', // eu.europeana.vars.locale,
				data_ga_property: eu.europeana.vars.gaId,
				data_ga_social : true,
				data_track_clickback: true,
				ui_click: true,		// disable hover
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

		
	};
	
	var init = function() {
		
		jQuery("#query-input").focus();
		
		initCarousels();
		
		initResponsiveImages();
		
		
		js.loader.loadScripts([{
			file: 'addthis' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/com/addthis/' + js.min_directory,
			callback : function() {
				initAddThis();
			}
		}]);
	};
	
	jQuery(document).ready(function(){
		init();
	});
	
	
}());