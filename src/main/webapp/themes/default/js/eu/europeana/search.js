js.utils.registerNamespace( 'eu.europeana.search' );

eu.europeana.search = {
	
	facet_sections : [],

	init : function() {
		
		this.loadComponents();
		this.setupFacetSections();
		this.openActiveFacetSections();
		
		// Andy: conditional load test
		jQuery("#query-input").focus(function(){
			europeana_bootstrap.common.loadResultSizer(
				function(){ console.log("in callback for search.js loadResultSizer"); }
			);
		});

	},
	
	loadComponents : function() {
		
		var self = eu.europeana.search;
		
		js.loader.loadScripts([{
			file: 'addthis' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/com/addthis/' + js.min_directory,
			callback : function() { self.addThis(); }
		}]);
		
	},
	
	
	setupFacetSections : function() {
		var self = this;
		jQuery('#filter-search > li').not(".ugc-li").each( function( key, value ) {
			var $elm = jQuery( value ),
			facet_section;
			facet_section = self.addFacetSectionDetail( $elm );
			self.facet_sections[facet_section].$heading.bind( 'click', { self : self, facet_section : facet_section }, self.handleFacetSectionClick );
			self.setFacetSectionHeight( self.facet_sections[facet_section].$facets, self.facet_sections[facet_section].$heading );
		});
	},
	
	
	addFacetSectionDetail : function( $elm ) {
		
		this.facet_sections.push({
			
			$section : $elm,
			$heading : $elm.children().eq(0).children(),
			$facets : $elm.children().eq(1)
			
		});
		
		return this.facet_sections.length - 1;
		
	},
	
	
	setFacetSectionHeight : function( $facet_section, $heading ) {
		var maxheight = 100;
		if ( $heading.hasClass('facet-media') ) {
			maxheight = 125;
		}
		var height = ( $facet_section.outerHeight( true ) < maxheight ) ? $facet_section.outerHeight( true ) : maxheight;
		$facet_section.css( 'height',  height );
		
	},
	
	
	handleFacetSectionClick : function( e ) {
		
		var $heading = jQuery(this),
			self = e.data.self,
			facet_section = e.data.facet_section;
		
		e.preventDefault();
		
		if ( $heading.hasClass('active') ) {
			
			self.hideFacetSection( $heading, self.facet_sections[facet_section].$facets );
			
		} else {
			
			self.showFacetSection( $heading, self.facet_sections[facet_section].$facets );
			
		}
		
	},
	
	
	showFacetSection : function( $heading, $facets ) {
		
		$heading.addClass('active');
		$facets.slideDown();
		
	},
	
	
	hideFacetSection : function( $heading, $facets ) {
		
		$heading.removeClass('active');
		$facets.slideUp();
		
	},
	
	
	openActiveFacetSections : function() {
		
		var i, ii = this.facet_sections.length;
		
		for ( i = 0; i < ii; i += 1 ) {
			
			if ( this.facet_sections[i].$heading.hasClass('active') ) {
				
				this.facet_sections[i].$facets.slideDown('fast');
				
			}
			
		}
		
	},
	
	addThis : function() {
		
		
		var url = jQuery('head link[rel="canonical"]').attr('href'),
			title = jQuery('head title').html(),
			description = jQuery('head meta[name="description"]').attr('content');
		
			window.addthis_config = com.addthis.createConfigObject({
				
				pubid : eu.europeana.vars.addthis_pubid,
				ui_language: 'en', // eu.europeana.vars.locale,
				data_ga_property: eu.europeana.vars.gaId,
				data_ga_social : true,
				data_track_clickback: true,
				ui_use_css : true
				
			});
			
			window.addthis_share = com.addthis.createShareObject({
				
				// nb: twitter templates will soon be deprecated, no date is given
				// @link http://www.addthis.com/help/client-api#configuration-sharing-templates
				templates: { twitter: title + ': ' + url + ' #europeana' }
				
			});
		
		
		jQuery('.search-results-navigation').append(
				
			com.addthis.getToolboxHtml({
				
				html_class : 'addthis',
				url : url,
				title : title,
				description : description,
				services : {
					compact : {},
					twitter : {},
					google_plusone : { count : 'false' },
					facebook_like : { layout : 'button_count', width : '51' }
				}
				
			})
			
		);

		jQuery('.addthis').hide();
		com.addthis.init( null, true, false );
		setTimeout( function() { jQuery('.addthis').fadeIn(); }, 600 );
		
	}

};

eu.europeana.search.init();
