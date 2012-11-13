(function() {
	
	'use strict';

	var initResponsiveImages = function(){

		var setup = function(){
			var initialSuffix = '_1.'; // smallest by default
			if(jQuery.browser.msie  && ( parseInt(jQuery.browser.version, 10) === 7 || parseInt(jQuery.browser.version, 10) === 8 )  ){
				initialSuffix = '_4.'; // largest by default
			}
			new responsiveGallery({
				imgSelector		: '.responsive',
				initialSuffix	: initialSuffix,
				suffixes: {
					'1': '_1.',
					'2': '_2.',
					'3': '_3.',
					'4': '_4.'
				}
			});
		};
		setup();
	};
	
	var initCarousels = function(){

//alert("initCarousels   1");

		Galleria.loadTheme(eu.europeana.vars.branding + '/js/galleria/themes/europeanax/galleria.europeanax.js');
		Galleria.configure({
				transition:		'fadeslide',		/* fade, slide, flash, fadeslide, pulse */
				carousel:		true,
				carouselSpeed:	1200,				/* transition speed */
				carouselSteps:	2,
				easing:			'galleriaOut',
				imageCrop:		false,				/* if true, make pan true */
				imagePan:		false,
				lightbox:		true,
				responsive:		true,
				idleMode:		true,
				popupLinks:		true,
				debug:			false
		});

//alert("initCarousels   2");
		
		$('<img src="' + carouselData[0].image + '" style="visibility:hidden"/>').appendTo("#carousel-1");
		$("#carousel-1").imagesLoaded(
			function() {

				var imgW			= $(this).width();
				var imgH			= $(this).height();
				
				$("#carousel-1 img").remove();

				var carousel		= jQuery("#carousel-1");
				var parentWidth		= carousel.width();
				
				var ratio			= imgW / imgH;
				var thumb			= jQuery('<div class="galleria-thumbnails-container"></div>').appendTo(carousel);
				
				carousel.css("height",  (parentWidth/ratio) + thumb.height() + 5 + "px");
				
				carousel.css("width",	"100%");
				thumb.remove();
				
				jQuery('#carousel-1').galleria({
					dataSource:carouselData,
					autoplay:17000
				});
			
			}).each(function() {
				if(this.complete){
					$(this).load();
				}
		});
		
		
		// Make sections collapsible
		jQuery("#section-blog").Collapsible({
			headingSelector:	"#collapse-header-1",
			iconSelector:		".collapse-icon",
			bodySelector:		".collapse-content",
			expandedClass:		'icon-arrow',
			collapsedClass:		'icon-arrow-3',
			executeDefaultClick: true,
			toggleFn: function(){return $("#mobile-menu").is(":visible");},
			fireFirstOpen:		function(){
				jQuery.ajax({
					url: 'index.html?fragment=blog',
					dataType: 'json',
					success: function(data){
						$("#section-blog .collapse-content").html(data.markup);
					},
					error: function(x, status, e){
						alert("error = " + JSON.stringify(e));
					}
				});

				
			}
		});
		

		jQuery("#section-featured-content").Collapsible({
			headingSelector:	"#collapse-header-2",
			iconSelector:		".collapse-icon",
			bodySelector:		".collapse-content",
			followerSelector:	"#section-featured-partner",
			expandedClass:		'icon-arrow',
			collapsedClass:		'icon-arrow-3',
			fireFirstOpen:		function(){
				jQuery.ajax({
					url: 'index.html?fragment=featuredContent',
					dataType: 'json',
					success: function(data){
						$("#section-featured-content .collapse-content").html(data.markup);
						$("#collapse-header-2").parent().after(data.markup2);
					},
					error: function(x, status, e){
						alert("error = " + JSON.stringify(e));
					}
				});				
			},
			toggleFn: function(){return $("#mobile-menu").is(":visible");}
		});

		
		jQuery("#section-pinterest").Collapsible({
			headingSelector:	"#collapse-header-3",
			iconSelector:		".collapse-icon",
			bodySelector:		".collapse-content",

			expandedClass:		'icon-arrow',
			collapsedClass:		'icon-arrow-3',
			fireFirstOpen:		function(){
				jQuery.ajax({
						url: 'index.html?fragment=pinterest',
						dataType: 'json',
						success: function(data){
							$("#section-pinterest .collapse-content").html(data.markup);
							
							var carousel3Data = data.data.carousel3Data;
							
							jQuery('#carousel-3').galleria({
								dataSource:carousel3Data,
								extend: function(e){
									// add ellipsis
									var doEllipsis = function(){
										var ellipsisObjects = [];
										jQuery('.europeana-carousel-info').each(
											function(i, ob){
												ellipsisObjects[ellipsisObjects.length] = new Ellipsis($(ob));					
											}
										);
										$(window).bind('resize', function(){
											for(var i=0; i<ellipsisObjects.length; i++ ){
												ellipsisObjects[i].respond();
											}
										});
									};

									$(this).ready(function(e) {
										setTimeout(doEllipsis, 1000);
									});
								} 
							});
							
							
						},
						error: function(x, status, e){
							alert("error = " + JSON.stringify(e));
						}
				});
				
        		

        		/*
				jQuery('#carousel-3').galleria({
					dataSource:carousel3Data,
					extend: function(e){
						// add ellipsis
						var doEllipsis = function(){
							var ellipsisObjects = [];
							jQuery('.europeana-carousel-info').each(
								function(i, ob){
									ellipsisObjects[ellipsisObjects.length] = new Ellipsis($(ob));					
								}
							);
							$(window).bind('resize', function(){
								for(var i=0; i<ellipsisObjects.length; i++ ){
									ellipsisObjects[i].respond();
								}
							});
						};

						$(this).ready(function(e) {
							setTimeout(doEllipsis, 1000);
						});
					} 
				});
				*/
				
			},
			
			toggleFn: function(){return $("#mobile-menu").is(":visible");}
		});
	};
	
	var initAddThis = function(){
return;
		var url = jQuery('head link[rel="canonical"]').attr('href'),
			title = jQuery('head title').html(),
			description = jQuery('head meta[name="description"]').attr('content');
			window.addthis_config = com.addthis.createConfigObject({
				pubid : eu.europeana.vars.addthis_pubid,
				ui_language: eu.europeana.vars.locale,
				data_ga_property: eu.europeana.vars.gaId,
				data_ga_social : true,
				data_track_clickback: true,
				ui_click: true,		// disable hover
				ui_use_css : true});
		
		// nb: tweet does not accept twitter templates, it only accepts html attributes
		// @see /js/com/addthis/addthis.js for those attributes
		
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

		
		/*
		<span style="width:100%;" title="get the title" class="addthis_toolbox addthis_default_style" addthis:title="Europeana - Homepage" addthis:description="Europeana - Homepage">
			<a class="addthis_button">
				<span title="Share item on facebook, twitter, etc.">
					<span aria-hidden="true" class="icon-share">
					</span>
				</span>
			</a>
		</span>	
		*/

		var addThisHtml = com.addthis.getToolboxHtml_ANDY({
			html_class : '',
			url : url,
			title : title,
			description : description,
			services : {
				compact			:	{},
				tweet			:	{},
				google_plusone	:	{},
				facebook_like	:	{},
				email			:	{},
				pinterest		:	{}

			}
			//, link_html : $('#shares-link').html()
		});

		
		
		$("#addthis_new").html(addThisHtml);
		alert("done: " + $("#addthis_new").length + "    \n\n\n" + addThisHtml   );
		
		$('#addthis_new .addthis_button')					.html('+')
		$('#addthis_new .addthis_button_tweet')				.html('T')
		$('#addthis_new .addthis_button_google_plusone')	.html('G')
		$('#addthis_new .addthis_button_facebook_like')		.html('F')
		
		
//		alert("com.addthis.loaded = " + com.addthis.loaded  );
		
		
		//addthis.addEventListener('addthis.ready' );
		
		com.addthis.init( null, true, false,
			function(){
				alert("add this has loaded..... rerork the dom");
				
//				$('#addthis_new .addthis_button_facebook_like')		.html('<span class="icon-facebook"></span>');

				
			}
		);
		
		return;
		
		
		
		//alert("addThisHtml = " + addThisHtml);
		
		jQuery('#shares-link').html(
			addThisHtml
		);
		
		jQuery('#shares-link').hide();
		com.addthis.init( null, true, false );
		
		setTimeout( function() {
			jQuery('#shares-link').fadeIn(function(){
				$(this).css("display", "inline-block");
			}); },
			600 );

		
	};
	
	var init = function() {
		js.loader.loadScripts([{
			file : 'jquery.imagesloaded.min.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/jquery/' + js.min_directory,
			callback : function() {
				initCarousels();
			}
		}]);
//		initCarousels();
		
		initResponsiveImages();
		
		
		
		js.loader.loadScripts([{
			file: 'addthis' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/com/addthis/' + js.min_directory,
			callback : function() {
				initAddThis();
			}
		}]);
		
	};
	
	jQuery(document).ready(function(){
		init();
	});
	
	
}());