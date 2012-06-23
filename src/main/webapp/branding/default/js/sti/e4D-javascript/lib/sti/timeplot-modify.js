Timeplot.DefaultEventSource.prototype.loadData = function(events){

    this._events.maxValues = new Array();
    this._events.removeAll();
    for (var i = 0; i < events.length; i++) {
        var event = events[i];
        var numericEvent = new Timeplot.DefaultEventSource.NumericEvent(event.date, event.value);
        this._events.add(numericEvent);
    }
    this._fire("onAddMany", []);
    
};

Timeplot._Impl.prototype.resetPlots = function(plotInfos){

    this._plotInfos = plotInfos;
    this._painters = {
        background: [],
        foreground: []
    };
    this._painter = null;
    
    var timeplot = this;
    var painter = {
        onAddMany: function(){
            timeplot.update();
        },
        onClear: function(){
            timeplot.update();
        }
    }
    
    for (i = this._plots.length; i > 0; i--) {
        this._plots[i-1].opacityPlot.removeChild( this._plots[i-1]._opacityCanvas );
        this._plots[i - 1].dispose();
        if (document.addEventListener) {
            this._containerDiv.removeEventListener("mousemove", this._plots[i - 1].mousemove, false);
            this._containerDiv.removeEventListener("mouseover", this._plots[i - 1].mouseover, false);
        }
        else 
            if (document.attachEvent) {
                this._containerDiv.detachEvent("onmousemove", this._plots[i - 1].mousemove);
                this._containerDiv.detachEvent("onmouseover", this._plots[i - 1].mouseover);
            }
        delete this._plots[i - 1];
    }
    
    this._plots = [];
    
    for (i = 0; i < this._plotInfos.length; i++) {
        var plot = new Timeplot.Plot(this, this._plotInfos[i]);
        var dataSource = plot.getDataSource();
        if (dataSource) {
            dataSource.addListener(painter);
        }
        this.addPainter("background", {
            context: plot.getTimeGeometry(),
            action: plot.getTimeGeometry().paint
        });
        this.addPainter("background", {
            context: plot.getValueGeometry(),
            action: plot.getValueGeometry().paint
        });
        this.addPainter("foreground", {
            context: plot,
            action: plot.paint
        });
        this._plots.push(plot);
        plot.initialize();
    }
    
};

Timeplot.DefaultTimeGeometry.prototype._calculateGrid = function() {
    var grid = [];
    
    var time = SimileAjax.DateTime;
    var u = this._unit;
    var p = this._period;

    if (p == 0) 
        return grid;
    
    var periodUnit = -1;
    do {
        periodUnit++;
    }
    while (time.gregorianUnitLengths[periodUnit] < p);
    
    var unit;
    if (periodUnit < time.HALFMINUTE) {
		unit = time.SECOND;
    }
    else if (periodUnit < time.WEEK) {
    	unit = periodUnit - 2;
    }
    else {
        unit = periodUnit - 3;
    }
    
    if( unit < this._granularity ){
    	unit = this._granularity;
    }
    
    var t = u.cloneValue(this._earliestDate);
    var timeZone;
   	do {
		time.roundDownToInterval(t, unit, timeZone, 1, 0);
       	var x = this.toScreen(u.toNumber(t));
       	var l = SimileAjax.DateTime.getTimeLabel(unit,t);
        if (x > 0) { 
           	grid.push({ x: x, label: l });
        }
        time.incrementByInterval(t, unit, timeZone);
	} while (t.getTime() < this._latestDate.getTime());

    return grid;
    
};

