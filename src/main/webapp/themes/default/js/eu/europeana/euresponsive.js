
(function() {
	
	var galleries = [];
	var self = null;
	
	window.euResponsiveTriggerRespond = function(){
		for(var i=0; i<galleries.length; i++){
			galleries[i].respond();
		}
	};
	
	window.euResponsive = function(args) {
		self = this;
		self.ops= args ? args : {};
		
		if(typeof self.ops.selector == "undefined"){
			self.ops.selector = ".responsive";				/* default class for responsive images unless otherwise specified */
		}
		if(typeof self.ops.initialSuffix == "undefined"){
			self.ops.initialSuffix = "_1";					/* default initial suffix unless otherwise specified */
		}
		if(typeof self.ops.suffixes == "undefined"){
			self.ops.suffixes = ["_1", "_2", "_3", "_4"];	/* default suffixes unless otherwise specified */
		}
		
		var measureDivClassName = self.ops.galleryName ? self.ops.galleryName : 'euresponsive';
		
		if(! $('.' + measureDivClassName)[0] ){
			
	console.log("eu responsive: create the measure div");
			
			self.measureDiv					= document.createElement( 'div' );
			self.measureDiv.style.cssText	= 'position:absolute;top:-100em';
			self.measureDiv.className		= measureDivClassName;
			
			document.body.insertBefore(self.measureDiv, document.body.firstChild);
		}
		
	console.log("eu responsive created, selector = " + self.ops.selector + ", image count = " + $(self.ops.selector).length );
		self.lastSuffix				= self.ops.initialSuffix;
		self.respond();
		galleries.push(self);

	};
	
	euResponsive.prototype.respond = function(){
	
		var galleryImages = $(self.ops.selector);
		var gallerySuffix = self.ops.suffixes[self.measureDiv.offsetWidth] || self.ops.initialSuffix;
		var changed = false;
		
		
		if(gallerySuffix != self.lastSuffix){
			
			for(var i=0; i<galleryImages.length; i++){
				var galleryImage = galleryImages[i];
				var newSrc = $(galleryImage).attr("src").replace(self.lastSuffix + ".", gallerySuffix + ".");

	console.log("eu responsive updates image " + $(galleryImage).attr('src') + " to " + newSrc);
				
				$(galleryImage).attr('src', newSrc);
				
	console.log("self.measureDiv.offsetWidth =  " + self.measureDiv.offsetWidth + "   suffix becomes " + gallerySuffix + " (was " + self.lastSuffix + ")");
				
				changed = true;
			}
		}
		if(changed){
			self.lastSuffix	= gallerySuffix;			
		}
	};
	
	$(window).bind("resize", function(){
		for(var i=0; i<galleries.length; i++){
			galleries[i].respond();
		}
	});
	
	
})();

