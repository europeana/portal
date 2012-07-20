/**
 * defines the map component of the Spatio Temporal Interface.
 * it builds a map context with the OpenLayers JavaScript Framework
 * @param {STICore} core the sti core component, the map component has to deal with
 * @param {String} container the div for the container of the map widget
 *
 * @constructor
 */

function STIMap(core,containerId,toolbarStyle){

    this.core = core;
    this.openlayersMap;
    this.baseLayers;
    this.objectLayer;
    this.drilldownLayer;
    this.connectionLayer;
    
    this.drawPolygon;
    this.drawCircle;
    this.selectCountry;
    this.dragArea;
    this.selectFeature;
    this.navigation;
    
    this.polygon;
    this.displayPointSet;
    
    this.lastHovered;
    this.showConnections;
    this.connections;
    
    this.initialize(containerId,toolbarStyle);
}

STIMap.prototype = {

    /**
     * initializes the map for the Spatio Temporal Interface.
     * it includes setting up all layers of the map and defines all map specific interaction possibilities
     */
    initialize: function(containerId,toolbarStyle){
        var map					= this;
		this.pointSelected		= false;
        this.showConnections	= false;
        this.labelDivs			= [];
        this.polygons			= [];
		var tools				= this.initializeMapTools();
        			
		var container = document.getElementById(containerId);
		
        if( container.offsetWidth == 0 ){
        	container.style.width = "800px";
        }
        var w = container.offsetWidth;
        if( container.offsetHeight == 0 ){
        	container.style.height = (9/16*w)+"px";
        }
        var h = container.offsetHeight;
        var l = 0;
        if( tools.length > 0 ){
        	w -= STIStatic.toolbarWidth;
        	if( toolbarStyle.h == "Left" ){
        		l += STIStatic.toolbarWidth;
        	}
        }
		this.mapWindow = document.createElement("div");
		this.mapWindow.id = "mapWindow";
		
		var attributionHeight = 22;
		this.attribution = document.createElement("div");
		this.attribution.style.position			= "relative";
		this.attribution.style.width			= "100%";
		this.attribution.style.height			= attributionHeight + "px";
		this.attribution.style.left				= "0px";
		this.attribution.style.textAlign		= "left";
		this.attribution.style.borderTop		= "1px solid #989696";
		this.attribution.style.lineHeight		= attributionHeight + "px";
		
		
		// target = new 
		
		var link1 = document.createElement('a');
		link1.setAttribute('id', 'linkAttribution1');
		link1.setAttribute('target', '_new');
		link1.setAttribute('href', 'http://www.openstreetmap.org');
		link1.innerHTML = 'Map data &copy; OpenStreetMap contributors';
		
		var link2 = document.createElement('a');
		link2.setAttribute('id', 'linkAttribution2');
		link2.setAttribute('target', '_new');
		link2.setAttribute('href', 'http://creativecommons.org/licenses/by-sa/2.0/');
		link2.innerHTML = 'CC-BY-SA';
		
		var link3 = document.createElement('a');
		link3.setAttribute('id', 'linkAttribution3');
		link3.setAttribute('target', '_new');
		link3.setAttribute('href', 'http://www.mapquest.com/');
		link3.innerHTML = 'MapQuest';
		
		this.attribution.setAttribute('id', 'attribution');
		
		this.attribution.appendChild(link1);
		this.attribution.appendChild(document.createTextNode(", "));
		this.attribution.appendChild(link2);
		
		
		this.mapQuestPanel = document.createElement('span');
		this.mapQuestPanel.setAttribute("id", "mapQuestAttribution");

		this.mapQuestPanel.appendChild(document.createTextNode(", " + eu.europeana.vars.mapview.tiles_attribution + " "));
		this.mapQuestPanel.appendChild(link3);
		
		this.attribution.appendChild(this.mapQuestPanel);

		this.mapWindow.style.position	= "absolute";
		this.mapWindow.style.overflow	= "hidden";		
		this.mapWindow.style.width		= w+"px";
		this.mapWindow.style.height		= h +"px";
		this.mapWindow.style.left		= l+"px";
		
		container.appendChild(this.mapWindow);

		this.mapContainer				= document.createElement("div");
		this.mapContainer.id			= "mapContainer";
		this.mapContainer.style.position= "absolute";
		this.mapContainer.style.width	= w+"px";
		this.mapContainer.style.height	= h+"px";
		this.mapWindow.appendChild(this.mapContainer);

		container.parentNode.parentNode.appendChild(this.attribution);
        
        this.toolbar = document.createElement("div");
        
        
        this.mapWindow.appendChild(this.toolbar);

        this.drag = document.createElement("div");
        this.drag.title = "Drag Area: drag a selection area and with left mouse-button";
        this.drag.setAttribute('class','dragRange');
        this.drag.style.visibility = "hidden";
        this.toolbar.appendChild(this.drag);
        this.drag.onclick = function(evt){
        	if( map.activeControl == "drag" ){
        		map.deactivate("drag");
				map.activate("navigate");
        	}
        	else {
        		map.deactivate(map.activControl);
        		map.activate("drag");
        	}
        };
        
        /*
        var zoom = document.createElement("div");
        zoom.title = "Zoom into selection. To undo, go a step back in the History.";
        zoom.setAttribute('class','zoomRange');
        this.toolbar.appendChild(zoom);
        zoom.onclick = function(){
        	map.core.refine();
        };
        */

        var cancel = document.createElement("div");
        cancel.title = "Clear Selection";
        cancel.setAttribute('class','cancelRange');
        this.toolbar.appendChild(cancel);
        cancel.onclick = function(){
        	map.core.reset();
        };

        this.controlLockDiv = document.createElement("div");
        this.controlLockDiv.setAttribute('class','controlLock');
        this.mapWindow.appendChild(this.controlLockDiv);        
                
        this.leftTagCloudDiv = document.createElement("div");
        this.leftTagCloudDiv.setAttribute('class','tagCloudDiv');
        this.leftTagCloudDiv.style.position = 'absolute';
        
        this.mapWindow.appendChild(this.leftTagCloudDiv);
                
		this.rightTagCloudDiv = document.createElement("div");
        this.rightTagCloudDiv.setAttribute('class','tagCloudDiv');
        this.rightTagCloudDiv.style.position = 'absolute';
        
        this.mapWindow.appendChild(this.rightTagCloudDiv);
        
        this.pointClickDiv = document.createElement("div");
        this.pointClickDiv.setAttribute('class','pointClickDiv');
        this.mapWindow.appendChild(this.pointClickDiv);
        
        var pointClickDivBackground = document.createElement("div");
        pointClickDivBackground.setAttribute('class','pointClickDivBackground');
        this.pointClickDiv.appendChild(pointClickDivBackground);
        
        if( STIStatic.popups ){
        	this.popup = new STIMapPopup(this, this.core,this.mapWindow);
        }
        
        this.objectLayer = new OpenLayers.Layer.Vector("Data Objects", {
            projection: "EPSG:4326"
        });
        this.connectionLayer = new OpenLayers.Layer.Vector("Connections", {
            projection: "EPSG:4326"
        });
        this.drilldownLayer = new OpenLayers.Layer.Vector("Drilldown", {
            projection: "EPSG:4326"
        });
        this.setBaseLayers();
        
        this.navigation = new OpenLayers.Control.Navigation({
            zoomWheelEnabled: STIStatic.mouseWheelZoom
        });
        
        if(STIStatic.popups){
        	// close popup when the map is clicked
            this.navigation.defaultClick = function(evt){
            	if(map.popup.popupDiv.style.visibility == "visible"){
            		map.labels = null;
            		map.core.reset();            		
            	}
            };        	
        }
        
        this.navigation.defaultDblClick = function(evt){
            var newCenter = this.map.getLonLatFromViewPortPx(evt.xy);
            this.map.setCenter(newCenter, this.map.zoom + 1);
            map.drawObjectLayer(true);
            if( !STIStatic.commonMapTools ){
            	map.zoomSlider.setValue(map.openlayersMap.getZoom());
            }
        };
        this.navigation.wheelUp = function(evt){
        	this.wheelChange(evt, 1);
            map.drawObjectLayer(true);
            if( !STIStatic.commonMapTools ){
            	map.zoomSlider.setValue(map.openlayersMap.getZoom());
            }
        };
        this.navigation.wheelDown = function(evt){
        	this.wheelChange(evt, -1);
            map.drawObjectLayer(true);
            if( !STIStatic.commonMapTools ){
            	map.zoomSlider.setValue(map.openlayersMap.getZoom());
            }
        };

        
        var options = {
            controls: [this.navigation, new OpenLayers.Control.ScaleLine() ],
            //controls: [this.navigation, new OpenLayers.Control.ScaleLine(), clickControl ],
            projection: new OpenLayers.Projection("EPSG:900913"),
            displayProjection: new OpenLayers.Projection("EPSG:4326"),
            units: "m",
			minZoomLevel: 1,
			maxZoomLevel: 17,
			numZoomLevels: 17,
			maxResolution: 78271.51695,
            maxExtent: new OpenLayers.Bounds(-20037508.34, -20037508.34, 20037508.34, 20037508.34)
        };
        
        
		
        this.openlayersMap = new OpenLayers.Map("mapContainer", options);
        
        
        
        
        
        for (var i = 0; i < this.baseLayers.length; i++){
	            this.openlayersMap.addLayers([this.baseLayers[i]]);
        }
        this.openlayersMap.fractionalZoom = false;
    	this.activeControl = "navigate";
    	
    	var boundaries = STIStatic.boundaries;
        var bounds = new OpenLayers.Bounds(boundaries.minLon, boundaries.minLat, boundaries.maxLon, boundaries.maxLat);
        var projectionBounds = bounds.transform(this.openlayersMap.displayProjection, this.openlayersMap.projection);
		this.openlayersMap.zoomToExtent(projectionBounds);
		this.openlayersMap.addLayers([this.connectionLayer, this.objectLayer, this.drilldownLayer]);

		if( STIStatic.commonMapTools ){
	        this.zoomPanel = new OpenLayers.Control.ModifiedZoomPanel();
	        this.zoomPanel.zoomIn.trigger = function(){
	        	this.map.zoomIn();
	            map.drawObjectLayer(true);
	    	};
	        this.zoomPanel.zoomOut.trigger = function(){
	        	this.map.zoomOut();
	            map.drawObjectLayer(true);
	    	};
	    	this.zoomPanel.zoomToMaxExtent.trigger = function() {
	        	if (this.map) {
	            	this.map.zoomToMaxExtent();
		            map.drawObjectLayer(true);
	        	}
	    	};
	        this.openlayersMap.addControl(this.zoomPanel);
	        this.openlayersMap.addControl(new OpenLayers.Control.PanPanel());
	        this.openlayersMap.addControl(new OpenLayers.Control.LayerSwitcher());
		}		
		
		if( STIStatic.popups ){
			var panMap = function(){
				if( map.pointSelected ){
		    		var lonlat = map.selectedPoint.getBounds().getCenterLonLat();
		    		var pixel = map.openlayersMap.getPixelFromLonLat(lonlat);
		    		map.popup.shift(pixel.x,pixel.y);
				}
			};
			this.openlayersMap.events.register("move",this.openlayersMap,panMap);
		}
		
		// places the toolbar inside the window
		var placeToolbar = function(){
			map.toolbar.style.visibility = "visible";
			var left = 0;
			var top = 0;
			if( map.polygons.length > 0 ){
			    map.drag.style.visibility = "visible";		
				for (var i = 0; i < map.polygons.length; i++){				 
					for (var j = 0; j < map.polygons[i].components.length; j++){
						var vertices = map.polygons[i].components[j].getVertices();
						for (var k = 0; k < vertices.length; k++){
							var lonlat = new OpenLayers.LonLat( vertices[k].x, vertices[k].y );
            				var pixel = map.openlayersMap.getPixelFromLonLat(lonlat);
            				if( pixel.x > left ){
            					left = pixel.x;
            					top = pixel.y;
            				}
						}
					}
				}
			    map.toolbar.style.width = "69px";
				map.toolbar.style.left = left+"px";
				map.toolbar.style.top = (top-map.toolbar.offsetHeight/2)+"px";
			}
			else {
			    map.drag.style.visibility = "hidden";
			    map.toolbar.style.width = "47px";
				map.toolbar.style.left = (map.pointClickDiv.offsetLeft + map.pointClickDiv.offsetWidth - map.toolbar.offsetWidth)+"px";
				map.toolbar.style.top = map.pointClickDiv.offsetTop+"px";
			}
		};

		this.openlayersMap.div.onmousedown = function(){
        	map.toolbar.style.visibility = "hidden";
		    map.drag.style.visibility = "hidden";		
		};
		this.openlayersMap.div.onmouseup = function(){
			if( map.polygons.length > 0 ){
				placeToolbar();
        	}
		};

		// manages selection of elements if a polygon was drawn        
        var drawnPolygonHandler = function(polygon){
        	if( map.displayPointSet == undefined ){
        		return;
        	}
        	map.polygon = polygon;
            var polygonArea;
            if (polygon instanceof OpenLayers.Geometry.Polygon) 
                polygonArea = new OpenLayers.Geometry.MultiPolygon([polygon]);
            else 
                if (polygon instanceof OpenLayers.Geometry.MultiPolygon) 
                    polygonArea = polygon;
            var points = map.displayPointSet[Math.floor(map.openlayersMap.getZoom())];
            var polygons = polygonArea.components;
            var innerPoints = [];
            for (var i = 0; i < points.length; i++) 
                for (var j = 0; j < polygons.length; j++) 
                    if (polygons[j].containsPoint(points[i].pointFeature.geometry)) {
                        innerPoints.push(points[i]);
                        continue;
                    }
            map.updateByPlace(innerPoints, 0);
            map.drilldownLayer.addFeatures([new OpenLayers.Feature.Vector(polygon)]);
            map.polygons = polygons;
            placeToolbar();
            map.deactivate();
            map.activate("navigate");
        };
        
		// resets the core
        var snapper = function(){
			map.core.reset();
        };

		this.getCoords = function(){
			var extent  = this.openlayersMap.getExtent();
			return extent.transform( this.openlayersMap.projection, this.openlayersMap.displayProjection);
		}
		
		this.zoomCoords = function(coordString){
			var coords =  coordString.split(",") // bottom left (longitude, latitude), top right (longitude, latitude)
	    	var boundsCoords = new OpenLayers.Bounds(coords[0], coords[1], coords[2], coords[3]);
	    	var projectionBoundsCoords = boundsCoords.transform(this.openlayersMap.displayProjection, this.openlayersMap.projection);
	    	this.openlayersMap.zoomToExtent(projectionBoundsCoords);
		}

		if( STIStatic.polygonSelect ){        
	        this.drawPolygon = new OpenLayers.Control.DrawFeature(map.drilldownLayer, OpenLayers.Handler.Polygon, {
	            displayClass: "olControlDrawFeaturePolygon",
	            title: "Polygon Drilldown",
	            callbacks: {
	                "done": drawnPolygonHandler,
	                "create": snapper
	            }
	        });
	        this.openlayersMap.addControl(this.drawPolygon);
        }

		if( STIStatic.circleSelect ){    

        this.drawCircle = new OpenLayers.Control.DrawFeature(map.drilldownLayer, OpenLayers.Handler.RegularPolygon, {
            displayClass: "olControlDrawFeaturePolygon",
            title: "Cirlce Drilldown",
            handlerOptions: {
                sides: 40
            },
            callbacks: {
                "done": drawnPolygonHandler,
                "create": snapper
            }
        });
        this.openlayersMap.addControl(this.drawCircle);
        }
        
		if( STIStatic.polygonSelect || STIStatic.circleSelect ){
	        this.dragArea = new OpenLayers.Control.DragFeature(map.drilldownLayer, {
	        	onStart: function(){
	        		map.toolbar.style.visibility = "hidden";
	        	},
	            onComplete: function(feature){
	                drawnPolygonHandler(feature.geometry);
	            }
	        });
	        this.openlayersMap.addControl(this.dragArea);
        }

		if( STIStatic.historicMaps && STIStatic.countrySelect ){
        	this.selectCountry = new OpenLayers.Control.GetFeature({
            	protocol: OpenLayers.Protocol.WFS.fromWMSLayer(map.openlayersMap.baseLayer)
        	});
        	this.selectCountry.events.register("featureselected", this, function(e){
            	if (map.pointSelected){ 
                	map.pointSelected = false;
				}
            	else {
                	drawnPolygonHandler(e.feature.geometry);
				}
        	});
        	this.selectCountry.events.register("featureunselected", this, function(e){
            	snapper();
        	});
        	this.openlayersMap.addControl(this.selectCountry);
		}

		// changes selection between labels (click, hover)
		var changeLabelSelection = function(point,label,update, callback){
			if( STIStatic.popups ){
				map.popup.showLabelContent(label);
				
                if(label.elements.length == 1 && STIStatic.skipBubbleTitles){
                    map.popup.showElements = 1;
                    map.popup.pages = 1;
                    map.popup.page = 0;
                    map.popup.update();                    
                }
			}
			if( update && map.lastLabel.div == label.div ){
				
               if(typeof callback === 'string' && callback == 'FORCE_UPDATE' && STIStatic.skipBubbleTitles){ }
               else{
            	   return;
               }
			}
			var k = point.search;
			var light = STIStatic.lightScheme;
			var c = STIStatic.colors[k];
			var color0 = 'rgb('+c.r0+','+c.g0+','+c.b0+')';
			var color1 = c.hex;
			if( update ){
				map.lastLabel.div.style.color = color0;
				map.lastLabel.selected = false;	
				if( light ){
					//map.lastLabel.div.style.textShadow = "0 0 0.4em black, 0 0 0.4em black, 0 0 0.4em black, 0 0 0.4em "+color1;
					map.lastLabel.div.style.textDecoration = "none";
					map.lastLabel.div.style.color = '#000';
				}
			}
			map.lastLabel = label;
			label.selected = true;							
			label.div.style.color = color1;
			if( light ){
				label.div.style.textDecoration = "underline";
				//label.div.style.textShadow = "0 0 0.1em white, 0 0 0.1em white, 0 0 0.1em white, 0 0 0.1em "+color1;
				label.div.style.color = '#000';
			}
			else {
				//label.div.style.textShadow = "0 0 1em black, 0 0 1em black, 0 0 1em black, 0 0 1em "+color1;
				label.div.style.color = '#000';
			}
			

            if(typeof callback != 'string'){
                map.updateByPlaceLabel(point,label.elements,0);
            }
            if(typeof callback == 'function'){
                callback();
            }
			//map.updateByPlaceLabel(point,label.elements,0);
		};

		var getLevelOfDetail = function(){
			var zoom = map.openlayersMap.getZoom();
			if( zoom <= 1 ){
				return 0;
			}
			else if( zoom <= 3 ){
				return 1;
			}
			else if( zoom <= 8 ){
				return 2;
			}
			else {
				return 3;
			}
		};
		
		// calculates the tag cloud
		var calculateTagCloud = function(){
			var elements = map.lastHovered.elements;
			var weight = 0;
			var labels = [];
			var levelOfDetail = getLevelOfDetail();
			for( var i=0; i<elements.length; i++ ){
				weight += elements[i].weight;
				var found = false;
				var label = elements[i].getPlace(levelOfDetail);
				if( label == "" ){
					label = "unknown";
				}
				for( var j=0; j<labels.length; j++ ){
					if( labels[j].place == label ){
						labels[j].elements.push(elements[i]);
						labels[j].weight += elements[i].weight;
						found = true;
						break;
					}
				}
				if( !found ){
					labels.push( { place: label, elements: new Array(elements[i]), weight: elements[i].weight } );
				}
			}
            var sortBySize = function(label1, label2){
                if (label1.weight > label2.weight){ 
                    return -1;
				}
                return 1;
            };
			labels.sort(sortBySize);
			if( labels.length+1 > STIStatic.maxPlaceLabels ){
				var c = [];
				var w = 0;
				for( var i=STIStatic.maxPlaceLabels-2; i<labels.length; i++ ){
					c = c.concat(labels[i].elements);
					w += labels[i].weight;
				}
				labels = labels.slice(0,STIStatic.maxPlaceLabels-2);
				labels.push( { place: "others", elements: c, weight: w } );
			}
			if( labels.length > 1 ){
				labels.push( { place: "all", elements: elements, weight: weight } );
			}
			else if( labels[0].place == "unknown" ){
				labels[0].place = "all";
			}
			map.labels = labels;

			var light = STIStatic.lightScheme;
			var k = map.lastHovered.search;
			var c = STIStatic.colors[k];
			var color = 'rgb('+c.r0+','+c.g0+','+c.b0+')';
			var shadow = c.hex;
			var clickFunction = function(point,label){
				label.div.onclick = function(){
					if( map.pointSelected ){
						changeLabelSelection(point,label,true);
					}
				};
				label.div.onmouseover = function(){
					if( map.pointSelected && !label.selected ){
						//label.div.style.textShadow = "0 -1px "+shadow+", 1px 0 "+shadow+", 0 1px "+shadow+", -1px 0 "+shadow;
						map.updateByPlaceLabel(point,label.elements,1);
						label.div.style.color = '#000';
					}
				};
				label.div.onmouseout = function(){
					if( map.pointSelected && !label.selected ){
						if( light ){
							//label.div.style.textShadow = "0 0 0.4em black, 0 0 0.4em black, 0 0 0.4em black, 0 0 0.4em "+shadow;
							label.div.style.color = '#000';
						}
						else {
							//label.div.style.textShadow = "0 0 1em black, 0 0 1em black, 0 0 1em black, 0 0 1em "+shadow;
							label.div.style.color = '#000';
						}
						map.updateByPlaceLabel(point,label.elements,2);
					}
				};
			};
			for( var i=0; i<map.labels.length; i++ ){
				var l = map.labels[i];
				l.selected = false;				
				var div = document.createElement("div");
				div.setAttribute('class','tagCloudItem');
				div.style.color = color;
				div.style.backgroundColor = "white";
				div.style.padding = "6px";

				div.style.position = 'absolute';
				div.style.zIndex = 1009;
				
				div.style.whiteSpace = "nowrap";
				//div.style.cursor = 'pointer';
				
				var fs = 2*l.weight/1000;
				if( l.place == "all" ){
					fs = 0;
				}
				if( fs > 2 ){
					fs = 2;
				}
				map.mapWindow.appendChild(div);
				div.style.fontSize = (1+fs)+"em";
				if( light ){
					//div.style.textShadow = "0 0 0.4em black, 0 0 0.4em black, 0 0 0.4em black, 0 0 0.4em "+shadow;
					div.style.color = '#000';
				}
				else {
					//div.style.textShadow = "0 0 1em black, 0 0 1em black, 0 0 1em black, 0 0 1em "+shadow;
					div.style.color = '#000';
				}
				div.innerHTML = l.place + "<span style='font-size:"+(1-fs/(1+fs))+"em'>&nbsp;(" + l.weight + ")</span>";
				l.div = div;
				var point = map.lastHovered; 
				clickFunction(point,l);
			}
			var createDiv = function( mod, div ){
				var height = 0;
				var width = 0;
				for( var i=0; i<map.labels.length; i++ ){
					if( i%2 == mod ){ 
						height += map.labels[i].div.offsetHeight;
						if( map.labels[i].div.offsetWidth > width ){
							width = map.labels[i].div.offsetWidth;
						}
						if( i>1 ){
							height += 5;
						}
					}
				}
				div.style.width = width+"px";
				div.style.height = height+"px";
				height = 0;
				for( var i=0; i<map.labels.length; i++ ){
					if( i%2 == mod ){ 
						var h = map.labels[i].div.offsetHeight;
						div.appendChild(map.labels[i].div);
						if( mod == 0 ){
							map.labels[i].div.style.right = "0px";
						}
						else {
							map.labels[i].div.style.left = "0px";
						}
						map.labels[i].div.style.top = height+"px";
						height += h+5;
					}
				}
			};
			map.leftTagCloudDiv.innerHTML = "";
			map.rightTagCloudDiv.innerHTML = "";
			createDiv(0,map.leftTagCloudDiv);			
			createDiv(1,map.rightTagCloudDiv);
			map.placeTagCloud(map.lastHovered);
		};

		// manages hover selection of point objects
        var hoverSelect = function(event){
        	if( map.pointSelected ){
        		return;
        	}
			map.core.undoHover(true);
			var index = event.feature.index;
			map.lastHovered = map.displayPointSet[Math.floor(map.openlayersMap.getZoom())][index];
			calculateTagCloud();
			map.updateByPlace([map.lastHovered], 1);
        };
        var hoverUnselect = function(event){
        	map.hoverUnselect();
        };
        var highlightCtrl = new OpenLayers.Control.SelectFeature(this.objectLayer, {
            hover: true,
            highlightOnly: true,
            renderIntent: "temporary",
            eventListeners: {
                featurehighlighted: hoverSelect,
                featureunhighlighted: hoverUnselect
            }
        });
        this.openlayersMap.addControl(highlightCtrl);
        highlightCtrl.activate();
        
        this.selectFeature = new OpenLayers.Control.SelectFeature(this.objectLayer);
        
		// manages click selection of point objects
        var onFeatureSelect = function(event){
        	if( map.pointSelected ){
        		return;
        	}
        	hoverUnselect();
			var index = event.feature.index;
            var point = map.displayPointSet[Math.floor(map.openlayersMap.getZoom())][index];
        	if( STIStatic.popups ){
        		var width = map.mapWindow.offsetWidth;
        		var height = map.mapWindow.offsetHeight;
        		var lonlat = event.feature.geometry.getBounds().getCenterLonLat();
        		var pixel = map.openlayersMap.getPixelFromLonLat(lonlat);
        		var shiftX = 0;
        		var shiftY = 0;        		
        		if( pixel.x < 20 ){
        			shiftX = -20;
        		}
        		else if( width - pixel.x < 20 ){
        			shiftX = 20;
        		}
        		if( pixel.y < 20 ){
        			shiftY = -20;
        		}
        		else if( height - pixel.y < 20 ){
        			shiftY = 20;
        		}
        		if( !( shiftX == 0 && shiftY == 0 ) ){
        			map.openlayersMap.pan(shiftX,shiftY);
        		}


        		// tablets don't "hover", so the map labels are still empty at this point, so we simulate a hover event.
				//if(typeof map.labels == 'undefined'){
				if(typeof map.labels == 'undefined' || map.labels == null){
					map.lastHovered = map.displayPointSet[Math.floor(map.openlayersMap.getZoom())][index];
					calculateTagCloud();
				}

        		
        		if(STIStatic.skipBubbleTitles){

        			// pseudo pan executed here to place the bubble accurately on the iPad (which is losing info due to not supporting hover states)
        			map.openlayersMap.pan(1,1);
        			map.openlayersMap.pan(-1,-1);

                    map.popup.createMapPopup(pixel.x-shiftX,pixel.y-shiftY,map.labels);
            		map.selectedPoint = event.feature.geometry;
        			changeLabelSelection(point,map.labels[map.labels.length-1],false,
        					function(){
	                        	changeLabelSelection(
	                                point,
	                                map.labels[map.labels.length-1],  // callback will bind to this....
	                                true,
	                                'FORCE_UPDATE');
	        					});

        		}
        		else{
                    changeLabelSelection(point,map.labels[map.labels.length-1],false);
                    map.popup.createMapPopup(pixel.x-shiftX,pixel.y-shiftY,map.labels);
            		map.selectedPoint = event.feature.geometry;
        		}
        	}
        	else {
    			map.openlayersMap.setCenter(event.feature.geometry.getBounds().getCenterLonLat());
    			map.placeTagCloud(point);
    			map.pointClickDiv.style.visibility = "visible";
    			map.polygons = [];
    			placeToolbar();
    			changeLabelSelection(point,map.labels[map.labels.length-1],false);        		
        	}
        };
        this.objectLayer.events.on({ "featureselected": onFeatureSelect });
        this.openlayersMap.addControl(this.selectFeature);
        this.selectFeature.activate();
		
        this.setCanvas();

        if( tools.length > 0 ){
    		var mapToolbar = document.createElement("div");
    		
			// Andy: restore css styling (and delete this) when IE7 requirement is dropped
    		mapToolbar.setAttribute('class','toolbar'+toolbarStyle.h+' toolbar'+toolbarStyle.v+' toolbar'+toolbarStyle.h+''+toolbarStyle.s);
    		mapToolbar.style.position = "absolute";
    		
    		// The left, right, top and bottom values are already specified in "Sti.css", but need to be programatically set here for the benefit of IE7
    		if(toolbarStyle.h == 'Right'){
    			mapToolbar.style.right		= "0px";
    			mapToolbar.style.height		= "100%";
    			//mapToolbar.style.maxWidth	= "40px";
    			mapToolbar.style.width		= "41px";
    			mapToolbar.style.paddingTop	= "3px";
    		}
    		else if(toolbarStyle.h == 'Left'){
    			mapToolbar.style.left		= "0px";
    			mapToolbar.style.height		= "100%";
    			mapToolbar.style.maxWidth	= "40px";
    		}
    		else if(toolbarStyle.h == 'Top'){
    			mapToolbar.style.top		= "0px";
    		}
    		else if(toolbarStyle.h == 'Bottom'){
    			mapToolbar.style.bottom		= "0px";
    		}
    		
    		if(toolbarStyle.s == 'Light'){
    			mapToolbar.style.backgroundColor		= "white";
    			mapToolbar.style.backgroundImage		= "url(" + eu.europeana.vars.branding + "/js/sti/e4D-javascript/images/light/bg-right.png)";
    			mapToolbar.style.backgroundRepeat		= "repeat-y";
    		}
    		else if(toolbarStyle.s == 'Dark'){
    			// TODO - move css properties into this .js
    		}
    		
    		mapToolbar.setAttribute('class','toolbar'+toolbarStyle.h+' toolbar'+toolbarStyle.v+' toolbar'+toolbarStyle.h+''+toolbarStyle.s);
    		mapToolbar.appendChild(STIStatic.createToolbar(tools));
    		container.appendChild(mapToolbar);
        }
        
        if( this.zoomSlider != undefined ){
			this.zoomSlider.setMaxAndLevels(1000,this.openlayersMap.getNumZoomLevels());
			this.zoomSlider.setValue(this.openlayersMap.getZoom());
        }
        
        
        var control = new OpenLayers.Control();
        this.openlayersMap.addControl(control);
        var callbacks = {
        		done: function(){
        			
        			
        			var radio = jQuery("#mapContainer div dl dt input:checked");
        			setTimeout(function(){radio.click();}, 200);
        		}
        };
        
        var handler = new OpenLayers.Handler.Pinch(control, callbacks);
        handler.activate();

    },
    
    /**
     * parses base layers in a given xmlFile and initializes google, bing, yahoo and osm layers if needed
     */
    setBaseLayers: function(){

		this.baseLayers = [];
	
		
		
		
		if( STIStatic.historicMaps ){
	        var xmlDoc = STIStatic.getXmlDoc("layers.xml");
	        var wmsLayers = xmlDoc.getElementsByTagName("wms");
	        for (i = 0; i < wmsLayers.length; i++) {
	            var name = wmsLayers[i].getElementsByTagName("name")[0].childNodes[0].nodeValue;
	            var server = wmsLayers[i].getElementsByTagName("server")[0].childNodes[0].nodeValue;
	            var layer = wmsLayers[i].getElementsByTagName("layer")[0].childNodes[0].nodeValue;
	            var format = wmsLayers[i].getElementsByTagName("format")[0].childNodes[0].nodeValue;
	            var transparency = wmsLayers[i].getElementsByTagName("transparency")[0].childNodes[0].nodeValue;
	            var layer = new OpenLayers.Layer.WMS(name, server, {
	                layers: layer,
	                format: format,
	                transparent: transparency
	            }, {
	                isBaseLayer: true
	            });
	            this.baseLayers.push(layer);
	        }
		}			
		
		if( STIStatic.osmMaps ){ // OSM via mapquest
			
			this.baseLayers.push(
				new OpenLayers.Layer.OSM(
					eu.europeana.vars.mapview.layers.labels.openStreetMapTiles,
					'http://otile1.mqcdn.com/tiles/1.0.0/osm/${z}/${x}/${y}.png',
					//'http://a.tile.openstreetmap.org/${z}/${x}/${y}.png',
					//'http://tah.openstreetmap.org/Tiles/tile/${z}/${x}/${y}.png',
					{
						numZoomLevels: 17
						//,
						//CLASS_NAME: OpenLayers.Layer.MapQuestOSM
					}
				)
			);
		}
		
		if( STIStatic.googleMaps ){
			this.baseLayers.push(
				new OpenLayers.Layer.Google(
					eu.europeana.vars.mapview.layers.labels.googlePhysical,
				    {type: google.maps.MapTypeId.TERRAIN, numZoomLevels: 15}
				)
			);
			
			this.baseLayers.push(
				new OpenLayers.Layer.Google(
					eu.europeana.vars.mapview.layers.labels.googleStreet,
				    {numZoomLevels: 18}
				)
			);
			
			this.baseLayers.push(
				new OpenLayers.Layer.Google(
					eu.europeana.vars.mapview.layers.labels.googleHybrid,
				    {type: google.maps.MapTypeId.HYBRID, numZoomLevels: 18}
				)
			);
			
			this.baseLayers.push(
				new OpenLayers.Layer.Google(
					eu.europeana.vars.mapview.layers.labels.googleSatellite,
					{type: google.maps.MapTypeId.SATELLITE, numZoomLevels: 18}
				)
			);


		}
		
		if( STIStatic.bingMaps ){
			this.baseLayers.push( new OpenLayers.Layer.VirtualEarth("Bing Streets", { type: VEMapStyle.Shaded, 'sphericalMercator': true } ) );
	        this.baseLayers.push( new OpenLayers.Layer.VirtualEarth("Bing Aerial", { type: VEMapStyle.Aerial, 'sphericalMercator': true } ) );
	        this.baseLayers.push( new OpenLayers.Layer.VirtualEarth("Bing Hybrid", { type: VEMapStyle.Hybrid, 'sphericalMercator': true } ) );
        }
		
		if( STIStatic.yahooMaps ){
	        this.baseLayers.push( new OpenLayers.Layer.Yahoo( "Yahoo Street", {'sphericalMercator': true} ) );
	        this.baseLayers.push( new OpenLayers.Layer.Yahoo( "Yahoo Satellite", {'type': YAHOO_MAP_SAT, 'sphericalMercator': true} ) );
	        this.baseLayers.push( new OpenLayers.Layer.Yahoo( "Yahoo Hybrid", {'type': YAHOO_MAP_HYB, 'sphericalMercator': true} ) );
		}
		

	
		if( !STIStatic.commonMapTools ){
			this.mapsMenu		= document.createElement("div");
			
			// Andy: restore css styling (and delete this) when IE7 requirement is dropped
			//this.mapsMenu.setAttribute('class', 'mapsMenu');
			//this.mapsMenu.setAttribute('class', 'mapsMenuBubble');
			
			this.mapsMenu.style.position = "absolute";

			if(STIStatic.mapmenuBubble){
				this.mapsMenu.style.display = "none";
				this.mapsMenu.style.backgroundColor = "white";
				this.mapsMenu.style.borderColor = "#666";
				this.mapsMenu.style.borderStyle = "solid";
				this.mapsMenu.style.borderWidth = "1px";
				this.mapsMenu.style.padding = "5px";
				this.mapsMenu.style.borderRadius = "10px";
				this.mapsMenu.style.zIndex = "20000";
				this.mapsMenu.style.right = "auto";

				// bubble tail styling
				this.mapsMenuTail	= document.createElement("div");
				//this.mapsMenuTail.setAttribute('class', 'mapsMenuBubbleTail');
				this.mapsMenuTail.style.position = "absolute";
				this.mapsMenuTail.style.display = "none";
				this.mapsMenuTail.style.backgroundImage = "url(/portal/branding/portal2/js/sti/e4D-javascript/images/bubble-arrow-point-right.png)";
				this.mapsMenuTail.style.backgroundPosition = "-12px 0px";
				this.mapsMenuTail.style.backgroundRepeat = "no-repeat";
				this.mapsMenuTail.style.height = "31px";
				this.mapsMenuTail.style.width = "18px";
				this.mapsMenuTail.style.zIndex = "20001";
			}
			else{
				// original styling
				this.mapsMenu.style.background		= "#C9C9CB";
				this.mapsMenu.style.zIndex 			= "2000";
				this.mapsMenu.style.borderColor		= "#666";
				this.mapsMenu.style.borderStyle		= "solid";
				this.mapsMenu.style.borderWidth		= "1px";
				this.mapsMenu.style.display 		= "none";
			}

			var maps = document.createElement("dl");
			maps.style.margin  = "0px";
			var map = this;
			var addEntry = function(layer){
				var entry = document.createElement("dt");
				
				entry.style.padding		= "2px";
				entry.style.cursor		= "pointer";
				
				entry.innerHTML = '<input type="radio" name="layer" id="rd' + layer.name + '" ' + (layer.name == 'OSM Tiles@Home' ? ' checked="checked"' : '') + '>' +  eu.europeana.vars.mapview.layers.mapping[layer.name];
				entry.onclick = function(){
					// update layer
					map.openlayersMap.setBaseLayer(layer);
					
					// update attributions
					if(layer.name == 'OSM Tiles@Home'){
						map.mapQuestPanel.style.display = "inline";
					}
					else{
						map.mapQuestPanel.style.display = "none";						
					}
					
					// update layers menu
					map.layerSwitcherControl.style.backgroundPosition	= "0px -1766px";
					map.mapsMenu.style.display = "none";
					
					if(STIStatic.mapmenuBubble){
						document.getElementById('rd' + layer.name).checked = true;
						map.mapsMenuTail.style.display = "none";
					}
					
					// update points to (possibly different) zoom level
			        map.resetPoints();
				};
				maps.appendChild(entry);

			};
			for( i in this.baseLayers ){
				addEntry(this.baseLayers[i]);
			}
			
			this.mapsMenu.appendChild(maps);
			this.mapContainer.appendChild(this.mapsMenu);
			if(STIStatic.mapmenuBubble){
				this.mapContainer.appendChild(this.mapsMenuTail);
			}
		};	
    },
    
    setDefaultLayer : function(layerName){
    	var map = this;
    	jQuery(this.baseLayers).each(function(i, ob){
    		if(ob.name == layerName){    			
    			map.openlayersMap.setBaseLayer(ob);
    			map.layerSwitcherControl.style.backgroundPosition	= "0px -1766px";
    			map.mapsMenu.style.display = "none";
    			if(STIStatic.mapmenuBubble){
    				map.mapsMenuTail.style.display = "none";
					document.getElementById('rd' + layerName).checked = true;
    			}
    		}
    	});
    },
    
    /**
     * sets the background canvas of the map window (or resets it after resizing the browser window)
     */
    setCanvas: function(){
        var cv = document.createElement("canvas");
        cv.id = "mapCanvas";
        this.mapWindow.appendChild(cv);
        cv.width = this.mapWindow.clientWidth;
        cv.height = this.mapWindow.clientHeight;
        
        
        if (!cv.getContext && typeof G_vmlCanvasManager != 'undefined' && G_vmlCanvasManager){
        	cv = G_vmlCanvasManager.initElement(cv);        	
        }

        var ctx = cv.getContext('2d');
        
        var gradient = ctx.createLinearGradient(0, 0, 0, cv.height);
        if( STIStatic.lightScheme ){
            gradient.addColorStop(0, '#8bafd8');
            gradient.addColorStop(1, '#355272');        	
        }
        else {
            gradient.addColorStop(0, 'rgba(95,136,178,1)');
            gradient.addColorStop(1, 'rgba(42,64,86,1)');
        }
        ctx.fillStyle = gradient;
        ctx.fillRect(0, 0, cv.width, cv.height);
    },
    
    /**
     * draws the object layer.
	 * @param {boolean} zoom if there was a zoom; if not, the new boundary of the map is calculated
     */
    drawObjectLayer: function(zoom){
		if( this.displayPointSet == undefined ){
			
			return;
		}
        this.objectLayer.removeAllFeatures();
        if (!zoom) {
            var minLat, maxLat, minLon, maxLon;
            var points = this.displayPointSet[this.openlayersMap.getNumZoomLevels() - 1];
            for (i = 0; i < points.length; i++) {
                var point = points[i];
                if (minLon == null || point.originX < minLon) 
                    minLon = point.originX;
                if (maxLon == null || point.originX > maxLon) 
                    maxLon = point.originX;
                if (minLat == null || point.originY < minLat) 
                    minLat = point.originY;
                if (maxLat == null || point.originY > maxLat) 
                    maxLat = point.originY;
            }
            if (minLon == maxLon && minLat == maxLat) {
                this.openlayersMap.setCenter(new OpenLayers.LonLat(minLon, minLat), 5);
            }
            else {
            	var gapX = 0.1 * ( maxLon - minLon );
            	var gapY = 0.1 * ( maxLat - minLat );
                this.openlayersMap.zoomToExtent(new OpenLayers.Bounds(minLon-gapX, minLat-gapY, maxLon+gapX, maxLat+gapY));
                this.openlayersMap.zoomTo(Math.floor(this.openlayersMap.getZoom()));
            }
            if( !STIStatic.commonMapTools ){
            	this.zoomSlider.setValue(this.openlayersMap.getZoom());
            }
        }
    	
    	
        var points = this.displayPointSet[Math.floor(this.openlayersMap.getZoom())];


    	for (var i = 0; i < points.length; i++) {
        	var resolution = this.openlayersMap.getResolution();
        	var p = points[i];
        	var x = p.originX + resolution * p.shiftX; 
        	var y = p.originY + resolution * p.shiftY;
        	p.pointFeature.geometry.x = x;
        	p.pointFeature.geometry.y = y;
        	p.olPointFeature.geometry.x = x;
        	p.olPointFeature.geometry.y = y;
            this.objectLayer.addFeatures([p.pointFeature]);
            this.objectLayer.addFeatures([p.olPointFeature]);

        }
        
        var dist = function(p1,p2){
        	return Math.sqrt( (p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y) );
        };
        
        
        if( STIStatic.connections && STIStatic.timeplot ){
            for (var i = 0; i < this.connections.length; i++) 
                this.connections[i] = [];
            var slices = this.core.timeplot.getSlices();
            for (var i = 0; i < slices.length; i++) {
                for (var j = 0; j < slices[i].elements.length; j++) {
                	var e = slices[i].elements[j]; 
                    if (e.length == 0) 
                        continue;
                    var points = [];
                    for (var k = 0; k < e.length; k++) {
                    	var point = e[k].pointobjects[Math.floor(this.openlayersMap.getZoom())].pointFeature.geometry;   
                    	if( points.indexOf(point) == -1 ){
                   			points.push(point);
                    	}
                    }
                    var matrix = new AdjMatrix(points.length);
                    for (var k = 0; k < points.length-1; k++) {
                    	for (var l = k+1; l < points.length; l++) {
                        	matrix.setEdge(k,l,dist(points[k],points[l]));
                        }
                    }
    				var tree = Prim(matrix);
    				var lines = [];
    				for( var z=0; z<tree.length; z++ ){
    					lines.push(new OpenLayers.Geometry.LineString(new Array(points[tree[z].v1],points[tree[z].v2])));
    				}
                    this.connections[j].push({
                    	first: e[0].pointobjects[Math.floor(this.openlayersMap.getZoom())].pointFeature.geometry,
                    	last: e[e.length-1].pointobjects[Math.floor(this.openlayersMap.getZoom())].pointFeature.geometry,
                        lines: lines,
                        time: slices[i].date
                    });
                }
            }
        }
    	
        this.updateMap();			
    },
    
    /**
     * initializes the object layer.
     * all point representations for all zoom levels are calculated and initialized
     * @param {MapObject[][]} mapObjects an array of map objects from different (1-4) sets
     */
    initMap: function(mapObjects){
    	this.mapObjects = mapObjects;
    	this.clearMap();
        var map = this;
        
        var getPointCluster = function(mapObjects){
       		STIStatic.maximumRadius = STIStatic.minimumRadius;
            var zoomLevels = map.openlayersMap.getNumZoomLevels();
			var getMaxRadius = function(size){
                var exponent = 0;
                while (Math.pow(STIStatic.classBase, exponent) < size) 
                    exponent++;
                return STIStatic.minimumRadius + exponent;
			};
			var dd = new DynamicDelaunay(-20037508.34,-20037508.34,20037508.34,20037508.34);
			
            for (var i = 0; i < mapObjects.length; i++){

            	var weight = 0;
               	for (var j = 0; j < mapObjects[i].length; j++) {
               		var o = mapObjects[i][j];
                   	var p = new OpenLayers.Geometry.Point(o.longitude, o.latitude, null);
                   	p.transform(map.openlayersMap.displayProjection, map.openlayersMap.projection);
                   	var point = new Vertex(Math.floor(p.x), Math.floor(p.y), mapObjects.length);
                   	
					point.addElement(o,o.weight,i);
					dd.add(point);

					weight += o.weight;
               	}
               	var r = getMaxRadius(weight);
               	if( r > STIStatic.maximumRadius ){
               		STIStatic.maximumRadius = r;
               		STIStatic.maximumPoints = weight;
               	}
           	}
            var displayPoints = [];
            for (var i = 0; i < zoomLevels; i++) {
                var points = [];
                var resolution = map.openlayersMap.getResolutionForZoom(zoomLevels - i - 1);
                dd.mergeForResolution(resolution);
                for (var j = 0; j < dd.vertices.length; j++) {
                	var point = dd.vertices[j];
                	if( !point.legal ){
                		continue;
                	}
                    var balls = [];
                    for (var k = 0; k < point.elements.length; k++){                    	
                        if (point.elements[k].length > 0){
                            balls.push({
                                search: k,
                                elements: point.elements[k],
                                radius: point.radii[k],
                                weight: point.weights[k]
                            });                        	
                        }
                    }
                   	var orderBalls = function( b1, b2 ){
                		if ( b1.radius > b2.radius ){
                			return -1;
                		}
                		if ( b2.radius > b1.radius ){
                			return 1;
                		}
                		return 0;
                	};
                    if (balls.length == 1) {
                        points.push(new DisplayPointObject(point.x, point.y, 0, 0, balls[0].elements, balls[0].radius, balls[0].search, balls[0].weight));
                    }
                    else 
                        if (balls.length == 2) {
                            var r1 = balls[0].radius;
                            var r2 = balls[1].radius;
                            points.push(new DisplayPointObject(point.x, point.y, -1*r2, 0, balls[0].elements, r1, balls[0].search, balls[0].weight));
                            points.push(new DisplayPointObject(point.x, point.y, r1, 0, balls[1].elements, r2, balls[1].search, balls[1].weight));
                        }
                        else 
                            if (balls.length == 3) {
                                var r1 = balls[0].radius;
                                var r2 = balls[1].radius;
                                var r3 = balls[2].radius;
                                var d = (2 / 3 * Math.sqrt(3) - 1) / 2;
                                points.push(new DisplayPointObject(point.x, point.y, -2*d*r1-r1, 0, balls[0].elements, r1, balls[0].search, balls[0].weight));
                                points.push(new DisplayPointObject(point.x, point.y, r2/2, r2, balls[1].elements, r2, balls[1].search, balls[1].weight));
                                points.push(new DisplayPointObject(point.x, point.y, r3/2, -1*r3, balls[2].elements, r3, balls[2].search, balls[2].weight));
                            }
                            else 
                                if (balls.length == 4) {
                		    balls.sort(orderBalls);                    
                                    var r1 = balls[0].radius;
                                    var r2 = balls[1].radius;
                                    var r3 = balls[2].radius;
                                    var r4 = balls[3].radius;
                                    var d = (Math.sqrt(2) - 1)*r2;
                                    points.push(new DisplayPointObject(point.x, point.y, -1*d-r2, 0, balls[0].elements, r1, balls[0].search, balls[0].weight));
                                    points.push(new DisplayPointObject(point.x, point.y, r1-r2, -1*d-r4, balls[3].elements, r4, balls[3].search, balls[3].weight));
                                    points.push(new DisplayPointObject(point.x, point.y, r1-r2, d+r3, balls[2].elements, r3, balls[2].search, balls[2].weight));
                                    points.push(new DisplayPointObject(point.x, point.y, d+r1, 0, balls[1].elements, r2, balls[1].search, balls[1].weight));
                                }
                }
                displayPoints.push(points);
            }
            return displayPoints.reverse();
        };
        
        this.connections = [];
        for (var i = 0; i < mapObjects.length; i++) {
           	this.connections.push([]);
           	for (var j = 0; j < mapObjects[i].length; j++){ 
               	mapObjects[i][j].pointobjects = [];
            }
        }

        this.displayPointSet = getPointCluster(mapObjects);

    	if( this.displayPointSet.length == 0 ){
    		return;
    	}
    	
        for (var i = 0; i < this.displayPointSet.length; i++) { 
            for (var j = 0; j < this.displayPointSet[i].length; j++) {
                var point = this.displayPointSet[i][j];
                var c = STIStatic.colors[point.search];
                for (var k = 0; k < point.elements.length; k++) 
                    point.elements[k].pointobjects.push(point);
                var style = {
                    fillColor: 'rgb(' + c.r0 + ',' + c.g0 + ',' + c.b0 + ')',
                    fillOpacity: 1,
				strokeWidth: 1,
				strokeColor: 'rgb(' + c.r1 + ',' + c.g1 + ',' + c.b1 + ')',
				stroke: false,
                    pointRadius: point.radius
                };
                var pointGeometry = new OpenLayers.Geometry.Point(point.originX, point.originY, null);
                var pointFeature = new OpenLayers.Feature.Vector(pointGeometry);
                pointFeature.style = style;
                pointFeature.index = j;
                point.setPointFeature(pointFeature);
                var olStyle = {
                    fillColor: 'rgb(' + c.r1 + ',' + c.g1 + ',' + c.b1 + ')',
                    fillOpacity: 1,
				stroke: false,
                    pointRadius: 0
                };
                var olPointGeometry = new OpenLayers.Geometry.Point(point.originX, point.originY, null);
                var olPointFeature = new OpenLayers.Feature.Vector(olPointGeometry);
                olPointFeature.style = olStyle;
                olPointFeature.index = j;
                point.setOlPointFeature(olPointFeature);
            }
        }
        this.drawObjectLayer(false);
        //this.zoomToEurope();
    },

    zoomToEurope: function(){
    	var boundsEurope = new OpenLayers.Bounds(1, 35, 30, 65);
    	var projectionBoundsEurope = boundsEurope.transform(this.openlayersMap.displayProjection, this.openlayersMap.projection);
    	this.openlayersMap.zoomToExtent(projectionBoundsEurope);
    },

    /**
     * hides all tagCloud and pointclick divs
     */
	setVisibility: function(visibility){
        this.toolbar.style.visibility = visibility;
       	this.leftTagCloudDiv.style.visibility = visibility;
       	this.rightTagCloudDiv.style.visibility = visibility;
		this.pointClickDiv.style.visibility = visibility;       	
	},
    
    /**
     * resets the map by destroying all additional elements except the point objects, which are replaced
     */
    resetMap: function(){
        this.connectionLayer.destroyFeatures();
        this.drilldownLayer.destroyFeatures();
		this.controlLockDiv.style.visibility = "hidden";
		this.selectFeature.unselectAll();
        this.resetPoints();
    	
        if( this.activeControl == "drag" ){
	  		this.deactivate("drag");
	  		this.activate("navigate");
  		}
	    this.drag.style.visibility = "hidden";		
		this.setVisibility("hidden");
		this.pointSelected = false;
		this.polygons = [];
        if( STIStatic.popups ){
        	this.popup.reset();
        }
    },
    
    /**
     * resets the map by destroying all elements
     */
    clearMap: function(){
        this.connectionLayer.destroyFeatures();
        this.drilldownLayer.destroyFeatures();
        this.objectLayer.destroyFeatures();
		delete this.displayPointSet;
        this.displayPointSet = undefined;
    },
    
    /**
     * updates the proportional selection status of a point object
     * @param {PointObject} point the point to update
     * @param {OpenLayers.Geometry.Polygon} polygon the actual displayed map polygon
     */
    updatePoint: function(point,polygon){
		var pFull = 0;
		var pRest = 0;
		for (var j = 0; j < point.elements.length; j++) {
			var o = point.elements[j];
			if (o.percentage == 1 || ( o.hoverSelect && this.lastHovered == undefined ) ) {
				pFull += o.weight;
			}
			else {
				pRest += o.percentage * o.weight;
			}
		}
		var drawOl = false;
		var draw = false;
		var pf = point.pointFeature;
		var olf = point.olPointFeature;
		if (pFull == 0 && olf.style.pointRadius != 0) {
			olf.style.pointRadius = 0;
			drawOl = true;
		}
		else if (pFull > 0) {
			olf.style.pointRadius = STIStatic.getRadius(pFull);
			drawOl = true;
		}
		if (point.percentage != pRest) {
			point.percentage = pRest;
			draw = true;
			var p;
			if (pRest == 0){
				p = 0;
			}
			else { 
				p = pRest / (point.elements.length - pFull);
			}
			var s = point.search;
			var c = STIStatic.colors[s];
			var r = c.r0 + Math.round(p * (c.r1 - c.r0));
			var g = c.g0 + Math.round(p * (c.g1 - c.g0));
			var b = c.b0 + Math.round(p * (c.b1 - c.b0));
			point.pointFeature.style.fillColor = 'rgb(' + r + ',' + g + ',' + b + ')';
		}
		if (polygon.containsPoint(point.pointFeature.geometry)) {
			if (draw || drawOl) {
				this.objectLayer.drawFeature(point.pointFeature);
				this.objectLayer.drawFeature(point.olPointFeature);
			}
		}
    },
    
    /**
     * updates the the object layer of the map after selections had been executed in timeplot or table or zoom level has changed
     */
    updateMap: function(){
        var points = this.displayPointSet[Math.floor(this.openlayersMap.getZoom())];
        var polygon = this.openlayersMap.getExtent().toGeometry();
        for (var i = 0; i < points.length; i++) {
        	this.updatePoint(points[i],polygon);
        }
        if( STIStatic.connections && STIStatic.timeplot ){ 
        	this.displayConnections();
        }
    },
    
    /**
     * resets the point objects depending on the actual zoom level in basic style
     */
    resetPoints: function(){
    	if( this.displayPointSet != undefined ){
    		if( this.displayPointSet.length == 0 ){
    			return;
    		}
        	var points = this.displayPointSet[Math.floor(this.openlayersMap.getZoom())];        	
        	for (var i = 0; i < points.length; i++) {
                var c = STIStatic.colors[points[i].search];
            	points[i].pointFeature.style.fillColor = 'rgb(' + c.r0 + ',' + c.g0 + ',' + c.b0 + ')';
            	points[i].olPointFeature.style.pointRadius = 0;
        	}
        	
        	// Andy - changed "redraw" to "drawObjectLayer(true)" to fix ipad error of the non-disappearing marker
        	//this.objectLayer.redraw();
    		this.drawObjectLayer(true);
       	}       	
    },
    
    /**
     * updates the data objects percentages after a selection on the map had been performed
     * @param {PointObject[]} pointObjects the point objects that corresponds to the selection
     * @param {boolean} hover if it was a hover selection
     */
    updateByPlace: function(pointObjects, hover){
        if (hover == 0) {
            this.core.reset();
        }
        for (var i = 0; i < pointObjects.length; i++) {
            var s = pointObjects[i].search;
            var c = STIStatic.colors[s].hex;
            if (hover == 1) {
                pointObjects[i].pointFeature.style.stroke = true;
                for (var j = 0; j < pointObjects[i].elements.length; j++) {
                    pointObjects[i].elements[j].setHover(true);
                }
            }
            else 
                if (hover == 2) {
                    pointObjects[i].pointFeature.style.stroke = false;
                    for (var j = 0; j < pointObjects[i].elements.length; j++) {
                        pointObjects[i].elements[j].setHover(false);
                    }
                }
                else {
                    pointObjects[i].pointFeature.style.fillColor = c;
                    pointObjects[i].pointFeature.style.stroke = false;
                    for (var j = 0; j < pointObjects[i].elements.length; j++) {
                        pointObjects[i].elements[j].setHover(false);
                        pointObjects[i].elements[j].setPercentage(1);
                    }
                }
            
            // ANDY: commented out these lines to fix the hover-break following layer switch
            //this.objectLayer.drawFeature(pointObjects[i].pointFeature);
            //this.objectLayer.drawFeature(pointObjects[i].olPointFeature);
        }
        if( hover == 0 ){
        	this.core.updateTimeAndTable(false);
        }
        else {
        	this.core.updateTimeAndTable(true);
        }
    },
    
    /**
     * updates the data objects percentages after a selection on the map by clicking on a place label
     * @param {PointObject} point the point object that corresponds to the label selection
     * @param {DataObject[]} elements the data objects corresponding to the selected label
     * @param {boolean} hover if it was a hover selection
     */
    updateByPlaceLabel: function(point,elements,hover){
    	if( hover == 0 ){
			this.core.reset();
		}
		for (var j = 0; j < elements.length; j++) {
			if( hover == 0 ){
				elements[j].setPercentage(1);
			}
			else if( hover == 1 ){
				elements[j].hoverSelect = true;
			} 
			else if( hover == 2 ){
				elements[j].hoverSelect = false;
			} 
		}
		if( !STIStatic.popups ){
			this.controlLockDiv.style.visibility = "visible";
			this.setVisibility("visible");
		}
		this.pointSelected = true;
		this.updatePoint(point,this.openlayersMap.getExtent().toGeometry());
        if( hover == 0 ){
        	this.core.updateTimeAndTable(false);
        }
        else {
        	this.core.updateTimeAndTable(true);
        }
    },    
    
    /**
     * displays connections between data objects
     */
    displayConnections: function(){
        var ltm = this.core.timeplot.leftFlagTime;
        var rtm = this.core.timeplot.rightFlagTime;
        if (ltm == undefined || ltm == null){
            return;
		}
        else {
            ltm = ltm.getTime();
            rtm = rtm.getTime();
        }
        this.connectionLayer.destroyFeatures();
        if (this.showConnections) {
            for (var i = 0; i < this.connections.length; i++) {
                var c = STIStatic.colors[i];
                var style = {
                    strokeColor: 'rgb(' + c.r1 + ',' + c.g1 + ',' + c.b1 + ')',
                    strokeOpacity: 0.5,
                    strokeWidth: 3	
                };
                var pointsToConnect = [];
                var last = undefined;
                for (var j = 0; j < this.connections[i].length; j++) {
                	var c = this.connections[i][j];
                    var ct = c.time.getTime();
					if (ct >= ltm && ct <= rtm) {
						if( last != undefined ){
                			var line = new OpenLayers.Geometry.LineString(new Array(last,c.first));
                			this.connectionLayer.addFeatures([new OpenLayers.Feature.Vector(line, null, style)]);
						}
						for( var k=0; k<c.lines.length; k++ ){
                			this.connectionLayer.addFeatures([new OpenLayers.Feature.Vector(c.lines[k], null, style)]);							
						}
						last = c.last;
                    }
                }
            }
            this.connectionLayer.redraw();
        }
    },

    /**
     * causes deselection of a hovered point object
     */
	hoverUnselect: function(){
        if( this.pointSelected ){
        	return;
        }
		if (this.lastHovered != undefined) {
        	this.leftTagCloudDiv.style.visibility = "hidden";
        	this.rightTagCloudDiv.style.visibility = "hidden";
			this.updateByPlace([this.lastHovered], 2);
            this.lastHovered = undefined;
		}
	},
	
    /**
     * places the tagCloud divs inside the map window
     * @param {PointObject} point the point object that corresponds to the tagCloud
     */
	placeTagCloud: function( point ){
		return;
		var lonlat = new OpenLayers.LonLat( point.pointFeature.geometry.x, point.pointFeature.geometry.y );
		var pixel = this.openlayersMap.getPixelFromLonLat(lonlat);
		var radius = point.pointFeature.style.pointRadius;
		var lw = this.leftTagCloudDiv.offsetWidth;
		var rw = this.rightTagCloudDiv.offsetWidth;
		var lp = false;
		var rp = false;		
		if( pixel.x - radius - lw -5 < 0 ){
			lp = true;
			if( pixel.x + radius + lw + rw + 10 > this.mapWindow.offsetWidth ){
				rp = true;
			}
		}
		else if( pixel.x + radius + rw + 5 > this.mapWindow.offsetWidth ){
			rp = true;
			if( pixel.x - radius - lw - rw - 10 < 0 ){
				lp = true;
			}
		}			
		if( !lp && !rp ){
			this.leftTagCloudDiv.style.left = (pixel.x-radius-lw-5)+"px";
			this.rightTagCloudDiv.style.left = (pixel.x+radius+5)+"px";
		}
		else if( lp && !rp ){
			this.leftTagCloudDiv.style.left = (pixel.x+radius+5)+"px";
			this.rightTagCloudDiv.style.left = (pixel.x+radius+lw+10)+"px";
		}
		else if( !lp && rp ){
			this.leftTagCloudDiv.style.left = (pixel.x-radius-lw-rw-10)+"px";
			this.rightTagCloudDiv.style.left = (pixel.x-radius-rw-5)+"px";
		}
		var lh = this.leftTagCloudDiv.offsetHeight;
		var rh = this.rightTagCloudDiv.offsetHeight;
		var lt = pixel.y - lh/2; 
		var rt = pixel.y - rh/2;
		if( lt < 0 ){
			lt = 0;
		}
		else if( lt + lh > this.mapWindow.offsetHeight ){
			lt = this.mapWindow.offsetHeight - lh;
		}
		if( rt < 0 ){
			rt = 0;
		}
		else if( rt + rh > this.mapWindow.offsetHeight ){
			rt = this.mapWindow.offsetHeight - rh;
		}
		this.leftTagCloudDiv.style.top = lt+"px";
		this.rightTagCloudDiv.style.top = rt+"px";
		this.leftTagCloudDiv.style.visibility = "visible";
		this.rightTagCloudDiv.style.visibility = "visible";
		
		this.pointClickDiv.style.height = (lh+45)+"px";
		this.pointClickDiv.style.width = (lw+rw+2*radius+70)+"px"; 
		this.pointClickDiv.style.left = (this.leftTagCloudDiv.offsetLeft-30)+"px";
		this.pointClickDiv.style.top = (lt-30)+"px";
	},
	
    /**
     * performs a zoom on the map
     * @param {int} delta the change of zoom levels
     */
	zoom: function( delta ){
		if( this.pointSelected ){
			return false;
		}
		var zoom = this.openlayersMap.getZoom() + delta;
		if( this.openlayersMap.baseLayer instanceof OpenLayers.Layer.WMS ){
			this.openlayersMap.zoomTo(zoom);	
		}
		else {
			this.openlayersMap.zoomTo(Math.round(zoom));
			if( !STIStatic.commonMapTools ){
				this.zoomSlider.setValue(this.openlayersMap.getZoom());
			}
		}
		this.drawObjectLayer(true);
		return true;
	},
	
	setMap: function(index){
		if (this.baseLayers[index] instanceof OpenLayers.Layer.WMS) {
			this.openlayersMap.fractionalZoom = true;
			this.selectCountry.protocol = OpenLayers.Protocol.WFS.fromWMSLayer(this.baseLayers[index]);
		}
		else {
			this.openlayersMap.fractionalZoom = false;
		}
		this.openlayersMap.zoomTo(Math.floor(this.openlayersMap.getZoom()));
		this.openlayersMap.setBaseLayer(this.baseLayers[index]);
	},

    /**
     * activates a specific map control
     * @param {String} status the identifier of the control to activate
     */
    activate: function(status){
    	this.activeControl = status;
   		if( status == "drag" ){
			this.dragArea.activate();
			this.drilldownLayer.setZIndex(parseInt(this.objectLayer.getZIndex())+1);
	        this.drag.setAttribute('class','dragRangeClick');
		}
		else if( status == "navigate" ){
			if( STIStatic.countrySelect || STIStatic.polygonSelect || STIStatic.circleSelect ){
				this.navigationControl.src = STIStatic.path+"hand_2.png";
			}
		}
		else if( status == "polygon" ){
			this.drawPolygon.activate();
			this.polygonSelectionControl.src = STIStatic.path+"poly_2.png";
		}
		else if( status == "circle" ){
			this.drawCircle.activate();
			this.circleSelectionControl.src = STIStatic.path+"rad_2.png";
		}
		else if( status == "country" ){
			this.selectCountry.activate();
			this.countrySelectionControl.src = STIStatic.path+"land_2.png";
		}
	},
    
    /**
     * deactivates a specific map control
     * @param {String} status the identifier of the control to deactivate
     */
    deactivate: function(){
    	var status = this.activeControl;
		if( status == "drag" ){
			this.dragArea.deactivate();
			this.drilldownLayer.setZIndex(parseInt(this.objectLayer.getZIndex())-1);
	        this.drag.setAttribute('class','dragRange');
		}
		else if( status == "navigate" ){
			if( STIStatic.countrySelect || STIStatic.polygonSelect || STIStatic.circleSelect ){
				this.navigationControl.src = STIStatic.path+"hand_0.png";
			}
		}
		else if( status == "polygon" ){
			this.drawPolygon.deactivate();
			this.polygonSelectionControl.src = STIStatic.path+"poly_0.png";
		}
		else if( status == "circle" ){
			this.drawCircle.deactivate();
			this.circleSelectionControl.src = STIStatic.path+"rad_0.png";
		}
		else if( status == "country" ){
			this.selectCountry.deactivate();
			this.countrySelectionControl.src = STIStatic.path+"land_0.png";
		}
	},
		
	initializeMapTools: function(){
		var mapTools = [];
		var map = this;
		var changeStatus = function(status){
			if( status != map.activeControl ){
				map.deactivate();
				map.activate(status);
			}
		};
		if( !STIStatic.commonMapTools ){
			this.zoomSlider = new ZoomSlider('mapZoom',this);
			mapTools.push(this.zoomSlider.div);
			
    		this.layerSwitcherControl = document.createElement("div");
    		this.layerSwitcherControl.style.backgroundImage		= "url(" + STIStatic.spritePath + ")";
    		this.layerSwitcherControl.style.backgroundPosition	= "0px -1766px";
    		this.layerSwitcherControl.style.backgroundRepeat	= "no-repeat";
    		this.layerSwitcherControl.style.width				= "23px";
    		this.layerSwitcherControl.style.height				= "25px";
    		this.layerSwitcherControl.style.marginTop			= "12px";
    		
    		this.layerSwitcherControl.title = eu.europeana.vars.msg.chooseMapType;
			mapTools.push(this.layerSwitcherControl);
			this.layerSwitcherControl.onclick = function(){
				
				if( map.mapsMenu.style.display == "none" ){
		    		map.layerSwitcherControl.style.backgroundPosition	= "0px -1735px";
		    		map.mapsMenu.style.display = "block";
	    			map.mapsMenu.style.top = map.layerSwitcherControl.offsetTop+"px";
	    			map.mapsMenu.style.left = "0px";
	    			
	    			if(STIStatic.mapmenuBubble){
		    			map.mapsMenuTail.style.display = "block";
		    			map.mapsMenu.style.top = map.layerSwitcherControl.offsetTop - 36 + "px";
		    			map.mapsMenuTail.style.top = map.layerSwitcherControl.offsetTop - 36 + 30 +"px";
	    			}
	    			
		    		if( !STIStatic.toolbarLeft ){
		    			var left = map.mapContainer.offsetWidth - map.mapsMenu.offsetWidth;
		    			if(STIStatic.mapmenuBubble){
		    				left = left - 18; 
		    				map.mapsMenuTail.style.left = (left+map.mapsMenu.offsetWidth-4)+"px";
		    			}
		    			map.mapsMenu.style.left = left+"px";
		    		}
				}
				else {
		    		map.layerSwitcherControl.style.backgroundPosition	= "0px -1766px";
		    		map.mapsMenu.style.display = "none";
		    		if(STIStatic.mapmenuBubble){
		    			map.mapsMenuTail.style.display = "none";
		    		}
				}
			};
		}
		if( STIStatic.countrySelect || STIStatic.polygonSelect || STIStatic.circleSelect ){
    		this.navigationControl			= document.createElement("img");
			this.navigationControl.src		= STIStatic.path+"hand_2.png";
			this.navigationControl.title	= eu.europeana.vars.msg.navigateMap;
			mapTools.push(this.navigationControl);
			this.navigationControl.onclick = function(){
				changeStatus("navigate");
			};
			this.activeControl = "navigate";
		}
		if( STIStatic.countrySelect ){
    		this.countrySelectionControl = document.createElement("img");
			this.countrySelectionControl.src = STIStatic.path+"land_0.png";
			this.countrySelectionControl.title = "Select via political border: click any country with left mouse-button to select";
			mapTools.push(this.countrySelectionControl);
			this.countrySelectionControl.onclick = function(){
				changeStatus("country");
			};
		}
		if( STIStatic.circleSelect ){
    		this.circleSelectionControl = document.createElement("img");
			this.circleSelectionControl.src = STIStatic.path+"rad_0.png";
			this.circleSelectionControl.title = "Select via radius: click and hold left mouse-button for the center, move mouse to widen the perimeter";
			mapTools.push(this.circleSelectionControl);
			this.circleSelectionControl.onclick = function(){
				changeStatus("circle");
			};
		}
		if( STIStatic.polygonSelect ){
    		this.polygonSelectionControl = document.createElement("img");
			this.polygonSelectionControl.src = STIStatic.path+"poly_0.png";
			this.polygonSelectionControl.title = eu.europeana.vars.msg.freeForm;
			mapTools.push(this.polygonSelectionControl);
			this.polygonSelectionControl.onclick = function(){
				changeStatus("polygon");
			};
		}
		
		
		if( STIStatic.connections && STIStatic.timeplot ){
    		var connectionControl = document.createElement("img");
			connectionControl.src = STIStatic.path+"connect_0.png";
			connectionControl.title = "Switch on time-dependent connections between points on the map";
			mapTools.push(connectionControl);
			connectionControl.onclick = function(){
				map.showConnections = !map.showConnections;
				map.displayConnections();
				if( map.showConnections ){
					connectionControl.src = STIStatic.path+"connect_2.png";
					connectionControl.title = "Switch off time-dependent connections";
				}
				else {
					connectionControl.src = STIStatic.path+"connect_0.png";
					connectionControl.title = "Switch on time-dependent connections between points on the map";
				}
			};
		}
		if( mapTools.length > 0 ){
			this.activate("navigate");
		}
    	return mapTools;
	},
	
	getZoom: function(){
		return this.openlayersMap.getZoom();
	}
	
};
