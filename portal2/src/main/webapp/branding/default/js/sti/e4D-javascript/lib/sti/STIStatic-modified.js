var STIStatic = {

	// configuration of modules
	map:				true,
	timeplot:			false,
	tables:				false,
	incompleteData:		true,

	// general settings
	maxResultsPerBubble:5,
	maxDatasets:		1,
	mouseWheelZoom:		true,	
	popups:				true,
	history:			false,
	urlPrefix: 			eu.europeana.vars.branding + "/js/sti/e4D-javascript/",


	// bubble behaviour
	skipBubbleTitles:	true,
	
	// configuration of the map settings
	historicMaps:		false,
	googleMaps:			true,
	
	bingMaps:			false,
	yahooMaps:			false,
	
	osmMaps:			true,
	countrySelect:		false,
	polygonSelect:		false,
	circleSelect:		false,
	commonMapTools:		false,
	connections:		true,
	boundaries: 		{ minLon: -29, minLat: 35, maxLon: 44, maxLat: 67 },
	maxPlaceLabels:		6,
	circleGap:			0,
	minimumRadius:		6, // was 4, increased for ipad
	classBase:			1.5,

	// configuration of the timeplot settings

	timeZoom:			true,
	animationControl:	true,
	timeplotHeight:		100,
	timeUnit:			SimileAjax.DateTime.YEAR,
	
	// configuration of the style settings
	lightScheme:		true,
	lightImagesPath:	'images/light/',
	darkImagesPath:		'images/dark/',
	spritePath:			'/portal/branding/portal2/images/icons/sprite.png',
	toolbarLeft:		false,
	toolbarWidth:		41,
	mapmenuBubble:		true,
	header:				false,
	headerHeight:		41,
	e4DStyle:			true,
	resultSize:			null,
	colors:				[
	       				 
/*		1st line (id:0) 
 * 		default marker 				= 153,0,255
 * 		selected marker + stroke	= 46,0,248
 */
	       				 { id: 0, r0: 153, g0: 0, b0: 255, 	r1: 46, g1: 0, b1: 248,	rt: 0, gt: 0, bt: 255, hex: '#9900FF' },
	
	       				 { id: 2, r0: 250, g0: 212, b0: 169, r1: 245, g1: 70,  b1: 0, rt: 255, gt: 101, bt: 41, hex: '#f54600' },                                        
	       				 { id: 1, r0: 194, g0: 255, b0: 194, r1: 0,   g1: 214, b1: 0, rt: 70,  gt: 221, bt: 70, hex: '#00D600'},
	       				 { id: 3, r0: 255, g0: 245, b0: 138, r1: 250, g1: 230, b1: 0, rt: 255, gt: 241, bt: 87, hex: '#FAE500'}
                        ]

//	colors: 			[{ id: 2, r1: 245, g1: 70, b1: 0, r0: 250, g0: 212, b0: 169, rt: 255, gt: 101, bt: 41, hex: '#f54600' },
//	        			 { id: 0, r1: 46, g1: 0, b1: 248, r0: 209, g0: 199, b0: 255, rt: 105, gt: 71, bt: 255, hex: '#2e00f8' },
//	        			 { id: 1, r1: 0, g1: 214, b1: 0, r0: 194, g0: 255, b0: 194, rt: 70, gt: 221, bt: 70, hex: '#00D600'	},
//	        			 { id: 3, r1: 250, g1: 230, b1: 0, r0: 255, g0: 245, b0: 138, rt: 255, gt: 241, bt: 87, hex: '#FAE500'}]
	
};


STIStatic.path = STIStatic.urlPrefix;
if( STIStatic.lightScheme ){
	STIStatic.path += STIStatic.lightImagesPath;
}
else {
	STIStatic.path += STIStatic.darkImagesPath;
}

/**
 * creates a toolbar of an array of tools as a dl list
 * @return the dl element
*/
STIStatic.createToolbar = function(tools){
	var dl = document.createElement("dl");
	dl.style.margin = "0px";
	
	for(var i=0; i<tools.length; i++){
		tools[i].style.cursor = "pointer";
		tools[i].style.marginLeft = "auto";
		tools[i].style.marginRight = "auto";
		var dt = document.createElement("dt");
		dt.style.textAlign = "center";
		dt.appendChild(tools[i]);
		dl.appendChild(dt);
	}

	return dl;
};

/**
 * returns the actual mouse position
 * @param {Event} e the mouseevent
 * @return the top and left position on the screen
*/
STIStatic.getMousePosition = function(e){
	if(!e){
    	e = window.event;
	}
    var body = (window.document.compatMode && window.document.compatMode == "CSS1Compat") ? window.document.documentElement : window.document.body;
    return {
    	top: e.pageY ? e.pageY : e.clientY,
        left: e.pageX ? e.pageX : e.clientX
    };
};

