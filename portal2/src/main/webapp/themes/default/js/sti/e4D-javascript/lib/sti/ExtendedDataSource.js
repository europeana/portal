/**
 * small class that represents a time slice of the actual timeplot.
 * it has a specific date and contains its corrsponding data objects as well
 * @param {Date} date the date of the timeslice
 * 
 * @constructor
*/
function TimeSlice(date,rows){

    this.date = date;
	this.selected = false;

    this.elements = [];
    this.values = [];
    this.overlays = [];
    for( var i=0; i<rows; i++ ){
    	this.elements.push([]);
    	this.values.push(0);
    	this.overlays.push(0);
    }
	
};

TimeSlice.prototype.addObject = function(row,object){
	this.elements[row].push(object);
	this.values[row] += object.weight;
};

TimeSlice.prototype.reset = function(){
	for( i in this.overlays ){
		this.overlays[i] = 0;
	}
};

TimeSlice.prototype.overlay = function(){
	var value = 0;
	for( i in this.overlays ){
		if( this.overlays[i] > value ){
			value = this.overlays[i];
		}
	}
	return value;
};

/**
 * data source to allow dynamic and manual creation of time slices, which will be used as input for the Simile Timeplot.
 * at the end we will get around 200 time slices, which units are depending on the time range of the input data
 * 
 * @constructor
*/
function ExtendedDataSource(){

    this.timeSlices;
	this.unit;
	this.minDate;
	this.maxDate;
	this.eventSources;
	this.events;
	this.leftSlice;
	this.rightSlice;
    
};

