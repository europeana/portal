(function ($) {

	'use strict';

	var AccordionTab = function (elIn, parentIn, indexIn) {
		var self	= this;
		self.el		= elIn;
		self.a		= self.el.find("a");
		self.index	= indexIn;
		self.parent = parentIn;

		var open = function () {
			self.parent.el.find('.is-open').removeClass('is-open');
			self.parent.el.find('.active').removeClass('active');
			self.a.next().addClass('is-open');
			self.el.addClass('active');	
			
			var contentToClone = self.a.next().find(">:first-child");
			if(contentToClone.length>0){
				var nextId =  contentToClone.attr("id");
				self.parent.el.find('.tab_content').html(contentToClone[0].outerHTML);
				self.parent.el.find('.tab_content').find("#" + nextId).attr("id", nextId + "-tabbed");
				self.parent.el.find('.tab_content').find('.is-open').removeClass('is-open');
			}
			else{
				self.parent.el.find('.tab_content').html(self.a.next().html());
			}
			self.parent.selectionMade(self.index, self.el.attr('id'), self.a.attr('href') );
		};

		self.a.on('click', function (e) {
			e.preventDefault();
			open();
		});

		return {
			openTab : function () {
				open();
			}
		};
	};
	
	window.AccordionTabs = function(elIn, callbackIn, hash){
		var self		= this;
		
		//var firstTab	= null;
		
		var allTabs		= [];
		
		self.el			= elIn;
		self.activeId	= '';
		self.activeHash	= '';
		self.callback	= callbackIn;
		
		self.el.addClass('accordion-tabs');
		self.selectionMade = function(index, id, hash){
			self.activeId	= id;
			self.activeHash	= hash;
			if(self.callback){
				self.callback(index, id, hash);
			}
		};

		self.el.find('.section').each(function (i, ob) {
			allTabs[allTabs.length] = new AccordionTab($(ob), self, i);
		});
		
		self.tabContent = $('<div class="tab_content">').appendTo(self.el);
		
		self.openTab = function(hash){
			if(hash != self.activeHash){
				self.el.find('.section').each(function (i, ob) {
					ob = $(ob);
					if( ob.children('a').attr('href') == hash ){
						allTabs[i].openTab();
						return;
					}
				});					
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
				self.openTab(hash);
			}
		};
	};
	
}(jQuery));