/**
 * returns the radius for a circle that contains n elements
 * @param {int} n the number of assigned elements
 * @return the calculated radius for the corresponding cirlce
*/
STIStatic.getRadius = function(n){
	if( n == 0 ){
		return 0;
	}
	if( n == 1 ){
		return STIStatic.minimumRadius;
	}
	var aMax = Math.PI * STIStatic.maximumRadius * STIStatic.maximumRadius; 
  	var aMin = Math.PI * STIStatic.minimumRadius * STIStatic.minimumRadius;
    return Math.sqrt( ( aMin + (aMax-aMin)/(STIStatic.maximumPoints-1)*(n-1) * Math.sqrt(Math.log(n)) )/Math.PI );
};

/**
 * returns the xml dom object of the file of the given url
 * @param {String} url the url of the file to parse
 * @return xml dom object of the given file
*/
STIStatic.getXmlDoc = function(url){
    var xmlhttp = false;
    if(!xmlhttp){
        try {
            xmlhttp = new XMLHttpRequest();
        } 
        catch (e) {
            xmlhttp = false;
        }
    }
    if (typeof ActiveXObject != "undefined") {
        if(!xmlhttp){
            try {
                xmlhttp = new ActiveXObject("MSXML2.XMLHTTP");
            } 
            catch (e) {
                xmlhttp = false;
            }
        }
        if(!xmlhttp){
            try {
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            } 
            catch (e) {
                xmlhttp = false;
            }
        }
    }
    xmlhttp.open('GET', url, false);
    xmlhttp.send("");
    var xmlDoc;
    if (window.DOMParser) {
        parser = new DOMParser();
        xmlDoc = parser.parseFromString(xmlhttp.responseText, "text/xml");
    }
    else {
        xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
        xmlDoc.async = false;
        xmlDoc.loadXML(xmlhttp.responseText);
    	if (xmlDoc.parseError.errorCode != 0){
    		console.log(xmlDoc.parseError.errorCode + "\n" +  xmlDoc.parseError.reason);
    	}
    }
    return xmlDoc;
};


/**
 * returns the json object of the file of the given url
 * @param {String} url the url of the file to parse
 * @return json object of the given file
*/
STIStatic.getJsonDoc = function(url){
    var xmlhttp = false;
    if(!xmlhttp){
        try {
            xmlhttp = new XMLHttpRequest();
        } 
        catch (e) {
            xmlhttp = false;
        }
    }
    if (typeof ActiveXObject != "undefined") {
        if(!xmlhttp){
            try {
                xmlhttp = new ActiveXObject("MSXML2.XMLHTTP");
            } 
            catch (e) {
                xmlhttp = false;
            }
        }
        if(!xmlhttp){
            try {
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            } 
            catch (e) {
                xmlhttp = false;
            }
        }
    }
    xmlhttp.open('GET', url, false);
    xmlhttp.send("");
    var jsonDoc = new Function("return " + xmlhttp.responseText)();
    return jsonDoc;
};


/**
 * returns a Date and a SimileAjax.DateTime granularity value for a given XML time
 * @param {String} xmlTime the XML time as String
 * @return JSON object with a Date d and a SimileAjax.DateTime granularity g
*/
STIStatic.getTimeData = function( xmlTime ){
	var dateData;
	try {
		var bc = false;
		if( xmlTime.startsWith("-") ){
			bc = true;
			xmlTime = xmlTime.substring( 1, xmlTime.length-1 );
		}
		var timeSplit = xmlTime.split("T");
		var timeData = timeSplit[0].split("-");
		for (var i = 0; i < timeData.length; i++) {
			parseInt(timeData[i]);
		}
		if( bc ){
			timeData[0] = "-"+timeData[0];
		}
		if (timeSplit.length == 1) {
			dateData = timeData;
		}
		else {
			var dayData;
			if (timeSplit[1].indexOf("Z") != -1) {
				dayData = timeSplit[1].substring(0, timeSplit[1].indexOf("Z") - 1).split(":");
			}
			else {
				dayData = timeSplit[1].substring(0, timeSplit[1].indexOf("+") - 1).split(":");
			}
			for (var i = 0; i < timeData.length; i++) {
				parseInt(dayData[i]);
			}
			dateData = timeData.concat(dayData);
		}
	} 
	catch (exception) {
		return null;
	}
	var date, granularity;
	if (dateData.length == 6) {
		granularity = SimileAjax.DateTime.SECOND;		
    	date = new Date(Date.UTC(dateData[0], dateData[1]-1, dateData[2], dateData[3], dateData[4], dateData[5]));
	}
	else if (dateData.length == 3) {
		granularity = SimileAjax.DateTime.DAY;
    	date = new Date(Date.UTC(dateData[0], dateData[1]-1, dateData[2]));                	
	}
	else if (dateData.length == 2) {
		granularity = SimileAjax.DateTime.MONTH;
       	date = new Date(Date.UTC(dateData[0], dateData[1]-1, 1));
  	}
	else if (dateData.length == 1) {
		granularity = SimileAjax.DateTime.YEAR;
       	date = new Date(Date.UTC(dateData[0], 0, 1));
  	}
	return { d: date, g: granularity };
};


