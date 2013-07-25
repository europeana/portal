js.utils.registerNamespace('eu.europeana.timeline');

eu.europeana.timeline = {
    
    ajax_src_prefix : eu.europeana.vars.branding + '/js/simile-widgets/ajax-2.2.2/',
    timeline_src_prefix : eu.europeana.vars.branding + '/js/simile-widgets/timeline-2.3.1/',
    default_locale : 'en',
    user_locale : eu.europeana.vars.locale,
    tl : {},
    tl_requests : [],
    $loader_bubble : jQuery('#loader-bubble'),
    
    startFrom : ( eu.europeana.vars.startFrom && parseInt( eu.europeana.vars.rows, 10 ) > 0 ) ? parseInt( eu.europeana.vars.rows, 10 ) : 1,
    rows_limit : 1000,
    rows : ( eu.europeana.vars.rows && parseInt( eu.europeana.vars.rows, 10 ) > 0 ) ? parseInt( eu.europeana.vars.rows, 10 ) : 1000,
    initial_rows : ( eu.europeana.vars.initial_rows && parseInt( eu.europeana.vars.initial_rows, 10) > 0 ) ? parseInt( eu.europeana.vars.initial_rows, 10 ) : undefined,
    initial_data_placed : false,
    
    
    load : function() {
    	
    	this.loadComponents();
    	this.resizeTimeline();
        this.loadSimileAjax();
        jQuery(window).bind('resize', this.handleOnResize );
        jQuery(window).bind('beforeunload', this.handleUnload );
    },
    
	loadComponents : function() {
		
		var self = eu.europeana.timeline;
		
		js.loader.loadScripts([{
			file: 'addthis' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/com/addthis/' + js.min_directory,
			callback : function() { self.addThis(); }
		}]);
		
	},    
    
    loadSimileAjax : function() {
    	
    	window.SimileAjax = {
			urlPrefix : this.ajax_src_prefix,
			Platform : {}
		};
		
		js.loader.loadScripts([{
			file: 'simile-ajax-bundle-no-jquery.js',
			path: eu.europeana.timeline.ajax_src_prefix,
			callback : function() { SimileAjax.History.enabled = false; eu.europeana.timeline.loadTimeline(); }
		}]);
        
    },
    
    
    loadTimeline : function() {
        
    	window.Timeline = {    			
			DateTime : window.SimileAjax.DateTime,
			urlPrefix : this.timeline_src_prefix,
			serverLocale : this.default_locale,
            clientLocale : this.default_locale
		};
		
		js.loader.loadScripts([{
			file: 'timeline-bundle-modified.js',
			path: eu.europeana.timeline.timeline_src_prefix
		}]);
		
		js.loader.loadScripts([{
			file: 'labellers.js',
			path: eu.europeana.timeline.timeline_src_prefix + 'scripts/l10n/' + this.default_locale + '/',
			dependencies : ['timeline-bundle-modified.js']
		}]);
		
		js.loader.loadScripts([{
			file: 'timeline.js',
			path: eu.europeana.timeline.timeline_src_prefix + 'scripts/l10n/' + this.default_locale + '/',
			dependencies : ['timeline-bundle-modified.js', 'labellers.js'],
			callback : eu.europeana.timeline.setupTimeline
		}]);
		
    },
    
    
    setupTimeline : function() {
    	
    	var eventSource = new Timeline.DefaultEventSource(),
    		bandInfos = eu.europeana.timeline.getBandInfos( eventSource );
	    	
			bandInfos[0].syncWith = 2;			// synchronizes the band movement of the first band [0] with the third[2]
			bandInfos[1].syncWith = 2;			// synchronizes the band movement of the second band [1] with the third[2]
			bandInfos[1].highlight = true;		// creates a highlight on the second band
		
			eu.europeana.timeline.tl = Timeline.create( document.getElementById("tl"), bandInfos );
			
			Timeline.DefaultEventSource.Event.prototype.fillInfoBubble = function( elmt, theme, labeller ) {
				
				eu.europeana.timeline.fillInfoBubble( this, elmt, theme, labeller );
				
			};
			
		eu.europeana.timeline.getEvents( eventSource );
        
    },
    
    
    getEvents : function( eventSource, startFrom, rows ) {
	    
    	/**
    	 *	event_source_url
    	 *	@see eu.europeana.web.controllers.standard.search.SearchAsJsonController
    	 *	
    	 *	startFrom - the row to start with, so &startFrom=13 would start with the 13th row, or more plainly, the 13th item
    	 *	rows - each item is considered a row, so &rows=12 would return 12 items
    	 *	initial_rows - initial batch start; e.g. start with 200 then continue with amount set by rows
    	 *	
    	 *	default start is 1
    	 *	default limit on nr of rows/items returned is 1000
    	 */
    	var	startFrom = ( startFrom ) ? startFrom : eu.europeana.timeline.startFrom,
    		rows = ( rows ) ? rows : eu.europeana.timeline.rows,
    		rows_limit = eu.europeana.timeline.rows_limit,
    		initial_rows = eu.europeana.timeline.initial_rows,
    		startFrom_param = ( startFrom > 0 ) ? '&startFrom=' + startFrom : '',
    		rows_param = ( rows > 0 ) ? '&rows=' + rows : '',
    		event_source_url = eu.europeana.vars.event_source_url + startFrom_param;
    		
    		if ( startFrom > rows_limit ) {
    			
    			return;
    		
    		}
    		
    		if ( initial_rows && !eu.europeana.timeline.initial_data_placed ) {
    			
    			rows_param = '&rows=' + initial_rows;
    			startFrom += initial_rows;
	    		
	    	} else if ( startFrom < rows ) {
	    		
	    		startFrom += rows;
	    		
	    	}
			
	    	event_source_url += rows_param;
	    	
	    	// if we have no search query then exit here.
		    if(!eu.europeana.vars.query){
				eu.europeana.timeline.$loader_bubble.fadeOut();
		    	return;
		    }


    	eu.europeana.timeline.tl_requests.push( jQuery.ajax({
			
			url :		event_source_url,
			dataType :	'json',
			timeout :	15000,
			
			success :	function( data ) {
							
							if ( eu.europeana.timeline.initial_data_placed ) {
								
								startFrom += rows;
								
							}
							
							eu.europeana.timeline.placeEvents( data, eventSource, event_source_url );
							
							if ( rows <= rows_limit && startFrom < data.totalResults ) {

								eu.europeana.timeline.getEvents( eventSource, startFrom, rows );
								
							}
							
							
							
						},
						
			error :		function( request, textStatus, errorThrown ) {
							
							eu.europeana.timeline.$loader_bubble.fadeOut();
							alert( eu.europeana.vars.msg.ajax_data_retrieval_error + '( ' + textStatus + ' )' );
							
						}
			
		}));
    	
    },
    
    
    placeEvents : function( data, eventSource, event_source_url ) {
    	
    	if ( !data || !eventSource || !event_source_url ) {
    		
    		return;
    		
    	}
    	
    	eu.europeana.timeline.$loader_bubble.fadeOut();
		eventSource.loadJSON( data, event_source_url );
		
		if ( !eu.europeana.timeline.initial_data_placed && data.events && data.events.length > 0 ) {
			
			eu.europeana.timeline.centerOnMiddleDate( eu.europeana.timeline.tl, data );
			eu.europeana.timeline.initial_data_placed = true;
			eu.europeana.timeline.placeTotalHits( data.totalResults );
			eu.europeana.timeline.enableRefinement( data.totalResults );
		} else {
			eu.europeana.timeline.enableRefinement( 0 );
		}
    	
    	js.console.log('asynchronous events placed on the timeline');
    	
    },
    
    
    getBandInfos : function( eventSource ) {
    
    	return [
		   
			Timeline.createBandInfo({
				eventSource :		eventSource,
				width :				'8%',
				intervalUnit :		Timeline.DateTime.MILLENNIUM,
				intervalPixels :	400,
				overview :			true
			}),
		   
			Timeline.createBandInfo({
				eventSource :		eventSource,
				width :				'10%',
				intervalUnit :		Timeline.DateTime.CENTURY,
				intervalPixels :	350,
				overview :			true
			}),

			Timeline.createBandInfo({
				eventSource :		eventSource,
				width :				'80%',
				intervalUnit :		Timeline.DateTime.YEAR,
				intervalPixels :	120,
				eventPainter :		Timeline.CompactEventPainter,
				eventPainterParams :	{
					iconLabelGap :		20,
					labelRightMargin :	20,
					iconWidth :			20,
					iconHeight :		20,
					stackConcurrentPreciseInstantEvents: {
						limit: 8,
						moreMessageTemplate:    "%0 More Objects"
					}
				}
			})
		];
    	
    },
    
    
    centerOnEarliestDate : function( timeline, eventSource ) {
    	
    	timeline.getBand(0).setCenterVisibleDate(
    		
    		eventSource.getEarliestDate()
    	
    	);
    	
    },
    
    
    /**
     * nb: while developing the application only a year was returned as the start date. in order for the
     * javascript date object to produce a proper date stamp for ie7 the month and day are appended as 1, 1 
     * 
     * @param timeline
     * @param data
     */
    centerOnMiddleDate : function( timeline, data ) {
    	
    	var date = data.events[ Math.floor( data.events.length / 2 ) ].start,
    		date_to_use = js.utils.isNumber( date ) ? new Date( date, 1, 1 ) : new Date( date );
    	
    	timeline.getBand(0).setCenterVisibleDate( date_to_use );
    	
    },
    
    
    /**
     * nb: while developing the application only a year was returned as the start date. in order for the
     * javascript date object to produce a proper date stamp for ie7 the month and day are appended as 1, 1 
     * 
     * @param timeline
     * @param data
     */
    centerOnRandomDate : function( timeline, data ) {
    	
    	var date = data.events[ Math.floor( Math.random() * data.events.length ) ].start,
			date_to_use = js.utils.isNumber( date ) ? new Date( date, 1, 1 ) : new Date( date );
		
    	timeline.getBand(0).setCenterVisibleDate( date_to_use );
    	
    },
    
    
    handleOnResize : function(e) {
    	
    	if ( this.resizeTimerID ) {
    		
    		clearTimeout( this.resizeTimerID );
    		
    	}
    	
    	this.resizeTimerID = setTimeout(
    		
    		function() {
    			
    			eu.europeana.timeline.resizeTimeline();
    			eu.europeana.timeline.tl.layout();
    			
    		},
    		
    		500
            
        );
        
    },
    
    
    resizeTimeline : function() {
    	
    	jQuery('#tl').css( 'height' , jQuery(window).height() - 350 ); // set timeline height (minus height needed for other elements above)
    	
    },

    
    placeTotalHits : function( total_hits ) {
    	
    	var hits_html =
    		'<ul class="navigation-pagination">' +
				'<li class="page-nr">' +
					eu.europeana.vars.msg.results + ' ' + js.utils.addCommas( total_hits ) +
				'</li>' +
				'<li class="page-nr" id="item-limit">' +
					eu.europeana.vars.limit_exceeded +
				'</li>' +
			'</ul>';
    	
    	jQuery('.navigation-icons').after( jQuery( hits_html ) );
    	
    	if ( total_hits > 1000 ) {
    		
    		jQuery('#item-limit').fadeIn();
    		
    	}
    	
    },
    
    enableRefinement : function( total_hits ) {
    	jQuery("#refine-search").toggleClass("disabled", total_hits==0);
    },
    
    
    fillInfoBubble : function ( context, elmt, theme, labeller ) {  
    	
    	var doc = elmt.ownerDocument;
        
		var title = context.getText();
		var link = context.getLink();
		var image = context.getImage();
		
		if (image != null) {
			
		    var img = doc.createElement("img"),
		    	img_a = doc.createElement("a");
		    
		    img.src = image;
		    theme.event.bubble.imageStyler(img);
		    
		    img_a.href = link;
		    img_a.title = title;
		    img_a.appendChild(img);
		    
		    elmt.appendChild(img_a);
		    
		}
		
		var divTitle = doc.createElement("div");
		var textTitle = doc.createTextNode(title);
		
		if (link != null) {
			
		    var a = doc.createElement("a");
		    a.href = link;
		    a.appendChild(textTitle);
		    divTitle.appendChild(a);
		    
		} else {
		    
			divTitle.appendChild(textTitle);
			
		}
		
		theme.event.bubble.titleStyler(divTitle);
		elmt.appendChild(divTitle);
		
		var divBody = doc.createElement("div");
		context.fillDescription(divBody);
		theme.event.bubble.bodyStyler(divBody);
		elmt.appendChild(divBody);
		
		var divTime = doc.createElement("div");
		//context.fillTime(divTime, labeller);
		
		//divTime.appendChild( doc.createTextNode( eu.europeana.vars.msg.date + ': ' + context._obj.start ) );
		
		var divTimeLabel = doc.createElement("span");
		divTimeLabel.className = "timeline-label";
		divTimeLabel.appendChild( doc.createTextNode( eu.europeana.vars.msg.date + ': ' ) );
		
		divTime.appendChild( divTimeLabel );
		divTime.appendChild( doc.createTextNode( context._obj.start ) );
		theme.event.bubble.timeStyler(divTime);
		
		elmt.appendChild(divTime);
		
		var divWiki = doc.createElement("div");
		context.fillWikiInfo(divWiki);
		theme.event.bubble.wikiStyler(divWiki);
		elmt.appendChild(divWiki);
		
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
					facebook_like : { layout : 'button_count', width: '51' }
				}
				
			})
			
		);

		jQuery('.addthis').hide();
		com.addthis.init( null, true, false );
		setTimeout( function() { jQuery('.addthis').fadeIn(); }, 500 );
		
	},
	
	
	handleUnload : function(e) {
		
		var i,
			ii = eu.europeana.timeline.tl_requests.length;
		
		for ( i = 0; i < ii; i += 1 ) {
			
			if ( 4 !== eu.europeana.timeline.tl_requests[i].readyState ) {
				
				eu.europeana.timeline.tl_requests[i].abort();
				
			}
			
		}
		
	}
    

};

eu.europeana.timeline.load();