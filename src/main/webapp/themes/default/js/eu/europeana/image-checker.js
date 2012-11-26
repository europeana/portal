/**
 *  external-search-services.js
 *
 *  @package	eu.europeana
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @created	2011-07-08 10:11 GMT +1
 *  @version	2011-07-25 11:00 GMT +1
 */

/**
 * 	relies on the attribute data-type on the <img> element
 * 	to determine the replacement image
 *
 *	@todo		ie is failing the if test below even when the images do load, need to figure out the best way
 *				to make sure ie passes the test when image is actually loaded		
 *  @package	eu.europeana
 *  @author		dan entous <contact@gmtpluosone.com>
 */
js.utils.registerNamespace( 'eu.europeana.image_checker' );

eu.europeana.image_checker = {

	init : function() {
		
		jQuery('img').each( function( key, value ) {
			
			if ( !this.complete
				 || !this.naturalWidth 
				 || this.naturalWidth === 0 
				 || !this.naturalHeight
				 || this.naturalHeight === 0 ) {
				
				var type = ( jQuery(this).attr('data-type') );
				
				if ( type ) {
					this.src = eu.europeana.vars.branding + '/images/icons/' + jQuery(this).attr('data-type') + '.png';
				}
				
			}
			
		});
		
	}

};

eu.europeana.image_checker.init();
