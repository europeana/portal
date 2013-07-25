/**
 *  external-search-services.js
 *
 *  @package	eu.europeana
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @created	2011-07-07 11:14 GMT +1
 *  @version	2011-07-07 11:14 GMT +1
 */

/**
 *  @package	eu.europeana
 *  @author		dan entous <contact@gmtpluosone.com>
 */
js.utils.registerNamespace( 'eu.europeana.ess' );

eu.europeana.ess = {
	
	
	init : function() {
		
		var self = this;
		
		jQuery('.external-services').each( function(i) {
			
			jQuery(this).bind( 'click', { self : self }, self.handleEssClick );
			
		});

	},
	
	
	handleEssClick : function( e ) {
		
		e.preventDefault();
		e.data.self.showEssOptions( jQuery(this) );
		
	},
	
	
	showEssOptions : function( $elm ) {
		
		var self = this;
		
		if ( $elm.siblings('.external-services-container').length > 0 ) {
			
			self.closeEssOptions( $elm.siblings('.external-services-container') );
			return;
			
		}
		
		jQuery.get(
			
			$elm.attr('href'),
			
			function( data ) {
				$elm.parent().append( data );
				$elm.siblings('.external-services-container').slideDown();
				$elm.addClass('active');
				
				jQuery( $elm.siblings('.external-services-container').children('.header').children('.close-button') )
					.add( $elm )
					.click( function(e) {
						e.preventDefault();
						self.closeEssOptions( $elm );
						
					});
				
			}
			
		);
		
		
		
	},
	
	closeEssOptions : function( $elm ) {
		
		$elm.removeClass('active');
		
		$elm.siblings('.external-services-container').slideUp(
				
			function() {
				
				$elm.siblings('.external-services-container').remove();
				
			}
			
		);
		
	}
	
};