(function( $ ) {
	'use strict';

	var myeuropeana = {

		addPortalLanguageListener: function() {
			$( '#portalLanguage' ).on( 'change', this.handlePortalLanguageChange );
		},

		handlePortalLanguageChange: function() {
			$( this ).closest( 'form' ).submit();
		},

		init: function() {
			this.addPortalLanguageListener();
		}
	}

	myeuropeana.init();

}( jQuery ));
