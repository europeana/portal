var EuMenu = function(cmpIn, options){
	
	var self		= this;
	self.cmp		= cmpIn;
	self.ops		= self.cmp.find(".item");
	self.label		= self.cmp.find(".menu-label").html();
	self.options	= options;
	self.val		= null;
	
	self.cmp.click(function(e){
		$('.eu-menu' ).removeClass("active");
		self.cmp.toggleClass("active");
		e.stopPropagation();
	});
	
	
	self.setLabel = function(val){
		self.cmp.find(".menu-label").html(  self.label + " " + val );
	};
	

	self.getActive = function(){
		/*
		 * var res = null;
		self.cmp.find(".item").each(function(i, ob){
			if($(ob).hasClass("active")){
				res = $(ob);						
			}
		});
		*/
		return self.val;//res;
	};
	
	self.setActive = function(val){
		self.cmp.removeClass("selected");
		self.cmp.find(".item a").each(function(i, ob){
			if($(ob).attr("class") == val){
				$(ob).parent().addClass("active");
				if(val){
					self.cmp.addClass("selected");
					self.val = val;
				}
				self.setLabel( $(ob).html() );
			}
			else{						
				$(ob).parent().removeClass("active");						
			}
		});
		self.cmp.removeClass("active");
	};

	
	self.cmp.find(".item a").click(
		function(e){
			var selected = $(this).attr("class");
			self.setActive(selected);
			e.stopPropagation();
			
			if(self.options.fn_item){
				self.options.fn_item(self, selected);
			}
			return false;
		}
	);
				
	return {
		"init" : function(){
			if(self.options.fn_init){
				self.options.fn_init(self);
			}
		},
		"submit":function(){
			if(self.options.fn_submit){
				self.options.fn_submit(self);
			}
		},
		"setActive":function(val, highlight){
			self.setActive(val);
		}
	};
};
