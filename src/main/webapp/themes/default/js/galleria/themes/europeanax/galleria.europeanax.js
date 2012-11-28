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
    name: 'europeanax',
    author: 'Andy MacLean',
    css: eu.europeana.vars.galleria.css,
    defaults: {
        transition: 'slide',
        thumbCrop:  'height',

        // set this to false if you want to show the caption all the time:
        _toggleInfo: false
    },
    init: function(options) {
        Galleria.requires(1.28, 'This version of Classic theme requires Galleria 1.2.8 or later');
	
        /* europeana */
    	var thisGallery		= this;
    	var dataSource		= this._options.dataSource;
    	var carouselId		= this.$('container').parent().attr("id");
    	var carouselMode	= $('#' + carouselId).hasClass('europeana-carousel');
    	var borderedMode	= $('#' + carouselId).hasClass('europeana-bordered');
    	

    	thisGallery._options.europeana = {};
    	var europeana = thisGallery._options.europeana;
    	
    	
    	var info			= thisGallery.$( 'container' ).find(".galleria-info");
		var navLeft			= thisGallery.$( 'container' ).find(".galleria-image-nav-left");
		var navRight		= thisGallery.$( 'container' ).find(".galleria-image-nav-right");
		var thumbs			= thisGallery.$( 'container' ).find('.galleria-thumbnails'); 
		var stage			= thisGallery.$( 'container' ).find(".galleria-stage");
		var thumbNavLeft	= thisGallery.$( 'container' ).find(".galleria-thumb-nav-left");
		var thumbNavRight	= thisGallery.$( 'container' ).find(".galleria-thumb-nav-right");
		

		
		/* custom styling of nav arrows */
		navLeft	.addClass("icon-arrow-4");
		navRight.addClass("icon-arrow-2");
		
		/* custom styling of nav arrows */
		thumbNavLeft.addClass("icon-arrow-4");
		thumbNavRight.addClass("icon-arrow-2");

		
		var rerunOnResize = function(){
			thisGallery._run();
			
	        $(thisGallery._thumbnails).each(function( i, thumb ) {
	        	if(thumb.ready){
		    		thisGallery.trigger({
		    			type: Galleria.THUMBNAIL,
		                      thumbTarget: thumb.image,
		                      index: i,
		                      galleriaData: dataSource
		    		});
	        	}
	        });	
	        
		};
		
		
    	if(carouselMode){
        	var headerSelector = '#' + carouselId + '-header';
        	var footerSelector = '#' + carouselId + '-footer';
        	europeana.header = 	$('#' + carouselId).parent().find(headerSelector);
        	europeana.footer = 	$('#' + carouselId).parent().find(footerSelector);
			thisGallery._options.europeana.similarItems = thisGallery.$( 'container' ).parent().parent().attr('id') == "similar-content";
    		
        	europeana.thumbRatios = [];
        	for(var i=0; i<dataSource.length; i++){
        		europeana.thumbRatios[i] = null;
        	}
        	
			europeana.setActive = function(index){
				thumbs.find('.galleria-image').removeClass('active');
				thumbs.find('.galleria-image').eq(index).addClass('active');

		    	if(europeana.header && europeana.footer && europeana.header.length > 0 && europeana.footer.length > 0){
		    		thisGallery._options.europeana.header.setAccelerator(Math.floor( index / thisGallery._options.carouselSteps));
		    	}
			};
			
			thumbs.find('.galleria-image').each(function(i, e){
				$('<div class="europeana-carousel-info">' + dataSource[i].title + '</div>').appendTo(e);
				
				$(e).bind("mouseover", function(){					
					thisGallery.trigger(Galleria.IDLE_EXIT);
				});

			});
			
			thisGallery.bind("idle_enter",function(e) {
			    thumbNavRight.hide();
			    thumbNavLeft.hide();                      
			});
			
			thisGallery.bind("idle_exit",function(e) {
				thumbNavRight.show();
				thumbNavLeft.show();                      
			});
			
						
			thisGallery._options.responsive = false; /* disable default responsive handling (because it's rubbish) and use custom fns */
		    
		    this.$('loader').hide();
		    
			var containerHeight	= parseInt( this.$( 'container' ).css("height") );
			var containerWidth	= parseInt( this.$( 'container' ).css("width") );
			
			
			/* Styling & info:
			 * 
			 * Size the images according to container width.
			 * Calculate carousel step
			 * Create the accelerators accordingly
			 *  */
			var expectedCallBackCount = 0;
			var completedCallBackCount = 0;
			
			var setThumbStyle = function(thumb, thumbOb, index){	// called for each thumbnail once loaded
				var imgBoxW		= thisGallery._options.europeana.imgBoxW;
				var tParent		= thumb.parent();
				var imgBoxH		= containerHeight;
				
				tParent.css("width",	imgBoxW + "px");
				tParent.css("height",	imgBoxH + "px");
				
				var imgMargin	= thisGallery._options.europeana.imgMargin;
				tParent.css("margin",	"0 " + imgMargin + "px");

				
				// image sizing 
				thumb.removeAttr('width');
				thumb.removeAttr('height');
				thumb.css('width',	'auto');
				thumb.css('height',	'auto');

				imgW = thumb.width();
				imgH = thumb.height();
				
				
				thumb.css("max-width",	 "100%");
				thumb.css("max-height",	 "100%");
				
				/* Galleria.updateCarousel() looks 1st for property "outerWidth" when calculating the total width of the thumbnail list.
				 * If the width is too short the last item(s) will wrap and never be viewable.
				 * If the width is too big there is spare space at the end of the scroll.
				 * 
				 * Here we help updateCarousel() by setting "outerWidth" to our box dimension plus the relative component margins and offsets. 
				 *
				 * thumb.outerWidth || $( thumb.container ).outerWidth( true );
				 *  
				 *  */

				thumbOb.outerWidth = tParent.outerWidth(true);

				/* Vertical centering of individual images */
				if(imgBoxH > thumb.height()){
					var top = (imgBoxH - parseFloat(thumb.css("height")) ) / 2;
					thumb.css("top", top + "px");
				}

				completedCallBackCount ++;	// bump callback counter
				
				if(completedCallBackCount == expectedCallBackCount){
					
					/* Do this once only  */
					thisGallery.updateCarousel();

					/* vertically centre the navigation icons */
					var offsetTop = (containerHeight - Math.max( parseInt(thumbNavLeft.height()), parseInt(thumbNavRight.height()) ) ) / 2;
					
					thumbNavLeft.css	("top", offsetTop + "px");
					thumbNavRight.css	("top", offsetTop + "px");

					completedCallBackCount = 0;
				}
		    };

		    var calculateLayout = function(){	// called once
		    	
		    	/* scaling */
		    	var imgBoxW = 0;
					
		    	/* update containerWidth & containerHeight */
		    	containerHeight = parseInt( thisGallery.$( 'container' ).parent().css("height") );
		    	containerWidth = parseInt( thisGallery.$( 'container' ).parent().css("width") );
		    	
				var thumbnailsList	= thisGallery.$( 'container' ).find('.galleria-thumbnails-list');
				var maxItems		= 0;
				var	itemWidth		= thisGallery._options.europeana.similarItems ? eu.europeana.fulldoc.getCarousel2Dimensions().w : 200;
				
				itemWidth = Math.max(itemWidth, 150);	// leave room for text where image is extremely narrow
				
				var	minMargin	= 9;	// min margin pixels
				 	maxItems	= parseInt(		(containerWidth + 2 * minMargin) / (itemWidth + (2 * minMargin))		);
					maxItems	= Math.max(maxItems, 1); // avoid division by zero!

				if(window.showingPhone()){
					maxItems = 1;
				}
					
				var newMargin	= (containerWidth - (maxItems * itemWidth))   / (( Math.max(1,  maxItems-1)) * 2);

				imgBoxW = Math.round(itemWidth);
				
				
				/* store steps and width data in europeana config - adjust thumnail list (negative) margin to override first/last margins */
				thisGallery._options.europeana.imgMargin	= newMargin;
				thisGallery._options.carouselSteps			= maxItems;
		    	thisGallery._options.europeana.imgBoxW		= imgBoxW;

				var imgMargin = newMargin;
				
				if(!window.showingPhone()){
					thumbnailsList.css("margin",	"0 -" + imgMargin + "px");
				}
				else{
					thumbnailsList.css("margin",	"0 0px");
				}

		    	/* iterate thumbs & call setStyle on each */

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
		    calculateLayout();
		    

	        // CLICK HANDLING
	        
	    	this.$( 'thumbnails' ).find('.galleria-image').each(function(i, ob){
	    		$(ob).unbind('click');
	    		$(ob).bind("click", function(){
	    			if(dataSource[i].linkTarget){
	        			window.open(dataSource[i].link, dataSource[i].linkTarget);    				
	    			}
	    			else{
	        			window.location = dataSource[i].link;
	    			}
	    		});

	        });
	        
	   	
	        /*
	        * touchSwipe - jQuery Plugin
	        * https://github.com/mattbryson/TouchSwipe-Jquery-Plugin
	        * http://labs.skinkers.com/touchSwipe/
	        * http://plugins.jquery.com/project/touchSwipe
	        *
	        * Copyright (c) 2010 Matt Bryson (www.skinkers.com)
	        * Dual licensed under the MIT or GPL Version 2 licenses.
	        *
	        * $version: 1.3.3
	        */
	    	
	    	if( ! $("html").hasClass('ie8') ){
		        (function(g){function P(c){if(c&&void 0===c.allowPageScroll&&(void 0!==c.swipe||void 0!==c.swipeStatus))c.allowPageScroll=G;c||(c={});c=g.extend({},g.fn.swipe.defaults,c);return this.each(function(){var b=g(this),f=b.data(w);f||(f=new W(this,c),b.data(w,f))})}function W(c,b){var f,p,r,s;function H(a){var a=a.originalEvent,c,Q=n?a.touches[0]:a;d=R;n?h=a.touches.length:a.preventDefault();i=0;j=null;k=0;!n||h===b.fingers||b.fingers===x?(r=f=Q.pageX,s=p=Q.pageY,y=(new Date).getTime(),b.swipeStatus&&(c= l(a,d))):t(a);if(!1===c)return d=m,l(a,d),c;e.bind(I,J);e.bind(K,L)}function J(a){a=a.originalEvent;if(!(d===q||d===m)){var c,e=n?a.touches[0]:a;f=e.pageX;p=e.pageY;u=(new Date).getTime();j=S();n&&(h=a.touches.length);d=z;var e=a,g=j;if(b.allowPageScroll===G)e.preventDefault();else{var o=b.allowPageScroll===T;switch(g){case v:(b.swipeLeft&&o||!o&&b.allowPageScroll!=M)&&e.preventDefault();break;case A:(b.swipeRight&&o||!o&&b.allowPageScroll!=M)&&e.preventDefault();break;case B:(b.swipeUp&&o||!o&&b.allowPageScroll!= N)&&e.preventDefault();break;case C:(b.swipeDown&&o||!o&&b.allowPageScroll!=N)&&e.preventDefault()}}h===b.fingers||b.fingers===x||!n?(i=U(),k=u-y,b.swipeStatus&&(c=l(a,d,j,i,k)),b.triggerOnTouchEnd||(e=!(b.maxTimeThreshold?!(k>=b.maxTimeThreshold):1),!0===D()?(d=q,c=l(a,d)):e&&(d=m,l(a,d)))):(d=m,l(a,d));!1===c&&(d=m,l(a,d))}}function L(a){a=a.originalEvent;a.preventDefault();u=(new Date).getTime();i=U();j=S();k=u-y;if(b.triggerOnTouchEnd||!1===b.triggerOnTouchEnd&&d===z)if(d=q,(h===b.fingers||b.fingers=== x||!n)&&0!==f){var c=!(b.maxTimeThreshold?!(k>=b.maxTimeThreshold):1);if((!0===D()||null===D())&&!c)l(a,d);else if(c||!1===D())d=m,l(a,d)}else d=m,l(a,d);else d===z&&(d=m,l(a,d));e.unbind(I,J,!1);e.unbind(K,L,!1)}function t(){y=u=p=f=s=r=h=0}function l(a,c){var d=void 0;b.swipeStatus&&(d=b.swipeStatus.call(e,a,c,j||null,i||0,k||0,h));if(c===m&&b.click&&(1===h||!n)&&(isNaN(i)||0===i))d=b.click.call(e,a,a.target);if(c==q)switch(b.swipe&&(d=b.swipe.call(e,a,j,i,k,h)),j){case v:b.swipeLeft&&(d=b.swipeLeft.call(e, a,j,i,k,h));break;case A:b.swipeRight&&(d=b.swipeRight.call(e,a,j,i,k,h));break;case B:b.swipeUp&&(d=b.swipeUp.call(e,a,j,i,k,h));break;case C:b.swipeDown&&(d=b.swipeDown.call(e,a,j,i,k,h))}(c===m||c===q)&&t(a);return d}function D(){return null!==b.threshold?i>=b.threshold:null}function U(){return Math.round(Math.sqrt(Math.pow(f-r,2)+Math.pow(p-s,2)))}function S(){var a;a=Math.atan2(p-s,r-f);a=Math.round(180*a/Math.PI);0>a&&(a=360-Math.abs(a));return 45>=a&&0<=a?v:360>=a&&315<=a?v:135<=a&&225>=a? A:45<a&&135>a?C:B}function V(){e.unbind(E,H);e.unbind(F,t);e.unbind(I,J);e.unbind(K,L);}var O=n||!b.fallbackToMouseEvents,E=O?"touchstart":"mousedown",I=O?"touchmove":"mousemove",K=O?"touchend":"mouseup",F="touchcancel",i=0,j=null,k=0,e=g(c),d="start",h=0,y=p=f=s=r=0,u=0;try{e.bind(E,H),e.bind(F,t)}catch(P){g.error("events not supported "+E+","+F+" on jQuery.swipe")}this.enable=function(){e.bind(E,H);e.bind(F,t);return e};this.disable=function(){V();return e};this.destroy=function(){V();e.data(w,null); return e}}var v="left",A="right",B="up",C="down",G="none",T="auto",M="horizontal",N="vertical",x="all",R="start",z="move",q="end",m="cancel",n="ontouchstart"in window,w="TouchSwipe";g.fn.swipe=function(c){var b=g(this),f=b.data(w);if(f&&"string"===typeof c){if(f[c])return f[c].apply(this,Array.prototype.slice.call(arguments,1));g.error("Method "+c+" does not exist on jQuery.swipe")}else if(!f&&("object"===typeof c||!c))return P.apply(this,arguments);return b};g.fn.swipe.defaults={fingers:1,threshold:75, maxTimeThreshold:null,swipe:null,swipeLeft:null,swipeRight:null,swipeUp:null,swipeDown:null,swipeStatus:null,click:null,triggerOnTouchEnd:!0,allowPageScroll:"auto",fallbackToMouseEvents:!0};g.fn.swipe.phases={PHASE_START:R,PHASE_MOVE:z,PHASE_END:q,PHASE_CANCEL:m};g.fn.swipe.directions={LEFT:v,RIGHT:A,UP:B,DOWN:C};g.fn.swipe.pageScroll={NONE:G,HORIZONTAL:M,VERTICAL:N,AUTO:T};g.fn.swipe.fingers={ONE:1,TWO:2,THREE:3,ALL:x}})(jQuery);
	
				thisGallery.$( 'container' ).find( '.galleria-thumbnails-container .galleria-image').swipe({
						swipeStatus:function(event, phase, direction, distance, fingerCount) {
							if(phase=="end"){
								if(direction == "left"){
									thisGallery.$( 'container' ).find('.galleria-thumb-nav-right').click();
								}
								if(direction == "right"){
									thisGallery.$( 'container' ).find('.galleria-thumb-nav-left').click();
								}
							}
						},
						triggerOnTouchEnd:false,
						threshold:100
					}
				);
	    	}
			
			
		    
		    var onResize = function(){
				thisGallery.$('container').css("height", thisGallery.$('container').parent().css("height"));
				thisGallery.$('container').css("width", thisGallery.$('container').parent().css("width"));
				calculateLayout();
				thisGallery._carousel.set(0);
				thisGallery._options.europeana.setActive(0);    	
		    };

		    
		    /* If this carousel lives in a collapsible container then it needs to refresh when that container has been opened.
		     * Intercept the custom event here
		     */
			$(window).bind('collapsibleExpanded', function(event, elements) {
				var sectionIsParent = false;
				$(elements).each(function(i, e){
			    	var parent = thisGallery.$('container')[0];
			    	while(parent != null ){
			    		if( parent == e ){
			    			sectionIsParent = true;
			    		}
			    		parent = parent.parentNode;
			    	}
				});
				if(sectionIsParent){
					onResize();
				}
			});
			
			$(window).resize( function() {
				if(eu.europeana.vars.suppresResize){
		  			return;
		  		}
				onResize();
		     });
		           
			
			// END CAROUSEL MODE
			
			
    	}
    	else if(borderedMode){
    		
    		thisGallery._options.responsive = false;
    		
    		// Doesn't seem to matter
    		//alert(    			thisGallery._options.responsive )
    		
    		var thumbsC	= thisGallery.$( 'container' ).find('.galleria-thumbnails-container');
    		
    		if(dataSource.length == 1){
            	
       			/* we're showing a single image */
				
				/* use extra that no thumbs gives us - move the stage down */
				var extraHeight = parseInt(thumbsC.css("height"));
				thumbs.css	("height", "0px");
				thumbsC.css	("height", "0px");
				stage.css("bottom", parseInt(stage.css("bottom"))	- extraHeight + "px");

				// hide navigation
				thumbNavLeft.css	("display", "none");
				thumbNavRight.css	("display", "none");
				navLeft.css			("display", "none");
				navRight.css		("display", "none");
				
				thisGallery.resize();
				
				// custom full doc options
				this.$( 'container' ).css("border-radius", "10px 10px 0px 0px");
    		}
    		
    		// resize function for bordered mode (full-doc page)
    		
       		$(window).resize( function() {
       			
				if(eu.europeana.vars.suppresResize){
	       			Galleria.log("suppress resize!");
	       		
	       			return;
	       		}
	
	   			var container			= thisGallery.$('container');
	   			var stage				= container.find('.galleria-stage');
	   			
	   			var stageBottomPhone	= 110;	/* linked to css values */
	   			var stageBottomDesktop	= 60;
	   			
	   			var containerCalculatedHeight = eu.europeana.fulldoc.getCarousel1Height();
	   			container.parent().css	("height", containerCalculatedHeight + "px");	// resize container to make space for bigger thumbs
	   			container.css			("height", containerCalculatedHeight + "px");
	
	   			if( window.showingPhone() ){
	       			stage.css("bottom", stageBottomPhone + "px");
	   			}
	   			else{
	   				stage.css("bottom", stageBottomDesktop + "px");     	
	   			}
	
	       		// vanishing image fix
        		thisGallery.bind('transitionend webkitTransitionEnd', function(e) {	
       				var imgShows = false;
       				thisGallery.$( 'container' ).find(".galleria-images .galleria-image").each(function(i, ob){
       					
       					$(ob).find("img").css("top", ( ($(ob).height() - $(ob).find("img").height()) /2 ) + "px");	// vertical alignment fix
       					
   						if($(ob).is(':visible') && $(ob).css("opacity") == "1" ){
       						imgShows = true;
       					}
       				});
       				if(!imgShows){
           				thisGallery.$( 'container' ).find(".galleria-images .galleria-image:first").css("opacity", "1");       					
       				}
        		});

	       		thisGallery.load(dataSource);		// reload needed as rerun / doing nothing shrinks the images
	   			thisGallery._createThumbnails();	// thumbs need updated too
	    		//thisGallery.refreshImage();
       		});
    	}
    	else{
    		
    		/* non-carousel non-bordered (index page) */
			thisGallery._options.responsive = false;

			var title		= thisGallery.$( 'container' ).find(".galleria-info-title");
			var description	= thisGallery.$( 'container' ).find(".galleria-info-description");
			
			
    		if(dataSource.length > 1){ // will always be the case on index page for this type of galleria
    			stage.after(info);
    			info.append(title);
    			info.append(description);


	    			
	    		info.click(function(){
	    			var i = thisGallery.getIndex();
        			window.open(dataSource[i].link, "_new");
        			
	    		});
    			

     			
        		thisGallery.bind('transitionend webkitTransitionEnd', function(e) {
       				var imgShows = false;
       				thisGallery.$( 'container' ).find(".galleria-images .galleria-image").each(function(i, ob){
   						if($(ob).is(':visible') && $(ob).css("opacity") == "1" ){
       						imgShows = true;
       					}
       				});
       				if(!imgShows){
           				thisGallery.$( 'container' ).find(".galleria-images .galleria-image:first").css("opacity", "1");       					
       				}
        		});
        		
        		thisGallery.bind("loadfinish", function(e) {
        			/* stretch images and pull to top */
        			var imagesC	= stage.find(".galleria-images");
        			var images	= stage.find(".galleria-image img");
        			
        			var pullUp = parseInt(images.eq(0).css("top"));
        			imagesC.css("top", "-" + pullUp +  "px"); // shift whole gallery up to overcome pre-set 'top' value of all images
        			
        			images.css("width",		images.eq(0).parent().css("width") );
        			images.css("height",	"auto");
        			images.css("display",	"block");
        			images.css("left",		"0px");
        			images.removeAttr("width");
        			images.removeAttr("height");
	
        			navLeft.css("left", "0px");
        			navRight.css("right", "0px");
         		 });
        		$(window).resize( function() {
           			if(!eu.europeana.vars.suppresResize){	/* waiting for a collapsible section (parent) to open or close */
            			thisGallery.$(	'container' ).parent().css("height", thisGallery.$( 'container' ).css("height"));
            			rerunOnResize();
           			}
        		});
    		}
    	}
    	
    	/* end europeana */


        // cache some stuff
        var touch = Galleria.TOUCH,
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

			if(carouselMode){
				this.addIdleState( this.get('galleria-thumb-nav-left'), { left:-50 });
				this.addIdleState( this.get('galleria-thumb-nav-right'), { right:-50 });
			}			
		}
		else{
			//alert("touch")
		}

		
		
		
		
        this.bind('thumbnail', function(e) {
            if (! touch && !carouselMode) {
                // fade thumbnails
            	
                $(e.thumbTarget).css('opacity', 0.6).parent().hover(function() {
                    $(this).not('.active').children().stop().fadeTo(100, 1);
                }, function() {
                    $(this).not('.active').children().stop().fadeTo(400, 0.6);
                });

                if ( e.index === this.getIndex() ) {
                    $(e.thumbTarget).css('opacity',1);
                }
            }
            else if(carouselMode){
            	$(e.thumbTarget).css('opacity', 1);            	
            }
            else{
                $(e.thumbTarget).css('opacity', this.getIndex() ? 1 : 0.6);
            }
        });

        this.bind('loadstart', function(e) {
        	/* europeana */
        	if(!carouselMode){
                if (!e.cached) {
                    this.$('loader').show().fadeTo(200, 0.4);
                }
                $(e.thumbTarget).css('opacity',1).parent().siblings().children().css('opacity', 0.6);
        	}
        	/* end europeana */
        });

        this.bind('loadfinish', function(e) {
            this.$('loader').fadeOut(200);
        });

        
		info.css("visibility", "visible");
		navLeft.css("visibility", "visible");
		navRight.css("visibility", "visible");
		
    }
});

}(jQuery));