/**
 * converts a JSON array into an array of map objects
 * @param {String} JSON a JSON array of spatial objects
 * @return an array of map objects
*/
STIStatic.loadSpatialJSONData = function(JSON){
	var mapObjects = [];
	for( i in JSON ){
		try {
			var item = JSON[i];
			var name = item.name || "";
			var description = item.description || "";
			var place = item.place || "unknown";
			var lon = item.lon || "";
			var lat = item.lat || "";
        	if( lon == "" || lat == "" || isNaN(lon) || isNaN(lat) ){
        		throw "e";
        	}
			var weight = item.weight || "1";
			mapObjects.push(new MapObject(name,description,place,lon,lat,weight));			
		}
		catch(e){
			continue;
		}
	}
	return mapObjects;
};


/**
 * converts a JSON array into an array of time objects
 * @param {String} JSON a JSON array of temporal objects
 * @return an array of time objects
*/

STIStatic.loadTemporalJSONData = function(JSON){
	var timeObjects = [];
	for( i in JSON ){
		try {
			var item = JSON[i];
			var name = item.name || "";
			var description = item.description || "";
			var timeStart, timeEnd, granularity;
			var time1 = STIStatic.getTimeData(item.time);
			if( time1 == null ){
				continue;
			}
			timeStart = time1.d;
			granularity = time1.g;
			var time2 = STIStatic.getTimeData(item.time2);
			if( time2 == null ){
				time2 = time1;
			}
			timeEnd = time2.d;
			if( time2.g > granularity ){
				granularity = time2.g;
			}
			var weight = item.weight || "1";
			timeObjects.push(new TimeObject(name,description,timeStart,timeEnd,granularity,weight));			
		}
		catch(e){
			continue;
		}
	}
	return timeObjects;
};


/**
 * converts a JSON array into an array of map time objects
 * @param {String} JSON a JSON array of spatio-temporal objects
 * @return an array of map time objects
*/
STIStatic.loadSpatioTemporalJSONData = function(JSON){
	var mapTimeObjects = [];
	for( i in JSON ){
		try {
			var item = JSON[i];
			var name = item.name || "";
			var description = item.description || "";
			var place = item.place || "unknown";
			var lon = item.lon || "";
			var lat = item.lat || "";
        	if( lon == "" || lat == "" || isNaN(lon) || isNaN(lat) ){
        		throw "e";
        	}
			var timeStart, timeEnd, granularity;
			var time1 = STIStatic.getTimeData(item.time);
			if( time1 == null ){
				continue;
			}
			timeStart = time1.d;
			granularity = time1.g;
			var time2 = STIStatic.getTimeData(item.time2);
			if( time2 == null ){
				time2 = time1;
			}
			timeEnd = time2.d;
			if( time2.g > granularity ){
				granularity = time2.g;
			}
			var weight = item.weight || "1";
			mapTimeObjects.push(new MapTimeObject(name,description,place,lon,lat,timeStart,timeEnd,granularity,weight));
		}
		catch(e){
			continue;
		}
	}
	return mapTimeObjects;
};


/**
 * parses a KML-File and returns a Dataset object containing the KML-Placemarks as data objects
 * @param {URL} url URL of some KML-File
 */
