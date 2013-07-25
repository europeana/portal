SimileAjax.DateTime.MILLISECOND = 0;
SimileAjax.DateTime.SECOND = 1;
SimileAjax.DateTime.FIVESECONDS = 2;
SimileAjax.DateTime.TENSECONDS = 3;
SimileAjax.DateTime.HALFMINUTE = 4;
SimileAjax.DateTime.MINUTE = 5;
SimileAjax.DateTime.FIVEMINUTES = 6;
SimileAjax.DateTime.TENMINUTES = 7;
SimileAjax.DateTime.HALFHOUR = 8;
SimileAjax.DateTime.HOUR = 9;
SimileAjax.DateTime.TWOHOURS = 10;
SimileAjax.DateTime.FOURHOURS = 11;
SimileAjax.DateTime.EIGHTHOURS = 12;
SimileAjax.DateTime.DAY = 13;
SimileAjax.DateTime.TWODAYS = 14;
SimileAjax.DateTime.FOURDAYS = 15;
SimileAjax.DateTime.WEEK = 16;
SimileAjax.DateTime.TWOWEEKS = 17;
SimileAjax.DateTime.MONTH = 18;
SimileAjax.DateTime.QUARTER = 19;
SimileAjax.DateTime.SEMESTER = 20;
SimileAjax.DateTime.YEAR = 21;
SimileAjax.DateTime.TWOYEARS = 22;
SimileAjax.DateTime.LUSTRUM = 23;
SimileAjax.DateTime.DECADE = 24;
SimileAjax.DateTime.TWODECADES = 25;
SimileAjax.DateTime.HALFCENTURY = 26;
SimileAjax.DateTime.CENTURY = 27;
SimileAjax.DateTime.TWOCENTURIES = 28;
SimileAjax.DateTime.HALFMILLENNIUM = 29;
SimileAjax.DateTime.MILLENNIUM = 30;
SimileAjax.DateTime.TWOMILLENNIUMS = 31;
SimileAjax.DateTime.FIVEMILLENNIUMS = 32;
SimileAjax.DateTime.DECAMILLENNIUM = 33;
SimileAjax.DateTime.TWODECAMILLENNIUMS = 34;
SimileAjax.DateTime.FIVEDECAMILLENNIUMS = 35;
SimileAjax.DateTime.HECTOMILLENNIUM = 36;
SimileAjax.DateTime.TWOHECTOMILLENNIUMS = 37;
SimileAjax.DateTime.FIVEHECTOMILLENNIUMS = 38;
SimileAjax.DateTime.MEGENNIUM = 39;
SimileAjax.DateTime.TWOMEGENNIUMS = 40;
SimileAjax.DateTime.FIVEMEGENNIUMS = 41;
SimileAjax.DateTime.DECAMEGENNIUM = 42;
SimileAjax.DateTime.TWODECAMEGENNIUMS = 43;
SimileAjax.DateTime.FIVEDECAMEGENNIUMS = 44;
SimileAjax.DateTime.HECTOMEGENNIUM = 45;
SimileAjax.DateTime.TWOHECTOMEGENNIUMS = 46;
SimileAjax.DateTime.FIVEHECTOMEGENNIUMS = 47;
SimileAjax.DateTime.GIGENNIUM = 48;

