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
		var langSelect=  jQuery("select[name=embeddedlang]");
		langSelect.change(function(){
			jQuery(this).parent().submit();
		});

		
		
		this.addQueryFocus();
		this.addLanguageChangeHandler();
		this.addAutocompleteHandler();
		this.addRefineSearchClickHandler();		
		this.addAjaxMethods();
		this.addMenuFocusTriggers();
		
		//this.enableMapLink();
		//this.enableTimelineLink();
		
		this.setupResultSize();
		
		this.setupMobileMenu();
		this.setupSearchMenu();
		

		jQuery('#save-search').bind('click', this.handleSaveSearchClick );
		jQuery('#query-search').bind('submit', this.handleSearchSubmit );
		
		jQuery('.jump-to-page').bind('submit', this.jumpToPageSubmit );
		jQuery('.jump-to-page #start-page').bind('keypress', this.validateJumpToPage);
		
	},
	
	setCookie: function(val){
		document.cookie = "europeana_rows=" + val;
	},
	getCookie: function(){
		var cookies		= document.cookie.split(";");
		var cookieVal	= null;
		
		for(i=0; i<cookies.length; i++)
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
	
	setupMobileMenu: function(){

		var menu = new EuMenu(
			$("#mobile-menu"),
			{
				"fn_item": function(self){
				},

				"fn_init": function(self){
				},
				
				"fn_submit":function(self){
				}
			
			}
		);
		menu.init();
	},
	
	
	setupSearchMenu:function(){
		
		var menu = new EuMenu( 
			$("#search-menu"),
			{
				"fn_item": function(self){
				},

				"fn_init": function(self){
					var input		= $('#query-input');
					var searchTerm	= eu.europeana.vars.query.replace("*:*", "");
					
					self.cmp.find(".item a").each(function(i, ob){
						var searchType = $(ob).attr("class");
						if(searchTerm.indexOf(searchType) == 0){
							self.setLabel(searchType);
							input.val(   searchTerm.substr( searchType.length, searchTerm.length)  );
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
          'full-doc.html',
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
		
		if ( input_focus ) {
			
			jQuery('#query-input').focus();
			
		}
		
	},
	
	
	addAutocompleteHandler : function() {
		
		if ( js.debug ) {
			
			jQuery('#query-input, #qf')
				.autocomplete({
					
					//minLength : 2,
					delay : 200,
					//dataType : 'text',
					
					//define callback to format results
			        source: function( request, response ) {
			            
			        	//pass request to server
			        	jQuery.getJSON(  '/' + eu.europeana.vars.portal_name + '/suggestions.json', request, function(data) {
			        		
			                //create array for response objects
			                var suggestions = [];
			                
			                //process response
			                jQuery.each( data.suggestions, function(i, val) {
			                    
			                	suggestions.push( val );
			                    
			                });
			                
			                //pass array to callback
			                response( suggestions );
			                
			            });
			        	
			        },
			        
			        select : function(e) {
			        	
			        	switch ( this.id ) {
			        	
				        	case 'query-input' :
				        		
				        		setTimeout( function() { jQuery('#query-search').submit(); }, 10 );
				        		break;
				        		
				        		
				        	case 'qf' :
				        		
				        		setTimeout( function() { jQuery('#refine-search-form').submit(); }, 10 );
				        		break;
			        	
			        	}
			        	
			        }
				
				});
			
		} else {
			
			jQuery('#query-input')
				.autocomplete( {
					
					//define callback to format results
			        source: function( request, response ) {
			            
			        	//pass request to server
			        	jQuery.getJSON(  '/' + eu.europeana.vars.portal_name + '/suggestions.json', request, function(data) {
			        		
			                //create array for response objects
			                var suggestions = [];
			                
			                //process response
			                jQuery.each( data.suggestions, function(i, val) {
			                    
			                	suggestions.push( val );
			                    
			                });
			                
			                //pass array to callback
			                response( suggestions );
			                
			            });
			        	
			        }
				
				});
			
		}
		
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
			return parseInt( $this.val() +  String.fromCharCode(key) ) <= maxRows; 
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
	
	jumpToPageSubmit : function( e ){
		
		// set the "start" parameter
		
		var $jumpToPage	= $(this).parent();
		var rows		= parseInt($jumpToPage.find("input[name=rows]").val());
		var pageNum		= parseInt($jumpToPage.find("#start-page").val());
		
		$jumpToPage.find("#start").val(1 + ((pageNum-1)*rows) );
		return true;
	},
	
	handleSearchSubmit : function( e ) {
		
		if ( jQuery('#query-input').val().length < 1 ) {
			
			e.preventDefault();
			jQuery('#query-input').addClass('error-border');
			jQuery('#additional-feedback')
				.addClass('error')
				.html(eu.europeana.vars.msg.search_error);
			
		}
		
	},
	
	
	handleSaveSearchClick : function( e ) {
		
		e.preventDefault();
		
		var ajax_feedback = {
			
			saved_searches_count : 0,
			$saved_searches : jQuery('#saved-searches-count'),
				
			success : function() {
				
				var html =
					'<span>' +
						eu.europeana.vars.msg.search_saved +
					'</span>';
				
				eu.europeana.ajax.methods.addFeedbackContent( html );
				eu.europeana.ajax.methods.showFeedbackContainer();
				
				ajax_feedback.saved_searches_count = parseInt( ajax_feedback.$saved_searches.html(), 10 );
				ajax_feedback.$saved_searches.html( ajax_feedback.saved_searches_count + 1 );
				
			},
			
			failure : function() {
				
				var html =
					'<span id="save-search-feedback" class="error">' +
						eu.europeana.vars.msg.search_save_failed +
					'</span>';
				
				eu.europeana.ajax.methods.addFeedbackContent( html );
				eu.europeana.ajax.methods.showFeedbackContainer();
				
			}
			
		},
		
		ajax_data = {
				
			className : "SavedSearch",
			query : jQuery('#query-to-save').val(),
			queryString : jQuery('#query-string-to-save').val()
			
		};
		
		eu.europeana.ajax.methods.user_panel( 'save', ajax_data, ajax_feedback );
		
	}
};

eu.europeana.header.init();
