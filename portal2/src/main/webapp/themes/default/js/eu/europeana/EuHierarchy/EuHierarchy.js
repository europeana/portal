var EuHierarchy = function(cmp, rows) {

	var self            = this;
	var rows            = rows;
	var defaultChunk    = rows;
	var lineHeight      = 1.4;
	
	self.treeCmp        = cmp;
	self.pageNodeId     = false;	// the id of the node corresponding to the full-doc page we're on
	self.container      = self.treeCmp.closest('.hierarchy-container');
	self.scrollDuration = 0;
	
	
	// debug vars
	var locked = true;
	var spin = false;

	var log = function(msg){
		console.log(msg);
	};
	
	var formatNodeData = function(ob){
		//ob.text = "formatted-" + ob.text;
		//return ob;
		if(ob.data && ob.data.europeana){
			console.log('europeana data found....' + JSON.stringify(ob.data.europeana));
			
			if(ob.data.europeana.icon){
				if(ob.data.europeana.icon.toUpperCase() == 'IMAGE'){
					ob.text += '<span class="icon-image" aria-hidden="true"></span>';
				}
				else if(ob.data.europeana.icon.toUpperCase() == 'SOUND'){
					ob.text += '<span class="icon-sound" aria-hidden="true"></span>';
				}
				else if(ob.data.europeana.icon.toUpperCase() == 'TEXT'){
					ob.text += '<span class="icon-text" aria-hidden="true"></span>';
				}
				else if(ob.data.europeana.icon.toUpperCase() == 'VIDEO'){
					ob.text += '<span class="icon-video" aria-hidden="true"></span>';
				}
			}
			
			
		}
		
		ob.text = "<span>" + ob.text + "</span>";
//		ob.text = '<span style="display:inline-block; width:100%;">' + ob.text + '</span>';

		return ob;
	}
	
	// this simulates what will be an ajax call

	var loadData = function(url, callback) {
		if(!url){
			callback([]);
			return;
		}
		var data = eval(url);
		
		// sanity
		$.each(data, function(i, ob){
			if(ob.data){
				if(!ob.data.index){
					log('missing index for ' + ob.text);
				}
			}
		});
		
		setTimeout(function(){
			callback(data);
		}, 500);
	};


	// START DOM HELPER FUNCTIONS
	
	var getRootEl = function() {
		return self.treeCmp.find(">ul>li:first-child");
	};
	
	var getPageNodeEl = function() {
		return self.treeCmp.find("#" + self.pageNodeId + " a");
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

	
	// returns a url suffix simulating start / rows parameters
	// returns an empty string if a loadable range is unavailable
	var getRange = function(node, backwards, max){
		
		if(!node.parent || (typeof node.parent).toLowerCase() != 'string'){ /* prevent jQuery parent function interfering */
			log('getRange returns empty no parent')
			return '';
		}
		
		var parent = self.treeCmp.jstree('get_node', node.parent);

		if(!parent.data || (typeof parent.data).toLowerCase() == 'function' ){
			log('getRange returns empty - no parent data')
			return '';
		}
		
		var total = parent.data.total;

		//if(!total){
		//	alert('missing total for parent:\n' + JSON.stringify(parent) + '\n node = ' +  + JSON.stringify(node) );
		//}
		
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
			  log('getRange return no consecutive keys - keys were ' + keys + ', range was ' + JSON.stringify(range) + ', total is ' + total );
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
		if(node.data && node.data.childrenUrl && (!node.children || !node.children.length) ){
			var firstChildUrl = node.data.childrenUrl + '[0]';
			loadData(firstChildUrl, function(fcData){
				if(self.treeCmp){
					self.treeCmp.jstree("create_node", node, formatNodeData(fcData), "first", false, true);					
				}
				if(callback){
					callback(fcData);
				}
			});
		}
		else if(callback){
			callback();					
		}
	};

	
	var viewPrevOrNext = function(node, backwards, leftToLoad, deepen, callback) {
			
		log('viewPrevOrNext -> ' + node.text + ', backwards -> ' + backwards + ', deepen -> ' + deepen);
		
		if( (!backwards) && deepen){	/* find deepest opened with children */
			
			var switchTrackNode = node;
			
			while(switchTrackNode.children.length && switchTrackNode.state.opened){
				switchTrackNode = self.treeCmp.jstree('get_node', switchTrackNode.children[0]);
			}

			//log('switchtrack 1: ' + switchTrackNode.text);
			
			var parentLoaded = function(node){
				var parent = self.treeCmp.jstree('get_node', node.parent);
				if(!parent.data){					
					log('no parent data for node ' + node.text + '    (' + parent.text + ')');
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
			
			//log('switchtrack 2: ' + switchTrackNode.text);
			
			node = switchTrackNode;			 
		}
	

		if( (typeof node.parent).toLowerCase() == 'string'){ /* prevent jQuery parent function interfering */

			var parent = self.treeCmp.jstree('get_node', node.parent);
			
			if(parent.data){
				var urlSuffix = getRange( node, backwards, leftToLoad ); // rows param
				var url       = parent.data.childrenUrl + urlSuffix;

				//log('urlSuffix ' + urlSuffix + ', leftToLoad=' + leftToLoad);
				
				if(urlSuffix.length){
					loadData(url, function(data){
						
						$.each(backwards ? data.reverse() : data, function(i, ob) {

							var newId   = self.treeCmp.jstree("create_node", parent, formatNodeData(ob), backwards ? "first" : "last", false, true);
							var newNode = self.treeCmp.jstree('get_node', newId);

							loadFirstChild(newNode);
							
							if(i+1==data.length){
								leftToLoad -= data.length;
								if(leftToLoad > 0){
									log('recurse point one')
									viewPrevOrNext(node, backwards, leftToLoad, true, callback)
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
							log('enable node ' + parent);
							self.treeCmp.jstree("enable_node", parent );
							leftToLoad --;
						}						
						if(leftToLoad > 0){
							log('recurse point three');
							viewPrevOrNext(parent, backwards, leftToLoad, true, callback);
						}
						else{
							if(callback){
								log('recurse point three EXIT - (no suffix, going backwards, nothing left to load)');
								callback();
							}
						}
					}
					else{
						// forwards (with no suffix) - we 're at the end of this sibling list.

						// TODO - check this is needed - it may be an error!
						if(self.treeCmp.jstree( 'is_disabled', parent )){
							self.treeCmp.jstree("enable_node", parent );
							leftToLoad --;
						}
						// end TODO

						if(leftToLoad > 0){
							var np = self.treeCmp.jstree('get_node', node.parent, false);
							if(typeof np.data == 'object'){	/* prevent jQuery data function interfering */
								viewPrevOrNext(
										np,
										backwards,
										leftToLoad,
										false,		/* @deepen to false to avoid infinite recurse and load parent's siblings */
										callback);
							}
							else{
								log('could not recurse fwd (parent data a jquery function, not an object)')
								if(callback){
									callback();
								}
							}
						}
						else{
							if(callback){
								log('EXIT LOAD (no suffix, going forwards - nothing left to load)');
								callback();
							}
						}
						
					}
				}
			}// end if parent.data
			else{
				log('no parent data - text = ' + parent.text + ' parent = ' + node.parent + '\nobject = ' + JSON.stringify(parent) );
				if(callback){
					callback();						
				}
			}
		}// end if parent
		else{
			log('no parent');
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
		
		//console.log('cont height: ' + ch  + ', (tree height: ' + th + ', offset: ' + offset + ') -->  (th-offset) = ' + (th-offset)  );
		
		if(th-offset > ch){
			$('.hierarchy-next').show();
		}
		else{
			$('.hierarchy-next').hide();
		}
	};

	
	var doScrollTo = function(el, callback) {
		
		console.log()
		if (typeof el == 'undefined') {
			log('doScrollTo error - undefined @el');
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
		loadAndScroll(getVisibleNodes()[0], true, function(){
		});
	});

	/**
	 * Load wrapper to handle scrolling.
	 * 
	 * @initiatingNode - node to load from
	 * */
	var loadAndScroll = function(initiatingNode, backwards, callback){

		// Scroll tracking:
		// Get the tree height and offset
		
		var heightMeasureSel = '.jstree-container-ul>.jstree-node>.jstree-children';
		var disabledMeasure  = $('.jstree-container-ul .jstree-disabled').first().height();
		var disabledCount    = $('.jstree-container-ul .jstree-disabled').length;
		var origHeight       = $(heightMeasureSel).height();
		var origScrollTop    = parseInt(self.container.scrollTop());

		// load nodes

		showSpinner();

		viewPrevOrNext(initiatingNode, backwards,  defaultChunk, true, function(){

			hideSpinner();
			
			var newHeight        = $(heightMeasureSel).height();
			var containerH       = self.container.height();
			var diffHeight       = newHeight - origHeight;
			var resetScrollTop   = origScrollTop + diffHeight;
			var newDisabledCount = $('.jstree-container-ul .jstree-disabled').length;
			var extraDiff        = (disabledCount - newDisabledCount) * disabledMeasure;
			    diffHeight      += extraDiff;

			/*
			var stats = function(){
				var x = '\nSTATS:' 
				+ '\nnewHeight:\t\t' + newHeight 
				+ '\ndiffHeight:\t\t' + diffHeight 
				+ '\norigScrollTop:\t\t' + origScrollTop 
				+ '\nresetScrollTop:\t\t' + resetScrollTop 
				+ '\ndisabledCount:\t\t' + disabledCount 
				+ '\nnewDisabledCount:\t\t' + newDisabledCount;
				console.log(x);
			}
			*/
			    
			    
			var finalScroll      = function(){
				//stats();
				
				var newScrollTop = resetScrollTop;
				var visibleNodes = getVisibleNodes();
				
				if(initiatingNode == visibleNodes[0]){

					var newScrollTop      = resetScrollTop;
					var singleNodeMeasure = self.container.find('.jstree-anchor').first().height();
					
					if(backwards){
						newScrollTop -= containerH;
						newScrollTop += singleNodeMeasure;		
					}
					else{
						newScrollTop += containerH;
						newScrollTop -= singleNodeMeasure;
					}
					newScrollTop = Math.max(0, newScrollTop);				
				}

				if(newScrollTop == resetScrollTop){
					self.scrollDuration = 0;
				}
				
				doScrollTo(newScrollTop, function(){
					self.scrollDuration = 1000;

					togglePrevNextLinks();
					if(callback){
						callback();
					}
				});	
			};
	
			if(diffHeight == 0 || (origScrollTop == 0 && newHeight > containerH) ){
				diffHeight = containerH; // this is a max - trimmed within function if too big 
				finalScroll();
			}
			else{
				self.scrollDuration = 0;
				doScrollTo(Math.max(0, resetScrollTop), function(){
					self.scrollDuration  = 1000;				
					finalScroll();			// scroll from reset scroll view 
				});
			}
		});
	};	
	
	
	$('.hierarchy-next').click(function(){
		var visibleNodes = getVisibleNodes();
		loadAndScroll(visibleNodes[0], false, function(){
			togglePrevNextLinks();
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
	
	// END UI BINDING



	// Instantiate tree here.
	// We load up the tree to get the absolute root.
	var init = function(baseUrl){
		
		// Set UI viewport size
		
		self.container.css('height',         (rows * lineHeight) + 'em');
		self.container.css('max-height',     (rows * lineHeight) + 'em');
		self.treeCmp  .css('padding-bottom', (rows * lineHeight) + 'em');
		
		// TREE BINDING

		// utility function for select and initial load
		var setLoadPoint = function(elId){
			$('.loadPoint').removeClass('loadPoint');
			$('#' + elId).addClass('loadPoint');			
		};
		
		var doOnSelect = function(node, callback){
			showSpinner();
			viewPrevOrNext(node, false, defaultChunk, true, function(){
				doScrollTo($('#' + node.id), function(){
					togglePrevNextLinks();
					
					setLoadPoint(node.id);
					
					$('#' + node.id + '>a').focus();
					if(callback){
						callback();
					}
					hideSpinner();
				});
			});
			$('.debug-area').html(JSON.stringify(node));
		};
		
		// select (invoke by loaded callback below)

		self.treeCmp.bind("select_node.jstree", function(event, data) {
			doOnSelect(data.node);
		});

		// loaded
		
		self.treeCmp.bind("loaded.jstree", function(event, data) {
			setTimeout(function() {
				var pageNode = self.treeCmp.jstree('get_node', self.pageNodeId);
				doOnSelect(pageNode, function(){
					setTimeout(function() {
						var pageNode = self.treeCmp.jstree('get_node', self.pageNodeId);
						loadFirstChild(pageNode, function(){
							self.treeCmp.jstree("disable_node", pageNode.parent);
							
							setLoadPoint(self.pageNodeId);
							//$('.loadPoint').removeClass('loadPoint');
							//$('#' + self.pageNodeId).addClass('loadPoint');
							self.scrollDuration = 1000;
						});
					}, 1);
				});
			}, 1);
		});

		// arrow down

		self.treeCmp.bind('keydown.jstree', function(e) {

			// Catch 'Down' || 'Up' keystrokes
			
			if (e.which == 40 || e.which == 38) { 

				var hoveredNodeEl = self.treeCmp.find('.jstree-hovered');
				var hoveredNode   = self.treeCmp.jstree('get_node', hoveredNodeEl.parent());
				var doLoad        = false;
				var backwards     = e.which == 38;
				var initiatingNode;

				// we've keyed up to a disabled parent
				
				if(hoveredNodeEl.hasClass('jstree-disabled')){
					doLoad         = true;
					initiatingNode = self.treeCmp.jstree('get_node', $('.loadPoint').attr('id'));
				}
				else {
					var positions = getPILOT(hoveredNode);
					    positions = positions ? backwards ? positions.reverse() : positions : false;
					    
					if(!positions || positions[0] > positions[1]   ||  positions[1] - positions[0] > (defaultChunk / 2) ){
						doLoad         = true;
						initiatingNode = hoveredNode;
					}
				}
				
				if(doLoad){
					loadAndScroll(initiatingNode, backwards, function(){
						
						setLoadPoint(initiatingNode.id);
						//$('.loadPoint').removeClass('loadPoint');
						//$('#' + initiatingNode.id).addClass('loadPoint');

						// refocussing can also break the offset...
						
						$('#' + initiatingNode.id + '>a').focus();
						doScrollTo('#' + getVisibleNodes()[0].id, function(){
							log('done scroll to');
						});

					});					
				}
				else{ // fix wonky offset on way up
					self.scrollDuration = 0;
					doScrollTo('#' + getVisibleNodes()[0].id, function(){
						self.scrollDuration = 1000;
						togglePrevNextLinks();
					});
				}
			}
		});

		// OPEN

		self.treeCmp.on("open_node.jstree", function(e, data) {
			
			log('open node: ' + data.node.text);
			showSpinner();
			
			var fChild = self.treeCmp.jstree('get_node', data.node.children[0]);
			
			loadFirstChild(fChild, function(){
				viewPrevOrNext(fChild, false, defaultChunk, true, function(){

					setLoadPoint(data.node.id);
					//$('.loadPoint').removeClass('loadPoint');
					//$('#' + data.node.id).addClass('loadPoint');
					$('#' + data.node.id + '>a').focus();
					hideSpinner();

					setTimeout(function(){
						togglePrevNextLinks();				
					}, 500);

				});
			});

		});

		// CLOSE

		self.treeCmp.bind("close_node.jstree", function(event, data) {
			log('closed ' + data.node);
			doOnSelect(data.node, function(){
				setTimeout(function(){
					togglePrevNextLinks();				
				}, 500);
			});
		});

		// END TREE BINDING
		
		var chainUp = function(url, data, callbackWhenDone) {
			
			if(!url){
				callbackWhenDone(data);
				return;
			}

			loadData(url, function(newDataIn){
								
				var newData;

				if(typeof newDataIn == 'object'){
					newData = formatNodeData(newDataIn);
				}
				else if(typeof newDataIn == 'array'){
					newData = [];
					$.each(newDataIn, function(i, ob){
						newData.push(formatNodeData(ob));
					});
				}
				
				if(!data){
					data            = newData;
					self.pageNodeId = data.id;
				}
				else{
					newData.state    = {"opened" : true, "disabled" : true};
					newData.children = [data];
					data             = newData;
				}
			
				if(data.data){					
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

		// used to build tree lines 
		// starting with a route from the landed node to the top parent
		// we work from the root back to the leaf adding in the next sibling of each object (if there is one)
		// the first child of each sibling is also in order to draw the tree correctly
		var loadSiblings = function(node, callback){
			
			if(node.children){
				var child = node.children[0];
				var index = child.data.index;
				var total = node.data.total;
				
				if(index < total){
					var childUrl = node.data.childrenUrl + '[' + index + ']';
					loadData(childUrl, function(data){
						
						node.children.push(data);
						
						if(data.data.childrenUrl && data.data.total > 0){	// load 1st child
							loadFirstChild(data, function(fcData){
								data.children = [fcData];
								loadSiblings(child, callback);
							});
						}
						else{
							loadSiblings(child, callback);
						}			
					});
				}
				else{
					callback();
				}
			}
			else{
				callback();
			}
		};
		
		
		// build initial tree structure
		
		chainUp(baseUrl, false, function(data){
			loadSiblings(data, function(){
				log('Initialise tree with model:\n\n' + JSON.stringify(data));
				var tree = self.treeCmp.jstree({
					"core" : {
						"data" : data,
						"check_callback" : true
					},
					"plugins" : [ "themes", "json_data", "ui"]
				});
			});
		});		
	};
	
	return {
		init : function(baseUrl){
			init(baseUrl);
		},
		getContainer : function(){
			return self.container;
		},
		getVisibleNodes : function(){
			return getVisibleNodes();
		},
		getLocked : function(){
			return self.locked;
		},
		setLocked : function(val){
			self.locked = val;
		}	
	}
};

