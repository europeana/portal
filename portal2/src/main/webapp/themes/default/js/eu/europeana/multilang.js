(function( $ ) {
	'use strict';

	var multilang = {
		addListeners: function() {
			$( '.browser-language-change' )
				.on( 'click', multilang.handleClick );
		},

		handleClick: function( evt ) {
			evt.preventDefault();
			var hash = this.href.substring( this.href.indexOf( '#' ) );

			if ( hash === '#yes' ) {
				$.cookie( 'portalLanguage', eu.europeana.vars.browser_locale, { path: '/' } );
				location.reload();
			} else {
				$.cookie( 'portalLanguage', eu.europeana.vars.locale, { path: '/' } );
				$( '#browser-language-query' ).slideUp();
			}
		},

		init: function() {
			if ( !eu.europeana.vars.browser_locale || eu.europeana.vars.browser_locale.length < 2 ) {
				return;
			}

			$( 'body' ).prepend( $( '#browser-language-query' ).slideDown() );
			this.addListeners();
		}
	}

	multilang.init();
}( jQuery ));
