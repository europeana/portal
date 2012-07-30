(function() {

  'use strict';
  
  var index = {
	
	carouselInit : function() {
	  
	  jQuery('#index-carousel').imagesLoaded(function() {
		this.rCarousel();
	  });	
	  
	},
	
	
	loadDependencies : function() {
	  
	  js.loader.loadScripts([{
		file : 'jquery.imagesloaded.min.js',
		path : 'themes/common/js/com/jquery/plugins/'
	  }]);
	  
	  js.loader.loadScripts([{
		file : 'rcarousel.js',
		path : 'themes/common/js/com/gmtplusone/',
		dependencies : [ 'jquery.imagesloaded.min.js' ]
	  }]);
	  
	},
	
	
	init : function() {
	  
	  index.loadDependencies();
	  index.carouselInit();
	  
	}
	
  };
  
  index.loadDependencies();
  
  
}());