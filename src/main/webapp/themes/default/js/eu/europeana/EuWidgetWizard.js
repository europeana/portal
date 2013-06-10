
var EuWidgetWizard = function(cmpIn, options){
	
	var self          = this;
	self.cmp          = cmpIn;
	self.options      = options;
	self.tabs         = false;
	self.disabledTabs = [1, 2, 3, 4];
		
	var fnSwitch = function(index, id, hash){
		//alert('switch: index = ' + index + ", id = " + id + ", hash = " + hash);
	}
	
	var setDisabledTabs = function(){
		var disabled = [];
		
		$.each(self.tabs.getTabs(), function(i, ob){

			var inputs  = ob.getTabContent().find('input');
			var selects = ob.getTabContent().find('select');
	
			inputs.each(function(j, field){
				if($(field).val().length == 0 ){
					disabled.push(i+1);
					//console.log('found empty field - disable tab ' + (i+1) );
				}
			});
			
			selects.each(function(j, field){
				if($(field).val().length == 0 ){
					disabled.push(i+1);
					//console.log('found empty select - disable tab ' + (i+1) );
				}
			});
		});
		
		//console.log('disable the following: ' + JSON.stringify(disabled));
		
		self.disabledTabs = disabled;
		self.tabs.setDisabledTabs(self.disabledTabs); 
	}
	
	var init = function(){
		self.tabs = new AccordionTabs(self.cmp, fnSwitch);
		self.tabs.setDisabledTabs(self.disabledTabs);
		
		self.cmp.find('input').val('');
		self.cmp.find('select').val('');
		
		self.cmp.find('input').keyup(function(e){
			setDisabledTabs();
		});
		self.cmp.find('select').change(function(e){
			setDisabledTabs();
		});

	};
	
	
	/* exposed functionality */
	
	return {
		"init" : function(){
			init();
		}
	};
};
