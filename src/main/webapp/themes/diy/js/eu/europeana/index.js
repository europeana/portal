(function() {

  'use strict';
  
  var index = {
	
	carousel_width : $('#index-carousel ul').eq(0).width(),
	image_width : $('#index-carousel img').eq(0).width(),
	
	
	openCarousel : function() {
	  
	  jQuery('#index-carousel').rCarousel({ item_width_is_container_width : false });
	  
	},
	
	
	createItemHtml : function( data ) {
	  
	  var item,
		  i,
		  ii = data.items.length,
		  html = '';
	  
	  for ( i = 0; i < ii; i += 1 ) {
		
		item = data.items[i];
		
		html +=
		  '<li>' +
			'<a href="' + item.anchorUrl + '" title="' + item.anchorTitle + '" target="'+ item.anchorTarget + '">' +
			  '<img src="' + item.imgUrl + '" alt="' + item.imgAlt + '" width="' + item.imgWidth + '" height="' + item.imgHeight + '"/>' +
			  '<div class="caption">' +
				item.anchorTitle +
			  '</div>' +
			'</a>' +
		  '</li>';
		
	  }
	  
	  return html;
	  
	},
	
	
	placeImages : function( data ) {
	  
	  $('#index-carousel ul').append( this.createItemHtml( data ) );
	  this.openCarousel();
	  
	},
	
	
	getImages : function() {
	  
	  $.ajax({
		url : '/portal2/carousel.json',
		data : { start : $('#index-carousel li').length, rows : 2 },
		success : function( data ) { index.placeImages( data ); }
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
		dependencies : [ 'jquery.imagesloaded.min.js' ],
		callback : function() { index.getImages(); }
	  }]);
	  
	},
	
	
	init : function() {
	  
	  index.loadDependencies();
	  
	}
	
	
  };
  
  index.init();
  
  
}());