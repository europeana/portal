var AccordionTab = function (elIn, parentIn, indexIn) {
	var self          = this;
	self.el           = elIn;
	self.a            = self.el.find("a");
	self.index        = indexIn;
	self.parent       = parentIn;

	var open = function () {
		self.parent.el.find('.is-open').removeClass('is-open');
		self.parent.el.find('.active').removeClass('active');
		self.a.next().addClass('is-open');
		self.el.addClass('active');	

		self.parent.selectionMade(self.index, self.el.attr('id'), self.a.attr('href') );
	};

	self.a.on('click', function (e) {
		e.preventDefault();

		console.log("self.index " + self.index + ", self.disabledTabs " + self.parent.disabledTabs + " -> " + JSON.stringify(self.parent.disabledTabs) )
		if(self.parent.disabledTabs.length){
			if(self.parent.disabledTabs.indexOf(self.index)>-1){
				return;
			}
		}
		open();

	});

	return {
		openTab : function (){
			open();
		},
		getTabContent : function(){
			return self.el;
		}
	};
};
	

var AccordionTabs = function(elIn, callbackIn, hash){
	var self		= this;
	var allTabs		= [];
	
	self.disabledTabs = [];
	self.el			= elIn;
	self.activeId	= '';
	self.activeHash	= '';
	self.callback	= callbackIn;
		
	self.el.addClass('accordion-tabs');
	self.selectionMade = function(index, id, hash){
		self.activeId      = id;
		self.activeHash    = hash;
		if(self.callback){
			self.callback(index, id, hash);
		}
	};

	self.el.find('.section').each(function (i, ob) {
		allTabs[allTabs.length] = new AccordionTab($(ob), self, i);
	});
		
	self.tabContent = $('<div class="tab_content">').appendTo(self.el);
		
	self.openTab = function(hash){
			
		alert("open tab " + JSON.stringify(self.disabledTabs)  + " hash = " + hash + ", self.disabledTabs.indexOf(hash)" + self.disabledTabs.indexOf(hash));
			
		if(self.disabledTabs.indexOf(hash) == -1){
			if(hash != self.activeHash){
				self.el.find('.section').each(function (i, ob) {
					ob = $(ob);
					if( ob.children('a').attr('href') == hash ){
						allTabs[i].openTab();
						return;
					}
				});					
			}				
		}
	};
		
	if (allTabs.length>0) {
		if(hash){
			self.openTab(hash);
		}
		else{
			allTabs[0].openTab();				
		}
	}
		
	return {
		getOpenTabId : function () {
			return self.activeId;
		},
		openTab : function(hash){
			alert("exposed openTab");
			self.openTab(hash);
		},
		setDisabledTabs : function(disabledTabs){			
			self.disabledTabs = disabledTabs;
			self.el.find('.section').removeClass('disabled');
			$.each(disabledTabs, function(i, ob){
				var section = $(self.el.find('.section')[ob]);
				section.addClass('disabled');
			});

		},
		getTabs : function(){
			return allTabs;
		}
	};
};
