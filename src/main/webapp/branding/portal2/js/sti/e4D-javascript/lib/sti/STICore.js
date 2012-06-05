/**
 * defines the core component of the Spatio Temporal Interface
 *
 * @constructor
 */
function STICore(){

    this.map;
    this.timeplot;
    this.tables;
    
    this.refining;
    
    this.history;
    this.historyIndex;
    
    this.initialize();
    
};

STICore.prototype = {

	    /**
	     * initializes the core component for the Spatio Temporal Interface.
	     * here, the handling of the search interface is defined (including undo, refine and clear selection button).
	     * furthermore, the elements (map, timeplot, tables) are instanciated.
	     */
	    initialize: function(){
	    
	        var context = this;

	        this.history = [];
	        
	        this.history.push( new HistoryEntry([]) );
	        this.historyIndex = 0;
	        
			this.gui = new STIGui(this);
			this.gui.initialize();
			
	    },
		
	/**
 	* refines the given data by creating a new history entry
 	*/
	refine: function(){
            if (this.refining > -2) {
            	var contains = false;
				var newDataSets = [];
				var oldDataSets = this.history[this.historyIndex].datasets;
            	for (var i = 0; i < oldDataSets.length; i++) {
					var dataSet = new Dataset();
					dataSet.setID(oldDataSets[i].getID());
					var objects = oldDataSets[i].getObjects();
                	for (var j = 0; j < objects.length; j++) {
                    	if ( objects[j].percentage == 1 || this.refining > -1 && this.refining != i ){
							dataSet.addObject( objects[j] );
                        	contains = true;
						}
                	}
					newDataSets.push(dataSet);            			
            	}
            	if (contains) {
           			this.addHistoryEntry( new HistoryEntry(newDataSets) );
                    this.reset();
                    this.initElements();
                }
                else {
                    alert("Your Selection contains no elements!");
                }
            }
            else {
                alert("For Refining choose an Area on the map or a Range on the Timeplot!");
			}
			return -1;
	},
	
	/**
 	* adds selected elements of a specific dataset as a new dataset
 	* @param {int} id the id of the dataset
 	*/
	storeSelected: function(id){
		var datasets = this.history[this.historyIndex].datasets;
		if( datasets.length == STIStatic.maxDatasets ){
			alert( "The maximum number of "+ STIStatic.maxDatasets +" parallel datasets is reached!" );
		}
		else {
			var dataSet = datasets[id].copy();
       		var contains = false;
           	for (var j = 0; j < datasets[id].objects.length; j++) {
    	       	if ( datasets[id].objects[j].percentage == 1 ){
					dataSet.addObject( datasets[id].objects[j] );
                   	contains = true;
				}
           	}
       		if (contains) {
				this.addDataSet(dataSet);
			}
        	else {
        		alert("Your Selection contains no elements!");
        	}
      	}
	},

	/**
     * Adds a dataset to the actual history entry
 	*/
	addDataSet: function( dataSet ){
		var oldDataSets = this.history[this.historyIndex].datasets;
		var newDataSets = oldDataSets.concat(dataSet);
		this.addHistoryEntry(new HistoryEntry(newDataSets));
		this.initElements();
        this.reset();
	},
        
    loadUnlinkedDataset: function(){    	
    	var ds = new UnlinkedDataset();
    	for( i in mapObjects ){
    		var o = mapObjects[i];
    		ds.addMapObject(new MapObject("","",o.value,o.lon,o.lat,o.count));
    	}
    	for( i in timeObjects ){
    		var o = timeObjects[i];
    		var tuple = STIStatic.getTimeData(o.value);
    		ds.addTimeObject(new TimeObject("","",tuple.d,tuple.d,tuple.g,o.count));
    	}
        this.addDataSet( ds );
    },
    
    /**
     * constructs an url to a dynamic datasource with the specific user input as attribute
     * @param {int} ds the datasource index
     * @param {String} input the user input
     */
    retrieveKml: function(ds,input){
    	var url = this.sources[ds].url;
		url += input;
    	var dataset = STIStatic.loadKML(url);
        dataset.setID(input);
        this.addDataSet(dataset);
    },
    
    loadKml: function(url,term){
    	var dataset = STIStatic.loadKML(url);
    	if ( !dataset ) { console.error('KML is empty or malformed'); this.kmlLoaded = false; return; }
    	this.kmlLoaded = true;
    	if( term != undefined ){
            dataset.setID(term);
    	}
        this.addDataSet(dataset);
    },
    
    loadJson: function(url,term){
    	var jsonDoc = STIStatic.getJsonDoc(url);
    	if(jsonDoc.totalResults == 0 || (typeof jsonDoc.Document.elements != 'undefined' && jsonDoc.Document.elements.length == 0)){
   			console.error('JSON is empty or malformed');
   			this.jsonLoaded = false;
    	}
    	else{    		
    		var dataset = STIStatic.loadJSON(jsonDoc);
    		this.jsonLoaded = true;
    		if( term != undefined ){
    			dataset.setID(term);
    		}
    		this.addDataSet(dataset);
    	}
    },
    
    /**
     * updates the timeplot and table element.
     * its called from the STIMap object, when objects on the map had been selected by featureSelect or polygon.
     * @param {boolean} hover true, if there was a hover selection
     */
    updateTimeAndTable: function(hover){
    	if( STIStatic.tables ){
			this.tables.update(hover);
		}
		if( STIStatic.timeplot ){
        	this.timeplot.polesBySlices(false);
        }
        this.refining = -1;
    },
    
    /**
     * updates the timeplot and map element.
     * its called from the STITable object, when objects in one of the tables had been selected.
     */
    updateTimeAndMap: function(){
		if( STIStatic.map ){
	        this.map.updateMap();
		}
		if( STIStatic.timeplot ){
        	this.timeplot.polesBySlices(false);
        }
        this.refining = -1;
    },
    
    /**
     * updates the table and map element.
     * its called from the STITimeplot object, when objects in the timeplot had been selected by timestamp or -range.
     * @param {boolean} hover true, if there was a hover selection
     */
    updateTableAndMap: function(hover){
    	if( STIStatic.tables ){
			this.tables.update(hover);
		}
		if( STIStatic.map ){
	        this.map.updateMap();
		}
        this.refining = -1;
    },
    
    /**
     * initializes the sti components (map, timeplot, table) depending on the top masks of the data sets.
     * its called after a new search was performed, refining or undo button had been clicked
     */
    initElements: function(){
    	this.reset();
    	
    	var datasets = this.history[this.historyIndex].datasets;
    	var timeObjects = [];
    	var mapObjects = [];
    	for( var i=0; i<datasets.length; i++ ){
    		mapObjects.push(datasets[i].getMapObjects());
    		timeObjects.push(datasets[i].getTimeObjects());
    	}
    	if( STIStatic.tables ){
			this.tables.init(datasets);
		}
		if( STIStatic.timeplot ){
    		this.timeplot.initTimeplot(timeObjects);
        }
		if( STIStatic.map ){
        	this.map.initMap(mapObjects);
		}
		if( STIStatic.history ){
	        this.gui.updateHistory();
		}
    },
    
    /**
     * deletes a data set with specific index
     * @param {int} index the index of the data set to delete
     */
    deleteDataSet: function(index){
    	var color = colors[index];
        colors.splice(index, 1);
    	var oldDataSets = this.history[this.historyIndex].datasets;
    	var newDataSets = [];
    	for( var i=0; i<oldDataSets.length; i++ ){
    		if( i != index ){
    			newDataSets.push( oldDataSets[i] );
    		}
    	}
		colors.splice( newDataSets.length, 0, color );
		this.addHistoryEntry(new HistoryEntry(newDataSets));
		this.initElements();
        this.reset();
   	},

    /**
     * Switches to another history entry with the given index
     * @param {int} index the index of the history entry to load
    */
	switchThroughHistory: function( index ){
		this.historyIndex = index;
		this.initElements();
	},
	
    /**
     * Adds a new history entry containing actual datasets
     * @param {HistoryEntry} historyEntry the history entry to add
    */
	addHistoryEntry: function( historyEntry ){	
		this.history = this.history.slice(0,this.historyIndex+1);
		this.history.push(historyEntry);
		this.historyIndex = this.history.length - 1;
//GWT		addHistoryItem(this.historyIndex);
	},
   
    /**
     * resets the core within all elements and data objects to non-selection-status
     */
    reset: function(){
        this.refining = -2;
        var datasets = this.history[this.historyIndex].datasets;
        for (var i = 0; i < datasets.length; i++){ 
        	var objects = datasets[i].getObjects();
            for (var j = 0; j < objects.length; j++){
                objects[j].setPercentage(0);
				objects[j].setHover(false);
			}
		}
    	if( STIStatic.tables ){
			this.tables.reset();
		}
		if( STIStatic.timeplot ){
        	this.timeplot.resetTimeplot();
        }
		if( STIStatic.map ){
        	this.map.resetMap();
		}
    },
	
    /**
     * Security hover unselection of elements if browser events got stucked
    */
	undoHover: function(update){
		if( STIStatic.timeplot ){
			this.timeplot.hoverUnselect(update);
        }
		if( STIStatic.map ){
			this.map.hoverUnselect();
		}
	}
	    
};

/**
 * defines a history entry
 * @param {DataSet[]} datasets the datasets of this history entry
 * 
 * @constructor
 */
function HistoryEntry( datasets ){
	this.datasets = datasets;
};