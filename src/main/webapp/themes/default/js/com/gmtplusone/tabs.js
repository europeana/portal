/**
 *  tabs.js
 *
 *  @package	com.gmtplusone
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @created	2011-03-30 16:13 GMT +1
 *  @version	2011-10-20 10:18 GMT +1
 */

/**
 *  @package	com.gmtplusone
 *  @author		dan entous <contact@gmtplusone.com>
 */
js.utils.registerNamespace( 'com.gmtplusone.tabs' );

com.gmtplusone.tabs = function( target, options ) {
	
	var
		default_options = {
			
			target : '#tabs',
			menu_item : 'h3',
//			outer_content : false,
			callbacks : {
				opened : function() {},
				closed : function() {}
			},
			
			css : {
				active_tab_class : 'active' 
			}
		
		},
		
		properties = {
			
			menu_items : [],
			tabs : []
			
		};
	
	
	this.options = jQuery.extend( true, {}, default_options, options );
	this.options.menu_ids = [];
	this.options.target = ( typeof target === 'string' && target.length > 0 ) ? target : this.options.target;
		
	this.init = function( callback ) {
		
		this.getMenuItems();
		this.getTabs();
		this.createNewNav();
		
		if ( callback ) { callback.call(); }
		
	};
	
	
	com.gmtplusone.tabs.prototype.getMenuItems = function() {
		var self = this,
			$elm;
		
		jQuery( self.options.target + ' > ' + self.options.menu_item ).each(function( key, value ) {

			$elm = jQuery( value );		
			properties.menu_items.push( $elm );
			self.options.menu_ids.push( $elm.children().eq(0).attr('href') );
			$elm.remove();
			
		});
		
	};
	
	
	com.gmtplusone.tabs.prototype.getTabs = function() {
		var self = this,
			$elm;
		jQuery( self.options.target + ' > div' ).each(function( key, value ) {
			$elm = jQuery( value );
			properties.tabs.push( $elm );
		});	
	};
	
	
	
	com.gmtplusone.tabs.prototype.createNewNav = function() {
		
		var $nav_container = {},
			i = properties.menu_items.length - 1;
		
		jQuery( this.options.target ).prepend('<div class="tab-navigation"></div>');
		$nav_container = jQuery( this.options.target + ' .tab-navigation' );
		
		for ( i; i >= 0; i -= 1 ) {
			
			$nav_container.prepend( properties.menu_items[i] );
			properties.menu_items[i].children().eq(0)
				.bind( 'click', { self : this }, 
					this.handleMenuClick
				);
			
		}
	};
	
	
	com.gmtplusone.tabs.prototype.handleMenuClick = function( e ) {
		var self = e.data.self,
			$item = jQuery(this);
		self.toggleTab( $item.attr('href') );
		e.preventDefault();
	};
	

	com.gmtplusone.tabs.prototype.getOpenTabId = function() {
		var i,
		ii = properties.tabs.length;

		for ( i = 0; i < ii; i += 1 ) {
			var i_id = properties.tabs[i].attr('id');
			var $elm = jQuery('#' + i_id); 
			
			if ($elm.hasClass("active")){
				return i_id;
			}
		}
		return null;
	}
		
	/**
	 * TODO rename to "show", as this function doesn't actually "toggle".
	 * 
	 * Shows the tab identified by @id, and ensures that all other tabs are closed.
	 * */
	com.gmtplusone.tabs.prototype.toggleTab = function( id ) {
		var options = this.options;
		
		// IE7 fix for cases where id appears with full website url
		if(id.indexOf("#")>0){
			id = id.substr(id.indexOf("#"), id.length);
		}
			
		var atag = jQuery('a[href="' + id + '"]');
		
		var visibleTabIds = [];


			var innerDivSelector = this.options.target + " > div";
			var tabs = jQuery(innerDivSelector).not('.tab-navigation');
			tabs.each(function(index, tab){
				tab = jQuery(tab);
				
				if("#" + tab.attr("id") == id){
					tab.addClass( options.css.active_tab_class );
					tab.slideDown();
				}
				else{
					tab.removeClass( options.css.active_tab_class );
					tab.slideUp();
				}
			});

			var tabLinks = options.menu_ids;


			jQuery(tabLinks).each(function(index, ob){
				
				// IE7 fix for cases where id appears with full website url
				if(ob.indexOf("#")>0){
					ob = ob.substr(ob.indexOf("#"), ob.length);
				}
				var objectX = jQuery(ob);
				var megaSelector = options.target + ' div.tab-navigation a[href$="' + ob + '"]';
				var menuLink = jQuery(megaSelector);  
				
				if("#" + objectX.attr("id") == id){
					menuLink.addClass( options.css.active_tab_class );
					options.callbacks.opened.call( atag, "#" + objectX.attr("id") );
				}
				else{
					menuLink.removeClass( options.css.active_tab_class );
					options.callbacks.closed.call( atag, "#" + objectX.attr("id") );
				}
				
			});
			
			
			/*
			jQuery(tabLinks).each(function(index, ob){
				var objectX = jQuery(ob);
				var menuLink = jQuery(options.target + ' div.tab-navigation a[href="#' + objectX.attr("id") + '"]');  
				if("#" + objectX.attr("id") == id){
					
					menuLink.addClass( options.css.active_tab_class );
					options.callbacks.opened.call( atag, "#" + objectX.attr("id") );

				}
				else{
					menuLink.removeClass( options.css.active_tab_class );
					options.callbacks.closed.call( atag, "#" + objectX.attr("id") );
				}
			});
			*/


	};

};
