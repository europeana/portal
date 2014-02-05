/**
 *  myeuropeana.js
 *
 *  @package	eu.europeana
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @author		andy maclean
 *  @created	2011-07-11 18:07 GMT +1
 *  @version	2012-11-30 08:26 GMT +1
 */

js.utils.registerNamespace( 'eu.europeana.myeuropeana' );

eu.europeana.myeuropeana = {
	
	init : function() {
		this.loadComponents();
		this.addUserBarListeners();		
		this.addUserPanelListeners();
		this.addHashListener();
		this.addItemHighlight();
		this.fmtSavedSearches();
		$('.item-apikey-save').bind('submit', this.handleSaveApiKeySubmit );
	},
	
	fmtSavedSearches : function(){
		if([].filter){
			$('.saved-search .go').each(function(i, ob){
				var html	=	$(ob).html();
				var query	=	html.substr(0, html.indexOf('&'));
				var fmted	=	html;
				if(query.length){
					var params = html.substr(html.indexOf('&'));
					params = params.split("&amp;qf=").filter(function(e){return e ? e.trim() : e });
					var fmted	= query + " &nbsp; &rarr; &nbsp;" + params.join("&nbsp; &rarr; &nbsp;");
				}
				$(ob).html(fmted);
				js.utils.fixSearchRowLinks($(ob));
			});
		}
		
		$(window).euRsz(function(){
			$('.saved-search .go').each(function(i, ob){
				js.utils.fixSearchRowLinks($(ob));
			});
		});
		
	},
	
	loadComponents : function() {
		var self = eu.europeana.myeuropeana;
//		js.loader.loadScripts([{
	//		file: 'tabs' + js.min_suffix + '.js' + js.cache_helper,
		//	path: eu.europeana.vars.branding + '/js/com/gmtplusone/' + js.min_directory,
			//callback: function(){
				self.addAccordionTabs();
			//}
//		}]);
	},
	
	addAccordionTabs:function(){
		js.loader.loadScripts([{
			//name : 'accordion-tabs',
			file : 'EuAccordionTabs' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			callback:function(){
				eu.europeana.myeuropeana.accordionTabs = new AccordionTabs( $('#user-panels'),
					function(i, id, hash){
						window.location.hash = hash;
					},
					window.location.hash
				);
			}
		}]);
	},

	openTab : function() {
		var hash = window.location.hash ? window.location.hash : '#user-information';
		eu.europeana.myeuropeana.accordionTabs.openTab(hash);
	},
	
	addUserBarListeners : function() {
		if ( 'myeuropeana.html' == eu.europeana.vars.page_name ) {
			var self = this;
			jQuery('#saved-items-count, #saved-searches-count, #saved-tags-count')
				.click( function() { setTimeout( function(e) {
					self.openTab();
				}, 100 ); });
		}
	},
	
	addUserPanelListeners : function() {
		
		$(document).on('click',
			'.remove-saved-search',
			{ type : 'SavedSearch' },
			this.handleRemoveUserPanelItem
		);
		$(document).on('click',
			'.remove-saved-item',
			{ type : 'SavedItem' },
			this.handleRemoveUserPanelItem
		);
		$(document).on('click',
			'.remove-saved-tag',
			{ type : 'SocialTag' },
			this.handleRemoveUserPanelItem
		);
		
	},
	
	addHashListener : function() {
		$(window).bind('hashchange', function() {
			eu.europeana.myeuropeana.openTab(window.location.hash);
		});
	},	
	
	handleRemoveUserPanelItem : function( e ) {
		e.preventDefault();		
		
		
		var type = e.data.type,
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
		
		switch(type){
			
			case 'SavedSearch' :
				item.$count = $('#saved-searches-count');
				item.$panel = $('#saved-searches');
				item.feedback_html = '<span>' + eu.europeana.vars.msg.saved_search_removed + '</span>';
				item.no_saved_msg = eu.europeana.vars.msg.no_saved_searches;
				item.removeSelector = '.saved-searches .saved-search.' + $(this).attr('id'); 
				break;
				
			case 'SavedItem' :
				item.$count = $('#saved-items-count');
				item.$panel = $('.saved-items');
				item.feedback_html = '<span>' + eu.europeana.vars.msg.saved_item_removed + '</span>';
				item.no_saved_msg = eu.europeana.vars.msg.no_saved_items;
				item.removeSelector = '.saved-items .saved-item.' + $(this).attr('id'); 
				break;
				
			case 'SocialTag' :
				item.$count = jQuery('#saved-tags-count');
				item.$panel = jQuery('#saved-tags');
				item.feedback_html = '<span>' + eu.europeana.vars.msg.saved_tag_removed + '</span>';
				item.no_saved_msg = eu.europeana.vars.msg.no_saved_tags;
				item.removeSelector = '.saved-tags .saved-tag.' + $(this).attr('id'); 
				break;
		}
		
		ajax_feedback = {
			count : item.count,
			$count : item.$count,
			success : function() {
				
				eu.europeana.ajax.methods.addFeedbackContent( item.feedback_html );
				eu.europeana.ajax.methods.showFeedbackContainer();
				
				ajax_feedback.count = parseInt( ajax_feedback.$count.html(), 10 );
				ajax_feedback.$count.html( ajax_feedback.count - 1 );

				if(item.removeSelector){
					$(item.removeSelector).each(function(i, ob){
						$(ob).remove();
					});
				}
				/*
				if ( 'SocialTag' !== type ) {
					if(item.removeSelector){
						$(item.removeSelector).each(function(i, ob){
							$(ob).remove();
						});
					}
				}
				else{
					if ( $elm.parent().parent().children().length === 1 ){
						$elm.parent().parent().parent().remove();
					}
					else{
						$elm.parent().remove();
					}
				}
				*/
				if ( ( ajax_feedback.count - 1 ) == 0 ) {
					item.$panel.append( item.no_saved_msg );
				}				
			},
			failure : function() {
				eu.europeana.ajax.methods.addFeedbackContent( error_feedback_html );
				eu.europeana.ajax.methods.showFeedbackContainer();
			}
		};
			
		ajax_data = {
			className : type,
			id : parseInt( $elm.attr('id'), 10 )
		};
		eu.europeana.ajax.methods.user_panel( 'remove', ajax_data, ajax_feedback );
		
	},
	
	addItemHighlight : function() {
		
		var shade = function(){ $(this).css('background-color', '#f5f5f5'); };
		var unshade = function(){ $(this).css('background-color', '#fff'); };
		
		$(document).on('mouseenter',
			'.saved-item',
			shade
		);
		$(document).on('mouseenter',
			'.saved-search',
			shade
		);
		$(document).on('mouseenter',
			'.saved-tag',
			shade
		);
		
		$(document).on('mouseleave',
			'.saved-item',
			unshade
		);
		$(document).on('mouseleave',
			'.saved-search',
			unshade
		);
		$(document).on('mouseleave',
			'.saved-tag',
			unshade
		);
		
	},
	
	handleSaveApiKeySubmit : function( e ) {
		e.preventDefault();
		
		if ( $('#item-tag').val() < 1 ){
			return;
		}

		var ajax_feedback = {
			saved_tags_count : 0,
			$saved_tags : $('#saved-tags-count'),
			success : function() {
				var html =
					'<span id="save-tag-feedback">' +
						eu.europeana.vars.msg.saved_tag +
					'</span>';
				eu.europeana.ajax.methods.addFeedbackContent( html );
				eu.europeana.ajax.methods.showFeedbackContainer();
				ajax_feedback.saved_tags_count = parseInt( ajax_feedback.$saved_tags.html(), 10 );
				ajax_feedback.$saved_tags.html( ajax_feedback.saved_tags_count + 1 );
			},
			failure : function() {
				var html =
					'<span id="save-tag-feedback" class="error">' +
						'eu.europeana.vars.msg.save_tag_failed' +
					'</span>';
				eu.europeana.ajax.methods.addFeedbackContent( html );
				eu.europeana.ajax.methods.showFeedbackContainer();
			}
		},
		
		ajax_data = {
			className : "ApiKey",
			apikey : $(e.target).closest('form').find('.apikey_id').val(),
			appName : encodeURIComponent( $(e.target).closest('form').find('.apikey_appName').val() )
		};
		eu.europeana.ajax.methods.user_panel( 'save', ajax_data, ajax_feedback );
	}
	
};

eu.europeana.myeuropeana.init();