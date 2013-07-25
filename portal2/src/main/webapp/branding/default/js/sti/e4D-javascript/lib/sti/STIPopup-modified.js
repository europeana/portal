function STIMapPopup(map,core,parent){

	this.core = core;
    this.parent = parent;
    this.tWidth = 32;
    this.tHeight = 18;
    this.initialize();
    this.map = map;
}

STIMapPopup.prototype = {

		initialize: function(){
			var popup = this;
			this.maxResultsPerBubble = STIStatic.maxResultsPerBubble;
			this.popupDiv = document.createElement("div");
			
			// Andy: restore css styling (and delete this) when IE7 requirement is dropped
			//this.popupDiv.setAttribute('class','popupDiv');
			this.popupDiv.style.position		= "absolute";
			this.popupDiv.style.backgroundColor	= "white";
			this.popupDiv.style.borderColor		= "#666";
			this.popupDiv.style.borderStyle		= "solid";
			this.popupDiv.style.borderWidth		= "1px";
			this.popupDiv.style.paddingTop		= "25px";
			this.popupDiv.style.paddingRight	= "5px";
			this.popupDiv.style.paddingBottom	= "20px";
			this.popupDiv.style.paddingLeft		= "5px";
			this.popupDiv.style.visibility		= "hidden";
			this.popupDiv.style.borderRadius	= "10px";
			this.popupDiv.style.zIndex			= "100";
			this.popupDiv.style.minWidth		= "150px";
			
			
			
			this.parent.appendChild(this.popupDiv);
	        this.previous = document.createElement("div");
	        this.previous.title = "previous";
	        
	        
			// Andy: restore css styling (and delete this) when IE7 requirement is dropped
	        //this.previous.setAttribute('class', 'previous');
	        this.previous.style.position			= "absolute";
	        this.previous.style.height				= "16px";
	        this.previous.style.width				= "16px";
	        this.previous.style.bottom				= "5px";
	        this.previous.style.left				= "5px";
	        this.previous.style.backgroundImage		= "url(" + eu.europeana.vars.branding + "/js/sti/e4D-javascript/images/prev.png)";
	        this.previous.style.backgroundRepeat	= "no-repeat"; 
	        this.previous.style.cursor				= "pointer";
	        
	        this.popupDiv.appendChild(this.previous);
	        this.previous.onclick = function(){
	        	popup.page--;
	        	popup.update();
	        };
	        this.next = document.createElement("div");
	        this.next.title = "next";
	        
			// Andy: restore css styling (and delete this) when IE7 requirement is dropped
	        //this.next.setAttribute('class','next');
	        this.next.style.position			= "absolute";
	        this.next.style.height				= "16px";
	        this.next.style.width				= "16px";
	        this.next.style.bottom				= "5px";
	        this.next.style.right				= "5px";
	        this.next.style.backgroundImage		= "url(" + eu.europeana.vars.branding + "/js/sti/e4D-javascript/images/next.png)";
	        this.next.style.backgroundRepeat	= "no-repeat"; 
	        this.next.style.cursor				= "pointer";

	        this.popupDiv.appendChild(this.next);
	        this.next.onclick = function(){
	        	popup.page++;
	        	popup.update();
	        };
	        
			this.input = document.createElement("div");
			this.input.style.maxWidth = "300px";
			this.popupDiv.appendChild(this.input);
			this.triangle = document.createElement("div");
	        this.popupDiv.appendChild(this.triangle);
	        var cancel = document.createElement("div");
	        cancel.title = "Close Popup";
	        
			// Andy: restore css styling (and delete this) when IE7 requirement is dropped
	        //cancel.setAttribute('class','cancelRange');
	        cancel.style.position			= "absolute";
	        cancel.style.height				= "16px";
	        cancel.style.width				= "16px";
	        cancel.style.top				= "2px";
	        cancel.style.right				= "5px";
	        cancel.style.backgroundImage	= "url(" + eu.europeana.vars.branding + "/js/sti/e4D-javascript/images/close-button.png)";
	        cancel.style.backgroundRepeat	= "no-repeat";
	        cancel.style.cursor				= "pointer";

	        
	        this.popupDiv.appendChild(cancel);
	        cancel.onclick = function(){
	        	popup.map.labels = null;
	        	popup.core.reset();
	        };
	        this.back = document.createElement("div");
	        this.back.title = "Back";
	        
	        
			// Andy: restore css styling (and delete this) when IE7 requirement is dropped
	        //this.back.setAttribute('class','back');
	        this.back.style.position			= "absolute";
	        this.back.style.height				= "16px";
	        this.back.style.width				= "16px";
	        this.back.style.top					= "2px";
	        this.back.style.left				= "5px";
	        this.back.style.backgroundImage		= "url(" + eu.europeana.vars.branding + "/js/sti/e4D-javascript/images/back.png)";
	        this.back.style.backgroundRepeat	= "no-repeat";
	        this.back.style.cursor				= "pointer";		

	        this.popupDiv.appendChild(this.back);
	        this.back.onclick = function(){
	        	if( popup.showElements == 1 ){
	    			popup.showElements = popup.maxResultsPerBubble;
	    			popup.pages = Math.floor((popup.elements.length+(popup.maxResultsPerBubble-1))/popup.showElements);
	    			popup.page = Math.floor(popup.page/popup.showElements);
	    			popup.update();
	        	}
	        	else {
	        		popup.fillWithLabels();
	        	}
	        };
	        
	        this.resultsLabel = document.createElement("div");
	        
			// Andy: restore css styling (and delete this) when IE7 requirement is dropped
	        //this.resultsLabel.setAttribute('class','popupResultsLabel');
	        this.resultsLabel.style.position	= "absolute";
	        this.resultsLabel.style.bottom		= "10px";
	        this.resultsLabel.style.color		= "#999999";
	        
	        this.popupDiv.appendChild(this.resultsLabel);
		},
		update: function(){
			var popup = this;
			if( this.page+1 == this.pages ){
				this.next.style.visibility = "hidden";
			}
			else {
				this.next.style.visibility = "visible";
			}
			if( this.page == 0 ){
				this.previous.style.visibility = "hidden";
			}
			else {
				this.previous.style.visibility = "visible";
			}
			if( this.showElements == 1 ){
				this.input.innerHTML = "";
				var description = document.createElement("div");
				
				// Andy: restore css styling (and delete this) when IE7 requirement is dropped
				//description.setAttribute('class','popupDescription');
				description.innerHTML = this.elements[this.page].description;
				if( description.innerHTML == "" ){
					description.innerHTML = "<i>no description available</i>";
				}
		        this.input.appendChild(description);
				this.resultsLabel.innerHTML = (this.page+1)+" of "+this.pages+" Results";

				// Show back button only for details when skipping bubble titles - if there are other elements to show
				if(STIStatic.skipBubbleTitles && popup.elements.length > 1){					
					this.back.style.visibility = "visible";
				}
			}
			else {
				// Hide back button if we've skipped the bubble title
				if(STIStatic.skipBubbleTitles){					
					this.back.style.visibility = "hidden";
				}
				var list = document.createElement("ol");
				var start = this.page * this.showElements;
				list.start = (start+1)+"";
				var appendClick = function(item,index){
					item.onclick = function(){
						popup.showElements = 1;
						popup.pages = popup.elements.length;
						popup.page = index;
						popup.update();
					};
				};
				for( var i=start; i<start+this.showElements; i++ ){
					if( i == this.elements.length ){
						break;
					}
					var item = document.createElement("li");
					item.innerHTML = this.elements[i].name;
					
					// Andy: restore css styling (and delete this) when IE7 requirement is dropped
					//item.setAttribute('class','popupContent');
					item.style.fontWeight		= "bold";
					item.style.padding			= "5px";
					list.appendChild(item);
					
					item.onmouseover = function(){
						this.style.textDecoration	 = "underline";
					};
					item.onmouseout = function(){
						this.style.textDecoration	 = "none";
					};

					appendClick(item,i);
				}
				this.input.innerHTML = "";
				this.input.appendChild(list);
				var start = this.page * this.showElements + 1;
				var end = ((this.page+1)*this.showElements);
				if( end > this.elements.length ){
					end = this.elements.length;
				}
				this.resultsLabel.innerHTML = start+"-"+end+" of "+this.elements.length+" Results";
			}
			this.place();
			setTimeout( function(){
				popup.place(); 
				}, 100 );
		},
		
		shift: function(x,y){			
			this.left = this.left-this.x+x;
			this.top = this.top-this.y+y;
			this.x = x;
			this.y = y;
			if( this.left + this.popupDiv.offsetWidth > this.parent.offsetWidth ){
				this.popupDiv.style.left = 'auto';
				this.popupDiv.style.right = (this.parent.offsetWidth-this.left-this.popupDiv.offsetWidth)+"px";
			}
			else {
				this.popupDiv.style.right = 'auto';
				this.popupDiv.style.left = this.left+"px";
			}
			this.popupDiv.style.top = this.top+"px";
		},
		
		place: function(){
			this.popupDiv.style.visibility = "visible";
			this.popupDiv.style.left = 'auto';
			this.popupDiv.style.right = 'auto';
			
			var left = this.x;
			var right = this.parent.offsetWidth - left;
			var top = this.y;
			var bottom = this.parent.offsetHeight - top;
			var width = this.popupDiv.offsetWidth;
			var height = this.popupDiv.offsetHeight;

			var vertical = true;
			
			this.triangle.style.position			= "absolute";
			this.triangle.style.zIndex				= "1000";
			this.triangle.style.backgroundRepeat	= "no-repeat";
			
			
			if( right >= left && right >= top && right >=bottom ){
				this.left = left + this.tHeight;
				
				// Andy: restore css styling (and delete this) when IE7 requirement is dropped
				//this.triangle.setAttribute('class','popupTriangleLeft');
				this.triangle.style.left = (-1*this.tHeight)+"px";
				this.triangle.style.height					= "31px";
				this.triangle.style.width					= "18px";
			    this.triangle.style.backgroundImage			= "url(" + eu.europeana.vars.branding + "/js/sti/e4D-javascript/images/bubble-arrow-point-left.png)";
				this.triangle.style.backgroundPosition		= "-68px -8px";
			}
			else if( left >= right && left >= top && left >=bottom ){
				this.left = left - width - this.tHeight + 4;
				
				// Andy: restore css styling (and delete this) when IE7 requirement is dropped
				//this.triangle.setAttribute('class','popupTriangleRight');
				this.triangle.style.left = (width-4)+"px";
				this.triangle.style.height					= "31px";
				this.triangle.style.width					= "18px";
			    this.triangle.style.backgroundImage			= "url(" + eu.europeana.vars.branding + "/js/sti/e4D-javascript/images/bubble-arrow-point-right.png)";
				this.triangle.style.backgroundPosition		= "-12px 0px";
			}
			else if( top >= right && top >= left && top >=bottom ){
				this.top = top - height - this.tHeight + 4;
				
				// Andy: restore css styling (and delete this) when IE7 requirement is dropped
				//this.triangle.setAttribute('class','popupTriangleDown');
				this.triangle.style.top = (height-4)+"px";
				this.triangle.style.height					= "18px";
				this.triangle.style.width					= "31px";
				this.triangle.style.backgroundImage			= "url(" + eu.europeana.vars.branding + "/js/sti/e4D-javascript/images/bubble-arrow-point-down.png)";
				this.triangle.style.backgroundPosition		= "-8px -13px";

				vertical = false;
			}
			else {
				this.top = top + this.tHeight;
				
				// Andy: restore css styling (and delete this) when IE7 requirement is dropped
				//this.triangle.setAttribute('class','popupTriangleUp');
				this.triangle.style.top = (-1*this.tHeight)+"px";
				this.triangle.style.height					= "18px";
				this.triangle.style.width					= "31px";
			    this.triangle.style.backgroundImage			= "url(" + eu.europeana.vars.branding + "/js/sti/e4D-javascript/images/bubble-arrow-point-up.png)";
			    this.triangle.style.backgroundPosition		= "0px -67px";

				vertical = false;
			}
			if( vertical ){
				if( this.y < height/2 ){
					this.top = 0;
					this.triangle.style.top = (this.y-this.tWidth/2-2)+"px";
				}
				else if( this.parent.offsetHeight < this.y + height/2 ){
					this.top = this.parent.offsetHeight - height;
					this.triangle.style.top = (height-bottom-this.tWidth/2-2)+"px";
				}
				else {
					this.top = top - height/2;
					this.triangle.style.top = (height/2-this.tWidth/2-2)+"px";
				}
			}
			else {
				this.left = left - width/2;
				this.triangle.style.left = (width/2-this.tWidth/2-2)+"px";
			}
			this.popupDiv.style.left	= this.left+"px";
			this.popupDiv.style.top		= this.top+"px";
			this.popupDiv.style.zIndex	= 20000;
			
			this.resultsLabel.style.left = ((width-this.resultsLabel.offsetWidth)/2)+"px";
		},
		
		showLabelContent: function(label){
			// Hide back button if we're skipping the bubble titles
			this.back.style.visibility = "visible";
			if(STIStatic.skipBubbleTitles){				
				this.back.style.visibility = "hidden";
			}
			
			this.resultsLabel.style.visibility = "visible";
			this.label = label;
			this.elements = label.elements;
			this.page = 0;
			this.showElements = this.maxResultsPerBubble;
			//this.pages = Math.floor((this.elements.length+9)/this.showElements);
			this.pages = Math.floor((this.elements.length+(this.showElements-1))/this.showElements);
			this.update();
		},
		
		fillWithLabels: function(){
			var labelList = document.createElement("ul");
			for( i in this.labels ){
				this.labels[i].div.style.position = "relative";
				this.labels[i].div.style.top = "0px";
				var li = document.createElement("li");
				li.appendChild(this.labels[i].div);
				labelList.appendChild(li);
			}
			this.input.innerHTML = "";
			this.input.appendChild(labelList);
			this.back.style.visibility = "hidden";
			this.resultsLabel.style.visibility = "hidden";
			this.place();
		},
		
		createMapPopup: function(x,y,labels){
			this.labels = labels;
			this.x = x;
			this.y = y;
			this.fillWithLabels();
		},
		
		reset: function(){
			this.popupDiv.style.visibility = "hidden";
			this.next.style.visibility = "hidden";
			this.previous.style.visibility = "hidden";
			this.back.style.visibility = "hidden";
			this.resultsLabel.style.visibility = "hidden";
			this.page = 0;
			this.pages = 0;
		}
		
};


