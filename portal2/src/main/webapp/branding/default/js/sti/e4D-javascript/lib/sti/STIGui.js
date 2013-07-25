function STIGui(core){

	this.core = core;
    
};

STIGui.prototype = {

    /**
     * initializes the GUI of the Spatio Temporal Interface
     */
	initialized: false,
	
    initialize: function(){    	
   	
		this.container = document.getElementById("e4DContainer");

		var toolbar = document.createElement("div");
		toolbar.style.position = "absolute";
		toolbar.style.top = "0px";
		this.container.appendChild(toolbar);

		var scheme = "Dark";
    	if( STIStatic.lightScheme ){
    		this.path = STIStatic.prefix+"images/light/";
    		scheme = "Light";
    		this.container.style.backgroundColor = "rgb(219,219,219)";
    	}
    	else {
    		this.path = STIStatic.prefix+"images/dark/";
    		this.container.style.backgroundColor = "rgb(171,171,171)";    		
    	}

    	var orientation = "Right";
		if( STIStatic.toolbarLeft ){
			toolbar.style.left = "0px";
			orientation = "Left";
		}
		else {
			toolbar.style.left = (this.container.offsetWidth-STIStatic.toolbarWidth)+"px";
		}
    	
		var timeplotHeight = STIStatic.timeplotHeight + 12;
    	var width = this.container.offsetWidth;
		var height = width * 3 / 4;

		this.top = 0;
		if( STIStatic.header ){
			this.top = STIStatic.headerHeight;
			height -= STIStatic.headerHeight;
		}
		
		if( STIStatic.map ){
			var mapWindow = document.createElement("div");
			mapWindow.id = "map";
			mapWindow.style.position = "absolute";
			mapWindow.style.width = width+"px";
			mapWindow.style.overflow = "hidden";
			
			if((height-timeplotHeight-1) > -1){
				mapWindow.style.height = (height-timeplotHeight-1)+"px";
			}
			
			mapWindow.style.top = this.top+"px";

			mapWindow.style.left = "0px";
			if( STIStatic.timeplot ){
				if( STIStatic.lightScheme ){
					mapWindow.style.borderBottom = "1px solid white";
				}
				else {
					mapWindow.style.borderBottom = "1px solid #444444";
				}
			}
			else {
				mapWindow.style.height = height+"px";
			}
			this.container.appendChild(mapWindow);
			this.core.map = new STIMap(this.core,"map",{h:orientation,v:"Top",s:scheme});
		}
		
		if( STIStatic.timeplot ){
			var plotDiv = document.createElement("div");
			plotDiv.id = "plot";
			plotDiv.style.position = "absolute";
			plotDiv.style.width = width+"px";
			plotDiv.style.top = (height-timeplotHeight+this.top)+"px";
			plotDiv.style.left = "0px";
			this.container.appendChild(plotDiv);
			this.core.timeplot = new STITimeplot(this.core,"plot",{h:orientation,v:"Bottom",s:scheme});
			if( !STIStatic.map ){
				this.container.style.height = timeplotHeight+"px";
				plotDiv.style.top = "0px";
				if( !STIStatic.popups && timeplotHeight < 200 ){
					this.container.style.top = (200-timeplotHeight)+"px";
				}
				else if( STIStatic.popups ){
					this.container.style.top = "240px";
				}
			}
		}
		toolbar.style.height = (this.container.offsetHeight+1)+"px";
		toolbar.style.width = (STIStatic.toolbarWidth-1)+"px";
		toolbar.setAttribute('class','toolbar'+orientation+''+scheme);
		
		if( STIStatic.history ){
	    	this.initializeHistory();
		}
    },
        
    initializeHistory: function(path){
    	var gui = this;
    	this.back = document.createElement("img");
    	this.back.src = this.path+"undo_0.png";
    	this.back.title = "undo";
    	this.back.onclick = function(){
    		if( gui.core.historyIndex > 0 ){
    			gui.core.switchThroughHistory(gui.core.historyIndex-1);
    		}
    	}
    	this.forward = document.createElement("img");
    	this.forward.src = this.path+"redo_0.png";
    	this.forward.title = "redo";
    	this.forward.onclick = function(){
    		if( gui.core.historyIndex < gui.core.history.length-1 ){
    			gui.core.switchThroughHistory(gui.core.historyIndex+1);
    		}
    	}
    },
    
    updateHistory: function(){
    	if( this.core.historyIndex == 0 ){
    		this.back.src = this.path+"undo_0.png";
    	}
    	else {
    		this.back.src = this.path+"undo_1.png";
    	}
    	if( this.core.historyIndex == this.core.history.length-1 ){
    		this.forward.src = this.path+"redo_0.png";
    	}
    	else {
    		this.forward.src = this.path+"redo_1.png";
    	}
    }
    
}