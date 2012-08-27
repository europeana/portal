/**
 * Andy MacLean:
 *  
 * Adds the hidden field "resultSize" to the query form and set its value according to the screen width.
 * Binds window resize event to a function that updates the resultSize value.
 * 
 * TODO: the breakpoints defined here are provisional: investigate what the real values should be.
 * 
 * */

(function(){
	
	var setResultSize = function(screenWidth){
		
		if(!document.getElementById('resultSize')){
			var hiddenField = document.createElement('input');
			hiddenField.setAttribute("id",		"resultSize");
			hiddenField.setAttribute("type",	"hidden");
			hiddenField.setAttribute("name",	"rows");
			hiddenField.setAttribute("value",	"1");
			document.getElementById('query-search').appendChild(hiddenField);
		}

		if(screenWidth<321){
			document.getElementById('resultSize').setAttribute("value", "4");
		}
		else if(screenWidth<541){
			document.getElementById('resultSize').setAttribute("value", "4");
		}
		else if(screenWidth<921){
			document.getElementById('resultSize').setAttribute("value", "6");
		}
		else if(screenWidth<1001){
			document.getElementById('resultSize').setAttribute("value", "8");
		}
		else{
			document.getElementById('resultSize').setAttribute("value", "12");
		}
		//console.log("set result size to " + jQuery("#resultSize").val() + " for screen width " + screenWidth);
	};	
	window.onresize = setResultSize(document.documentElement.clientWidth);
	setResultSize(document.documentElement.clientWidth);

})();