ExtendedDataSource.prototype = {

	/**
 	* initializes the ExtendedDataSource
 	* @param {Timeplot.ColumnSource[]} dataSources the column sources corresponding to the data sets
 	* @param {Timeplot.DefaultEventSource[]} eventSources the event sources corresponding to the column sources
    * @param {TimeObject[][]} timeObjects an array of time objects of different sets
 	* @param {SimileAjax.DateTime} granularity the time granularity of the given data
	*/
	initialize: function( dataSources, eventSources, timeObjects, granularity, timeUnit ){
		        
		this.minDate = undefined;
		this.maxDate = undefined;
		        
        for (var i = 0; i < timeObjects.length; i++) {
            for (var j = 0; j < timeObjects[i].length; j++) {
                var o = timeObjects[i][j];
                var timeMin = o.timeStart;
                var timeMax = o.timeEnd;
                if (this.minDate == undefined || timeMin.getTime() < this.minDate.getTime()){
                    this.minDate = timeMin;
                } 
                if (this.maxDate == undefined || timeMax.getTime() > this.maxDate.getTime()){
                    this.maxDate = timeMax;
                }
            }
        }
        this.timeSlices = [];
        
        var time = SimileAjax.DateTime;
        var u = SimileAjax.NativeDateUnit;
        var p = this.maxDate - this.minDate;
		
		var periodUnit = -1;
		do {
			periodUnit++;
		}
		while( time.gregorianUnitLengths[periodUnit] < p );

		switch (granularity){
			case time.MILLISECOND:
			case time.SECOND:
				if( periodUnit > time.QUARTER )
					this.unit = periodUnit - 7;
				else if( periodUnit > time.TWOWEEKS )
					this.unit = time.DAY;
				else if( periodUnit > time.HOUR )
					this.unit = periodUnit - 5;
				else if( periodUnit > time.HALFMINUTE )					
					this.unit = periodUnit - 4;
				else
					this.unit = time.SECOND;
				break;
			case time.DAY:
				if( periodUnit > time.QUARTER )
					this.unit = periodUnit - 7;
				else 
					this.unit = time.DAY;
				break;
			case time.MONTH:
				if( periodUnit > time.DECADE )
					this.unit = periodUnit - 7;
				else 
					this.unit = time.MONTH;
				break;
			case time.YEAR:
				if( periodUnit > time.CENTURY )
					this.unit = periodUnit - 7;
				else 
					this.unit = time.YEAR;
				break;
		}

		if( this.unit < timeUnit ){
			this.unit = timeUnit;
		}

        var timeZone;
        var t = new Date(this.minDate.getTime() - 0.9 * time.gregorianUnitLengths[this.unit]);
        do {
            time.roundDownToInterval(t, this.unit, timeZone, 1, 0);
            var slice = new TimeSlice(u.cloneValue(t),timeObjects.length);
            this.timeSlices.push(slice);
            time.incrementByInterval(t, this.unit, timeZone);
        }
        while (t.getTime() <= this.maxDate.getTime() + 1.1 * time.gregorianUnitLengths[this.unit]);
        
		for(var i=0; i<timeObjects.length; i++ ){
            for (var j = 0; j < timeObjects[i].length; j++) {
                var o = timeObjects[i][j];
                var timeMin = o.timeStart;
                var timeMax = o.timeEnd;
				for( var k=0; k<this.timeSlices.length-1; k++ ){
					var t1 = this.timeSlices[k].date.getTime();
					var t2 = this.timeSlices[k+1].date.getTime();
					if( ( timeMin >= t1 && timeMin < t2 ) || ( timeMax >= t1 && timeMax < t2 ) || ( timeMin <= t1 && timeMax >= t2 ) ){
						this.timeSlices[k].addObject(i,o);
					}
					if( k == this.timeSlices.length-2 && ( timeMin >= t2 || timeMax >= t2 ) ){
						this.timeSlices[k+1].addObject(i,o);
					}
				}
			}
		}

		this.events = [];
		for(var i=0; i<eventSources.length; i++ ){
			var eventSet = [];
			for(var j=0; j<this.timeSlices.length; j++ ){
				var value = new Array( ""+this.timeSlices[j].values[i] );
				eventSet.push( { date: this.timeSlices[j].date, value: value } );
			}
			eventSources[i].loadData( eventSet );
			this.events.push(eventSet);
		}
		
		this.eventSources = eventSources;
		
		this.leftSlice = 0;
		this.rightSlice = this.timeSlices.length - 1;
		
	},
	
	/**
 	* computes the slice index corresponding to a given time
 	* @param {Date} time the given time
 	* @return the corresponding slice index
	*/
	getSliceIndex: function( time ){
		for(var i=0; i<this.timeSlices.length; i++ ){
			if( time == this.timeSlices[i].date ){
				return i;
			}
		}
	},
	
	/**
 	* returns the time of a specific time slice
 	* @param {int} time the given slice index
 	* @return the corresponding slice date
	*/
	getSliceTime: function( index ){
		return this.timeSlices[index].date;
	},

	/**
 	* shifts the actual zoomed range
 	* @param {int} delta the value to shift (negative for left shift, positive for right shift)
 	* @return boolean value, if the range could be shifted
	*/
	setShift: function( delta ){
		if( delta == 1 && this.leftSlice != 0 ){
			this.leftSlice--;
			this.rightSlice--;
			return true;
		}	
		else if( delta == -1 && this.rightSlice != this.timeSlices.length-1 ){
			this.leftSlice++;
			this.rightSlice++;
			return true;
		}	
		else {
			return false;
		}
	},
	
	/**
 	* zooms the actual range
 	* @param {int} delta the value to zoom (negative for zoom out, positive for zoom in)
 	* @param {Date} time the corresponding time of the actual mouse position on the plot
 	* @param {Date} leftTime the time of the left border of a selected timerange or null
 	* @param {Date} rightTime the time of the right border of a selected timerange or null
 	* @return boolean value, if the range could be zoomed
	*/
	setZoom: function( delta, time, leftTime, rightTime ){
		var n1 = 0;
		var n2 = 0;
		var m = -1;
		if( delta > 0 ){
			m = 1;
			if( leftTime != null ){
				n1 = this.getSliceIndex(leftTime) - this.leftSlice;
				n2 = this.rightSlice - this.getSliceIndex(rightTime);
			}
			else {
				slice = this.getSliceIndex(time);
				if( slice == this.leftSlice || slice == this.rightSlice ){
					return;
				}
				n1 = slice - 1 - this.leftSlice;
				n2 = this.rightSlice - slice - 1;
			}
		}
		else if( delta < 0 ){
			n1 = this.leftSlice;
			n2 = this.timeSlices.length - 1 -this.rightSlice;
		}
				
		var zoomSlices = 2*delta;
		if( Math.abs(n1 + n2) < Math.abs(zoomSlices) ){
			zoomSlices = n1 + n2;
		}

		if( n1 + n2 == 0 ){
			return false;
		}

		var m1 = Math.round( n1 / (n1 + n2) * zoomSlices );
		var m2 = zoomSlices - m1;

		this.leftSlice += m1;
		this.rightSlice -= m2;
		 
		return true;
	},
	
	/**
 	* resets the plots by loading data of actual zoomed range
	*/
	reset: function( timeGeometry ){
		for(var i=0; i<this.eventSources.length; i++ ){
			this.eventSources[i].loadData( this.events[i].slice( this.leftSlice, this.rightSlice + 1) );
			if( i+1 < this.eventSources.length ){
				timeGeometry._earliestDate = null;
				timeGeometry._latestDate = null;
			}
		}
	},
	
	/**
 	* Getter for actual zoom
 	* @return actual zoom value
	*/
	getZoom: function(){
		if( this.timeSlices == undefined ){
			return 0;
		}
		return Math.round((this.timeSlices.length-3)/2 ) - Math.round((this.rightSlice-this.leftSlice-2)/2); 
	},
	
	/**
 	* Getter for date of the first timeslice
 	* @return date of the first timeslice
	*/
	earliest: function(){
		return this.timeSlices[0].date;
	},
	
	/**
 	* Getter for date of the last timeslice
 	* @return date of the last timeslice
	*/
	latest: function(){
		return this.timeSlices[ this.timeSlices.length - 1 ].date;
	}
			
};
