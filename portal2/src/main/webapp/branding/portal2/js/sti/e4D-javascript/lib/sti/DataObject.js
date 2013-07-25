/**
 * abstract class with all features needed for user interaction and informations to the data object
 * @param {String} name the name of the data object
 * @param {String} description description of the data object
 * @param {int} weight weight of the given object
 * 
 * @constructor
 */
DataObject = function(name, description, weight){

    this.name = name;
    this.description = description;
    this.weight = weight;

    this.status = false;
    this.percentage = 0;
	this.hoverSelect = false;
    
};

DataObject.prototype = {

    /**
     * sets the selection-percentage of the data object
     * @param {float} percentage sets the percentage value (p); it describes the ratio to the actual selection <br>
     * p = 0 if this data object is unselected, p = 1 if it is selected and 0 < p < 1 if its in a feather range 
     */
    setPercentage: function(percentage){
        if (this.percentage == percentage) 
            this.status = false;
        else {
            this.percentage = percentage;
            this.status = true;
        }
    },
    
    /**
     * sets the object to a hover selection status
     * @param {boolean} hover bool value that tells if an object is hovered or not
     */
    setHover: function(hover){
    	this.hoverSelect = hover;
    	if( this.percentage != 1 ){
    		this.status = true;
    	} 
    }
    
};

/**
 * map object class
 * @param {String} place place of the data object
 * @param {float} lon longitude value of the given place 
 * @param {float} lat latitude value of the given place
 * @param {int} weight weight of the map object
 * 
 * @constructor
 */
MapObject = function(name, description, place, lon, lat, weight){

	this.base = DataObject;
	this.base(name, description, weight);
	
    this.longitude = lon;
    this.latitude = lat;

    this.placeDetail = place.split("/");
    this.place = "";
    for( var i=0; i<this.placeDetail.length; i++ ){
    	if( i>0 ){
    		this.place += ", ";
    	}
    	this.place += this.placeDetail[i];
    }
    
};

MapObject.prototype = new DataObject;

/**
 * returns the place name for the given level of detail
 * @param {int} level level of detail (depends on map zoom level)
 * @return place name of the given level
 */
MapObject.prototype.getPlace = function(level){
	if( level >= this.placeDetail.length ){
		return this.placeDetail[this.placeDetail.length-1];
	}
	return this.placeDetail[level];
};

/**
 * time object class
 * @param {Date} timeStart start time of the data object
 * @param {Date} timeEnd end time of the data object
 * @param {int} granularity granularity of the given time
 * @param {int} weight weight of the time object
 * 
 * @constructor
 */
TimeObject = function(name, description, timeStart, timeEnd, granularity, weight){

	this.base = DataObject;
	this.base(name, description, weight);

	this.timeStart = timeStart;
    this.timeEnd = timeEnd;
	this.granularity = granularity;
    
};

TimeObject.prototype = new DataObject;
   
/**
 * returns the string representation of the objects time
 * @return the string representation of the objects time
 */
TimeObject.prototype.getTimeString = function(){
	if( this.timeStart != this.timeEnd ){
		return ( SimileAjax.DateTime.getTimeString(this.granularity,this.timeStart)+" - "+SimileAjax.DateTime.getTimeString(this.granularity,this.timeEnd));
	}
	else {
		return SimileAjax.DateTime.getTimeString(this.granularity,this.timeStart)+"";
	}
};

/**
 * class that contains all given information of an object with spatial and temporal information
 * @param {String} name name of the data object
 * @param {String} description description of the data object
 * @param {String} place place of the data object
 * @param {float} lon longitude value of the given place 
 * @param {float} lat latitude value of the given place
 * @param {Date} timeStart start time of the data object
 * @param {Date} timeEnd end time of the data object
 * @param {int} granularity granularity of the given time
 * @param {int} weight weight of the time object
 * 
 * @constructor
 */
MapTimeObject = function(name, description, place, lon, lat, timeStart, timeEnd, granularity, weight){

	this.base = DataObject;
	this.base(name, description, weight);

    this.longitude = lon;
    this.latitude = lat;

    this.placeDetail = place.split("/");
    this.place = "";
    for( var i=0; i<this.placeDetail.length; i++ ){
    	if( i>0 ){
    		this.place += ", ";
    	}
    	this.place += this.placeDetail[i];
    }

	this.timeStart = timeStart;
    this.timeEnd = timeEnd;
	this.granularity = granularity;
    
};

MapTimeObject.prototype = new DataObject;

/**
 * returns the place name for the given level of detail
 * @param {int} level level of detail (depends on map zoom level)
 * @return place name of the given level
 */
MapTimeObject.prototype.getPlace = function(level){
	if( level >= this.placeDetail.length ){
		return this.placeDetail[this.placeDetail.length-1];
	}
	return this.placeDetail[level];
};

/**
 * returns the string representation of the objects time
 * @return the string representation of the objects time
 */
MapTimeObject.prototype.getTimeString = function(){
	if( this.timeStart != this.timeEnd ){
		return ( SimileAjax.DateTime.getTimeString(this.granularity,this.timeStart)+" - "+SimileAjax.DateTime.getTimeString(this.granularity,this.timeEnd));
	}
	else {
		return SimileAjax.DateTime.getTimeString(this.granularity,this.timeStart)+"";
	}
};