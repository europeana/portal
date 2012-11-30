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
	},
	
	loadComponents : function() {
		var self = eu.europeana.myeuropeana;
		js.loader.loadScripts([{
			file: 'tabs' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/com/gmtplusone/' + js.min_directory,
			callback: function(){
				self.addAccordionTabs();
			}
		}]);
	},
	
	addAccordionTabs:function(){
		js.loader.loadScripts([{
			name : 'accordion-tabs',
			file : 'accordion-tabs' + js.min_suffix + '.js' + js.cache_helper,
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
		$('.remove-saved-search').live('click', { type : 'SavedSearch' }, this.handleRemoveUserPanelItem );
		$('.remove-saved-item').live('click', { type : 'SavedItem' }, this.handleRemoveUserPanelItem );
		$('.remove-saved-tag').live('click', { type : 'SocialTag' }, this.handleRemoveUserPanelItem );
	},
	
	addHashListener : function() {
		$(window).bind('hashchange', function() {
			eu.europeana.myeuropeana.openTab(window.location.hash);
		});
	},	
	
	handleRemoveUserPanelItem : function( e ) {
		e.preventDefault();		
		var type = e.data.type,
			$elm = jQuery(this),
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
				item.$count = jQuery('#saved-searches-count');
				item.$panel = jQuery('#saved-searches');
				item.feedback_html = '<span>' + eu.europeana.vars.msg.saved_search_removed + '</span>';
				item.no_saved_msg = eu.europeana.vars.msg.no_saved_searches;
				break;
				
			case 'SavedItem' :
				item.$count = jQuery('#saved-items-count');
				item.$panel = jQuery('#saved-items');
				item.feedback_html = '<span>' + eu.europeana.vars.msg.saved_item_removed + '</span>';
				item.no_saved_msg = eu.europeana.vars.msg.no_saved_items;
				break;
				
			case 'SocialTag' :
				item.$count = jQuery('#saved-tags-count');
				item.$panel = jQuery('#saved-tags');
				item.feedback_html = '<span>' + eu.europeana.vars.msg.saved_tag_removed + '</span>';
				item.no_saved_msg = eu.europeana.vars.msg.no_saved_tags;
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
				
				if ( 'SocialTag' !== type ) {
					$elm.parent().remove();
				}
				else{
					if ( $elm.parent().parent().children().length === 1 ){
						$elm.parent().parent().parent().remove();
					}
					else{
						$elm.parent().remove();
					}
				}
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
		$('.saved-item, .saved-search, .saved-tag').live(
				'mouseenter',
					function(){
						$(this).css('background-color', '#f5f5f5');
			         }
		);

		$('.saved-item, .saved-search, .saved-tag').live(
				'mouseleave',
					function(){
						$(this).css('background-color', '#fff');
					}
		);
	}
	
};

eu.europeana.myeuropeana.init();