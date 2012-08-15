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
        _toggleInfo: true
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

    this.$('loader').hide();
	var containerHeight = parseInt( this.$( 'container' ).css("height") );
	
	var dataSource = this._options.dataSource;
	
	this.$( 'thumbnails' ).find('.galleria-image').each(function(i, ob){
		// action
		$(ob).unbind('click');
		$(ob).click(function(e, a){
			alert(dataSource[i].link);
       		e.stopPropagation();
			window.open(dataSource[i].link, "_new");
		});

		// styling
		$(ob).css("height", containerHeight - 2 + "px");
	});

	// centre align nav caontrols
	var navRight	= 	$(".galleria-thumb-nav-right");
	var navLeft		= 	$(".galleria-thumb-nav-left");
	
	navRight.css	("top", (containerHeight - parseInt(navRight.css("height")))/2 + "px");
	navLeft.css		("top", (containerHeight - parseInt(navLeft.css("height")))/2 + "px");
	
	var thisGallery = this;
	$(window).resize( function() {
	
		var x = $(".galleria-container").parent().css("height");
		thisGallery.$( 'container' ).css("height", x);
		thisGallery.rescale();
//      	console.log("resize..." + thisGallery.$( 'container' ).css("height") + "  " + x);
      	
      	// 
     } );
            
	
    }
});

}(jQuery));