SimileAjax.DateTime.gregorianUnitLengths = [];
(function(){
    var d = SimileAjax.DateTime;
    var a = d.gregorianUnitLengths;
    
    a[d.MILLISECOND] = 1;
    a[d.SECOND] = 1000;
    a[d.FIVESECONDS] = a[d.SECOND] * 5;
    a[d.TENSECONDS] = a[d.SECOND] * 10;
    a[d.HALFMINUTE] = a[d.SECOND] * 30;
    a[d.MINUTE] = a[d.SECOND] * 60;
    a[d.FIVEMINUTES] = a[d.MINUTE] * 5;
    a[d.TENMINUTES] = a[d.MINUTE] * 10;
    a[d.HALFHOUR] = a[d.MINUTE] * 30;
    a[d.HOUR] = a[d.MINUTE] * 60;
    a[d.TWOHOURS] = a[d.HOUR] * 2;
    a[d.FOURHOURS] = a[d.HOUR] * 4;
    a[d.EIGHTHOURS] = a[d.HOUR] * 8;
    a[d.DAY] = a[d.HOUR] * 24;
    a[d.TWODAYS] = a[d.DAY] * 2;
    a[d.FOURDAYS] = a[d.DAY] * 4;
    a[d.WEEK] = a[d.DAY] * 7;
    a[d.TWOWEEKS] = a[d.DAY] * 14;
    a[d.MONTH] = a[d.DAY] * 31;
    a[d.QUARTER] = a[d.DAY] * 91;
    a[d.SEMESTER] = a[d.DAY] * 182;
    a[d.YEAR] = a[d.DAY] * 365;
    a[d.TWOYEARS] = a[d.YEAR] * 2;
    a[d.LUSTRUM] = a[d.YEAR] * 5;
    a[d.DECADE] = a[d.YEAR] * 10;
    a[d.TWODECADES] = a[d.YEAR] * 20;
    a[d.HALFCENTURY] = a[d.YEAR] * 50;
    a[d.CENTURY] = a[d.YEAR] * 100;
    a[d.TWOCENTURIES] = a[d.YEAR] * 200;
    a[d.HALFMILLENNIUM] = a[d.YEAR] * 500;
    a[d.MILLENNIUM] = a[d.YEAR] * 1000;
    a[d.TWOMILLENNIUMS] = a[d.YEAR] * 2000;
    a[d.FIVEMILLENNIUMS] = a[d.YEAR] * 5000;
    a[d.DECAMILLENNIUM] = a[d.YEAR] * 10000;
    a[d.TWODECAMILLENNIUMS] = a[d.YEAR] * 20000;
    a[d.FIVEDECAMILLENNIUMS] = a[d.YEAR] * 50000;
    a[d.HECTOMILLENNIUM] = a[d.YEAR] * 100000;
    a[d.TWOHECTOMILLENNIUMS] = a[d.YEAR] * 200000;
    a[d.FIVEHECTOMILLENNIUMS] = a[d.YEAR] * 500000;
    a[d.MEGENNIUM] = a[d.YEAR] * 1000000;
    a[d.TWOMEGENNIUMS] = a[d.YEAR] * 2000000;
    a[d.FIVEMEGENNIUMS] = a[d.YEAR] * 5000000;
    a[d.DECAMEGENNIUM] = a[d.YEAR] * 10000000;
    a[d.TWODECAMEGENNIUMS] = a[d.YEAR] * 20000000;
    a[d.FIVEDECAMEGENNIUMS] = a[d.YEAR] * 50000000;
    a[d.HECTOMEGENNIUM] = a[d.YEAR] * 100000000;
    a[d.TWOHECTOMEGENNIUM] = a[d.YEAR] * 200000000;
    a[d.FIVEHECTOMEGENNIUM] = a[d.YEAR] * 500000000;
    a[d.GIGENNIUM] = a[d.YEAR] * 1000000000;
})();

