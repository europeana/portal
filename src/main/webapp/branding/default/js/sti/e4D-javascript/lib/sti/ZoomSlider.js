function ZoomSlider(id,parent){
	this.div;
	this.slider;
	this.parent = parent;
	this.initialize(id);
    
};

ZoomSlider.prototype = {

    /**
     * initializes the GUI of the Spatio Temporal Interface
     */
    initialize: function(id){
    	var zs = this;
    	
    	this.div = document.createElement("div");
    	
		// Andy: restore css styling (and delete this) when IE7 requirement is dropped
    	//this.div.setAttribute('class','sliderStyle');
    	this.div.style.position		= "relative";
    	this.div.style.width		= "20px";
    	this.div.style.height		= "100px";
    	this.div.style.borderWidth	= "1px";
    	this.div.style.borderStyle	= "solid";
    	this.div.style.borderColor	= "#444";
    	
    	this.div.style.margin		= "auto";
    	this.div.style.marginTop	= "3px";
    	this.div.style.marginBottom	= "3px";
    	
    	
    	this.imagepath = '/portal/branding/portal2/js/sti/e4D-javascript/images/';
    	
    	var sliderContainer = document.createElement("div");
    	
    	/* Styled here */
    	sliderContainer.setAttribute('class','zoomSliderContainer');
    	
    	sliderContainer.style.position	= "absolute";
    	sliderContainer.style.top		= "10px";
    	sliderContainer.style.left		= "0px";	

		var sliderDiv = document.createElement("div");
		sliderDiv.id = id;
		sliderDiv.tabIndex = 1;
		var sliderInputDiv = document.createElement("div");
		sliderInputDiv.id = id+"-input";
		sliderDiv.appendChild(sliderInputDiv);		
		sliderContainer.appendChild(sliderDiv);		
		this.slider = new Slider( sliderDiv, sliderInputDiv, "vertical" );
		this.div.appendChild(sliderContainer);
		
		var zoomIn = document.createElement("img");
		zoomIn.src = this.imagepath + "zoom-in.png";
		zoomIn.title = eu.europeana.vars.msg.zoomIn;
		
		/* Styled here */
		zoomIn.setAttribute('class','zoomSliderIn');
		zoomIn.style.position	= "absolute";
		zoomIn.style.top		= "0px";
		zoomIn.style.left		= "2px";	

		
		zoomIn.onclick = function(){
	        zs.parent.zoom(1);
		};
		this.div.appendChild(zoomIn);
		
		var zoomOut = document.createElement("img");
		zoomOut.src = this.imagepath + "zoom-out.png";
		zoomOut.title = eu.europeana.vars.msg.zoomOut;
		
		/* Styled here */
		zoomOut.setAttribute('class','zoomSliderOut');
		zoomOut.style.position	= "absolute";
		zoomOut.style.top		= "84px";
		zoomOut.style.left		= "2px";	

		
		zoomOut.onclick = function(){
	        zs.parent.zoom(-1);
		};
		this.div.appendChild(zoomOut);
		
		this.slider.handle.onmousedown = function(){
			var oldValue = zs.slider.getValue();
			document.onmouseup = function(){
				if( !zs.parent.zoom( (zs.slider.getValue()-oldValue) / zs.max*zs.levels ) ){
					zs.setValue(oldValue);
				}
				document.onmouseup = null;
			}
		}
		
    },
    
    setValue: function(value){
    	this.slider.setValue(value/this.levels*this.max);
    },
    
    setMaxAndLevels: function(max,levels){
    	this.max = max;
    	this.levels = levels;
    	this.slider.setMaximum(max);
    }

}