var EuHierarchyTools = function(hierarchy) {

	var self            = this;	
	self.hierarchy      = hierarchy;
	self.container      = hierarchy.getContainer();

	// debug vars
	var locked = true;
	var spin   = false;

	$('.load-more').click(function(){ alert('no handler'); });
	$('.view-next').click(function(){ alert('no handler'); });
	
	$('.lock').click(function() {
		self.hierarchy.setLocked(!self.hierarchy.getLocked());
		
		if (self.hierarchy.getLocked()) {
			$(this).html('[unlock]');
			self.container.css('overflow', 'hidden');
		} else {
			$(this).html('[lock]');
			self.container.css('overflow', 'auto');
		}
	});
	
	$('.enable').click(function() {
		alert('not done - not needed');
		//$('.jstree-disabled').each(function(){
		//	var node = self.treeCmp.jstree( 'get_node', $(this).closest('.jstree-node') );			
		//	self.treeCmp.jstree("enable_node", node );
		//});		
	});
	
	
	
	$('.spin').click(function() {

		$('#apocalypse_vol_3>a').click();
		alert(  $('#apocalypse_vol_3>a').length  )
		return;
		
		console.log('get limits...');
		
		var limits = self.hierarchy.getVisibleNodes();
		
		console.log(limits[0].id + ' <--> ' + limits[1].id);
		
		$('#' +  limits[0].id).css('background-color', 'red');
		$('#' +  limits[1].id).css('background-color', 'blue');			
		
		setTimeout(function(){
			$('#' +  limits[0].id).css('background-color', 'white');
			$('#' +  limits[1].id).css('background-color', 'white');
		}, 1000);

		return;
	});
	

};


