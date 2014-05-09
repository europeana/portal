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
	
	
	
	$('.spin').click(function() {

		$('#apocalypse_vol_3>a').click();
		alert(  $('#apocalypse_vol_3>a').length  )
	});
	
	$('.tba').click(function() {
		self.hierarchy.brokenArrows();
	});

	$('.visible-nodes').click(function(){
		
		console.log('get limits...');
		
		var limits = self.hierarchy.getVisibleNodes();
		
		console.log(limits[0].id + ' <--> ' + limits[1].id);
		
		$('#' +  limits[0].id).css('background-color', 'red');
		$('#' +  limits[1].id).css('background-color', 'blue');			
		
		setTimeout(function(){
			$('#' +  limits[0].id).css('background-color', 'white');
			$('#' +  limits[1].id).css('background-color', 'white');
		}, 3000);
	});


	$('.points').click(function(){

		// Replacement for function getVisibleNodes().
		// element-to-node and node-to-element conversions happening too often
		// TODO: add parameter @fmt [TYPE_EL, TYPE_NODE, TYPE_BOTH] and return accordingly
		
		var upToNode = function(el){
			console.log('getVis()-upToNode: land on ' + el[0].nodeName + ' ' + el.attr('class') )
			if(el.hasClass('hierarchy-container')){				
				console.log('getVis()-upToNode: hit container');
				return false;
			}
			else if(el.hasClass('jstree-node')){
				console.log('getVis()-upToNode: found node ');
				return el;
			}
			else if(el.parent()){
				console.log('getVis()-upToNode: not found node -recurse');
				return upToNode(el.parent());
			}
			else{
				console.log('getVis()-upToNode: no parent, returning false');
				return false;
			}
		};

		var backToNode = function(nodeIn){
			var x = nodeIn.prevAll("li.jstree-node:first");
			if(x.length){
				console.log('getVis()-backToNode: x is good');
				return x;
			}
			console.log('getVis()-backToNode: having to go up...');
			return nodeIn.closest("li.jstree-node");;			
		};

		
		var stepDown     = 10; /* pixels below the centre of the top panel */
		var pnlTop       = $('.hierarchy-top-panel');
		var container    = $('.hierarchy-container');
		var rect         = pnlTop[0].getBoundingClientRect();
		var pointX       = rect.left + (pnlTop.width() / 2);
		var pointY       = rect.top +  (pnlTop.height());
		var topNode      = upToNode($(document.elementFromPoint(pointX, pointY + stepDown)));
		
		var previousNode = backToNode(topNode);
		
		var bottomNode;
		var stepUp       = 2 * stepDown;
		var count        = 1;
		
		while(!bottomNode){
			bottomNode = upToNode($(document.elementFromPoint(pointX, (pointY + container.height()) - (count * stepUp)  )));
			count ++;
		}

		topNode.css('background-color', 'purple');
		bottomNode.css('background-color', 'cyan');
		previousNode.css('background-color', 'brown');
		
	});
	
	
};


