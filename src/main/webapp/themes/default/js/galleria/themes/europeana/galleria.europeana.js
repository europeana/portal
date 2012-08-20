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
    	var thisGallery = this;
    	/*
    	var carouselMode = false;
    	var parent = this.get('stage');
    	while(parent != null ){
    		if( $(parent).hasClass('europeana-carousel')){
    			carouselMode = true;
    		}
    		parent = parent.parentNode;
    	}
    	*/
    	var carouselId		= thisGallery.$('container').parent().attr("id");// hasClass('europeana-carousel');
    	var carouselMode	= $('#' + carouselId).hasClass('europeana-carousel');
    	
    	thisGallery._options.europeana = {
    			header:	$('#' + carouselId).parent().find('#' + carouselId + '-header'),
    			footer:	$('#' + carouselId).parent().find('#' + carouselId + '-footer')
    	};
    	var europeana = thisGallery._options.europeana;
    	if(europeana.footer){
    		europeana.footer.append( $('#' + carouselId + ' .galleria-info-description') );
    	}
    	if(europeana.header){
    		europeana.header.append( $('#' + carouselId + ' .galleria-info-title') );
    	}
    	
    	if(carouselMode){
        	thisGallery._options.europeana.thumbRatios = [];
        	for(var i=0; i<this._options.dataSource.length; i++){
        		this._options.europeana.thumbRatios[i] = null;
        	}
    	}
    	else{
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
    		$(window).resize( function() {
    			// TODO: replace this generic selector with a gallery-instance scoped one
    			//$(".galleria-container").parent().css("height", $(".galleria-container").css("height"));
    			thisGallery.$( 'container' ).parent().css("height", $(".galleria-container").css("height"));
    		});
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
    var centreItems = false;

	thisGallery._options.responsive = false; // don't trigger automatic scale

//alert(JSON.stringify(thisGallery._options))


    
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
    	var imgBoxW = thisGallery._options.europeana.imgBoxW;
    	var tParent	= thumb.parent();
		var imgBox = containerHeight;

		//Galleria.log("setThumbStyle:  imgBoxW = " + imgBoxW + ", containerWidth = " + containerWidth);

		
		tParent.css("width",	imgBoxW + "px");
		tParent.css("height",	imgBox + "px");


		imgW = thumb.width();
		imgH = thumb.height();
		
		var ratio;
		
		if(thisGallery._options.europeana.thumbRatios[index]){
			ratio = thisGallery._options.europeana.thumbRatios[index];
		}
		else{
			thisGallery._options.europeana.thumbRatios[index] = imgW / imgH;
			ratio = imgW / imgH;
		}
		
		if(imgW > imgH){ // landscape
			var marginR = parseInt(tParent.css("margin-right"));
			var newW	= imgBoxW-marginR;
			
			if(newW/ratio > containerHeight){
				var newH = containerHeight / ratio;
				var newW = newH *  ratio;
			}
			
			if(index==0){
				//Galleria.log("w=" + imgW + ", h=" + imgH + ", newW=" + newW + ", ratio = " + ratio + " --> makes new height " + (newW/ratio) + " (" + imgH + ")");
			}
			
			thumb.attr("width", newW + "px");
			thumb.attr("height", newW/ratio + "px");

			thumb.css("width", newW + "px");
			thumb.css("height", newW/ratio + "px");
		}
		else{ // portrait of perfect square
			var newH = (containerHeight - 2 * 7);
			var newW = newH * ratio;

			thumb.attr("height",	newH + "px");
			thumb.attr("width",		newW + "px");
						
			thumb.css("height",		newH + "px");
			thumb.css("width",		newW + "px");

			/*
			var newH = (containerHeight - 2 * 7);
			var ratio = imgH / newH;
			
			thumb.attr("height", newH  + "px");
			thumb.attr("width",	 imgW/ratio + "px");
						
			thumb.css("height",		newH + "px");
			thumb.css("width",		 imgW/ratio + "px");
			*/
		}

		//thumb.attr("width", "auto");
		//thumb.attr("height", "auto");

		
		//thumb.css("width",		"auto");
		//thumb.css("height",		"auto");
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
		thumbOb.outerWidth = imgBoxW + offset;
		
		/* Vertical centering of individual images */
		if(imgBox > thumb.height()){//parseInt(thumb.css("height"))){
			var top = (imgBox - parseInt(thumb.css("height")) ) / 2;
			thumb.css("top", top + "px");
		}
		
		// add info box and position
		/* TODO: Friday */
		/*
		if(tParent.find(".europeana-carousel-info").length == 0){
			tParent.append('<div class="europeana-carousel-info"><div class="title">' + dataSource[index].title + "</div><div>" + dataSource[index].description + '</div></div>');
		}
		
		// position info
		var info = tParent.find(".europeana-carousel-info");
		info.css("top",
			(	parseInt(thumb.css("top"))	+ parseInt(thumb.css("height")) )
			-	parseInt(info.css("height"))
			+	"px"
		);
		*/
		completedCallBackCount ++;	// bump callback counter
		
		if(completedCallBackCount == expectedCallBackCount){
			/* Do this once only: horizontal positioning of the full image strip to centre an image within the viewer  */			
			
			/*
			 * containerWidth
			 * imgBox (= CONTAINER WIDTH)
			 * tParent
			 * thisGallery
			 */
			if(centreItems){
				var galleryWidth	= containerWidth;
				var offsetLeft		= (galleryWidth / 2) - (imgBox / 2);
				
				offsetLeft -= parseInt(tParent.closest(".galleria-carousel").css("left"));
				offsetLeft -= parseInt(tParent.closest(".galleria-carousel .galleria-thumbnails-list").css("margin-left"));
				
				tParent.closest(".galleria-thumbnails").css("margin-left", offsetLeft + "px");
				
				tParent.closest(".galleria-thumbnails").css("margin-right", offsetLeft + "px");
	
				tParent.closest(".galleria-thumbnails").css("width",
						parseInt(tParent.closest(".galleria-thumbnails").css("width"))
						+ offsetLeft + "px");
	
				thisGallery.updateCarousel();
	
				//Galleria.log("Adding offset " + offsetLeft + " to " + thisGallery._carousel.max + " give total of " + (thisGallery._carousel.max + offsetLeft) )
				thisGallery._carousel.max += offsetLeft;
			}
			else{
				thisGallery.updateCarousel();
			}
			
			
			/* And since we have access to the dom, fix the navigation icons */
			
			var navRight	= tParent.closest(".galleria-carousel").find(".galleria-thumb-nav-right");
			var navLeft		= tParent.closest(".galleria-carousel").find(".galleria-thumb-nav-left");
			navRight.css	("top", (containerHeight - 124)/2 + "px");
			navLeft.css		("top", (containerHeight - 124)/2 + "px");    	

			/* TODO: Andy Friday change: allows win resize to reuse variable*/
			completedCallBackCount = 0;
		}
    };

    var callSetThumbStyle = function(){
    	
    	/* scaling */
    	var imgBoxW = 0;
			
    	/* update containerWidth & containerHeight */
    	containerHeight = parseInt( thisGallery.$( 'container' ).parent().css("height") );
    	containerWidth = parseInt( thisGallery.$( 'container' ).parent().css("width") );
    	
		var thumbnailsList = thisGallery.$( 'container' ).find('.galleria-thumbnails-list');//  thisGallery.$( 'thumbnails' );//.parent();//galleria-thumbnails-list'
		
		var reduce = 0;
		reduce += parseInt(thumbnailsList.css('margin-left'));
		reduce += parseInt(thumbnailsList.css('marginRight'));
		reduce += parseInt(thumbnailsList.parent().css('right'));
		reduce += parseInt(thumbnailsList.parent().css('left'));
		reduce = 80; /* hard coded: 30+30+10+10*/
			
		var maxItems = (
				(containerWidth-reduce) 
				- 
				(
						(containerWidth-reduce) % containerHeight
				)
				) / containerHeight;
		imgBoxW = (containerWidth-reduce) / maxItems;
		
		imgBoxW -= parseInt(thumbnailsList.find('.galleria-image:first').css("margin-right"));
		
		thisGallery._options.carouselSteps = maxItems;
		
    	thisGallery._options.europeana.imgBoxW = imgBoxW;

		//Galleria.log("In callSetThumbStyle:  imgBoxW = " + imgBoxW + ", containerWidth = " + containerWidth);
		
		/* end  scaling */

		
		/* end sunday scaling */
        $(thisGallery._thumbnails).each(function( i, thumb ) {

        	if(thumb.ready){

	    		thisGallery.trigger({
	    			type: Galleria.THUMBNAIL,
	                      thumbTarget: thumb.image,
	                      index: i,
	                      galleriaData: dataSource
	    		});

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
    };
    
    callSetThumbStyle();
    
	/*
	 * TODO
	 */
	$(window).resize( function() {
		
/*
		jQuery.each($('#elem').data('events'), function(i, event){
		    jQuery.each(event, function(i, handler){
		        console.log( handler.toString() );
		    });
		});
	*/	
		//return;
		
		//thisGallery.$('container').parent().css("height", thisGallery.$('container').css("height"));
		thisGallery.$('container').css("height", thisGallery.$('container').parent().css("height"));
		callSetThumbStyle();
		thisGallery._carousel.set(0);
     });
           
	
    }
});

}(jQuery));
