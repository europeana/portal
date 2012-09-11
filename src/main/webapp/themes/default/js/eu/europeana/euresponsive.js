
		(function() {
			
			function log(msg){
				if(console){
					console.log(msg);
				}
			}

			function escapeRegex(text) {
				return text.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&");
			}

			function hasClass(elm, className) {
				return (' ' + elm.className + ' ').indexOf( className ) != -1;
			}

			function addListener(elm, type, callback) {
				if (elm.addEventListener) {
					elm.addEventListener( type, callback, false );
				}
				else if (elm.attachEvent) {
					elm.attachEvent( 'on' + type, callback );
				}
			}

			function Gallery(script) {
				/*
				this.htmlStr = script.nextSibling.nodeValue.slice( 10, -11 );
				this.container = document.createElement( 'div' );
				script.parentNode.insertBefore( this.container, script.nextSibling );
				*/
				this.htmlStr = script.nextSibling.nodeValue.slice( 10, -11 ); // extract img tag from comment
				
				
				
				log("euresponsive constructor: " + this.htmlStr );

				
				
				this.container = document.createElement( 'div' );
				script.parentNode.insertBefore( this.container, script.nextSibling );
			}
			

			
			Gallery.prototype.changeLayout = function(escapedInitialSuffix, newSuffix) {
				
				var img = jQuery(this.container).find("img");

				/*
				if(jQuery.browser.msie  && ( parseInt(jQuery.browser.version, 10) === 7 || parseInt(jQuery.browser.version, 10) === 8 )  ){
					// browsers that can't handle media queries should not continue
					newSuffix = newSuffix.replace(/_euresponsive_1/g, '');
					newSuffix = newSuffix.replace(/_euresponsive_2/g, '');
					newSuffix = newSuffix.replace(/_euresponsive_3/g, '');
					newSuffix = newSuffix.replace(/_euresponsive_4/g, '');
					//return;
				}
				*/
				

				log("euresponsive changeLayout: img = " + img + " escapedInitialSuffix = " + escapedInitialSuffix + ", newSuffix = " + newSuffix + ", this.htmlStr = " + this.htmlStr);

				
				var newHtmlStr = this.htmlStr.replace(
					new RegExp('(src="[^"]*)' + escapedInitialSuffix + '"', 'g'),
					'$1' + newSuffix + '"'
				);
				
				// regex expression doesn't work in IE.... quick fix: text replace
				if(jQuery.browser.msie  && ( parseInt(jQuery.browser.version, 10) === 7 || parseInt(jQuery.browser.version, 10) === 8 )  ){
					newHtmlStr = newHtmlStr.replace("_euresponsive_1", "_euresponsive_4");
				}

				
				var display		= img.css("display");
				var visibility	= img.css("visibility");
				

				this.container.innerHTML = newHtmlStr;
				
				
				log("euresponsive changeLayout: changed container html to = " + newHtmlStr );


				img = jQuery(this.container).find("img");
				
				img.css("display", display);
				
				img.css("visibility", visibility);
				
				
				log("euresponsive changeLayout: recovered img = " + img );

				
				//log("changelayout called, new html = " + newHtmlStr);
			};
			
			
			
			window.responsiveGallery = function(args) {
				
				// fn to measure the size of the suffixes associative array
				/*
				if(typeof Object.prototype.size == "undefined"){					
					Object.prototype.size = function () {
						var len = this.length ? --this.length : -1;
						for (var k in this)
							len++;
						return len;
					}
				}
				*/
				var testDiv = document.createElement( 'div' ),
					scripts = document.getElementsByTagName( 'script' ),
					lastSuffix,
					escapedInitialSuffix = escapeRegex( args.initialSuffix || '' ),
					galleries = [];

				// Add the test div to the page
				testDiv.className = args.testClass || 'gallery-test';
				testDiv.style.cssText = 'position:absolute;top:-100em';
				document.body.insertBefore( testDiv, document.body.firstChild );

				// Init galleries
				for ( var i = scripts.length; i--; ) {
					var script = scripts[i];
					
					if ( hasClass(script, args.scriptClass) ) {
						galleries.push( new Gallery(script) );
					}
				}

				function respond() {

					var newSuffix = args.suffixes[testDiv.offsetWidth] || args.initialSuffix;
					
					if (newSuffix === lastSuffix) {
						return;
					}
					
					for (var i = galleries.length; i--;) {
						galleries[i].changeLayout(escapedInitialSuffix, newSuffix);
					}
					
					lastSuffix = newSuffix;
				}
				
				respond();
				addListener(window, 'resize', respond);
			};
		})();
		
		
		