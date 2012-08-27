(function() {
	
	'use strict';

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
				thumb.remove();
				
				carouselData[0].themeRef = eu.europeana.vars.branding + '/js/galleria/themes/europeanax/galleria.europeanax.js'
				jQuery('#carousel-1').galleria({dataSource:carouselData});
			
			}).each(function() {
				if(this.complete){
					$(this).load();
				}
		});
		
		
		/*
		$("#carousel-1-scale-image").one('load', function() {
			
			var parentWidth		= $(this).parent().width();
			var imgW			= $(this).width();
			var imgH			= $(this).height();
			var ratio			= parentWidth / imgW;
			
			$("#carousel-1-scale-image").css("display", "none");
			$("#carousel-1").css("height",  (imgH * ratio) + 50 + 5 + "px");

			jQuery('#carousel-1').galleria({dataSource:carouselData});
			
			}).each(function() {
			  if(this.complete){
				  $(this).load();
			  }
		});
		*/
		
	
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
	
	function setupMobileMenu(){
		/* constructor */
		var mobileMenu = function(cmp){
			var self 	= this;
			self.cmp	= cmp;
			self.ops	= cmp.find(".item");

			cmp.click(function(){
				self.cmp.toggleClass("active");
			});
		};

		$(".mobile-menu").each(function(i, ob){
			new mobileMenu($(ob));
		});
	}
	
	var init = function() {
		var globalCopyOfBreakpoint = 800;
		if(jQuery("body").width()<globalCopyOfBreakpoint){
			setupMobileMenu();
		}
		
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