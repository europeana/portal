
jQuery(document).ready(function(){
	/**
	 * Andy MacLean:
	 *  
	 * Adds the hidden field "resultSize" to the query form and set its value according to the screen width.
	 * Binds window resize event to a function that updates the resultSize value.
	 * 
	 * TODO: the breakpoints defined here are provisional: investigate what the real values should be.
	 * 
	 * */
	jQuery("#query-search").append('<input type="hidden" id="resultSize" name="rows" value="1"/>');
	
	var setResultSize = function(screenWidth){
		
		if(screenWidth<321){
			jQuery("#resultSize").val(1);
		}
		else if(screenWidth<541){
			jQuery("#resultSize").val(4);
		}
		else if(screenWidth<941){
			jQuery("#resultSize").val(6);
		}
		else{
			jQuery("#resultSize").val(12);
		}
		console.log("set result size to " + jQuery("#resultSize").val() + " for screen width " + screenWidth);
	};
	
	jQuery(window).resize(function(){
		setResultSize(document.documentElement.clientWidth);
	});

	setResultSize(document.documentElement.clientWidth);
	
});

