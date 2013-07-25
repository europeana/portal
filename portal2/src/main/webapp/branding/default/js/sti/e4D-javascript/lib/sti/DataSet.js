/**
 * data set class definition
 * 
 * @constructor
 */
function Dataset(){

    this.mapTimeObjects = [];
    this.mapObjects = [];
    this.timeObjects = [];
	
}

Dataset.prototype = {

   /**
    * Getter for all objects with spatial information
    * @return an array of data objects with spatial information
    */
    getMapObjects: function(){
    	return this.mapObjects.concat(this.mapTimeObjects);
    },

    /**
     * Getter for all objects with temporal information
     * @return an array of data objects with temporal information
     */
    getTimeObjects: function(){
    	return this.timeObjects.concat(this.mapTimeObjects);
    },

    /**
     * Getter for all objects
     * @return an array of all data objects
     */
    getObjects: function(){
    	return this.timeObjects.concat(this.mapTimeObjects).concat(this.mapObjects);
    },

    /**
     * adds an object to the data set
     * @param {DataObject} object object to add
     */
    addObject: function(object){
    	if( object instanceof MapObject ){
    		this.mapObjects.push(object);
    	}
    	else if( object instanceof TimeObject ){
        	this.timeObjects.push(object);
    	}
    	else if( object instanceof MapTimeObject ){
    		this.mapTimeObjects.push(object);
    	}
    },

    /**
     * Sets an ID for this dataset (may be used in table header)
     * @param {String} id the id for the dataset
     */
    setID: function(id){
    	this.id = id;
    },
    
    /**
     * Getter for the ID of the dataset
     * @return the ID of the dataset
     */
    getID: function(){
    	if( this.id == undefined ){
    		return "Unknown ID";
    	}
    	return this.id;
    }
    
}