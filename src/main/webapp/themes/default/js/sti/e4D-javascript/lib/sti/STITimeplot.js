/**
 * defines the timeplot component of the Spatio Temporal Interface
 * it builds a timeplot context with the Simile Widget Timeplot JavaScript Framework
 * @param {STICore} core the sti core component, the timeplot component has to deal with
 * @param {String} container the div id for the container of the timeplot widget
 *
 * @constructor
 */
function STITimeplot(core,containerId,toolbarStyle){

    this.core = core;
    this.timeplotDiv;
    this.timeplot;
    this.dataSources;
    this.eventSources;
    this.eds;
    this.timeGeometry;
    this.valueGeometry;
    this.canvas;
    
    this.leftFlagPole;
    this.rightFlagPole;
    this.rangeBox;
    this.leftFeather;
    this.rightFeather;
    this.leftHandle;
    this.rightHandle;
    
    this.leftFlagPos = null;
    this.leftFlagTime = null;
    this.rightFlagPos = null;
    this.rightFlagTime = null;
    this.leftFeatherTime = null;
    this.rightFeatherTime = null;
    
    this.mouseDownTime;
    this.mouseUpTime;
    this.mouseTempTime;    
    this.mouseDownPos;
    this.mouseUpPos;
    this.mouseTempPos;    
    
    this.placePoles;    
    this.hoverSlice;
    
    this.status;
    this.featherWidth;    
    this.slider;
    
    this.initialize(containerId,toolbarStyle);
    
}

