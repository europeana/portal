var EuHierarchy = function(cmp) {

	var self            = this;
	var defaultChunk    = 6;
	
	self.treeCmp        = cmp;
	self.pageNodeId     = false;	// the id of the node corresponding to the full-doc page we're on
	self.container      = self.treeCmp.closest('.hierarchy-container');
	self.scrollDuration = 0;		

	var jstActiveNode;		    // the last selected node	
	var newestNode = false;
	var createdNodes = [];
	
	
	// debug vars
	var locked = true;
	var spin = false;

	// START LOAD FUNCTIONS

	// this simulates what will be an ajax call

	var loadData = function(url, callback) {
		if(!url){
			callback([]);
			return;
		}
		//console.log(url)
		var data = eval(url);
		
		// sanity
		$.each(data, function(i, ob){
			if(ob.data){
				if(!ob.data.index){
					console.log('missing index for ' + ob.text);
				}
			}
		});
		
		setTimeout(function(){
			callback(data);
		}, 500);
	};

	var loadAndAppendData = function(node, callback) {

		// get url

		var start = node.data.loaded ? node.data.loaded : 0;
		var end = start + Math.min(node.data.total - start, defaultChunk);
		var url = node.data.childrenUrl;
		
		if(url && url.length){
			url += '.slice(' + start + ',' + end + ')';

			loadData(url, function(newData) {

				createdNodes = []; // reset global
				$.each(newData, function(i, ob) {
					self.treeCmp.jstree("create_node", node, ob, "last", function(){}, true);
				});
				var created = createdNodes;
				createdNodes = null; // re-nullify global

				// update tracking variables
				node.data.loaded = node.data.loaded ? node.data.loaded + newData.length : newData.length;

				// callback
				if (callback) {
					callback(created);
				}
			});
		}
		else if (callback) {
			alert("missing out on this callback:\n\n" + callback)
			callback(created);
		}
		// load and append

	};

	var loadMore = function() {
		if (jstActiveNode) {
			if (jstActiveNode.data) {
				loadAndAppendData(jstActiveNode, function(data) {
					// console.log('loaded and appended more (' + data.length + ') - (this is the callback)');
				});
				doScrollTo($('#' + jstActiveNode.id)[0]);
			}
		};
	};

	// loads the node's children

	var loadChildren = function(node, callback) {
		if (node.data) {
			loadAndAppendData(node, function(data) {
				if (callback) {
					callback();
				}
			});
		}
		else {
			if (callback) {
				callback();
			}
		}
	};

	// chainLoad utility: performs @fn for @nodes - in
	// callbacked sequence - and executes @callbackWhenDone when
	// done
	var chainLoad = function(nodes, fn, callbackWhenDone) {

		var index = 0;
		var recursiveLoad = function() {

			if (index < nodes.length){
				fn(nodes[index], function(){
					index++;
					recursiveLoad()
				});
			}
			else {
				if (callbackWhenDone) {
					callbackWhenDone();
				}
			}
		};
		recursiveLoad();
	};

	
	var filterChildList = function(arr, startAt) {
		var startAt = startAt ? startAt : 0;
		var result = [];

		$(arr).each(function(i, ob) {
			if (i >= startAt) {
				var child = self.treeCmp.jstree('get_node', ob);
				if (child.data ? (child.data.loaded == 0 || typeof child.data.loaded == 'undefined') : false) {
					
					//console.log('child.data.loaded pushed ' + child.data.loaded + '  nodename ' + child.text );
					result.push(child);
				}
			}
		});
		return result;
	};

	// loads the children's children

	var loadGrandChildren = function(node, callback, startAt) {

		if (node.children.length) {
			var index = 0;
			var children = filterChildList(node.children, startAt ? startAt : 0);
			
			
			if (children.length) {
				chainLoad(children, loadAndAppendData, callback);
			}
			else{
				callback();
			}
		}
		else{
			consol.log('loadGrandChildren returns without creating anything');
			callback();
		}
	};

	// END LOAD FUNCTIONS

	// START DOM HELPER FUNCTIONS
	var getRootEl = function() {
		return self.treeCmp.find(">ul>li:first-child a");
	};
	
	var getPageNodeEl = function() {
		var res = self.treeCmp.find("#" + self.pageNodeId + " a");
		
		/*
		res.attr('style', 'background-color:red');		
		setTimeout(function(){
			res.attr('style', 'background-color:white');
		}, 2000);
		*/
		
		return res;
	};
	

	/*
	 * Positions In Loaded Open Tree (PILOT)
	 * 
	 * @return [int, int]
	 * 
	 * node count from root to the the last load point
	 * node count from root to @node
	 *  
	 */
	var getPILOT = function(node) {
		
		var loadPointPosition = 0;
		var nodePosition = 0;
		var loadPoint = $('.loadPoint').length ? $('.loadPoint').attr('id') : jstActiveNode.id;
		

		// inner function: 
		var countNodes = function(startNode, total) {

			total = total ? total : 0;

			$.each(startNode.children, function(i, ob) {

				var cNode = self.treeCmp.jstree('get_node', ob);
				
				total++;

				if (cNode.id == loadPoint) {
					loadPointPosition = total;
				}
				if (cNode == node) {
					nodePosition = total;
				}

				if (cNode.state.opened) {
					total = countNodes(cNode, total);
				}
			});
			return total;
		}

		countNodes(self.treeCmp.jstree('get_node', 'root'));
		return [ loadPointPosition, nodePosition ];
	};

	/*
	 * Simple next sibling finder - TODO - find native way
	 */
	/*
	var getNext = function(node) {

		if (!node) {
			console.log('getNext requires @node');
			return;
		}

		var nodeId = node.id;
		var parent = node.parent;
		var result = null;
		var nodeIndex = -1;

		parent = self.treeCmp.jstree('get_node', parent);

		if(parent) {			
			$.each(parent.children, function(i, ob) { // find this node's index
				if (ob == nodeId) {
					nodeIndex = i;
					return false;
				}
			});

			if (parent.children.length > (nodeIndex + 1)) {
				result = self.treeCmp.jstree('get_node', parent.children[nodeIndex + 1]);
			}
		}
		return result;
	}
	*/
	
	// END DOM HELPER FUNCTIONS

	// loads up the hierarchy - next or parent.
	// child loads not deducted from @leftToLoad
	var viewNextRecurse = function(node, leftToLoad, callbackComplete) {

		// inner function - logic for recursion
		var recurse = function() {
			
			//if(!confirm('cont?')){
			//	return;
			//}
			
			// strategy - get next sibling nodes that are open and load their content.
			// if there's still data to load after that we recurse from the parent node
			// console.log('vnr: recurse - remaining = ' + leftToLoad );

			var next = getNext(node);

			if (next) {
				// recurse
				viewNextRecurse(next, leftToLoad, callbackComplete);
			}
			else {
				var parent = self.treeCmp.jstree('get_node',	node.parent);
				if (typeof parent == 'object' && parent.id != self.pageNodeId) {
					// recurse
					viewNextRecurse(parent, leftToLoad, callbackComplete);
				}
				else{
					callbackComplete('no parent (left to load = ' + leftToLoad );
				}
			}
		};

		if (node.data && node.state.opened) {

			// load initial child data
			loadAndAppendData(node, function(data) {
				
				leftToLoad -= data.length;
				var newNodes = filterChildList(data);

				chainLoad(newNodes, loadChildren, function() { // executed when all newNodes have been processed
					if (leftToLoad > 0) {
						recurse();
					}
					else{
						callbackComplete('loaded all (A)');
					}
				});
			});
		}
		else { // no data or node closed
			if (leftToLoad > 0) {
				recurse();
			}
			else{
				callbackComplete('loaded all (B)');
			}
		}
	};

	
	// returns a url for backwards node load
	var getUrlSuffix = function(node, backwards, max){
		if(!node.parent){
			return;
		}
		var parent = self.treeCmp.jstree('get_node', node.parent);
		if(!parent.data){
			return;
		}
		var total = parent.data.total;
		var range = {};
		
		for(var i = backwards ? node.data.index : total; i> (backwards ? 0 : node.data.index); i--){
			range[i] = false;
		}
		
		$.each(parent.children, function(i, ob){
			var child = self.treeCmp.jstree('get_node', ob);
			range[child.data.index] = true;
		});

		// convert to array
		var keys = [];
		$.each(range, function(key, val){
			if(!val){
				keys.push(parseInt(key) -1 );  // tweak for zero counting here
			}
		});
		keys = keys.sort(function(a, b){ return parseInt(a) > parseInt(b)}).reverse();
		
		//console.log('range (keys) is ' + JSON.stringify( keys ) );

		if(keys.length > max){
			
			keys = backwards ? keys.slice(0, max) : keys.slice(keys.length - max);
			/*
			if(backwards){
				keys = keys.slice(0, max);
			}
			else{
				keys = keys.slice(keys.length - max);			
			}
			console.log('range trimmed to ' + JSON.stringify( keys ));
			*/
		} 
		
		var last            = false;
		var consecutiveKeys = [];
		$.each(keys, function(i, ob){
			if(!last || ob==last-1){
				consecutiveKeys.push(ob);
				last = ob;
			}
			else{
				return false;
			}
		});
		
		var res = '';
		switch (consecutiveKeys.length){
		  case 0:
			  break;
		  case 1:
			  //console.log('case 1');
			  res = '.slice(' + consecutiveKeys[0] + ', ' + (consecutiveKeys[0] + 1) + ')';
			  break;
		  default:
			  //console.log('case 2: ' + JSON.stringify(consecutiveKeys));
			  res = '.slice(' + consecutiveKeys[consecutiveKeys.length-1] + ', ' +  (consecutiveKeys[0]+1) + ')';
		}
		return res;
	};
	
	// DONE: TODO: limit to left-to-load (done for 17 nodes)
	// DONE: TODO: chain this to make many calls	
	// DONE: TODO: if going backwards start enabling parents
	
	// TODO: if called with active node "Book 2, Volume 1, Chapter 3" everything is reloaded (due to missing indexes probably)
	//		 - can't reproduce
	// TODO: book 1 isn't showing due to "loaded" val being set.
	// TODO: clicking down to node should load more.
	//
	// Can't we do all loading like this????
	
	var loadFirstChild = function(node, callback){
		if(node.data && node.data.childrenUrl && !node.children.length){
			var firstChildUrl = node.data.childrenUrl + '[0]';
			loadData(firstChildUrl, function(fcData){
				self.treeCmp.jstree("create_node", node, fcData, "first", false, true);
				if(callback){
					callback();					
				}
			});
		}
		else if(callback){
			callback();					
		}
	};
	
	var viewPrevOrNext = function(node, backwards, leftToLoad, scroll, callback) {
			
		console.log('viewPrevOrNext -> ' + node.text + ', backwards -> ' + backwards)
		
		if(!backwards){
			var switchTrackNode = node;

			console.log('going deep... ' + switchTrackNode.text);
			
			// find deepest opened with children
			while(switchTrackNode.children.length && switchTrackNode.state.opened){
				
				console.log('  ...deeper... ' + JSON.stringify(switchTrackNode.children[0]) );
				
				switchTrackNode = self.treeCmp.jstree('get_node', switchTrackNode.children[0]);
			}

			console.log('went deep to  ' + switchTrackNode.text);

			var parentLoaded = function(node){
				var parent = self.treeCmp.jstree('get_node', node.parent);

				if(!parent.data){					
					console.log('no parent data for node ' + parent.text);
					return false;
				}

				var loaded = parent.children.length;
				var total  = parent.data.total;
				return loaded == total;
			}
			
			// pull back up
			while(parentLoaded(switchTrackNode)){
				console.log('while 2 ' + switchTrackNode.text);
				switchTrackNode = self.treeCmp.jstree('get_node', switchTrackNode.parent);
			}
			console.log(
	'after while '+				switchTrackNode.id + '  ' + switchTrackNode.text
					)
//			if(){
	//			alert();
		//	}
			node = switchTrackNode;			 
		}
		
		if(node.parent){
			var parent    = self.treeCmp.jstree('get_node', node.parent);
			
			if(parent.data){
				var urlSuffix = getUrlSuffix( node, backwards, leftToLoad );
				var url       = parent.data.childrenUrl + urlSuffix;
			
				if(urlSuffix.length){
					loadData(url, function(data){
						
						$.each(backwards ? data.reverse() : data, function(i, ob) {

							var newId   = self.treeCmp.jstree("create_node", parent, ob, backwards ? "first" : "last", false, true);
							var newNode = self.treeCmp.jstree('get_node', newId);

							loadFirstChild(newNode);
							
//							console.log('appended new node ' + newNode.text);
							
							if(i+1==data.length){
								leftToLoad -= data.length;
								if(leftToLoad > 0){
									console.log('recurse point one')
									viewPrevOrNext(node, backwards, leftToLoad, scroll, callback)
								}
								else{
									if(callback){
										callback();
									}
								}
							}							
						});
						
					});
					
				}
				else{
//					console.log('No suffix - already loaded everyhing (still looking to load another ' + leftToLoad + ')');
					if(backwards){
						
						if(self.treeCmp.jstree( 'is_disabled', parent )){
							self.treeCmp.jstree("enable_node", parent );
							leftToLoad --;
						}
						if(scroll){
							//doScrollTo($('#' + parent.id), function(){
							//	togglePrevLink();				
							//	viewPrevOrNext(parent, backwards, leftToLoad, scroll)
							//});														
							togglePrevLink();
							console.log('recurse point two')
							viewPrevOrNext(parent, backwards, leftToLoad, scroll, callback);
						}
						else{
							console.log('recurse point three')
							///viewPrevOrNext(parent, backwards, leftToLoad, scroll, callback);
							viewPrevOrNext(parent, backwards, leftToLoad, scroll, callback);
						}
					}
					else{
						// forwards
						
						console.log('recurse point four -fwd no suffix for parent ' + parent.text)
						
						// we may have no suffix because we're at the end.
						// we may not really be at the end if there are previous nodes.
						// since we go deep & back up....
						// ... we should not recurse if the starting index of this node is > 1
						if(node.data.index == 0){
							viewPrevOrNext(parent, backwards, leftToLoad, scroll, callback);							
						}
						else{
							if(callback){
								callback();
							}
						}
					}
				}
			}// end if parent.data
			else{
				console.log('no parent data');
				if(callback){
					callback();
				}
			}
		}// end if parent
		else{
			console.log('no parent');
		}
	};
	
	
	
	// UI FUNCTIONS

	var showSpinner = function(){
		if(!self.container.find('.ajax-overlay').length){
			self.container.append('<div class="ajax-overlay">');
			self.container.find('.ajax-overlay').css('top', self.container.scrollTop() + 'px');			
		}
	};
	
	var hideSpinner = function(){
		self.container.find('.ajax-overlay').remove();
	};
	
	/**
	 * Called on:
	 * - select_node
	 * - parent enable (viewPrevOrNext)
	 * 
	 * - key up
	 * - key down
	 *   open_node
	 *   append_node
	 * - init
	 * 
	 * */
	var togglePrevLink = function(){
		var offset = self.container.scrollTop();

		if(self.container.scrollTop() > 1){
			$('.hierarchy-prev').show();
		}
		else{
			$('.hierarchy-prev').hide();
		}
		
		var ch = self.container.height();
		var th = self.treeCmp.height();
		
		console.log('cont height: ' + ch  + ', tre height: ' + th + ', offset: ' + offset );
		if(true){
			$('.hierarchy-next').show();
		}
		else{
			$('.hierarchy-next').hide();
		}
	};

	
	var doScrollTo = function(el, callback) {
		if (typeof el == 'undefined') {
			console.log('doScrollTo error - undefined @el');
			return;
		}

		self.container.css('overflow', 'auto');
		self.container.scrollTo(el, self.scrollDuration, {
			"axis" : "y",
			"onAfter" : function(){
				if(callback){
					callback();
				}
			}
		});
		if (locked) {
			self.container.css('overflow', 'hidden');
		}
	};

	// UI BINDING

	$('.load-more').click(loadMore);
	$('.view-next').click(function(){ alert('no handler'); });
	
	$('.hierarchy-prev').click(function(){
		/* TODO: 
		 * 		if 
		 * 			offset - (disabled.length) * 1em height)
		 * 		then
		 * 			change the offset - don't bother to load
		 * 
		 */
		showSpinner();
		
		var visibleNodes = getVisibleNodes();
		
		viewPrevOrNext(jstActiveNode, true,  defaultChunk, false, function(){
			
			self.scrollDuration = 0;
			doScrollTo('#' + visibleNodes[0].id, function(){
				self.scrollDuration = 1000;
				
				var scrollTop  = parseInt(self.container.scrollTop());
				var containerH = parseInt(self.container.scrollTop());
				var newScollTo = scrollTop - containerH;
				
				console.log('scroll up ST ' + scrollTop  + '  ' + typeof scrollTop )
				console.log('scroll up CH ' + containerH + '  ' + typeof containerH )
				console.log('scroll up NW ' + newScollTo + '  ' + typeof newScollTo )
				
				doScrollTo(newScollTo);
			});
			
			hideSpinner();
		});
	});
	
	
	$('.hierarchy-next').click(function(){
		showSpinner();
		var visibleNodes = getVisibleNodes();
		viewPrevOrNext(visibleNodes[0], false, defaultChunk, true, function(){
			hideSpinner();
			doScrollTo('#'+visibleNodes[1].id);
		})
	});

	
	$('.lock').click(function() {
		locked = !locked;
		if (locked) {
			$(this).html('[unlock]');
			self.container.css('overflow', 'hidden');
		} else {
			$(this).html('[lock]');
			self.container.css('overflow', 'auto');
		}
	});
	
	
	$('.enable').click(function() {
		$('.jstree-disabled').each(function(){
			var node = self.treeCmp.jstree( 'get_node', $(this).closest('.jstree-node') );			
			self.treeCmp.jstree("enable_node", node );
		});		
	});
	
	
	var getVisibleNodes = function(){
		
		var topEntry    = false;
		var bottomEntry = false;
		var lastEntry   = false;	// if the bottom node is above the bottom of the viewport we return this as the bottom
		var totalH      = 0;
		var offset      = self.container.scrollTop();
		var offsetB     = -2 + offset + self.container.height();

		var set = function(node){
			if(totalH >= offset){
				if(!topEntry){
					topEntry = node;
				}
				if(totalH >= offsetB){
					if(!bottomEntry){
						bottomEntry = node;
					}
				}
			}
		}
		
		var countNodes = function(startNode, total) {
			lastEntry = startNode;
			
			var anchor = $('#' + startNode.id + '>.jstree-anchor');
			if(anchor.hasClass('jstree-disabled')){
				totalH += $('#' + startNode.id + '>.jstree-icon').height();
			}
			else{
				//console.log('S: ' + startNode.id + ' anchor ++  ' + anchor.height())
				totalH += anchor.height();
			}
			set(startNode);
			
			if(!topEntry || !bottomEntry){
				
				//console.log('loop children: ' + startNode.id + ' x ' + startNode.children.length )
				
				$.each(startNode.children, function(i, ob) {
					if(topEntry && bottomEntry){
						//console.log('got both -> return');
						return false;
					}
					var cNode = self.treeCmp.jstree('get_node', ob);
					lastEntry = cNode;					
					
					if (! cNode.state.opened) {
						var anchor = $('#' + cNode.id + '>.jstree-anchor');
						totalH += anchor.height();
						set(cNode);
					}
					else{
						countNodes(cNode, total);
					}
				});
			}
		}

		var root = self.treeCmp.jstree('get_node', getRootEl().parent().attr('id') );
		countNodes(root);
		
		topEntry    = topEntry    ? topEntry    : root;
		bottomEntry = bottomEntry ? bottomEntry : lastEntry;
		/*
		$('#' +  topEntry.id)   .css('background-color', 'red');
		$('#' +  bottomEntry.id).css('background-color', 'blue');			
		setTimeout(function(){
			$('#' +  topEntry.id)   .css('background-color', 'white');
			$('#' +  bottomEntry.id).css('background-color', 'white');
		}, 1000);
		*/
		return [topEntry, bottomEntry];
	};
	
	
	
	
	
	$('.spin').click(function() {
		console.log('get limits...')
		var limits = getVisibleNodes();
		console.log(limits[0].id + '   ' + limits[1].id)
		return;
		/*
		if(jstActiveNode){
			var el = $('#' + jstActiveNode.id).find('>.jstree-anchor');
			var bg = el.css('background-color');
			el.css('background-color', 'red');
			setTimeout(function(){
				el.css('background-color', bg);
			}, 500);
		};
		*/
		
		spin = !spin;
		if (spin) {
			$(this).html('[unspin]');
			showSpinner();
		} else {
			$(this).html('[spin]');
			hideSpinner();
		}
	});

	// END UI BINDING



	// Instantiate tree here.
	// We load up the tree to get the absolute root.
	var init = function(baseUrl){
		
		// TREE BINDING

		// create

		self.treeCmp.bind('create_node.jstree', function(e, data) {
			if (!newestNode) {
				newestNode = data.node.id;
				//console.log(' - SET NEWEST NODE (id) = ' +  data.node.id);
			}
			if (createdNodes) {
				createdNodes.push(data.node.id);
			}
		});

		// select (invoke by loaded callback below)

		self.treeCmp.bind("select_node.jstree", function(event, data) {
			jstActiveNode = data.node;
			doScrollTo($('#' + jstActiveNode.id), function(){
				togglePrevLink();				
			});
			
			$('.debug-area').html(JSON.stringify(jstActiveNode));
			return data.node;
		});

		// loaded
		
		self.treeCmp.bind("loaded.jstree", function(event, data) {
			
			// post init:
			self.treeCmp.find('a').focus(function(){
			});
			
			// set active and load
			//getRootEl().click();
			getPageNodeEl().click();
			self.scrollDuration = 1000;
			setTimeout(function() {
				//loadMore();
				var pageNode = self.treeCmp.jstree('get_node', self.pageNodeId);
				loadFirstChild(pageNode);
				//viewPrevOrNext( pageNode, false, defaultChunk);
			}, 1);
		});

		
		
		// arrow down

		self.treeCmp.bind('keydown.jstree', function(e) {
			if (e.key == 'Down') {
				var hoveredNode = self.treeCmp.jstree('get_node', self.treeCmp.find('.jstree-hovered').parent());
				var positions = getPILOT(hoveredNode);

				//console.log('pilot poistions = ' + JSON.stringify(positions) );
				
				if (positions[0] > positions[1]   ||  positions[1] - positions[0] > (defaultChunk / 2)) {
					console.log('load next from hovered');
					showSpinner();
					viewNextRecurse(hoveredNode, defaultChunk, function(msg){

						// make this the marker node
						//console.log('Done Down load' + (typeof msg == 'undefined' ? '' : ': msg = ' + msg) );

						$('.loadPoint').removeClass('loadPoint');
						$('#' + hoveredNode.id).addClass('loadPoint');
						
						hideSpinner();
						
						// regain focus
						$('#' + hoveredNode.id + ' a').focus();

						
					});
					
				}
			}
		});

		// OPEN

		self.treeCmp.on("open_node.jstree", function(e, data) {
			
			console.log('open node: ' + data.node.text);
			showSpinner();
			
			var fChild = self.treeCmp.jstree('get_node', data.node.children[0]);
			
			if(fChild.data && fChild.data.childrenUrl){
				
				loadFirstChild(fChild, function(){	// load first child of subsquent nodes
				
					viewPrevOrNext(fChild, false, defaultChunk, false, function(){
						hideSpinner();
					})
				
				});
				/*
				var firstChildUrl = fChild.data.childrenUrl + '[0]';  // load first child of new node
				loadData(firstChildUrl, function(fcData){
					self.treeCmp.jstree("create_node", fChild, fcData, "first", false, true);
					
					viewPrevOrNext(fChild, false, defaultChunk);  // load first child of subsquent nodes				
				});
				*/
			}
			else{
				// load first child of subsquent nodes				
				viewPrevOrNext(fChild, false, defaultChunk, false, function(){
					hideSpinner();
				})

			}
			return;
			//showSpinner();
			//loadGrandChildren(data.node, function(){
			//	hideSpinner();
			//});
		});

		// CLOSE

		self.treeCmp.bind("close_node.jstree", function(event, data) {
			console.log('closed ' + data.node)
		});

		// END TREE BINDING
		
		

		var chainUp = function(url, data, callbackWhenDone) {
			if(!url){
				callbackWhenDone(data);
				return;
			}
			console.log('data.parentUrl ' + data.parentUrl + ', data.index ' + data.index );
			
			loadData(url, function(newData){
				if(!data){
					data            = newData;
					self.pageNodeId = data.id;
				}
				else{
					var count = data.length;
					newData.state    = {"opened" : true, "disabled" : true};
					newData.children = [data];
					data             = newData;
					//data.loaded      = count;
				}
				if(data.data){
					data.data.backwards = true;
					//data.data.loaded    = 1; // todo - remove
					if(data.data.parentUrl && data.data.index){
						chainUp(data.data.parentUrl, data, callbackWhenDone);
					}
					else{
						callbackWhenDone(data);							
					}
				}
				else{
					callbackWhenDone(data);						
				}
			});	
		};
		
		chainUp(baseUrl, false, function(data){
			//console.log('Initialise tree with model:\n\n' + JSON.stringify(data));
			var tree = self.treeCmp.jstree({
				"core" : {
					"data" : data,
					"check_callback" : true
				},
				"plugins" : [ "themes", "json_data", "ui"]
			});
			

		});
	};
	
	return {
		init : function(baseUrl){
			init(baseUrl);
		}	
	}
};

