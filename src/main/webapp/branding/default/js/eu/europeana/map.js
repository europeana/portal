js.utils.registerNamespace('eu.europeana.mapview');


eu.europeana.mapview = {
    
	ajax_src_prefix : eu.europeana.vars.branding + '/js/simile-widgets/ajax-2.2.2/',
    timeline_src_prefix : eu.europeana.vars.branding + '/js/simile-widgets/timeline-2.3.1/',
    timeplot_src_prefix : eu.europeana.vars.branding + '/js/simile-widgets/timeplot-1.1/',
    default_locale : 'en',
    user_locale : eu.europeana.vars.locale,
    tl : {},
    timeplot : {},
    core:null,
    $loader_bubble : jQuery('#loader-bubble'),
    
    load : function() {
    	// don't bother if we're runnng in IE6
    	if(navigator.userAgent.indexOf("MSIE 6") > -1 ){
        	eu.europeana.mapview.$loader_bubble.fadeOut(2000);
        	jQuery('#e4DContainer').hide();
        	jQuery('#e4DContainer').append(jQuery("#noIE").html());
        	jQuery('#e4DContainer').fadeIn(3500);
        }
    	else{   		
            this.loadSimileAjax();
    	}
    },
    
    enableRefinement : function( total_hits ) {
    	jQuery("#refine-search").toggleClass("disabled", total_hits==0);
    },
    
    placeTotalHits : function( total_hits ) {
    	
    	var hits_html =
    		'<ul class="navigation-pagination">' +
				'<li class="page-nr">' +
					eu.europeana.vars.msg.results + ': ' + js.utils.addCommas( total_hits ) +
				'</li>' +
				'<li class="page-nr" id="item-limit">' +
					eu.europeana.vars.mapview.limit_exceeded +
				'</li>' +
			'</ul>';
    	
    	jQuery('.navigation-icons').after( jQuery( hits_html ) );
    	
   		if(total_hits > eu.europeana.vars.mapview.limit){
    		jQuery('#item-limit').fadeIn();
    	}
    },
    
    loadSimileAjax : function() {
        
    	window.SimileAjax = {
			urlPrefix : eu.europeana.mapview.ajax_src_prefix,
			Platform : {}
		};
		
		js.loader.loadScripts([{
			file: 'simile-ajax-bundle-no-jquery.js',
			path: eu.europeana.mapview.ajax_src_prefix,
			callback : function() { SimileAjax.History.enabled = false; eu.europeana.mapview.loadTimeline(); }
		}]);
        
    },
    
    
    loadTimeline : function() {
        
    	window.Timeline = {    			
			DateTime : window.SimileAjax.DateTime,
			urlPrefix : eu.europeana.mapview.timeline_src_prefix,
			serverLocale : eu.europeana.mapview.default_locale,
            clientLocale : eu.europeana.mapview.default_locale
		};
		
		js.loader.loadScripts([{
			file: 'timeline-bundle-modified.js',
			path: eu.europeana.mapview.timeline_src_prefix
		}]);
		
		js.loader.loadScripts([{
			file: 'labellers.js',
			path: eu.europeana.mapview.timeline_src_prefix + 'scripts/l10n/' + this.default_locale + '/',
			dependencies : ['timeline-bundle-modified.js']
		}]);
		
		js.loader.loadScripts([{
			file: 'timeline.js',
			path: eu.europeana.mapview.timeline_src_prefix + 'scripts/l10n/' + this.default_locale + '/',
			dependencies : ['timeline-bundle-modified.js', 'labellers.js'],
			callback : eu.europeana.mapview.loadTimeplot
		}]);
        
    },
    
    
    loadTimeplot : function() {
        
    	window.Timeplot = {
    		urlPrefix:	eu.europeana.mapview.timeplot_src_prefix,
    		loaded:     false,
            params:     { bundle: true, autoCreate: true },
            namespace:  "http://simile.mit.edu/2007/06/timeplot#",
            importers:  {}
        };
    	
    	js.loader.loadScripts([{
			file: 'timeplot-bundle.js',
			path: eu.europeana.mapview.timeplot_src_prefix,
			//callback: eu.europeana.mapview.loadAdditionalScripts
			callback: eu.europeana.mapview.loadGoogleMaps
		}]);
        
    },
    
    loadGoogleMaps:function(){
    	var googleCallback = 'eu.europeana.mapview.loadAdditionalScripts';
    	js.loader.loadScripts([{
			file: 'js?v=3.2&sensor=false&callback=' + googleCallback,
			path: "http://maps.google.com/maps/api/"
		}]);
    }, 
    
    loadTestPlot : function() {
    	
    	var eventSource = new Timeplot.DefaultEventSource();
        var plotInfo = [
            Timeplot.createPlotInfo({
            	id: "plot1",
            	dataSource: new Timeplot.ColumnSource(eventSource,1)
            })
        ];
        
        this.timeplot = Timeplot.create(document.getElementById("my-timeplot"), plotInfo);
        var data_src = '/' + eu.europeana.vars.branding + '/js/simile-widgets/timeplot-1.1/data.txt';
        this.timeplot.loadText( data_src, ",", eventSource );
        
    },
    
    
	loadAdditionalScripts : function() {
    	var scripts = [];
    	
    	// could not lazy load, so put it in debug-css.ftl for the moment
    	// @link http://www.google.nl/search?aq=f&gcx=w&sourceid=chrome&ie=UTF-8&q=G_PHYSICAL_MAP+is+not+defined
    	
    	scripts.push({
			name : 'virtualearth-mapcontrol',
			file : 'mapcontrol.ashx?v=6.1',
			path : 'http://dev.virtualearth.net/mapcontrol/'
		});
    	
		scripts.push({
			file : 'slider.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/',
			dependencies : [ 'virtualearth-mapcontrol' ]
		});		
		
		/*
		scripts.push({
			name : 'googleMaps',
			file : 'js?v=3.2&sensor=false',
			path : 'http://maps.google.com/maps/api/'
		});	
		*/	
		
		//http://maps.google.com/maps/api/js?v=3.2&sensor=false
		
		/*
		scripts.push({
			name : 'googleMaps',
			//file : 'maps?file=api&v=2&key=' + eu.europeana.vars.google_maps_key,
			file : 'js?sensor=false&key=' + eu.europeana.vars.google_maps_key,
			path : 'http://maps.googleapis.com/maps/api/'
		});	
		*/	
		/*
		http://maps.googleapis.com/maps/api/js?ABQIAAAAw5pOymCJNlguY_xAJt8XZBSyXC-ut-A2RKV0fpiiIqeKpeG3JxRwvyCPqRFUaBUJUTUl8LYa0n8ywA

		  */
		 scripts.push({
			file : 'OpenLayers.js',
			path : eu.europeana.vars.branding + '/js/org/openlayers/openlayers-2.11/',
			dependencies : ['slider.js']//, 'googleMaps']
		 });		
		 
		scripts.push({
			file : 'timeplot-modify.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'OpenLayers.js' ]
		});			
		
		scripts.push({
			file : 'STIStatic-modified.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'timeplot-modify.js' ]
		});
		
		scripts.push({
			file : 'ZoomSlider.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'STIStatic-modified.js' ]
		});
		
		scripts.push({
			name : 'STIMap',
			file : 'STIMap-modified.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'ZoomSlider.js' ]
		});
		
		scripts.push({
			file : 'STITimeplot.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'STIMap' ]
		});
		
		scripts.push({
			file : 'STICore.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'STITimeplot.js' ]
		});
		
		scripts.push({
			file : 'STIGui.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'STICore.js' ]
		});
		
		scripts.push({
			file : 'STIPopup-modified.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'STIGui.js' ]
		});
		
		scripts.push({
			file : 'DataObject.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'STIPopup-modified.js' ]
		});
		
		scripts.push({
			file : 'DataSet.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'DataObject.js' ]
		});
		
		scripts.push({
			file : 'ExtendedDataSource.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'DataSet.js' ]
		});
		
		scripts.push({
			file : 'ExtendedSimileTimeDate.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'ExtendedDataSource.js' ]
		});
		
		scripts.push({
			file : 'PointObject.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'ExtendedSimileTimeDate.js' ]
		});
		
		scripts.push({
			file : 'delaunay.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'PointObject.js' ]
		});
		
		scripts.push({
			file : 'kruskal.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'delaunay.js' ]
		});
		
		scripts.push({
			file : 'ModifiedZoomPanel.js',
			path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/sti/',
			dependencies : [ 'kruskal.js' ],
			callback : eu.europeana.mapview.loadMap
		});
		
		scripts.push({
			file: 'addthis' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/com/addthis/' + js.min_directory,
			callback: function(){
				eu.europeana.mapview.addThis();
			}
		});
		
		js.loader.loadScripts( scripts );
    },
    
    loadMap : function() {
    	   	
		jQuery('#query-search').submit(function(){
			if(jQuery('#box_search').is(':checked')){
				jQuery('#query-search').append('<input type="hidden" name="coords" value="' + eu.europeana.mapview.core.map.getCoords() + '"/>');
			}
		});

		jQuery('#refine-search-form').submit(function(){
			if(jQuery('#box_search_refine').is(':checked')){
				jQuery('#refine-search-form').append('<input type="hidden" name="coords" value="' + eu.europeana.mapview.core.map.getCoords() + '"/>');
			}
		});
		
		
		eu.europeana.mapview.core = new STICore();
		
		 // If there's no query then there's no data (exit).
		if(!eu.europeana.vars.query){
	    	eu.europeana.mapview.$loader_bubble.fadeOut();
			return;
		}
    	
    	eu.europeana.mapview.core.loadJson(eu.europeana.vars.mapview.json_url);
    	if (!eu.europeana.mapview.core.jsonLoaded){
    		eu.europeana.mapview.showNoResultsMsg();
    	}
    	else{
    		eu.europeana.mapview.enableRefinement( STIStatic.resultSize );
			eu.europeana.mapview.placeTotalHits( STIStatic.resultSize);

			if(eu.europeana.vars.mapview.json_url.indexOf("coords=")>-1){
				var coords = eu.europeana.vars.mapview.json_url.substr(eu.europeana.vars.mapview.json_url.indexOf("coords=")+7);
				if(coords.indexOf("&")>-1){
					coords=coords.substr(0, coords.indexOf("&"));
				}
				if(coords.indexOf("?")>-1){
					coords=coords.substr(0, coords.indexOf("?"));
				}
				eu.europeana.mapview.core.map.zoomCoords(coords);
				jQuery("#box_search").attr('checked','checked');
				jQuery("#box_search_refine").attr('checked','checked');
			}
    	}
    	eu.europeana.mapview.$loader_bubble.fadeOut();
    },
    
    showNoResultsMsg : function() {
    	jQuery('#e4DContainer').html('<div id="e4DContainer-noresults">' + eu.europeana.vars.mapview.json_noresult1 + '<br /><br />' + eu.europeana.vars.mapview.json_noresult2 +  '</div>');
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
		
				
	    	if(navigator.userAgent.indexOf("MSIE") > -1 ){
	    		jQuery('.search-results-navigation').append(
    				com.addthis.getToolboxHtml({
						html_class : 'addthis',
						url : url,
						title : title,
						description : description,
						services : {
							compact : {},
							twitter : {},
							google_plusone : { count : 'false' }
						}
    				})
					);
	    	}
	    	else{
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
							facebook_like : {}
			    			//facebook_like : { layout : 'box_count' }
			    			//facebook_like : { layout : 'button_count' }
			    		}
			    	})
		    		);
    		}
			
	    	

		jQuery('.addthis').hide();
		com.addthis.init( null, true, false );
		setTimeout( function() { jQuery('.addthis').fadeIn(); }, 500 );
		/*
		*/
	}

};

eu.europeana.mapview.load();
