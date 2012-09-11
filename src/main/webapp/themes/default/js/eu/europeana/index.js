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

						var doEllipsis = function(){
							$('.europeana-carousel-info').ellipsis();
						};
						$(window).resize(function(){
						 	doEllipsis();
						});
						$(this).ready(function(e) {
							
							// http://pvdspek.github.com/jquery.autoellipsis/
							(function(a){function m(){if(!d){d=!0;for(var c in b)a(c).each(function(){var d,e;d=a(this),e=d.data("jqae"),(e.containerWidth!=d.width()||e.containerHeight!=d.height())&&f(d,b[c])});d=!1}}function l(a){b[a]&&(delete b[a],b.length||c&&(window.clearInterval(c),c=undefined))}function k(a,d){b[a]=d,c||(c=window.setInterval(function(){m()},200))}function j(){return this.nodeType===3}function i(b){if(b.contents().length){var c=b.contents(),d=c.eq(c.length-1);if(d.filter(j).length){var e=d.get(0).nodeValue;e=a.trim(e);if(e==""){d.remove();return!0}return!1}while(i(d));if(d.contents().length)return!1;d.remove();return!0}return!1}function h(a){if(a.contents().length){var b=a.contents(),c=b.eq(b.length-1);return c.filter(j).length?c:h(c)}a.append("");var b=a.contents();return b.eq(b.length-1)}function g(b){var c=h(b);if(c.length){var d=c.get(0).nodeValue,e=d.lastIndexOf(" ");e>-1?(d=a.trim(d.substring(0,e)),c.get(0).nodeValue=d):c.get(0).nodeValue="";return!0}return!1}function f(b,c){var d=b.data("jqae");d||(d={});var e=d.wrapperElement;e||(e=b.wrapInner("<div/>").find(">div"),e.css({margin:0,padding:0,border:0}));var f=e.data("jqae");f||(f={});var j=f.originalContent;j?e=f.originalContent.clone(!0).data("jqae",{originalContent:j}).replaceAll(e):e.data("jqae",{originalContent:e.clone(!0)}),b.data("jqae",{wrapperElement:e,containerWidth:b.width(),containerHeight:b.height()});var k=b.height(),l=(parseInt(b.css("padding-top"),10)||0)+(parseInt(b.css("border-top-width"),10)||0)-(e.offset().top-b.offset().top),m=!1,n=e;c.selector&&(n=a(e.find(c.selector).get().reverse())),n.each(function(){var b=a(this),d=b.text(),f=!1;if(e.innerHeight()-b.innerHeight()>k+l)b.remove();else{i(b);if(b.contents().length){m&&(h(b).get(0).nodeValue+=c.ellipsis,m=!1);while(e.innerHeight()>k+l){f=g(b);if(!f){m=!0,b.remove();break}i(b);if(b.contents().length)h(b).get(0).nodeValue+=c.ellipsis;else{m=!0,b.remove();break}}c.setTitle=="onEllipsis"&&f||c.setTitle=="always"?b.attr("title",d):c.setTitle!="never"&&b.removeAttr("title")}}})}var b={},c,d=!1,e={ellipsis:"...",setTitle:"never",live:!1};a.fn.ellipsis=function(b,c){var d,g;d=a(this),typeof b!="string"&&(c=b,b=undefined),g=a.extend({},e,c),g.selector=b,d.each(function(){var b=a(this);f(b,g)}),g.live?k(d.selector,g):l(d.selector);return this}})(jQuery);
							
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