/*global jQuery, eu */
/*jslint browser: true, white: true */
(function( $, eu ) {
	'use strict';

	var panelMenu = {
		$panel_links: $('#panel-links li'),
		default_panel: 'login',
		active_panel: null,
		hash: null,

		checkHash: function() {
			var hash = window.location.hash.substring(1);

			if ( hash !== panelMenu.hash ) {
				if ( hash === '' ) {
					panelMenu.hash = panelMenu.default_panel;
					window.location.hash = panelMenu.hash;
				} else {
					panelMenu.hash = hash;
				}

				panelMenu.setActivePanel();
			}
		},

		addHashListener: function() {
			if ( window.onhashchange !== undefined ) {
				$(window).on('hashchange', panelMenu.checkHash);
			} else {
				eu.europeana.timer.addCallback({
					timer: 100,
					fn: panelMenu.checkHash,
					context: this
				});
			}
		},

		init: function() {
			if ( eu.europeana.vars.user ) {
				panelMenu.default_panel = 'user-information';
			}

			panelMenu.addHashListener();
			panelMenu.checkHash();
		},

		setActivePanel: function() {
			panelMenu.setActivePanelLink();
			$('#' + panelMenu.hash).fadeIn();

			switch ( panelMenu.hash ) {
				case'login': $('#j_username').focus(); break;
				case'register': $('#register_email').focus(); break;
				case'request-password': $('#forgot_email').focus(); break;
			}
		},

		setActivePanelLink: function() {
			panelMenu.$panel_links.each(function() {
				var $elm = $(this),
					$a = $('<a>'),
					$panel_a = $elm.find('a'),
					panel = $elm.attr('data-settings-panel'),
					panel_text = $elm.attr('data-settings-panel-text');

				if ( panel === panelMenu.hash ) {
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

		animation_delay: 12,
		$translate_keyword_languages: $( '#translate-keyword-languages input' ),
		translate_keyword_languages_count: 0,
		translate_keyword_language_limit: 3,
		translate_keyword_language_user_limit: 6,
		translate_keywords_disabled: false,
		cookie_field: 'searchLanguages',
		cookie_value_delimeter: ',',
		$clear_selection: $( '#clear-selection' ),

		addClearSelectionListener: function() {
			tkl.$clear_selection.on( 'click', tkl.handleClearSelectionClick );
		},

		addTranslateKeywordLanguagesListener: function() {
			this.$translate_keyword_languages.each(function() {
				$(this).on( 'click', tkl.handleTranslateKeywordsClick );
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
				tkl.disableTranslateKeywords();
				tkl.toggleDisabledKeywordsMessage();
			} else if ( tkl.translate_keywords_disabled ) {
				tkl.enableTranslateKeywords();
				tkl.toggleDisabledKeywordsMessage();
			}
		},

		checkLimit: function() {
			if ( eu.europeana.vars.user ) {
				this.translate_keyword_language_limit = this.translate_keyword_language_user_limit;
			}
		},

		disableTranslateKeywords: function() {
			var ii = tkl.$translate_keyword_languages.size(),
				r = tkl.randsort(ii);

			tkl.$translate_keyword_languages.each(function(i) {
				var $elm = $(this);

				if ( $elm.prop('checked') === false ) {
					setTimeout(function(){
						$elm.closest('label').addClass('disabled');
						$elm.prop('disabled', true);
					}, r[i] * tkl.animation_delay);
				}
			});

			tkl.translate_keywords_disabled = true;
		},

		enableTranslateKeywords: function() {
			var ii = tkl.$translate_keyword_languages.size(),
				r = tkl.randsort(ii);

			tkl.$translate_keyword_languages.each(function(i) {
				var $elm = $(this);

				setTimeout(function(){
					$elm.closest('label').removeClass('disabled');
					$elm.prop('disabled', false);
				}, r[i] * tkl.animation_delay);


			});

			tkl.translate_keywords_disabled = false;
		},

		getCookie: function() {
			return $.cookie( this.cookie_field )
				? $.cookie( this.cookie_field ).split( this.cookie_value_delimeter )
				: undefined;
		},

		handleClearSelectionClick: function() {
			$.removeCookie( tkl.cookie_field );
			tkl.translate_keyword_languages_count = 0;
			tkl.checkCurrentCookie();
		},

		handleTranslateKeywordsClick: function() {
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
			tkl.addTranslateKeywordLanguagesListener();
			tkl.addClearSelectionListener();
			tkl.checkLimit();
			tkl.checkCurrentCookie();
		},

		randsort: function(c) {
			var i, n, o = [];

			for ( i = 0; i < c; i += 1 ) {
				n = Math.floor( Math.random() * c );

				if ( $.inArray( n, o ) > 0 ) {
					i -= 1;
				} else {
					o.push(n);
				}
			}

			return o;
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
		},

		toggleDisabledKeywordsMessage: function() {
			$('#disabled-translate-keywords-msg').slideToggle();
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

	},

	/**
	 * user panels
	 */
	userPanels = {
		addRemoveItemListener: function() {
			$('#myeuropeana').one('click',
				'.remove-saved-item',
				{ type : 'SavedItem' },
				this.handleRemoveUserPanelItem
			);
		},

		addRemoveSearchListener: function() {
			$('#myeuropeana').one('click',
				'.remove-saved-search',
				{ type : 'SavedSearch' },
				this.handleRemoveUserPanelItem
			);
		},

		addRemoveTagListener: function() {
			$('#myeuropeana').one('click',
				'.remove-saved-tag',
				{ type : 'SocialTag' },
				this.handleRemoveUserPanelItem
			);
		},

		removeUserPanelItemFailure: function( error_feedback ) {
			eu.europeana.ajax.methods.addFeedbackContent( error_feedback_html );
			eu.europeana.ajax.methods.showFeedbackContainer();
		},

		removeUserPanelItemSuccess: function( item, ajax_feedback ) {
			var $elm;

			eu.europeana.ajax.methods.addFeedbackContent( item.feedback_html );
			eu.europeana.ajax.methods.showFeedbackContainer();

			ajax_feedback.count = parseInt( ajax_feedback.$count.html(), 10 );
			ajax_feedback.$count.html( ajax_feedback.count - 1 );

			if ( item.removeSelector ) {
				$elm = $(item.removeSelector).eq(0);
				$elm.slideToggle(function() {
					$elm.remove()
				});
			}

			if ( ( ajax_feedback.count - 1 ) === 0 ) {
				item.$panel.append( item.no_saved_msg );
			}
		},

		/**
		 * @param {object} evt
		 * jQuery Event object
		 */
		handleRemoveUserPanelItem : function( evt ) {
			evt.preventDefault();

			var type = evt.data.type,
			self = this,
			$elm = $(this),
			ajax_feedback,
			ajax_data,
			item = {
				count : 0,
				$count : {},
				$panel : {},
				removed_msg : '',
				no_saved_msg : ''
			},
			error_feedback_html =
				'<span id="remove-search-feedback" class="error">' +
					eu.europeana.vars.msg.error_occurred + ' ' +
					eu.europeana.vars.msg.item_not_removed +
				'</span>';

			switch ( type ) {
				case 'SavedSearch' :
					item.$count = $('#saved-searches-count');
					item.$panel = $('#saved-searches');
					item.feedback_html = $('<span>').text( eu.europeana.vars.msg.saved_search_removed );
					item.no_saved_msg = eu.europeana.vars.msg.no_saved_searches;
					item.removeSelector = '.saved-search.' + $elm.attr('id');
					break;

				case 'SavedItem' :
					item.$count = $('#saved-items-count');
					item.$panel = $('.saved-items');
					item.feedback_html = $('<span>').text( eu.europeana.vars.msg.saved_item_removed );
					item.no_saved_msg = eu.europeana.vars.msg.no_saved_items;
					item.removeSelector = '.saved-item.' + $elm.attr('id');
					break;

				case 'SocialTag' :
					item.$count = jQuery('#saved-tags-count');
					item.$panel = jQuery('#saved-tags');
					item.feedback_html = $('<span>').text( eu.europeana.vars.msg.saved_tag_removed );
					item.no_saved_msg = eu.europeana.vars.msg.no_saved_tags;
					item.removeSelector = '.saved-tag.' + $elm.attr('id');
					break;
			}

			ajax_feedback = {
				count : item.count,
				$count : item.$count,
				success : function() {
					userPanels.removeUserPanelItemSuccess.call( self, item, ajax_feedback );
				},
				failure : function() {
					userPanels.removeUserPanelItemFailure.call( self, error_feedback );
				}
			};

			ajax_data = {
				className : type,
				id : parseInt( $elm.attr('id'), 10 )
			};

			eu.europeana.ajax.methods.user_panel( 'remove', ajax_data, ajax_feedback );
		},

		//handleSaveApiKeySubmit : function( e ) {
		//	e.preventDefault();
		//
		//	if ( $('#item-tag').val() < 1 ){
		//		return;
		//	}
		//
		//	var ajax_feedback = {
		//		saved_tags_count : 0,
		//		$saved_tags : $('#saved-tags-count'),
		//		success : function() {
		//			var html =
		//				'<span id="save-tag-feedback">' +
		//					eu.europeana.vars.msg.saved_apikey +
		//				'</span>';
		//			eu.europeana.ajax.methods.addFeedbackContent( html );
		//			eu.europeana.ajax.methods.showFeedbackContainer();
		//			ajax_feedback.saved_tags_count = parseInt( ajax_feedback.$saved_tags.html(), 10 );
		//			ajax_feedback.$saved_tags.html( ajax_feedback.saved_tags_count + 1 );
		//		},
		//		failure : function() {
		//			var html =
		//				'<span id="save-tag-feedback" class="error">' +
		//					eu.europeana.vars.msg.save_apikey_failed +
		//				'</span>';
		//			eu.europeana.ajax.methods.addFeedbackContent( html );
		//			eu.europeana.ajax.methods.showFeedbackContainer();
		//		}
		//	},
		//
		//	ajax_data = {
		//		className : "ApiKey",
		//		apikey : $(e.target).closest('form').find('.apikey_id').val(),
		//		appName : encodeURIComponent( $(e.target).closest('form').find('.apikey_appName').val() )
		//	};
		//
		//	eu.europeana.ajax.methods.user_panel( 'save', ajax_data, ajax_feedback );
		//},

		init: function() {
			this.addRemoveSearchListener();
			this.addRemoveItemListener();
			this.addRemoveTagListener();
			//$('.item-apikey-save').bind('submit', this.handleSaveApiKeySubmit );
		}
	};

	pl.init();
	tkl.init();
	til.init();
	panelMenu.init();
	userPanels.init();

}( jQuery, eu ));
