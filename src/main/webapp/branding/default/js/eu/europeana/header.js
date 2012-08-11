/**
 *  header.js
 *
 *  @package	eu.europeana
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @created	2011-07-06 17:34 GMT +1
 *  @version	2011-10-20 08:26 GMT +1
 */

/**
 *  @package	eu.europeana
 *  @author		dan entous <contact@gmtplusone.com>
 */
js.utils.registerNamespace( 'eu.europeana.header' );

eu.europeana.header = {
	
	init : function() {
		this.addQueryFocus();
		this.addLanguageChangeHandler();
		
		// Andy: TODO: enable this
		//this.addAutocompleteHandler();
		this.addRefineSearchClickHandler();		
		this.addAjaxMethods();
		this.addMenuFocusTriggers();
		
		this.enableMapLink();
		this.enableTimelineLink();
		
		jQuery('#save-search').bind('click', this.handleSaveSearchClick );
		jQuery('#query-search').bind('submit', this.handleSearchSubmit );
	},
	
	enableMapLink:function(){
		if(typeof eu.europeana.vars.mapview.json_url === "undefined"){
			return;
		}
		if(!eu.europeana.vars.query){
			jQuery(".mapview_icon").css('display', 'block');
			return;
		}
		jQuery.ajax({
		  url: eu.europeana.vars.mapview.json_url + "&rows=1",
		  dataType: 'json',
		  success: function(data){
			  if(data.totalResults > 0){
				  jQuery(".mapview_icon").css('display', 'block');	
			  }
			  if(eu.europeana.vars.page_name == "map.html"){
				  eu.europeana.header.placeBreadCrumb(data.breadcrumb);				  
			  }
		  }
		});
	},
	
	enableTimelineLink:function(){
		if(typeof eu.europeana.vars.timeline.json_url === "undefined"){
			return;
		}
		if(!eu.europeana.vars.query){
			jQuery(".timeline_icon").css('display', 'block');
			return;
		}
		jQuery.ajax({
			url: eu.europeana.vars.timeline.json_url + "&rows=1",
			dataType: 'json',
			success: function(data){
				if(data.totalResults > 0){
					jQuery(".timeline_icon").css('display', 'block');	
				}
			    if(eu.europeana.vars.page_name == "timeline.html"){
			    	eu.europeana.header.placeBreadCrumb(data.breadcrumb);
			    }
			}
		});
	},
	
	
    placeBreadCrumb : function( breadcrumb ){
    	if(typeof breadcrumb == 'undefined' || typeof eu.europeana.vars.msg.matches == 'undefined'){
    		return;
    	}
    	
    	var i,
    		ii = breadcrumb.length,
    		breadcrumb_html =
    			'<li>' +
    				'<span class="bold black">' + eu.europeana.vars.msg.matches + ':</span>' +
    			'</li>';
    	
    	if ( breadcrumb.length > 1 ) {
	    	for ( i = 0; i < ii; i += 1 ) {
	    		breadcrumb_html += '<li>';
	    		if ( breadcrumb[i].url ) {
	    			breadcrumb_html +=
	    				'<a href="' + breadcrumb[i].url + '">' + breadcrumb[i].label + '</a>';
	    		}
	    		else{
	    			breadcrumb_html += '<span class="bold">' + breadcrumb[i].label + '</span>';
	    		}
		    		
		    	if ( ( i + 1 ) !== ii ) {
		    		breadcrumb_html += ' > </li>';
		    	}
		    	else{
		    		breadcrumb_html += '</li>';
		    	}
	    	}
    	}
    	else{
    		breadcrumb_html += '<li>' + breadcrumb[0].label + '</li>';
    	}
    	jQuery('#breadcrumb').html( jQuery( breadcrumb_html ) );
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
			
			jQuery('#query-input, #rq')
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
				        		
				        		
				        	case 'rq' :
				        		
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
			jQuery('#rq').focus();
			
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