SimileAjax.DateTime.roundDownToInterval = function(date, intervalUnit, timeZone, multiple, firstDayOfWeek){
	timeZone = (typeof timeZone == 'undefined') ? 0 : timeZone;
    var timeShift = timeZone * SimileAjax.DateTime.gregorianUnitLengths[SimileAjax.DateTime.HOUR];
    
    var date2 = new Date(date.getTime() + timeShift);
    var clearInDay = function(d){
        d.setUTCMilliseconds(0);
        d.setUTCSeconds(0);
        d.setUTCMinutes(0);
        d.setUTCHours(0);
    };
    var clearInYear = function(d){
        clearInDay(d);
        d.setUTCDate(1);
        d.setUTCMonth(0);
    };
    
    switch (intervalUnit) {
        case SimileAjax.DateTime.MILLISECOND:
            var x = date2.getUTCMilliseconds();
            date2.setUTCMilliseconds(x - (x % multiple));
            break;
        case SimileAjax.DateTime.SECOND:
            date2.setUTCMilliseconds(0);
            var x = date2.getUTCSeconds();
            date2.setUTCSeconds(x - (x % multiple));
            break;
        case SimileAjax.DateTime.FIVESECONDS:
            date2.setUTCMilliseconds(0);
            var x = date2.getUTCSeconds();
            date2.setUTCSeconds(x - (x % 5));
            break;
        case SimileAjax.DateTime.TENSECONDS:
            date2.setUTCMilliseconds(0);
            var x = date2.getUTCSeconds();
            date2.setUTCSeconds(x - (x % 10));
            break;
        case SimileAjax.DateTime.HALFMINUTE:
            date2.setUTCMilliseconds(0);
            var x = date2.getUTCSeconds();
            date2.setUTCSeconds(x - (x % 30));
            break;
        case SimileAjax.DateTime.MINUTE:
            date2.setUTCMilliseconds(0);
            date2.setUTCSeconds(0);
            var x = date2.getUTCMinutes();
            date2.setTime(date2.getTime() - (x % multiple) * SimileAjax.DateTime.gregorianUnitLengths[SimileAjax.DateTime.MINUTE]);
            break;
        case SimileAjax.DateTime.FIVEMINUTES:
            date2.setUTCMilliseconds(0);
            date2.setUTCSeconds(0);
            var x = date2.getUTCMinutes();
            date2.setUTCMinutes(x - x % 5);
            break;
        case SimileAjax.DateTime.TENMINUTES:
            date2.setUTCMilliseconds(0);
            date2.setUTCSeconds(0);
            var x = date2.getUTCMinutes();
            date2.setUTCMinutes(x - x % 10);
            break;
        case SimileAjax.DateTime.HALFHOUR:
            date2.setUTCMilliseconds(0);
            date2.setUTCSeconds(0);
            var x = date2.getUTCMinutes();
            date2.setUTCMinutes(x - x % 30);
            break;
        case SimileAjax.DateTime.HOUR:
            date2.setUTCMilliseconds(0);
            date2.setUTCSeconds(0);
            date2.setUTCMinutes(0);
            var x = date2.getUTCHours();
            date2.setUTCHours(x - (x % multiple));
            break;
        case SimileAjax.DateTime.TWOHOURS:
            date2.setUTCMilliseconds(0);
            date2.setUTCSeconds(0);
            date2.setUTCMinutes(0);
            var x = date2.getUTCHours();
            date2.setUTCHours(x - (x % 2));
            break;
        case SimileAjax.DateTime.FOURHOURS:
            date2.setUTCMilliseconds(0);
            date2.setUTCSeconds(0);
            date2.setUTCMinutes(0);
            var x = date2.getUTCHours();
            date2.setUTCHours(x - (x % 4));
            break;
        case SimileAjax.DateTime.EIGHTHOURS:
            date2.setUTCMilliseconds(0);
            date2.setUTCSeconds(0);
            date2.setUTCMinutes(0);
            var x = date2.getUTCHours();
            date2.setUTCHours(x - (x % 8));
            break;
        case SimileAjax.DateTime.DAY:
            clearInDay(date2);
            break;
        case SimileAjax.DateTime.TWODAYS:
        case SimileAjax.DateTime.FOURDAYS:
        case SimileAjax.DateTime.WEEK:
        case SimileAjax.DateTime.TWOWEEKS:
            clearInDay(date2);
            break;
        case SimileAjax.DateTime.MONTH:
            clearInDay(date2);
            date2.setUTCDate(1);
            var x = date2.getUTCMonth();
            date2.setUTCMonth(x - (x % multiple));
            break;
        case SimileAjax.DateTime.QUARTER:
            clearInDay(date2);
            date2.setUTCDate(1);
            var x = date2.getUTCMonth();
            date2.setUTCMonth(x - (x % 3));
            break;
        case SimileAjax.DateTime.SEMESTER:
            clearInDay(date2);
            date2.setUTCDate(1);
            var x = date2.getUTCMonth();
            date2.setUTCMonth(x - (x % 6));
            break;
        case SimileAjax.DateTime.YEAR:
            clearInYear(date2);
            var x = date2.getUTCFullYear();
            date2.setUTCFullYear(x - (x % multiple));
            break;
        case SimileAjax.DateTime.TWOYEARS:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 2) * 2);
            break;
        case SimileAjax.DateTime.LUSTRUM:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 5) * 5);
            break;
        case SimileAjax.DateTime.DECADE:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 10) * 10);
            break;
        case SimileAjax.DateTime.TWODECADES:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 20) * 20);
            break;
        case SimileAjax.DateTime.HALFCENTURY:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 50) * 50);
            break;
        case SimileAjax.DateTime.CENTURY:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 100) * 100);
            break;
        case SimileAjax.DateTime.TWOCENTURIES:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 200) * 200);
            break;
        case SimileAjax.DateTime.HALFMILLENNIUM:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 500) * 500);
            break;
        case SimileAjax.DateTime.MILLENNIUM:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 1000) * 1000);
            break;
        case SimileAjax.DateTime.TWOMILLENNIUMS:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 2000) * 2000);
            break;
        case SimileAjax.DateTime.FIVEMILLENNIUMS:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 5000) * 5000);
            break;
        case SimileAjax.DateTime.DECAMILLENNIUM:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 10000) * 10000);
            break;
        case SimileAjax.DateTime.TWODECAMILLENNIUMS:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 20000) * 20000);
            break;
        case SimileAjax.DateTime.FIVEDECAMILLENNIUMS:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 50000) * 50000);
            break;
        case SimileAjax.DateTime.HECTOMILLENNIUM:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 100000) * 100000);
            break;
        case SimileAjax.DateTime.TWOHECTOMILLENNIUMS:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 200000) * 200000);
            break;
        case SimileAjax.DateTime.FIVEHECTOMILLENNIUMS:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 500000) * 500000);
            break;
        case SimileAjax.DateTime.MEGENNIUM:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 1000000) * 1000000);
            break;
        case SimileAjax.DateTime.TWOMEGENNIUMS:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 2000000) * 2000000);
            break;
        case SimileAjax.DateTime.FIVEMEGENNIUMS:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 5000000) * 5000000);
            break;
        case SimileAjax.DateTime.DECAMEGENNIUM:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 10000000) * 10000000);
            break;
        case SimileAjax.DateTime.HECTOMEGENNIUM:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 100000000) * 100000000);
            break;
        case SimileAjax.DateTime.TWOHECTOMEGENNIUMS:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 200000000) * 200000000);
            break;
        case SimileAjax.DateTime.FIVEHECTOMEGENNIUMS:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 500000000) * 500000000);
            break;
        case SimileAjax.DateTime.GIGENNIUM:
            clearInYear(date2);
            date2.setUTCFullYear(Math.floor(date2.getUTCFullYear() / 1000000000) * 1000000000);
            break;
    }
    
    date.setTime(date2.getTime() - timeShift);
};

