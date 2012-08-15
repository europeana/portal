(function() {
	
	'use strict';

	var initCarousels = function(){
		if(typeof Galleria == "undefined") {
	        window.setTimeout(initCarousels, 100);
	    }
	    else{
			Galleria.loadTheme(eu.europeana.vars.branding + '/js/galleria/themes/europeana/galleria.europeana.js');
			Galleria.configure({
					transition:		'fadeslide',		/* fade, slide, flash, fadeslide, pulse */
					carousel:		true,
					carouselSpeed:	1200,				/* transition speed */
					carouselSteps:	2,
					easing:			'galleriaOut',
					imageCrop:		false,				/* if true, make pan true */
					imagePan:		false,
					lightbox:		true,
					responsive:		true
			});
			jQuery('#carousel-1').galleria({dataSource:carouselData});
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