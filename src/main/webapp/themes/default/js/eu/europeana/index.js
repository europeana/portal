(function() {
	
	'use strict';

	var initCarousels = function(){
		if(typeof jQuery("#carousel-1").galleria == "undefined") {
	        window.setTimeout(initCarousels, 200);
	    }
	    else{
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
					idleMode:		true
			});
			jQuery('#carousel-1').galleria({dataSource:carouselData});
			jQuery('#carousel-2').galleria({dataSource:carousel2Data});
			jQuery('#carousel-3').galleria({dataSource:carousel3Data});
	    }		
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