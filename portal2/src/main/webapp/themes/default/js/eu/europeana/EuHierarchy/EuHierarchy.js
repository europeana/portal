var EuHierarchy = function(cmp, rows, wrapper) {

	
	var self             = this;
	var debug            = true;
	var dataServerPrefix = window.hierarchicalObjectServer ? window.hierarchicalObjectServer : '';
	var rows             = rows;
	var defaultChunk     = rows * 2;
	var lineHeight       = 1.4;
	
	
	self.treeCmp         = cmp;
	self.timer           = null;
	self.pageNodeId      = false;		// the id of the node corresponding to the full-doc page we're on
	self.silentClick     = false;
	self.loadingAll      = false;
	self.loadedAll       = false;	
	self.isLoading       = false;
	self.container       = self.treeCmp.closest('.hierarchy-container');
	self.scrollDuration  = 0;
	self.topPanel        = wrapper.find('.hierarchy-top-panel');
	self.bottomPanel     = wrapper.find('.hierarchy-bottom-panel');
	
	
	// debug vars
	var locked           = true;
	var defaultDelay     = 500;	
    	
	var Timer = function(){
		
		var self               = this;
		self.timerId           = 0;
		self.secondsElapsed    = 0;

		self.secondElapsed = function(){
			
			self.secondsElapsed ++;
			var msg     = '';
			var nextMsg = '';
			
			if(waitMessages){
				$.each(waitMessages, function(i, ob){
					if(self.secondsElapsed > ob.time){
						msg = ob.msg;
					}
					if(self.secondsElapsed+2 > ob.time){
						nextMsg = ob.msg;
					}
				});				
			}
			
			var currentMsg = $('.wait-msg').html()
			
			if(nextMsg != currentMsg){
				$('.wait-msg').removeClass('opaque');
				$('.wait-msg').addClass('transparent');
			}

			if(msg != currentMsg){
				$('.wait-msg').removeClass('transparent');
				$('.wait-msg').addClass('opaque');
			}
			
			$('.wait-msg').html(msg.length ? msg : '');
			
			log(self.secondsElapsed + ' second' + (self.secondsElapsed==1?'':'s')  + ' elapsed' + (msg.length ? ' - ' + msg : '.') );
		}
		
		self.start = function(){
			
			self.secondsElapsed    = 0;
			
			$('.wait-msg').html('');
			try{
				window.clearInterval(self.timerId);				
			}
			catch(e){}

			self.timerId = window.setInterval(self.secondElapsed, 1000);
		};
		
		self.stop = function(){
			log('STOP TIMER')
			window.clearInterval(self.timerId);
		};
		
		return {
			start : function(){
				self.start();
			},
			stop : function(){
				self.stop();
			}
		}
		
	};
	
	
	
	var log = function(msg){
		if(debug){
			console.log(msg);			
		}
	};
	
	var formatNodeData = function(ob){
		
		if(ob.data && ob.data.europeana){
			
			var iconText = '';
			if(ob.data.europeana.icon){
				if(ob.data.europeana.icon.toUpperCase() == 'IMAGE'){
					iconText = 'icon-image';
				}
				else if(ob.data.europeana.icon.toUpperCase() == 'SOUND'){
					iconText = 'icon-sound';
				}
				else if(ob.data.europeana.icon.toUpperCase() == 'TEXT'){
					iconText = 'icon-text';
				}
				else if(ob.data.europeana.icon.toUpperCase() == 'VIDEO'){
					iconText = 'icon-video';
				}
			}
			
			if(ob.data.europeana.url){
				
				//var handler = 'onClick="javascript:window.hierarchy.nodeLinkClick(this)"';
				//var handler = 'onClick="javascript:alert(\'Eventually this will link to the object page of this item.\')"';
				//ob.text = '<a href="' + ob.data.europeana.url + '" ' + handler + '>' + ob.text + '</a>';
			}
		}
		
		ob.text = '<span class="icon ' + iconText + '">' + ob.text + '&nbsp;&nbsp;</span>';

		return ob;
	}
	
	// this simulates ajax call

	var loadData = function(url, callback) {
		if(!url){
			alert('load data called without url');
			callback([]);
			return;
		}
		
		if(dataServerPrefix.length){
			url = dataServerPrefix + url;
			$.ajax({
				url: url,
				context: document.body
			}).done(function(data) {
				callback(data);
			});
		}
		else{
			var data = eval(url);
			
			// sanity
			$.each(data, function(i, ob){
				if(ob.data){
					if(!ob.data.index){
						alert('missing index for ' + ob.text);
					}
					if(ob.data.childrenUrl  && !ob.data.total){
						alert('missing total for ' + ob.text);
					}
				}
			});
			
			setTimeout(function(){
				callback(data);
			}, defaultDelay);			
		}
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
			console.log('getRange returns empty no parent')
			return '';
		}
		
		var parent = self.treeCmp.jstree('get_node', node.parent);

		if(!parent.data || (typeof parent.data).toLowerCase() == 'function' ){
			console.log('getRange returns empty - no parent data')
			return '';
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

		console.log('range B: ' + JSON.stringify(range))

		// convert to array
		var keys = [];
		$.each(range, function(key, val){
			if(!val){
				keys.push(parseInt(key) -1 );  // tweak for zero counting here
			}
		});

		keys = keys.sort(function(a, b){ return b - a });

		console.log('keys A: ' + JSON.stringify(keys))

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
		
		console.log('consecutiveKeys C: ' + JSON.stringify(consecutiveKeys))

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
		
		console.log('getRange() returns ' + res);
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

			log('switchtrack 1: ' + switchTrackNode.text);
			
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
			
			log('switchtrack 2: ' + switchTrackNode.text);
			
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
									//log('recurse point one')
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
							//log('enable node ' + parent);
							
							self.treeCmp.jstree("enable_node", parent );
							leftToLoad --;
						}						
						if(leftToLoad > 0){
							//log('recurse point three');
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
								//log('could not recurse fwd (parent data a jquery function, not an object)')
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
			self.container.before('<div class="ajax-overlay"><span class="wait-msg"></span></div>');
		}
		
		self.container.prev('.ajax-overlay').show();	
		$('<style></style>').appendTo($(document.body)).remove();	// force repaint
	};
	
	var hideSpinner = function(){
		self.container.prev('.ajax-overlay').hide();
		$('<style></style>').appendTo($(document.body)).remove();	// force repaint
	};
	
	var nodeLinkClick = function(e){
		var url = window.location.href.indexOf('?') ? window.location.href.split('?')[0] : window.location.href;
		window.location.href = url + '?' + $(e).attr('href');
	}	
	
	var brokenArrows = function(){
		
		self.topPanel.find('.top-arrows').remove();
		
		var topArrows = $('<div class="top-arrows"></div>').appendTo(self.topPanel);
		var vNodes    = getVisibleNodes();
		var xNode     = vNodes[0];
		var rightIndent     = 1;

		
		getName = function(){
			return  $('#' + xNode.id).find('>a>span').html();
		} 

		var origIndex = xNode.data.index;


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
					
					rightIndent ++;
					
					var createClass  = (origIndex == totalChildren ? 'arrow-spacer' : 'arrow top');

					if(overrideSpacer){
						createClass    = 'arrow top';
						overrideSpacer = false;
					}
					
					var createString = '<div class="' + createClass + '" style="right:' + rightIndent + 'em">';
					topArrows.append(createString);
					topArrows.css('width', rightIndent + 'em');
					
					$('.hierarchy-prev').css('margin-left', (rightIndent+2) + 'em');
				}
			}			
		}// end while	

		brokenArrowsBottom(vNodes);
	}
	
	
	

	var brokenArrowsBottom = function(vNodes){

		self.bottomPanel.find('.bottom-arrows').remove();
		
		var bottomArrows = $('<div class="bottom-arrows"></div>').prependTo(self.bottomPanel);
		var xNode        = vNodes[1];
		var rightIndent  = 2; // CHANGE
		
		getName = function(){
			return  $('#' + xNode.id).find('>a>span').html();
		} 

		var origIndex = xNode.data.index;


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
			var createString = '<div class="' + createClass + '" style="right:' + rightIndent + 'em">';
			rightIndent++;
			bottomArrows.append(createString);
			bottomArrows.css('width', rightIndent + 'em');
		}
		
		while(xNode.parent){
			origIndex = xNode.data.index;				
			xNode     = self.treeCmp.jstree('get_node', xNode.parent );

			if( xNode.id != '#'){
				var totalChildren = xNode.data.total;

				if($('#' + xNode.id + ' > .jstree-children').length){
					rightIndent ++;
					
					var createClass  = (origIndex == totalChildren ? 'arrow-spacer' : 'arrow bottom');

					if(overrideSpacer){
						createClass    = 'arrow bottom';
					}
					
					var createString = '<div class="' + createClass + '" style="right:' + rightIndent + 'em">';
					
					// CHANGE
					bottomArrows.append(createString);
					bottomArrows.css('width', rightIndent + 'em');
					
					$('.hierarchy-next').css('margin-left', (rightIndent+1) + 'em');
				}
			}			

			overrideSpacer = false;
		}// end while	
	} // end function brokenArrowsBottom

	var getRandom = function(avoid){
		avoid = avoid ? parseInt(avoid.replace('(', '').replace(')', '')) : 1;
		var r = avoid;
		while(r == avoid){
			r = parseInt(Math.random() * 100);
		}
		return r;
	};
	
	var setPrevNextCount = function(){
		$('.hierarchy-next .count').html( '(' + getRandom( $('.hierarchy-next .count').html()) + ' items)' );
		$('.hierarchy-prev .count').html( '(' + getRandom( $('.hierarchy-prev .count').html()) + ' items)' );
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
		
		if (typeof el == 'undefined') {
			log('doScrollTo error - undefined @el');
			return;
		}
		self.container.css('overflow', 'auto');
		self.container.scrollTo(el, self.scrollDuration, {
			"axis" : "y",
			"onAfter" : function(){
				if(callback){
					
					if (locked) {
						self.container.css('overflow', 'hidden');
					}
					callback();
					return;
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
		if(!self.loadingAll){
			self.timer.start();			
		}
		
		$('.top-arrows').add($('.bottom-arrows')).addClass('transparent');
		
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
					if(!self.loadingAll){
						self.timer.stop();
					}
					callback();
				}
			}
			else{ // clicked (not keyed)

				
				// After the invisible reset, the animated scroll
				var finalScroll      = function(){
					
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

					hideSpinner();

					doScrollTo(newScrollTop, function(){
						
						self.scrollDuration = 0;
						setTimeout(function(){

							doScrollTo('#' + getVisibleNodes()[0].id, function(){

								self.scrollDuration = 1000;
								togglePrevNextLinks();
								
								if(!self.loadingAll){
									self.timer.stop();
								}
								
								self.isLoading = false;
								
								if (callback) {
									callback();
								}
								
							});	// tidy scroll							
							
						}, 1);
						
					});	
				}; // end fn:finalScroll()
				
				self.scrollDuration = 0;
				doScrollTo('#' + origTopNodeId);
				
				self.scrollDuration = 1000;
				finalScroll();			// scroll from reset scroll view
			}
		});
	};	
	
	
	$('.hierarchy-next>a').click(function(){
		loadAndScroll(getVisibleNodes()[0], false, false, function(){
			togglePrevNextLinks();
		});
	});

	$('.load-all').click(function(){
		loadAll();
	});

	$('.expand-all').click(function(){
		expandAll();
	});

	$('.collapse-all').click(function(){
		
		showSpinner();
		self.timer.start();
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
						
			var newTop = ob.getBoundingClientRect().top + 5;
			
			if( !topNode && (newTop >= topBottom) ){
				
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
	
	
	/**
	 * init()
	 * 
	 * - set container height
	 * - set timer
	 * - instantiate tree
	 * 		- Load up the tree to get the absolute root
	 * 		- (return to base node)
	 * - bind tree events
	 * 
	 * */
	
	var init = function(baseUrl){
		
		self.timer = new Timer();
		
		// Set UI viewport size
		
		var setContainerHeight = function(){
			
			if(self.loadedAll){
				return;
			}
			
			self.container.css({
				'height':         (rows * lineHeight) + 'em',
				'max-height':     (rows * lineHeight) + 'em'
			});
			self.treeCmp  .css('padding-bottom', (rows * lineHeight) + 'em');
						
			var remainderRemoved = self.container.outerHeight(true);
			remainderRemoved     = remainderRemoved - (remainderRemoved % rows);
			remainderRemoved    += 3;
			
			//console.log('init container height at ' + remainderRemoved)
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
			self.isLoading = true;
			
			if(!self.loadingAll){
				self.timer.start();			
			}
			
			viewPrevOrNext(node, false, defaultChunk, true, function(){
				doScrollTo($('#' + node.id), function(){
					togglePrevNextLinks();
					
					setLoadPoint(node.id);
					
					$('#' + node.id + '>a').focus();
					if(callback){
						callback();
					}
					self.isLoading = false;
					
					if(!self.loadingAll){
						self.timer.stop();
					}

					hideSpinner();
				});
			});
		};
		
		// select (invoke by loaded callback below)

		self.treeCmp.bind("select_node.jstree", function(event, data) {
			if(!self.silentClick){
				if(!self.loadedAll){
					doOnSelect(data.node);					
				}
			}
			self.silentClick = false;
			$('.debug-area').html(JSON.stringify(data.node));

		});

		// loaded
		
		self.treeCmp.bind("loaded.jstree", function(event, data) {
			
			// cancel default right-key handling
			//self.treeCmp.off('keydown.jstree', '.jstree-anchor');
			
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
			
			if(self.loadingAll){
				return;
			}
			if(self.isLoading){
				return;
			}

			// Catch 'Down' || 'Up' keystrokes
			
			if (e.which == 40 || e.which == 38) { 

				if(e.ctrlKey || e.shiftKey){
					
					var refocus = function(){
						$('#' + getVisibleNodes()[0].id + '>a').focus();						
					};
					
					if(e.which==38 && $('.hierarchy-prev').is(':visible')){
						loadAndScroll(getVisibleNodes()[0], true, false, function(){
							togglePrevNextLinks();
							refocus();
						});
					}
					
					if(e.which==40 && $('.hierarchy-next').is(':visible')){
						loadAndScroll(getVisibleNodes()[0], false, false, function(){
							togglePrevNextLinks();
							refocus();
						});
					}
					return;
				}

				
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
			if(self.loadingAll){
				return;
			}
			if(self.isLoading){
				return;
			}
			log('open node: ' + data.node.text);
			self.isLoading = true;
			self.timer.start();
			
			var fChild = self.treeCmp.jstree('get_node', data.node.children[0]);
			
			showSpinner();
			loadFirstChild(fChild, function(){
				viewPrevOrNext(fChild, false, defaultChunk, true, function(){
					setLoadPoint(data.node.id);
					hideSpinner();
					
					$('#' + data.node.id + '>a').focus();
					setTimeout(function(){
						togglePrevNextLinks();
						self.isLoading = false;
						self.timer.stop();
						
					}, 500);

				});
			});

		});

		// CLOSE

		self.treeCmp.bind("close_node.jstree", function(event, data) {
			if(self.loadingAll){
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
					wrapper.find('.hierarchy-title>.count').html('(contains ' + getRandom(0) + ' items)');
					wrapper.find('.hierarchy-title>a').html(data.text);
					wrapper.find('.hierarchy-title>a').click(function(){
						alert(	'When we start using real data this will link to the object page for the root item\n\n'
						+		'(unless we\'re already on that page, then this title will be plain text and not a link)');
					});
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

	
	/* function getQty
	 * 
	 * Recursive load helper - gets the number of children still to load for a given node
	 * 
	 * @node the node to analyse
	 * 
	 * returns the number of children that have still to be loaded for  @node
	 *  
	 *  */
	var getQty = function(node){
		//log(node.id + ' node.data.total ' + node.data.total + ', node.children.length ' + node.children.length + ' (url=' + node.data.childrenUrl + ')');
		return node.data.childrenUrl ? node.data.total - node.children.length : 0;
	}

	/* function recursiveLoad
	 * 
	 * @node the node to load
	 * @callback the callback to execute when done
	 *  
	 */
	/*
	var recursiveLoad = function(node, callback){
		
		var remaining = getQty(node);
	    
	    // append new nodes
		if(remaining){
			
			if(node.data && node.data.childrenUrl){
				var start        = node.children ? node.children.length : 0;
				var urlSuffix    = '.slice(' + start + ',' + (start + (remaining > defaultChunk ? defaultChunk : remaining)) + ')';
				var childUrl     = node.data.childrenUrl + urlSuffix;
				
				loadData(childUrl, function(data){
				
					var children = [];
					
					$.each(data, function(i, ob) {
						
						var newId   = self.treeCmp.jstree("create_node", node, formatNodeData(ob), "last", false, true);
						var newNode = self.treeCmp.jstree('get_node', newId);
						var newQty  = getQty(newNode);
						var loads   =  Math.ceil(newQty/defaultChunk);
						
						for(var j=0; j<loads; j++){
							children.push(newNode);
						}
						
						//log('pushed ' + newNode.id + ' ' + loads + ' times');
					}); 
					
					
					// open node
					
					if(!node.state.opened){
						self.silentClick = true;
						self.treeCmp.jstree('open_node', node);
					}
					
					
					// manage sub process
					
					var index = 0;
					var nodeDone = function(){
						index ++
						if(index < children.length){
							
							// recurse 
							recursiveLoad(children[index], 
								function(){
									if(index+1 == children.length){																							
										callback();
									}
									else{
										nodeDone();
									}
								}
							);
						}
						else{
							callback();
						}
					}; // end nodeDone
					
					if(children.length > 0){
						
						// recurse
						recursiveLoad(children[0], nodeDone);
					}
					else{
						callback();
					}
					
				}); // end loadData()
				
			}
			else{
				alert('is this ever used?');
				callback();						
			}
		}
		else{ // no remaining
			callback();
		}
	};
	*/

	var recursiveLoad = function(node, callback){
						
		var remaining = getQty(node);
	    
		var callCallback = function(){
			
			var uncheckedChildren = [];
			
			$.each(node.children, function(i, ob) {
				
				var cNode = self.treeCmp.jstree('get_node', ob);
				
				if(!cNode.data.rcl){
					uncheckedChildren.push(cNode);
				}	
			}); 

			if(uncheckedChildren.length>0){
				
				// manage sub process
				var index = 0;
				var nodeDone = function(){				
					index ++
					if(index < uncheckedChildren.length){
						
						// recurse 
						recursiveLoad(uncheckedChildren[index], 
							function(){
								if(index+1 == uncheckedChildren.length){																							
									callback();
								}
								else{
									nodeDone();
								}
							}
						);
					}
					else{
						callback();
					}
				}; // end nodeDone
				recursiveLoad(uncheckedChildren[0], nodeDone);
			}
			else{
				callback();				
			}			
		}
		
	    // append new nodes
		if(remaining){
			
			var start        = node.children ? node.children.length : 0;
			var urlSuffix    = '.slice(' + start + ',' + (start + (remaining > defaultChunk ? defaultChunk : remaining)) + ')';
			var childUrl     = node.data.childrenUrl + urlSuffix;
			
			loadData(childUrl, function(data){
			
				var children = [];
				
				$.each(data, function(i, ob) {

					var newId   = self.treeCmp.jstree("create_node", node, formatNodeData(ob), "last", false, true);
					var newNode = self.treeCmp.jstree('get_node', newId);
					
					var newQty  = getQty(newNode);
					var loads   =  Math.ceil(newQty/defaultChunk);

					for(var j=0; j<loads; j++){
						children.push(newNode);
					}
				}); 
				
				
				// open node
				
				if(!node.state.opened){
					self.silentClick = true;
					self.treeCmp.jstree('open_node', node);
				}
				
				// manage sub process
				
				var index = 0;
				
				var nodeDone = function(){
					index ++
					if(index < children.length){
						
						// recurse 
						recursiveLoad(children[index], 
							function(){
								if(index+1 == children.length){																							
									callCallback();
								}
								else{
									nodeDone();
								}
							}
						);
					}
					else{
						node.data.rcl = true;
						callCallback();
					}
				}; // end nodeDone
		

				if(children.length > 0){
					
					// recurse
					recursiveLoad(children[0], nodeDone);
				}
				else{
					node.data.rcl = true;
					callCallback();
				}
				
			}); // end loadData()
				
		}
		else{ // no remaining
			
			node.data.rcl = true;
			callCallback();
		}
	};


	
	/* function loadAll
	 * 
	 * @node the node to load
	 * @callback the callback to execute when done
	 *  
	 */
	var loadAll = function(){
		showSpinner();
		self.timer.start();
		
		self.loadingAll = true;
		
		var rNode = self.treeCmp.jstree('get_node', getRootEl().attr('id') );

		// fade the arrows
		
		$('.top-arrows').add($('.bottom-arrows')).removeClass('opaque').addClass('transparent');

		// enable disabled parents
		
		$('.jstree-disabled').each(function(i, ob){
			var ob = $(ob)
			self.treeCmp.jstree("enable_node", ob.closest('.jstree-node').attr('id'));
		});
		
		// load the lot
		
		recursiveLoad(rNode, function(){
			$('.hierarchy-next').hide();
			$('.hierarchy-prev').hide();
			
			self.container.css('height',         'auto');
			self.container.css('max-height',     'none');
			self.treeCmp  .css('padding-bottom', '0');
			
			$('.load-all').unbind('click');
			$('.load-all').addClass('disabled');
			
			self.loadedAll = true;
			self.timer.stop();
			hideSpinner();
		});		
	};
	
	
	/* function getCommonParent
	 * 
	 * returns the common ancestor of @node1 and @node2
	 *  
	 */
	self.getCommonParent = function(node1, node2){
		
		var parents1 = [];
		var result   = null;
		
		if(!node1 || !node2){
			return null;
		}
		
		parents1.push(node1.id);

		while(node1.parent){
			node1 = self.treeCmp.jstree('get_node', node1.parent);
			parents1.push(node1.id);
		}
		
		while(node2.parent){
			
			node2 = self.treeCmp.jstree('get_node', node2.parent);

			if( $.inArray(node2.id, parents1) > -1 ){
				result = node2;
				break;
			}
		}
		
		//console.log('getcp returns ' + (result ? result.text : 'null'));
		
		return result;
	};
	
	
	/* function expandAll
	 * 
	 * Finds the common ancestor or the visible nodes and recursively loads that node's unloaded children.
	 * 
	 * The nodes are then expanded
	 * 
	 * Nodes outwith the viewport will not be loaded unless their closest common ancestor is under that of the visible nodes
	 * 
	 * returns the common ancestor of @node1 and @node2
	 *  
	 */
	var expandAll = function(){
		
		var visible = getVisibleNodes();
		var cp      = self.getCommonParent(visible[0], visible[1]);
		var openId  = null;
			
		if(!cp){
			console.log('Error: no common parent found for ' + visible[0].id + ' and ' +  visible[1].id);
			return;
		}
		
		var afterLoad = function(){

			// timer to check when done.
			openId = setInterval(function(){
				
				if($('#' + cp.id + ' .jstree-closed').length < 2){	// ommit root (#) that can't be found by id
					
					self.loadingAll = false;
					window.clearInterval(openId);
					hideSpinner();
					togglePrevNextLinks();
					brokenArrows();
					self.timer.stop();

					self.scrollDuration = 1000;
				}
				//else{
					//console.log('still waiting for ' + $('#' + cp.id + ' .jstree-closed').length + ' to open under #' + cp.id );
					//if($('#' + cp.id + ' .jstree-closed').length == 1){
					//	console.log('  - waiting for ' + $('#' + cp.id + ' .jstree-closed')[0].text + '   = '+ $('#' + cp.id + ' .jstree-closed')[0].id );
					//}
				//}
			}, 500);

			$('#' + cp.id + ' .jstree-closed').each(function(i, ob){				
				self.treeCmp.jstree('open_node', $(ob).attr('id'));
			});
			
		}
		
		if(!cp.data.rcl){
			showSpinner();
			self.timer.start();
			self.loadingAll = true;

			if(self.treeCmp.jstree( 'is_disabled', cp )){
				self.treeCmp.jstree("enable_node", cp );
			}
			
			recursiveLoad(cp, function(){
				afterLoad();
			});			
		}
		else{
			afterLoad();
		}

		
		/*
		var loadUp = function(){
			loadAndScroll(getVisibleNodes()[0], true, false, function(){
				
				if(wrapper.find('.hierarchy-prev').is(':visible')){
					loadUp();
				}
				else{
					showSpinner();
					
					var startNode = getVisibleNodes()[0];
					var rNode     = self.treeCmp.jstree('get_node', getRootEl().attr('id') );

					self.treeCmp.jstree("delete_node", rNode.children );
					brokenArrows();

					recursiveLoad(rNode, function(){
						$('.hierarchy-next').hide();
						$('.hierarchy-prev').hide();
						$('.bottom-arrows').hide();
						$('.top-arrows').hide();
						
						self.container.css('height',         'auto');
						self.container.css('max-height',     'none');
						self.treeCmp  .css('padding-bottom', '0');
						
						self.timer.stop();
						hideSpinner();
					});

				}				
			});
		};
		loadUp();		
		*/
		
	};
	
	
	/* function collapseAll
	 * 
	 * Strategy: collapse the open child nodes of the common ancestor of the top and bottom visble nodes and repeat until all visible nodes are closed
	 * 
	 */
	var collapseAll = function(){
		
		self.loadingAll = true;  // TODO rename this variable
		
		var visible = getVisibleNodes();
		var cp      = self.getCommonParent(visible[0], visible[1]);
		var closeId = null;
		
		if(!cp){
			console.log('Error: no common parent found for ' + visible[0].id + ' and ' +  visible[1].id);
			return;
		}

		/**
		 * Test to see if we need to call collapseAll again.
		 * 
		 * */
		var recurseTest = function(){
			var visible  = getVisibleNodes();
			var bVisible = visible[1]; 

			if( $('#' + bVisible.id).hasClass('jstree-leaf') ){
				bVisible = self.treeCmp.jstree('get_node', bVisible.parent);
			}

			if( bVisible.state.opened ){
				if(self.treeCmp.jstree( 'is_disabled', bVisible )){
					self.treeCmp.jstree("enable_node", bVisible );
				}
				return true;
			}
			return false;
		};

		
		/**
		 * Execute when all collapsing is done.
		 * 
		 * */
		var afterLoad = function(){			
			window.clearInterval(closeId);
			self.loadingAll = false;
			brokenArrows();
			togglePrevNextLinks();
			hideSpinner();
			self.timer.stop();
		};		
		
		$('#' + cp.id + ' .jstree-open').each(function(i, ob){				
			self.treeCmp.jstree('close_node', $(ob).attr('id'));
		});
		
		closeId = setInterval(function(){
			if($('#' + cp.id + ' .jstree-open').length == 0){
				if(recurseTest()){
					if( visible[0].id == cp.id ){
						
						self.treeCmp.jstree('close_node', cp.id);
						
						setTimeout(function(){
							window.clearInterval(closeId);
							collapseAll();
						}, 500);
					}
					else{
						doScrollTo('#' + cp.id, function(){
							window.clearInterval(closeId);
							collapseAll();							
						});
					}
				}
				else{
					setTimeout(function(){
						afterLoad();			
					}, 500);
				}
			}
		}, 1000);
	};
	
	
	// publicly exposed functions
	
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
			showSpinner();
			self.timer.start();
			collapseAll();
		},
		getLocked : function(){
			return self.locked;
		},
		getIsLoading : function(){
			return self.isLoading;
		},
		setLocked : function(val){
			self.locked = val;
		},
		getCommonParent : function(node1, node2){
			return self.getCommonParent(node1, node2);
		},
		brokenArrows : function(){
			brokenArrows();
		},
		setDefaultDelay : function(delayIn){
			defaultDelay = delayIn;
		},
		startTimer : function(delayIn){
			showSpinner();
			self.timer.start();
		},
		stopTimer : function(delayIn){
			hideSpinner();
			self.timer.stop();
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


