(function() {
	
	'use strict';

	var initResponsiveImages = function(){
		
		
		var setup = function(){
			
			var initialSuffix = '_1.'; // smallest by default
			if(jQuery.browser.msie  && ( parseInt(jQuery.browser.version, 10) === 7 || parseInt(jQuery.browser.version, 10) === 8 )  ){
				initialSuffix = '_4.'; // largest by default
			}
			
			responsiveGallery({
				scriptClass:	'euresponsive-script',
				testClass:		'euresponsive',
				initialSuffix:	initialSuffix,

				suffixes: {
					'1': '_1.',
					'2': '_2.',
					'3': '_3.',
					'4': '_4.'
				}

			});
			
		};

		js.loader.loadScripts([{
			file: 'euresponsive' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			callback : function() {
				setup(); 
			}
		}]);

		
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
		
				var parentWidth		= $(this).parent().width();
				var imgW			= $(this).width();
				var imgH			= $(this).height();
				var ratio			= parentWidth / imgW;
			
				$(this).remove();
			
				
				var carousel	= jQuery("#carousel-1");
				var thumb		= jQuery('<div class="galleria-thumbnails-container"></div>').appendTo(carousel);
				
				carousel.css("height",  (imgH * ratio) + thumb.height() + 5 + "px");
				carousel.css("width",	"100%");
				thumb.remove();
				
				carouselData[0].themeRef = eu.europeana.vars.branding + '/js/galleria/themes/europeanax/galleria.europeanax.js';
				jQuery('#carousel-1').galleria({dataSource:carouselData});
			
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
			
			toggleFn: function(){return $("#mobile-menu").is(":visible");}
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
	
	var init = function() {
		
		jQuery("#query-input").focus();
		
		initCarousels();
		
		initResponsiveImages();
	};
	
	jQuery(document).ready(function(){
		init();
	});
	
	
}());