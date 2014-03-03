var defaultChunk = 6;
var tree = $('#hierarchy');

var rowsToShow = function(node) {

	var count = 1;

	if (node.is_open) {
		$.each(node.children, function(i, ob) {
			count += rowsToShow(ob);
		});
	}
	return count;

};

$(document).ready(function() {

	var jstActiveNode;		// the last selected node	
	var newestNode = false;
	var createdNodes = [];

	var locked = true;
	var spin = false;


	// START LOAD FUNCTIONS

	// this simulates what will be an ajax call

	var loadData = function(url, callback) {
		var data = eval(url);
		setTimeout(function(){
			callback(data);			
		}, 100);
	};

	var loadAndAppendData = function(node, callback) {

		// get url

		var start = node.data.loaded ? node.data.loaded : 0;
		var end = start + Math.min(node.data.total - start, defaultChunk);
		var url = node.data.url + '.slice(' + start + ',' + end + ')';

		// load and append

		loadData(url, function(newData) {

			createdNodes = []; // reset global
			$.each(newData, function(i, ob) {
				tree.jstree("create_node", node, ob, "last", function(){}, true);
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
				var child = tree.jstree('get_node', ob);
				if (child.data ? (child.data.loaded == 0 || typeof child.data.loaded == 'undefined') : false) {
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
			callback();
		}
	};

	// END LOAD FUNCTIONS

	// START DOM HELPER FUNCTIONS
	var getRootEl = function() {
		return tree.find("li[id=root] a");
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

				var cNode = tree.jstree('get_node', ob);
				
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

		countNodes(tree.jstree('get_node', 'root'));
		return [ loadPointPosition, nodePosition ];
	};

	/*
	 * Simple next sibling finder - TODO - find native way
	 */
	var getNext = function(node) {

		if (!node) {
			console.log('getNext requires @node');
			return;
		}

		var nodeId = node.id;
		var parent = node.parent;
		var result = null;
		var nodeIndex = -1;

		parent = tree.jstree('get_node', parent);

		if(parent) {			
			$.each(parent.children, function(i, ob) { // find this node's index
				if (ob == nodeId) {
					nodeIndex = i;
					return false;
				}
			});

			if (parent.children.length > (nodeIndex + 1)) {
				result = tree.jstree('get_node', parent.children[nodeIndex + 1]);
			}
		}
		return result;
	}

	// END DOM HELPER FUNCTIONS

	// loads up the hierarchy - next or parent.
	// child loads not deducted from @leftToLoad
	var viewNextRecurse = function(node, leftToLoad, callbackComplete) {

		// inner function - logic for recursion
		var recurse = function() {
			// strategy - get next sibling nodes that are open and load their content.
			// if there's still data to load after that we recurse from the parent node
			// console.log('vnr: recurse - remaining = ' + leftToLoad );

			var next = getNext(node);

			if (next) {
				viewNextRecurse(next, leftToLoad, callbackComplete);
			}
			else {
				var parent = tree.jstree('get_node',	node.parent);
				if (typeof parent == 'object') {
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

	var viewNext = function() {

		if (jstActiveNode) {
			newestNode = null;
			viewNextRecurse(jstActiveNode, defaultChunk);

			if (newestNode) {
				doScrollTo($('#' + newestNode)[0]);
			} else {
				console.log('no newestNode (so no scroll)');
			}
		}
	};

	// UI FUNCTIONS

	var showSpinner = function(){
		if(!$('.hierarchy-container').find('.ajax-overlay').length){
			$('.hierarchy-container').append('<div class="ajax-overlay">');
			$('.hierarchy-container').find('.ajax-overlay').css('top', $('.hierarchy-container').scrollTop() + 'px');			
		}
	};
	
	var hideSpinner = function(){
		$('.hierarchy-container').find('.ajax-overlay').remove();
	};

	var doScrollTo = function(el, duration) {
		duration = 1000;
		if (typeof el == 'undefined') {
			console.log('doScrollTo error - undefined @el');
			return;
		}

		$('.hierarchy-container').css('overflow', 'auto');
		$('.hierarchy-container').scrollTo(el, duration, {
			"axis" : "y"
		});
		if (locked) {
			$('.hierarchy-container').css('overflow', 'hidden');
		}
	};

	// UI BINDING

	$('.load-more').click(loadMore);
	$('.view-next').click(viewNext);
	$('.hierarchy-container').focus(function() {
		getRootEl().focus();
	});
	$('.lock').click(function() {
		locked = !locked;
		if (locked) {
			$(this).html('[unlock]');
			$('.hierarchy-container').css('overflow', 'hidden');
		} else {
			$(this).html('[lock]');
			$('.hierarchy-container').css('overflow', 'auto');
		}
	});
	
	$('.prepend').click(function() {

		createdNodes = []; // reset global
		
		var newData = [
	        {
	        	"text" : "New Root???",
	        	"id" : "new_root",
	        	"name" : "root"
	        	/*	
				, "data": {
					"url":	"dataGen.books()",
					"total": 14
				}
				*/			        	
	        }
		];
		$.each(newData, function(i, ob) {
	//      $.jstree.reference("#myjstreediv").create_node(parent_node, {attr : {id: "g3", parent : "#" }, data: "My New Group" }, "first",false,true);
			tree.jstree("create_node", '#', ob, "first", function(){ alert('new node callback'); }, true);
		});
		var created = createdNodes;

		
	});
	
	
	$('.spin').click(function() {
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


	// START TREE BINDING

	// CREATE

	tree.bind('create_node.jstree', function(e, data) {
		if (!newestNode) {
			newestNode = data.node.id;
			console.log(' - SET NEWEST NODE (id) = ' +  data.node.id);
		}
		if (createdNodes) {
			createdNodes.push(data.node.id);
		}
	});

	// SELECT (invoke by loaded callback below)

	tree.bind("select_node.jstree", function(event, data) {
		jstActiveNode = data.node;

		doScrollTo($('#' + jstActiveNode.id));

		return data.node;
	});

	// LOADED
	tree.bind("loaded.jstree", function(event, data) {
		// set active and load
		getRootEl().click();
		setTimeout(function() {
			loadMore();
		}, 1);
	});

	// ARROW KEYS

	tree.bind('keydown.jstree', function(e) {
		if (e.key == 'Down') {
			var hoveredNode = tree.jstree('get_node', tree.find('.jstree-hovered').parent());
			var positions = getPILOT(hoveredNode);

			//console.log('pilot poistions = ' + JSON.stringify(positions) );
			
			if (positions[0] > positions[1]   ||  positions[1] - positions[0] > (defaultChunk / 2)) {
				console.log('load next from hovered');
				showSpinner();
				viewNextRecurse(hoveredNode, defaultChunk, function(msg){

					// make this the marker node
					console.log('Done Down load' + (typeof msg == 'undefined' ? '' : ': msg = ' + msg) );

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

	tree.on("open_node.jstree", function(e, data) {
		showSpinner();
		loadGrandChildren(data.node, function(){
			hideSpinner();			
		});
	});

	// CLOSE

	tree.bind("close_node.jstree", function(event, data) {
		console.log('closed ' + data.node)
	});


	// END TREE BINDING

});

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
 * BEHAVIOUR:
 *  - parent tree
 *  - previous button
 * 
 * THEME:
 * 
 *  - Loading zoomed, scrolling and reseting zoom to zero loses position (or loses entire tree)
 *  - DONE remove unwanted responsive class 
 *  
 *  
 *  
 *  
 *  
 */


