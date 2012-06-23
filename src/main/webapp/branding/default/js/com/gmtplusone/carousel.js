/**
 *  carousel.js
 *
 *  @package	com.gmtplusone
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @created	2011-03-30 16:13 GMT +1
 *  @version	2011-10-20 08:26 GMT +1
 */

/**
 *  @package	com.gmtplusone
 *  @author		dan entous <contact@gmtpluosone.com>
 */
js.utils.registerNamespace( 'com.gmtplusone.carousel' );

com.gmtplusone.carousel = function( target, options ) {
	
	var default_options = {
		
		animation : {
			duration : ( 'ontouchstart' in document ) ? 100 : 700,
			marginLeft : 0,
			arrow_keys_active : true,
			hover_title : true
		},
		
		properties : {
			item_widths : [],
			item_count : 0,
			current_item : 0,
			container_width : 0,
			nr_in_container : 0,
			carousel : '#carousel',
			carousel_container : '.carousel-container',
			$previous : null,
			$next : null
		},
		
		controls : {
			previous : '<a href="" class="carousel-control previous nofollow" title="previous"></a>',
			next : '<a href="" class="carousel-control next nofollow" title="next"></a>'
		},
		
		touch : {		
			x_start : null,
			x_end : null,
			move_registered : false		
		}
		
	};
	
	this.options = jQuery.extend( true, {}, default_options, options );
	this.options.properties.carousel = ( 'string' === typeof target ) ? target : this.options.properties.carousel;
	this.options.properties.$carousel_container = jQuery( this.options.properties.carousel + ' ' + this.options.properties.carousel_container );
	
	
	this.init = function() {
		
		jQuery( this.options.properties.carousel_container ).stop().animate({opacity:1}, 1700);
		this.setupCarousel();
		
		if ( this.options.properties.item_count > this.options.properties.nr_in_container ) {
		
			this.placeControls();
			this.randomLocation();
			
		}
		
		if ( this.options.animation.arrow_keys_active ) {
			
			this.addKeyUpHandler();
			
		}
		
	};
	
	
	com.gmtplusone.carousel.prototype.setupCarousel = function() {
		
		var self = this,
			greatest_width = 0,
			$elm;
		
		jQuery(this.options.properties.carousel + ' li').each(function( key, value ) {
			
			$elm = jQuery( value );
			self.options.properties.item_widths.push( $elm.outerWidth(true) );
			self.options.properties.container_width += $elm.outerWidth(true);
			if ( self.options.properties.container_width > greatest_width ) { greatest_width = $elm.outerWidth(true); }
			self.options.properties.item_count += 1;
			
			if ( self.options.animation.hover_title ) {
				
				self.addHoverBox( $elm );
				//self.addHoverHandler( $elm );
				
			}
			
		});
		
		this.options.properties.nr_in_container = Math.round( this.options.properties.$carousel_container.width() / greatest_width );
		
		jQuery(this.options.properties.carousel + ' li a').each(function( key, value ) {
			
			$elm = jQuery( value );
			self.addTouchHandler( $elm );
			self.addItemClickHandler( $elm );
			
		});
		
		jQuery(this.options.properties.carousel + ' ul').css( 'width', this.options.properties.container_width );
		
	};
	
	
	com.gmtplusone.carousel.prototype.placeControls = function() {
		
		this.options.properties.$carousel_container.before( this.options.controls.previous );
		this.options.properties.$previous = jQuery(this.options.properties.carousel + ' .previous');
		this.addControlClickHandler( this.options.properties.$previous );
		
		this.options.properties.$carousel_container.after( this.options.controls.next );
		this.options.properties.$next = jQuery(this.options.properties.carousel + ' .next');
		this.addControlClickHandler( this.options.properties.$next );
		
	};


	com.gmtplusone.carousel.prototype.addControlClickHandler = function( $e ) {
		
		var self = this;
		
		$e.bind( 'click', function( e ) {
			
			e.preventDefault();
			self.handleControlClick( jQuery(this) );
			
		});
		
	};
	
	
	com.gmtplusone.carousel.prototype.handleControlClick = function( $e ) {
		
		var current_marginLeft = this.options.animation.marginLeft;
		
	    if ( $e.hasClass('previous') ) { this.handleClickPrevious(); }
	    else if ( $e.hasClass('next') ) { this.handleClickNext(); }
	    
		if ( current_marginLeft !== this.options.animation.marginLeft ) { this.animateCarousel(); }
		
	};
	
	
	com.gmtplusone.carousel.prototype.handleClickPrevious = function() {
		
		if ( 0 === this.options.properties.current_item )  { return; }
		this.options.properties.current_item -= 1;
		
		this.options.animation.marginLeft +=
			jQuery(this.options.properties.carousel + ' li')
			.eq( this.options.properties.current_item )
			.outerWidth(true);
		
	};
	
	
	com.gmtplusone.carousel.prototype.handleClickNext = function() {
		
		if ( this.options.properties.item_count - this.options.properties.nr_in_container === this.options.properties.current_item ) { return; }
	        this.options.properties.current_item += 1;
			
		this.options.animation.marginLeft +=
			-1 *
			jQuery(this.options.properties.carousel + ' li')
			.eq( this.options.properties.current_item )
			.outerWidth(true);
		
	};
	
	
	com.gmtplusone.carousel.prototype.addItemClickHandler = function( $elm ) {
		
		$elm.bind( 'click', {carousel : this}, this.handleItemClick );
		
	};
	
	
	com.gmtplusone.carousel.prototype.handleItemClick = function( e ) {
		
		var self = e.data.carousel;
		
		if ( self.options.touch.move_registered ) {
			
			e.stopPropagation();
			e.preventDefault();
			self.options.touch.move_registered = false;
			
		}
		
	};
	
	
	com.gmtplusone.carousel.prototype.addKeyUpHandler = function() {
		
		jQuery(document).bind('keyup', {carousel : this}, this.handleKeyUp );
		
	};
	
	
	com.gmtplusone.carousel.prototype.handleKeyUp = function( e ) {
	        
	    if ( !e || !e.keyCode ) { return; }
	    
		var self = e.data.carousel,
			current_marginLeft = self.options.animation.marginLeft;
		
	    switch( e.keyCode ) {
	        
	        case 37 :
				
	            self.handleClickPrevious();
	            break;
	        
	        case 39 :
	        
	            self.handleClickNext();
	            break;
	        
	    }
		
		if ( current_marginLeft !== self.options.animation.marginLeft ) {
			
			self.animateCarousel();
			
		}
	    
	};
	
	
	com.gmtplusone.carousel.prototype.toggleControls = function() {
			
		this.togglePrevious();
		this.toggleNext();
		
	};
	
	
	com.gmtplusone.carousel.prototype.togglePrevious = function() {
			
		var self = this;
		
		if ( 0 === this.options.properties.current_item ) {
			
			this.options.properties.$previous.fadeOut( function() {
				
				self.options.properties.$carousel_container.removeClass('previous-added');
				
			});
			
		} else if ( 'block' !== this.options.properties.$previous.css('display') ) {
			
			this.options.properties.$carousel_container.addClass('previous-added');
			this.options.properties.$previous.fadeIn();
			
		}
		
	};
	
	
	com.gmtplusone.carousel.prototype.toggleNext = function() {
		
		if ( this.options.properties.current_item === this.options.properties.item_count - this.options.properties.nr_in_container ) {
			
			this.options.properties.$next.fadeOut();
			
		} else if ( 'block' !== this.options.properties.$next.css('display') ) {
			
			this.options.properties.$next.fadeIn();
			
		}
		
	};
	
	
	com.gmtplusone.carousel.prototype.addHoverBox = function( $elm ) {
		
		var hover_box =
			'<div class="hover-box">' +
				$elm.children('a').attr('title') +
			'</div>';
		
		$elm.append( hover_box );
		// $elm.children('.hover-box').css('margin-left', -1 * parseInt( $elm.children('.hover-box').outerWidth(true), 10 ) );
		$elm.children('.hover-box').css('margin-left', 0 );
		
	};
	
	
	com.gmtplusone.carousel.prototype.addHoverHandler = function( $elm ) {
		
		var $hover_box = $elm.children('.hover-box'),
			hover_box_width = parseInt( $elm.children('.hover-box').outerWidth(true), 10 ) + 20;
		
		$elm.hover(
			
			function() {
				$hover_box
					.stop()
					.animate(
						{ marginLeft : 0 },
						500
					);
			},
			
			function() {
				$hover_box
					.stop()
					.animate(
						{ marginLeft : -1 * hover_box_width },
						500
					);
			}
			
		);
		
	};
	
	
	com.gmtplusone.carousel.prototype.randomLocation = function() {
			
		var i,
			self = this,
			randomnumber = Math.floor( Math.random() * ( this.options.properties.item_count - this.options.properties.nr_in_container ) + 1);
		
		
		if ( 0 === randomnumber ) { return; }
		this.options.properties.current_item = randomnumber;
		
		for ( i = 0; i < randomnumber; i += 1 ) {
			
			this.options.animation.marginLeft +=
				-1 * self.options.properties.item_widths[i];
			
		}
		
		this.animateCarousel();

	};
	
	
	com.gmtplusone.carousel.prototype.animateCarousel = function() {
		
		var self = this;
		
		jQuery(this.options.properties.carousel + ' ul').animate(
			this.options.animation,
			this.options.animation.duration,
			function() { self.toggleControls(); }
		);
		
	};
	
	
	
	/**
	 *	touch events
	 */
	com.gmtplusone.carousel.prototype.addTouchHandler = function( $elm ) {
		
		$elm.bind( 'touchstart', { self : this }, this.handleTouchEvent, false );
		$elm.bind( 'touchend', { self : this }, this.handleTouchEvent, false );
		$elm.bind( 'touchmove', { self : this }, this.handleTouchEvent, false );
		$elm.bind( 'touchcancel', { self : this }, this.handleTouchEvent, false );
		
	};
	
	
	com.gmtplusone.carousel.prototype.handleTouchEvent = function( e ) {
		
		var self = e.data.self;
		
		
		switch ( e.originalEvent.type ) {
			
			case 'touchstart' :
				
				self.options.touch.x_start = e.originalEvent.touches[0].screenX;
				break;
			
			
			case 'touchmove' :
				
				e.preventDefault();
				break;
			
			
			case 'touchend' :
				
				self.options.touch.x_end = e.originalEvent.changedTouches[0].screenX;
				
				if ( self.options.touch.x_start !== null && self.options.touch.x_end !== null ) {
					
					var x_start = parseInt( self.options.touch.x_start, 10 ),
						x_end	= parseInt( self.options.touch.x_end, 10 );
						
					if ( x_start !== x_end ) {
						
						self.options.touch.move_registered = true;
						
						if ( x_end > x_start ) {
							
							self.handleClickPrevious();
							
						} else {
							
							self.handleClickNext();
							
						}
						
						self.options.touch.x_start = null;
						self.options.touch.x_end = null;
						self.animateCarousel();
						
					}
					
				}
				
				break;
			
			
			case 'click' :
				
				if ( self.options.touch.move_registered ) {
					
					e.stopPropagation();
					e.preventDefault();
					self.options.touch.move_registered = false;
					
				}
				
				break;
			
		}
		
	};

};
