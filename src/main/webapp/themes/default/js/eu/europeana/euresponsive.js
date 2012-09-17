
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

			function Gallery(elIn) {
				console.log("new gallery " + elIn.attr("class"));
				this.el = elIn;
			}
			

			Gallery.prototype.changeLayout = function(escapedInitialSuffix, newSuffix) {
				

				var newHtmlStr = this.el.attr("src").replace(escapedInitialSuffix, newSuffix);
console.log("chsnge from  " + this.el.attr("src") + " to " + newHtmlStr + "\n   - newSuffix " + newSuffix);
				
				// regex expression doesn't work in IE.... quick fix: text replace
				if(jQuery.browser.msie  && ( parseInt(jQuery.browser.version, 10) === 7 || parseInt(jQuery.browser.version, 10) === 8 )  ){
					newHtmlStr = newHtmlStr.replace("_1.", "_4.");
				}

				// don't show what was hidden...
				var display		= this.el.css("display");
				var visibility	= this.el.css("visibility");

				this.el.attr("src", newHtmlStr);
console.log("el.src is now  " + this.el.attr("src")  );
	
				this.el.css("display", display);
				this.el.css("visibility", visibility);
			};
			
			
			window.responsiveGallery = function(args) {
				/* testDiv:			used to measure the window width
				 * lastSuffix:		stores the last applied suffix
				 * 
				 * */
				var self = this;
				self.ops = args;
				self.testDiv = document.createElement( 'div' ),
				self.lastSuffix = null,
				self.escapedInitialSuffix = self.ops.initialSuffix; // escapeRegex( args.initialSuffix || '' ),
				self.galleries = [];

				// Add the test div to the page
				self.testDiv.className 		= self.ops.galleryName ? self.ops.galleryName : 'euresponsive';
				self.testDiv.style.cssText	= 'position:absolute;top:-100em';
				document.body.insertBefore(self.testDiv, document.body.firstChild);

				// Init galleries
				$(self.ops.imgSelector).each(function(i, ob){
					self.galleries.push( new Gallery( $(ob) ) );
				});


				function respond() {
					var newSuffix = self.ops.suffixes[self.testDiv.offsetWidth] || self.ops.initialSuffix;
					
					console.log("size " + newSuffix + ", num to update = " + self.galleries.length );
					
					if (newSuffix === self.lastSuffix) {
						
						console.log("returning");

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
		
		
		