SimileAjax.DateTime.incrementByInterval = function(date, intervalUnit, timeZone){
    timeZone = (typeof timeZone == 'undefined') ? 0 : timeZone;
    
    var timeShift = timeZone * SimileAjax.DateTime.gregorianUnitLengths[SimileAjax.DateTime.HOUR];
    
    var date2 = new Date(date.getTime() + timeShift);
    
    switch (intervalUnit) {
        case SimileAjax.DateTime.MILLISECOND:
            date2.setTime(date2.getTime() + 1)
            break;
        case SimileAjax.DateTime.SECOND:
            date2.setTime(date2.getTime() + 1000);
            break;
        case SimileAjax.DateTime.FIVESECONDS:
            date2.setTime(date2.getTime() + 5000);
            break;
        case SimileAjax.DateTime.TENSECONDS:
            date2.setTime(date2.getTime() + 10000);
            break;
        case SimileAjax.DateTime.HALFMINUTE:
            date2.setTime(date2.getTime() + 30000);
            break;
        case SimileAjax.DateTime.MINUTE:
            date2.setTime(date2.getTime() + SimileAjax.DateTime.gregorianUnitLengths[SimileAjax.DateTime.MINUTE]);
            break;
        case SimileAjax.DateTime.FIVEMINUTES:
            date2.setTime(date2.getTime() + 5 * SimileAjax.DateTime.gregorianUnitLengths[SimileAjax.DateTime.MINUTE]);
            break;
        case SimileAjax.DateTime.TENMINUTES:
            date2.setTime(date2.getTime() + 10 * SimileAjax.DateTime.gregorianUnitLengths[SimileAjax.DateTime.MINUTE]);
            break;
        case SimileAjax.DateTime.HALFHOUR:
            date2.setTime(date2.getTime() + 30 * SimileAjax.DateTime.gregorianUnitLengths[SimileAjax.DateTime.MINUTE]);
            break;
        case SimileAjax.DateTime.HOUR:
            date2.setTime(date2.getTime() + SimileAjax.DateTime.gregorianUnitLengths[SimileAjax.DateTime.HOUR]);
            break;
        case SimileAjax.DateTime.TWOHOURS:
            date2.setTime(date2.getTime() + 2 * SimileAjax.DateTime.gregorianUnitLengths[SimileAjax.DateTime.HOUR]);
            break;
        case SimileAjax.DateTime.FOURHOURS:
            date2.setTime(date2.getTime() + 4 * SimileAjax.DateTime.gregorianUnitLengths[SimileAjax.DateTime.HOUR]);
            break;
        case SimileAjax.DateTime.EIGHTHOURS:
            date2.setTime(date2.getTime() + 8 * SimileAjax.DateTime.gregorianUnitLengths[SimileAjax.DateTime.HOUR]);
            break;
        case SimileAjax.DateTime.DAY:
            date2.setUTCDate(date2.getUTCDate() + 1);
            break;
        case SimileAjax.DateTime.TWODAYS:
            date2.setUTCDate(date2.getUTCDate() + 2);
            break;
        case SimileAjax.DateTime.FOURDAYS:
            date2.setUTCDate(date2.getUTCDate() + 4);
            break;
        case SimileAjax.DateTime.WEEK:
            date2.setUTCDate(date2.getUTCDate() + 7);
            break;
        case SimileAjax.DateTime.TWOWEEKS:
            date2.setUTCDate(date2.getUTCDate() + 14);
            break;
        case SimileAjax.DateTime.MONTH:
            date2.setUTCMonth(date2.getUTCMonth() + 1);
            break;
        case SimileAjax.DateTime.QUARTER:
            date2.setUTCMonth(date2.getUTCMonth() + 3);
            break;
        case SimileAjax.DateTime.SEMESTER:
            date2.setUTCMonth(date2.getUTCMonth() + 6);
            break;
        case SimileAjax.DateTime.YEAR:
            date2.setUTCFullYear(date2.getUTCFullYear() + 1);
            break;
        case SimileAjax.DateTime.TWOYEARS:
            date2.setUTCFullYear(date2.getUTCFullYear() + 2);
            break;
        case SimileAjax.DateTime.LUSTRUM:
            date2.setUTCFullYear(date2.getUTCFullYear() + 5);
            break;
        case SimileAjax.DateTime.DECADE:
            date2.setUTCFullYear(date2.getUTCFullYear() + 10);
            break;
        case SimileAjax.DateTime.TWODECADES:
            date2.setUTCFullYear(date2.getUTCFullYear() + 20);
            break;
        case SimileAjax.DateTime.HALFCENTURY:
            date2.setUTCFullYear(date2.getUTCFullYear() + 50);
            break;
        case SimileAjax.DateTime.CENTURY:
            date2.setUTCFullYear(date2.getUTCFullYear() + 100);
            break;
        case SimileAjax.DateTime.TWOCENTURIES:
            date2.setUTCFullYear(date2.getUTCFullYear() + 200);
            break;
        case SimileAjax.DateTime.HALFMILLENNIUM:
            date2.setUTCFullYear(date2.getUTCFullYear() + 500);
            break;
        case SimileAjax.DateTime.MILLENNIUM:
            date2.setUTCFullYear(date2.getUTCFullYear() + 1000);
            break;
        case SimileAjax.DateTime.TWOMILLENNIUMS:
            date2.setUTCFullYear(date2.getUTCFullYear() + 2000);
            break;
        case SimileAjax.DateTime.FIVEMILLENNIUMS:
            date2.setUTCFullYear(date2.getUTCFullYear() + 5000);
            break;
        case SimileAjax.DateTime.DECAMILLENNIUM:
            date2.setUTCFullYear(date2.getUTCFullYear() + 10000);
            break;
        case SimileAjax.DateTime.TWODECAMILLENNIUMS:
            date2.setUTCFullYear(date2.getUTCFullYear() + 20000);
            break;
        case SimileAjax.DateTime.FIVEDECAMILLENNIUMS:
            date2.setUTCFullYear(date2.getUTCFullYear() + 50000);
            break;
        case SimileAjax.DateTime.HECTOMILLENNIUM:
            date2.setUTCFullYear(date2.getUTCFullYear() + 100000);
            break;
        case SimileAjax.DateTime.TWOHECTOMILLENNIUMS:
            date2.setUTCFullYear(date2.getUTCFullYear() + 200000);
            break;
        case SimileAjax.DateTime.FIVEHECTOMILLENNIUMS:
            date2.setUTCFullYear(date2.getUTCFullYear() + 500000);
            break;
        case SimileAjax.DateTime.MEGENNIUM:
            date2.setUTCFullYear(date2.getUTCFullYear() + 1000000);
            break;
        case SimileAjax.DateTime.TWOMEGENNIUMS:
            date2.setUTCFullYear(date2.getUTCFullYear() + 2000000);
            break;
        case SimileAjax.DateTime.FIVEMEGENNIUMS:
            date2.setUTCFullYear(date2.getUTCFullYear() + 5000000);
            break;
        case SimileAjax.DateTime.DECAMEGENNIUM:
            date2.setUTCFullYear(date2.getUTCFullYear() + 10000000);
            break;
        case SimileAjax.DateTime.TWODECAMEGENNIUMS:
            date2.setUTCFullYear(date2.getUTCFullYear() + 20000000);
            break;
        case SimileAjax.DateTime.FIVEDECAMEGENNIUMS:
            date2.setUTCFullYear(date2.getUTCFullYear() + 50000000);
            break;
        case SimileAjax.DateTime.HECTOMEGENNIUM:
            date2.setUTCFullYear(date2.getUTCFullYear() + 100000000);
            break;
        case SimileAjax.DateTime.TWOHECTOMEGENNIUMS:
            date2.setUTCFullYear(date2.getUTCFullYear() + 200000000);
            break;
        case SimileAjax.DateTime.FIVEHECTOMEGENNIUMS:
            date2.setUTCFullYear(date2.getUTCFullYear() + 500000000);
            break;
        case SimileAjax.DateTime.GIGENNIUM:
            date2.setUTCFullYear(date2.getUTCFullYear() + 1000000000);
            break;
    }
    date.setTime(date2.getTime() - timeShift);
};

