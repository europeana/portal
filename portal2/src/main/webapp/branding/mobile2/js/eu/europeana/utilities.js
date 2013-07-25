/**
 *	utilities.js
 *
 *	@package	eu.europeana
 *	@subpackage	mobile2
 *	@version	2011-07-25 12:32
 */
js.type.registerNamespace( 'eu.europeana.utilities' );

eu.europeana.utilities = {
	
	
	init : function() {

		this.addLanguageChangeHandler();
		jQuery('#searchform').bind( 'submit', eu.europeana.utilities.submitQuery );
	    
	},
	
	
	submitQuery : function( e ) {
		
		if ( jQuery('#query').val().length < 1 ) {

			jQuery('#query').css('border','1px dotted firebrick');
			jQuery('#additional-feedback')
				.addClass('error')
				.html( eu.europeana.vars.msg.search_error );
			
			return false;
			
		}
		
		return true;
		
	},
	
	
	addLanguageChangeHandler : function( e ) {
    	
    	//e.preventDefault();
    	//jQuery('#lang').val( jQuery(this).val() );
		//jQuery('#frm-lang').submit();

		jQuery('#embeddedlang').change( function() {
			
			jQuery('#language-selector').submit();
			
		});
	},
		
	
	contactMe : function( prefix,suffix ) {
		
	    var m =  Array(109,97,105,108,116,111,58);
	    var s = '';
	    for (var i = 0; i < m.length; i++){
	        s += String.fromCharCode(m[i]);
	    }
	    window.location.replace(s + prefix + String.fromCharCode(8*8) + suffix);
	    return false;
	    
	}
	
	
};

jQuery(document).ready(function() { eu.europeana.utilities.init(); });
