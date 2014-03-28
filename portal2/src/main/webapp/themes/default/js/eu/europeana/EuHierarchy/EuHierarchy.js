var EuHierarchy = function(cmp, rows) {

	/*
	var Icon = function(el){
		
		var self = this;
		self.el  = el;
		
		self.position = function(){
			self.el.hide();
			var parent = self.el.parent();
			var h1 = parent.height();
			
			self.el.show();
			
			var h2 = parent.height();
			
			console.log('h1 = ' + h1 + ', h2 = ' + h2);
			
			if( h1 == h2){
				self.el.css('float:none');
			}
			else{				
				self.el.css('float:right');
			}
		};
		
		self.position();
		
		return {
			position: function(){
				self.position();
			}
		};
	};
	*/
	
	
	var self            = this;
	var rows            = rows;
	var defaultChunk    = rows;
	var lineHeight      = 1.4;
	
	self.treeCmp        = cmp;
	self.pageNodeId     = false;	// the id of the node corresponding to the full-doc page we're on
	self.silentClick    = false;
	self.container      = self.treeCmp.closest('.hierarchy-container');
	self.scrollDuration = 0;
	
	
	// debug vars
	var locked = true;
	var spin   = false;

	var log = function(msg){
		console.log(msg);
	};
	
	var formatNodeData = function(ob){
		if(ob.data && ob.data.europeana){
			//console.log('europeana data found....' + JSON.stringify(ob.data.europeana));
			
			if(ob.data.europeana.icon){
				if(ob.data.europeana.icon.toUpperCase() == 'IMAGE'){
					ob.text += '<span class="icon icon-image" aria-hidden="true"></span>';
				}
				else if(ob.data.europeana.icon.toUpperCase() == 'SOUND'){
					ob.text += '<span class="icon icon-sound" aria-hidden="true"></span>';
				}
				else if(ob.data.europeana.icon.toUpperCase() == 'TEXT'){
					ob.text += '<span class="icon icon-text" aria-hidden="true"></span>';
				}
				else if(ob.data.europeana.icon.toUpperCase() == 'VIDEO'){
					ob.text += '<span class="icon icon-video" aria-hidden="true"></span>';
				}
			}
			
			if(ob.data.europeana.url){
				var handler = ' onClick="javascript:window.hierarchy.nodeLinkClick(this)"';
				ob.text = '<a href="' + ob.data.europeana.url + '" ' + handler + '>' + ob.text + '</a>';
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
				if(ob.data.childrenUrl  && !ob.data.total){
					log('missing total for ' + ob.text);
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
	 * Positions in Loaded Open Tree (PILOT)
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
		var range = {};
		
		for(var i = backwards ? node.data.index : total; i> (backwards ? 0 : node.data.index); i--){
			range[i] = false;
		}
		
		//log('range A: ' + JSON.stringify(range))
		
		$.each(parent.children, function(i, ob){
			var child = self.treeCmp.jstree('get_node', ob);
			range[child.data.index] = true;
		});

		//log('range B: ' + JSON.stringify(range))

		// convert to array
		var keys = [];
		$.each(range, function(key, val){
			if(!val){
				keys.push(parseInt(key) -1 );  // tweak for zero counting here
			}
		});

		keys = keys.sort(function(a, b){ return b - a });

		//log('keys A: ' + JSON.stringify(keys))

		if(keys.length > max){
			keys = backwards ? keys.slice(0, max) : keys.slice(keys.length - max);
		} 

		//log('keys B: ' + JSON.stringify(keys))

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
		
		//log('consecutiveKeys C: ' + JSON.stringify(consecutiveKeys))

		var res = '';
		switch (consecutiveKeys.length){
		  case 0:
			  // if all are loaded - or if total is higher than actual children available (data error) we exit here
			  //log('getRange return no consecutive keys - keys were ' + keys + ', range was ' + JSON.stringify(range) + ', total is ' + total );
			  break;
		  case 1:
			  res = '.slice(' + consecutiveKeys[0] + ', ' + (consecutiveKeys[0] + 1) + ')';
			  break;
		  default:
			  res = '.slice(' + consecutiveKeys[consecutiveKeys.length-1] + ', ' +  (consecutiveKeys[0]+1) + ')';
		}
		return res;
	};
	
	
	// Loads first child of @node (if available) and executes @callback (if available) when done
	// @node: jstree object
	// @ callback: fn
	var loadFirstChild = function(node, callback){
		if(node.data && node.data.childrenUrl && (!node.children || !node.children.length) ){
			var firstChildUrl = node.data.childrenUrl + '[0]';
			loadData(firstChildUrl, function(fcData){
				if(self.treeCmp){
					self.treeCmp.jstree("create_node", node, formatNodeData(fcData), "first", false, true);
					//new Icon( $('#' + fcData.id).find('.icon') ) ;
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


	// Main load function - (recursive)
	//
	// @node: (object) - the jstree node to load from 
	// @backwards: (boolean) - direction in tree
	// @leftToLoad: (number) - number of nodes still to load
	// @deepen: (boolean) - used to change the load depth
	// @callback: (fn) - function to execute on completion
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
					alert('CAN THIS CHECK BE DELETED???')
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
				
				
				if(backwards){
					var origIndex = 0;							// find the index of @node
					$.each(parent.children, function(i, ob){	// this is only the same as node.data.index if everything is loaded
						if(ob == node.id){
							origIndex = i;
						}
					});
					
					if(origIndex>0){	// the node 
						var prevNode = self.treeCmp.jstree('get_node', parent.children[origIndex-1]);
						if(self.treeCmp.jstree( 'is_disabled', prevNode )){
							var disabledNodesLastChild = prevNode.children[prevNode.children.length - 1];
							
							node = self.treeCmp.jstree('get_node', disabledNodesLastChild);
							parent = prevNode;							
						}						
					}
				}
				
				
				var urlSuffix = getRange( node, backwards, leftToLoad ); // rows param
				var url       = parent.data.childrenUrl + urlSuffix;

				
				if(urlSuffix.length){
					loadData(url, function(data){
						
						log(' - loaded ' + data.length)
												
						$.each(backwards ? data.reverse() : data, function(i, ob) {

							var newId   = self.treeCmp.jstree("create_node", parent, formatNodeData(ob), backwards ? "first" : "last", false, true);
							var newNode = self.treeCmp.jstree('get_node', newId);

							//new Icon( $('#' + newId).find('.icon') ) ;
							//alert( 'append ' + (newId) );// $('#' + newId).length );
							
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
				//log('no parent data - text = ' + parent.text + ' parent = ' + node.parent + '\nobject = ' + JSON.stringify(parent) );
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
//		alert('hide s')
		self.container.prev('.ajax-overlay').hide();
	};
	
	var nodeLinkClick = function(e){
		var url = window.location.href.indexOf('?') ? window.location.href.split('?')[0] : window.location.href;
		window.location.href = url + '?' + $(e).attr('href');
	}
	
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
		
		//console.log()
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
		loadAndScroll(getVisibleNodes()[0], true, false, function(){
		});
	});

	/**
	 * Load wrapper to handle scrolling.
	 * 
	 * @initiatingNode - jsnode to load from
	 * @backwards - boolean
	 * @keyedNode - object.... shortcut to hovered node - false if user clicked
	 * @callback - fn
	 * */
	var loadAndScroll = function(initiatingNode, backwards, keyedNode, callback){

		// Scroll tracking:
		// Get the tree height and offset
		
		//var heightMeasureSel  = '.jstree-container-ul>.jstree-node>.jstree-children';
		//var disabledMeasure   = $('.jstree-container-ul .jstree-disabled').first().height();
		var disabledCount     = $('.jstree-container-ul .jstree-disabled').length;
		//var origHeight        = $(heightMeasureSel).height();
		var origScrollTop     = parseInt(self.container.scrollTop());		
		var visibleNodes      = getVisibleNodes();
		var initFromTop       = !keyedNode ? false : (visibleNodes[0] == keyedNode || keyedNode.state.disabled);
		var origTopNodeId     = visibleNodes[0].id;
				
		// load nodes
		showSpinner();
		viewPrevOrNext(initiatingNode, backwards,  defaultChunk, true, function(){

			//hideSpinner();
			
			//var newHeight        = $(heightMeasureSel).height();
			var containerH       = self.container.height();
			//var diffHeight       = newHeight - origHeight;
			//var resetScrollTop   = origScrollTop + diffHeight;
			var newDisabledCount = $('.jstree-container-ul .jstree-disabled').length;
			//var extraDiff        = (disabledCount - newDisabledCount) * disabledMeasure;
			  //  diffHeight      += extraDiff;

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

			// If we're skipping the scroll it's because we're keying up or down
			// Keying up can enable hitherto disabled parents
			// Invoking the load on the element immediately under a disabled parent by keying up
			// causes no vertical movement in the viewport - hence we bump the scrollTop by one
			// element height here
			//
			// Relates to calls up (@backwards = true)
			//
			// returns true if we bumped
			var skipScrollBump = function(){

				var enabledSomething = newDisabledCount != disabledCount;
				
				if(backwards && enabledSomething){
					var clickTgtId = getVisibleNodes()[2].id
					var clickTgt   = $('#' + clickTgtId + '>a');
					self.silentClick = true;
					clickTgt.click();
					
					// fine tune scroll & regain focus
					self.scrollDuration = 0;
					doScrollTo('#' + clickTgtId, function(){
						self.scrollDuration = 1000;
						hideSpinner();
						clickTgt.focus();
					});
					return true;
				}				
				return clickTgt;
			};

			if(keyedNode){
	
				self.scrollDuration = 0;
				doScrollTo('#' + origTopNodeId);
				self.scrollDuration = 1000;

				if(initFromTop){
					var vNode = skipScrollBump();					
				}
				else{
					$('#' + initiatingNode.id + '>a').focus();
				}
				
				hideSpinner();

				if(callback){
					callback();
				}
			}
			else{ // clicked (not keyed)

				// After the invisible reset, the animated scroll
				var finalScroll      = function(){

					hideSpinner();
					
					//new
					var scrollTop    = self.container.scrollTop();
					var newScrollTop = scrollTop;
					
					//var newScrollTop = resetScrollTop;					
					var visibleNodes = getVisibleNodes();
					
					if(initiatingNode == visibleNodes[0]){

						//var newScrollTop      = resetScrollTop;
						//var singleNodeMeasure = self.container.find('.jstree-anchor').first().height();
						
						if(backwards){
							newScrollTop -= containerH;
						}
						else{
							newScrollTop += containerH;
						}
						newScrollTop = Math.max(0, newScrollTop);				
					}
					else{
						alert('how can this not be true????')
					}

					if(newScrollTop == scrollTop){
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
				// end fn:finalScroll()
				
				self.scrollDuration = 0;
				doScrollTo('#' + origTopNodeId);
				self.scrollDuration = 1000;
				finalScroll();			// scroll from reset scroll view
			}
		});
	};	
	
	
	$('.hierarchy-next').click(function(){
		var visibleNodes = getVisibleNodes();
		loadAndScroll(visibleNodes[0], false, false, function(){
			togglePrevNextLinks();
		});
	});

	
	var getVisibleNodes = function(){
		
		var topEntry    = false;
		var bottomEntry = false;
		var beforeTop   = false;
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
			else{
				beforeTop = node;
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
		beforeTop   = beforeTop   ? beforeTop   : topEntry;
		return [topEntry, bottomEntry, beforeTop];
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
			if(!self.silentClick){
				doOnSelect(data.node);				
			}
			self.silentClick = false;
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
					var disabledCount    = $('.jstree-container-ul .jstree-disabled').length;
					loadAndScroll(initiatingNode, backwards, hoveredNode, function(){
						setLoadPoint(initiatingNode.id);
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
			
			var fChild = self.treeCmp.jstree('get_node', data.node.children[0]);
			
			showSpinner();
			loadFirstChild(fChild, function(){
				viewPrevOrNext(fChild, false, defaultChunk, true, function(){
					setLoadPoint(data.node.id);
					hideSpinner();
					
					$('#' + data.node.id + '>a').focus();
					setTimeout(function(){
						togglePrevNextLinks();
					}, 500);

				});
			});

		});

		// CLOSE

		self.treeCmp.bind("close_node.jstree", function(event, data) {
			log('closed ' + data.node.text);

			showSpinner();
			viewPrevOrNext(data.node, false, defaultChunk, true, function(){
				
				setLoadPoint(data.node.id);
				hideSpinner();
				
				$('#' + data.node.id + '>a').focus();
				setTimeout(function(){
					togglePrevNextLinks();	//  delay needed for animations to finish
				}, 500);
				
			});			
		});

		// END TREE BINDING
		
		var chainUp = function(url, data, callbackWhenDone){
			if(!url){
				callbackWhenDone(data);
				return;
			}

			loadData(url, function(newDataIn){
								
				var newData;
					newData = formatNodeData(newDataIn);

				if(!data){
					data            = newData;//formatNodeData( newData );
					self.pageNodeId = data.id;
				}
				else{
					newData.state    = {"opened" : true, "disabled" : true};
					newData.children = $.isArray(data) ? data : [data];
					data             = newData;
				}
				
				var recurseData = false;
				if($.isArray(newData) && newData.length){
					alert('this should never happen');
					recurseData = data[0].data;
				}
				else{
					recurseData = data.data;
				}

				if(recurseData && recurseData.parentUrl && recurseData.index){					
					chainUp(recurseData.parentUrl, data, callbackWhenDone);							
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
			
			if(node.children && $.isArray(node.children)){
	
				var child = node.children[node.children.length-1];
				var index = child.data.index;				
				var total = node.data.total;
				
				if(index < total){
					
					var childUrl = node.data.childrenUrl + '[' + index + ']';	// load single sibling
					loadData(childUrl, function(data){
						
						node.children.push( formatNodeData(data) );
						
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
				
				//log('Initialise tree with model:\n\n' + JSON.stringify(data));
				
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
		nodeLinkClick : function(e){
			nodeLinkClick(e);
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

/*
TODO:

 - icon should be pinned right ONLY if it would be the only thing on a new line

 - bug (focussing wrong node)
   - open this:
     - file:///home/andy/git/portal/portal2/src/main/webapp/themes/default/js/eu/europeana/EuHierarchy/index.html?base=dataGen.apocalypse_vol_2%28%29
     

SCROLL BEHAVIOUR

 		Key Up:
 		
 		move up 1 item only, bringing 1 item into view to do so if necessary
 		
 		Error:
 		 - file:///home/andy/git/portal/portal2/src/main/webapp/themes/default/js/eu/europeana/EuHierarchy/index.html?base=dataGen.apocalypse_vol_3_content%28%29[2]
		 - (animates a lengthy scroll)

		
 		Key Down:

 		Key Left (close):

		(should load down) and not scroll

 		Key Right (open):

		(should load down) and not scroll


		Click Up:
		
 		Should scroll up by the viewport height minus height of 1 item
 		
 		Error:
 		 - file:///home/andy/git/portal/portal2/src/main/webapp/themes/default/js/eu/europeana/EuHierarchy/index.html?base=dataGen.apocalypse_vol_3_content%28%29[2]
 		 - the pre-animate scroll is wrong
		 - before the scroll is hidden

*/


