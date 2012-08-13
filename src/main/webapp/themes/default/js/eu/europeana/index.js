(function() {
  
  'use strict';

  var init = function() {


    // conditional load
    jQuery("#query-input").focus(function(){
    	// saves 4 ms
		europeana_bootstrap.common.loadResultSizer(
				function(){ console.log("in callback for index.js loadResultSizer"); }
		);
    });
    
    
  };
  
  init();
  
}());