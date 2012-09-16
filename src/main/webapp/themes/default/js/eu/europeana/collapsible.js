(function( $ ) {
$.fn.Collapsible = function() {
	
	var ops	= {
			expandedClass		:	'icon-arrow-7',
			collapsedClass		:	'icon-arrow-6',
			beenOpened			:	false
	},
	opsIn	= arguments[0] || {};
	for (var attrname in opsIn){
		ops[attrname] = opsIn[attrname];
	}

	var log = function(x){
		console.log(x);
	};

	eu.europeana.vars.suppresResize = false;
	
	return this.each(function(){
        var $this 		= $(this);
        var $header 	= $this.find(ops.headingSelector);
        var $body		= $this.find(ops.bodySelector);
        var $icon		= ops.iconSelector		?  $this.find(ops.iconSelector)		: null;
        var $follower	= ops.followerSelector	?  $this.find(ops.followerSelector)	: null;

        var up = function(fast){
			$body.slideUp(fast);
			if($follower){
				$follower.slideUp(fast);
			}
        };
        
        var down = function(fast){
        	$body.slideDown(fast,
	        	function(){
	            	if(!ops.beenOpened && ops.fireFirstOpen){
	            		log("fire first open....");
	            		ops.beenOpened = true;
	            		ops.fireFirstOpen();
	            	}
	            	else{
	            		var eventElements = [$this[0]];
	            		if($follower){
	            			eventElements[1] = $follower[0]; 
	            		}
	            	   	$(window).trigger('collapsibleExpanded', [eventElements] );        		
	            	}
	        	}
        	);
        	if($follower){
        		$follower.slideDown(fast);
        	}
        };
        
        var getTarget = function(){
        	return $icon ? $icon : $header;	
        };
        
        /* called on setup (@set = false) and following a click (@set = true)  */
        var setClasses = function(set){
        	var $target = getTarget();
        	
        	if( $target.hasClass('active') ){
        		if(!set){        			
        			$target.addClass	(ops.expandedClass);
        			$target.removeClass	(ops.collapsedClass);
        			down('fast');
        		}
        		else{
        			$target.addClass	(ops.collapsedClass);
        			$target.removeClass	(ops.expandedClass);
        			$target.removeClass('active');
        			up();        			
        		}
        	}
        	else{
        		if(!set){        			
        			$target.addClass	(ops.collapsedClass);
        			$target.removeClass	(ops.expandedClass);
	    			up('fast');
        		}    			
        		else{        			
        			$target.addClass	(ops.expandedClass);
        			$target.removeClass	(ops.collapsedClass);
        			$target.addClass('active');
	    			down();
        		}    			
        	}
        };
        
        /* collapse on small size and show expand/collapse icons, show on big size and hide expand/collapse icons */
    	if(ops.toggleFn){
    		
    		var fnResize = function(){
    			if(eu.europeana.vars.suppresResize){
    				return;
    			}
    			var target = getTarget(); 
    			if(ops.toggleFn() == true){
    				target.removeClass('active');
    				target.show();   				
    			}
    			else{
    				target.addClass('active');
    				target.hide();
    			}
    			setClasses();
    		};
    		$(window).bind('resize', fnResize);
    		fnResize();
    	}
    	else{
    		setClasses();    		
    	}
        
        
    	$header.bind('click', function(e){
    		
    		//alert($(this)[0].nodeName);
    		
    		//awaitingOpen = true;
    		eu.europeana.vars.suppresResize = true;
    		var finishedOpen = function(){
    			eu.europeana.vars.suppresResize = false;    			
    		};
    		setTimeout(finishedOpen, 1200);
    		
    		//e.preventDefault();
    		if(getTarget().is(':visible')){    			
    			setClasses(true);
    		}
    			//return false;
		});
    });
};
}( jQuery ) );