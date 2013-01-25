
var EuAccessibility = function(cmpIn, fnGetItems){

	var self		= this;
	self.cmp		= cmpIn;
	self.fnGetItems = fnGetItems;
	
	self.keyPress = function(e){
	
		if(e.ctrlKey || e.metaKey){
			// ctrl or cmd
			return;
		}
			
		var tabIndex = parseInt($(e.target).attr('tabIndex'));
	
		if([39, 40].indexOf(e.keyCode)>-1){
			// left, up 
			
			tabIndex += 1;
		}
		else if([37, 38].indexOf(e.keyCode)>-1){
			// right, down
			
			if(e.target == self.cmp[0]){
				self.cmp.removeClass("active");
				e.preventDefault();
				return;
			}
			else{
				tabIndex -= 1;
				
			}
		}
		else if(e.keyCode == 13){
			// return
			
			e.preventDefault();
			e.target.click();
			self.cmp.focus();
			
			return;			
		}
		else if(e.keyCode == 9){
			// tab
			
			/* jump to next / prev component and close the menu */
			var items = self.fnGetItems();
			var targetTabIndex = e.shiftKey ? parseInt($(':focus').attr("tabIndex")) - 1 : parseInt( items.last().attr("tabIndex")  ) + 1;
			var target = $('*[tabIndex=' + targetTabIndex + ']');

			// tabbing backwards over closed collapsible sections means creates an invisible focus. 
			// This works (i.e. finds the next visible predecessor) but kills performance on large facets (year / language of description).....
			/*
			while( !target.is(':visible') && targetTabIndex > 0 ){
				targetTabIndex --;
				target = $('*[tabIndex=' + targetTabIndex + ']');
			}
			*/
			// ..so we do a smart search by looking at the parent instead
			
			if(e.shiftKey && !target.is(':visible') ){
				var targetLi = target;
				while(  targetLi[0].nodeName.toUpperCase() != 'LI'){
					targetLi = targetLi.parent();
				}
				while(targetLi.prev().length>0){
					targetLi = targetLi.prev();
				}
				targetTabIndex = targetLi.find('*[tabIndex]').first().attr('tabIndex') ;
				targetTabIndex = parseInt(targetTabIndex) -1;
				target = $('*[tabIndex=' + targetTabIndex + ']');
			}
			
			if(target[0]){
				target.focus();
			}
			
			self.cmp.removeClass("active");
			e.preventDefault();
			return;
		}
		else{
			var key	= window.event ? e.keyCode : e.which;
			
			if(key==27){
				
				/* esc */
				
				self.cmp.removeClass("active");
				self.cmp.focus();
				return;
				
			}
			if ( key < 48 || key > 57 ) {
				
				/* alphabet */
				
				var val		= String.fromCharCode(key).toUpperCase();
				var items	= self.fnGetItems();
	

				var allWithName = items.filter(function(){
					var result = false;
					if($(this).is(':visible')){
						
						// normalise (<label>&nbsp; text</label>) and just plain (text)
						var compare = $(this).find("*").html();
						if(typeof compare == 'undefined'){
							compare = $(this).html();
						}
						
						compare = compare.replace(/&nbsp;/g, '').trim();
						if( (compare.charAt(0) + '').toUpperCase() == val){
							result = true;
						}
					}
					return result;
				});
				
				var nextWithName = allWithName.filter(function() {
					var thisTabIndex = parseInt($(this).attr("tabIndex"));
				    return thisTabIndex > tabIndex;
				});
				
				if(nextWithName[0]){
					nextWithName[0].focus();
				}
				else{
					var prevWithName = allWithName.filter(function() {
					    return parseInt($(this).attr("tabIndex")) < tabIndex;
					});
					if(prevWithName[0]){
						prevWithName[0].focus();
					}
				}
				if(! $(e.target).is(':focus') ){
					return;
				}
			}
		}
				
		if(!self.cmp.find('ul').is(':visible') ){
			self.cmp.find('ul').click();
		}
		var target = $('*[tabIndex=' + tabIndex + ']');
			
		if(target[0]){
			target.focus();
		}				
		
		e.preventDefault();			
		
	};
	
	
	return {
		"keyPress" : function(e){
			self.keyPress(e);
		},
		"fnGetItems" : function(){
			return self.fnGetItems ? self.fnGetItems() : null;
		}
	};
	
};


