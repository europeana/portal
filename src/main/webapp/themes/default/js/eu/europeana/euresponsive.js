
		(function() {

			function escapeRegex(text) {
				return text.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
			}

			function hasClass(elm, className) {
				return (' ' + elm.className + ' ').indexOf( className ) != -1;
			}

			function addListener(elm, type, callback) {
				if (elm.addEventListener){
					elm.addEventListener(type, callback, false);
				}
				else if (elm.attachEvent) {
					elm.attachEvent('on' + type, callback);
				}
			}

			function Gallery(elIn, modeIn) {
				console.log("new gallery " + elIn.attr("class"));
				this.el = elIn;
				this.mode = modeIn
			}
			

			Gallery.prototype.changeLayout = function(escapedInitialSuffix, newSuffix) {
				
				if(this.mode == "img"){
					var newHtmlStr = this.el.attr("src").replace(escapedInitialSuffix, newSuffix);
	
	//console.log("change from  " + this.el.attr("src") + " to " + newHtmlStr + "\n   - newSuffix " + newSuffix);
					
					// media queries don't work in IE - revert to biggest (last) image size
					if(jQuery.browser.msie  && ( parseInt(jQuery.browser.version, 10) === 7 || parseInt(jQuery.browser.version, 10) === 8 )  ){
						newHtmlStr = newHtmlStr.replace(self.ops.suffixes[0], self.ops.suffixes[self.ops.suffixes.length-1]);
					}
	
					// don't show what was hidden...
					var display		= this.el.css("display");
					var visibility	= this.el.css("visibility");
	
					this.el.attr("src", newHtmlStr);
	
	//console.log("el.src is now  " + this.el.attr("src")  );
		
					this.el.css("display", display);
					this.el.css("visibility", visibility);
				
				}
				else{
					
					var bgImg = this.el.css("background-image");
					
					//console.log("background-image =  " + bgImg );
					
					bgImg = bgImg.replace(escapedInitialSuffix, newSuffix);
					
					// media queries don't work in IE - revert to biggest (last) image size
					if(jQuery.browser.msie  && ( parseInt(jQuery.browser.version, 10) === 7 || parseInt(jQuery.browser.version, 10) === 8 )  ){
						bgImg = bgImg.replace(self.ops.suffixes[0], self.ops.suffixes[self.ops.suffixes.length-1]);
					}

					// don't show what was hidden...
					var display		= this.el.css("display");
					var visibility	= this.el.css("visibility");

					
					this.el.css("background-image", bgImg);


		//console.log("el.background-image is now  " + this.el.css("background-image")  );

					
					this.el.css("display", display);
					this.el.css("visibility", visibility);

					
				}
			};
			
			
			window.responsiveGallery = function(args) {
				
				/* *
				 * testDiv:			used to measure the window width
				 * lastSuffix:		stores the last applied suffix
				 * 
				 * */
				
				var self = this;
				
				self.ops					= args ? args : {};
				self.testDiv				= document.createElement( 'div' ),
				self.lastSuffix				= null,
				self.escapedInitialSuffix	= self.ops.initialSuffix;
				self.galleries				= [];
				
				// Add the test div to the page
				self.testDiv.className 		= self.ops.galleryName ? self.ops.galleryName : 'euresponsive';
				self.testDiv.style.cssText	= 'position:absolute;top:-100em';
				document.body.insertBefore(self.testDiv, document.body.firstChild);

				// Init galleries
				$(self.ops.imgSelector).each(function(i, ob){
					self.galleries.push( new Gallery( $(ob), 
						typeof self.ops.mode == 'undefined' ? 'img' : self.ops.mode
					) );
				});


				function respond() {
					var newSuffix = self.ops.suffixes[self.testDiv.offsetWidth] || self.ops.initialSuffix;
					
					//console.log("size " + newSuffix + ", num to update = " + self.galleries.length );
					
					if (newSuffix === self.lastSuffix) {
						
						//console.log("returning");

						return;
					}
					
					for (var i = self.galleries.length; i--;) {
						self.galleries[i].changeLayout(self.lastSuffix ? self.lastSuffix : self.escapedInitialSuffix, newSuffix);
					}
					
					self.lastSuffix = newSuffix;
				}
				respond();
				addListener(window, 'resize', respond);
			};
		})();
		
		
		