STIStatic.loadKML = function(url){
	var xmlDoc = STIStatic.getXmlDoc(url);
	var elements = xmlDoc.getElementsByTagName("Placemark");
    if( elements.length == 0 ){
    	return;
    }    


	var extData = xmlDoc.getElementsByTagName("ExtendedData");
	if(extData && extData.length > 0){
		var data = extData[0].getElementsByTagName("Data");
		jQuery(data).each(function(index, node){
			if(node.getAttribute("name") == "totalResults"){
				STIStatic.resultSize = parseInt(node.getElementsByTagName("value")[0].childNodes[0].nodeValue);
			}
		});
	}
	
    
    var dataset = new Dataset();
    for (var i = 0; i < elements.length; i++) {
        var placemark = elements[i];
		var name, description, place, timeStart, timeEnd, granularity, lon, lat;
		var weight = 1;
		var timeData = false, mapData = false;		
		try {
			name = placemark.getElementsByTagName("name")[0].childNodes[0].nodeValue;
		}
		catch(e){
			name = "";
		}		
		try {
			description = placemark.getElementsByTagName("description")[0].childNodes[0].nodeValue;
		}
		catch(e){
			description = "";
		}
		try {
			place = placemark.getElementsByTagName("address")[0].childNodes[0].nodeValue;
		}
		catch(e){
			place = "";
		}
		try {
			var coordinates = placemark.getElementsByTagName("Point")[0].getElementsByTagName("coordinates")[0].childNodes[0].nodeValue;
        	var lonlat = coordinates.split(",");
        	lon = lonlat[0];
        	lat = lonlat[1];
        	if( lon == "" || lat == "" || isNaN(lon) || isNaN(lat) ){
        		throw "e";
        	}
			mapData = true;
		}
		catch(e){
			if( !STIStatic.incompleteData ){
				continue;
			}
		}
		try {
			var tuple = STIStatic.getTimeData(placemark.getElementsByTagName("TimeStamp")[0].getElementsByTagName("when")[0].childNodes[0].nodeValue);
			if( tuple != null ){
				timeStart = tuple.d;
				timeEnd = timeStart;
				granularity = tuple.g;
				timeData = true;
			}
			if( timeStart == undefined && !STIStatic.incompleteData ){
				continue;
			}
		}
		catch(e){
			try {
				var timeSpanTag = placemark.getElementsByTagName("TimeSpan")[0];
				var tuple1 = STIStatic.getTimeData(timeSpanTag.getElementsByTagName("begin")[0].childNodes[0].nodeValue);
				timeStart = tuple1.d;
				granularity = tuple1.g;
				var tuple2 = STIStatic.getTimeData(timeSpanTag.getElementsByTagName("end")[0].childNodes[0].nodeValue ); 
				timeEnd = tuple2.d;
				if( tuple2.g > granularity ){
					granularity = tuple2.g; 
				}
				timeData = true;
			}
			catch(e){
				if( !STIStatic.incompleteData ){						
					continue;
				}
			}
		}
		var object;
		if( timeData && mapData ){
			object = new MapTimeObject(name, description, place, lon, lat, timeStart, timeEnd, granularity, 1);
		}
		else if( mapData ){
			object = new MapObject(name, description, place, lon, lat, 1);
		}
		else if( timeData ){
			object = new TimeObject(name, description, timeStart, timeEnd, granularity, 1);
		}
		dataset.addObject(object);		
    }
    if( dataset.getObjects().length == 0 ){
    	console.error('no kml dataset provided');
    	return null;
    }
    return dataset;
};





/**
 * parses a JSON-File and returns a Dataset object containing the JSON-Placemarks as data objects
 * @param {URL} url URL of some KML-File
 */
STIStatic.loadJSON = function(jsonDoc){
	var elements = jsonDoc.Document;
    STIStatic.resultSize = parseInt(jsonDoc.totalResults);
	
    var dataset = new Dataset();
    for (var i = 0; i < elements.length; i++) {
        var placemark = elements[i].Placemark;
		var name = "", description = "", place = "", lon = 0, lat = 0, link, timeStart = "", timeEnd = "", granularity;
		var timeData = false, mapData = false;
		
		if(placemark.name){
			name = placemark.name;
		}
		else{
			name = "no name available";
		}		
		
		if(placemark.description){
			description = placemark.description;
		}
		else{
			description = "no description available";
		}
		
		if(placemark.address){
			place = placemark.address;
		}
		else{
			place = eu.europeana.vars.mapview.no_place;
		}
		
		if(placemark.timeStart){
			timeStart = placemark.timeStart; 
		}
		
		if(placemark.link){
			link = placemark.link; 
		}
		
		try {
			var coordinates = placemark.Point.coordinates;
        	var lonlat = coordinates.split(",");
        	lon = lonlat[0];
        	lat = lonlat[1];
        	if( lon == "" || lat == "" || isNaN(lon) || isNaN(lat) ){
        		throw "e";
        	}
			mapData = true;
		}
		catch(e){
			if( !STIStatic.incompleteData ){
				continue;
			}
		}
		
		
		var object;
		if( timeData && mapData ){
			object = new MapTimeObject(name, description, place, lon, lat, timeStart, timeEnd, granularity, 1);
		}
		else if( mapData ){
			object = new MapObject(name, description, place, lon, lat, 1);
		}
		else if( timeData ){
			object = new TimeObject(name, description, timeStart, timeEnd, granularity, 1);
		}
		dataset.addObject(object);		
    }
    if( dataset.getObjects().length == 0 ){
    	console.error('no json dataset provided');
    	return null;
    }
    return dataset;
};



