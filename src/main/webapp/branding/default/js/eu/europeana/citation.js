/**
 *  citation.js

 *
 *  @package	eu.europeana
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @created	2011-07-07 11:14 GMT +1
 *  @version	2011-07-07 15:02 GMT +1
 */

/**
 *  @package	eu.europeana
 *  @author		dan entous <contact@gmtpluosone.com>
 */


js.utils.registerNamespace( 'eu.europeana.citation' );

eu.europeana.citation = {
	
	options : {
		
		link : '#citation-link',
		close_button : '.close-button',
		container : '#citation',
		contents : '#citations',
		heading : '#citation .heading',
		active : 'active',
		placed : false,
		tabbed : false,
		html : ''
		
	},
	
	
	init : function() {
		
		var self = this,
			contents = jQuery( this.options.contents ).html(),
			heading = jQuery( this.options.heading ).html();

		this.options.html =
			'<div class="external-services-container">' +
					'<div id="citetabs">' +				
						'<h3><a href="#citestyle1" class="active"	onClick="javascript:return false;">' + eu.europeana.vars.msg.cite.citation + '</a></h3>' + 
						'<h3><a href="#citestyle2"  				onClick="javascript:return false;">' + eu.europeana.vars.msg.cite.footnote + '</a></h3>' + heading ;
 				jQuery( this.options.contents ).find(".citation").each(function(index, item){
 					var clss	= '';
 					var style	= ' style="display:none;"';
 					if(index==0){
 						clss	= 'active';
 						style	= '';
 					}
 					self.options.html += '<div class="' + clss + '" ' + style + ' id="citestyle' + (index+1) + '">' + item.innerHTML + '</div>';
 				});
		this.options.html += '</div></div>';
		jQuery( this.options.link ).bind( 'click', { self : self }, this.handleCitationClick );
	},
	
	
	handleCitationClick : function( e ) {
		com.google.analytics.europeanaEventTrack("Wikipedia Citation");

		var self = e.data.self;
		e.preventDefault();
		
		if ( !self.options.placed ) {
			
			jQuery( self.options.container )
				.html( self.options.html )
				.addClass( self.options.active );
			
			jQuery( self.options.container + ' ' + self.options.close_button )
				.bind( 'click', { self : self }, self.toggleCitation );
			
			self.options.placed = true;
		}
		self.toggleCitation( e );
	},

	
	toggleCitation : function( e ) {
		e.preventDefault();
		jQuery( e.data.self.options.container ).toggle('slow', function(){
			eu.europeana.citation.addTheTabs();
		} );
		
	},
	
	selectElementContents : function(el) {
	    var range;
	    if (window.getSelection && document.createRange) {
	        range = document.createRange();
	        var sel = window.getSelection();
	        if(typeof sel != 'undefined' && sel != null){
		        range.selectNodeContents(el);
		        try{
		        	sel.removeAllRanges();
		        }catch(e){}
		        try{
		        	sel.addRange(range);
		        }catch(e){}
	        }
	    } else if (document.body && document.body.createTextRange) {
	    	try{
		        range = document.body.createTextRange();
		        range.moveToElementText(el);
		        range.select();
	    	}
	    	catch(e){}
	    }
	},
	
	addTheTabs : function(){
		if(!eu.europeana.citation.options.tabbed){
			eu.europeana.tabsCitation = {};
			eu.europeana.tabsCitation.tabs = new com.gmtplusone.tabs(
			'#citetabs',
				{
					callbacks : {
						opened : function(){
							var openTab = com.gmtplusone.tabs.prototype.getOpenTabId();
							var highlight = function(){
								
								eu.europeana.citation.selectElementContents(document.getElementById(openTab));
							}
							setTimeout(highlight, 300);
						}
					}
				}
			);
			eu.europeana.citation.options.tabbed = true;
			eu.europeana.tabsCitation.tabs.init(
					function(){
						eu.europeana.citation.selectElementContents(document.getElementById("citestyle1"));
					}		
			);
		}
		else{
			var openTab = com.gmtplusone.tabs.prototype.getOpenTabId();
			if(openTab){
				eu.europeana.citation.selectElementContents(document.getElementById(openTab));				
			}
		}
	}
};