//modified function to prevent from drawing left and right axis
Timeplot.DefaultValueGeometry.prototype.paint = function() {
        if (this._timeplot) {
            var ctx = this._canvas.getContext('2d');
    
            ctx.lineJoin = 'miter';
    
            // paint grid
            if (this._gridColor) {        
                var gridGradient = ctx.createLinearGradient(0,0,0,this._canvas.height);
                gridGradient.addColorStop(0, this._gridColor.toHexString());
                gridGradient.addColorStop(0.3, this._gridColor.toHexString());
                gridGradient.addColorStop(1, "rgba(255,255,255,0.5)");

                ctx.lineWidth = this._gridLineWidth;
                ctx.strokeStyle = gridGradient;
    
                for (var i = 0; i < this._grid.length; i++) {
                    var tick = this._grid[i];
                    var y = Math.floor(tick.y) + 0.5;
                    if (typeof tick.label != "undefined") {
                        if (this._axisLabelsPlacement == "left") {
                            var div = this._timeplot.putText(this._id + "-" + i, tick.label,"timeplot-grid-label",{
                                left: 4,
                                bottom: y + 2,
                                color: this._gridColor.toHexString(),
                                visibility: "hidden"
                            });
                            this._labels.push(div);
                        } else if (this._axisLabelsPlacement == "right") {
                            var div = this._timeplot.putText(this._id + "-" + i, tick.label, "timeplot-grid-label",{
                                right: 4,
                                bottom: y + 2,
                                color: this._gridColor.toHexString(),
                                visibility: "hidden"
                            });
                            this._labels.push(div);
                        }
                        if (y + div.clientHeight < this._canvas.height + 10) {
                            div.style.visibility = "visible"; // avoid the labels that would overflow
                        }
                    }

                    // draw grid
                    ctx.beginPath();
                    if (this._gridType == "long" || tick.label == 0) {
                        ctx.moveTo(0, y);
                        ctx.lineTo(this._canvas.width, y);
                    } else if (this._gridType == "short") {
                        if (this._axisLabelsPlacement == "left") {
                            ctx.moveTo(0, y);
                            ctx.lineTo(this._gridShortSize, y);
                        } else if (this._axisLabelsPlacement == "right") {
                            ctx.moveTo(this._canvas.width, y);
                            ctx.lineTo(this._canvas.width - this._gridShortSize, y);
                        }                       
                    }
                    ctx.stroke();
                }
            }
        }
    };
    
