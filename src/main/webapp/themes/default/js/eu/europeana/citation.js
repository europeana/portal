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

		/*
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
		*/
		this.options.html = '' 
			+	'<div class="external-services-container">'
			+		'<a rel="nofollow" title="Close" class="close-button icon-remove" href="">&nbsp;</a>' 
			+		'<div class="accordion-tabs" id="citation-tabs">'
			+			'<div class="section" id="citestyle1">'
			+				'<a href="#">Citation</a>'
			+				'<div class="content">'
			+					'Copy and paste the wiki-markup below:'
			+					'{{cite web | url=5099b038e4b05dd5e3ba8b69|title=Kniender Stifter|accessdate=2012-11-27 |publisher=Europeana}}'
			+				'</div>'
			+			'</div>'
			+			'<div class="section" id="citestyle2">'
			+				'<a href="#">Footnote</a>'
			+				'<div class="content">'
			+					'Copy and paste the wiki-markup below:'
			+					'<br />'
			+					'&lt;ref&gt;{{cite web | url=5099b038e4b05dd5e3ba8b69|title=Kniender Stifter|accessdate=2012-11-27 |publisher=Europeana}}&lt;/ref&gt;'
			+				'</div>'
			+			'</div>'
			+		'</div>'
			+	'</div>';
		
		
		//alert("html = \n\n" + this.options.html);
		
		jQuery( this.options.link ).bind( 'click', { self : self }, this.handleCitationClick );
	},
	
	
	handleCitationClick : function( e ) {

		com.google.analytics.europeanaEventTrack("Wikipedia Citation");

		e.preventDefault();
		
		var self = e.data.self;
		
		if(!self.options.placed){
			$(self.options.container).html(self.options.html);
			$(self.options.container + ' ' + self.options.close_button).bind('click', { self : self }, self.toggleCitation);
			self.options.placed = true;
		}
		self.toggleCitation(e);
	},

	
	toggleCitation : function( e ) {
		e.preventDefault();
		$(e.data.self.options.container).toggle('slow', function(){
			eu.europeana.citation.addTheTabs();
		});
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

			eu.europeana.citation.options.tabbed = true;
			
			var callback = function(index, id){
				if($("#mobile-menu").is(":visible") ){
					eu.europeana.citation.selectElementContents(  $('#citation .section.active>.content.is-open')[0]   );					
				}
				else{
					eu.europeana.citation.selectElementContents($('#citation .tab_content')[0]);					
				}
			};
			
			eu.europeana.citation.tabs = new AccordionTabs( $('#citation-tabs'), callback );
			
			
			/*
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
			
			*/
			
		}
		else{
			
			
			eu.europeana.citation.selectElementContents( $('#citation .tab_content')[0]  );
					
									
	
		}
	}
	
};



