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
	
        /* europeana */
    	var carouselMode = false;
    	var parent = this.get('stage');
    	while(parent != null ){
    		if( $(parent).hasClass('europeana-carousel')){
    			carouselMode = true;
    		}
    		parent = parent.parentNode;
    	}
    	if(!carouselMode){
    		if(this._options.dataSource.length == 1){
				var thumbs =	this.$( 'container' ).find(".galleria-thumbnails-container");
				var stage = 		this.$( 'container' ).find(".galleria-stage");
				var info = 			this.$( 'container' ).find(".galleria-info");
				var navLeft =		this.$( 'container' ).find(".galleria-image-nav-left");
				var navRight =		this.$( 'container' ).find(".galleria-image-nav-right");

				// use extra height - move the stage down
				
				var extraHeight = parseInt(thumbs.css("height"));
				thumbs.css("height", "0px");
				stage.css("bottom", parseInt(stage.css("bottom"))	- (extraHeight / 2) + "px");
				stage.css("top",	parseInt(stage.css("top"))		+ (extraHeight / 2) + "px");

				// position info
				
				info.css("top",			"auto");
				info.css("position",	"absolute");
				info.css("width",		stage.css("width"));
				info.css("left",		stage.css("left"));
				info.css("bottom",		stage.css("bottom"));

				// hide navigation
				
				navLeft.css("display", "none");
				navRight.css("display", "none");
				
				// custom full doc options
				this.$( 'container' ).css("border-radius", "10px 10px 0px 0px");
    		}
    		//return;
    	}
    	
    	/* end europeana */
    	
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
        	/* europeana */
        	if(!carouselMode){
                if (!e.cached) {
                    this.$('loader').show().fadeTo(200, 0.4);
                }
                this.$('info').toggle( this.hasInfo() );
        	}
        	/* end europeana */


            $(e.thumbTarget).css('opacity',1).parent().siblings().children().css('opacity', 0.6);
        });

        this.bind('loadfinish', function(e) {
            this.$('loader').fadeOut(200);
        });

        
        
        
        
	/**/
        if(!carouselMode){
        	return;
        }
        
        
        

	
	var thisGallery = this;

    this.$('loader').hide();
    
	var containerHeight = parseInt( this.$( 'container' ).css("height") );
	var containerWidth = parseInt( this.$( 'container' ).css("width") );
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
	
	
	/* Styling & info */
	/*
	var navRight	= 	$(".galleria-thumbnails-container .galleria-thumb-nav-right");
	var navLeft		= 	$(".galleria-thumbnails-container .galleria-thumb-nav-left");
	navRight.css	("top", (containerHeight - 124)/2 + "px");
	navLeft.css		("top", (containerHeight - 124)/2 + "px");    	
	 */
	
	var expectedCallBackCount = 0;
	var completedCallBackCount = 0;
	
    var setThumbStyle = function(thumb, thumbOb, index){
    	var tParent	= thumb.parent();
		var margin = 0;
		var imgBox = containerHeight-margin;
		
		tParent.css("width",	imgBox + "px");
		tParent.css("height",	imgBox + "px");
		
		thumb.css("width",		"auto");
		thumb.css("height",		"auto");
		thumb.css("max-width",	 "100%");
		thumb.css("max-height",	 "100%");
		
		
		/* Gallery.updateCarousel() looks 1st for property "outerWidth" when calculating the total width of the thumbnail list.
		 * If the width is too short the last item(s) will wrap and never be viewable.
		 * If the width is too big there is spare space at the end of the scroll.
		 * 
		 * Here we help updateCarousel() by setting "outerWidth" to our box dimension plus the relative component margins and offsets. 
		 *  */
		var offset = 2;
		offset += parseInt( tParent.css("margin-right") );
		thumbOb.outerWidth = imgBox + offset;
		
		/* Vertical centering of individual images */
		if(imgBox > parseInt(thumb.css("height"))){
			var top = (imgBox - parseInt(thumb.css("height")) ) / 2;
			thumb.css("top", top + "px");
		}
		
		// add info box and position
		tParent.append('<div class="europeana-carousel-info"><div class="title">' + dataSource[index].title + "</div><div>" + dataSource[index].description + '</div></div>');
		
		// position info
		var info = tParent.find(".europeana-carousel-info");
		info.css("top",
				(
					parseInt(thumb.css("top"))
				+
					parseInt(thumb.css("height"))
				)
				-
				parseInt(info.css("height"))
				+ "px"
		);
		completedCallBackCount ++;	// bump callback counter
		
		if(completedCallBackCount == expectedCallBackCount){
			/* Do this once only: horizontal positioning of the full image strip to centre an image within the viewer  */
			// this works perfectly at gallery height 260 */
			
			var galleryWidth	= containerWidth;
			var offsetLeft		= (galleryWidth / 2) - (imgBox / 2);
			
			offsetLeft -= parseInt(tParent.closest(".galleria-carousel").css("left"));
			offsetLeft -= parseInt(tParent.closest(".galleria-carousel .galleria-thumbnails-list").css("margin-left"));
			
			tParent.closest(".galleria-thumbnails").css("margin-left", offsetLeft + "px");
			thisGallery.updateCarousel();

			/* And since we have access to the dom, fix the navigation icons */
			
			var navRight	= tParent.closest(".galleria-carousel").find(".galleria-thumb-nav-right");
			var navLeft		= tParent.closest(".galleria-carousel").find(".galleria-thumb-nav-left");
			navRight.css	("top", (containerHeight - 124)/2 + "px");
			navLeft.css		("top", (containerHeight - 124)/2 + "px");    	

		}
    };

    $(this._thumbnails).each(function( i, thumb ) {
    	if(thumb.ready){
    	}
    	else{
    		expectedCallBackCount ++;
    		thisGallery.bind("thumbnail", function(e) {
    			if(e.index == i){
        	        setThumbStyle($(e.thumbTarget), thumb, i);
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
