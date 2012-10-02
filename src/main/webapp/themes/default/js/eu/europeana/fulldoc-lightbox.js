js.utils.registerNamespace( 'eu.europeana.lightbox' );

eu.europeana.lightbox = {

		playMedia:false,
		
		overlay:null,
		
		carouselData: null,
		
		showMetadata:function(){
			jQuery(".content-wrap .info-open").show();
			eu.europeana.lightbox.metaDataToggled();				
		},

		hideMetadata:function(){
			jQuery(".content-wrap .info-open").hide();
			eu.europeana.lightbox.metaDataToggled();
		},

		hideMetadataCtrls:function(){
			jQuery(".showMeta").hide();
			jQuery(".hideMeta").hide();
		},
		
		showMetadataCtrls:function(){
			if(jQuery(".content-wrap .info-open").is(":visible")){
				jQuery(".hideMeta").show();
				jQuery(".showMeta").hide();
			}
			else{
				jQuery(".showMeta").show();
				jQuery(".hideMeta").hide();
			}
		},

		metaDataToggled:function(){
			if(jQuery(".content-wrap .info-open").is(":visible")){
				jQuery(".hideMeta").show();
				jQuery(".showMeta").hide();				
			}
			else{
				jQuery(".hideMeta").hide();
				jQuery(".showMeta").show();				
			}
			eu.europeana.lightbox.layout( jQuery(".content-wrap .info-open").is(":visible") ? "expanded" : "contracted" );
		},

		infoDefaults:function(){
			/* The values here indicate the amount of width or height the info panel will take up.
			 * Note that this is not the same as a "minimum height", as in portrait view, for example, the height is
			 * always zero.  This is because the image itself is assumed to take up height, and so the info panel next
			 * to it will not "take up" any more height.
			 * 
			 * You should not attempt to use these defaults to set minimum heights.
			 * */
			return {
			    "height": {
			    	"portrait":{// show enlarge close close open
			    		"expanded":		0,
			    		"contracted":	0
			    	},
			    	"landscape":{
			    		"expanded":		175,
			        	"contracted":	60
			    	},
			    	"none":{
			    		"expanded":		0,
			    		"contracted":	0			    		
			    	},
			    	"zoom":{
			    		"default":		0		    		
			    	}
			    },
			    "width":{
			     	"portrait":{
			     		"expanded":		250,
			     		"contracted":	250
			     	},
			     	"landscape":{
			     		"expanded":		0,
			     		"contracted":	0
			     	},
			    	"none":{
			    		"expanded":		0,
			    		"contracted":	0
				    },
				    "zoom":{
				    	"default":		0
				    }
			    }
			};
		},

		 /* portrait / landscape setting doesn't depend on the image aspect alone (i.e. w > h is not automatically landscape),
		  * but depends on the context - the screen aspect ratio.  This function returns the maximum dimensions that the image
		  * can be, as will as the orientation required to achieve this maximum. */
		calculateMaxImgSize : function(orientation, showMeta){
			var result			= {'w':0, 'h':0, 'o':orientation, 'top':0, 'left':0, 'shrink':false, 'info':{'w':0, 'h':0}, showMeta:showMeta, code:'', metaSameSize:false};
			
		    if(orientation == "none"){ // initial call when sizing image
		    	var allStats = [];
		    	var landscapeContracted, portraitContracted, landscapeExpanded, portraitExpanded;
		    	
	    		var comparatorGt	= function(a, b){ return a > b; };
	    		var comparatorGtE	= function(a, b){ return a >= b; };	    		
	    		
		    	if(showMeta != "expanded"){ // contracted / null
		    		landscapeContracted = eu.europeana.lightbox.calculateMaxImgSize("landscape",	"contracted");
		    		portraitContracted = eu.europeana.lightbox.calculateMaxImgSize("portrait",		"contracted");
			    	allStats.push(landscapeContracted);
			    	allStats.push(portraitContracted);
		    	}
		    	
		    	if(showMeta != "contracted"){ // expanded / null
		    		landscapeExpanded = eu.europeana.lightbox.calculateMaxImgSize("landscape",	"expanded");
		    		portraitExpanded = eu.europeana.lightbox.calculateMaxImgSize("portrait",	"expanded");
			    	allStats.push(landscapeExpanded);
			    	allStats.push(portraitExpanded);
		    	}

		    	if(landscapeExpanded && landscapeContracted){
		    		if(landscapeExpanded.w + landscapeExpanded.h == landscapeContracted.w + landscapeContracted.h){
		    			landscapeExpanded.metaSameSize		= true;
		    			landscapeContracted.metaSameSize	= true;
		    		}
		    	}
		    	if(portraitExpanded && portraitContracted){
		    		if(portraitExpanded.w + portraitExpanded.h == portraitContracted.w + portraitContracted.h){
		    			portraitExpanded.metaSameSize		= true;
		    			portraitContracted.metaSameSize		= true;
		    		}
		    	}
	    		
		    	jQuery(allStats).each(function(index, ob){
		    		var comparator = ob.showMeta ? comparatorGtE : comparatorGt;
		    		if( comparator( ob.w * ob.h, result.w * result.h ) ){
			    		result = function(){ 
			    			return ob;
			    		}();
			    	}
		    	});
		    }
		    else{	// portrait / landscape / zoom
				var infoDefaults	= eu.europeana.lightbox.infoDefaults();
				var infoW			= infoDefaults.width[orientation][showMeta]; 
				var infoH			= infoDefaults.height[orientation][showMeta];
				var overlay			= eu.europeana.lightbox.overlay;
	
				var contentWrap		= jQuery(overlay.find(".content-wrap"));
				var contentImage	= jQuery(contentWrap.find(".content-image"));
	
				var screenW			= jQuery(window).width();
				var screenH			= jQuery(window).height();
				
				var imgOrigWH		= eu.europeana.lightbox.getImgDimensionsFromSrc(contentImage.attr("src"));
	
				var zoomImgMargin	= 20;
				var maxAvailH		= screenH - ((2*zoomImgMargin) + infoH);
				var maxAvailW		= screenW - ((2*zoomImgMargin) + infoW);
				
			    var ratio			= 0;
			    var maxImgW			= imgOrigWH.w;
			    var maxImgH			= imgOrigWH.h;
	
			    // Check if the current width is larger than the max
			    if(maxImgW > maxAvailW){
			        ratio	= maxAvailW / maxImgW;
			        maxImgW	= maxAvailW;
			        maxImgH	= maxImgH * ratio;

			    }
	
			    // Check if current height is larger than max
			    if(maxImgH > maxAvailH){
			        ratio = maxAvailH / maxImgH;
			        maxImgH = maxAvailH;
			        maxImgW = maxImgW * ratio;
			    }
			    
			    result.w		= maxImgW;
		    	result.h		= maxImgH;
		    	
		    	result.top		= (screenH - (maxImgH+infoH))/2;
		    	result.left		= (screenW - (maxImgW+infoW))/2;
		    	
		    	result.shrink	= maxImgW < imgOrigWH.w;
		    	
		    	result.info.w	= infoW;
		    	result.info.h	= infoH;
		    }
		    return result;
		},
		
		zoomImg : function(){					
			var stats	= eu.europeana.lightbox.calculateMaxImgSize("zoom", "default");
			var img		= jQuery(".content-image");
			var html	= '<div class="zoomedImgDiv"><img id="zoomedImg" src="' + img.attr("src") + '" style="width:' + stats.w + 'px; height:' + stats.h + 'px;">' + (eu.europeana.vars.lightbox_rights ? eu.europeana.vars.lightbox_rights : '') + '</div>';

			jQuery("body").append(html);
			
			
			jQuery(".zoomedImgDiv").css("top",		stats.top + "px");
			jQuery(".zoomedImgDiv").css("left",		stats.left + "px");
			jQuery(".zoomedImgDiv").css("display",	"block");
			jQuery(".zoomedImgDiv").css("position",	"fixed");
			jQuery(".zoomedImgDiv").css("zIndex",	"9999");
			
			
			html = '<div id="zoomClose"></div>';
			jQuery(".zoomedImgDiv").append(html);
			jQuery('#zoomClose').click(function(){
				eu.europeana.lightbox.closeZoom();
				eu.europeana.lightbox.overlay.find(".close").click();
			});
			
			html = '<div id="enlittle"></div>';
			jQuery(".zoomedImgDiv").append(html);
			
			
			jQuery('#enlittle').click(function(){eu.europeana.lightbox.closeZoom();});
			
			jQuery(".zoomedImgDiv a").css("position", "absolute");
			jQuery(".zoomedImgDiv a").css("display", "block");
			jQuery(".zoomedImgDiv a").css("bottom", "4px");
			jQuery(".zoomedImgDiv a").css("right", "4px");

			eu.europeana.lightbox.overlay.hide();
		},

		closeZoom:function(){
			jQuery(".zoomedImgDiv").remove();
			if(eu.europeana.lightbox.overlay){				
				eu.europeana.lightbox.overlay.show();
			}
		},
		
//		init:function(src, carouselData) {
		init:function(src, navOb) {
			//alert("init")
			//eu.europeana.lightbox.carouselData = carouselData;
			eu.europeana.lightbox.navOb = navOb;
			
			jQuery(".lb-trigger span").overlay({
				mask: {
					color: '#ffffff',
					loadSpeed: 200,
					opacity: 0.9
				},
				closeOnClick: true,
				onBeforeLoad: function() {
					// src is used for (flash) movies
					eu.europeana.lightbox.overlay = this.getOverlay();
	
					var overlay 		= eu.europeana.lightbox.overlay;
					var contentWrap		= jQuery(overlay.find(".content-wrap"));
					var contentImage	= jQuery(contentWrap.find(".content-image"));
					var playerDiv		= jQuery(contentWrap.find(".playerDiv"));
					
					//var contentAnchor	= jQuery(playerDiv.find("a"));
					//var src				= contentAnchor.attr("href");
					
					if(src && typeof src === 'string'){
						var wmv		= src.match(/wmv$/)		|| src.match(/WMV$/);
						var mp3		= src.match(/mp3$/)		|| src.match(/MP3$/);
						var mp4		= src.match(/mp4$/)		|| src.match(/MP4$/);
						var avi		= src.match(/avi$/)		|| src.match(/AVI$/);
						var flac	= src.match(/flac$/)	|| src.match(/FLAC$/);
						var flv		= src.match(/flv$/)		|| src.match(/FLV/);

						eu.europeana.lightbox.playMedia = flv || flac || avi || mp3  || mp4 || wmv;

						if(eu.europeana.lightbox.playMedia){
							contentImage.hide();
							var dimensions = eu.europeana.lightbox.getDimensionsFromStyleAttr(contentAnchor.attr("style"));
							jQuery(playerDiv).css("width",	dimensions.w);
							jQuery(playerDiv).css("height",	dimensions.h);
							contentAnchor.hide();									
							var callback;
							if(wmv){
								callback = function(data){
									jQuery(playerDiv).html(eu.europeana.lightbox.getWmvPlayer(data.streamLocation, dimensions.w, dimensions.h, false));
								}
							}
							else if(flv || avi || mp4 || mp3){
								callback = function(data){
									jQuery(playerDiv).html(eu.europeana.lightbox.getJwPlayer(data.streamLocation, dimensions.w, dimensions.h, (eu.europeana.lightbox.playMedia + "").toLowerCase()  ));
									jwplayer("jwPlayer").setup({
									    modes: [
										        { type: 'html5' },
										        { type: 'flash', src: eu.europeana.vars.lightbox_swf },
										        {type: 'download'}
										    ],
										autostart: true,
										events:{
											ready:function(){

											}
										}
									});
								}
							}
							else{
								alert("flac files are not supported yet");
							}
							
							if(callback){
								// show a spinner (centered) during the download...
								var spinnerImgLocation =  eu.europeana.vars.branding + '/images/spinner.gif';
								var spinnerImgWidth = eu.europeana.lightbox.getImgDimensionsFromSrc(spinnerImgLocation).h;
								playerDiv.html('<img id="mediaSpinner" src="' + spinnerImgLocation + '"/>');
								jQuery("#mediaSpinner").css("margin-top", (dimensions.h - spinnerImgWidth)/2 + "px");
								jQuery.ajax({
									  url: src,
									  success: function(data){
										  callback(data);
									  }
								});
							}
						}
						else{
							contentImage.show();
							playerDiv.hide();
						}
					}
				},
				onLoad:function(){
					//eu.europeana.lightbox.layout(jQuery(".content-wrap .info-open").is(":visible") ? "expanded" : "contracted");
					eu.europeana.lightbox.layout();
				},
				onBeforeClose: function(){
					obj = document.getElementById("mediaPlayer");
					
					if(obj && typeof obj != 'undefined' && typeof obj.controls != 'undefined' && typeof obj.controls.stop != 'undefined'){
						obj.controls.stop();
					}
					var closeBtn = eu.europeana.lightbox.overlay.find(".close");
					if(typeof closeBtn.data("originalCss") != 'undefined'){
						closeBtn.css("right",	closeBtn.data("originalCss").right);
					}
					
					var jw = jwplayer("jwPlayer");
					if(jw){
						jw.stop();
					}
				}
			});
			
			jQuery(".showMeta").click(function(){
				eu.europeana.lightbox.showMetadata();
			});
			
			jQuery(".hideMeta").click(function(){
				eu.europeana.lightbox.hideMetadata();
			});
			
			
			$(".info-open .item-metadata:first").focus();
		},// end init()

		playerOps : {
			wmode: 'opaque',
		    clip: {
				autoBuffering: true,
				autoPlay: true,
				scaling: 'scale'
			},
			plugins: {
				controls: {
	                autoHide: 'never',
					play: true,
					volume: true,
					mute: true,
					time: true,
					stop: true,
					playlist: false,
					fullscreen: false
				}
	        }
		},
		
		layout:function(showMeta){
			
			com.google.analytics.europeanaEventTrack("Lightbox View");
			
			var overlay = eu.europeana.lightbox.overlay;
			var contentWrap = jQuery(overlay.find(".content-wrap"));
			var contentInfo = jQuery(overlay.find(".info"));
			var borderWidth = jQuery(contentInfo).outerWidth(false) - jQuery(contentInfo).innerWidth();
			borderWidth = 0;
			
			var playerDiv = jQuery(contentWrap.find(".playerDiv"));
			contentInfo.show();
			
			if(eu.europeana.lightbox.playMedia){
				var contentImage = jQuery(contentWrap.find(".playerDiv"));
				contentWrap.height(playerDiv.outerHeight(true) + "px");
				
				contentInfo.css("width",		contentWrap.outerWidth(true) + "px");
				if(showMeta == "expanded"){
					contentInfo.css("height",		"175px");					
				}
				else{
					contentInfo.css("height",		"80px");					
				}
			}
			else{
				// cancel any previous zoom
				
				eu.europeana.lightbox.closeZoom();
				var contentImage = jQuery(contentWrap.find(".content-image"));
				var stats = eu.europeana.lightbox.calculateMaxImgSize("none", showMeta);
				
console.log("layout.... " + stats.w + " x " + stats.h);				
				
				overlay.css("height",	stats.h + "px");
				overlay.css("width",	(stats.w - borderWidth) + "px");

				contentImage.css("width",	(stats.w - borderWidth) + "px");
				contentImage.css("height",	stats.h + "px");

				contentWrap.css("width", 		(stats.w - borderWidth) + "px");
				contentWrap.css("height",		stats.h + "px");

				var closeBtn	= overlay.find(".close");
				var navNext		= contentWrap.find('#nav-next');
				var navPrev		= contentWrap.find('#nav-prev');

				if(!closeBtn.data("originalCss")){					
					closeBtn.data("originalCss", {"right":closeBtn.css("right")} );
				}
				if(stats.o == "portrait"){
					
					// reposition close button
					
					closeBtn.css("right", 0 - (stats.info.w + borderWidth + 20) + "px");
					
					contentInfo.css("position", 	"absolute");
					contentInfo.css("top",			"-1px");
					contentInfo.css("left",			stats.w + borderWidth  + "px");
					contentInfo.css("width",		stats.info.w + "px");
					
					if(stats.info.h > stats.h){ // this is the case for really small images
						contentInfo.css("height",		stats.info.h + "px");
						contentInfo.css("minHeight",	stats.info.h + "px");
						contentInfo.css("maxHeight",	stats.info.h + "px");
						contentWrap.css("height",		stats.info.h + "px");
					}
					else{
						contentInfo.css("height",		stats.h + "px");
						contentInfo.css("minHeight",	stats.h + "px");
						contentInfo.css("maxHeight",	stats.h + "px");						
					}
				

					// Unpin "original context" from bottom of info panel - display it normally - in line with other metadata.
					contentInfo.find('.original-context').css('position', 'static');
					contentInfo.find('.original-context').css('bottom', 'auto');
				}
				else if(stats.o == "landscape"){
					
					// reposition close button
					
					closeBtn.css("right",	closeBtn.data("originalCss").right);
					contentInfo.css("width",		(stats.w + borderWidth) + "px");
					contentInfo.css("height",		stats.info.h + "px");
					contentInfo.css("minHeight",	stats.info.h + "px");
					contentInfo.css("maxHeight",	stats.info.h + "px");

					contentInfo.css("position", 	"relative");
					contentInfo.css("top",			"0px");
					contentInfo.css("left",			"-1px");
					
					
					//alert("landscape")	
//				contentInfo.find('#nav-next').css('top', );
	//			contentInfo.find('#nav-prev').css('top', );

					// pin "original context" to bottom of info panel - there's room for it there and it looks better
					contentInfo.find('.original-context').css('position', 'absolute');
					contentInfo.find('.original-context').css('bottom', '25px');
				}

				// set metadata visibility and view-control options
				if(stats.showMeta == "expanded"){					
					jQuery(".content-wrap .info-open").show();
				}
				else{
					jQuery(".content-wrap .info-open").hide();
				}
				
				// if collapsed/expanded metadata occupies the exact same space them show the metadata and remove the ability to toggle its display 
				if(stats.metaSameSize){
					eu.europeana.lightbox.hideMetadataCtrls();
					jQuery(".content-wrap .info-open").show();
				}
				else{
					eu.europeana.lightbox.showMetadataCtrls();
				}
				
				// add zoom functionality				
				if(stats.shrink){
					if(jQuery('#zoomBtn').length == 0){
						contentWrap.append('<div id="zoomBtn"></div>');
						jQuery('#zoomBtn').css('position',	'absolute');
						jQuery('#zoomBtn').css('top',		'5px');
						jQuery('#zoomBtn').css('right',		'5px');
						jQuery('#zoomBtn').click(function(){eu.europeana.lightbox.zoomImg();});
					}
				}
				
				// centre the lightbox 
				overlay.css("left",	stats.left	+ "px" );
				overlay.css("top",	stats.top	+ "px" );
				
				navNext.css('top', (stats.h - navNext.height()) / 2 + "px");
				navPrev.css('top', (stats.h - navNext.height()) / 2 + "px");

				if(!eu.europeana.lightbox.navOb.wired){
					navNext.click(function(){
						eu.europeana.lightbox.navOb.next();
					});
					navPrev.click(function(){
						eu.europeana.lightbox.navOb.prev();
					});
					eu.europeana.lightbox.navOb.wired = true;
				}
				
				
			}

		},
		
		getDimensionsFromStyleAttr:function (style){
			var attributes = style.split(";");
			var res = {w:0, h:0};
			jQuery(attributes).each(function(index, attr){
				if(attr.toLowerCase().indexOf("width") > -1){
					res.w = parseInt(attr.split(":")[1]);
				}
				if(attr.toLowerCase().indexOf("height") > -1){
					res.h = parseInt(attr.split(":")[1]);
				}
			});
			return res;
		},
		
		getImgDimensionsFromSrc: function(src) { /* returns the original image dimensions */
			var res = {w:0,h:0};
			var t = new Image();
			t.src = src;
			res.w = t.width;
			res.h = t.height;
			t = null;
			
			
			if(res.w == 0){
				res.w = jQuery("img.content-image").width();
				res.h = jQuery("img.content-image").height();				
			}
			if(res.w == 0){
				res.w = document.getElementById("lightbox_image").width;
				res.h = document.getElementById("lightbox_image").height;
			}
			/*
			if(res.w == 0 || true){
				var defImgDisplay = jQuery("#lightbox_image").css("display");
				var defImgVis     = jQuery("#lightbox_image").css("visibility");
				var defLBDisplay  = jQuery("#lightbox").css("display");
				var defLBVis      = jQuery("#lightbox").css("visibility");
				
				jQuery("#lightbox").css("display", "block");
				jQuery("#lightbox_image").css("display", "block");
				jQuery("#lightbox").css("visibility", "hidden");
				jQuery("#lightbox_image").css("visibility", "hidden");
				
				res.w = document.getElementById("lightbox_image").width;
				res.h = document.getElementById("lightbox_image").height;
				
				// reshow
				jQuery("#lightbox_image").css("display", defImgDisplay);
				jQuery("#lightbox_image").css("visibility", defImgVis);
				jQuery("#lightbox").css("display", defLBDisplay);
				jQuery("#lightbox").css("visibility", defLBVis);
			}
			*/
			
			return res;
		},

		getMp4AviPlayer: function (videoUrl, playerW, playerH, controls){
			var videoHTML = ''
			+ '<object width="' + playerW + '" height="' + playerH + '" id="qtPlayer" style="position:absolute;"'
			+	'classid="clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B"'
			+	'codebase="http://www.apple.com/qtactivex/qtplugin.cab">'
			+	'<param name="src" value="' + videoUrl + '">'
			+	'<param name="controller" value="' + (controls ? 'true' : 'false') + '">'
			+	'<param name="autoplay" value="true">'
			+	'<param name="enablejavascript" value="true">'
			+		'<embed src="' + videoUrl + '" width="' + playerW + '" height="' + playerH + '"  style="position:static;"'
			+			'autoplay="true" controller="' + (controls ? 'true' : 'false') + '"'
			+			'pluginspage="http://www.apple.com/quicktime/download/">'
			+		'</embed>'
			+	'</object>';

			return videoHTML;
		},
		
		getJwPlayer:function(videoUrl, playerW, playerH, movieType){
			var videoHTML = ''
			+ '<video width="' + playerW + '" height="' + playerH + '" id="jwPlayer" poster="/portal/branding/portal2/images/logos/media_poster.png" autoplay>'
			+   '<source src="' + videoUrl + '" type="video/' + movieType + '">'
			+ '</video>';
			
			return videoHTML;
		},
		
		getWmvPlayer: function (videoUrl, playerW, playerH, controls){
			var videoHTML = ''
				+ '<object id="mediaPlayer" '
				+ 'classid="CLSID:6BF52A52-394A-11d3-B153-00C04F79FAA6"'
				+ 'standby="Loading Microsoft Windows Media Player components..." ';			
			if(navigator.userAgent.indexOf("MSIE")>-1){
				videoHTML += ' type="application/x-oleobject">';
			}
			else{
				videoHTML += ' type="application/x-ms-wmp">';				
			}
			videoHTML += ''
				
				+ '<PARAM NAME="URL" VALUE="' + videoUrl + '"> '
				+ '<PARAM NAME="SendPlayStateChangeEvents" VALUE="True"> '
				+ '<PARAM NAME="AutoStart" VALUE="True"> '
				+ '<PARAM name="uiMode" value="none"> '
				+ '<PARAM name="PlayCount" value="9999"> '
				+ '<param name="showControls" value="' + (controls ? "true" : "false") + '">'
			//	+ '<param name="wmode"			value="transparent"/>'
			
				//+ '<PARAM NAME="DisplayBackColor" VALUE="True">'
				//+ '<PARAM NAME="DisplayForeColor" VALUE="16777215">'
				//+ '<PARAM NAME="AutoSize" VALUE="True">' 
				//+ '<param name="StretchToFit" value="true">'
/*				
				+ '<param name="fileName" value="' + videoUrl + '">'
				+ '<param name="animationatStart" value="true">'
				+ '<param name="transparentatStart" value="true">'
				+ '<param name="autoStart" value="true">'
				
				+ '<param name="uiMode" value="none">'
				
				+ '<param name="loop" value="false">'
				
			    + '<param name="windowlessvideo" value="true">'
				
				+ '<param name="AutoSize" value="false">'
*/
				+ '<embed type="application/x-mplayer2" '
				+ 	'pluginspage="http://microsoft.com/windows/mediaplayer/en/download/" '
				+	'wmode="transparent" '
				+ 	'id="mediaPlayer-embed" '
				+ 	'name="mediaPlayer-embed" '
				+	'stretchToFit="1" '
				+	'allowscriptaccess="always" '
				+	'displaySize="0" '
				+ 	'bgcolor="darkblue" '
				+ 	'showcontrols="' + (controls ? "true" : "false") + '" '
				+ 	'showtracker="-1" '
				+ 	'showdisplay="0" showstatusbar="-1" videoborder3d="-1" '
				+ 	'width="' + playerW + '" '
				+ 	'height="' + playerH + '" '
				//+   'AutoSize=1 '
				+ 	'src="' + videoUrl + '" '
				+ 	'autostart="true" '
				+ 	'designtimesp="5311" '
				+ 	'loop="false">'
				+ 	'</embed>'
				+ '</object>';
			return videoHTML;
		}
		
};

//eu.europeana.lightbox.init();
