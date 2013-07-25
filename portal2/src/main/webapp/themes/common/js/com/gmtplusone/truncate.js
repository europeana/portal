/**
 *  truncate-content.js
 *
 *  @package    com.gmtplusone
 *  @author     dan entous <contact@gmtplusone.com>
 *  @created    2011-07-15 07:00 gmt +1
 *  @version    2012-04-26 03:10 gmt +1
 */

/**
 *  @package	com.gmtplusone
 *  @author		dan entous <contact@gmtplusone.com>
 */
(function() {
	
	'use strict';
	
	
	if ( 'function' !== typeof Object.create ) {
		
		Object.create = function( obj ) {
			
			function F() {}
			F.prototype = obj;
			return new F();
			
		};
		
	}
	
	
	var Truncate = {
		
		$target : null,
		options : null,
		$toggle_link : null,
		height : {},
		
		$content_tester : jQuery('<div/>', { 'class': 'metadata', style : 'position: absolute; visibility: hidden;' } ),
		
		default_options : {
				limit : {
				pixels : 100,
				percent : 0.33,
				use_pixels : true
			}
		},
		
		
		handleToggleClick : function( evt ) {
			
			var self = evt.data.self,
					height = evt.data.height;
			
			
			evt.preventDefault();
			self.determineHeight();
			
			if ( self.$target.outerHeight(true) < self.height.total ) {
				
				self.$toggle_link
					.html( self.options.toggle_html.less )
					.removeClass( self.options.toggle_html.more_class )
					.addClass( self.options.toggle_html.less_class );
				
				self.$target.animate( { height: height.total }, 500 );
				
			} else {
				
				self.$toggle_link
					.html( self.options.toggle_html.more )
					.removeClass( self.options.toggle_html.less_class )
					.addClass( self.options.toggle_html.more_class );
				
				self.$target.animate( { height: height.truncated }, 500 );
				
			}
			
		},
		
		
		adjustHeight : function() {
			
			var self = this;
			
			if ( self.height.total > self.height.truncated ) {
				
				self.$target
					.css({ height : self.height.truncated, overflow : 'hidden' });
				
				self.$toggle_link.on(
					'click',
					{ self : self, height : self.height }, 
					self.handleToggleClick
				);
				
			} else {
				
				self.$target.css('display', 'block' );
				
			}
			
		},
		
		
		getTruncatedHeight : function() {
			
			var self = this,
					truncated_height = 0;
			
				if ( self.options.limit.use_pixels ) {
					
					truncated_height =
						Math.floor( self.options.limit.pixels / self.height.line_height )
						* self.height.line_height;
					
				} else {
					
					truncated_height =
						Math.floor( self.height.total * self.options.limit.percent );
					
				}
			
			return truncated_height;
			
		},
		
		
		determineHeight : function( evt ) {
			
			var self = evt ? evt.data.self : this;
			
			self.$content_tester.html( self.$target.html() );			
			self.height.total = self.$content_tester.outerHeight();
			self.height.line_height = parseInt( self.$content_tester.css('line-height'), 10 );
			self.height.truncated = self.getTruncatedHeight();
			
		},
		
		
		prepOptions : function() {
			
			var self = this;
			
			self.options.limit.pixels =
				!isNaN( parseInt( self.options.limit.pixels, 10 ) )
					? parseInt( self.options.limit.pixels, 10 )
					: self.default_options.limit.pixels;
			
			self.options.limit.percent =
				!isNaN( parseFloat( self.options.limit.percent ) )
					? parseFloat( self.options.limit.percent )
					: self.default_options.limit.percent;
			
			self.options.limit.use_pixels =
				'boolean' === typeof self.options.limit.use_pixels
					? self.options.limit.use_pixels
					: self.default_options.limit.use_pixels;
			
			self.$toggle_link =
				jQuery( self.options.toggle_html.container )
				.addClass( self.options.toggle_html.more_class )
				.html( self.options.toggle_html.more );
			
			self.$content_tester.insertAfter( self.$target );
			
		},
		
		
		init : function( options, target ) {
			
			var self = this;
				self.$target = jQuery(target);
			
			self.options = jQuery.extend( true, {}, jQuery.fn.truncate.options, options );
			
			self.prepOptions();
			self.determineHeight();
			self.$target.after( self.$toggle_link );
			self.adjustHeight( self.height );
			//self.watchContent();
			
			if ( self.options.callback ) { self.options.callback.call( self.$target ); }
			
		}
		
	};
	
	
	jQuery.fn.truncate = function( options ) {
		
		return this.each(function() {
			
			var truncate = Object.create( Truncate );
			truncate.init( options, this );
			
		});
		
	};
	
	
	jQuery.fn.truncate.options = {
		
		limit : {
			pixels : 100,
			percent : 0.33,
			use_pixels : true
		},
		
		toggle_html : {
			container : '<a href="" class="truncate-toggle"></a>',
			more : 'show more ...',
			less : 'show less ...',
			more_class : '',
			less_class : ''
		},
		
		callback : null
		
	};
	
	
}());