/*global jQuery */
/*jslint browser: true, white: true */
(function( $ ) {
	'use strict';

	var panels = {
		$panel_links: $('#panel-links li'),
		default_panel: 'language-settings',
		active_panel: null,
		hash: null,

		checkHash: function() {
			var hash = window.location.hash.substring(1);

			if ( hash !== panels.hash ) {
				if ( hash === '' || panels.hash === null ) {
					panels.hash = panels.default_panel;
					window.location.hash = panels.hash;
				} else {
					panels.hash = hash;
				}

				panels.setActivePanel();
			}
		},

		init: function() {
			eu.europeana.timer.addCallback({
				timer: 100,
				fn: panels.checkHash,
				context: this
			});
		},

		setActivePanel: function() {
			panels.setActivePanelLink();
			$('#' + panels.hash).fadeIn();
		},

		setActivePanelLink: function() {
			panels.$panel_links.each(function() {
				var $elm = $(this),
					$a = $('<a>'),
					$panel_a = $elm.find('a'),
					panel = $elm.attr('data-settings-panel'),
					panel_text = $elm.attr('data-settings-panel-text');

				if ( panel === panels.hash ) {
					if ( $a.length > 0 ) {
						$elm.text( panel_text );
					}
				} else {
					if ( $panel_a.length < 1 ) {
						$('#' + panel).hide();
						$a.attr('href', '#' + panel).text(panel_text);
						$elm.html($a);
					}
				}
			});
		}
	},

	/**
	 * portal language
	 */
	pl = {

		addPortalLanguageListener: function() {
			$('#portalLanguage').on( 'change', this.handlePortalLanguageChange );
		},

		handlePortalLanguageChange: function() {
			$( this ).closest('form').submit();
		},

		init: function() {
			this.addPortalLanguageListener();
		}

	},

	/**
	 * translate keyword languages
	 */
	tkl = {

		$translate_keyword_languages: $( '#translate-keyword-languages input' ),
		translate_keyword_languages_count: 0,
		translate_keyword_language_limit: 3,
		translate_keyword_language_user_limit: 6,
		translate_keyword_languages_disabled: false,
		cookie_field: 'searchLanguages',
		cookie_value_delimeter: ',',

		addTranslateKeywordLanguagesListener: function() {
			this.$translate_keyword_languages.each(function() {
				$(this).on( 'click', tkl.handleTranslateKeywordLanguageClick );
			});
		},

		/**
		 * add a value to the cookie if it does not already exist in the cookie
		 * @param {string} value
		 */
		addValueToCookie: function( value ) {
			var cookie_value = this.getCookie();

			$.cookie.raw = true;

			if ( cookie_value === undefined ) {
				$.cookie( this.cookie_field, value );
			} else if ( $.inArray( value, cookie_value ) === -1 ) {
				cookie_value.push( value );
				$.cookie( this.cookie_field, cookie_value.join( this.cookie_value_delimeter ) );
			}
		},

		checkCurrentCookie: function() {
			var cookie_value = this.getCookie();

			this.$translate_keyword_languages.each(function() {
				var $elm = $(this);

				if ( $.inArray( $elm.val(), cookie_value ) > -1 ) {
					$elm.prop('checked', true);
					tkl.translate_keyword_languages_count += 1;
				}
			});

			this.checkDisabledState();
		},

		checkDisabledState: function() {
			if ( tkl.translate_keyword_languages_count
				>= tkl.translate_keyword_language_limit
			) {
				tkl.disableTranslateKeywordLanguages();
			} else if ( tkl.translate_keyword_languages_disabled ) {
				tkl.enableTranslateKeywords();
			}
		},

		checkLimit: function() {
			if ( eu.europeana.vars.user ) {
				this.translate_keyword_language_limit = this.translate_keyword_language_user_limit;
			}
		},

		disableTranslateKeywordLanguages: function() {
			tkl.$translate_keyword_languages.each(function() {
				var $elm = $(this);

				if ( $elm.prop('checked') === false ) {
					$elm.closest('label').addClass('disabled');
					$elm.prop('disabled', true);
				}
			});

			tkl.translate_keyword_languages_disabled = true;
		},

		enableTranslateKeywords: function() {
			tkl.$translate_keyword_languages.each(function() {
				var $elm = $(this);
				$elm.closest('label').removeClass('disabled');
				$elm.prop('disabled', false);
			});

			tkl.translate_keywords_disabled = false;
		},

		getCookie: function() {
			return $.cookie( this.cookie_field )
				? $.cookie( this.cookie_field ).split( this.cookie_value_delimeter )
				: undefined;
		},

		handleTranslateKeywordLanguageClick: function() {
			var $elm = $(this);

			if ( $elm.prop('checked') ) {
				tkl.translate_keyword_languages_count += 1;
				tkl.addValueToCookie( $elm.val() );
			} else {
				tkl.translate_keyword_languages_count -= 1;
				tkl.removeValueFromCookie( $elm.val() );
			}

			tkl.checkDisabledState();
		},

		init: function() {
			this.addTranslateKeywordLanguagesListener();
			this.checkLimit();
			this.checkCurrentCookie();
		},

		removeValueFromCookie: function( value ) {
			var i,
				cookie_value = this.getCookie(),
				new_cookie_value = [];

			if ( cookie_value !== undefined && $.inArray( value, cookie_value ) > -1 ) {
				for ( i = 0; i < cookie_value.length; i += 1 ) {
					if ( cookie_value[i] !== value ) {
						new_cookie_value.push( cookie_value[i] );
					}
				}

				$.cookie( this.cookie_field, new_cookie_value.join( this.cookie_value_delimeter ) );
			}
		}

	},

	/**
	 * translate item language
	 */
	til = {
		$translate_item_language: $('#translate-item-language'),
		$item_language: $('#item-language'),
		$item_languages: $('#item-language option'),
		cookie_field: 'itemLanguage',

		addItemLanguageListener: function() {
			til.$item_language.on( 'change',  til.handleItemLanguageChange );
		},

		addTranslateItemLanguageListener: function() {
			til.$translate_item_language.on( 'click',  til.handleTranslateItemLanguageClick );
		},

		checkCookie: function() {
			var cookie_value = $.cookie( this.cookie_field );

			if ( cookie_value !== undefined ) {
				this.$translate_item_language.prop('checked', true);

				this.$item_languages.each(function() {
					var $elm = $(this);

					if ( $elm.val() === cookie_value ) {
						$elm.prop('selected', true);
						return false;
					}

					return true;

				});
			}
		},

		handleItemLanguageChange: function() {
			var $elm = $(this);

			if ( til.$translate_item_language.prop('checked') ) {
				$.cookie( til.cookie_field, $elm.val() );
			}
		},

		handleTranslateItemLanguageClick: function() {
			var $elm = $(this);

			if ( $elm.prop('checked') ) {
				$.cookie( til.cookie_field, til.$item_language.val() );
			} else {
				$.removeCookie( til.cookie_field );
			}
		},

		init: function() {
			this.addItemLanguageListener();
			this.addTranslateItemLanguageListener();
			this.checkCookie();
		}

	};

	pl.init();
	tkl.init();
	til.init();
	panels.init();

}( jQuery ));
