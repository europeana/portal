
var EuWidgetWizard = function(cmpIn, options){
	
	var self          = this;
	self.cmp          = cmpIn;
	self.options      = options;
	self.tabs         = false;
	self.disabledTabs = [ 1, 2, 3, 4];
		
	var fnSwitch = function(index, id, hash){
		//alert('switch: index = ' + index + ", id = " + id + ", hash = " + hash);
	}
	
	
	var init = function(){
		self.tabs = new AccordionTabs(self.cmp, fnSwitch);
		self.tabs.setDisabledTabs(self.disabledTabs);
		
		var input = self.cmp.find('#step1input');
		
		input.keypress(function(e){
			self.disabledTabs = [2, 3, 4];
			self.tabs.setDisabledTabs(self.disabledTabs); 
		});

	};
	
	
	/* exposed functionality */
	
	return {
		"init" : function(){
			init();
		}
	};
};
