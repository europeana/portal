
fnSearchWidget = function($, config){

    var self             = this;
    var container        = false;
    var itemTemplate     = false;
    var facetTemplate    = false;
    var filterTemplate   = false;
    var showFacets       = false;
    
    if( typeof config != 'undefined' ){
    	self.config      = config;
        self.withResults = self.config.withResults == 'true';
        self.theme       = self.config.theme;
    }
    
    
    var addKeywordTemplate      = false;
    var resultServerUrl         = config.rootUrl;
	var searchUrl				= typeof config.apiUrl == 'undefined' ? 'http://api-test.de.a9sapp.eu/v2/search.json?wskey=api2demo&profile=facets,params&v=2' : config.apiUrl;
	var searchUrlWithoutResults = resultServerUrl + '/search.html';
	
	var markupUrl               = rootUrl +  '/template.html?id=search&showFacets=' + showFacets;
	var cssUrl                  = rootUrl +  (js.debug ? '/themes/default/css/' : '/themes/default/css-min/');
	var responsiveContainersUrl = rootUrl +  '/themes/default/js/eu/europeana/responsive-containers.js';
    
    var defaultRows             = 6;
    var pagination              = [];
    var paginationData          = {};
    
    
    // get markup from portal - callback will invoke init
    self.load = function(){
       	$.holdReady(true);
       	
       	var url = markupUrl + (self.config.lang ? '&lang=' + self.config.lang[0] : '');

        $.ajax({
            "url" :         url,
            "type" :        "GET",
            "crossDomain" : true,
            "dataType" :    "script",
            "contentType" :	"application/x-www-form-urlencoded;charset=UTF-8"
        });
        
        return self;
    };

    // load style / initialise events / set state - called by load callback

    self.init = function(htmlData) {
    	
    	
       	$.getScript("http://ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js", function() {
    	     $.holdReady(false);
    	});
      		

    	$(document).ready(function(){
    		
   		
	    	var containerClass    = "search-widget-container";
	    	var containerSelector = "." + containerClass;
	        container = $(containerSelector);
	        
	        
	        if(!container.length){
	        	var scripts    = document.getElementsByTagName('script');
	        	$.each(scripts, function(i, ob){
	        		var src   = $(ob).attr('src');
	        		var regex = new RegExp('^' + rootUrl + '(.*)EuSearchWidget[\S]*');
	        		if(regex.test(src)){	        			
	        			$(ob).after('<div class="' + containerClass + '"></div>');
	        			container = $(containerSelector);
	        		}
	        	});
	        }
	        
	        container.append('<div id="overlay"></div>');
	        $('#overlay').hide();

	        container.append(htmlData.markup);
	        container.find('#content').hide();
	        container.find('#no-results').hide();


	        itemTemplate       = container.find('.thumb-frame').parent();
	        
	        
	        facetTemplate      = container.find('#filter-search li:nth-child(2)');
	        addKeywordTemplate = container.find('#filter-search li:first');
	        filterTemplate     = container.find('#search-filter li:first');
	        
	        setupQuery();
	        setupMenus();
	        setUpRefinements(); // TODO
	        
	        pagination = [];
	        $.each( $('.result-pagination'), function(i, ob){
		        pagination.push( pager = new EuPagination($(ob),
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
		            			searchWidget.search(pager.getMaxStart());
							},
		            		"fnSubmit":function(val){
								val = parseInt(val);
		            			var start = ((val-1) * paginationData.rows) + 1;
		   						searchWidget.search( start );            				
					            return false;
							}
		        		}
		        	})
		        );
	        });
	        
	        

        	container.css('overflow-y', 'auto');
        	container.css('overflow-x', 'hidden');
        	
        	// responsive-containers initialisation
        	
//        	container.attr('data-squery', 'max-width:48em=mobile min-width:48em=desktop min-width:71em=min71em max-width:30em=max30em max-width:48em=max48em min-width:22em=min22em min-width:55em=min55em max-width:55em=max55em');
//        	container.attr('data-squery', 'max-width:48em=mobile min-width:48em=desktop min-width:71em=min71em max-width:30em=max30em                        min-width:22em=min22em min-width:55em=min55em max-width:55em=max55em');
        	container.attr('data-squery', 'max-width:55em=max55em max-width:48em=mobile max-width:30em=max30em min-width:22em=min22em min-width:48em=desktop min-width:55em=min55em min-width:71em=min71em');

        	
            // load style - as single files if in debug mode
            if(js.debug){
    			$.each(['html-sw', 'common-sw', 'header-sw', 'menu-main', 'responsive-grid-sw', 'eu-menu', 'ellipsis', 'europeana-font-icons-widget', 'europeana-font-face', 'search-sw', 'search-pagination-sw', 'sidebar-facets-sw', 'styling-sw'], function(i, ob){
    	        	$('head').append('<link rel="stylesheet" href="' + cssUrl + ob + '.css" type="text/css" />');
    			});
            }
            else{
            	$('head').append('<link rel="stylesheet" href="' + cssUrl + 'search-widget-all.css" type="text/css" />');
            }
            if(self.theme){
            	container.removeClass('dark');            	
            	$('body').removeClass('dark');
               	container.addClass('' + self.theme);
               	$('body').addClass(self.theme);
            }
            else{
            	container.removeClass('dark');            	
            	$('body').removeClass('dark');
            }
            
           	$.getScript(responsiveContainersUrl, function() {});
        	
           	if(config.query && self.withResults){
           		setTimeout(doSearch, 200);
           	}
           	
        });

    };

    
    var doSearch = function(startParam, query){
    	
    	try{
    		var url = buildUrl(startParam, query);
    		
	        if(typeof url != 'undefined' && url.length){
	        	
	        	if(self.withResults){
	            	if(searchUrl.indexOf('file')==0){
	    				getFake();
	            	}
	            	else{
	            		showSpinner();
	            		
	                    $.ajax({
	                        "url" : url,
	                        "type" : "GET",
	                        "crossDomain" : true,
	                        "dataType" : "script",
	                        "contentType" :	"application/x-www-form-urlencoded;charset=UTF-8"
	                    });
	            	}        		
	        	}
	        	else{
	        		window.open(url, '_blank');        		
	        	}
	        }
	        else{
	            self.q.addClass('error-border');
	        }
    	}
    	catch(e){
    		console.log(e);
    	}
    };

    
    // build search param from url-fragment hrefs.  @startParam set to 1 if not specified
    var buildUrl = function(startParam, query){

        var term = self.q.val();
        if (!term) {
        	term = '*:*';
        }
        
        var url = '';
        if(self.withResults){
        	url = query ? searchUrl + '&' + query : searchUrl + '&query=' + term;        	
        	url += "&callback=searchWidget.showRes";
        	url += '&rows=' + (self.resMenu1.getActive() ? self.resMenu1.getActive() : defaultRows);
        	url += '&start=' + (startParam ? startParam : 1);
        	
        	// remove-filter links provide the url - return here if provided
        	if(query){
        		console.log("return the pre-set query");
        		return url;
        	}
        }
        else{
        	url = searchUrlWithoutResults + '?query=' + term + '&bt=searchwidget2';
        }
                
        // params
        
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
        			ob = ob.replace(/[\{\}]/g, '"');
        			url += '&qf=';

        			 var urlBit = (ob.indexOf(' ')>-1) ? (ob.split(':')[0] + ':' + '' + ob.split(':')[1] + '') : ob;
        			 urlBit = urlBit.replace(/\&/g, '%26');

        			 url += urlBit;
        		});
        	}
        }

        return url;
    };

    
    var showRes = function(data){

        // Append items
        var grid = container.find('#items');
        grid.empty();

        var start = data.params.start ? data.params.start : 1;
        var query = self.q.val();

        if( $(data.items).length ){
        	container.find('#no-results').hide();        	
        }
        else{        	
        	container.find('#no-results').show();        	
        }
        
        $(data.items).each(function(i, ob){
        	
            var item = itemTemplate.clone();
            
            item.find('a').attr(
                'href', resultServerUrl + '/record' + ob.id + '.html?start=' + start + '&query=' + query + '&bt=searchwidget2'
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
            
            if(item.find('.ellipsis span span').length){
            	item.find('.ellipsis span span').attr('class', 'icon-' + ob.type.toLowerCase());
            }
            
            item.find('.thumbnail').attr('data-type', ob.type.toLowerCase());
    
            
            if(item.find('.thumbnail').attr('src').indexOf('europeanastatic') > -1){
            	var newSrc =  item.find('.thumbnail').attr('src');
            	newSrc = newSrc.replace(/type=[a-zA-Z]+/, 'type=' + ob.type.toUpperCase() );
            	item.find('.thumbnail').attr('src', newSrc);
            }
            
            grid.append(item);
        });

        // setupEllipsis();

        // pagination
        
        paginationData = {"records":data.totalResults, "rows": data.params.rows, "start":start};
        
        //pagination.setData(data.totalResults, data.itemsCount, data.start);
        $.each(pagination, function(i, ob){
            ob.setData(data.totalResults,
            		data.params.rows,
            		start);
        });

        
        // result stats
        container.find('.count').hide();
        container.find('.of').html('/');

        if(data.items){
        	$('.search-results-navigation').show();        	
        }
        else{
        	$('.search-results-navigation').hide();
        }
        
        if(false){
        	container.find('.first-vis-record').html(start);
        	container.find('.last-vis-record') .html(start - 1 + data.itemsCount);
        	container.find('.last-record')     .html(data.totalResults);        	
        }


        // facets
        if(showFacets){
        
	        var cbCount  = 0;
	        var ugcFacet = container.find('#filter-search .ugc-li');
	        var selected = container.find('#filter-search input[type=checkbox]:checked').next('a');
	
	        container.find('#filter-search>li:not(:first)').remove(); // remove all but the "Add Keyword" form.
	        
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
	            container.find('#filter-search').append(facet);
	        });

	        // facet actions 
	        
	        var refinements = container.find('#refine-search-form');
			
	        container.find('#filter-search  a label').add(container.find('#filter-search h4 input')).click(function(e){
	            var cb = $(this);
	            if(cb.attr("for")){
	                cb = container.find('#filter-search #' + cb.attr("for"));
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
	              
	        container.find('#filter-search>li:not(:first)').each(function(i, ob){
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
	            var object = container.find('a[href="' + $(ob).attr('href') + '"]');
	            var opener = object.closest('ul').prev('h3').find('a');
	            
	            if(!opened[opener]){
	                opened[opener] = true;
	                opener.click();
	            }
	            object.prev().prop('checked', true);
	        });
	        
	        // open "Add Keyword"
	        
	        if(container.find('#refinements').css('display') == 'none'){
	        	container.find('#filter-search li:first h3 a').click();
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
	            			container.find('#refine-search-form input[type=hidden]').each(function(iFilter, obFilter){
	            				if(iFilter >= i){
	            					var settingCbs = container.find('input[value="' + $(this).val() + '"]');
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
	        }
        }
        container.find('#content').show();
        setupEllipsis();

        hideSpinner();
        
    }; // end showRes

    
    var showSpinner = function(){
    	container.find('#overlay').show();
    	$('.search-widget-container').css('overflow-y', 'hidden');    	
    };
    
    var hideSpinner = function(){
    	container.find('#overlay').hide();
    	$('.search-widget-container').css('overflow-y', 'auto');
    };
    
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
        container.find('.ellipsis').each(
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

        $(window).euRsz(function(){
        	
            for(var i=0; i<ellipsisObjects.length; i++ ){
                ellipsisObjects[i].respond();
            }
        });
    };

    var setupQuery = function(){
 
        self.q = container.find('#query-input');
        self.q.blur(function(){
            $(this).parent().removeClass('glow');
        }).focus(function(){
            $(this).parent().addClass('glow');    
        }).val(config.query ? config.query : '');

        var submitCell          = container.find('.submit-cell');
        var submitCellButton    = container.find('button');
        var menuCell            = container.find('.menu-cell');
        var searchMenu          = container.find('#search-menu');

        // form size adjust
        submitCell.css("width", submitCellButton.outerWidth(true) + "px"); 
        menuCell.css("width", searchMenu.width() + "px");
        
        // do this after the resize to stop 1px gap in FF        	
        if(container.hasClass('dark')){
        	submitCellButton.css("border-left",    "solid 1px #333");
        }
        else{
        	submitCellButton.css("border-left",    "solid 1px #4C7ECF");        	
        }

        
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
        	container.find("#search-menu"),
            {
                "fn_item": function(self){},
                "fn_init": function(self){
                	/*
                    var input        = container.find('#query-input');
                    var searchTerm   = input.attr("valueForBackButton").replace("*:*", "");
                    self.cmp.find(".item a").each(function(i, ob){
                        var searchType = $(ob).attr("class");
                        if(searchTerm.indexOf(searchType) == 0){
                             self.setLabel(searchType);
                            input.val( searchTerm.substr( searchType.length, searchTerm.length) );
                            self.setActive(searchType);
                        }
                    });
                    
                    */
                },
                "fn_submit":function(self){
                    var active    = self.cmp.find(".item.active a").attr("class");
                    var input    = container.find('#query-input');
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
        
        self.resMenu1 = new EuMenu( container.find(".nav-top .eu-menu"), config);
        self.resMenu2 = new EuMenu( container.find(".nav-bottom .eu-menu"), config);

        
        self.resMenu1.init();
        self.resMenu2.init();

        // menu closing
        $(container).click( function(){
        	container.find('.eu-menu' ).removeClass("active");
        });
    };

    var setUpRefinements = function(){
        var addKeyword = container.find('#filter-search>li:first');
       	var heading = addKeyword.find("h3 a");
		createCollapsibleSection(addKeyword, function(){
    	        return heading.parent().next('form').find('input[type!="hidden"]');
	        },
	        heading);
		
		container.find('#refine-search-form > input').remove();
    };
    
    return {
        "init" : function(data){ self.init(data); },
        "load" : function(){ self.load(); },
        "search" : function(startParam){ doSearch(startParam); },
        "showRes" : function(data){ showRes(data); },
        "setWithResults" : function(withResults){
        	self.withResults = withResults;
        	if(!withResults){
        		container.find('#content').hide();        		
        	}
        },
        "setTheme" : function(theme){
        	if(self.theme){
        		container.removeClass(self.theme);
        		$('body	').removeClass(self.theme);
        	}
        	if(theme){
            	self.theme = theme;
        		container.addClass(theme);        		
        	}
        	else{
        		container.removeClass('dark');
        	}
        }
    };
};



var theParams = function(){
		
	var scripts    = document.getElementsByTagName('script');
	var thisScript = false;
	
	for(var i=0; i<scripts.length; i++){
		// remote
		if(scripts[i].src.indexOf('EuSearchWidget') > -1){
			thisScript = scripts[i];			
		}
	}
	if(!thisScript){
		// within preview page on portal
		if($('#widget-url-ref').length>0 && $('#widget-url-ref').val().length>0){
			thisScript = {"src": $('#widget-url-ref').val()};
		}
		else{
			return {};
		}
	}
	
	rootJsUrl	= thisScript.src.split('EuSearchWidget')[0];
	rootUrl		= rootJsUrl.split('/themes')[0];
	
	var queryString = thisScript.src.replace(/^[^\?]+\??/,'');
		
	function parseQuery ( query ) {
		
		var Params = new Object();
		if(!query){
			return Params; // return empty object
		}
		
		var Pairs = query.split('&');
		
		for ( var i = 0; i < Pairs.length; i++ ) {
			
			var KeyVal = Pairs[i].split('=');
			if(!KeyVal || KeyVal.length != 2 ){
				//console.log("invalid parameter");
				continue;
			}
			//var key = unescape( KeyVal[0] );
			//var val = unescape( KeyVal[1] );
			var key = unescape( decodeURIComponent(KeyVal[0]) );
			var val = unescape( decodeURIComponent(KeyVal[1]) );
			
			//console.log(key + " = " + val);
			
			//val = val.replace(/\+/g, ' ');
			if(!Params[key]){
				Params[key] = new Array ();
			}

			//Params[key].push(encodeURIComponent(val));
			Params[key].push(val);
		}
		Params['rootUrl'] = [rootUrl];
		
		
		return Params;
	};
	
	// load js
	return parseQuery( queryString );
}();



var searchWidget;
var rootUrl;
var rootJsUrl;

var withJQuery = function($){
	
	$(document).ready(function($){


		/*	workaround for dealiased jQuery used to fix issue #1053
		 *   
		 *  proper fix requires all js files to be rewritten (scope $ to their respective ready callbacks).
		 *  
		 * */
		
		if(typeof window.$ == 'undefined'){
			window.$ = jQuery;
		}

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
		            				"url": rootJsUrl + dependencies[index],
		            				"dataType": "script",
		            				"success": function(){
		            					//console.log('loaded ' + dependencies[index] + ', now get ' + (index+1));
		            					recursiveLoad(index + 1);	
		            				},
		            	            "contentType" :	"application/x-www-form-urlencoded;charset=UTF-8"
		            			});
		            		}
		            		else{
		            			searchWidget = fnSearchWidget($, theParams);
		            			searchWidget.load();
		            		}
		            	}
		            	recursiveLoad(0);
		
		
	});
	

};

if(typeof jQuery == "undefined"){
	
	var jq = document.createElement('script');
	jq.setAttribute('src', 'http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js');
	jq.setAttribute('type', 'text/javascript');


	if ( 'onload' in document || 'addEventListener' in window ) {
		jq.onload = function() {
			withJQuery(jQuery);
		};
		
	}
	else if ( 'onreadystatechange' in document ) {
		jq.onreadystatechange = function () {
			if ( jq.readyState === 'loaded' || jq.readyState === 'complete' ) {
				withJQuery(jQuery);
			}
		};
	}
	
	document.getElementsByTagName('body')[0].appendChild(jq);
}
else{
	withJQuery(jQuery);	
}




