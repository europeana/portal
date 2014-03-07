var EuHierarchy = function(cmp) {

	var self            = this;
	var defaultChunk    = 6;
	
	self.treeCmp        = cmp;
	self.pageNodeId     = false;	// the id of the node corresponding to the full-doc page we're on
	self.container      = self.treeCmp.closest('.hierarchy-container');
	self.scrollDuration = 0;		

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
		return self.treeCmp.find(">ul>li:first-child");
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
	 * @return false if there's no load point, otherwise [int, int]
	 * 
	 * 	- node count from root to the the last load point
	 * 	- node count from root to @node
	 *  
	 */
	var getPILOT = function(node) {
		
		var loadPoint         = $('.loadPoint');		// TODO - remove load points and re-set on node_select / node_open
		
		if(!loadPoint.length){
			//console.log('added load position to ' + node.text)
			//$('#' + node.id).addClass(loadPoint);
			return false;
		}
		loadPoint = loadPoint.attr('id');
		var loadPointPosition = 0;
		var nodePosition      = 0;
		

		// inner function: 
		var countNodes = function(startNode, total) {

			total = total ? total : 0;

			$.each(startNode.children, function(i, ob) {
				total++;
				var cNode = self.treeCmp.jstree('get_node', ob);
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
		
		countNodes(self.treeCmp.jstree('get_node', getRootEl().attr('id')));
		
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

		keys = keys.sort(function(a, b){ return b - a });

		if(keys.length > max){
			keys = backwards ? keys.slice(0, max) : keys.slice(keys.length - max);
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
			  res = '.slice(' + consecutiveKeys[0] + ', ' + (consecutiveKeys[0] + 1) + ')';
			  break;
		  default:
			  res = '.slice(' + consecutiveKeys[consecutiveKeys.length-1] + ', ' +  (consecutiveKeys[0]+1) + ')';
		}
		return res;
	};
	
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

	// TODO - remove scroll parameter
	
	var viewPrevOrNext = function(node, backwards, leftToLoad, scroll, callback) {
			
		console.log('viewPrevOrNext -> ' + node.text + ', backwards -> ' + backwards)
		
		if(!backwards){
			var switchTrackNode = node;
			
			// find deepest opened with children
			while(switchTrackNode.children.length && switchTrackNode.state.opened){
				switchTrackNode = self.treeCmp.jstree('get_node', switchTrackNode.children[0]);
			}

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
				switchTrackNode = self.treeCmp.jstree('get_node', switchTrackNode.parent);
			}

			node = switchTrackNode;			 
		}
		
		if(node.parent){
			var parent = self.treeCmp.jstree('get_node', node.parent);
			
			if(parent.data){
				var urlSuffix = getUrlSuffix( node, backwards, leftToLoad );
				var url       = parent.data.childrenUrl + urlSuffix;
			
				if(urlSuffix.length){
					loadData(url, function(data){
						
						$.each(backwards ? data.reverse() : data, function(i, ob) {

							var newId   = self.treeCmp.jstree("create_node", parent, ob, backwards ? "first" : "last", false, true);
							var newNode = self.treeCmp.jstree('get_node', newId);

							loadFirstChild(newNode);
							
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
					if(backwards){
						
						if(self.treeCmp.jstree( 'is_disabled', parent )){
							self.treeCmp.jstree("enable_node", parent );
							leftToLoad --;
						}
						if(scroll){
							//doScrollTo($('#' + parent.id), function(){
							//	togglePrevNextLinks();				
							//	viewPrevOrNext(parent, backwards, leftToLoad, scroll)
							//});														
							togglePrevNextLinks();
							console.log('recurse point two')
							viewPrevOrNext(parent, backwards, leftToLoad, scroll, callback);
						}
						else{
							console.log('recurse point three')
							viewPrevOrNext(parent, backwards, leftToLoad, scroll, callback);
						}
					}
					else{
						// forwards
						
						//console.log('recurse point four -fwd no suffix for parent ' + parent.text)
						
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
		if(!self.container.prev('.ajax-overlay').length){
			self.container.before('<div class="ajax-overlay">');
		}
		self.container.prev('.ajax-overlay').show();
	};
	
	var hideSpinner = function(){
		self.container.prev('.ajax-overlay').hide();
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
	var togglePrevNextLinks = function(){
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

	$('.load-more').click(function(){ alert('no handler'); });
	$('.view-next').click(function(){ alert('no handler'); });
	
	$('.hierarchy-prev').click(function(){
		
		showSpinner();
		
		// scroll tracking
		
		var heightMeasure   = $('.jstree-container-ul>.jstree-node>.jstree-children');
		var disabledMeasure = $('.jstree-container-ul .jstree-disabled').first().height();
		var disabledCount   = $('.jstree-container-ul .jstree-disabled').length;
		var origHeight      = heightMeasure.height();
		var origScrollTop   = parseInt(self.container.scrollTop());

		// load nodes

		viewPrevOrNext(getVisibleNodes()[0], true,  defaultChunk, false, function(){
			
			var newHeight   = heightMeasure.height();
			var diffHeight  = newHeight - origHeight;
			var newScrollTo = origScrollTop + diffHeight;

			// reset view
			self.scrollDuration = 0;

			doScrollTo(newScrollTo, function(){

				self.scrollDuration  = 1000;

				var containerH       = parseInt(self.container.height());
				var newDisabledCount = $('.jstree-container-ul .jstree-disabled').length;
				var extraDiff        = disabledMeasure * (disabledCount - newDisabledCount)
				
				diffHeight += extraDiff;

				// no data loaded - we still should scrol up
				if(diffHeight == 0){
					diffHeight = containerH;
				}
				
				newScrollTo -= (containerH > diffHeight && newHeight < (containerH + newScrollTo)) ? diffHeight : containerH;
					
				/*if(containerH > diffHeight && newHeight < (containerH + newScrollTo) ){
					newScrollTo -= diffHeight						
				}	
				else{
					newScrollTo -= containerH
				}*/
						
				newScrollTo = Math.max(0, newScrollTo);
				doScrollTo(newScrollTo, function(){
					togglePrevNextLinks();
					hideSpinner();
				});
			});
			
		});
	});
	
	
	$('.hierarchy-next').click(function(){
		showSpinner();

		// TODO - firefox bug jumps down too far
		
		var visibleNodes = getVisibleNodes();
		
		viewPrevOrNext(visibleNodes[0], false, defaultChunk, true, function(){
			doScrollTo('#' + visibleNodes[1].id, function(){
				togglePrevNextLinks();
				hideSpinner();
			});
		});
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
		var totalH      = -2;		// start negative to keep a couple of pixels inside - focussed borders and outlines can knock this off otherwise
		var offset      = self.container.scrollTop();
		var offsetB     = -4 + offset + self.container.height();

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
				totalH += anchor.height();
			}
			set(startNode);
			
			if(!topEntry || !bottomEntry){
				
				$.each(startNode.children, function(i, ob){
					if(topEntry && bottomEntry){
						return false;		// break
					}
					var cNode = self.treeCmp.jstree('get_node', ob);
					lastEntry = cNode;					
					
					if (! cNode.state.opened){
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

		var root = self.treeCmp.jstree('get_node', getRootEl().attr('id') );
		countNodes(root);
		
		topEntry    = topEntry    ? topEntry    : root;
		bottomEntry = bottomEntry ? bottomEntry : lastEntry;
		
		return [topEntry, bottomEntry];
	};
	
	
	
	
	
	$('.spin').click(function() {

		console.log('get limits...');
		
		var limits = getVisibleNodes();
		
		console.log(limits[0].id + ' <--> ' + limits[1].id);
		
		$('#' +  limits[0].id).css('background-color', 'red');
		$('#' +  limits[1].id).css('background-color', 'blue');			
		
		setTimeout(function(){
			$('#' +  limits[0].id).css('background-color', 'white');
			$('#' +  limits[1].id).css('background-color', 'white');
		}, 1000);

		
		return;
		
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

		// TODO - delete this if not used
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
			
			showSpinner();
			
			viewPrevOrNext(data.node, false, defaultChunk, false, function(){
				doScrollTo($('#' + data.node.id), function(){
					togglePrevNextLinks();

					$('.loadPoint').removeClass('loadPoint');
					$('#' + data.node.id).addClass('loadPoint');
					$('#' + data.node.id + '>a').focus();

					hideSpinner();
				});
			});
			
			$('.debug-area').html(JSON.stringify(data.node));
			return data.node;
		});

		// loaded
		
		self.treeCmp.bind("loaded.jstree", function(event, data) {
			
			// set active and load

			getPageNodeEl().click(); // used to scroll beyond disabled parents
			self.scrollDuration = 1000;
			
			setTimeout(function() {
				var pageNode = self.treeCmp.jstree('get_node', self.pageNodeId);
				loadFirstChild(pageNode, function(){
					
					getPageNodeEl().click(); // used again to load immediate siblings
					
				});
			}, 1);
		});

		
		
		// arrow down

		self.treeCmp.bind('keydown.jstree', function(e) {

			
			// 'Down' || 'Up'
			
			if (e.which == 40 || e.which == 38) { 

				var hoveredNodeEl = self.treeCmp.find('.jstree-hovered');
				var hoveredNode   = self.treeCmp.jstree('get_node', hoveredNodeEl.parent());
				var doLoad        = false;
				var initiatingNode;
				var backwards;
				
				if(hoveredNodeEl.hasClass('jstree-disabled')){
					doLoad         = true;
					initiatingNode = self.treeCmp.jstree('get_node', $('.loadPoint').attr('id'));
					backwards      = true;
				}
				else {
					var positions = getPILOT(hoveredNode);
					    positions = positions ? e.which == 38 ? positions.reverse() : positions : false;
					
					if(!positions || positions[0] > positions[1]   ||  positions[1] - positions[0] > (defaultChunk / 2) ){
						doLoad         = true;
						initiatingNode = hoveredNode;
						backwards      = e.which == 38;
					}
				}
				if(doLoad){
					showSpinner();
					viewPrevOrNext(initiatingNode, backwards,  defaultChunk, false, function(){
						$('.loadPoint').removeClass('loadPoint');
						$('#' + initiatingNode.id).addClass('loadPoint');
						
						// TODO: unnatural scroll
						// Look at previous fix for this applied to (view next? select?)
						// All calls ot vpon need wrapped in a scroll handler *
						//
						// This scroll DOES fix the line-height misalign (chrome & maybe ff)...
						// ...however 
						// ...maybe the scroll can be made to either:
						// 
						// - rest at the point where it started (but aligned to node)
						//    - good for keying
						//    - good for opening... though not needed as opening can only load down
						//    - override if start point was at viewport edge
						//  OR
						//    - move the smallest between (the height of the viewport / the height of the data added)
						//
						// * this scroll handler would benefit from knowing (or it may have to know) how many nodes had been added and where
						//   in order to work around firefox bug...
						
						doScrollTo('#' + initiatingNode.id, function(){
							hideSpinner();
							$('#' + initiatingNode.id + '>a').focus();							
						});

					});
				}
			}
		});

		// OPEN

		self.treeCmp.on("open_node.jstree", function(e, data) {
			
			console.log('open node: ' + data.node.text);
			showSpinner();
			
			var fChild = self.treeCmp.jstree('get_node', data.node.children[0]);
			
			loadFirstChild(fChild, function(){
				viewPrevOrNext(fChild, false, defaultChunk, false, function(){
					$('.loadPoint').removeClass('loadPoint');
					$('#' + data.node.id).addClass('loadPoint');
					$('#' + data.node.id + '>a').focus();
					hideSpinner();

				})
			});

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
			
			// console.log('data.parentUrl ' + data.parentUrl + ', data.index ' + data.index );
			
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
				}
				if(data.data){
					data.data.backwards = true;
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

 * BEHAVIOUR:
 * 
 * THEME:
 *  
 *  - Load flicker - show spinner on actual load - set enbaler flag (reset on showSpinner call) 
 *  
 *  TODO:
 *  
 *  
 *  scrolling 
 *   - particularly up the way
 *   - common handler needed 
 *   
 *  showing
 *   - labels
 *   - when to show
 *  
 *  sizing
 *   - configure viewport height
 *     - let js configue the css
 *   - remove size limit on load all?
 */





