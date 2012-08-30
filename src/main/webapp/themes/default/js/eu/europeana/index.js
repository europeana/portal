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
			
			toggleBreakpoint:	eu.europeana.vars.breakpoints.collapse_on_min
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
			toggleBreakpoint:	eu.europeana.vars.breakpoints.collapse_on_min
		});

		jQuery("#section-pinterest").Collapsible({
			headingSelector:	"#collapse-header-3",
			iconSelector:		".collapse-icon",
			bodySelector:		".collapse-content",

			expandedClass:		'icon-arrow',
			collapsedClass:		'icon-arrow-3',
			fireFirstOpen:		function(){
				jQuery('#carousel-3').galleria({dataSource:carousel3Data});					
			},
			toggleBreakpoint:	eu.europeana.vars.breakpoints.collapse_on_min
		});

	};
	
	function setupMobileMenu(){
		/* constructor */
		var mobileMenu = function(cmp){
			var self 	= this;
			self.cmp	= cmp;
			self.ops	= cmp.find(".item");

			cmp.click(function(e){
				self.cmp.toggleClass("active");
				$('.search-menu' ).removeClass("active");
				e.stopPropagation();
			});
		};

		$(".mobile-menu").each(function(i, ob){
			new mobileMenu($(ob));
		});
		
	}
	
	/*
	function setupSearchMenu(){
		
		var searchMenu = function(cmp){
			var self 	= this;
			self.cmp	= cmp;
			self.ops	= cmp.find(".item");

			console.log("search item")

			cmp.click(function(e){
				self.cmp.toggleClass("active");
				$('.mobile-menu' ).removeClass("active");
				e.stopPropagation();
				
				console.log("search click")
			});
			
		};

		$(".search-menu").each(function(i, ob){
			console.log("search menu consruct")

			new searchMenu($(ob));
		});

		
	}
	*/

	var init = function() {
		var globalCopyOfBreakpoint = 800;
		if(jQuery("body").width()<globalCopyOfBreakpoint){
			setupMobileMenu();
		}
		
		/* 
		$(document).click( function(){
			$('.mobile-menu' ).removeClass("active");
			$('.search-menu' ).removeClass("active");
		});
		 */
		
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