/*
 * TODO
 * 
 * LOADING BUG:
 * 
 * Only 6 out of 10 children loaded from the following sequence:
 * 
 *  - open book 1
 *  - open volume 3
 *  - scroll down to next load		- important step (it shifts the loadPoint to below )
 *  - go back and open volume 1
 *  - open volume 1 chapter 2
 *      - only 6 verses 
 *   
 *   FIXED:
 * 
 *   the condition to load on hover was changed from:
 *   	if (positions[1] - positions[0] > (defaultChunk / 2))
 * 	 to:
 * 		if (positions[0] > positions[1]   ||  positions[1] - positions[0] > (defaultChunk / 2))
 * 
 * 
 * 
 * LOADING BUG:
 * 
 * Open "Parent Test" and load down Book 1, Volume 1 chapter 1.
 * 
 * FIXED:
 * 
 * in viewNextRecurse() recursion via the parent will not happen if th parent id equals self.pageNodeId
 * 
 * 
 * Loading "down the tree" should never recurse back beyond the original item that was loaded.
 * Other children of the parents of the root node are loaded with a previous sibling load.
 * We need to distinguish between the root node and the page node - the one corresponding to the page we're on.
 * 
 * Possible problem with this strategy?  Consider the following:
 * 
 * Parent Top
 *  |
 *  |_ Some Node
 *  |    |
 *  |    |_ child 1
 *  |    |_ child 2
 *  |
 *  |_ Page Node
 *  |    |
 *  |    |_Book 1
 *  |
 *  |_ Further Nodes
 *  
 *  If I back-pedal to "Some Node" and open it will the load work?
 *   - we're "beyond" the pageNode.... will
 *  
 *  
 * BEHAVIOUR:
 *  1.		parent tree
 *  2.		previous button
 * 
 *  1.	Keying into the container should not draw focus up to the root element, but onto the pageNode
 * 
 * THEME:
 * 
 *  - Loading zoomed, scrolling and reseting zoom to zero loses position (or loses entire tree)
 *  - DONE remove unwanted responsive class 
 *  
 *  - Load flicker - show spinner on actual load - set enbaler flag (reset on showSpinner call) 
 *  
 * TODO:  
 *  
 *  
 *  loaded tracking --> collect indexes and compae to total.
 *  + start from
 *  + allow direction
 *
 *  OR
 *  
 *  Get childIndex from node / parent data and subtract from array created in slice....
 *  var loaded = loaded + loadedBack + (childIndex ? 1 : 0)
 *  
 */
