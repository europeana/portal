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
	
	var init = function(cmp, src, carouselData) {
		self.cmp = cmp;
		
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
					
					$('<div id="hidden_img" style="visibility:hidden;"><img src="' + submodel[newActive].url + '" /></div>').appendTo('#lightbox_image').imagesLoaded(
						function(){
							var zoomed = false;
							if($("#zoomedImg").is(":visible")){
								eu.europeana.lightbox.closeZoom();
								zoomed = true;
							}		
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
		self.origImgW = cmp.find('#lightbox_image').width();
		self.origImgH = cmp.find('#lightbox_image').height();
	};

	var layoutNew = function(showMeta){
		
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
			infoH	= showMeta ? showMeta : self.infoH,
			infoHx	= self.infoHx;
			brdr	= self.brdr;
		
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

			//js.console.log("projected " + projectedWidth + " / " + projectedHeight +  "   available = " + availableWidth + " x " + availableHeight );

			if(rec > 30){
				alert("recurse " + rec);
			}
			if(projectedHeight > availableHeight || projectedWidth > availableWidth){
				return testDimensions(	w*0.9, h*0.9, rec+1);
			}
			else{
				return [projectedWidth, projectedHeight]
			}
		};
		
		var dim = testDimensions(imgW, imgH, 0);
		
		// set img
		
		img.css('width',	dim[0]);
		
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
		layoutNew();
		
		self.cmp.find('#zoomIn').unbind().click(function(){
			self.cmp.find('#zoomIn').toggleClass('active');
			  
			self.zoomed = !self.zoomed;
			layoutNew();
		});
		
		if(callback){
			callback();
		}
	};
	
	return {
		"init" : function(cmp, src, navOb){
			init(cmp, src, navOb);
		},
		
		"layoutNew" : function(){
			layoutNew();
		},
		
		"showLightbox" : function(callback){
			showLightbox(callback);

			if(self.navOb){
				$('#nav-next, #nav-prev').css('display', 'block');
				
				var navNext		= self.cmp.find('#nav-next');
				var navPrev		= self.cmp.find('#nav-prev');
				
				navNext.click(function(){
					self.navOb.next();
				});
				
				navPrev.click(function(){
					self.navOb.prev();
				});

				self.cmp.unbind('swipe');
				self.cmp.swipe({
					swipeStatus:function(event, phase, direction, distance, fingerCount) {
						if(phase=="end"){
							if(direction == "left"){
								$('#nav-next').click();
							}
							else if(direction == "right"){
								$('#nav-prev').click();
							}
						}
					},
					triggerOnTouchEnd:false,
					threshold:100
					}
				);
			}
		},
		
		"switchImg" : function(url){
			self.cmp.find("#lightbox_image").attr("src", url);
			layoutNew();
		},
		
		"getCmp" : function(callback){
			return self.cmp;
		},
		
		"next" : function(){
			if(self.navOb){
				self.navOb.next();
			}
		},
		
		"prev" : function(){
			if(self.navOb){
				self.navOb.prev();
			}
		}
		
	};
};
