
log = function(msg){
	console.log(msg);
}

var EuCarousel = function(cmp, data){
	var position = 1;
	var left, right, items;
	var cmp = $(cmp);
	var minSpacingPx = 20;
	var spacing = minSpacingPx;
	var inView = 0;
	var total = data.length;
	var animating = false;
	
	var anchor = function(){
		animating = true;
		cmp.css('overflow-x', 'auto');
		items.css('left', '0');
		cmp.scrollTo( items.find('.carousel-item:nth-child(' + position + ')'), inView == 1 ? 0 : 1000, {"axis":"x",  "onAfter":function(){
			
			var done = function(){
				cmp.css('overflow-x', 'hidden');
				animating = false;
				setArrowState();				
			};
			
			if(inView == 1){
				var margin = items.find('.carousel-item:first').css('margin-left');
				items.css('left', spacing + 'px');
			}
//			else{
	//			items.css('left', '0');
		//	}
			
			done();
			
		} } );
	};
	

	var resize = function(){
		
		log('resizing');
		
		var w    = cmp.width();
		var itemW =  items.find('.carousel-item').first().outerWidth();
		var maxFit =  parseInt(w / (itemW + minSpacingPx));
		spacing = minSpacingPx;
		
		if(maxFit == 1){
			spacing = (w - itemW) / 2;
		}
		else{				
			spacing = ( w - (maxFit * itemW) ) / (maxFit - 1);
		}
		spacing = parseInt(spacing);
	
		inView = maxFit;
		
		items.find('.carousel-item').css('margin-left', parseInt(spacing) + 'px');
				
		log('w: ' + w + ', itemW: ' + itemW + ', maxFit ' + maxFit);
		if(maxFit != 11111){
			items.find('.carousel-item:first').css('margin-left', '0px');
		}
		items.css('width', w + (total * (itemW + spacing) ) );
		anchor();
	};
	
	var setArrowState = function(){
		if(position == 1){
			left.hide();
		}
		else{
			left.show();				
		}
		if(position + inView <= total){
			right.show();
		}
		else{
			right.hide();				
		}
	}
	
	var goLeft = function(){
		animating = true;
		var prevItem = position - inView < 1 ? 1 : position - inView;
		log('prev index = ' + prevItem);
		
		position = prevItem;
		
		prevItem = items.find('.carousel-item:nth-child(' + prevItem + ')');

		cmp.css('overflow-x', 'auto');
		items.css('left', '0');

		cmp.scrollTo( prevItem, inView == 1 ? 0 : 1000, {"axis":"x",  "onAfter":function(){
			
			var done = function(){
				cmp.css('overflow-x', 'hidden');
				animating = false;
				setArrowState();				
			};

			if(inView == 1){
				var margin = items.find('.carousel-item:first').css('margin-left');
				items.css('left', spacing + 'px');
			}
			
			done();
			
		} } );

	};
	
	var goRight = function(){
		animating = true;
		var nextItem = position + inView;
		
		position = nextItem;

		log('next index = ' + nextItem);
		nextItem = items.find('.carousel-item:nth-child(' + nextItem + ')');
		
		cmp.css('overflow-x', 'auto');

		items.css('left', '0');
		cmp.scrollTo( nextItem, inView == 1 ? 0 : 1000, {"axis":"x", "onAfter":function(){
			
			var done = function(){
				cmp.css('overflow-x', 'hidden');
				animating = false;
				setArrowState();				
			};
			
			if(inView == 1){
				var margin = items.find('.carousel-item:first').css('margin-left');
				items.css('left', spacing + 'px');
			}

			done();

		}} );
		
	};

	
	
	var init = function(){

		left  = $('<div class="carousel-left icon-arrow-4"></div>').appendTo(cmp);
		items = $('<div class="carousel-items">').appendTo(cmp);
		right = $('<div class="carousel-right icon-arrow-2"></div>').appendTo(cmp);
		
		$.each(data, function(i, ob){
			items.append('<span class="carousel-item" href="' + ob.link + '" target="' + (ob.linkTarget) + '"><img src="' + ob.thumb + '"/><span class="info">' + ob.title + '</span></span>');
		});
		
		$('.carousel-item .info').each(function(i, ob){
			new Ellipsis(ob);
		});
		
		cmp.css('overflow-y', 'hidden');

		/*
		 * 
		 * TODO - we're not using this so remove the dependency
		 * 
		cmp.imagesLoaded(
			function($images, $proper, $broken) {
				$images.each(function(i, ob){
					var top = (($(ob).parent().height() - 32) - $(ob).height() ) /2;
					
					$(ob).css('top', top + 'px');
				});
			}
		);
		*/

		$('.carousel-item').click(function(){
			var item = $(this);
			var href = item.attr('href');
			var trgt = item.attr('target');
			if(trgt == '_self'){
				window.location.href = href;
			}
			else{
				window.open(href);
			}
		});
		
		
		left.click(function(){
			if(!animating){
				log('go left....');				
				goLeft();					
			}
		});

		right.click(function(){
			if(!animating){
				log('go right....');
				goRight();
			}
		});


		if(! $("html").hasClass("ie8")){
			cmp.swipe({
				swipeStatus:function(event, phase, direction, distance, fingerCount) {

					var x = cmp.scrollLeft();
					var threshold = 50;
					log('swiping ' + event + ', ' + phase + ', ' + direction + ', ' + distance + ', ' + fingerCount  + '      >>> '  +  x  );

					if(phase=="start"){
						cmp.css('overflow-x', 'auto');
					}

					if(phase=="move"){
						
						if(direction == 'left'){
							log('left');
							
							//cmp.scrollLeft(x+1);
							
							if(distance > threshold){
								right.click();
							}
						}
						if(direction == 'right'){
							
							//cmp.scrollLeft(x-1);

							log('right')
							if(distance > threshold){
								left.click();
							}
						}
						
					}

					if(phase=="end" || phase=="cancel"){
						cmp.css('overflow-x', 'hidden');
						anchor();
					}
					
						/*
					if(phase=="end"){
						if(direction == "left"){
							self.navOb.prev();
						}
						else if(direction == "right"){
							self.navOb.next();
						}
					}
					*/
				},
				triggerOnTouchEnd:false,
				threshold:100
				}
			);
		}


		$(window).euRsz(function(){
			resize();
		});
		resize();

	};
	
	init();
	return {
		resize : function(){
			resize();
		},
		inView : function(){
			return fnInView();
		},
		goLeft : function(){
			goLeft();
		},
		goRight : function(){
			goRight();
		}
	}
};