STITimeplot.prototype = {

    /**
     * clears the timeplot canvas and the timeGeometry properties
     */
	clearTimeplot: function(){
        this.timeplot._clearCanvas();        
        this.timeGeometry._earliestDate = null;
        this.timeGeometry._latestDate = null;
        this.timeGeometry._clearLabels();
	},

    /**
     * initializes the timeplot elements with arrays of time objects
     * @param {TimeObject[][]} timeObjects an array of time objects from different (1-4) sets
     */
    initTimeplot: function(timeObjects){
		this.clearTimeplot();
    	this.resetTimeplot();	
        for (var i = 0; i < this.timeplot._plots.length; i++) {
            this.timeplot._plots[i].dispose();
        }
        this.timeplot._clearCanvas();        
        this.timeGeometry._earliestDate = null;
        this.timeGeometry._latestDate = null;
        this.valueGeometry._minValue = null;
        this.valueGeometry._maxValue = null;
        this.dataSources = new Array();
        this.plotInfos = new Array();
        this.eventSources = new Array();
        var granularity = 0;
        for (var i = 0; i < timeObjects.length; i++) {
        	var eventSource = new Timeplot.DefaultEventSource();
        	var dataSource = new Timeplot.ColumnSource(eventSource, 1);
        	this.dataSources.push(dataSource);
        	this.eventSources.push(eventSource);
        	var c = STIStatic.colors[i];
        	var plotInfo = Timeplot.createPlotInfo({
            	id: "plot" + i,
            	dataSource: dataSource,
            	timeGeometry: this.timeGeometry,
            	valueGeometry: this.valueGeometry,
            	fillGradient: false,
            	lineColor: 'rgba(' + c.r1 + ',' + c.g1 + ',' + c.b1 + ', 1)',
            	fillColor: 'rgba(' + c.r0 + ',' + c.g0 + ',' + c.b0 + ', 0.3)',
            	showValues: true
        	});
        	this.plotInfos.push(plotInfo);
            for (var j = 0; j < timeObjects[i].length; j++){
            	if( timeObjects[i][j].granularity > granularity ){
            		granularity = timeObjects[i][j].granularity;
            	}
            }
        }
        this.timeGeometry._granularity = granularity;
        this.timeGeometry._clearLabels();
        this.timeplot.resetPlots(this.plotInfos);
    	if( this.plotInfos.length == 0 ){
    		this.initLabels( this.timeplot.regularGrid() );
    		return;
    	}
        this.timeGeometry.extendedDataSource = this.eds;
        this.eds.initialize(this.dataSources, this.eventSources, timeObjects, granularity, STIStatic.timeUnit);
        var levels = Math.round( (this.eds.timeSlices.length-3)/2 );
        if( STIStatic.timeZoom ){
            this.zoomSlider.setMaxAndLevels(levels,levels);
        }
       	this.timeplot.repaint();
        // set maximum number of slider steps
        var slices = this.eds.timeSlices.length;
        var numSlices = Math.floor(slices / this.canvas.width * this.canvas.height + 0.5);
        this.slider.setMaximum(numSlices);
        this.initLabels([]);
        this.initOverview();
	},
    
    /**
     * initializes the timeplot for the Spatio Temporal Interface.
     * all elements (including their events) that are needed for user interaction are instantiated here, the slider element as well
     */
    initialize: function(containerId,toolbarStyle){
    
        this.status = 0;
    	this.paused = true;
        this.dataSources = new Array();
        this.plotInfos = new Array();
        this.eventSources = new Array();
        this.timeGeometry = new Timeplot.DefaultTimeGeometry({
            gridColor: "#000000",
            axisLabelsPlacement: "top"
        });
        this.timeGeometry._hideLabels = true;
        this.timeGeometry._granularity = 0;
        this.valueGeometry = new Timeplot.DefaultValueGeometry({
            min: 0
        });
        this.placePoles = new Array();

		var tools = this.initializeTimeplotTools();
        
		var container = document.getElementById(containerId);
        if( container.offsetWidth == 0 ){
        	container.style.width = "800px";
        }
        var w = container.offsetWidth;
        var l = 0;
        if( tools.length > 0 ){
        	w -= STIStatic.toolbarWidth;
        	if( toolbarStyle.h == "Left" ){
        		l += STIStatic.toolbarWidth;
        	}
        }
        var h = STIStatic.timeplotHeight;
		this.plotWindow = document.createElement("div");
		this.plotWindow.id = "plotWindow";
		this.plotWindow.style.width = w+"px";
		this.plotWindow.style.height = (h+12)+"px";
		this.plotWindow.style.left = l+"px";
		container.style.height = (h+12)+"px";

		var plotContainer = document.createElement("div");
		plotContainer.id = "plotContainer";
		plotContainer.style.width = w+"px";
		plotContainer.style.height = h+"px";
		this.plotWindow.appendChild(plotContainer);
		container.appendChild(this.plotWindow);

        this.timeplotDiv = document.createElement("div");
        this.timeplotDiv.style.left = "16px";
        this.timeplotDiv.style.width = (w-32)+"px";
        this.timeplotDiv.style.height = h+"px";
        plotContainer.appendChild(this.timeplotDiv);
        
        this.timeplot = Timeplot.create(this.timeplotDiv, this.plotInfos);
        this.eds = new ExtendedDataSource();
        
        this.canvas = this.timeplot.getCanvas();
        
        this.leftFlagPole = this.timeplot.putDiv("leftflagpole", "timeplot-dayflag-pole");
        this.rightFlagPole = this.timeplot.putDiv("rightflagpole", "timeplot-dayflag-pole");
        SimileAjax.Graphics.setOpacity(this.leftFlagPole, 50);
        SimileAjax.Graphics.setOpacity(this.rightFlagPole, 50);

        this.rangeBox = this.timeplot.putDiv("rangebox", "range-box");
        this.leftFeather = this.timeplot.putDiv("leftfeather", "left-feather");
        this.rightFeather = this.timeplot.putDiv("rightfeather", "right-feather");
        
        this.leftHandle = document.createElement("div");
        this.leftHandle.setAttribute('class','plotHandle');
        this.leftHandle.title = "Click and move mouse to change left border.";
       	this.rightHandle = document.createElement("div");
       	this.rightHandle.setAttribute('class','plotHandle');
        this.rightHandle.title = "Click and move mouse to change right border.";
        this.plotWindow.appendChild(this.leftHandle);
        this.plotWindow.appendChild(this.rightHandle);
        
		this.poles = this.timeplot.putDiv( "poles", "pole" );
        this.timeplot.placeDiv(this.poles, {
            left: 0,
            bottom: 0,
            width: this.canvas.width,
            height: this.canvas.height,
            display: "block"
        });
        this.poles.appendChild(document.createElement("canvas"));
        
        this.toolbar = document.createElement("div");
        this.toolbar.setAttribute('class','plotToolbar');
        this.plotWindow.appendChild(this.toolbar);
        
        this.toolbarAbsoluteDiv = document.createElement("div");
        this.toolbarAbsoluteDiv.setAttribute('class','absoluteToolbar');
        this.toolbar.appendChild(this.toolbarAbsoluteDiv);
        
        var drag = document.createElement("div");
        drag.title = "Click to drag the selection on the timeplot. Click again to deselect.";
        drag.setAttribute('class','dragRange');
        this.toolbarAbsoluteDiv.appendChild(drag);

        var zoom = document.createElement("div");
        zoom.title = "Zoom into selection. To undo, use the History button.";
        zoom.setAttribute('class','zoomRange');
        this.toolbarAbsoluteDiv.appendChild(zoom);
        zoom.onclick = function(){
        	plot.core.refine();
        }

        if( STIStatic.popups ){
        	this.popup = new STITimeplotPopup(this.plotWindow);
        	this.popupClickDiv = this.timeplot.putDiv("popupClickDiv", "popup-click-div");
        	this.popupClickDiv.title = "Click to open popup";        	
        }
        
		// displays the feather divs
        var displayFeather = function(){        
            plot.leftFeather.style.visibility = "visible";
            plot.rightFeather.style.visibility = "visible";            
            plot.timeplot.placeDiv(plot.leftFeather, {
                left: plot.leftFlagPos - plot.featherWidth,
                width: plot.featherWidth,
                bottom: 0,
                height: plot.canvas.height,
                display: "block"
            });
            plot.timeplot.placeDiv(plot.rightFeather, {
                left: plot.rightFlagPos,
                width: plot.featherWidth,
                bottom: 0,
                height: plot.canvas.height,
                display: "block"
            });            
            var leftCv = document.getElementById("leftFeatherCanvas");
            if (leftCv == null) {
                leftCv = document.createElement("canvas");
                leftCv.id = "leftFeatherCanvas";
                plot.leftFeather.appendChild(leftCv);
            }
            leftCv.width = plot.featherWidth;
            leftCv.height = plot.canvas.height;
 
           if (!leftCv.getContext && G_vmlCanvasManager) 
                leftCv = G_vmlCanvasManager.initElement(leftCv);
            var leftCtx = leftCv.getContext('2d');
            var leftGradient = leftCtx.createLinearGradient(plot.featherWidth, 0, 0, 0);
            leftGradient.addColorStop(0, 'rgba(95,136,178,0.6)');
            leftGradient.addColorStop(1, 'rgba(171,171,171,0)');
            leftCtx.fillStyle = leftGradient;
            leftCtx.fillRect(0, 0, plot.featherWidth, plot.canvas.height);   

            var rightCv = document.getElementById("rightFeatherCanvas");
            if (rightCv == null) {
                rightCv = document.createElement("canvas");
                rightCv.id = "rightFeatherCanvas";
                plot.rightFeather.appendChild(rightCv);
            }
            rightCv.width = plot.featherWidth;
            rightCv.height = plot.canvas.height;
            if (!rightCv.getContext && G_vmlCanvasManager) 
                rightCv = G_vmlCanvasManager.initElement(rightCv);
            var rightCtx = rightCv.getContext('2d');
            var rightGradient = rightCtx.createLinearGradient(0, 0, plot.featherWidth, 0);
            rightGradient.addColorStop(0, 'rgba(95,136,178,0.6)');
            rightGradient.addColorStop(1, 'rgba(171,171,171,0)');
            rightCtx.fillStyle = rightGradient;
            rightCtx.fillRect(0, 0, plot.featherWidth, plot.canvas.height);
        }         
                
        var featherSliderDiv = document.createElement("div");
		featherSliderDiv.setAttribute('class', 'featherSlider');
		featherSliderDiv.title = "Adjust the feather of the selection to smoothen the animations on the map.";
        var sliderInput = document.createElement("input");
        featherSliderDiv.appendChild(sliderInput);
        this.slider = new Slider(featherSliderDiv,sliderInput,"horizontal");
        this.slider.onchange = function(){
            if (plot.leftFlagPos != null) {
                plot.setFeather();
                var plots = plot.timeplot._plots;
                for (i = 0; i < plots.length; i++) {
                    plots[i].fullOpacityPlot(plot.leftFlagTime, plot.rightFlagTime, plot.leftFlagPos, plot.rightFlagPos, plot.leftFeatherTime, plot.rightFeatherTime, plot.featherWidth, STIStatic.colors[i]);
                    plots[i].opacityPlot.style.visibility = "visible";
                }
                displayFeather();
                plot.updateByTime();
            }
        }
        this.toolbarAbsoluteDiv.appendChild(featherSliderDiv);        
                
        var plot = this;
        
        var cancel = document.createElement("div");
        cancel.setAttribute('class','cancelRange');
        cancel.title = "Clear Selection";
        this.toolbarAbsoluteDiv.appendChild(cancel);
        cancel.onclick = function(){
        	plot.core.reset();
        }
        
        this.overview = document.createElement("div");
        this.overview.setAttribute('class','timeOverview');
        this.plotWindow.appendChild(this.overview);
        
		var mousedown = false;
        this.shift = function(shift){
        	if( !mousedown ){
        		return;
        	}
			if( plot.eds.setShift(shift) ){
				plot.redrawPlot();
			}
			setTimeout( function(){ plot.shift(shift); }, 100 );
        }
		
		var shiftPressed = function(shift){
        	mousedown = true;
        	document.onmouseup = function(){
        		mousedown = false;
        		document.onmouseup = null;
        	}
        	plot.shift(shift);
		}
		
        this.shiftLeft = document.createElement("div");
        this.shiftLeft.setAttribute('class','shiftLeft');
        this.plotWindow.appendChild(this.shiftLeft);
        this.shiftLeft.onmousedown = function(){
        	shiftPressed(1);
        }

        this.shiftRight = document.createElement("div");
        this.shiftRight.setAttribute('class','shiftRight');
        this.plotWindow.appendChild(this.shiftRight);
        this.shiftRight.onmousedown = function(){
        	shiftPressed(-1);
        }

        this.plotLabels = document.createElement("div");
        this.plotLabels.setAttribute('class','plotLabels');
        this.plotWindow.appendChild(this.plotLabels);
        
        this.initLabels( this.timeplot.regularGrid() );

        //Finds the time corresponding to the position x on the timeplot
        var getCorrelatedTime = function(x){
            if (x >= plot.canvas.width) 
                x = plot.canvas.width;
            if (isNaN(x) || x < 0) 
                x = 0;
            var t = plot.timeGeometry.fromScreen(x);
            if (t == 0) 
                return;
            return plot.dataSources[0].getClosestValidTime(t);
        }
        
        //Finds the position corresponding to the time t on the timeplot
        var getCorrelatedPosition = function(t){
			var x = plot.timeGeometry.toScreen(t);
            if (x >= plot.canvas.width) 
                x = plot.canvas.width;
            if (isNaN(x) || x < 0) 
                x = 0;
            return x;
        }

        //Maps the 2 positions in the right order to left and right bound of the chosen timeRange
        var mapPositions = function(pos1, pos2){
            if (pos1 > pos2) {
                plot.leftFlagPos = pos2;
                plot.rightFlagPos = pos1;
            }
            else {
                plot.leftFlagPos = pos1;
                plot.rightFlagPos = pos2;
            }
            plot.leftFlagTime = plot.dataSources[0].getClosestValidTime(plot.timeGeometry.fromScreen(plot.leftFlagPos));
            plot.rightFlagTime = plot.dataSources[0].getClosestValidTime(plot.timeGeometry.fromScreen(plot.rightFlagPos));
        }
        
        //Sets the divs corresponding to the actual chosen timeRange
        var setRangeDivs = function(){
            plot.leftFlagPole.style.visibility = "visible";
            plot.rightFlagPole.style.visibility = "visible";
            plot.rangeBox.style.visibility = "visible";
            if( STIStatic.popups ){
            	plot.popupClickDiv.style.visibility = "visible";
            }
            plot.timeplot.placeDiv(plot.leftFlagPole, {
                left: plot.leftFlagPos,
                bottom: 0,
                height: plot.canvas.height,
                display: "block"
            });
            plot.timeplot.placeDiv(plot.rightFlagPole, {
                left: plot.rightFlagPos,
                bottom: 0,
                height: plot.canvas.height,
                display: "block"
            });
            var boxWidth = plot.rightFlagPos - plot.leftFlagPos;
            plot.timeplot.placeDiv(plot.popupClickDiv, {
                left: plot.leftFlagPos,
                width: boxWidth + 1,
                height: plot.canvas.height,
                display: "block"
            });
            plot.timeplot.placeDiv(plot.rangeBox, {
                left: plot.leftFlagPos,
                width: boxWidth + 1,
                height: plot.canvas.height,
                display: "block"
            });
           	plot.setFeather();
           	displayFeather();
           	var plots = plot.timeplot._plots;
           	for (i = 0; i < plots.length; i++) {
               	plots[i].fullOpacityPlot(plot.leftFlagTime, plot.rightFlagTime, plot.leftFlagPos, plot.rightFlagPos, plot.leftFeatherTime, plot.rightFeatherTime, plot.featherWidth, STIStatic.colors[i]);
               	plots[i].opacityPlot.style.visibility = "visible";
           	}
        	var unit = plot.eds.unit;
			plot.leftHandle.innerHTML = SimileAjax.DateTime.getTimeLabel(unit,plot.leftFlagTime);
			plot.rightHandle.innerHTML = SimileAjax.DateTime.getTimeLabel(unit,plot.rightFlagTime);
        	var leftPos = plot.leftFlagPole.offsetLeft + plot.timeplot.getElement().offsetLeft;
        	var rightPos = plot.rightFlagPole.offsetLeft + plot.timeplot.getElement().offsetLeft;
        	
        	var top = plotContainer.offsetTop;
        	plot.leftHandle.style.visibility = "visible";
			plot.rightHandle.style.visibility = "visible";
        	plot.leftHandle.style.left = (leftPos-plot.leftHandle.offsetWidth/2)+"px";
        	plot.rightHandle.style.left = (rightPos-plot.rightHandle.offsetWidth+1+plot.rightHandle.offsetWidth/2)+"px";
        	plot.leftHandle.style.top = top+"px";     	
        	
        	if( rightPos == leftPos ){
    			plot.rightHandle.style.visibility = "hidden";
        	}        	
        	else if( rightPos-leftPos < plot.leftHandle.offsetWidth/2 + plot.rightHandle.offsetWidth/2 ){
				plot.rightHandle.style.top = (top+plot.rightHandle.offsetHeight)+"px";
        	}
        	else {
				plot.rightHandle.style.top = top+"px";
        	}

			var rW = rightPos-leftPos;
			var tW = plot.toolbarAbsoluteDiv.offsetWidth;
			var pW = plot.canvas.width;
			var pL = plot.timeplot.getElement().offsetLeft;
			if( rW >= tW ){
				plot.toolbar.style.left = leftPos+"px";
				plot.toolbar.style.width = (rW-1)+"px";
				plot.toolbarAbsoluteDiv.style.left = ((rW-tW)/2)+"px";
			} 
			else {
				plot.toolbar.style.left = (pL+plot.leftFlagPos*(pW-tW)/(pW-rW))+"px";
				plot.toolbar.style.width = tW+"px";
				plot.toolbarAbsoluteDiv.style.left = "0px";
			}
        	var topPos = top + plot.timeplot.getElement().offsetHeight;
			plot.toolbar.style.top = topPos+"px";
			plot.toolbar.style.visibility = "visible";
			plot.toolbarAbsoluteDiv.style.visibility = "visible";          
        }

		var startX, startY, multiplier;

		// mousemove function that causes moving selection of objects and toolbar divs		
		var moveToolbar = function(start,actual){
			var pixelShift = actual-start;
			if( plot.status == 2 ){
				var newTime = getCorrelatedTime(startX + pixelShift);
				if( newTime == plot.mouseTempTime ){
					return;
				}
				plot.mouseTempTime = newTime;
                plot.mouseTempPos = plot.timeGeometry.toScreen(plot.mouseTempTime);
                mapPositions(plot.mouseDownPos, plot.mouseTempPos);				
			}
			else if( plot.status == 3 ){
				pixelShift *= multiplier;
				var plotPos = actual - plot.timeplot.getElement().offsetLeft;
				if( plotPos <= plot.canvas.width/2 ){
					var newTime = getCorrelatedTime(startX + pixelShift);
					if( newTime == plot.leftFlagTime ){
						return;
					}
					plot.leftFlagTime = newTime;
					var diff = plot.leftFlagPos; 
	                plot.leftFlagPos = plot.timeGeometry.toScreen(plot.leftFlagTime);
	                diff -= plot.leftFlagPos;
	                plot.rightFlagTime = getCorrelatedTime(plot.rightFlagPos - diff);
	                plot.rightFlagPos = plot.timeGeometry.toScreen(plot.rightFlagTime);
				}
				else {
					var newTime = getCorrelatedTime(startY + pixelShift);
					if( newTime == plot.rightFlagTime ){
						return;
					}
					plot.rightFlagTime = newTime;
					var diff = plot.rightFlagPos; 
	                plot.rightFlagPos = plot.timeGeometry.toScreen(plot.rightFlagTime);
	                diff -= plot.rightFlagPos;
	                plot.leftFlagTime = getCorrelatedTime(plot.leftFlagPos - diff);
	                plot.leftFlagPos = plot.timeGeometry.toScreen(plot.leftFlagTime);                
				}
			}
			setRangeDivs();
           	plot.updateByTime();
		}

		// fakes user interaction mouse move
		var playIt = function(start,actual,reset){
			if( !plot.paused ){
				var pixel = plot.canvas.width / ( plot.eds.timeSlices.length - 1 ) / 5;
				var wait = 20 * pixel;
				if( reset ){
					actual = 0;
				}
				moveToolbar(start,actual);
				if( plot.rightFlagPos >= plot.canvas.width ){
					reset = true;
					wait = 1000;
				}
				else {
					reset = false;
				}
				setTimeout( function(){ playIt(start,actual+pixel,reset) }, wait );
			}
		}

		var deactivate;
		document.onclick = function(){
			if( plot.status > 1 ){
				if( deactivate ){
					plot.stop();
					document.onmousemove = null;
				}
				else {
					deactivate = true;
				}
			}
		}
		
		var setMultiplier = function(){
			var rangeWidth = plot.rightFlagPos - plot.leftFlagPos;
			var toolbarWidth = plot.toolbarAbsoluteDiv.offsetWidth;
			var plotWidth = plot.canvas.width;
			if( rangeWidth < toolbarWidth ){
				multiplier = (plotWidth-rangeWidth)/(plotWidth-toolbarWidth);
			}
			else {
				multiplier = 1;
			}
		}
		
    	/**
     	 * starts the animation
     	*/
		this.play = function(){
			if( this.leftFlagPos == null ){
				return;
			}
        	deactivate = false;
			plot.paused = false;
			plot.updateAnimationButton(2);
			plot.status = 3;
			setMultiplier();
			startX = plot.leftFlagPos;
			startY = plot.rightFlagPos;
			var position = Math.round(plot.leftFlagPos);
			playIt(position,position+1,false);
		}
		
    	/**
     	 * stops the animation
     	*/
		this.stop = function(){
			plot.paused = true;
			plot.status = 0;
	        drag.setAttribute('class','dragRange');
			plot.updateAnimationButton(1);
		}

		// triggers the mousemove function to move the range and toolbar
        var toolbarEvent = function(evt){
        	deactivate = false;
  			var left = STIStatic.getMousePosition(evt).left;
   			document.onmousemove = function(evt){
    			moveToolbar(left,STIStatic.getMousePosition(evt).left);
    			if( STIStatic.popups ){
    				plot.popup.reset();
    			}
      		}
        }
        
		// handles click on left handle
        this.leftHandle.onclick = function(evt){
        	if( plot.status != 2 ){
            	plot.mouseDownTime = plot.rightFlagTime;
            	plot.mouseTempTime = plot.leftFlagTime;
            	plot.mouseDownPos = plot.timeGeometry.toScreen(plot.mouseDownTime);
            	startX = plot.leftFlagPos; 
        		plot.status = 2;
				toolbarEvent(evt);
        	}
        }
        
		// handles click on right handle
        this.rightHandle.onclick = function(evt){
        	if( plot.status != 2 ){
            	plot.mouseDownTime = plot.leftFlagTime;
            	plot.mouseTempTime = plot.rightFlagTime;
            	plot.mouseDownPos = plot.timeGeometry.toScreen(plot.mouseDownTime);
            	startX = plot.rightFlagPos; 
        		plot.status = 2;
				toolbarEvent(evt);
        	}
        }
        
		// handles click on drag button
        drag.onclick = function(evt){
        	if( plot.status != 3 ){
        		plot.status = 3;
        		startX = plot.leftFlagPos;
        		startY = plot.rightFlagPos;
    			setMultiplier();
	        	drag.setAttribute('class','dragRangeClick');
				toolbarEvent(evt);
        	}
        }
                
        // handles mousedown-Event on timeplot
        var mouseDownHandler = function(elmt, evt, target){
        	if( plot.dataSources.length > 0 ){
            	var x = Math.round(SimileAjax.DOM.getEventRelativeCoordinates(evt, plot.canvas).x);
            	if (plot.status == 0 ) {
            		var time = getCorrelatedTime(x);
            		if( plot.leftFlagPos != null && STIStatic.popups && time >= plot.leftFlagTime && time <= plot.rightFlagTime ){
                		var elements = [];
                        var slices = plot.eds.timeSlices;
                        for (var i = 0; i < slices.length; i++) {
                        	if( slices[i].selected ){
                        		elements = elements.concat(slices[i].elements[0]);
                        	}
                        }
                        var x = plot.leftFlagPos+(plot.rightFlagPos-plot.leftFlagPos)/2;        		
                		plot.popup.createTimeplotPopup(x+20,elements);
            		}
            		else {
                    	plot.core.reset();
                    	plot.status = 1;
                    	plot.mouseDownTime = time;
                    	plot.mouseTempTime = plot.mouseDownTime;
                    	plot.mouseDownPos = plot.timeGeometry.toScreen(plot.mouseDownTime);
                    	mapPositions(plot.mouseDownPos, plot.mouseDownPos, plot.mouseDownTime, plot.mouseDownTime);
                        // handles mouseup-Event on timeplot
                    	document.onmouseup = function(){
                            if (plot.status == 1) {
                                plot.status = 0;
                                plot.mouseUpTime = plot.mouseTempTime;
                                plot.mouseUpPos = plot.timeGeometry.toScreen(plot.mouseUpTime);
                                mapPositions(plot.mouseDownPos, plot.mouseUpPos, plot.mouseDownTime, plot.mouseUpTime);
                                setRangeDivs();
                                plot.updateByTime();
            					plot.updateAnimationButton(1);
                                displayFeather();
                                document.onmouseup = null;
                            }
                    	}
            		}
            	}
            }
        }
        
        // handles mousemove-Event on timeplot
        var mouseMoveHandler = function(elmt, evt, target){        	
        	if( plot.dataSources.length > 0 ){
            	var x = Math.round(SimileAjax.DOM.getEventRelativeCoordinates(evt, plot.canvas).x);
            	if (plot.status == 1) {
                	plot.mouseTempTime = getCorrelatedTime(x);
                	plot.mouseTempPos = plot.timeGeometry.toScreen(plot.mouseTempTime);
                	mapPositions(plot.mouseDownPos, plot.mouseTempPos, plot.mouseDownTime, plot.mouseTempTime);
                	setRangeDivs();
            	}
            }
        }
		
        // handles mouseout-Event on timeplot
		var mouseOutHandler = function(elmt, evt, target){
        	if( plot.dataSources.length > 0 ){		
        		var x = Math.round(SimileAjax.DOM.getEventRelativeCoordinates(evt, plot.canvas).x);
        		var y = Math.round(SimileAjax.DOM.getEventRelativeCoordinates(evt, plot.canvas).y);
            	if (x > plot.canvas.width-2 || isNaN(x) || x < 2 )
					plot.hoverUnselect(true);
            	if (y > plot.canvas.height-2 || isNaN(y) || y < 2 )
					plot.hoverUnselect(true);
			}
		}

        // handles mouse(h)over-Event on timeplot
        var mouseHoverHandler = function(elmt, evt, target){
        	if( plot.dataSources.length > 0 ){
				plot.core.undoHover(false);
        		//plot.hoverUnselect(false);
				var x = Math.round(SimileAjax.DOM.getEventRelativeCoordinates(evt, plot.canvas).x);
				var time = getCorrelatedTime(x);
        		var slices = plot.eds.timeSlices;
				for (var i = 0; i < slices.length; i++) {
            		if (slices[i].date.getTime() == time.getTime()){
						if( plot.hoverSlice != undefined && plot.hoverSlice.date.getTime() == slices[i].date.getTime() ){
							return;						
						}
						plot.hoverSlice = slices[i];
            			var objects = slices[i].elements;
            			for (var j = 0; j < objects.length; j++){
                			for (var k = 0; k < objects[j].length; k++){
                       			objects[j][k].setHover(true);
							}
						}
						break;
					}
				}
        		plot.core.updateTableAndMap(true);
        	}
		}		

		this.redrawPlot = function(){
			plot.clearTimeplot();				
			plot.eds.reset(this.timeGeometry);
	       	plot.timeplot.repaint();
			if( plot.leftFlagPos != null ){
				plot.leftFlagPos = getCorrelatedPosition(plot.leftFlagTime);
				plot.rightFlagPos = getCorrelatedPosition(plot.rightFlagTime);
				setRangeDivs();
			}
			else {
				plot.polesBySlices(true);
			}				
			plot.initLabels([]);
			plot.updateOverview();
		}

    	/**
     	 * handles zoom of the timeplot
     	 * @param {int} delta the change of zoom
     	 * @param {Date} time a time that corresponds to a slice, that was clicked
     	*/
		this.zoom = function(delta,time){
			if( this.eventSources.length == 0 ){
				if( STIStatic.timeZoom ){
					this.zoomSlider.setValue(0);
				}
				return false;
			}
			if( time == null ){
				time = getCorrelatedTime(this.canvas.width/2);
			}
			if( this.eds.setZoom(delta,time,this.leftFlagTime,this.rightFlagTime) ){
				this.redrawPlot();
			}
			if( STIStatic.timeZoom ){
				this.zoomSlider.setValue(this.eds.getZoom());
			}
			return true;
		}		

		// handles mousewheel event on the timeplot		
		var mouseWheelHandler = function(elmt, evt, target){
			if (evt.preventDefault){
				evt.preventDefault();
			}
			if( plot.dataSources.length == 0 ){
				return;
			}
			var delta = 0;
			if (!evt) evt = window.event;
			if (evt.wheelDelta) {
				delta = evt.wheelDelta/120; 
				if (window.opera) delta = -delta;
			}
			else if (evt.detail) {
				delta = -evt.detail/3;
			}
			if (delta){
				var x = Math.round(SimileAjax.DOM.getEventRelativeCoordinates(evt, plot.canvas).x);
				var time = getCorrelatedTime(x);
				plot.zoom(delta,time);
			}		
		}
				
        var timeplotElement = this.timeplot.getElement();
        SimileAjax.DOM.registerEvent(timeplotElement, "mousedown", mouseDownHandler);
        SimileAjax.DOM.registerEvent(timeplotElement, "mousemove", mouseMoveHandler);
        SimileAjax.DOM.registerEvent(timeplotElement, "mousemove", mouseHoverHandler);
        SimileAjax.DOM.registerEvent(timeplotElement, "mouseout", mouseOutHandler);
        if( STIStatic.mouseWheelZoom ){
            SimileAjax.DOM.registerEvent(timeplotElement, "mousewheel", mouseWheelHandler);
        }
        
        this.setCanvas();
        
        if( tools.length > 0 ){
    		var timeplotToolbar = document.createElement("div");
    		timeplotToolbar.style.position = "absolute";
    		timeplotToolbar.setAttribute('class','toolbar'+toolbarStyle.h+' toolbar'+toolbarStyle.v+' toolbar'+toolbarStyle.h+''+toolbarStyle.s);
    		timeplotToolbar.style.position = "absolute";
    		timeplotToolbar.appendChild(STIStatic.createToolbar(tools));
    		container.appendChild(timeplotToolbar);
        }
        
    },
    
    /**
     * updates the data objects percentages after a selection on the timeplot had been performed
     */
    updateByTime: function(){
        this.setFeather();
        var slices = this.eds.timeSlices;
        var lfs, ls, rs, rfs;
        for (var i = 0; i < slices.length; i++) {
            if (slices[i].date.getTime() == this.leftFeatherTime.getTime()) 
                lfs = i;
            if (slices[i].date.getTime() == this.leftFlagTime.getTime()) 
                ls = i;
            if (slices[i].date.getTime() == this.rightFlagTime.getTime()) 
                rs = i;
            if (slices[i].date.getTime() == this.rightFeatherTime.getTime()) 
                rfs = i;
			if( slices[i].date.getTime() >= this.leftFlagTime.getTime() && slices[i].date.getTime() <= this.rightFlagTime.getTime() )
				slices[i].selected = true;
			else {
				slices[i].selected = false;
			}
        }
        for (var i = 0; i < slices.length; i++) {
            var objects = slices[i].elements;
            for (var j = 0; j < objects.length; j++) 
                for (var k = 0; k < objects[j].length; k++) 
                    if (i > lfs && i < ls) 
                        objects[j][k].setPercentage((i - lfs) / (ls - lfs));
                    else 
                        if (i >= ls && i <= rs)
                            objects[j][k].setPercentage(1);
                        else 
                            if (i > rs && i < rfs) 
                                objects[j][k].setPercentage((rfs - i) / (rfs - rs));
                            else 
                                objects[j][k].setPercentage(0);
        }
        this.core.updateTableAndMap(false);
    },
    
    /**
     * sets the background canvas of the timeplot window (or resets it after resizing the browser window)
     */
    setCanvas: function(){
        var cv = document.createElement("canvas");
        cv.id = "plotCanvas";     
        this.plotWindow.appendChild(cv);
        cv.width = this.plotWindow.clientWidth;
        cv.height = this.plotWindow.clientHeight;
        if (!cv.getContext && G_vmlCanvasManager) 
            cv = G_vmlCanvasManager.initElement(cv);
        var ctx = cv.getContext('2d');
        var gradient;
        if( STIStatic.lightScheme ){
        	gradient = ctx.createLinearGradient(0, 0, 0, this.canvas.height); 
	        gradient.addColorStop(0, '#c9c9cb');
	        gradient.addColorStop(1, '#ededed ');
        }
        else {
	        gradient = ctx.createLinearGradient(0, 0, this.canvas.width, 0);
	        gradient.addColorStop(0, 'rgb(136,136,136)');
	        gradient.addColorStop(0.5, 'rgb(206,206,206)');
	        gradient.addColorStop(1, 'rgb(136,136,136)');
        }
        ctx.fillStyle = gradient;
        ctx.fillRect(0, 0, this.plotWindow.clientWidth, this.plotWindow.clientHeight);
    },
    
    /**
     * resets the timeplot to non selection status
     */
    resetTimeplot: function(){
    
        this.leftFlagPole.style.visibility = "hidden";
        this.rightFlagPole.style.visibility = "hidden";
        this.rangeBox.style.visibility = "hidden";
        this.leftFeather.style.visibility = "hidden";
        this.rightFeather.style.visibility = "hidden";
        this.leftHandle.style.visibility = "hidden";
        this.rightHandle.style.visibility = "hidden";
        this.toolbar.style.visibility = "hidden";
        this.toolbarAbsoluteDiv.style.visibility = "hidden";
        this.poles.style.visibility = "hidden";
                
        var plots = this.timeplot._plots;
        for (var i = 0; i < plots.length; i++){
            plots[i].opacityPlot.style.visibility = "hidden";        	
        }
        
        var slices = this.eds.timeSlices;
        if( slices != undefined ){
        	for (var i = 0; i < slices.length; i++){
            	slices[i].reset();
            	slices[i].selected = false;
            }
        }

        this.status = 0;
        this.stop();
		document.onmousemove = null;
		this.updateAnimationButton(0);
        
        this.leftFlagPos = null;
        this.leftFlagTime = null;
        this.rightFlagPos = null;
        this.rightFlagTime = null;
        
        this.mouseDownTime = null;
        this.mouseUpTime = null;
        this.mouseTempTime = null;
        
        this.mouseDownPos = null;
        this.mouseUpPos = null;
        this.mouseTempPos = null;
        
        if( STIStatic.popups ){
        	this.popup.reset();
        	this.popupClickDiv.style.visibility = "hidden";
        }

    },
    
    /**
     * sets a pole on the timeplot
     * @param {Date} time the time of the specific timeslice
     * @param {int[]} the number of selected elements per dataset
     */
	setPoles: function(){	
        this.poles.style.visibility = "visible";
        var cv = this.poles.getElementsByTagName("canvas")[0];
        cv.width = this.canvas.width;
        cv.height = this.canvas.height;
        if (!cv.getContext && G_vmlCanvasManager){
            cv = G_vmlCanvasManager.initElement(cv);
        } 
        var ctx = cv.getContext('2d');
        ctx.clearRect(0,0,this.canvas.width,this.canvas.height);
		var plots = this.timeplot._plots;
        var slices = this.eds.timeSlices;
		for( var i=0; i<slices.length; i++ ){
			if( slices[i].overlay() == 0 ){
				continue;
			}
			var overlays = slices[i].overlays;
			var values = slices[i].values;
			var time = slices[i].date;
	        var pos = this.timeGeometry.toScreen(time);
			var heights = [];
			var h = 0;
			for ( var j = 0; j < overlays.length; j++) {
				var data = plots[j]._dataSource.getData();
				for ( var k = 0; k < data.times.length; k++){ 
					if (data.times[k].getTime() == time.getTime()) {
						var height = plots[j]._valueGeometry.toScreen(plots[j]._dataSource.getData().values[k]) * overlays[j] / values[j];
						heights.push(height);
						if( height > h ){
							h = height;
						}
						break;
					}
				}
			}
			ctx.fillStyle = "rgb(102,102,102)";
        	ctx.beginPath();
			ctx.rect(pos-1,100-h,2,h);
      		ctx.fill();
			for( var j=0; j<overlays.length; j++ ){
				if( heights[j] > 0 ){
					ctx.fillStyle = "rgba("+STIStatic.colors[j].r1+","+STIStatic.colors[j].g1+","+STIStatic.colors[j].b1+",0.6)";
					ctx.beginPath();
					ctx.arc(pos, 100-heights[j], 2.5, 0, Math.PI*2, true);
					ctx.closePath();
					ctx.fill();
				}
			}
		}
	},
	
    /**
     * updates the timeplot by setting place poles, after selections had been executed in map or table.
     * its called by the core component.
     */
    polesBySlices: function(scroll){
        var slices = this.eds.timeSlices;
        for (var i = 0; i < slices.length; i++) {
           	slices[i].reset();
			for (var j = 0; j < slices[i].elements.length; j++){
				for (var k = 0; k < slices[i].elements[j].length; k++){
					if ( ( !slices[i].selected && slices[i].elements[j][k].percentage == 1 ) || ( slices[i].elements[j][k].hoverSelect && !scroll ) ) {
						slices[i].overlays[j] += slices[i].elements[j][k].weight;
					}
				}
			}
		}
		this.setPoles();
    },
    
    /**
     * updates the timeplot by displaying place poles, after a selection had been executed in another widget
     */
    proportionalSelection: function(timeObjects){
        var slices = this.eds.timeSlices;
        for (var i = 0; i < slices.length; i++) {
           	slices[i].reset();
        }
		for(var i=0; i<timeObjects.length; i++ ){
            for (var j=0; j < timeObjects[i].length; j++){
                var o = timeObjects[i][j];
                var timeMin = o.timeStart;
                var timeMax = o.timeEnd;
				for( var k=0; k<slices.length-1; k++ ){
					var t1 = slices[k].date.getTime();
					var t2 = slices[k+1].date.getTime();
					if( ( timeMin >= t1 && timeMin < t2 ) || ( timeMax >= t1 && timeMax < t2 ) || ( timeMin <= t1 && timeMax >= t2 ) ){
						slices[k].overlays[i] += o.weight;
					}
					if( k == slices.length-2 && ( timeMin >= t2 || timeMax >= t2 ) ){
						slices[k+1].overlays[i] += o.weight;
					}
				}
			}
		}
		this.setPoles();
    },       

    /**
     * calculates the new feather bounds
     */
    setFeather: function(){
		this.leftFeatherTime = this.leftFlagTime; 
		this.rightFeatherTime = this.rightFlagTime; 
        this.featherWidth = Math.floor(this.canvas.width / this.eds.timeSlices.length * this.slider.getValue());
        var slices = this.eds.timeSlices;
        for (var i = 0; i < slices.length; i++) {
            if (slices[i].date.getTime() == this.leftFlagTime.getTime()) {
                if (i - this.slider.getValue() >= 0) 
                    this.leftFeatherTime = slices[i - this.slider.getValue()].date;
                else 
                    this.leftFeatherTime = slices[0].date;
            }
            if (slices[i].date.getTime() == this.rightFlagTime.getTime()) {
                if (i + this.slider.getValue() < slices.length) 
                    this.rightFeatherTime = slices[i + this.slider.getValue()].date;
                else 
                    this.rightFeatherTime = slices[slices.length - 1].date;
            }
        }
    },
	
    /**
     * undo hover selection of elements in the hovered slice
     */
	hoverUnselect: function(update){
		if( this.hoverSlice != undefined ){
   			var objects = this.hoverSlice.elements;
   			for (var j = 0; j < objects.length; j++){
       			for (var k = 0; k < objects[j].length; k++){
           			objects[j][k].setHover(false);
				}
			}
			this.hoverSlice = undefined;
			if( update ){
        		this.core.updateTableAndMap(true);
        	}
		}
	},
	
    /**
     * returns the approximate left position of a slice inside the overview representation
     * @param {Date} time time of the slice
     */
	getOverviewLeft: function(time){
		var w = this.overview.offsetWidth;
		var s = this.eds.earliest().getTime();
		var e = this.eds.latest().getTime();
		var t = time.getTime();		
		return Math.round(w*(t-s)/(e-s));
	},
	
    /**
     * visualizes the overview div (shows viewable part of zoomed timeplot)
     */
	initOverview: function(){
        var labels = this.timeGeometry._grid;
        if( labels.length == 0 ){
        	var plot = this;
        	setTimeout( function(){ plot.initOverview(); }, 10 );
        	return;
        }

        this.overview.style.width = this.canvas.width+"px";
        var left = this.timeplotDiv.offsetLeft;
		this.overview.innerHTML = "";
        this.overview.style.left = left+"px";

        this.overviewRange = document.createElement("div");
        this.overviewRange.setAttribute('class','overviewRange');
        this.overview.appendChild(this.overviewRange);

        for( var i=0; i<labels.length; i++ ){
        	var label = document.createElement("div");
        	label.setAttribute('class','overviewLabel');
        	label.innerHTML = labels[i].label;
        	label.style.left = Math.floor(labels[i].x)+"px";
        	this.overview.appendChild(label);
        }

		this.updateOverview();
	},

    /**
     * visualizes the labels of the timeplot
     */
	initLabels: function(labels){
		if( labels.length == 0 ){
	        labels = this.timeGeometry._grid;
    	    if( labels.length == 0 ){
        		var plot = this;
        		setTimeout( function(){ plot.initLabels([]); }, 10 );
        		return;
        	}
        }
        this.plotLabels.style.width = this.canvas.width+"px";
        var left = this.timeplotDiv.offsetLeft;
        this.plotLabels.style.left = left+"px";
		this.plotLabels.innerHTML = "";
        for( var i=0; i<labels.length; i++ ){
        	var label = document.createElement("div");
        	label.setAttribute('class','plotLabel');
        	label.innerHTML = labels[i].label;
        	label.style.left = Math.floor(labels[i].x)+"px";
        	this.plotLabels.appendChild(label);
        }
	},	
	
    /**
     * updates the overview div
     */
	updateOverview: function(){
		if( this.eds.getZoom() > 0 ){
			this.plotLabels.style.visibility = "hidden";
			this.timeGeometry._hideLabels = false;
			this.overview.style.visibility = "visible";
			this.shiftLeft.style.visibility = "visible";
			this.shiftRight.style.visibility = "visible";
			var left = this.getOverviewLeft( this.eds.timeSlices[this.eds.leftSlice].date );	
			var right = this.getOverviewLeft( this.eds.timeSlices[this.eds.rightSlice].date );
			this.overviewRange.style.left = left+"px";	
			this.overviewRange.style.width = (right-left)+"px";
		}
		else {
			this.timeGeometry._hideLabels = true;
			this.plotLabels.style.visibility = "visible";
			this.overview.style.visibility = "hidden";
			this.shiftLeft.style.visibility = "hidden";
			this.shiftRight.style.visibility = "hidden";
		}
	},
	
    /**
     * returns the time slices which are created by the extended data source
     */
	getSlices: function(){
		return this.eds.timeSlices;
	},
	
	initializeTimeplotTools: function(){
		var timeplotTools = [];
		if( STIStatic.animationControl ){
	    	this.animation = document.createElement("img");
	    	timeplotTools.push(this.animation);
	    	this.updateAnimationButton(0);
		}
		if( STIStatic.timeZoom ){
			this.zoomSlider = new ZoomSlider('timeZoom',this);
			this.zoomSlider.setMaxAndLevels(100,100);
			this.zoomSlider.setValue(0);
			timeplotTools.push(this.zoomSlider.div);
		}
		if( STIStatic.timeZoom || STIStatic.animationControl ){
			var logo = document.createElement("img");
			logo.src = STIStatic.path+"time.png";
			logo.title = "Timeplot Tools";
			timeplotTools.push(logo);
		}
    	return timeplotTools;
	},
	
	updateAnimationButton: function(status){
		if( !STIStatic.animationControl ){
			return;
		}
		if( status == 0 ){
			this.animation.src = STIStatic.path+"play_0.png";
    		this.animation.title = "Animation Control for a selected time range";
    	}
    	else if( status == 1 ){
			this.animation.src = STIStatic.path+"play_2.png";
    		this.animation.title = "Animate selected time range";
    	}
    	else if( status == 2 ){
			this.animation.src = STIStatic.path+"pause_2.png";
    		this.animation.title = "Pause Animation";
    	}
    	var plot = this;
    	this.animation.onclick = function(){
    		if( status == 1 ){
    			if( STIStatic.popups ){
    				plot.popup.reset();
    			}
    			plot.play();
    		}
    		else if( status == 2 ){
    			plot.stop();
    		}
    	}    	
	}
	
}
