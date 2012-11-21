js.utils.registerNamespace( 'eu.europeana.lightbox' );

eu.europeana.lightbox = {
	playMedia:false,
	
	overlay:null,
	
	carouselData: null,
	
	showMetadata:function(){
		$(".content-wrap .info-open").show();
		eu.europeana.lightbox.metaDataToggled();				
	},

	hideMetadata:function(){
		$(".content-wrap .info-open").hide();
		eu.europeana.lightbox.metaDataToggled();
	},

	hideMetadataCtrls:function(){
		$(".hide_show_meta").hide();
	},
	
	showMetadataCtrls:function(){
		if(jQuery(".content-wrap .info-open").is(":visible")){
			$(".hideMeta").show();
			$(".showMeta").hide();
		}
		else{
			$(".showMeta").show();
			$(".hideMeta").hide();
		}
		$(".hide_show_meta").show();
	},

	metaDataToggled:function(){
		if($(".content-wrap .info-open").is(":visible")){
			$(".hideMeta").show();
			$(".showMeta").hide();				
		}
		else{
			$(".hideMeta").hide();
			$(".showMeta").show();				
		}
		eu.europeana.lightbox.layout( $(".content-wrap .info-open").is(":visible") ? "expanded" : "contracted" );
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
	    	var landscapeContracted = null, portraitContracted = null, landscapeExpanded = null, portraitExpanded = null;
	    	
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
    		
	    	$(allStats).each(function(index, ob){
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

			var contentWrap		= $(overlay.find(".content-wrap"));
			var contentImage	= $(contentWrap.find(".content-image"));

			var screenW			= $(window).width();
			var screenH			= $(window).height();
			
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
		var img		= $(".content-image");
		var html	= '<div class="zoomedImgDiv"><img id="zoomedImg" src="' + img.attr("src") + '" style="width:' + stats.w + 'px; height:' + stats.h + 'px;">' 
						+ (eu.europeana.vars.lightbox_rights ? eu.europeana.vars.lightbox_rights : '');
						+ '</div>';

		var zoomedImgDiv = $(html).appendTo("body");
		
		zoomedImgDiv.css("top",			stats.top	+ "px");
		zoomedImgDiv.css("left",		stats.left	+ "px");
		zoomedImgDiv.css("display",		"block");
		zoomedImgDiv.css("position",	"fixed");
		zoomedImgDiv.css("zIndex",		"9999");
		
		var contentWrap	= $(eu.europeana.lightbox.overlay.find(".content-wrap"));
		
		if(eu.europeana.lightbox.navOb){
			var navNext		= contentWrap.find('#nav-next');
			var navPrev		= contentWrap.find('#nav-prev');
			
			$('#nav-next, #nav-prev').css('display', 'block');
			
			zoomedImgDiv.append(navNext);
			zoomedImgDiv.append(navPrev);

			navNext.css('top', (stats.h - navNext.height()) / 2 + "px");
			navPrev.css('top', (stats.h - navNext.height()) / 2 + "px");

			navNext.show();
			navPrev.show();
		}
	
		html = '<div id="zoomClose"></div>';
		$(".zoomedImgDiv").append(html);
		$('#zoomClose').click(function(){
			eu.europeana.lightbox.closeZoom();
			eu.europeana.lightbox.overlay.find(".close").click();
		});
		
		html = '<div id="enlittle"></div>';
		$(".zoomedImgDiv").append(html);
		
		$('#enlittle').click(function(){
			eu.europeana.lightbox.closeZoom();
			eu.europeana.lightbox.layout();
		});
		
		$(".zoomedImgDiv a").css("position", "absolute");
		$(".zoomedImgDiv a").css("display", "block");
		$(".zoomedImgDiv a").css("bottom", "4px");
		$(".zoomedImgDiv a").css("right", "4px");

		eu.europeana.lightbox.overlay.hide();
	},

	closeZoom:function(){
//		var contentWrap	= $(eu.europeana.lightbox.overlay.find(".content-wrap"));

	//	contentWrap.append($('#nav-next'));
		//contentWrap.append($('#nav-prev'));	
		
		eu.europeana.lightbox.removeZoomedImgDiv();
		if(eu.europeana.lightbox.overlay){
			eu.europeana.lightbox.overlay.show();
		}
	},
	
	init:function(src, navOb) {
		
		eu.europeana.lightbox.navOb = navOb;

		if(navOb){
			$('#nav-next, #nav-prev').css('display', 'block');
		}

		$(".lb-trigger span").overlay({
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
				var contentWrap		= $(overlay.find(".content-wrap"));
				var contentImage	= $(contentWrap.find(".content-image"));

				if(src && typeof src === 'string'){
					contentImage.show();
				}
			},
			onLoad:function(){
				eu.europeana.lightbox.layout();
			},
			onBeforeClose: function(){
				var closeBtn = eu.europeana.lightbox.overlay.find(".close");
				closeBtn.css("display", "none");
				if(typeof closeBtn.data("originalCss") != 'undefined'){
					closeBtn.css("right",	closeBtn.data("originalCss").right);
				}
				closeBtn.css("display", "block");
				
				eu.europeana.lightbox.removeZoomedImgDiv();

			}
		});
		
		$(".showMeta").click(function(){
			eu.europeana.lightbox.showMetadata();
		});
		
		$(".hideMeta").click(function(){
			eu.europeana.lightbox.hideMetadata();
		});
		
		
		$(".info-open .item-metadata:first").focus();
	},

	removeZoomedImgDiv:function(){
		
		var contentWrap	= $(eu.europeana.lightbox.overlay.find(".content-wrap"));
		contentWrap.append($('#nav-next'));
		contentWrap.append($('#nav-prev'));	
		$(".zoomedImgDiv").remove();

	},
	
	layout:function(showMeta){
		
		var overlay = eu.europeana.lightbox.overlay;
		
		if(!overlay){
			return;
		}
		if(!overlay.is(':visible')){
			if(  $(".zoomedImgDiv")[0]   ){
				eu.europeana.lightbox.removeZoomedImgDiv();
				eu.europeana.lightbox.zoomImg();				
			}
			return;
		}
		
		if(!eu.europeana.lightbox.tracked){
			eu.europeana.lightbox.tracked = true;
			com.google.analytics.europeanaEventTrack("Lightbox View");			
		}
		
		var contentWrap = $(overlay.find(".content-wrap"));
		var contentInfo = $(overlay.find(".info"));
		var borderWidth = $(contentInfo).outerWidth(false) - $(contentInfo).innerWidth();
		borderWidth = 0;
		
		contentInfo.show();
		
		// cancel any previous zoom
		eu.europeana.lightbox.closeZoom();

		var contentImage = $(contentWrap.find(".content-image"));
		var stats = eu.europeana.lightbox.calculateMaxImgSize("none", showMeta);
		
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
			
			// pin "original context" to bottom of info panel - there's room for it there and it looks better
			contentInfo.find('.original-context').css('position', 'absolute');
			contentInfo.find('.original-context').css('bottom', '25px');
		}

		// set metadata visibility and view-control options
		if(stats.showMeta == "expanded"){					
			$(".content-wrap .info-open").show();
		}
		else{
			$(".content-wrap .info-open").hide();
		}
		
		// if collapsed/expanded metadata occupies the exact same space them show the metadata and remove the ability to toggle its display 
		if(stats.metaSameSize){
			eu.europeana.lightbox.hideMetadataCtrls();
			$(".content-wrap .info-open").show();
		}
		else{
			eu.europeana.lightbox.showMetadataCtrls();
		}
		
		// add zoom functionality				
		if(stats.shrink){
			if($('#zoomBtn').length == 0){
				contentWrap.append('<div id="zoomBtn"></div>');
				$('#zoomBtn').css('position',	'absolute');
				$('#zoomBtn').css('top',		'5px');
				$('#zoomBtn').css('right',		'5px');
				$('#zoomBtn').click(function(){eu.europeana.lightbox.zoomImg();});
			}
		}
		
		// centre the lightbox 
		overlay.css("left",	stats.left	+ "px" );
		overlay.css("top",	stats.top	+ "px" );
		
		navNext.css('top', (stats.h - navNext.height()) / 2 + "px");
		navPrev.css('top', (stats.h - navNext.height()) / 2 + "px");

		if(eu.europeana.lightbox.navOb && !eu.europeana.lightbox.navOb.wired){
			navNext.click(function(){
				eu.europeana.lightbox.navOb.next();
			});
			navPrev.click(function(){
				eu.europeana.lightbox.navOb.prev();
			});
			eu.europeana.lightbox.navOb.wired = true;
		}
	},
	
	getDimensionsFromStyleAttr:function (style){
		var attributes = style.split(";");
		var res = {w:0, h:0};
		$(attributes).each(function(index, attr){
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
			res.w = $("img.content-image").width();
			res.h = $("img.content-image").height();				
		}
		if(res.w == 0){
			res.w = document.getElementById("lightbox_image").width;
			res.h = document.getElementById("lightbox_image").height;
		}
		return res;
	}
};
