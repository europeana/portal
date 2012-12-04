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

		$('.submit-cell').css("width",	$('.submit-cell button')	.outerWidth(true) + "px"); 
		$('.menu-cell').css("width",	$('#search-menu')			.outerWidth(true) + "px");
		
		$('.submit-cell button').css("border-left",	"solid 1px #4C7ECF");	// do this after the resize to stop 1px gap in FF
		
		$("#query-search>table")									.css("display",		"none");
		$("#query-search>table")									.css("visibility",	"visible");
		$("#query-search>table").fadeIn(600, function(){
			jQuery("#query-input").focus();
		});

		this.initResponsiveUtility();
		this.addQueryFocus();
		this.addLanguageChangeHandler();
		this.addRefineSearchClickHandler();		
		this.addAjaxMethods();
		this.addMenuFocusTriggers();
		this.setupResultSize();
		this.setupSearchMenu();
		this.setupLanguageMenu();
		
		this.addAutocompleteHandler();

		jQuery('#query-search').bind('submit', this.handleSearchSubmit );
		
		$("#footer-iframe").attr("src", "/" + eu.europeana.vars.portal_name + '/newsletter.html');
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
					if( ['myeuropeana.html', '/portal/'].indexOf(self.getActive()) > -1 ){
						window.location.href = self.getActive();
						return;
					}
					$("input[name=embeddedlang]").val(self.getActive());
					$("#language-selector").submit();
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
						console.log("find  " + searchType + " in " + searchTerm);
						
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
		
		jQuery('#menu-main li ul li a')
		
			.focusin(function() {
				
				jQuery(this).parent().parent()
					.css({
						'margin-top' : 0,
						'opacity' : 1
					});
				
				jQuery(this).parent().parent().prev().children().eq(0)
					.css({
						'color' : '#fff',
						'background-color' : '#000',
						'background-position' : 'right -189px'
					});
			})
			
			.focusout(function() {
				
				jQuery(this).parent().parent()
					.css({
						'margin-top' : -499,
						'opacity' : 0
					});
				
				jQuery(this).parent().parent().prev().children().eq(0)
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
		
		var exceptions = [
			'login.html',
			'forgotPassword.html',
			'register-success.html'
		],
		i,
		ii = exceptions.length,
		input_focus = true;
		
		for ( i = 0; i < ii; i += 1 ) {
			
			if ( exceptions[i] === eu.europeana.vars.page_name ) {
				
				input_focus = false;
				break;
				
			}
			
		}
		
		
		jQuery('#query-input').focus(function(){
			$("#query-full table tr:first-child .query-cell").addClass("glow");
		});
		jQuery('#query-input').blur(function(){
			$("#query-full table tr:first-child .query-cell").removeClass("glow");
		});
		
		
		if ( input_focus ) {
			jQuery('#query-input').focus();
		}
		
	},
	
	
	addAutocompleteHandler : function() {
		
		jQuery('#query-input, #qf').each(function(i, id){
			
			$(id).autocomplete({
				
			    open: function(event, ui) {
			        var oldLeft		= jQuery(".ui-autocomplete").offset().left;
			        var oldWidth	= jQuery(".ui-autocomplete").width();
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
					
					jQuery.getJSON( '/' + eu.europeana.vars.portal_name + '/suggestions.json', request, function(data) {
						
						//create array for response objects
						var suggestions = [];
						
						//process response
						jQuery.each( data.suggestions, function(i, val) {
							val.label = val.term;
							suggestions.push( val );
						});
						
						//pass array to callback
						response( suggestions );
						
					});
					
				},
				
				select : function(event, ui) {

					switch ( this.id ) {
						case 'query-input' :

							if(completionClasses[ui.item.field]){
								eu.europeana.header.searchMenu.setActive(completionClasses[ui.item.field]);
							}
							setTimeout( function() { jQuery('#query-search').submit(); }, 10 );
							break;
							
						case 'qf' :
							
							setTimeout( function() { jQuery('#refine-search-form').submit(); }, 10 );
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
		
		jQuery('#embeddedlang').change( function() {
			
			jQuery('#language-selector').submit();
			
		});
		
	},
	
	
	addRefineSearchClickHandler : function() {
		jQuery('#refine-search').click(function(e) {
			e.preventDefault();
			jQuery('#refine-search-form').fadeIn();
			jQuery('#qf').focus();
			
		});
		
		jQuery('#close-refine-search').click(function(e) {
			
			e.preventDefault();
			jQuery('#refine-search-form').fadeOut();
			
		});
		
	},
	
	
	addAjaxMethods : function() {
		
		eu.europeana.ajax.methods = new eu.europeana.ajax();
		eu.europeana.ajax.methods.init();
		
	},


	
	handleSearchSubmit : function( e ) {

		var emptySearch = jQuery('#query-input').val().length < 1 || (eu.europeana.header.searchMenu.getActive() == jQuery('#query-input').val());
		if ( emptySearch ) {
			
			e.preventDefault();
			jQuery('#query-input').addClass('error-border');
			jQuery('#additional-feedback')
				.addClass('error')
				.html(eu.europeana.vars.msg.search_error);
			jQuery('#query-input').val("");
		}
		
	}
	
};


