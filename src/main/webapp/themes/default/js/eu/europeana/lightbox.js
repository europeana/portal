js.utils.registerNamespace( 'eu.europeana.lightbox' );

eu.europeana.lightbox = function(){
	
	var self		= this;
	self.cmp		= null;
	self.origImgW	= null;
	self.origImgH	= null;
	self.infoW		= 250;
	self.infoH		= 30;
	self.infoHx		= 30;
	self.zoomed		= false;
	self.brdr		= 60;
	
	var init = function(initOb) {
		
		var cmp				= self.cmp	= initOb.cmp;
		var src				= initOb.src;
		var carouselData	= initOb.data;
		
		if(carouselData){
			
			// create inner navigation object
			
			var NavOb = function(){
				var self = this;
				var currentUrl = $("#lightbox_image").attr("src");
				var submodel = [];
				var submodelActive = 0;

				var nav = function(direction){

					for(var i=0; i<carouselData.length; i++){
						if(carouselData[i].external && carouselData[i].external.type == "image"){
							if(carouselData[i].external.url == currentUrl){
								submodelActive = i;
							}
							submodel[submodel.length] = carouselData[i].external;
						}
					}

					var newActive		= submodelActive + direction;
					if(newActive<0){
						newActive = submodel.length -1;
					}
					else if(newActive >= submodel.length){
						newActive = 0;						
					}
					submodelActive = newActive;

					$("#hidden_img").unbind( '.imagesLoaded' );
					$("#hidden_img").remove();
					
	
					$('<div id="hidden_img" style="visibility:hidden;"><img src="' + submodel[newActive].url + '" /></div>').appendTo('#lightbox').imagesLoaded(
						function(){
							$('#hidden_img').remove();
							com.google.analytics.europeanaEventTrack("Europeana Portal", "Europeana Lightbox", "External (lightbox nav)");
							eu.europeana.fulldoc.lightboxOb.switchImg(submodel[newActive].url);
						}
					);
				};

				return {
					"prev" : function(){
						nav(-1);
					},
					"next" : function(){
						nav(1);
					}
				};
			};
			self.navOb = new NavOb();
		}
		
		cmp.find('#lightbox_image').attr('src', src);
		
		self.origImgW = initOb.w;
		self.origImgH = initOb.h;
	};

	var layout = function(){
		
		/*	Strategy:
		 * 
		 * 	1) get layout by taking 2 hypothetical aspect ratios:
		 * 		- info underneath
		 * 		- info on the right
		 * 	and compare with window aspect ratio (the closest will show the image at its largest).
		 * 
		 * 	2) recursively reduce img dimensions until img + info fit available space
		 * 
		 */ 

		
		var	img		= self.cmp.find('#lightbox_image'),
			info	= self.cmp.find('#lightbox_info');
		
		var	imgW	= self.origImgW,
			imgH	= self.origImgH,
			infoW	= self.infoW,
			infoH	= self.infoH,
			infoHx	= self.infoHx;
			brdr	= self.brdr;
		
		js.console.log("self.origImgW " + self.origImgW);
			
		var	aspectWin	= ($(window).width()-brdr) / ($(window).height()-brdr);
		var	aspectUnder	= imgW / (imgH + infoH);
		var	aspectRight	= (imgW + infoW) / imgH;
		var	aspectImg	= imgW / imgH;
		var	aspectZoom	= imgW / imgH;
		
		var aspectClosest	= null;
		$.each([aspectUnder, aspectRight], function(){
		  if (aspectClosest == null || Math.abs(this - aspectWin) < Math.abs(aspectClosest - aspectWin)) {
			  aspectClosest = this;
		  }
		});
		
		var isRight			= aspectClosest == aspectRight;

		var availableHeight =	parseInt(
			$(window).height()
			- ( self.zoomed ? 0 : (isRight ? 0 : infoH) )
		) - brdr;

		var availableWidth =	parseInt(
			$(window).width()
			- ( self.zoomed ? 0 : (isRight ? infoW : 0) )
		) - brdr;
		
		//js.console.log("available = " + availableWidth + " x " + availableHeight + "  (window " + $(window).width() + " x " + $(window).height() + ")" );
		
		var testDimensions = function(w, h, rec){
			var projectedHeight	= (w/aspectImg);
			var projectedWidth	= (h*aspectImg);

			js.console.log('testing w/h  ' + projectedWidth + ' / ' + projectedHeight);
			
			if(rec > 30){
				js.console.log('avoid infinite recursion');
				return [projectedWidth, projectedHeight];
			}
			if(projectedHeight > availableHeight || projectedWidth > availableWidth){
				return testDimensions(	w*0.9, h*0.9, rec+1);
			}
			else{
				return [projectedWidth, projectedHeight];
			}
		};
		
		var dim = testDimensions(imgW, imgH, 0);
		
		// set img
		
		img.css('width',		dim[0] + "px");
		img.css('min-width',	dim[0] + "px");	/*	additional rule needed for ie8	*/
		$("#lightbox").css("visibility", "visible");
		
		// set meta
		
		$(".hide_show_meta").hide();

		if(self.zoomed){
			info.css('display', 'none');
		}
		else{
			if(isRight){
				info.css('display', 'inline-block');
				info.css('width',	infoW + 'px');
				info.css('height',	img.height() + 'px');
			}
			else{
				info.css('display',	'block');
				info.css('width',	dim[0] + 'px');
				info.css('height',	infoH + 'px');
			}	
		}
		
		// align zoom & nav
		
		if(self.navOb){
			self.cmp.find('#nav-next').css('right', self.zoomed ? 0 : isRight ? self.infoW + 'px' : '0');
		}
		
		var zoom = self.cmp.find('#zoomIn');
		if(img.width() < self.origImgW){
			zoom.show();			
			zoom.css('right', self.zoomed ? 0 : isRight ? self.infoW + 'px' : '0');			
		}
		else if(!self.zoomed){
			zoom.hide();
		}
		
		
		// meta show / hide
		
		if(!self.zoomed){
			var both	= self.cmp.find('.hide_show_meta');
			
			var showBoth = function(){
				both.show();
				self.cmp.find('ul').addClass('collapsed');				
			};

			var hideBoth = function(){
				both.hide();
				self.cmp.find('ul').removeClass('collapsed');				
			};
			
			
			if(isRight){
				hideBoth();
				info.css('overflow-y', 'auto');
			}
			else{
				showBoth();
				
				var show	= self.cmp.find('.showMeta');
				var hide	= self.cmp.find('.hideMeta');

				show.hide();
				hide.hide();

				// if info is hidden...
				
				if( info.find('ul')[0].offsetHeight > info.height()+3 ){/* chrome +3 for border */
					
					var showShow = function(){
						show.show();
						hide.hide();
						info.css('overflow-y', 'hidden');
					};
					
					var showHide = function(){
						hide.show();
						show.hide();
						info.css('overflow-y', 'auto');
					};
					
					info.height() > self.infoH ? showHide() : showShow();

					hide.unbind('click').click(function(){
						info.css('height', self.infoH + 'px');
						showShow();
					});

					var em = $('<div class="test" style="height:1em;">&nbsp;</div>').appendTo(info);
					em = em.height();
					info.find('.test').remove();
					
					// get natural height of list
					
					var list = info.find('ul'); 
					list.css('height', 'auto');
					
					var availHeight		= $(window).height() - (img.height() + brdr); 
					var naturalHeight	= list.height() + (2 * em);

					list.css('height', '100%');

	// BUG: when info under, expanded, scrolled then page resized (narrower), list is still "scrolled"
					
	//var ref = info.css('overflow-y');
	//info.css('overflow-y', 'auto');
	//info.scrollTop();
	//info.css('overflow-y', ref);
	//alert( info.css('overflow-y') );
					
					if(availHeight >= naturalHeight){
						info.css('height', naturalHeight + 1);  // add the one to stop a .2 pixel height variation causing a scrollbar to appear 
						hideBoth();
					}
					
					show.unbind('click').click(function(){
						info.css('height',	availHeight + em + 'px');
						showHide();
					});
				}
			}			
		}
	};
	
	var showLightbox = function(callback){
		layout();
		
		/*
		self.cmp.find('#zoomIn').unbind().click(function(){
			self.cmp.find('#zoomIn').toggleClass('active');
			self.zoomed = !self.zoomed;
			layout();
		});
		*/
		
		if(callback){
			callback();
		}
	};

	
	return {
		"init" : function(cmp, src, navOb){
			init(cmp, src, navOb);
		},
		
		"layout" : function(){
			layout();
		},
		
		"showLightbox" : function(callback){
			showLightbox(callback);
			
			if(self.navOb){
				
				var navNext		= self.cmp.find('#nav-next');
				var navPrev		= self.cmp.find('#nav-prev');
				
				navNext.add(navPrev).css('display', 'inline-block');
				self.cmp.unbind('swipe');
				
				if(! $("html").hasClass("ie8")){
					self.cmp.swipe({
						swipeStatus:function(event, phase, direction, distance, fingerCount) {
							if(phase=="end"){
								if(direction == "left"){
									self.navOb.next();
								}
								else if(direction == "right"){
									self.navOb.prev();
								}
							}
						},
						triggerOnTouchEnd:false,
						threshold:100
						}
					);
				}
				
			}
		},
		
		"switchImg" : function(url){


			/*
			$('<img src="' + url + '" style="visibility:hidden;"/>').appendTo("body").imagesLoaded(function($images, $proper, $broken){
				
				if($proper.length==1 ){
					alert("loaded ")
					
					self.origImgW = $proper.width();
					self.origImgW = $proper.height();

					
				}
			});
			*/
			
			
			
			
//			var img = self.cmp.find("#lightbox_image");
//			img.attr("style", "");
//			img.attr("src", url);
//			layout();
			
	
			
			//alert("switchImg, self.cmp.find('#lightbox_image').length = " + self.cmp.find('#lightbox_image').length   );

			var img = self.cmp.find('#lightbox_image');
			img.unbind( '.imagesLoaded' );

			//alert("data = " +   img.data['imagesLoaded']  );
			//alert("switch src.... ");
			//self.cmp.find("#lightbox_image").attr("src", url);
			
			$("#lightbox").css("visibility", "hidden");
			
			img.attr("style", "");// "visibility:hidden"); // clear old w / h and hide
			img.attr("src", url);

			
			// MACLEAN
			img.imagesLoaded(

					function($images, $proper, $broken){

						if($proper.length==1 && $proper.width() > 2){

				//			alert("set self.origImgW, was " + self.origImgW + ", will be " + $proper.width());
							
							self.origImgW = $proper.width();
							self.origImgH = $proper.height();
	
	
							img.unbind( '.imagesLoaded' );
							//alert("call to layout..." + layout);
							layout();
						}
					}

			);
			
			
			
		},
		
		"getCmp" : function(callback){
			return self.cmp;
		},
		
		"next" : function(){
			self.navOb.next();
		},
		
		"prev" : function(){
			self.navOb.prev();
		},
		
		"zoom" : function(){
			self.cmp.find('#zoomIn').toggleClass('active');
			self.zoomed = !self.zoomed;
			layout();
		}
		
	};
};
