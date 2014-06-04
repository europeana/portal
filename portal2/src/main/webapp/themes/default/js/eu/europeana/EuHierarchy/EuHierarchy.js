var EuHierarchy = function(cmp, rows, wrapper) {

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
	self.expandingAll   = false;
	self.isLoading      = false;
	self.container      = self.treeCmp.closest('.hierarchy-container');
	self.scrollDuration = 0;
	
	self.topPanel       = wrapper.find('.hierarchy-top-panel');
	self.bottomPanel    = wrapper.find('.hierarchy-bottom-panel');
	
	
	// debug vars
	var locked = true;

	var log = function(msg){
		console.log(msg);
	};
	
	var formatNodeData = function(ob){
		
		//ob.text = "<span>" + ob.text + "</span>";
		//return ob;//////////////////////////////////////
		
		
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
				var handler = '';// ' onClick="javascript:window.hierarchy.nodeLinkClick(this)"';
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
			
//		log('viewPrevOrNext -> ' + node.text + ', backwards -> ' + backwards + ', deepen -> ' + deepen);
		
		if( (!backwards) && deepen){	/* find deepest opened with children */
			
			var switchTrackNode = node;
			
			while(switchTrackNode.children.length && switchTrackNode.state.opened){
				switchTrackNode = self.treeCmp.jstree('get_node', switchTrackNode.children[0]);
			}

			//log('switchtrack 1: ' + switchTrackNode.text);
			
			var parentLoaded = function(node){
				var parent = self.treeCmp.jstree('get_node', node.parent);
				if(!parent.data){
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
		self.container.prev('.ajax-overlay').hide();
	};
	
	var nodeLinkClick = function(e){
		var url = window.location.href.indexOf('?') ? window.location.href.split('?')[0] : window.location.href;
		window.location.href = url + '?' + $(e).attr('href');
	}
	
	
	var brokenArrows = function(){
		
		self.topPanel.find('.top-arrows').remove();
		
//		var topArrows = $('<div class="top-arrows"></div>').prependTo(self.topPanel);
		var topArrows = $('<div class="top-arrows"></div>').appendTo(self.topPanel);
		var vNodes    = getVisibleNodes();
		var xNode     = vNodes[0];
		var count     = 1;

		
		getName = function(){
			return  $('#' + xNode.id).find('>a>span').html();
		} 

		var origIndex = xNode.data.index;

		console.log('START WHILE ON NODENAME ' + getName());


		/**
		 * When the calling node is the start of an open tree then it doesn't write the first arrow (that's the one on the far right).
		 * 
		 * This happens because @node is set to @node.parent before the recursion begins.
		 * 
		 * Simplest implementation is to treat this case as the exception and force it to display an arrow.
		 * 
		 * This is correct within the tree (works with or without the fix):
		 * 
		 *  ^^^ ^
		 *  _________
		 *  ||| |
		 *  ||| |
		 * 
		 * 
		 * If we're at the start of branch 'b' then it does this:
		 * 
		 *  ^^^
		 *  ____b_____
		 *  ||| |
		 *  ||| |
		 * 
		 * 
		 * without the override fix.  With the fix the arrowhead above the branch is no longer omitted:
		 * 
		 *  ^^^ ^
		 *  ____b_____
		 *  ||| |
		 *  ||| |
		 * 
		 * 
		 **/
		var overrideSpacer = false;
		if( (xNode.id != '#') && $('#' + xNode.id).hasClass('jstree-open')  ){
			overrideSpacer = true;
		}
		if( $('#' + xNode.id).hasClass('jstree-last')  ){
			overrideSpacer = true;
		}

		/* a node gets an arrow if:
		 *  - it has a ".jstree-children" element immediately below it
		 *  - 
		 *  
		 */
		while(xNode.parent){
			
			origIndex = xNode.data.index;				
			xNode     = self.treeCmp.jstree('get_node', xNode.parent );
			
			if( xNode.id != '#'){
				var totalChildren = xNode.data.total;				
				if($('#' + xNode.id + ' > .jstree-children').length   || $('#' + xNode.id).hasClass('jstree-last')  ){
					
					count ++;
					
					var createClass  = (origIndex == totalChildren ? 'arrow-spacer' : 'arrow top');

					if(overrideSpacer){
						createClass    = 'arrow top';
						overrideSpacer = false;
					}
					
					var createString = '<div class="' + createClass + '" style="right:' + count + 'em">';
					topArrows.append(createString);
					topArrows.css('width', count + 'em');
					
					$('.hierarchy-prev').css('margin-left', (count+2) + 'em');
				}
			}			
		}// end while	

		brokenArrowsBottom(vNodes);
	}
	
	
	

	var brokenArrowsBottom = function(vNodes){

		self.bottomPanel.find('.bottom-arrows').remove();
		
		var bottomArrows = $('<div class="bottom-arrows"></div>').prependTo(self.bottomPanel);
		var xNode        = vNodes[1];
		var count        = 2; // CHANGE

		getName = function(){
			return  $('#' + xNode.id).find('>a>span').html();
		} 

		var origIndex = xNode.data.index;

		console.log('BROKEN ARROWS START ON NODENAME ' + getName());


		/**
		 * Mid branch is OK
		 * 
		 *  |||||
		 *  |||||
		 *  _________
		 *  ^^^^^
		 *  
		 *  But with the top override we get this (wrong - last arrow missing)
		 * 
		 *  |||||
		 *  ||||b
		 *  _________
		 *  ^^^^
		 *  
		 *  	THIS APPLIES ONLY where branch 'b' has open children
		 *  
		 *  And with no override we get this:
		 *  
		 * 
		 */
		var overrideSpacer = false;

		if( (xNode.id != '#') && $('#' + xNode.id).hasClass('jstree-open')  ){			
			var createClass  = 'arrow bottom';
			var createString = '<div class="' + createClass + '" style="right:' + count + 'em">';
			count++;
			bottomArrows.append(createString);
			bottomArrows.css('width', count + 'em');
		}
		
		while(xNode.parent){
			origIndex = xNode.data.index;				
			xNode     = self.treeCmp.jstree('get_node', xNode.parent );

			if( xNode.id != '#'){
				var totalChildren = xNode.data.total;

				if($('#' + xNode.id + ' > .jstree-children').length){
					count ++;
					
					var createClass  = (origIndex == totalChildren ? 'arrow-spacer' : 'arrow bottom');

					if(overrideSpacer){
						createClass    = 'arrow bottom';
					}
					
					var createString = '<div class="' + createClass + '" style="right:' + count + 'em">';
					
					// CHANGE
					bottomArrows.append(createString);
					bottomArrows.css('width', count + 'em');
					
					$('.hierarchy-next').css('margin-left', (count+1) + 'em');
				}
			}			

			overrideSpacer = false;
		}// end while	
	}

	var getRandom = function(avoid){
		avoid = avoid ? parseInt(avoid.replace('(', '').replace(')', '')) : 1;
		var r = avoid;
		while(r == avoid){
			r = parseInt(Math.random() * 100);
		}
		return r;
	};
	
	var setPrevNextCount = function(){
		$('.hierarchy-next .count').html( '(' + getRandom( $('.hierarchy-next .count').html()) + ')' );
		$('.hierarchy-prev .count').html( '(' + getRandom( $('.hierarchy-prev .count').html()) + ')' );
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
		
		brokenArrows();
	
		setPrevNextCount();
		
		
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
	
	$('.hierarchy-prev>a').click(function(){
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

		if(self.isLoading){
			return;
		}
		self.isLoading = true;
		
		// Scroll tracking:
		// Get the tree height and offset
		
		var disabledCount     = $('.jstree-container-ul .jstree-disabled').length;
		var origScrollTop     = parseInt(self.container.scrollTop());		
		var visibleNodes      = getVisibleNodes();
		var initFromTop       = !keyedNode ? false : (visibleNodes[0] == keyedNode || keyedNode.state.disabled);
		var origTopNodeId     = visibleNodes[0].id;
				
		// load nodes
		showSpinner();
		viewPrevOrNext(initiatingNode, backwards,  defaultChunk, true, function(){
			var containerH       = self.container.height();
			var newDisabledCount = $('.jstree-container-ul .jstree-disabled').length;

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
				
				var clickTgtId = getVisibleNodes()[2].id
				var clickTgt   = $('#' + clickTgtId + '>a');
				if(backwards && enabledSomething){
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
				if(backwards){
					clickTgt.focus();
				}
			};

			if(keyedNode){
				self.scrollDuration = 0;
				doScrollTo('#' + origTopNodeId);
				self.scrollDuration = 1000;

				if(initFromTop){
					skipScrollBump();
				}
				else{
					$('#' + initiatingNode.id + '>a').focus();
				}
				
				hideSpinner();
				
				if(callback){
					self.isLoading = false;
					callback();
				}
			}
			else{ // clicked (not keyed)

				// After the invisible reset, the animated scroll
				var finalScroll      = function(){
					hideSpinner();
					
					var scrollTop    = self.container.scrollTop();
					var newScrollTop = scrollTop;
					var visibleNodes = getVisibleNodes();
					
					if(initiatingNode == visibleNodes[0]){
						
						if(backwards){
							newScrollTop -= containerH;
						}
						else{
							newScrollTop += containerH;
						}
						newScrollTop = Math.max(0, newScrollTop);				
					}
					else{
						alert(
							  'ERROR CODE 1\n\n'
							+ 'If you see this please record the what steps were needed to produce this error and what browser was used, and let Andy know about it\n\n'
							+ 'initiatingNode != visibleNodes[0]\n\ninitiator was ' + initiatingNode.id 
						)
					}


					doScrollTo(newScrollTop, function(){
						
						self.scrollDuration = 0;
						setTimeout(function(){

							doScrollTo('#' + getVisibleNodes()[0].id, function(){

								self.scrollDuration = 1000;
								togglePrevNextLinks();
								
								self.isLoading = false;
								
								if (callback) {
									callback();
								}
								
							});	// tidy scroll

							
						}, 1);
						
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
	
	
	$('.hierarchy-next>a').click(function(){
		var visibleNodes = getVisibleNodes();
		loadAndScroll(visibleNodes[0], false, false, function(){
			togglePrevNextLinks();
		});
	});

	$('.expand-all').click(function(){
		expandAll();
	});

	$('.collapse-all').click(function(){
		collapseAll();
	});
	

	
	var getVisibleNodes = function(){

		var overlayShowing = $('.ajax-overlay').is(':visible');
		
		if(overlayShowing){
			hideSpinner();
		}

		var topNode      = null;
		var bottomNode   = null;
		var previousNode = null;
		
		var bottomTop    = self.bottomPanel[0].getBoundingClientRect().top;
		var topBottom    = self.topPanel[0].getBoundingClientRect().bottom;
		
		
		$('.jstree-anchor').not('.jstree-disabled').each(function(i, ob){
						
			var newTop = ob.getBoundingClientRect().top + 2;
			
			console.log('newTop ' + newTop + ' ' + $(ob).html());
			
			if( !topNode && (newTop > topBottom) ){
				topNode = ob;
			}			
			if( newTop > bottomTop ){
				return false;	// exit loop
			}
			bottomNode = ob;
		});
		
		
		bottomNode   = $(bottomNode).closest("li.jstree-node");
		topNode      = $(topNode)   .closest("li.jstree-node");
		previousNode = topNode      .prevAll("li.jstree-node:first");
		
		if( !previousNode.length  ){
			previousNode = topNode.closest("li.jstree-node");
		}
		if( !previousNode.length  ){
			previousNode = topNode;
		}
		
		if(overlayShowing){
			showSpinner();
		}
				
		return [
			self.treeCmp.jstree('get_node', topNode.attr('id') ),
			self.treeCmp.jstree('get_node', bottomNode.attr('id') ),
			self.treeCmp.jstree('get_node', previousNode.attr('id') )
        ];
	}
	
	// END UI BINDING
	
	
	
	// Instantiate tree here.
	// We load up the tree to get the absolute root.
	var init = function(baseUrl){
		
		// Set UI viewport size
		
		var setContainerHeight = function(){
			
			self.container.css({
				'height':         (rows * lineHeight) + 'em',
				'max-height':     (rows * lineHeight) + 'em'
			});
			self.treeCmp  .css('padding-bottom', (rows * lineHeight) + 'em');
						
			var remainderRemoved = self.container.outerHeight(true);
			remainderRemoved = remainderRemoved - (remainderRemoved % rows);
			remainderRemoved+=1;
			
			console.log('init container height at ' + remainderRemoved)
			self.container.css({
				'height':     remainderRemoved + 'px',
				'max-height': remainderRemoved + 'px'				
			});
		}
		
		var zoom = document.documentElement.clientWidth / window.innerWidth;
		$(window).resize(function() {
		    var zoomNew = document.documentElement.clientWidth / window.innerWidth;
		    if (zoom != zoomNew) {	// zoom has changed
		        zoom = zoomNew;
				setContainerHeight();
				doScrollTo('#' + getVisibleNodes()[0].id, function(){
					togglePrevNextLinks();
				});

		    }
		});
		setContainerHeight();	
	
	
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

			if(self.expandingAll){
				return;
			}
			if(self.isLoading){
				return;
			}

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
						
						togglePrevNextLinks();
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
			if(self.expandingAll){
				return;
			}
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
			if(self.expandingAll){
				return;
			}
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
								
				var newData = formatNodeData(newDataIn);

				if(!data){
					data            = newData;
					self.pageNodeId = data.id;
				}
				else{
					newData.state    = {"opened" : true, "disabled" : true};
					newData.children = $.isArray(data) ? data : [data];
					data             = newData;
				}
				
				var recurseData = false;
				if($.isArray(newData) && newData.length){
					
					alert('ERROR CODE 2\n\n'
						+ 'If you see this please record the what steps were needed to produce this error and what browser was used, and let Andy know about it\n\n'
						+ 'Load error.' 
					)

					recurseData = data[0].data;
				}
				else{
					recurseData = data.data;
				}

				if(recurseData && recurseData.parentUrl && recurseData.index){					
					chainUp(recurseData.parentUrl, data, callbackWhenDone);							
				}
				else{
					wrapper.find('.hierarchy-title>a').html(data.text);
					wrapper.find('.hierarchy-title>.count').html('(contains ' + getRandom(0) + ' items)');
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

	
	var expandAll = function(){
		
		showSpinner();
		self.expandingAll = true;			
		
		var callingNodeId   = getVisibleNodes()[0].id;
		self.scrollDuration = 0;
		
		var expand = function(node){
			showSpinner();
			
			var getQty = function(node){
				return node.data.childrenUrl ? node.data.total - node.children.length : 0;
			}
			
			var loadChildren = function(node, completeCallback){
				var remaining = getQty(node);
				
				if(node.data && node.data.childrenUrl && remaining>0){
					var start        = node.children ? node.children.length : 0;
					var urlSuffix    = '.slice(' + start + ',' + (start + (remaining > defaultChunk ? defaultChunk : remaining)) + ')';
					var childUrl     = node.data.childrenUrl + urlSuffix;
					
					loadData(childUrl, function(data){
						$.each(data, function(i, ob) {
							var newId = self.treeCmp.jstree("create_node", node, formatNodeData(ob), "last", false, true);
							remaining --;
						});
						callLoadChildren(node, completeCallback, remaining);						
					});
				}
				else{
					callLoadChildren(node, completeCallback, remaining);
				}
			};

			var callLoadChildren = function(node, completeCallback, countRemaining){
								
				if(countRemaining<=0){  /*  countRemaining==0 works for FF, but <=0 needed for chrome.  Suffix wrong? TODO   */
					if(countRemaining<0){
						alert('CHROME BUG HERE:  LESS\n\n' +  $( '#' + node.id ).find('>a').html() );
					}
					completeCallback(node);
				}
				else{
					loadChildren(node, completeCallback);
				}
			};

			
			var allChildrenLoaded = function(node, createdNodes){
				$.each(node.children, function(i, ob){
					var cNode = self.treeCmp.jstree('get_node', ob);
					callLoadChildren(cNode, allChildrenLoaded, getQty(cNode), []);
				});
				self.silentClick = true;
				self.treeCmp.jstree('open_node', node);
			};
			
			callLoadChildren(node, allChildrenLoaded, getQty(node), []);

			setTimeout(function(){
				$('.hierarchy-next').hide();
				$('.hierarchy-prev').hide();
				$('.bottom-arrows').hide();
				$('.top-arrows').hide();
				
				self.container.css('height',         'auto');
				self.container.css('max-height',     'none');
				self.treeCmp  .css('padding-bottom', '0');

				doScrollTo($('#' + callingNodeId));
				hideSpinner();				
			}, 5000)
		};
					
		
		var loadUpCallback = function(){
			
			if(wrapper.find('.hierarchy-prev').is(':visible')){
				loadUp();
			}
			else{
				expand(getVisibleNodes()[0]);
			}				
		};
		
		var loadUp = function(){
			loadAndScroll(getVisibleNodes()[0], true, false, loadUpCallback);
		};
		
		loadUp();
		
	};

	var collapseAll = function(){
		
		setTimeout(function(){
			showSpinner();
		},1);
		
		setTimeout(function(){
			
			$('.bottom-arrows').show();
			$('.top-arrows').show();

			var height = rows * lineHeight;
			
			self.container.css('height',         height + 'em');
			self.container.css('max-height',     height + 'em');
			self.treeCmp  .css('padding-bottom', height + 'em');

			$('.jstree-open').each(function(i, ob){				
				self.treeCmp.jstree('close_node', $(ob).attr('id'));
			});

			$.scrollTo(self.topPanel, {offset:-16});

			doScrollTo(getRootEl(), function(){
				setTimeout(function(){
					hideSpinner();
					self.expandingAll = false;
					togglePrevNextLinks();
					
				},10);
				
			});
			
		}, 100);
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
		
		expandAll : function(){
			expandAll();
		},
		collapseAll : function(){
			collapseAll();
		},
		getLocked : function(){
			return self.locked;
		},
		setLocked : function(val){
			self.locked = val;
		},
		brokenArrows : function(){
			brokenArrows();
		},
		scrollTop : function(val){	// debug function

			if(val){
				self.container.scrollTop(val);
			}
			else{
				alert('scroll top is: ' + self.container.scrollTop());
			}
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


