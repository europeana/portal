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
	
	$('.scroll-top').click(function() {
		var val = $('#chunk').val();
		console.log('call scroll top with ' + val);
		self.hierarchy.scrollTop( val )
		//$('.jstree-disabled').each(function(){
		//	var node = self.treeCmp.jstree( 'get_node', $(this).closest('.jstree-node') );			
		//	self.treeCmp.jstree("enable_node", node );
		//});		
	});
	
	$('#chunk').keypress(function(e){
		if(e.which==13){
			$('.scroll-top').click();		
		}
	});
	
		
	$('.tba').click(function() {
		self.hierarchy.brokenArrows();
	});

	$('.visible-nodes').click(function(){
		
		console.log('get limits...');
		
		var limits = self.hierarchy.getVisibleNodes();
		
		console.log(limits[0].id + ' <--> ' + limits[1].id);
		
		$('#' +  limits[0].id).find('>a').css('background-color', 'red');
		$('#' +  limits[1].id).find('>a').css('background-color', 'blue');			
		
		setTimeout(function(){
			$('#' +  limits[0].id).find('>a').css('background-color', 'white');
			$('#' +  limits[1].id).find('>a').css('background-color', 'white');
		}, 3000);
	});


	$('.expand').click(function(){
		self.hierarchy.expandAll();
	});

	$('.collapse').click(function(){
		self.hierarchy.collapseAll();
	});
	
	$('.delay').click(function(){
		self.hierarchy.setDefaultDelay(10000);
	});
	
	$('.t-start').click(function(){
		self.hierarchy.startTimer();
	});

	$('.t-stop').click(function(){
		self.hierarchy.stopTimer();
	});
	
	
};


