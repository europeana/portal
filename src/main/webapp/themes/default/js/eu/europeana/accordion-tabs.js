(function ($) {

	'use strict';

	var AccordionTab = function (elIn, parentIn, indexIn) {
		var self	= this;
		self.el		= elIn;
		self.a		= self.el.find("a");
		self.index	= indexIn;
		self.parent = parentIn;

		var open = function () {
			if(!self.el.hasClass('active')){
				self.parent.el.find('.is-open').removeClass('is-open');
				self.a.next().toggleClass('is-open');
				self.parent.el.find('.active').removeClass('active');
				self.el.addClass('active');	
				
				var contentToClone = self.a.next().find(">:first-child");
				if(contentToClone.length>0){
					var nextId =  contentToClone.attr("id");
					self.parent.el.find('.tab_content').html(contentToClone[0].outerHTML);
					self.parent.el.find('.tab_content').find("#" + nextId).attr("id", nextId + "-tabbed");
				}
				else{
					self.parent.el.find('.tab_content').html(self.a.next().html());
				}
				self.parent.selectionMade(self.index, self.el.attr('id') );
			}
			else{
				self.parent.el.find('.is-open').removeClass('is-open');
				self.el.removeClass('active');
			}
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
	
	window.AccordionTabs = function(elIn, callbackIn){
		var self		= this;
		var firstTab	= null;
		self.el			= elIn;
		self.activeId	= '';
		self.callback	= callbackIn;
		
		self.el.addClass('accordion-tabs');
		self.selectionMade = function(index, id){
			self.activeId = id;
			if(self.callback){
				self.callback(index, id);
			}
		};

		self.el.find('.section').each(function (i, ob) {
			var tab = new AccordionTab($(ob), self, i);
			if(i==0){
				firstTab = tab;
			}
		});
		
		self.tabContent = $('<div class="tab_content">').appendTo(self.el);
		
		
		if (firstTab) {
			firstTab.openTab();
		}
		
		return {
			getOpenTabId : function () {
				return self.activeId;
			}
		};
	};
	
}(jQuery));
