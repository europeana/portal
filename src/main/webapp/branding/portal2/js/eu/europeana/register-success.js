(function() {
	
	var $login_response = jQuery('#login-response');
	
	
	function checkForResponse() {
		
		if ( $login_response.length > 0 && $login_response.html().length > 0 ) {
			
			$login_response.show();
			js.utils.flashHighlight( $login_response, '#ffff00', '#fff', 1500);
			
		}
		
	}
	
	checkForResponse();
	
})();