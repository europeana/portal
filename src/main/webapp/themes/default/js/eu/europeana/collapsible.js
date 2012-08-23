/*
 
 		scripts.push({
			file : 'collapsible' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ],
			callback: function(){

				jQuery("#filter-search li").Collapsible(
						{
							headingSelector:"h3 a",
							bodySelector: "ul"
						}
				);
				jQuery("#filter-search ul").css('display', 'block');
			}
		});
		
		
  
 */

(function( $ ) {
$.fn.Collapsible = function() {
	var ops = arguments[0] || {};
	
	return this.each(function(){
        var $this = $(this);
        var $header = $this.find(ops.headingSelector);
        var $body	= $this.find(ops.bodySelector);

        var setup = function(){
    		if ($header.hasClass('active') ) {
    			$header.addClass('icon-arrow-7');
    			$header.removeClass('icon-arrow-6');
    			$body.slideDown('fast');				
    		}
    		else{
    			$header.addClass('icon-arrow-6');
    			$header.removeClass('icon-arrow-7');
    			$body.slideUp('fast');
    		}
        };
		setTimeout(setup, 1);
		
    	$header.bind('click', function(e){
    		e.preventDefault();

        	if ( $header.hasClass('active') ) {	
        		$header.addClass('icon-arrow-6');
        		$header.removeClass('icon-arrow-7');
        		$header.removeClass('active');
        		$body.slideUp();
    			
    		}else{
    			$header.addClass('icon-arrow-7');
    			$header.removeClass('icon-arrow-6');
    			$header.addClass('active');
    			$body.slideDown();
    		}
		});
    });
};
}( jQuery ) );