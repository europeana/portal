/**
 * describes a temporal point object, that can save elements of different data sets for one place 
 * 
 * @constructor
 */
PointObject = function(){

    this.x;
    this.y;
    
    this.elements = [];
    this.size = 0;
    this.radius;
    
};

PointObject.prototype = {

    /**
     * adds an elements with a specific searchIndex to this point object
     * @param {int} searchIndex the search index of the actual dataset constellation
     * @param {DataObject} element the data object to add
     */
    addElement: function(searchIndex, element){
        while (this.elements.length - 1 < searchIndex) 
            this.elements.push([]);
        this.elements[searchIndex].push(element);
        this.size++;
    },
    
    /**
     * sets x (longitude) and y (latitude) values to this point object
     * @param {float} x the x (longitude) value
     * @param {float} y the x (latitude) value
     */
    setXY: function(x, y){
        this.x = x;
        this.y = y;
    },
    
    /**
     * sets the radius of this point object
     * @param {int} radius the radius to set
     */
    setRadius: function(radius){
        this.radius = radius;
    },
    
    /**
     * adds another point object to this point object.
     * here all elements of the other point object are inserted in the element array of this instance
     * @param {PointObject} point another point object.
     */
    addPoint: function(point){
        for (var i = 0; i < point.elements.length; i++) 
            for (var j = 0; j < point.elements[i].length; j++) 
                this.addElement(i, point.elements[i][j]);
    }
    
};

/**
 * describes a finally displayed point object.
 * each instance of this class corresponds to a specific place and to only one data set 
 * @param {float} x the x (longitude) value of the point object 
 * @param {float} y the y (latitude) value of the point object
 * @param {DataObject[]} elements array of data objects, that belong a point object instance
 * @param {int} radius the resulting radius (in pixel) of the point in the map  
 * @param {int} search corresponding search index of the elements
 * @param {int} weight weight of the elements of this dpo
 * 
 * @constructor
 */
DisplayPointObject = function(originX, originY, shiftX, shiftY, elements, radius, search, weight){

    this.originX = originX;
    this.originY = originY;
    this.shiftX = shiftX;
    this.shiftY = shiftY;
    this.elements = elements;
    this.radius = radius;
    this.search = search;
    this.weight = weight;

    this.pointFeature;
    this.olPointFeature;
	this.percentage = 0;
	this.selected = false;
    
};

DisplayPointObject.prototype = {
	
    /**
     * sets the OpenLayers point feature for this point object
     * @param {OpenLayers.Feature} pointFeature the point feature for this object
     */
    setPointFeature: function(pointFeature){
        this.pointFeature = pointFeature;
    },
    
    /**
     * sets the OpenLayers point feature for this point object to manage its selection status
     * @param {OpenLayers.Feature} olPointFeature the overlay point feature for this object
     */
    setOlPointFeature: function(olPointFeature){
        this.olPointFeature = olPointFeature;
    }

};
