/*global jQuery, eu */
/*jslint browser: true, white: true */
(function( $, eu ) {
	'use strict';

	var displayAjaxFeedback = function( html ) {
		eu.europeana.ajax.methods.addFeedbackContent( html );
		eu.europeana.ajax.methods.showFeedbackContainer();
	},


	/**
	 * item language
	 */
	itemLanguage = {
		$translate_item: $('#translate-item'),
		$item_language: $('#item-language'),

		addLanguageItemListener: function() {
			itemLanguage.$item_language.on( 'change', itemLanguage.handleItemLanguageChange );
		},

		getValue: function() {
			var value = '';

			if ( this.$translate_item.prop('checked') ) {
				value = this.$item_language.val();
			}

			return value;
		},

		handleItemLanguageChange: function() {
			itemLanguage.$translate_item.prop('checked', true);
		},

		init: function() {
			if ( !eu.europeana.vars.user ) {
				return;
			}

			this.addLanguageItemListener();
		}
	},


	/**
	 * translate keyword languages
	 */
	keywords = {
		animation_delay: 12,
		$keyword_languages: $('#keyword-languages input'),
		languages_count: 0,
		non_user_limit: 3,
		user_limit: 6,
		keywords_disabled: false,
		cookie_field: 'keywordLanguages',
		cookie_value_delimeter: ',',
		$clear_selection: $( '#clear-selection' ),

		addClearSelectionListener: function() {
			keywords.$clear_selection.on( 'click', keywords.handleClearSelectionClick );
		},

		/**
		 * if the cookie field for keyword languages
		 * contains languages from a previous session, check their checkboxes
		 */
		addCookieValues: function() {
			var cookie_value = this.getCookie();

			this.$keyword_languages.each(function() {
				var $elm = $(this);

				if ( $.inArray( $elm.val(), cookie_value ) > -1 ) {
					$elm.prop('checked', true);
					keywords.languages_count += 1;
				}

				if ( keywords.languages_count
					>= keywords.non_user_limit
				) {
					return false;
				}

				return true;
			});
		},

		addKeywordLanguagesListener: function() {
			this.$keyword_languages.each(function() {
				$(this).on( 'click', keywords.handleKeywordLanguagesClick );
			});
		},

		checkDisabledState: function() {
			if ( keywords.languages_count
				>= keywords.non_user_limit
			) {
				keywords.disableTranslateKeywords();
				keywords.toggleDisabledKeywordsMessage();
			} else if ( keywords.keywords_disabled ) {
				keywords.enableTranslateKeywords();
				keywords.toggleDisabledKeywordsMessage();
			}
		},

		adjustKeywordLanguageLimit: function() {
			if ( eu.europeana.vars.user ) {
				this.non_user_limit = this.user_limit;
			}
		},

		/**
		 * @param {undefined|object} $elm
		 * jQuery object representing the checkbox whose state needs to be checked
		 */
		adjustLanguagesCount: function( $elm ) {
			if ( $elm === undefined ) {
				this.languages_count = 0;

				this.$keyword_languages.each(function() {
					$elm = $(this);

					if ( $elm.prop('checked') ) {
						keywords.languages_count += 1;
					}
				});
			} else {
				if ( $elm.prop('checked') ) {
					keywords.languages_count += 1;
				} else {
					keywords.languages_count -= 1;
				}
			}
		},

		clearSelections: function() {
			this.$keyword_languages.each(function() {
				var $elm = $(this);

				if ( $elm.prop('checked') ) {
					// this should work, but it doesn’t
					// $elm.prop('checked', false);
					$elm.attr('checked', false);
				}
			});

			keywords.languages_count = 0;
			this.checkDisabledState();
		},

		disableTranslateKeywords: function() {
			var ii = keywords.$keyword_languages.size(),
				r = keywords.randsort(ii);

			keywords.$keyword_languages.each(function(i) {
				var $elm = $(this);

				if ( $elm.prop('checked') === false ) {
					setTimeout(function(){
						$elm.closest('label').addClass('disabled');
						$elm.prop('disabled', true);
					}, r[i] * keywords.animation_delay);
				}
			});

			keywords.keywords_disabled = true;
		},

		enableTranslateKeywords: function() {
			var ii = keywords.$keyword_languages.size(),
				r = keywords.randsort(ii);

			keywords.$keyword_languages.each(function(i) {
				var $elm = $(this);

				setTimeout(function(){
					$elm.closest('label').removeClass('disabled');
					$elm.prop('disabled', false);
				}, r[i] * keywords.animation_delay);
			});

			keywords.keywords_disabled = false;
		},

		getCookie: function() {
			return $.cookie( this.cookie_field )
				? $.cookie( this.cookie_field ).split( this.cookie_value_delimeter )
				: undefined;
		},

		/**
		 * @returns {string}
		 * a string of values joined by this.cookie_value_delimeter
		 */
		getValue: function() {
			var value = '',
			values = [];

			this.$keyword_languages.each(function() {
				var $elm = $(this);

				if ( $elm.prop('checked') ) {
					values.push( $elm.val() );
				}
			});

			if ( values.length > 0 ) {
				value = values.join( this.cookie_value_delimeter );
			}

			return value;
		},

		handleClearSelectionClick: function() {
			keywords.clearSelections();
		},

		handleKeywordLanguagesClick: function() {
			keywords.adjustLanguagesCount( $(this) );
			keywords.checkDisabledState();
		},

		init: function() {
			this.adjustKeywordLanguageLimit();
			this.addKeywordLanguagesListener();
			this.addClearSelectionListener();

			if ( eu.europeana.vars.user ) {
				this.adjustLanguagesCount();
			} else {
				this.addCookieValues();
			}

			this.checkDisabledState();
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

		saveToCookie: function() {
			var cookie_value = this.getValue();

			$.cookie.raw = true;

			if ( cookie_value.length < 1 ) {
				$.removeCookie( keywords.cookie_field, { path: '/' } );
			} else {
				$.cookie( this.cookie_field, cookie_value, { path: '/' } );
			}
		},

		toggleDisabledKeywordsMessage: function() {
			$('#disabled-translate-keywords-msg').slideToggle();
		}
	},


	/**
	 * panel menus
	 *
	 * takes care of displaying the corresponding panel
	 */
	panelMenu = {
		$panel_links: $('#panel-links li'),
		default_panel: 'login',
		active_panel: null,
		hash: null,

		checkHash: function() {
			var hash = window.location.hash.substring(1),
			speed = 'slow';

			if ( hash !== panelMenu.hash ) {
				if ( hash === '' ) {
					panelMenu.hash = panelMenu.default_panel;
					window.location.hash = panelMenu.hash;
					speed = 0;
				} else {
					panelMenu.hash = hash;
				}

				panelMenu.setActivePanel( speed );
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

		addPanelMenuListener: function() {
			$('#panel-links a').on( 'click', panelMenu.handlePanelMenuClick );
		},

		init: function() {
			if ( eu.europeana.vars.user ) {
				panelMenu.default_panel = 'user-information';
			}

			panelMenu.addHashListener();
			panelMenu.checkHash();
		},

		/**
		 * @param {string|int} speed
		 * speed at which to animate scrollTop
		 */
		scrollToTop: function( speed ) {
			if ( speed === undefined ) {
				speed = 'slow';
			}

			$('body').animate({scrollTop:0}, speed);
		},

		/**
		 * @param {string|int} speed
		 * speed at which to animate scrollTop
		 */
		setActivePanel: function( speed ) {
			panelMenu.setActivePanelLink();
			$('#' + panelMenu.hash).fadeIn();

			switch ( panelMenu.hash ) {
				case'login':
					$('#j_username').focus();
					break;

				case'register':
					$('#register_email').focus();
					break;

				case'request-password':
					$('#forgot_email').focus();
					break;
			}

			this.scrollToTop( speed );
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
	portalLanguage = {
		cookie_field: 'portalLanguage',
		$portal_language: $('#portal-language'),
		initial_value: '',

		/**
		 * if the cookie field for portal language
		 * contains a value from a previous session, set it as selected
		 */
		addCookieValue: function() {
			var value = $.cookie( this.cookie_field );

			if ( value && value.length > 1 ) {
				this.$portal_language.val( value );
			}
		},

		getValue: function() {
			return this.$portal_language.val();
		},

		init: function() {
			if ( !eu.europeana.vars.user ) {
				this.addCookieValue();
			}

			this.initial_value = portalLanguage.$portal_language.val();
		},

		saveToCookie: function() {
			$.cookie( this.cookie_field, this.getValue(), { path: '/' } );
		},

		submitOnSave: function() {
			if ( this.initial_value !== this.$portal_language.val() ) {
				panelMenu.scrollToTop();

				setTimeout(
					function() {
						window.location = window.location.href
							.replace( window.location.hash, '' )
							.replace( window.location.search, '' )
							+ '?lang=' + portalLanguage.$portal_language.val()
							+ '#language-settings';
					},
					1000
				);
			}
		}
	},


	/**
	 * user panels
	 */
	userPanels = {
		addApiSubmitListener: function() {
			$('#myeuropeana').on(
				'submit',
				'.api-key-save-form',
				{},
				this.handleApiKeySubmit
			);
		},

		addRemoveItemListener: function() {
			$('#myeuropeana').on('click',
				'.remove-saved-item',
				{ modification_action : 'saved_item' },
				this.handleRemoveUserPanelItem
			);
		},

		addRemoveSearchListener: function() {
			$('#myeuropeana').on('click',
				'.remove-saved-search',
				{ modification_action : 'saved_search' },
				this.handleRemoveUserPanelItem
			);
		},

		addRemoveTagListener: function() {
			$('#myeuropeana').on('click',
				'.remove-saved-tag',
				{ modification_action : 'social_tag' },
				this.handleRemoveUserPanelItem
			);
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
					$elm.remove();
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

			var modification_action = evt.data.modification_action,
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
			};

			switch ( modification_action ) {
				case 'saved_search' :
					item.$count = $('#saved-searches-count');
					item.$panel = $('#saved-searches');
					item.feedback_html = $('<span>').text(eu.europeana.vars.msg.saved_search_removed);
					item.no_saved_msg = eu.europeana.vars.msg.no_saved_searches;
					item.removeSelector = '.saved-search.' + $elm.attr('id');
					break;

				case 'saved_item' :
					item.$count = $('#saved-items-count');
					item.$panel = $('.saved-items');
					item.feedback_html = $('<span>').text(eu.europeana.vars.msg.saved_item_removed);
					item.no_saved_msg = eu.europeana.vars.msg.no_saved_items;
					item.removeSelector = '.saved-item.' + $elm.attr('id');
					break;

				case 'social_tag' :
					item.$count = jQuery('#saved-tags-count');
					item.$panel = jQuery('#saved-tags');
					item.feedback_html = $('<span>').text(eu.europeana.vars.msg.saved_tag_removed);
					item.no_saved_msg = eu.europeana.vars.msg.no_saved_tags;
					item.removeSelector = '.saved-tag.' + $elm.attr('id');
					break;
			}

			ajax_feedback = {
				count : item.count,
				$count : item.$count,
				success : function() {
					displayAjaxFeedback( item.feedback_html );
					userPanels.removeUserPanelItemSuccess.call( self, item, ajax_feedback );
				},
				failure : function() {
					var text = eu.europeana.vars.msg.error_occurred + ' ' +
					eu.europeana.vars.msg.item_not_removed;

					displayAjaxFeedback( $('<span>', {'class':'error'}).text(text) );
				}
			};

			ajax_data = {
				modificationAction : modification_action,
				id : parseInt( $elm.attr('id'), 10 )
			};

			eu.europeana.ajax.methods.user_panel( 'remove', ajax_data, ajax_feedback );
		},

		handleApiKeySubmit : function( evt ) {
			var $elm = $(this),
			ajax_feedback = {
				success: function() {
					displayAjaxFeedback( $('<span>').text(eu.europeana.vars.msg.saved_apikey) );
				},
				failure: function() {
					displayAjaxFeedback( $('<span>', {'class':'error'}).text(eu.europeana.vars.msg.saved_apikey_failed) );
				}
			},
			ajax_data = {
				modificationAction : "api_key",
				apikey : $elm.find("input[name='apikey-id']").val(),
				appName : $elm.find("input[name='apikey-application-name']").val()
			};

			evt.preventDefault();
			eu.europeana.ajax.methods.user_panel( 'save', ajax_data, ajax_feedback );
		},

		init: function() {
			this.addRemoveSearchListener();
			this.addRemoveItemListener();
			this.addRemoveTagListener();
			this.addApiSubmitListener();
		}
	},


	languageSettings = {
		addSubmitListener: function() {
			$('#language-settings').find('form').on( 'submit', this.handleSubmit );
		},

		handleSubmit: function( evt ) {
			var ajax_feedback = {
				success: function() {
					displayAjaxFeedback( $('<span>').text(eu.europeana.vars.msg.save_settings_success) );
				},
				failure: function() {
					displayAjaxFeedback( $('<span>', {'class':'error'}).text(eu.europeana.vars.msg.save_settings_failure) );
				}
			},
			ajax_data = {
				modificationAction : "user_language_settings",
				itemLanguage: itemLanguage.getValue(),
				portalLanguage : portalLanguage.getValue(),
				keywordLanguages : keywords.getValue()
			};

			evt.preventDefault();

			if ( eu.europeana.vars.user ) {
				this.updateMyEuropeanaDb( ajax_data, ajax_feedback );
			} else {
				keywords.saveToCookie();
				portalLanguage.saveToCookie();
				displayAjaxFeedback( $('<span>').text(eu.europeana.vars.msg.save_settings_success) );
				portalLanguage.submitOnSave();
			}
		},

		init: function() {
			this.addSubmitListener();
		},

		updateMyEuropeanaDb: function( ajax_data, ajax_feedback ) {
			if ( !eu.europeana.vars.user ) {
				return;
			}

			eu.europeana.ajax.methods.user_panel( 'save', ajax_data, ajax_feedback );
		}
	};

	keywords.init();
	itemLanguage.init();
	panelMenu.init();
	portalLanguage.init();
	userPanels.init();
	languageSettings.init();

}( jQuery, eu ));
