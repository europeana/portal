
fnSearchWidget = function($, config){

    var self                    = this;
    var container               = false;
    var itemTemplate            = false;
    var facetTemplate           = false;
    var filterTemplate          = false;

    var debug                   = true;	// true sets localhost urls and uncompressed js
    var showFacets				= false;
    
    if( typeof config != 'undefined'){
    	console.log("config supplied: " + JSON.stringify(config) );
    	
//    	config supplied: {"qf":["PROVIDER:Athena","PROVIDER:Bildarchiv Foto Marburg","PROVIDER:Progetto ArtPast- CulturaItalia"]}
  ///  	search...aItalia (line 2)

    	
    	/* 
    	 * 
    	 * Params object can contain:
    	 * 
    	 *  TYPE
    	 *  COPYRIGHT
    	 *  LANGUAGE
    	 *  PROVIDER
    	 * 
    	 * 
    	 */
    	self.config = config;
    	
    }
    
    var addKeywordTemplate      = false;
    // TODO:
    // move wskey and URLs to an external .jsp generated file, in order to 
    // 1) generate URLs dinamically
    // 2) hide wskey, and use a distinct wskey
    var searchUrl               = 'http://test.portal2.eanadev.org/api/v2/search.json?wskey=api2demo';
    var markupUrl               = debug ? 'http://localhost:8081/portal/template.html?id=search&showFacets=' + showFacets : 'http://test.portal2.eanadev.org/portal/template.html?id=search&showFacets=' + showFacets;
    var cssUrl                  = debug ? 'http://localhost:8081/portal/themes/default/css/' : 'http://test.portal2.eanadev.org/portal/themes/default/css/';
    var responsiveContainersUrl = debug ? 'http://localhost:8081/portal/themes/default/js/eu/europeana/responsive-containers.js' : 'http://test.portal2.eanadev.org/portal/themes/default/js/eu/europeana/responsive-containers.js';

    var resultServerUrl         = 'http://europeana.eu/portal';

    var defaultRows             = 6;
    var pagination              = false;
    var paginationData          = {};
    
    
    // get markup from portal
    self.load = function(){
       	$.holdReady(true);
       	
       	var url = self.config.lang ? markupUrl + '&lang=' + self.config.lang[0] : markupUrl;

        $.ajax({
            "url" :         url,
            "type" :        "GET",
            "crossDomain" : true,
            "dataType" :    "script"
        });
        return self;
    };

    // load style / initialise events / set state - called by load callback

    self.init = function(htmlData) {
        container = $('.search-widget-container');
        container.css('background-color',	'#FFF');
        container.css('border',				'solid 1px #999');

        container.append(htmlData.markup);
        container.find('#content').hide();

        $('#query-input').val('paris');

        itemTemplate       = container.find('.thumb-frame').parent();
        facetTemplate      = container.find('#filter-search li:nth-child(2)');
        addKeywordTemplate = container.find('#filter-search li:first');
        filterTemplate     = container.find('#search-filter li:first');

        setupQuery();
        setupMenus();
        setUpRefinements(); // TODO

        pagination = new EuPagination($('.result-pagination'),
        	{
        		"ajax":true,
        		"fns":{
            		"fnFirst":function(e){
            			e.preventDefault();
            			searchWidget.search(1);
            		},
					"fnPrevious":function(e){
						e.preventDefault();
            			searchWidget.search(paginationData.start - paginationData.rows);
					},       			
            		"fnNext":function(e){
            			e.preventDefault();
            			searchWidget.search(paginationData.start + paginationData.rows);
            		},
					"fnLast":function(e){
						e.preventDefault();
            			searchWidget.search(pagination.getMaxStart());
					},
            		"fnSubmit":function(val){
						val = parseInt(val);
            			var start = ((val-1) * paginationData.rows) + 1;
   						searchWidget.search( start );            				
			            return false;
					}
        		}
        	}
        );
        
       	$.getScript("http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js", function() {
     	     $.holdReady(false);
     	});
       		
        $(document).ready(function(){
        	/*
        	container.resizable({
        		resize: function( event, ui ) {
        			$(window).trigger("widgetResize");
        		}
        	});
        	*/
        	/*
        	container.draggable();
        	 */
        	container.css('overflow-y', 'auto');
        	container.css('overflow-x', 'hidden');
        	
        	// responsive-containers initialisation
        	container.attr('data-squery', 'max-width:48em=mobile min-width:48em=desktop min-width:71em=min71em max-width:30em=max30em max-width:48em=max48em min-width:22em=min22em min-width:55em=min55em max-width:55em=max55em');
        	
           	$.getScript(responsiveContainersUrl, function() {
           		console.log('initialise responsive containers here');
        	});

        	
        });

        // load style - as single files if in debug mode
        if(debug){
			$.each(['html-sw', 'common', 'header-sw', 'menu-main', 'responsive-grid-sw', 'eu-menu', 'ellipsis', 'europeana-font-icons-widget', 'europeana-font-face', 'search-sw', 'search-pagination-sw', 'sidebar-facets-sw'], function(i, ob){
	        	$('head').append('<link rel="stylesheet" href="' + cssUrl + ob + '.css" type="text/css" />');
			});
        }
        else{
        	$('head').append('<link rel="stylesheet" href="' + cssUrl + '/min/search-widget-all.css" type="text/css" />');
        }
        
        // load jquery ui style
        $('head').append('<link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.1/themes/base/jquery-ui.css" type="text/css" />');
    };

    
    var doSearch = function(startParam, query){
    	
    	var url = buildUrl(startParam, query);
       
        if(typeof url != 'undefined' && url.length){
        	
        	if(searchUrl.indexOf('file')==0){
				getFake();
        	}
        	else{
                $.ajax({
                    "url" : url + "&profile=portal,params&callback=searchWidget.showRes",
                    "type" : "GET",
                    "crossDomain" : true,
                    "dataType" : "script"
                });
        	}
        }
        else{
            self.q.addClass('error-border');
        }
    };

    // build search param from url-fragment hrefs.  @startParam set to 1 if not specified
    var buildUrl = function(startParam, query){

        var term = self.q.val();
        if (!term) {
            return '';
        }
        
        var url = query ? searchUrl + '&' + query : searchUrl + '&query=' + term;
        
        // params
        
        url += '&rows=' + (self.resMenu1.getActive() ? self.resMenu1.getActive() : defaultRows);
        url += '&start=' + (startParam ? startParam : 1);

        // remove-filter links provide the url - return here if provided
        if(query){
        	console.log("return the pre-set query");
        	return url;
        }
        
        // refinements & facets read from hidden inputs
        
        if(showFacets){
            container.find('#refine-search-form > input').each(function(i, ob){
            	var urlFragment = $(ob).attr('value');
            	if(urlFragment.indexOf(':')>0){
            		urlFragment = urlFragment.split(':')[0] + ':' + '"' + encodeURI(urlFragment.split(':')[1] + '"');
            	}
            	url += '&' + urlFragment;
            });        	
        }
        
        
        // config supplied: {"qf":["PROVIDER:Athena","PROVIDER:Bildarchiv Foto Marburg","PROVIDER:Progetto ArtPast- CulturaItalia"]}

        
        if(self.config){
        	if(self.config.qf){
        		$.each(self.config.qf, function(i, ob){
        			
        			console.log("Full ob = " + JSON.stringify(ob) + ", "  + ob.length);
        			
        			url += '&qf=';
        			url += (ob.indexOf(' ')>-1) ? (ob.split(':')[0] + ':' + '"' + ob.split(':')[1] + '"') : ob;
        			
        			console.log('append to url: ' + url);
        		});
        	}

        }

		console.log('final search url: ' + url);
        return url;
    };

    
    var showRes = function(data){

        // Append items
        var grid = container.find('#items');
        grid.empty();

        console.log("widget showRes(data), data = \n" + JSON.stringify(data));
        var start = data.params.start ? data.params.start : 1;

        $(data.items).each(function(i, ob){
            var item = itemTemplate.clone();

            item.find('a').attr(
                'href', resultServerUrl + '/record' + ob.id + '.html?start=' + start + '&query='
            );

            item.find('a .ellipsis').prepend(
                document.createTextNode(ob.title)
            );

            item.find('.thumb-frame a').attr({
                "title": ob.title,
                "target" : "_new"
            });

            if(ob.edmPreview){
	            item.find('img').attr(
	                'src', ob.edmPreview[0]
	            );
            }
            
            grid.append(item);
        });

        setupEllipsis();

        // pagination
        
        // PETER - can't the api send back the rows paramter?
        var xRows = (self.resMenu1.getActive() ? parseInt(self.resMenu1.getActive()) : defaultRows);

        //paginationData = {"records":data.totalResults, "rows":data.itemsCount, "start":data.start};
        paginationData = {"records":data.totalResults, "rows":xRows, "start":start};
        
        //pagination.setData(data.totalResults, data.itemsCount, data.start);
        pagination.setData(
        		data.totalResults,
        		xRows,
        		start);


        // result stats
        container.find('.first-vis-record').html(start);
        container.find('.last-vis-record') .html(start - 1 + data.itemsCount);
        container.find('.last-record')     .html(data.totalResults);


        // facets
        var cbCount  = 0;
        var ugcFacet = $('#filter-search .ugc-li');
        var selected = $('#filter-search input[type=checkbox]:checked').next('a');

        $('#filter-search>li:not(:first)').remove(); // remove all but the "Add Keyword" form.
        
        // write facet dom
        
        $(data.facets).each(function(i, ob){
            var facet           = facetTemplate.clone();
            var facetOps        = facet.find('ul');
            var facetOpTemplate = facetOps.find('li:nth-child(1)');
            facet.find('h3 a').html(capitalise(ob.name));
            facetOps.empty();
            $.each(ob.fields, function(i, field){
                
                var facetOp = facetOpTemplate.clone();
                var urlFragment = "qf=" + ob.name + ":" + field.label;
                
                facetOp.find('h4 a').attr({
                    "href"  : urlFragment,
                    "title" : field.label
                });

                facetOp.find("input").attr({
                    "name"  : "cb-" + cbCount,
                    "id"    : "cb-" + cbCount,
                    "value" : urlFragment
                });

                facetOp.find('label').html(field.label + ' (' + field.count + ')').attr({
                    "for"   : "cb-" + cbCount,
                    "title" : field.label
                });

                facetOps.append( facetOp );
                cbCount ++;
            });
            facet.append(facetOps);
            $('#filter-search').append(facet);
        });

        // facet actions 
        
        var refinements =  container.find('#refine-search-form');
		
        $("#filter-search  a label, #filter-search h4 input").click(function(e){
            var cb = $(this);

            if(cb.attr("for")){
                cb = $("#filter-search #" + cb.attr("for"));
            }
            cb.prop('checked', !cb.prop('checked') );
            e.preventDefault();
            
            
            var href = cb.next('a').attr('href');
            if(cb.prop('checked')){
            	$('<input type="hidden" name="qf" value="' + href + '"/>').appendTo(refinements);            	
            }
            else{
    			var toRemove =  refinements.find('input[value="' + href + '"]');
            	toRemove.remove();
            }
            doSearch();
        });
        
        // facet collapsibility 
              
        $("#filter-search>li:not(:first)").each(function(i, ob){
        	ob = $(ob);
        	var heading = ob.find('h3 a');
			createCollapsibleSection(ob, function(){
	            return heading.parent().next('ul').first().find('a');   
	        },
	        heading);
        });

        // restore facet selection
    
        var opened = {};
        $(selected).each(function(i, ob){ 
            var object = $('a[href="' + $(ob).attr('href') + '"]');
            var opener = object.closest('ul').prev('h3').find('a');
            
            if(!opened[opener]){
                opened[opener] = true;
                console.log("restore selected " + opener.html() );
                opener.click();
            }
            object.prev().prop('checked', true);
        });
        
        // open "Add Keyword"
        
        if($('#refinements').css('display') == 'none'){
            $('#filter-search li:first h3 a').click();
        }

        // filters
        
        if(data.breadCrumbs){
        	
        	//var hFields = {};
			var filters = container.find('#search-filter');
    		filters.empty();
			
            $.each(data.breadCrumbs, function(i, ob){
            	if(ob.param == "qf" || ob.param == "query"){
            		
            		// add dom data
                	ob.href = ob.href.replace(/&amp;/g, '&'); // unescape ampersans
                	ob.href = ob.href.replace(/\"/g, ''); // remove quotes
            		
            		var href = (ob.param == "query") ? '' : 'qf=' + ob.href.split('&qf=')[1];
            		            		
            		// add filter
            		var filter  = filterTemplate.clone();
            		
            		// 1st link cuts all susequent
            		filter.find('a:first').attr('href', ob.href);  
            		filter.find('a:first').html(ob.display); 
            		filter.find('a:first').click(function(e){

            			// remove all hidden fields occuring after the current filter / uncheck any checkboxes.
            			$('#refine-search-form').find('input[type=hidden]').each(function(iFilter, obFilter){
            				if(iFilter >= i){
            					var settingCbs = $('input[value="' + $(this).val() + '"]');
            					console.log("settingCbs = " + settingCbs.length );
            					settingCbs.prop('checked', false);
            					$(this).remove();
            				}
            			});
            			doSearch(self.resMenu1.getActive(), ob.href);

            			e.preventDefault();
            			return;
            		}); 
            		
            		// 2nd link removes this
            		var linkRemove = filter.find('a:nth-child(2)');  
            		linkRemove.attr('href', '');
            		linkRemove.click(function(e){
            			try{
	            			var toRemove =  refinements.find('input[value="' + href + '"]');
	            			toRemove.remove();
	            			
	            			// uncheck
	            			container.find("#filter-search").find('input[value="' + href + '"]') .prop('checked', false);
	            			
	            			doSearch();
            			}
            			catch(e){
            				console.log(e);
            			}
            			e.preventDefault();
            		});
            		filters.append(filter);
            	}
            });
            
            container.find('#content').show();
        }
    }; // end showRes

    var capitalise = function(str){
    	return (str.substr(0,1).toUpperCase() + str.substr(1).toLowerCase() ).replace(/_/g, ' ');
    };
    
	var createCollapsibleSection = function(ob, fnGetItems, heading){
        var accessibility =  new EuAccessibility(heading, fnGetItems);
        
        if(ob.hasClass('ugc-li')){
            ob.bind('keypress', accessibility.keyPress);
        }
        else{
            ob.Collapsible({
                "headingSelector" : "h3 a",
                "bodySelector"    : "ul",
                "keyHandler"      : accessibility
            });
        }
    };

    var setupEllipsis = function(){
        var ellipsisObjects = [];
        $('.ellipsis').each(
            function(i, ob){
                var fixed    = $(ob).find('.fixed');
                var html    = fixed.html();
                fixed.remove();
                ellipsisObjects[ellipsisObjects.length] = new Ellipsis(
                    $(ob),
                    {fixed:    '<span class="fixed">' + html + '</span>'},
                    function($ob){
                        var imgThumb = $(ob).parent().prev();
                        imgThumb.css('border-style', 'solid solid none');
                        imgThumb.css('border-width', '1px 1px medium');
                        $ob.css('visibility', 'visible');
                    }
                );                    
            }
        );

        $(container).euRsz(function(){
            for(var i=0; i<ellipsisObjects.length; i++ ){
                ellipsisObjects[i].respond();
            }
        });
    };

    var setupQuery = function(){
        self.q = $('#query-input');
        self.q.blur(function(){
            $(this).parent().removeClass('glow');
        }).focus(function(){
            $(this).parent().addClass('glow');    
        });

        var submitCell          = container.find('.submit-cell');
        var submitCellButton    = container.find('button');
        var menuCell            = container.find('.menu-cell');
        var searchMenu          = container.find('#search-menu');

        // form size adjust
        submitCell.css("width", submitCellButton.outerWidth(true) + "px"); 
        menuCell.css("width", searchMenu.width() + "px");
        submitCellButton.css("border-left",    "solid 1px #4C7ECF");    // do this after the resize to stop 1px gap in FF

        // Disable forms and wire submission to ajax call
        container.find("form").submit(function() {
            doSearch();
            return false;
        });
        
        container.find("#refine-search-form").unbind('submit').submit(function() {
	        try{	
	        	var keyInput = $(this).find('#newKeyword');
	        	var keyword  = keyInput.val();
	        	
	        	keyInput.val('');
	        	if(keyword){
	        		keyInput.removeClass('error-border');    		
		     		$(this).append('<input type="hidden" name="qf" value="qf=' + keyword + '"/>');
	                doSearch();    
	        	}
	        	else{
	        		keyInput.addClass('error-border');
	        	}
	        }
	        catch(e){
	        	console.log(e);
	        }
            return false;
        });
    };

    var setupMenus = function(){
    	
        // search 
        self.searchMenu = new EuMenu( 
            $("#search-menu"),
            {
                "fn_item": function(self){},
                "fn_init": function(self){
                    var input        = $('#query-input');
                    var searchTerm    = input.attr("valueForBackButton").replace("*:*", "");
                    self.cmp.find(".item a").each(function(i, ob){
                        var searchType = $(ob).attr("class");
                        if(searchTerm.indexOf(searchType) == 0){
                             self.setLabel(searchType);
                            input.val( searchTerm.substr( searchType.length, searchTerm.length) );
                            self.setActive(searchType);
                        }
                    });
                },
                "fn_submit":function(self){
                    var active    = self.cmp.find(".item.active a").attr("class");
                    var input    = $('#query-input');
                    input.val( (typeof active == "undefined" ? "" : active) + input.val());
                }
            }
        );
        self.searchMenu.init();

        // result size 
        var config = {
            "fn_init": function(self){
                self.setActive('6');
            },
            "fn_item":function(self, selected){
                doSearch();
            }
        };
        
        self.resMenu1 = new EuMenu( $(".nav-top .eu-menu"), config);
        self.resMenu2 = new EuMenu( $(".nav-bottom .eu-menu"), config);

        self.resMenu1.init();
        self.resMenu2.init();

        // menu closing
        $(container).click( function(){
            $('.eu-menu' ).removeClass("active");
        });
    };

    var setUpRefinements = function(){
        var addKeyword = $("#filter-search>li:first");
       	var heading = addKeyword.find("h3 a");
		createCollapsibleSection(addKeyword, function(){
    	        return heading.parent().next('form').find('input[type!="hidden"]');
	        },
	        heading);
		
		$("#refine-search-form > input").remove();
    };
    
    return {
        "init" : function(data){ self.init(data); },
        "load" : function(){ self.load(); },
        "search" : function(startParam){ doSearch(startParam); },
        "showRes" : function(data){ showRes(data); }
    };
};



var theParams = function(){
		
	var scripts    = document.getElementsByTagName('script');
	var thisScript = false;
	
	for(var i=0; i<scripts.length; i++){
		if(scripts[i].src.indexOf('EuSearchWidget') > -1){
			thisScript = scripts[i];			
		}
	}
	if(!thisScript){
		if($('#widget-url-ref').length>0 && $('#widget-url-ref').val().length>0){
			thisScript = {"src": $('#widget-url-ref').val()};
		}
		else{
			return {};
		}
	}
	
	var queryString = thisScript.src.replace(/^[^\?]+\??/,'');
	//var queryString = thisScript.src.substr(thisScript.src.indexOf('?')+1, thisScript.src.length);

	console.log("thisScript.src = " + thisScript.src + "\n\nqueryString = " + queryString);
	
	function parseQuery ( query ) {
		
		console.log("parseQuery ( " + query + " )");
		
		var Params = new Object ();
		if(!query){
			return Params; // return empty object
		}
		
		var Pairs = query.split('&');
		for ( var i = 0; i < Pairs.length; i++ ) {
			
			console.log("pairing.... ")
			var KeyVal = Pairs[i].split('=');
			if(!KeyVal || KeyVal.length != 2 ){
				console.log("invalid parameter");
				continue;
			}
//			var key = unescape( KeyVal[0] );
	//		var val = unescape( KeyVal[1] );
			var key = unescape( KeyVal[0] );
			var val = unescape( KeyVal[1] );
			val = val.replace(/\+/g, ' ');
			if(!Params[key]){
				Params[key] = new Array ();
			}
			console.log("push " + key + "  " + val);
			Params[key].push(val);
		}
		
		return Params;
	};
	
	// load js
	return parseQuery( queryString );
}();



var searchWidget;
var rootUrl = "http://localhost:8081/portal/themes/default/js/eu/europeana/";

var withJQuery = function($){
	var dependencies = [
		'utils.js',
		'EuAccessibility.js',
		'EuMenu.js',
		'ellipsis.js',
		'collapsible_widget.js',
		'collapsible.js',
		'EuPagination.js',
	];

	function recursiveLoad(index){
		if(dependencies.length > index){
			$.ajax({
				url: rootUrl + dependencies[index],
				dataType: "script",
				success: function(){
					console.log('loaded ' + dependencies[index] + ', now get ' + (index+1));
					recursiveLoad(index + 1);	
				}
			});
		}
		else{
			searchWidget = fnSearchWidget($, theParams);
			searchWidget.load();
		}
	}
	recursiveLoad(0);
};

if(typeof jQuery == "undefined"){
	
	var jq = document.createElement('script');
	jq.setAttribute('src', 'http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js');
	jq.setAttribute('type', 'text/javascript');


	if ( 'onload' in document || 'addEventListener' in window ) {
		jq.onload = function() {
			withJQuery($);
		};
		
	}
	else if ( 'onreadystatechange' in document ) {
		jq.onreadystatechange = function () {
			if ( jq.readyState === 'loaded' || jq.readyState === 'complete' ) {
				withJQuery($);
			}
		};
	}
	
	document.getElementsByTagName('body')[0].appendChild(jq);
}
else{
	withJQuery(jQuery);	
}




