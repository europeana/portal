
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
		var allTabs  = self.tabs.getTabs();
		var noTabs   = allTabs.length;
		
		$.each(allTabs, function(i, ob){

			var inputs  = ob.getTabContent().find('input:not(.next)');
			var selects = ob.getTabContent().find('select');
	
			inputs.each(function(j, field){
				if($(field).val().length == 0 ){
					if(i+1 <= noTabs){
						disabled.push(i+1);						
					}
				}
			});
			
			selects.each(function(j, field){
				if($(field).val().length == 0 ){
					if(i+1 <= noTabs){
						disabled.push(i+1);
					}
				}
			});
		});
		
		self.disabledTabs = disabled;
		self.tabs.setDisabledTabs(self.disabledTabs); 
		
		// progress bar
		var progress =  ((noTabs - disabled.length+1) / noTabs) * 100;
		$('.progress .bar').css('width', progress + '%'); 
		
		// next buttons
		self.cmp.find('input.next').removeClass('disabled');
		
		$.each(disabled, function(i, ob){
			$(  self.cmp.find('input.next')[ob-1]  ).addClass('disabled');
		});
		
	}
	
	var init = function(){
		self.tabs = new AccordionTabs(self.cmp, fnSwitch);
		self.tabs.setDisabledTabs(self.disabledTabs);
		
		self.cmp.find('input:not(.next)').val('');
		self.cmp.find('select').val('');
		
		self.cmp.find('input').keyup(function(e){
			setDisabledTabs();
		});
		self.cmp.find('select').change(function(e){
			setDisabledTabs();
		});

		self.cmp.find('input.next').addClass('disabled');
		
		$.each(self.cmp.find('input.next'), function(i, btn){
			$(btn).click(function(){
				if( $(this).hasClass('disabled')){
					return;
				}
				self.tabs.openTabAtIndex(i+1)
			});
		});
	};
	
	
	/* exposed functionality */
	
	return {
		"init" : function(){
			init();
		}
	};
};
