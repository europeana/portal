js.utils.registerNamespace( 'eu.europeana.search' );

eu.europeana.search = {
	
	facet_sections : [],

	init : function() {
		
		this.loadComponents();
		
		// make facet sections collapsible
		jQuery("#filter-search li").not(".ugc-li").Collapsible(
			{
				headingSelector:"h3 a",
				bodySelector: "ul"
			}
		);
		
		// add ellipsis 
		jQuery('.ellipsis').each(
				function(i, ob){
					var fixed	= $(ob).find('.fixed');
					var html	= fixed.html();
					fixed.remove();
					new Ellipsis($(ob), 	{fixed:	'<span class="fixed">' + html + '</span>'} );					
				}
		);

		
		// add result size control
		this.setupResultSizeMenu();
		
		
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
	

	setupResultSizeMenu : function(){
		
		var menu = new EuMenu( $("#result-size-menu"),
			{
				"fn_init": function(self){
					var active = self.getActive();
					if(active.length>0){
						self.setLabel(active.find("a").attr("class"));
					}
				},
				"fn_item":function(self, selected){
					jQuery("input[name=rows]").val(selected);
					self.setLabel(selected);
				}
			}
		);
		menu.init();
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
		
		/*
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
		*/
	}

};

eu.europeana.search.init();

