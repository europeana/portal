(function( $ ) {
	'use strict';

	var multilang = {
		addListeners: function() {
			$( '#browser-language-change-no, #browser-language-change-yes' )
				.on( 'click', multilang.handleClick );
		},

		handleClick: function( evt ) {
			evt.preventDefault();
			var hash = this.href.substring( this.href.indexOf( '#' ) );

			if ( hash === '#yes' ) {
				$.cookie( 'portalLanguage', eu.europeana.vars.browser_locale, { path: '/' } );
			} else {
				$.cookie( 'portalLanguage', eu.europeana.vars.locale, { path: '/' } );
			}
		},

		init: function() {
			this.addListeners();
		}
	}

	multilang.init();
}( jQuery ));
