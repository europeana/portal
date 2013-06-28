
function EuMultiSelect(elIn){

  var el = elIn;
  var self = this;
  var ops = [];
  
  var _init = function(){

	if(typeof el == "undefined" || typeof el.attr == 'undefined' || typeof el.attr('id') == 'undefined' || el.attr('id').length==0){
	    console.log('init failed: need an input with an id specified');
	    return;
	}
	else{
		console.log('el.attr("id") = ' + el.attr('id')  + ",  " + el.attr('id').length);
		
	}
    
    el.find('option').each(function(){
    	ops.push($(this).html());
    });
    
    
    
    var cmp = $('<div class="multi-select"></div>').appendTo(el.parent());
    
    cmp.before('<input class="filter" id="' + el.attr('id') + '-filter"/>');
    cmp.append('<div class="choices"></div>');

    
	
   	$('#' + el.attr('id') + '-filter').keyup( function(e){
    	var val =  $(this).val().toUpperCase();
    	
    	console.log("val = " + val);
    	
    	if(val.length > 0){
    		var re = new RegExp('^' + val + '[A-Za-z\\d\\s]*');
            $('.icon-arrow-2-after span').add('.no-children').each(function(i, ob){
            	var text = $(ob).html().toUpperCase();
            	var item = $(ob).closest('li');
            	
            	if(re.test(text)){
            		console.log("match: " + text );
            		item.show();
            	}
            	else{
            		item.hide();
            	}
            });    		
    	}
    	else{
            $('.icon-arrow-2-after span').add('.no-children').closest('li').show();    		
    	}

    } );
		
    
    
    
  };

  var findMatches = function(){
	  
		// href = href.replace(/([?&])rows=\d+/, '$1rows=' + rows);

  };
  
  
  return {
    init:function(){
      _init();
    }
  };

};
