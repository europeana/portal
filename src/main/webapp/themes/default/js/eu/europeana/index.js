(function() {
	
	'use strict';

	var initCarousels = function(){

		//Galleria.loadTheme(eu.europeana.vars.branding + '/js/galleria/themes/europeanax/galleria.europeanax.js');
		Galleria.loadTheme(eu.europeana.vars.branding + '/js/galleria/themes/europeana/galleria.europeana.js');
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
				debug:			false
		});
			
		/*
		window.scaleGalleria = function($loadedImg){
			$("#carousel-1-scale-image").show();

			var parentWidth		= $loadedImg.parent().width();
			var imgW			= $loadedImg.width();
			var imgH			= $loadedImg.height();
			var ratio			= parentWidth / imgW;
			$("#carousel-1-scale-image").hide();

			$("#carousel-1").css("height",  (imgH * ratio) + 50 + 5 + "px");

		};
		*/
		$("#carousel-1-scale-image").one('load', function() {
			
			var parentWidth		= $(this).parent().width();
			var imgW			= $(this).width();
			var imgH			= $(this).height();
			var ratio			= parentWidth / imgW;
			
			$("#carousel-1-scale-image").hide();
			$("#carousel-1").css("height",  (imgH * ratio) + 50 + 5 + "px");

			jQuery('#carousel-1').galleria({dataSource:carouselData});
			
			}).each(function() {
			  if(this.complete){
				  $(this).load();
			  }
		});
		
		
		
		// test fulldoc carousel here
		var carouselFDData = [{"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+1%2F1%2F1M16_B145043_151.jpg&size=FULL_DOC","title":"Porträtt"},{"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+3%2F18%2F1M16_B145142_572.jpg&size=FULL_DOC","title":"Stadsvy"},{"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+1%2F1%2F1M16_B145043_151.jpg&size=FULL_DOC","title":"Porträtt"},{"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+3%2F18%2F1M16_B145142_572.jpg&size=FULL_DOC","title":"Stadsvy"},{"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+1%2F1%2F1M16_B145043_151.jpg&size=FULL_DOC","title":"Porträtt"},{"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+3%2F18%2F1M16_B145142_572.jpg&size=FULL_DOC","title":"Stadsvy"},{"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+1%2F1%2F1M16_B145043_151.jpg&size=FULL_DOC","title":"Porträtt"},{"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+3%2F18%2F1M16_B145142_572.jpg&size=FULL_DOC","title":"Stadsvy"}];
		
		jQuery('#carousel-fd').css("height", "350px");
		
		Galleria.run('#carousel-fd', {
			transition:		'fadeslide',		/* fade, slide, flash, fadeslide, pulse */
			carousel:		true,
			carouselSpeed:	1200,				/* transition speed */
			carouselSteps:	1,
			easing:			'galleriaOut',
			imageCrop:		false,				/* if true, make pan true */
			imagePan:		false,
			lightbox:		true,
			responsive:		true,
			dataSource:		carouselFDData,
			thumbnails: 	carouselFDData.length>1
		});
			

		// Make sections collapsible
		var toggleBreakpoint = 800;
		
		jQuery("#section-blog").Collapsible({
			headingSelector:	"#collapse-header",
			iconSelector:		".collapse-icon",
			bodySelector:		".collapse-content",
			
			expandedClass:		'icon-arrow',
			collapsedClass:		'icon-arrow-3',
			
			toggleBreakpoint:	toggleBreakpoint
		});
		
		jQuery("#section-featured-content").Collapsible({
			headingSelector:	"#collapse-header",
			iconSelector:		".collapse-icon",
			bodySelector:		".collapse-content",
			followerSelector:	"#section-featured-partner",
			expandedClass:		'icon-arrow',
			collapsedClass:		'icon-arrow-3',
			fireFirstOpen:		function(){
				jQuery('#carousel-2').galleria({dataSource:carousel2Data});					
			},
			toggleBreakpoint:	toggleBreakpoint
		});

		jQuery("#section-pinterest").Collapsible({
			headingSelector:	"#collapse-header",
			iconSelector:		".collapse-icon",
			bodySelector:		".collapse-content",

			expandedClass:		'icon-arrow',
			collapsedClass:		'icon-arrow-3',
			fireFirstOpen:		function(){
				jQuery('#carousel-3').galleria({dataSource:carousel3Data});					
			},
			toggleBreakpoint:	toggleBreakpoint
		});

	};
	
	var init = function() {
		
		
		jQuery("#query-input").focus(function(){
			
			/* conditional load */
			
			europeana_bootstrap.common.loadResultSizer(
					function(){ console.log("in callback for index.js loadResultSizer"); }
			);
		});
		
		initCarousels();
		
	};
	
	jQuery(document).ready(function(){
		init();
	});
	
	
}());