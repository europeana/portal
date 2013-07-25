/**
 *  header.js
 *
 *  @package	eu.europeana
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @created	2011-09-09 13:52 GMT +1
 *  @version	2011-09-09 13:52 GMT +1
 */

/**
 *  @package	eu.europeana
 *  @author		dan entous <contact@gmtplusone.com>
 */
js.utils.registerNamespace( 'eu.europeana.search_widget' );

eu.europeana.search_widget = {
	
	$widget_help : jQuery('#widget-help'),
	$color_swatches : jQuery('#color-swatches'),
	$generate_code : jQuery('#widget-generate-code input:button'),
	$terms : jQuery('#widget-generate-code input:checkbox'),
	$code_section : jQuery('#widget-code'),
	$code : jQuery('#widget-code textarea'),
	$preview : jQuery('#preview'),
	$iframe : jQuery('#preview iframe'),
	
	$background_color : jQuery('#widget-background-color input'),
	$color : jQuery('#widget-text-color'),
	$language : jQuery('#widget-default-language select'),
	$search_term : jQuery('#widget-default-search-term input'),
	$media_types : jQuery('#widget-media-types select'),
	$providers : jQuery('#widget-providers select'),
	$logo : jQuery('#widget-logo-attribution input'),
	
	defaults : {
		background_color : 'transparent',
		color : '#000000',
		language : 'en',
		search_term : '*:*',
		rsw_def_query : '*:*',
		media_types : '',
		providers : '',
		logo : 'poweredbyeuropeanaBlack.png'
	},
	

	init : function() {
		
		this.setIframeVariables();
		this.addListeners();
		
	},
	
	addListeners : function() {
		
		var self = this;
		
		jQuery('#widget-heading input:reset').bind( 'click', { self : self }, this.clearColorSelection );
		jQuery('#widget-heading input:button').bind( 'click', { self : self }, this.toggleHelp );
		jQuery(document).bind('click', { self : self }, this.hideColorPicker );
		
		jQuery('.color-picker')
			.bind('click', { self : self }, this.showColorPicker )
			.bind('focus', { self : self }, this.showColorPicker )
			.bind('blur', { self : self }, this.hideColorPicker );
		
			
		jQuery('#color-swatches .color-swatch')
			.each( function( index, value ) {
				jQuery(value).bind( 'click', { self : self }, eu.europeana.search_widget.selectThisColor );
			});
		
		this.$language.bind('change', { self : self }, eu.europeana.search_widget.setIframeVariables );		
		this.$search_term.bind('change', { self : self }, eu.europeana.search_widget.setIframeVariables );		
		this.$media_types.bind('change', { self : self }, eu.europeana.search_widget.setIframeVariables );		
		this.$providers.bind('change', { self : self }, eu.europeana.search_widget.setIframeVariables );		
		this.$logo.bind('change', { self : self }, eu.europeana.search_widget.setIframeVariables );
		this.$terms.bind('change', { self : self }, eu.europeana.search_widget.toggleGenerateButton );
		this.$generate_code.bind('click', { self : self }, eu.europeana.search_widget.showCode );
		
	},
	
	
	toggleHelp : function(e) {
		
		var self = e.data.self;
		
		if ( self.$widget_help.is(':hidden') ) {
			
			self.$widget_help.slideDown();
			
		} else {
			
			self.$widget_help.slideUp();
			
		}
		
	},
	
	
	showColorPicker : function(e) {
		
		var self = e.data.self;
		e.stopPropagation();
		
		if ( self.$color_swatches.is(':hidden') || jQuery(this) !== self.$color_swatches.data['color-picker'] ) {
			
			self.$color_swatches
				.css({
					
					top : jQuery(this).position().top + 25,
					left : jQuery(this).position().left
					
				})
				.fadeIn();
			
			self.$color_swatches.data = { 'color-picker' : jQuery(this) };
			
		}
		
	},
	
	
	hideColorPicker : function(e) {
		
		var self = e.data.self;
		
		if ( self.$color_swatches.is(':visible') ) {
			
			self.$color_swatches.fadeOut();
			
		}

	},
	
	
	clearColorSelection : function(e) {
		
		var self = e.data.self;
		
		jQuery('div.color-picker').each( function( index, value ) {
			
			jQuery(this).css('background-color', 'transparent');
			
		});
		
		setTimeout( function() { self.setIframeVariables(); }, 100 );
		
	},
	
	
	selectThisColor : function(e) {
		
		var self = e.data.self,
			selected_color = jQuery(this).css('background-color');
		
		if ( 'rgba(0, 0, 0, 0)' !== selected_color && 'transparent' !== selected_color ) {
			
			if ( 'rgb' === selected_color.substring( 0, 3 ) ) {
			
				selected_color = '#' + self.convertToHexColor( selected_color );
			
			}
			
		} else {
			
			selected_color = 'transparent';
			
		}
		
		
		if ( self.$color_swatches.data['color-picker'].is('div') ) {
			
			self.$color_swatches.data['color-picker'].css( 'background-color', selected_color );
			self.$color_swatches.data['color-picker'].next().val(selected_color);
			
		} else if ( self.$color_swatches.data['color-picker'].is('input') ) {
			
			self.$color_swatches.data['color-picker'].prev().css( 'background-color', selected_color );
			self.$color_swatches.data['color-picker'].val(selected_color);
			
		}
		
		self.setIframeVariables();
		
	},
	
	convertToHexColor : function( rgb ) {
		
		var i,
			derived_value,
			hex_value = '',
			rgb_values = rgb.replace('rgb(','').split( ',' );
		
		for ( i = 0; i < 3; i += 1 ) {
			
			derived_value = parseInt( rgb_values[i], 10 ).toString(16);
			hex_value += ( derived_value.length < 2 ) ? '0' + derived_value : derived_value;
			
		}
		
		return hex_value;
		
	},
	
	
	toggleGenerateButton : function(e) {
		
		var self = e.data.self;
		
		if ( jQuery(this).is(':checked') ) {
			
			self.$generate_code.removeAttr('disabled');
			
		} else {
			
			self.$generate_code.attr('disabled','disabled');
			self.$code_section.slideUp();
			
		}
		
	},
	
	
	showCode : function(e) {
		
		var self = e.data.self;
		
		if ( self.$terms.is(':checked') ) {
			
			self.$code_section.slideDown();
			self.$code.select();
			
		} else {
			
			self.$code_section.slideUp();
			
		}
		
	},
	
	
	setIframeVariables : function(e) {
		
		var self = (e) ? e.data.self : this,
			iframe_id =		'europeanaEmbeddedFrame',
			iframe_name =	'europeanaEmbeddedFrame',
			iframe_style =	'overflow: hidden; width: 680px; height: 825px; border: none;',
			iframe_src =	eu.europeana.vars.portal_server + eu.europeana.vars.portal_name + '/search.html?' +
						 	self.getSearchTerm() +
						 	self.getRswDefQuery() +
						 	self.getRswUserId() +
						 	self.getBackgroundColor() +
						 	self.getColor() +
						 	self.getLanguage() +
						 	self.getMediaTypes() +
						 	self.getProviders() + 
						 	self.getLogo() +
						 	'&bt=sw' +
						 	'&embedded=true',
			iframe_html = 	'<!-- Europeana Search Widget : http://europeana.eu -->\n' +
							'<iframe ' +
								'src="' + iframe_src + '" ' +
								'id="' + iframe_id + '" ' +
								'name="' + iframe_id + '" ' +
								'style="' + iframe_style + '" ' +
								'scrolling="no" ' +
								'frameborder="0" ' +
								'allowtransparency="true"' +
							'>' +
							'</iframe>';
		
		if ( self.$iframe.length < 1 ) {
						
			self.$preview.html( iframe_html );
			self.$iframe = jQuery('#preview iframe');
				
		} else {
			
			self.$iframe.attr( 'src', iframe_src );
			
		}
		
		self.$code.text( iframe_html );
		
		
	},
	
	
	getLanguage : function() {
		
		var language = '&lang=';
		language += this.$language.val() || this.defaults.language;
		return language;
		
	},
	
	
	getBackgroundColor : function() {
		
		var background_color = '&embeddedBgColor=';
		
		background_color +=
			( this.$background_color.val() )
			? encodeURIComponent( this.$background_color.val() )
			: encodeURIComponent( this.defaults.background_color );
			
		return background_color;
		
	},
	
	
	getColor : function() {
		
		var color = '&embeddedForeColor=',
			selected_color = this.$color.find('input').val();
		
		color +=
			( selected_color )
			? encodeURIComponent( selected_color )
			: encodeURIComponent( this.defaults.color );
			
		return color;
		
	},
	
	
	getSearchTerm : function() {
		
		var search_term = 'query=';
		
		search_term +=
			( this.$search_term.val() )
			? encodeURIComponent( this.$search_term.val() )
			: this.defaults.search_term;
			
		return search_term;
		
	},
	
	
	getRswDefQuery : function() {
		
		var rsw_def_query = '&rswDefqry=';
		
		rsw_def_query +=
			( this.$search_term.val() )
			? encodeURIComponent( this.$search_term.val() )
			: this.defaults.rsw_def_query;
			
		return rsw_def_query;
		
	},
	
	
	getRswUserId : function() {
		
		var rsw_user_id = '&rswUserId=';
		rsw_user_id += new Date().getTime();
		return rsw_user_id;
		
		
	},
	
	
	getMediaTypes : function() {
		
		var media_types = '';
		
		if ( null !== this.$media_types.val() ) {
			
			this.$media_types.find(':selected').each( function( index, value ) {
				
				var elm_value = jQuery(value).val();
				
				if ( 'all' === elm_value ) {
					
					media_types = '';
					return false;
					
				}
				
				media_types += '&qf=TYPE:' + elm_value;
					
			});
			
		}
		
		return media_types;
		
	},
	
	
	getProviders : function() {
		
		var providers = '';
		
		if ( null !== this.$providers.val() ) {
			
			this.$providers.find(':selected').each( function( index, value ) {
				
				var elm_value = jQuery(value).val();
				
				if ( 'all' === elm_value ) {
					
					providers = '';
					return false;
					
				}
				
				providers += '&qf=PROVIDER:' + elm_value;
					
			});
			
		}
		
		return providers;
		
	},
	
	
	getLogo : function() {
		
		var embedded_logo = '&embeddedLogo=';
		
		this.$logo.each( function( index, value ) {
			
			var elm = jQuery(this);
			
			if ( jQuery(value).is(':checked') ) {
				
				embedded_logo += elm.val();
				return false;
					
			}
			
		});
		
		return embedded_logo;
		
	}


};

eu.europeana.search_widget.init();
			