function STITimeplotPopup(parent){

    this.parent = parent;
    this.tWidth = 32;
    this.tHeight = 18;
    this.initialize();
    
}

STITimeplotPopup.prototype = {

		initialize: function(){
			var popup = this;
			this.popupDiv = document.createElement("div");
			
			this.popupDiv.setAttribute('class','popupDiv');
			this.parent.appendChild(this.popupDiv);
	        this.previous = document.createElement("div");
	        this.previous.title = "previous";
	        this.previous.setAttribute('class','previous');
	        this.popupDiv.appendChild(this.previous);
	        this.previous.onclick = function(){
	        	popup.page--;
	        	popup.update();
	        };
	        this.next = document.createElement("div");
	        this.next.title = "next";
	        this.next.setAttribute('class','next');
	        this.popupDiv.appendChild(this.next);
	        this.next.onclick = function(){
	        	popup.page++;
	        	popup.update();
	        };
			this.input = document.createElement("div");
			this.input.style.maxWidth = "300px";
			this.input.style.height = "170px";
			this.input.style.overflow = "auto";
			this.popupDiv.appendChild(this.input);
			this.triangle = document.createElement("div");
	        this.popupDiv.appendChild(this.triangle);
	        var cancel = document.createElement("div");
	        cancel.title = "Close Popup";
	        cancel.setAttribute('class','cancelRange');
	        this.popupDiv.appendChild(cancel);
	        cancel.onclick = function(){
	        	popup.reset();
	        };
	        this.back = document.createElement("div");
	        this.back.title = "Back";
	        this.back.setAttribute('class','back');
	        this.popupDiv.appendChild(this.back);
	        this.back.onclick = function(){
	        	// europeana modification
    			popup.showElements = popup.maxResultsPerBubble;
    			popup.pages = Math.floor((popup.elements.length+(popup.showElements-1))/popup.showElements);
    			popup.page = Math.floor(popup.page/popup.showElements);
    			popup.update();
    			popup.back.style.visibility = "hidden";
	        };
	        this.resultsLabel = document.createElement("div");
	        this.resultsLabel.setAttribute('class','popupResultsLabel');
	        this.popupDiv.appendChild(this.resultsLabel);
		},
		
		update: function(){
			var popup = this;
			if( this.page+1 == this.pages ){
				this.next.style.visibility = "hidden";
			}
			else {
				this.next.style.visibility = "visible";
			}
			if( this.page == 0 ){
				this.previous.style.visibility = "hidden";
			}
			else {
				this.previous.style.visibility = "visible";
			}
			if( this.showElements == 1 ){
				this.input.innerHTML = "";
				var description = document.createElement("div");				
				description.setAttribute('class','popupDescription');
				description.innerHTML = this.elements[this.page].description;
				if( description.innerHTML == "" ){
					description.innerHTML = "<i>no description available</i>";
				}
				// ANDY error:timeline 1892 - 1964 popup = Nan-Nan of 165 Results
		        this.input.appendChild(description);
				this.resultsLabel.innerHTML = (this.page+1)+" of "+this.pages+" Results";
				this.back.style.visibility = "visible";
			}
			else {
				var list = document.createElement("ol");
				var start = this.page * this.showElements;
				list.start = (start+1)+"";
				var appendClick = function(item,index){
					item.onclick = function(){
						popup.showElements = 1;
						popup.pages = popup.elements.length;
						popup.page = index;
						popup.update();
					};
				};
				for( var i=start; i<start+this.showElements; i++ ){
					if( i == this.elements.length ){
						break;
					}
					var item = document.createElement("li");
					item.innerHTML = this.elements[i].name;
					list.appendChild(item);
					appendClick(item,i);
				}
				this.input.innerHTML = "";
				this.input.appendChild(list);
				var start = this.page * this.showElements + 1;
				var end = ((this.page+1)*this.showElements);
				if( end > this.elements.length ){
					end = this.elements.length;
				}
				this.resultsLabel.innerHTML = start+"-"+end+" of "+this.elements.length+" Results";
			}
			this.place();
			setTimeout( function(){
				popup.place();
				}, 100 );
		},
		
		place: function(){
			this.popupDiv.style.visibility = "visible";
			this.popupDiv.style.left = 'auto';
			this.popupDiv.style.right = 'auto';
			
			var left = this.x;
			var right = this.parent.offsetWidth - left;
			var top = this.parent.offsetTop;
			//var bottom = this.parent.offsetHeight - top;
			var width = this.popupDiv.offsetWidth;
			var height = this.popupDiv.offsetHeight;

			this.top = top - height + 4;
			this.triangle.setAttribute('class','popupTriangleDown');
			
			this.triangle.style.position				= "absolute";
			this.triangle.style.zIndex					= "1000";
			this.triangle.style.backgroundRepeat		= "no-repeat";
			this.triangle.style.top						= (height-4)+"px";
			this.triangle.style.height					= "18px";
			this.triangle.style.width					= "31px";
			this.triangle.style.backgroundImage			= "url(" + eu.europeana.vars.branding + "/js/sti/e4D-javascript/images/bubble-arrow-point-down.png)";
			this.triangle.style.backgroundPosition		= "-8px -13px";
			
			if( left - width/2 < 0 ){
				this.left = 0;
				this.triangle.style.left = (left-this.tWidth/2-2)+"px";
			}
			else if( left + width/2 > this.parent.offsetWidth ){
				this.left = this.parent.offsetWidth - width;
				this.triangle.style.left = (width-right-this.tWidth/2-2)+"px";
			}
			else {
				this.left = left - width/2;
				this.triangle.style.left = (width/2-this.tWidth/2-2)+"px";
			}				
			
			this.popupDiv.style.left = this.left+"px";
			this.popupDiv.style.top = (-1*height)+"px";
			this.resultsLabel.style.left = ((width-this.resultsLabel.offsetWidth)/2)+"px";
		},
		createTimeplotPopup: function(x,elements){
			/* Time ranges are specified by dragging across the timeline.  A click on the
			 * resulting period triggers this popup
			 */ 
			this.x = x;
			this.resultsLabel.style.visibility = "visible";
			this.elements = elements;
			this.page = 0;
			// europeana modification
			this.showElements = STIStatic.maxResultsPerBubble;
			this.pages = Math.floor((this.elements.length+(this.showElements-1))/this);
			this.update();
		},
		
		reset: function(){
			this.popupDiv.style.visibility = "hidden";
			this.next.style.visibility = "hidden";
			this.previous.style.visibility = "hidden";
			this.back.style.visibility = "hidden";
			this.resultsLabel.style.visibility = "hidden";
			this.page = 0;
			this.pages = 0;
		}
		
};
