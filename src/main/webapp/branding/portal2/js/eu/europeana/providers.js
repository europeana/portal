jQuery(function(){
	
	jQuery('.toggle-menu-icon').each(function( key, value ) {
		
		jQuery(value).bind( 'click', function(e) {
			
			var $elm = jQuery(this);
			
			e.preventDefault();
			$elm.next().slideToggle();
			
			if ( $elm.hasClass('active') ) {
				
				$elm.removeClass('active');
				
			} else {
				
				$elm.addClass('active');
				
			}			
			
		});
		
	});
	
});