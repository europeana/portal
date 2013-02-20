
(function() {
	
	window.galleries = [];

	
	window.euResponsiveTriggerRespond = function(){
		for(var i=0; i<window.galleries.length; i++){
			window.galleries[i].respond();
		}
	};
	
	window.euResponsive = function(args) {
		var self = this;
		self.ops= args ? args : {};
		
		if(typeof self.ops.selector == "undefined"){
			self.ops.selector = ".responsive";				/* default class for responsive images unless otherwise specified */
		}
		if(typeof self.ops.initialSuffix == "undefined"){
			self.ops.initialSuffix = "_1";					/* default initial suffix unless otherwise specified */
		}
		if(typeof self.ops.suffixes == "undefined"){
			self.ops.suffixes = ["_1", "_2", "_3", "_4", "_4"];	/* default suffixes unless otherwise specified - extra added for chrome*/

//			self.ops.suffixes = ["_1", "_2", "_3", "_4"];	/* default suffixes unless otherwise specified */
		}
		
		var measureDivClassName = self.ops.galleryName ? self.ops.galleryName : 'euresponsive';
		
		
		if(! $('.' + measureDivClassName)[0] ){
			self.measureDiv					= document.createElement( 'div' );
			self.measureDiv.style.cssText	= 'position:absolute;top:-100em';
			self.measureDiv.className		= measureDivClassName;
			
			document.body.insertBefore(self.measureDiv, document.body.firstChild);
		}
		else{
			self.measureDiv = $('.' + measureDivClassName + ':first')[0];
			self.measureDiv.className		= measureDivClassName;
		}
		
		self.lastSuffix				= self.ops.initialSuffix;
		self.respond = function(){
			var galleryImages = $(self.ops.selector);
			var gallerySuffix = self.ops.suffixes[self.measureDiv.offsetWidth] || self.ops.initialSuffix;
			var changed = false;
			if(gallerySuffix != self.lastSuffix){
				
				for(var i=0; i<galleryImages.length; i++){
					var galleryImage = galleryImages[i];
					var newSrc = $(galleryImage).attr("src").replace(self.lastSuffix + ".", gallerySuffix + ".");
					$(galleryImage).attr('src', newSrc);
					changed = true;
				}
			}

			if(changed){
				self.lastSuffix	= gallerySuffix;			
			}
		};
		self.respond();
		
		if(!self.ops.oneOff){
			window.galleries.push(self);			
		}
		else{
			self.getLastSuffix = function(){
				return self.lastSuffix;
			};
		}
	};
	
	$(window).bind("resize", function(){
		window.euResponsiveTriggerRespond();
	});
	
})();

