js.utils.registerNamespace( 'eu.europeana.search' );

eu.europeana.search = {
	
	facet_sections : [],

	init : function() {
		
		$("#query-input").focus();

		this.loadComponents();
		
		// make facet sections collapsible
		$("#filter-search li").not(".ugc-li").Collapsible(
			{
				headingSelector:"h3 a",
				bodySelector: "ul"
			}
		);
		
		// make facet checkboxes clickable
		$("#filter-search li input[type='checkbox']").click(function(){
			var label = $("#filter-search li label[for='" + $(this).attr('id') + "']");
			window.location = label.closest("a").attr("href");
		});

		this.setupResultSizeMenu();
		this.setupEllipsis();
		this.setupPageJump();
		
	},
	
	loadComponents : function() {
		
		var self = eu.europeana.search;
		
		js.loader.loadScripts([{
			file: 'addthis' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/com/addthis/' + js.min_directory,
			callback : function() { self.addThis(); }
		}]);
		
	},
	
	setupPageJump : function(){
		
		jQuery('.jump-to-page').bind('submit', 					this.jumpToPageSubmit );
		jQuery('.jump-to-page #start-page').bind('keypress',	this.validateJumpToPage);
	},
	
	jumpToPageSubmit : function( e ){
		var $jumpToPage	= $(this).parent();
		var rows		= parseInt($jumpToPage.find("input[name=rows]").val());
		var pageNum		= parseInt($jumpToPage.find("#start-page").val());
		var newStart	= 1 + ((pageNum-1)*rows);
		window.location.href = eu.europeana.search.urlAlterParam("start", newStart);
		return false; // stop submit
	},
	
	validateJumpToPage : function(e){
		
		var $this		= $(this);
		var $jumpToPage	= $(this).parent();
		
		if (!Array.prototype.indexOf) {	/* IE 8 */
			Array.prototype.indexOf = function(obj, start) {
				 for (var i = (start || 0), j = this.length; i < j; i++) {
					 if (this[i] === obj) { return i; }
				 }
				 return -1;
			};
		}

		var key		= window.event ? e.keyCode : e.which;
		var maxRows	= parseInt($jumpToPage.find("#max-rows").val());

		var underMax = function(){
			return parseInt( $this.val() + String.fromCharCode(key) ) <= maxRows; 
		};

		if([8, 46, 37, 38, 39, 40].indexOf(e.keyCode)>-1){
			/* delete, backspace, left, up, right, down */
			return true;
		}
		else if(e.keyCode == 13){
			/* return */
			var currVal = $jumpToPage.find("#start-page").val();
			return currVal.length > 0;
		}
		else if ( key < 48 || key > 57 ) {
			/* alphabet */
			return false;
		}
		else{
			/* number */
			return underMax();
		}
		
	},

	setupEllipsis : function(){
		// add ellipsis
		var ellipsisObjects = [];
		jQuery('.ellipsis').each(
				function(i, ob){
					var fixed	= $(ob).find('.fixed');
					var html	= fixed.html();
					fixed.remove();
					ellipsisObjects[ellipsisObjects.length] = new Ellipsis($(ob), {fixed:	'<span class="fixed">' + html + '</span>'} );					
				}
		);
		$(window).bind('resize', function(){
			for(var i=0; i<ellipsisObjects.length; i++ ){
				ellipsisObjects[i].respond();
			}
		});

	},

	urlAlterParam : function(paramNameIn, paramValIn){
		var params = {};

		document.location.search.replace(/\??(?:([^=]+)=([^&]*)&?)/g, function () {
		    function decode(s) {
		        return decodeURIComponent(s.split("+").join(" "));
		    }
		    
		    var paramName	= decode(arguments[1]);
		    var paramVal	= decode(arguments[2]);
		    if( params[paramName] ){
		    	if(typeof( params[paramName] )  == "string"){
		    		params[paramName] = [params[paramName], paramVal];
		    	}
		    	else if(typeof( params[paramName] )  == "object"){
		    		params[paramName].push(paramVal);
		    	}
		    }
		    else{
			    params[paramName] = paramVal;
		    }
		});

		var newUrl = window.location.href.substr(0, window.location.href.indexOf("?"));
		var index = 0;
		$.each(params, function(name, val) {
			if( typeof(val) == "string" ){
				newUrl += ((index==0) ? "?" : "&") + name + "=" + (name == paramNameIn ? paramValIn : val);				
			}
			else{
				for(var j=0; j<val.length; j++){
					newUrl += ((index==0 && j==0) ? "?" : "&") + name + "=" + (name == paramNameIn ? paramValIn : val[j]);
				}
			}
			index++;
		});
		return newUrl;
	},
	
	setupResultSizeMenu : function(){
		var config = {
			"fn_init": function(self){
				self.setActive( $("#query-search input[name=rows]").val() );
			},
			"fn_item":function(self, selected){
				window.location.href = eu.europeana.search.urlAlterParam("rows", selected);
			}
		};
		var menuTop = new EuMenu( $(".nav-top .eu-menu"), config);
		var menuBottom = new EuMenu( $(".nav-bottom .eu-menu"), config);
		menuTop.init();
		menuBottom.init();
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
			
			url = jQuery('head meta[property="og:url"]').attr('content');
			window.addthis_share = com.addthis.createShareObject({
				// nb: twitter templates will soon be deprecated, no date is given
				// @link http://www.addthis.com/help/client-api#configuration-sharing-templates
				templates: { twitter: title + ': ' + url + ' #europeana' }
			});
		
			var addThisHtml = com.addthis.getToolboxHtml_ANDY({
				html_class : '',
				url : url,
				title : title,
				description : description,
				services : {
					compact : {}
				},
				link_html : $('#shares-link').html()
			
			});
			
			jQuery('#shares-link').html(
				addThisHtml
			);
			
			com.addthis.init( null, true, false );
			
			setTimeout( function() {
				jQuery('#shares-link').fadeIn(); },
				600 );
			
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




