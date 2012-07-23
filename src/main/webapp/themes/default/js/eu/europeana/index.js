(function() {
  
  'use strict';

  
  function init() {
	
    loadDependencies();
    
    // Andy: conditional load test
    jQuery("#query-input").focus(function(){
      europeana_bootstrap.common.loadResultSizer(
        function(){ console.log("in callback for index.js loadResultSizer"); }
      );
    });
    
  }();
  
}());