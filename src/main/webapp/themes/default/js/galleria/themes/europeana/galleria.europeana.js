/**
 * Galleria Classic Theme 2012-08-08
 * http://galleria.io
 *
 * Licensed under the MIT license
 * https://raw.github.com/aino/galleria/master/LICENSE
 *
 */

(function($) {

/*global jQuery, Galleria */

Galleria.addTheme({
    name: 'europeana',
    author: 'Andy',
    css: 'galleria.europeana.css',
    defaults: {
        transition: 'slide',
        thumbCrop:  'height',

        // set this to false if you want to show the caption all the time:
        _toggleInfo: false
    },
    init: function(options) {
        Galleria.requires(1.28, 'This version of Classic theme requires Galleria 1.2.8 or later');
	
        // add some elements
        this.addElement('info-link','info-close');
        this.append({
            'info' : ['info-link','info-close']
        });

        // cache some stuff
        var info = this.$('info-link,info-close,info-text'),
            touch = Galleria.TOUCH,
            click = touch ? 'touchstart' : 'click';

        // show loader & counter with opacity
	if(options.carouselMode != true){
  	      this.$('loader,counter').show().css('opacity', 0.4);
	}

        // some stuff for non-touch browsers
        if (! touch ) {
            this.addIdleState( this.get('image-nav-left'), { left:-50 });
            this.addIdleState( this.get('image-nav-right'), { right:-50 });
            this.addIdleState( this.get('counter'), { opacity:0 });
        }

        // toggle info
        if ( options._toggleInfo === true ) {
            info.bind( click, function() {
                info.toggle();
            });
        } else {
            info.show();
            this.$('info-link, info-close').hide();
        }

        // bind some stuff
        this.bind('thumbnail', function(e) {

            if (! touch ) {
                // fade thumbnails
                $(e.thumbTarget).css('opacity', 0.6).parent().hover(function() {
                    $(this).not('.active').children().stop().fadeTo(100, 1);
                }, function() {
                    $(this).not('.active').children().stop().fadeTo(400, 0.6);
                });

                if ( e.index === this.getIndex() ) {
                    $(e.thumbTarget).css('opacity',1);
                }
            } else {
                $(e.thumbTarget).css('opacity', this.getIndex() ? 1 : 0.6);
            }
        });

        this.bind('loadstart', function(e) {
            //if (!e.cached) {
            //    this.$('loader').show().fadeTo(200, 0.4);
            //}

            this.$('info').toggle( this.hasInfo() );

            $(e.thumbTarget).css('opacity',1).parent().siblings().children().css('opacity', 0.6);
        });

        this.bind('loadfinish', function(e) {
            this.$('loader').fadeOut(200);
        });

        
        
        
        
	/**/

        
        
        
	var carouselMode = false;
	var parent = this.get('stage');
	while(parent != null ){
		if( $(parent).hasClass('europeana-carousel')){
			carouselMode = true;
		}
		parent = parent.parentNode;
	}
	if(!carouselMode){
		return;
	}
	var thisGallery = this;

    this.$('loader').hide();
    
    

	var containerHeight = parseInt( this.$( 'container' ).css("height") );
	//alert(containerHeight)
	
	var dataSource = this._options.dataSource;
	
	/* Action */
	this.$( 'thumbnails' ).find('.galleria-image').each(function(i, ob){
		$(ob).unbind('click');
		$(ob).click(function(e, a){
			alert(dataSource[i].link);
       		e.stopPropagation();
			window.open(dataSource[i].link, "_new");
		});

	});
	
	/* Style */
	var navRight	= 	$(".galleria-thumbnails-container .galleria-thumb-nav-right");
	var navLeft		= 	$(".galleria-thumbnails-container .galleria-thumb-nav-left");
	navRight.css	("top", (containerHeight - 124)/2 + "px");
	navLeft.css		("top", (containerHeight - 124)/2 + "px");    	

    var setThumbStyle = function(thumb, thumbOb){
    	var tParent	= thumb.parent();
		var margin = 0;
		var imgBox = containerHeight-margin;
		
		tParent.css("width",	imgBox + "px");
		tParent.css("height",	imgBox + "px");
		
		thumb.css("width",		"auto");
		thumb.css("height",		"auto");
		thumb.css("max-width",	imgBox + "px");
		thumb.css("max-height",	imgBox + "px");
		
		thumbOb.outerWidth = imgBox+7; // this '8' compensates a margin(?) that causes the last element to wrap (with 16 items)
		
		// vertical centering
		if(imgBox > parseInt(thumb.css("height"))){
			var top = (imgBox - parseInt(thumb.css("height")) ) / 2;
			thumb.css("top", top + "px");
		}
		thisGallery.updateCarousel();
    };

    $(this._thumbnails).each(function( i, thumb ) {
    	if(thumb.ready){
    	}
    	else{
    		thisGallery.bind("thumbnail", function(e) {
    			if(e.index == i){
        	        setThumbStyle($(e.thumbTarget), thumb);
    			}
    	    });
    	}
    });
    
	/*
	 * TODO
	 * 
	$(window).resize( function() {
		var x = $(".galleria-container").parent().css("height");
		thisGallery.$( 'container' ).css("height", x);
		thisGallery.rescale();
     });
     */      
	
    }
});

}(jQuery));
