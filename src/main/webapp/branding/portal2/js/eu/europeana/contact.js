(function() {
	
	var $thank_you = jQuery('#thank-you');
	
	jQuery('#feedback textarea')
		
		.hover(
			
			function() { $thank_you.fadeIn(); },
			function() { $thank_you.fadeOut(); }
			
		)
		
		.focus(function() { $thank_you.fadeIn(); })
		.blur(function(){ $thank_you.fadeOut(); });
	
	jQuery('span.addr').each(function() {
		
		$(this).attr("id", "addr0");
		var spt = "#" + $(this).attr("id");
		var to = ' [@] ';
		var period = ' [.] ';
		var addr = $(spt).text()
			.replace(to, "@")
			.replace(period, ".")
			.replace(period, ".");
		var title = $(this).attr("title");
		if ( title.length == 0 ) {
			title = addr;
		}
		$(spt).after('<a href="mailto:' + addr + '">' + title +'</a>');
		$(spt).remove();
		
	});
	
})();