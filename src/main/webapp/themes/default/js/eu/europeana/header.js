/**
 *  header.js
 *
 *  @package	eu.europeana
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @author		andy maclean <andyjmaclean@gmail.com>
 *
 *  @created	2011-07-06 17:34 GMT +1
 *  @version	2011-10-20 08:26 GMT +1
 */

js.utils.registerNamespace( 'eu.europeana.header' );

eu.europeana.header = {
	
	init : function() {

		if (!Array.prototype.indexOf) {	/* IE 8 */
			Array.prototype.indexOf = function(obj, start) {
				 for (var i = (start || 0), j = this.length; i < j; i++) {
					 if (this[i] === obj) { return i; }
				 }
				 return -1;
			};
		}
		
		$('.submit-cell').css("width",	$('.submit-cell button')	.outerWidth(true) + "px"); 
		$('.menu-cell').css("width",	$('#search-menu')			.outerWidth(true) + "px");
		
		$('.submit-cell button').css("border-left",	"solid 1px #4C7ECF");	// do this after the resize to stop 1px gap in FF
		
		$("#query-search>table")									.css("display",		"none");
		$("#query-search>table")									.css("visibility",	"visible");
		
		
		/*
		$("#query-search>table").fadeIn(600, function(){
			$("#query-input").focus();
		});
		*/
		
		$("#query-search>table").fadeIn(600);
		

		this.initResponsiveUtility();
		this.addLanguageChangeHandler();
		this.addRefineSearchClickHandler();		
		this.addAjaxMethods();
		this.addMenuFocusTriggers();
		this.setupResultSize();
		this.setupSearchMenu();
		this.setupLanguageMenu();
		
		this.addAutocompleteHandler();
		this.setupNewsletter();
		this.setupPinterestAnalytics();
		
		$('#query-search').bind('submit', this.handleSearchSubmit );
		
		// setup tabs
		this.setupTabbing();
		this.addQueryFocus();
	},
	
	
	setupTabbing : function(){
		
		var nextTabIndex = 1;

		function setTabIndex(selectorOrObject){
			var selected = typeof selectorOrObject == 'string' ? $(selectorOrObject) : selectorOrObject;
			
			if(selected.length==1){
				selected.attr('tabIndex', nextTabIndex);					
				nextTabIndex ++;
			}
			else if(selected.length>1){
				selected.each(function(i, ob){
					$(ob).attr('tabIndex', nextTabIndex);
					nextTabIndex ++;
				});
			}			
		}

		
		if(eu.europeana.vars.page_name == 'login.html'){
			setTabIndex( '#j_username' );
			setTabIndex( '#j_password' );
			setTabIndex( '#login input[type=submit]' );
			setTabIndex( '#login a' );
		}
		
		/* header */
		setTabIndex( '#query-input' );
		setTabIndex( '.submit-cell.hide-cell-on-phones button' );
		setTabIndex( '#search-menu' );
		setTabIndex( '#search-menu a' );
		setTabIndex( '#logo a' );
		setTabIndex( '#header-strip white>a, #header-strip a.white' );
		setTabIndex( '#lang-menu' );
		setTabIndex( '#lang-menu .item.lang a');
		setTabIndex( '#query-info .search-help' );
		
		/* search */
		
		if(eu.europeana.vars.page_name == 'search.html'){
			
			setTabIndex('#search-filter a');
			
			$('#filter-search a.facet-section').each(function(i, ob){
				setTabIndex( $(ob) );
				if(i==0){
					setTabIndex( $(ob).parent().next('form').find('input[type!="hidden"]') );
				}
				else{
					setTabIndex( $(ob).parent().next('ul').find('a') );
				}
			});

			setTabIndex('#cb-ugc');
			
			setTabIndex('#share-subscribe .icon-share');
			
			// controls not initiated yet... .we have a problem
			//alert( $('#at15s_inner a').length  )
			//setTabIndex('#at15s_inner a');
			
			setTabIndex('.nav-top .eu-menu');
			setTabIndex('.nav-top .eu-menu .item a');
			
			setTabIndex('.nav-top .nav-first a');
			setTabIndex('.nav-top .nav-prev a');
			setTabIndex('.nav-top #start-page');
			setTabIndex('.nav-top .nav-next a');
			setTabIndex('.nav-top .nav-last a');
			
			setTabIndex('.thumb-frame');		
		
			setTabIndex('.nav-bottom .eu-menu');
			setTabIndex('.nav-bottom .eu-menu .item a');
			setTabIndex('.nav-bottom .nav-first a');
			setTabIndex('.nav-bottom .nav-prev a');
			
			setTabIndex('.nav-bottom #start-page');
			setTabIndex('.nav-bottom .nav-next a');
			setTabIndex('.nav-bottom .nav-last a');
			
			$("#filter-search input[type=checkbox]").not('#cb-ugc').attr("tabindex", "-1");
			$("#items .li a").attr("tabindex", "-1");
		}
		
		else if(eu.europeana.vars.page_name == 'myeuropeana.html'){
			
		//	alert("my e");
		}
		else if(eu.europeana.vars.page_name == 'login.html'){
		//	alert("login ");
		}


		
	},
	
	initResponsiveUtility : function(){
		window.showingPhone = function(){ return $("#mobile-menu").is(":visible"); };
	},
	
	setCookie: function(val){
		document.cookie = "europeana_rows=" + val;
	},
	
	getCookie: function(){
		var cookies		= document.cookie.split(";");
		var cookieVal	= null;
		
		for(var i=0; i<cookies.length; i++)
		{
			var cookieName	= cookies[i].substr(0, cookies[i].indexOf("="));
			if(cookieName == "europeana_rows"){
				cookieVal = cookies[i].substr( cookies[i].indexOf("=") + 1, cookies[i].length);
			}
		}
		return cookieVal;		
	},
	
	setupResultSize: function(){

		var rowsField = $("#query-search input[name=rows]");
		

		// first check the parameter - this will override any cookie
		if(eu.europeana.vars.rows && eu.europeana.vars.rows != "null"){
			rowsField.val(eu.europeana.vars.rows);				
			
		}
		else{
			if(eu.europeana.vars.page_name != "search.html"){
				
				// check for cookie
				var cookie = eu.europeana.header.getCookie();
				if(cookie){
					rowsField.val(cookie);
				}
				else{
					if( $("#mobile-menu").is(":visible") ){
						rowsField.val("12");
					}
					else{
						rowsField.val("24");				
					}
				}
			}
		}
	},
	
	setupLanguageMenu: function(){

		var menu = new EuMenu(
			$("#lang-menu"),
			{
				"fn_item": function(self){
					if( self.getActive() == 'treat-as-link'){
						window.location.href = self.getActiveHref();
					}
					else{
						$("input[name=lang]").val(self.getActive());
						$("#language-selector").submit();
					}
				}
			}
		);
		menu.setActive("choose");
		menu.init();

	},
	
	setupSearchMenu:function(){
		
		if($("#search-menu").length==0){	// terms and conditions have no search fields
			return;
		}
		var menu = new EuMenu( 
			$("#search-menu"),
			{
				"fn_item": function(self){
				},

				"fn_init": function(self){
					var input		= $('#query-input');
					var searchTerm	= input.attr("valueForBackButton").replace("*:*", "");
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
					var active	= self.cmp.find(".item.active a").attr("class");
					var input	= $('#query-input');
					input.val( (typeof active == "undefined" ? "" : active) + input.val());
				}
			
			}
		);

		menu.init();
		
		eu.europeana.header.searchMenu = menu;
		
		/* menu close */
			
		$(document).click( function(){
			$('.eu-menu' ).removeClass("active");
		});

		$("#query-search").bind("submit", function(){
			menu.submit();
			return true;
		});
	},
	
	/**
	 *	js solution for tabbing through main menu
	 */
	addMenuFocusTriggers : function() {
		
		$('#menu-main li ul li a')
		
			.focusin(function() {
				
				$(this).parent().parent()
					.css({
						'margin-top' : 0,
						'opacity' : 1
					});
				
				$(this).parent().parent().prev().children().eq(0)
					.css({
						'color' : '#fff',
						'background-color' : '#000',
						'background-position' : 'right -189px'
					});
			})
			
			.focusout(function() {
				
				$(this).parent().parent()
					.css({
						'margin-top' : -499,
						'opacity' : 0
					});
				
				$(this).parent().parent().prev().children().eq(0)
				.css({
					'color' : '#000',
					'background-color' : '#fff',
					'background-position' : 'right -173px'
				});
				
			});
		
	},
	
	
	/**
	 *	adds focus to the search textbox
	 */
	addQueryFocus : function() {
		
		$('#query-input').focus(function(){
			$("#query-full table tr:first-child .query-cell").addClass("glow");
		});
		$('#query-input').blur(function(){
			$("#query-full table tr:first-child .query-cell").removeClass("glow");
		});
		
		var inputFocus = ['login.html', 'forgotPassword.html', 'register-success.html'].indexOf(eu.europeana.vars.page_name) >=0 ? $('#j_username') : $('#query-input');
		inputFocus.focus();
		
	},
	
	
	addAutocompleteHandler : function() {
		
		$('#query-input, #qf').each(function(i, id){
			
			$(id).autocomplete({
				
			    open: function(event, ui) {
			        var oldLeft		= $(".ui-autocomplete").offset().left;
			        var oldWidth	= $(".ui-autocomplete").width();
			        var newLeft 	= oldLeft	- parseInt( $(id).parent().css('padding-left') );
			        var newWidth	= oldWidth	- parseInt( $(id).parent().css('padding-left') );
	                $(".ui-autocomplete").css("left",		newLeft + "px");
	                $(".ui-autocomplete").css("width",		newWidth + "px");
	                $(".ui-autocomplete").css("z-index",	2);
			    },
			    
				minLength : 3,
				
				delay : 200,
								
				source: function( request, response ) {
					
					var filter = eu.europeana.header.searchMenu.getActive();
					if(filter){
						filter = filter.replace(/:/g, '');
						request.field = filter;
					}
					
					$.getJSON( '/' + eu.europeana.vars.portal_name + '/suggestions.json', request, function(data) {
						
						//create array for response objects
						var suggestions = [];
						
						//process response
						$.each( data.suggestions, function(i, val) {
							val.label = val.term;
							val.frequency = '<span dir="ltr">' + val.frequency + '</span>';
							suggestions.push( val );
						});
						response( suggestions );
					});
					
				},
				
				select : function(event, ui) {

					switch ( this.id ) {
						case 'query-input' :

							if(completionClasses[ui.item.field]){
								eu.europeana.header.searchMenu.setActive(completionClasses[ui.item.field]);
							}
							setTimeout( function() { $('#query-search').submit(); }, 10 );
							break;
							
						case 'qf' :
							
							setTimeout( function() { $('#refine-search-form').submit(); }, 10 );
							break;
					}
					
				}
			
			});
		
			// Formatting 
			
			$.ui.autocomplete.prototype._renderItem = function (ul, item) {
				if(!item.label){
					item.label = item.term;
				}
				
				item.label = item.label.replace(new RegExp("(?![^&;]+;)(?!<[^<>]*)(" + $.ui.autocomplete.escapeRegex(this.term) + ")(?![^<>]*>)(?![^&;]+;)", "gi"), "<strong>$1</strong>");
				item.label +=  " (" + item.frequency + ")";
				item.label += '<span style="float:right">' + completionTranslations[item.field] + '</span>';
				
				return $("<li></li>")
					.data("item.autocomplete", item )
					.append("<a>" + item.label + "</a>")
					.appendTo(ul);
	        };
		});
	},

	addLanguageChangeHandler : function() {
		$('#lang').change( function() {
			$('#language-selector').submit();
		});
	},

	addRefineSearchClickHandler : function() {
		$('#refine-search').click(function(e) {
			e.preventDefault();
			$('#refine-search-form').fadeIn();
			$('#qf').focus();
		});
		
		$('#close-refine-search').click(function(e) {
			e.preventDefault();
			$('#refine-search-form').fadeOut();
			
		});
	},

	addAjaxMethods : function() {
		
		eu.europeana.ajax.methods = new eu.europeana.ajax();
		eu.europeana.ajax.methods.init();
		
	},

	handleSearchSubmit : function( e ) {

		var emptySearch = $('#query-input').val().length < 1 || (eu.europeana.header.searchMenu.getActive() == $('#query-input').val());
		if ( emptySearch ) {
			
			e.preventDefault();
			$('#query-input').addClass('error-border');
			$('#additional-feedback')
				.addClass('error')
				.html(eu.europeana.vars.msg.search_error);
			$('#query-input').val("");
		}
		
	},
	
	
	setupNewsletter: function(){
		$("#newsletter-trigger").click(function(){
			
			$(".iframe-wrap").html(
					'<iframe marginheight="0" '
						+	'marginwidth="0" '
						+	'frameborder="0" '
						+	'src="' + window.emma.iframeUrl + '"/>'
						+ 	'<div class="close"></div>'
			);

			$(".overlaid-content, .iframe-wrap .close").unbind("click");
			$(".overlaid-content, .iframe-wrap .close").click(function(){
				$(".overlaid-content").css('visibility', 'hidden');
			});
			
			$(".iframe-wrap").unbind("click");
			$(".iframe-wrap").click(function(e){
				e.stopPropagation();
			});
			
			$(".overlaid-content").css('visibility', 'visible');
		});
		
	},
	
	setupPinterestAnalytics : function(){
		$('.icon-pinterest-2').click(function(){
			com.google.analytics.europeanaEventTrack("Pinterest Activity", "pinterest site");
		});
	}

	
};


