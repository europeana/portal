/**
 *  truncate-content.js
 *
 *  @package	com.gmtplusone
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @created	2011-07-15 07:00 GMT +1
 *  @version	2011-07-20 08:38 GMT +1
 */

/**
 *  @package	com.gmtplusone
 *  @author		dan entous <contact@gmtpluosone.com>
 */
js.utils.registerNamespace( 'com.gmtplusone.truncate' );

com.gmtplusone.truncate = function( target, options ) {
	
	var default_options = {
			
			target : '#container',
			
			limit : {
				pixels : 100,
				percent : .33,
				use_pixels : true
			},
			
			toggle_html : {
				container : '<a href=""></a>',
				more : 'show more ...',
				less : 'show less ...',
				more_class : '',
				less_class : ''
			}
			
		},
		
		properties = {
			
			$target : {}
			
		};
		
		this.options = jQuery.extend( true, {}, default_options, options );
		this.options.target = ( 'string' === typeof target ) ? target : this.options.target;
		properties.$target = jQuery( this.options.target );
	
		
	com.gmtplusone.truncate.prototype.init = function( callback ) {
		
		var height = {};
		height.total = properties.$target.outerHeight(true);
		height.line = parseInt( properties.$target.css('line-height'), 10 );
		height.truncated = this.getTruncatedHeight( height );
		
		if ( height.total > height.truncated ) {
		
			properties.$target.css('height', height.truncated )
				.slideDown()		
				.after( this.options.toggle_html.container )
				.next()
				.html( this.options.toggle_html.more )
				.addClass( this.options.toggle_html.more_class )
				.bind(
					'click',
					{ self : this, height : height }, 
					this.handleToggleClick
				);
			
		} else {
			
			properties.$target.css('display', 'block' );
			
		}
		
		if ( callback ) { callback.call(); }
		
	};
	
	
	com.gmtplusone.truncate.prototype.handleToggleClick = function( e ) {
		
		var self = e.data.self,
			height = e.data.height;
		
		e.preventDefault();
		
		if ( properties.$target.outerHeight(true) < height.total ) {
			
			properties.$target.next().html( self.options.toggle_html.less );
			properties.$target.next().removeClass( self.options.toggle_html.more_class );
			properties.$target.next().addClass( self.options.toggle_html.less_class );
			properties.$target.animate( { height: height.total }, 500 );
			
		} else {
			
			properties.$target.next().html( self.options.toggle_html.more );
			properties.$target.next().removeClass( self.options.toggle_html.less_class );
			properties.$target.next().addClass( self.options.toggle_html.more_class );
			properties.$target.animate( { height: height.truncated }, 500 );
			
		}
		
	};
	
	
	com.gmtplusone.truncate.prototype.getTruncatedHeight = function( height ) {
		
		var truncated_height = 0;
		
		if ( this.options.limit.use_pixels ) {
		
			truncated_height = Math.floor( this.options.limit.pixels / height.line ) * height.line;
		
		} else if ( !isNaN( parseFloat( this.options.limit.percent ) ) ) {
			
			truncated_height = Math.floor( height.total * this.options.limit.percent );
			
		}
		
		return truncated_height;
		
	};
	
	
};