//modified function to prevent from drawing hidden labels
Timeplot.DefaultTimeGeometry.prototype.paint = function() {
        if (this._canvas) {
            var unit = this._unit;
            var ctx = this._canvas.getContext('2d');
    
            var gradient = ctx.createLinearGradient(0,0,0,this._canvas.height);
    
            ctx.strokeStyle = gradient;
            ctx.lineWidth = this._gridLineWidth;
            ctx.lineJoin = 'miter';
    
            // paint grid
            if (this._gridColor) {        
                gradient.addColorStop(0, this._gridColor.toString());
                gradient.addColorStop(1, "rgba(255,255,255,0.9)");
                for (var i = 0; i < this._grid.length; i++) {
                    var tick = this._grid[i];
                    var x = Math.floor(tick.x) + 0.5;
                    if (this._axisLabelsPlacement == "top") {
                        var div = this._timeplot.putText(this._id + "-" + i, tick.label,"timeplot-grid-label",{
                            left: x + 4,
                            top: 2,
                            visibility: "hidden"
                        });
                        this._labels.push(div);
                    } else if (this._axisLabelsPlacement == "bottom") {
                        var div = this._timeplot.putText(this._id + "-" + i, tick.label, "timeplot-grid-label",{
                            left: x + 4,
                            bottom: 2,
                            visibility: "hidden"
                        });
                        this._labels.push(div);
                    }
                    if (!this._hideLabels && x + div.clientWidth < this._canvas.width + 10) {
                        div.style.visibility = "visible"; // avoid the labels that would overflow
                    }

                    // draw separator
                    ctx.beginPath();
                    ctx.moveTo(x,0);
                    ctx.lineTo(x,this._canvas.height);
                    ctx.stroke();
                }
            }    
        }
    };    

    Timeplot.Plot.prototype.initialize = function(){
        if (this._dataSource && this._dataSource.getValue) {
            this._timeFlag = this._timeplot.putDiv("timeflag", "timeplot-timeflag");
            this._valueFlag = this._timeplot.putDiv(this._id + "valueflag", "timeplot-valueflag");
            this._valueFlagLineLeft = this._timeplot.putDiv(this._id + "valueflagLineLeft", "timeplot-valueflag-line");
            this._valueFlagLineRight = this._timeplot.putDiv(this._id + "valueflagLineRight", "timeplot-valueflag-line");
            if (!this._valueFlagLineLeft.firstChild) {
                this._valueFlagLineLeft.appendChild(SimileAjax.Graphics.createTranslucentImage(Timeplot.urlPrefix + "images/line_left.png"));
                this._valueFlagLineRight.appendChild(SimileAjax.Graphics.createTranslucentImage(Timeplot.urlPrefix + "images/line_right.png"));
            }
            this._valueFlagPole = this._timeplot.putDiv(this._id + "valuepole", "timeplot-valueflag-pole");
            
            var opacity = this._plotInfo.valuesOpacity;
            
            SimileAjax.Graphics.setOpacity(this._timeFlag, opacity);
            SimileAjax.Graphics.setOpacity(this._valueFlag, opacity);
            SimileAjax.Graphics.setOpacity(this._valueFlagLineLeft, opacity);
            SimileAjax.Graphics.setOpacity(this._valueFlagLineRight, opacity);
            SimileAjax.Graphics.setOpacity(this._valueFlagPole, opacity);
            
            var plot = this;
            
            var mouseOverHandler = function(elmt, evt, target){
                plot._timeFlag.style.visibility = "visible";
                plot._valueFlag.style.visibility = "visible";
                plot._valueFlagLineLeft.style.visibility = "visible";
                plot._valueFlagLineRight.style.visibility = "visible";
                plot._valueFlagPole.style.visibility = "visible";
                if (plot._plotInfo.showValues) {
                    plot._valueFlag.style.display = "block";
                    mouseMoveHandler(elmt, evt, target);
                }
            }
            
            var mouseOutHandler = function(elmt, evt, target){
                plot._timeFlag.style.visibility = "hidden";
                plot._valueFlag.style.visibility = "hidden";
                plot._valueFlagLineLeft.style.visibility = "hidden";
                plot._valueFlagLineRight.style.visibility = "hidden";
                plot._valueFlagPole.style.visibility = "hidden";
            }
            
            var day = 24 * 60 * 60 * 1000;
            var month = 30 * day;
            
            var mouseMoveHandler = function(elmt, evt, target){
                if (typeof SimileAjax != "undefined" && plot._plotInfo.showValues) {
                    var c = plot._canvas;
                    var x = Math.round(SimileAjax.DOM.getEventRelativeCoordinates(evt, plot._canvas).x);
                    if (x > c.width) 
                        x = c.width;
                    if (isNaN(x) || x < 0) 
                        x = 0;
                    var t = plot._timeGeometry.fromScreen(x);
                    if (t == 0) { // something is wrong
                        plot._valueFlag.style.display = "none";
                        return;
                    }
                    
                    var validTime = plot._dataSource.getClosestValidTime(t);
                    x = plot._timeGeometry.toScreen(validTime);
                    var v = plot._dataSource.getValue(validTime);
                    if (plot._plotInfo.roundValues) 
                        v = Math.round(v);
                    plot._valueFlag.innerHTML = v;
                    
                    var t = new Date(validTime);
                    var unit = plot._timeGeometry.extendedDataSource.unit;
                    var time = SimileAjax.DateTime;
       				var l = SimileAjax.DateTime.getTimeLabel(unit,t);
                    
                    plot._timeFlag.innerHTML = l;
                    
                    var tw = plot._timeFlag.clientWidth;
                    var th = plot._timeFlag.clientHeight;
                    var tdw = Math.round(tw / 2);
                    var vw = plot._valueFlag.clientWidth;
                    var vh = plot._valueFlag.clientHeight;
                    var y = plot._valueGeometry.toScreen(v);
                    
                    if (x + tdw > c.width) {
                        var tx = c.width - tdw;
                    }
                    else 
                        if (x - tdw < 0) {
                            var tx = tdw;
                        }
                        else {
                            var tx = x;
                        }
                    
                    if (plot._timeGeometry._timeValuePosition == "top") {
                        plot._timeplot.placeDiv(plot._valueFlagPole, {
                            left: x,
                            top: th - 5,
                            height: c.height - y - th + 6,
                            display: "block"
                        });
                        plot._timeplot.placeDiv(plot._timeFlag, {
                            left: tx - tdw,
                            top: -6,
                            display: "block"
                        });
                    }
                    else {
                        plot._timeplot.placeDiv(plot._valueFlagPole, {
                            left: x,
                            bottom: 0,
                            height: c.height,
                            display: "block"
                        });
                        plot._timeplot.placeDiv(plot._timeFlag, {
                            left: tx - tdw,
                            bottom: 0,
                            display: "block"
                        });
                    }
                    
                    if (x + vw + 14 > c.width && y + vh + 4 > c.height) {
                        plot._valueFlagLineLeft.style.display = "none";
                        plot._timeplot.placeDiv(plot._valueFlagLineRight, {
                            left: x - 14,
                            bottom: y - 14,
                            display: "block"
                        });
                        plot._timeplot.placeDiv(plot._valueFlag, {
                            left: x - vw - 13,
                            bottom: y - vh - 13,
                            display: "block"
                        });
                    }
                    else 
                        if (x + vw + 14 > c.width && y + vh + 4 < c.height) {
                            plot._valueFlagLineRight.style.display = "none";
                            plot._timeplot.placeDiv(plot._valueFlagLineLeft, {
                                left: x - 14,
                                bottom: y,
                                display: "block"
                            });
                            plot._timeplot.placeDiv(plot._valueFlag, {
                                left: x - vw - 13,
                                bottom: y + 13,
                                display: "block"
                            });
                        }
                        else 
                            if (x + vw + 14 < c.width && y + vh + 4 > c.height) {
                                plot._valueFlagLineRight.style.display = "none";
                                plot._timeplot.placeDiv(plot._valueFlagLineLeft, {
                                    left: x,
                                    bottom: y - 13,
                                    display: "block"
                                });
                                plot._timeplot.placeDiv(plot._valueFlag, {
                                    left: x + 13,
                                    bottom: y - 13,
                                    display: "block"
                                });
                            }
                            else {
                                plot._valueFlagLineLeft.style.display = "none";
                                plot._timeplot.placeDiv(plot._valueFlagLineRight, {
                                    left: x,
                                    bottom: y,
                                    display: "block"
                                });
                                plot._timeplot.placeDiv(plot._valueFlag, {
                                    left: x + 13,
                                    bottom: y + 13,
                                    display: "block"
                                });
                            }
                }
            }
            
            var timeplotElement = this._timeplot.getElement();
            this.mouseover = SimileAjax.DOM.registerPlotEvent(timeplotElement, "mouseover", mouseOverHandler);
            this.mouseout = SimileAjax.DOM.registerPlotEvent(timeplotElement, "mouseout", mouseOutHandler);
            this.mousemove = SimileAjax.DOM.registerPlotEvent(timeplotElement, "mousemove", mouseMoveHandler);
            
            this.opacityPlot = this._timeplot.putDiv( "opacityPlot"+this._timeplot._plots.length, "opacityPlot" );
            SimileAjax.Graphics.setOpacity(this.opacityPlot, 50 );
//		this.opacityPlot.style.zIndex = this._timeplot._plots.length;
            this._timeplot.placeDiv( this.opacityPlot, { left: 0, bottom: 0, width: this._canvas.width, height: this._canvas.height });
            this._opacityCanvas = document.createElement("canvas");
            this.opacityPlot.appendChild(this._opacityCanvas);
            if(!this._opacityCanvas.getContext && G_vmlCanvasManager)
                this._opacityCanvas = G_vmlCanvasManager.initElement(this._opacityCanvas);
  		    this._opacityCanvas.width = this._canvas.width;
            this._opacityCanvas.height = this._canvas.height;
			this.opacityPlot.style.visibility = "hidden";
			
        }
    };

    SimileAjax.DOM.registerPlotEvent = function(elmt, eventName, handler){
        var handler2 = function(evt){
            evt = (evt) ? evt : ((event) ? event : null);
            if (evt) {
                var target = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
                if (target) {
                    target = (target.nodeType == 1 || target.nodeType == 9) ? target : target.parentNode;
                }
                
                return handler(elmt, evt, target);
            }
            return true;
        }
        
        if (SimileAjax.Platform.browser.isIE) {
            elmt.attachEvent("on" + eventName, handler2);
        }
        else {
            elmt.addEventListener(eventName, handler2, false);
        }
        
        return handler2;
    };
    