SimileAjax.DateTime.getTimeLabel = function( unit, t ){
		var time = SimileAjax.DateTime;
		var second = t.getUTCSeconds();
		var minute = t.getUTCMinutes();
		var hour = t.getUTCHours();
		var day = t.getUTCDate();
		var month = t.getUTCMonth()+1;
		var year = t.getUTCFullYear();
		switch(unit){
			case time.SECOND:
			case time.FIVESECONDS:
			case time.TENSECONDS:
			case time.HALFMINUTE:
           		return hour+":"+((minute<10)?"0":"")+minute+":"+((second<10)?"0":"")+second;
			case time.MINUTE:
			case time.FIVEMINUTES:
			case time.TENMINUTES:
			case time.HALFHOUR:
				return hour+":"+((minute<10)?"0":"")+minute;
			case time.TWOHOURS:
			case time.FOURHOURS:
			case time.EIGHTHOURS:				
			case time.HOUR:
				return hour + ":00";
			case time.DAY:
			case time.TWODAYS:
			case time.FOURDAYS:
			case time.WEEK:
			case time.TWOWEEKS:
			case time.MONTH:
			case time.QUARTER:
			case time.SEMESTER:
           		return year+"-"+((month<10)?"0":"")+month+"-"+((day<10)?"0":"")+day;
			case time.YEAR:
			case time.TWOYEARS:
			case time.LUSTRUM:
			case time.DECADE:
			case time.TWODECADES:
			case time.HALFCENTURY:
			case time.CENTURY:
			case time.TWOCENTURIES:
			case time.HALFMILLENNIUM:
			case time.MILLENNIUM:
				return year;
			case time.TWOMILLENNIUMS:
			case time.FIVEMILLENNIUMS:
			case time.DECAMILLENNIUM:
			case time.TWODECAMILLENNIUMS:
			case time.FIVEDECAMILLENNIUMS:
			case time.HECTOMILLENNIUM:
			case time.TWOHECTOMILLENNIUMS:
			case time.FIVEHECTOMILLENNIUMS:
				return Math.floor(year/1000) + "k";
			case time.MEGENNIUM:
			case time.TWOMEGENNIUMS:
			case time.FIVEMEGENNIUMS:
			case time.DECAMEGENNIUM:
			case time.TWODECAMEGENNIUMS:
			case time.FIVEDECAMEGENNIUMS:
			case time.HECTOMEGENNIUM:
			case time.TWOHECTOMEGENNIUMS:
			case time.FIVEHECTOMEGENNIUMS:
				return Math.floor(year/1000000) + "mill";		
		}
	};

