(function() {
	
	
	function init() {
		loadDependencies();

		// Andy: conditional load test
		jQuery("#query-input").focus(function(){
			alert("focus");
			europeana_bootstrap.common.loadResultSizer(
				function(){ console.log("in callback for index.js loadResultSizer"); }
			);
		});
	}
	
	
	function loadDependencies() {
		
		js.loader.loadScripts([{
			name : 'carousel',
			file: 'carousel' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/com/gmtplusone/' + js.min_directory,
			callback : function() { createCarousel(); }
		}]);
		
		js.loader.loadScripts([{
			name : 'addthis',
			file: 'addthis' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/com/addthis/' + js.min_directory,
			callback : function() { addThis( function() { addFacebookActivities(); } ); }
		}]);
		
		//rssFeed(jQuery('#pinterest'), eu.europeana.vars.pinterest.feedUrl);
	}
	
	
	function createCarousel() {
		
		eu.europeana.carousel = {};
		eu.europeana.carousel.index = new com.gmtplusone.carousel( '#carousel' );
		eu.europeana.carousel.index.init();
		
	}
	
	
	function addFacebookActivities() {

		// src needs &, not &amp;, however # for the color needs to be %23
		// recommendations s/b true, if false and not logged into facebook, nothing will appear
		// '<iframe src="http://www.facebook.com/plugins/activity.php?site=europeana.eu&amp;width=250&amp;height=320&amp;header=false&amp;colorscheme=light&amp;font=arial&amp;border_color=%23f2f2f2&amp;recommendations=true" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:250px; height:320px;" allowTransparency="true"></iframe>'
		jQuery('#facebook-activities').attr( 'src', 'http://www.facebook.com/plugins/activity.php?site=europeana.eu&width=250&height=320&header=false&colorscheme=light&font=arial&border_color=%23f2f2f2&recommendations=true' );

	}
	
	
	function addThis( callback ) {
		
	
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
		
		
		jQuery('#header').append(
				
			com.addthis.getToolboxHtml({
				
				html_class : 'addthis',
				url : url,
				title : title,
				description : description,
				services : {
					counter : { style : 'addthis_bubble_style' },
					compact : {},
					twitter : {},
					google_plusone : { count : 'false' },
					facebook_like : { layout : 'button_count', width : '51' }
				}
				
			})
			
		);
	
		jQuery('.addthis').hide();
		com.addthis.init( null, true, false );
		setTimeout( function() { jQuery('.addthis').fadeIn(); if ( callback ) { callback(); } }, 700 );
	}	
	

	rssFeed = function(element, url) {
		var entry	= eu.europeana.vars.pinterest.item;
		var html	= '';

		// Add body
		html += '<div class="rssBody"><ul><li class="rssRow">';

		var content = "";
		content = entry.description;
		
		// remove stray urls ins the content
		var regexUrls = / http:\/\/[^<]*/g;
		content = content.replace(regexUrls, "");
		
		// put in a line below the image
//		var regexPostImg = /"><\/a>/g;
//		content = content.replace(regexPostImg, '"></a><h3 class="bordertop">&nbsp;</h3>');

		// make links open in new tab
		content = content.replace('<a ', '<a target="_blank" ');

		content = content.replace('<p>',	'');
		content = content.replace('</p>',	'');

		
		html += '<a class="pinterest-link" href="http://' + entry.link + '" title="' + entry.title + ' " target="_blank">' + content + '</a>';

//		var titleText = '&nbsp;&nbsp;&nbsp;&nbsp;';   
//		html +=		'<h3>' + titleText	+ '</h3>';
		html += '</li>';
		html += '</ul></div>';

		jQuery(element).html(html);
		
		jQuery(element).find("img").load(function() {
			// correct tall image heights & widths
			
			// 1) remove the float to get an accurate measurement
			
			var floatVal = jQuery(".rssBody li").css("float");
			jQuery(".rssBody li").css("float", "none");
			
			// 2) pull in image to correct height
			
			var initialImgWidth = jQuery("li.rssRow img").width();
			while(jQuery("#pinterest-wrapper").height() > jQuery("#pinterest-wrapper").parent().height()){
				initialImgWidth -= 10;
				jQuery("li.rssRow a img").width(initialImgWidth + "px");
				if(initialImgWidth <= 0){
					break;
				} 
			}
			
			// 3) restore the float
			jQuery(".rssBody li").css("float", floatVal);

		});
		
		//correct href for images so they point to Pinterest

		jQuery('.rssFeed a').each(function() {
			var href = jQuery(this).attr('href');
			if(href.indexOf("/")==0){
				jQuery(this).attr('href', 'http://www.pinterest.com' + href);
			}
			
			jQuery(this).click(function(){
				com.google.analytics.europeanaEventTrack('pinterest item', 'Pinterest Activity', jQuery(this).attr("href") );
				return true;
			});
			
		});

		
		jQuery('a.pinterest-button').click(function(){
			com.google.analytics.europeanaEventTrack('pinterest site', 'Pinterest Activity');
			return true;
		});

	};
		
	init();
	
})();