SimileAjax.DOM.getEventRelativeCoordinates = function(evt, elmt) {
    if (SimileAjax.Platform.browser.isIE) {
        var coords = SimileAjax.DOM.getPageCoordinates(elmt);
        return {
          x: evt.clientX - coords.left, 
          y: evt.clientY - coords.top
        };        
    } else {
        var coords = SimileAjax.DOM.getPageCoordinates(elmt);

        if ((evt.type == "DOMMouseScroll") &&
          SimileAjax.Platform.browser.isFirefox &&
          (SimileAjax.Platform.browser.majorVersion == 2)) {
          // Due to: https://bugzilla.mozilla.org/show_bug.cgi?id=352179                  

          return {
            x: evt.screenX - coords.left,
            y: evt.screenY - coords.top 
          };
        } else {
          return {
              x: evt.pageX - coords.left,
              y: evt.pageY - coords.top
          };
        }
    }
};	

SimileAjax.Graphics.setOpacity = function(elmt, opacity) {
    if (SimileAjax.Platform.browser.isIE) {
        elmt.style.filter = "alpha(opacity = " + opacity + ")";
    } else {
        var o = (opacity / 100).toString();
        elmt.style.opacity = o;
        elmt.style.MozOpacity = o;
    }
};

	Timeplot.Plot.prototype.fullOpacityPlot = function( left, right, lp, rp, lft, rft, pixel, c ) {

        var ctx = this._opacityCanvas.getContext('2d');

        ctx.clearRect(0,0,this._canvas.width,this._canvas.height);
        ctx.lineWidth = this._plotInfo.lineWidth;
        ctx.lineJoin = 'miter';

        var h = this._canvas.height;
        ctx.fillStyle = this._plotInfo.lineColor.toString();
		
       	var data = this._dataSource.getData();
       	var times = data.times;
       	var values = data.values;
		var first;

		if( lft != null && lft.getTime() != left.getTime() ){
			var gradient = ctx.createLinearGradient(lp-pixel, 0, lp, 0);
        	gradient.addColorStop(0, 'rgba('+c.r1+','+c.g1+','+c.b1+',0)');
        	gradient.addColorStop(1, 'rgba('+c.r1+','+c.g1+','+c.b1+',1)');
			first = true;
	        ctx.beginPath();
       		for (var t = 0; t < times.length; t++){
				var x = this._timeGeometry.toScreen(times[t]);
				var y = this._valueGeometry.toScreen(values[t]);
				if( times[t].getTime() == left.getTime() ){
					ctx.lineTo(x, h - y);
        			ctx.lineTo(x, h);
        			ctx.fillStyle = gradient;
					ctx.fill();
					break;
				}
				else if ( times[t].getTime() >= lft.getTime() ) {
					if (first) {
						ctx.moveTo(x, h);
						first = false;
					}
					ctx.lineTo(x, h - y);	
				}
			}			
		}
		
		if( rft != null && rft.getTime() != right.getTime() ){
			var gradient = ctx.createLinearGradient(rp, 0, rp+pixel, 0);
        	gradient.addColorStop(0, 'rgba('+c.r1+','+c.g1+','+c.b1+',1)');
        	gradient.addColorStop(1, 'rgba('+c.r1+','+c.g1+','+c.b1+',0)');
			first = true;
	        ctx.beginPath();
       		for (var t = 0; t < times.length; t++){
				var x = this._timeGeometry.toScreen(times[t]);
				var y = this._valueGeometry.toScreen(values[t]);
				if( times[t].getTime() == rft.getTime() ){
					ctx.lineTo(x, h - y);
					ctx.lineTo(x, h);
        			ctx.fillStyle = gradient;
					ctx.fill();
					break;
				}
				else if ( times[t].getTime() >= right.getTime() ) {
					if (first) {
						ctx.moveTo(x, h);
						first = false;
					}
					ctx.lineTo(x, h - y);	
				}
			}			
		}

		first = true;
        ctx.beginPath();
        ctx.fillStyle = this._plotInfo.lineColor.toString();
       	for (var t = 0; t < times.length; t++)
			if (!(times[t].getTime() < left.getTime() || times[t].getTime() > right.getTime())) {
				var x = this._timeGeometry.toScreen(times[t]);
				var y = this._valueGeometry.toScreen(values[t]);
				if (first) {
					ctx.moveTo(x, h);
					first = false;
				}
				ctx.lineTo(x, h - y);
				if (times[t].getTime() == right.getTime() || t == times.length - 1 ) 
					ctx.lineTo(x, h);
			}
		ctx.fill();

    };
    
    Timeplot._Impl.prototype.regularGrid = function(){
    
    	var canvas = this.getCanvas();
        var ctx = canvas.getContext('2d');
		var gradient = ctx.createLinearGradient(0,0,0,canvas.height);
		gradient.addColorStop(0, "rgb(0,0,0)");
		gradient.addColorStop(1, "rgba(255,255,255,0.9)");
		ctx.strokeStyle = gradient;
		ctx.lineWidth = 0.5;
		ctx.lineJoin = 'miter';

		var xDist = canvas.width / 9;
		var positions = [];
		for( var i=1; i<9; i++ ){
			var x = i*xDist;
			ctx.beginPath();
			ctx.moveTo(x,0);
			ctx.lineTo(x,canvas.height);
			ctx.stroke();
			positions.push({ label: '', x: x });
		}
		return positions; 
    	
    };

    Timeplot.Plot.prototype._plot = function(f) {
    	var ctx = this._canvas.getContext('2d');
    	f = function(x,y,draw) {
			if( draw ){
				ctx.lineTo(x,y);
			}
			else {
				ctx.moveTo(x,y);
			}			
        };
        var data = this._dataSource.getData();
        if (data) {
            var times = data.times;
            var values = data.values;
            var T = times.length;
			ctx.moveTo(0,0);
            for (var t = 0; t < T; t++) {
                var x = this._timeGeometry.toScreen(times[t]);
                var y = this._valueGeometry.toScreen(values[t]);
                var draw = false;
                if( t > 0 && ( values[t-1] > 0 || values[t] > 0 ) ){
                	draw = true;
                }
                if( t == 0 ){
                	draw = true;
                }
                f(x,y,draw);
            }
        }
    };
    
SimileAjax.DOM.registerEvent = function(elmt, eventName, handler) {
    var handler2 = function(evt) {
        evt = (evt) ? evt : ((event) ? event : null);
        if (evt) {
            var target = (evt.target) ? 
                evt.target : ((evt.srcElement) ? evt.srcElement : null);
            if (target) {
                target = (target.nodeType == 1 || target.nodeType == 9) ? 
                    target : target.parentNode;
            }
            
            return handler(elmt, evt, target);
        }
        return true;
    }
    
    if (SimileAjax.Platform.browser.isIE) {
        elmt.attachEvent("on" + eventName, handler2);
    } else {    
    	if( eventName == "mousewheel" ){
    		eventName = "DOMMouseScroll";
    	}
        elmt.addEventListener(eventName, handler2, false);
    }
};
    