SimileAjax.DateTime.getTimeString = function( unit, t ){
		var time = SimileAjax.DateTime;
		switch(unit){
			case time.MILLISECOND:
			case time.SECOND:
			case time.FIVESECONDS:
			case time.TENSECONDS:
			case time.HALFMINUTE:
			case time.MINUTE:
			case time.FIVEMINUTES:
			case time.TENMINUTES:
			case time.HALFHOUR:
			case time.HOUR:
			case time.TWOHOURS:
			case time.FOURHOURS:
			case time.EIGHTHOURS:
				var m = t.getUTCMonth()+1;
				var d = t.getUTCDate();
				var h = t.getUTCHours();
				var min = t.getUTCMinutes();
				var s = t.getUTCSeconds();
           		return t.getUTCFullYear()+"-"+((m<10)?"0":"")+m+"-"+((d<10)?"0":"")+d+" "+((h<10)?"0":"")+h+":"+((min<10)?"0":"")+min+":"+((s<10)?"0":"")+s;
			case time.DAY:
			case time.TWODAYS:
			case time.FOURDAYS:
			case time.WEEK:
			case time.TWOWEEKS:
			case time.MONTH:
			case time.QUARTER:
			case time.SEMESTER:
				var m = t.getUTCMonth()+1;
				var d = t.getUTCDate();
           		return t.getUTCFullYear()+"-"+((m<10)?"0":"")+m+"-"+((d<10)?"0":"")+d;
			case time.YEAR:
			case time.TWOYEARS:
			case time.LUSTRUM:
			case time.DECADE:
			case time.TWODECADES:
			case time.HALFCENTURY:
			case time.CENTURY:
			case time.TWOCENTURIES:
			case time.HALFMILLENNIUM:
			case time.MILLENNIUM:
				return t.getUTCFullYear();
			case time.TWOMILLENNIUMS:
			case time.FIVEMILLENNIUMS:
			case time.DECAMILLENNIUM:
			case time.TWODECAMILLENNIUMS:
			case time.FIVEDECAMILLENNIUMS:
			case time.HECTOMILLENNIUM:
			case time.TWOHECTOMILLENNIUMS:
			case time.FIVEHECTOMILLENNIUMS:
				return Math.floor(t.getUTCFullYear() / 1000) + "k";
			case time.MEGENNIUM:
			case time.TWOMEGENNIUMS:
			case time.FIVEMEGENNIUMS:
			case time.DECAMEGENNIUM:
			case time.TWODECAMEGENNIUMS:
			case time.FIVEDECAMEGENNIUMS:
			case time.HECTOMEGENNIUM:
			case time.TWOHECTOMEGENNIUMS:
			case time.FIVEHECTOMEGENNIUMS:
				return Math.floor(t.getUTCFullYear() / 1000000) + "mill";		
		}
	};
