var EuPagination = function(cmpIn, options){
	
	// conf
	var self		= this;
	self.cmp		= cmpIn;
	self.options	= options ? options : {};
	self.ajax		= self.options.ajax ? self.options.ajax : false;

	// dom 
	self.first		= null;
	self.previous	= null;
	self.next		= null;
	self.last		= null;
	self.form		= null;
	self.jump		= null;
	
	// data
	self.records	= 0;
	self.rows		= 0;
	self.start		= 0;
	
	self.init = function(){
		self.first		= self.cmp.find('.nav-first');
		self.previous	= self.cmp.find('.nav-prev');
		self.next		= self.cmp.find('.nav-next');
		self.last		= self.cmp.find('.nav-last');
		self.form		= self.cmp.find('form');
		self.jump		= self.cmp.find('#start-page');
		
		self.form.bind('submit',   jumpToPageSubmit);
		self.jump.bind('keypress', validateJumpToPage);
		
		if(self.options.data){
			self.setData(self.options.data.records, self.options.data.rows, self.options.data.start)
		}
	};

	self.setData = function(records, rows, start){
		
		console.log("set data: records = " + records + ", rows = " + rows + ", start = " + start );
		
		self.records	= records;
		self.rows		= rows;
		self.start		= start;
		
		if(self.ajax){	// set links
			self.first   .find('a').href = "&start=1";
			self.previous.find('a').href = "&start=" + (start - rows);
			self.next    .find('a').href = "&start=" + ((start ? start : 1) + rows);
			self.last    .find('a').href = "&start=" + (records + 1 - (records % rows)  );			
		}
		else{ // populate form
		}
		
		// TODO links show / hide 
	};
	
	var jumpToPageSubmit = function(e){
		if(self.ajax){
			
		}
		else{
			var pageNum		= parseInt(self.jump.val());
			var newStart	= 1 + ((pageNum-1) * self.rows);
			self.cmp.find('#start').val(newStart);			
		}
	};
	
	var validateJumpToPage = function(e){
		
		if(e.ctrlKey || e.metaKey || e.keyCode == 9){
			// ctrl or cmd or tab
			return;
		}
		
		var $this		= $(this);
		var $jumpToPage	= $(this).parent();
		var key			= window.event ? e.keyCode : e.which;
		var maxRows		= parseInt(self.records / self.rows) + (self.records % self.rows ? 1 : 0);
		
		console.log("maxRows = " + maxRows);
		
		
		if([8, 46, 37, 38, 39, 40].indexOf(e.keyCode)>-1){
			/* delete, backspace, left, up, right, down */
			return true;
		}
		else if(e.keyCode == 13){
			/* return */
			var currVal = self.jump.val();
			return currVal.length > 0;
		}
		else if ( key < 48 || key > 57 ) {
			/* alphabet */
			return false;
		}
		else{
			/* number */
			
			var val = parseInt( $this.val() + String.fromCharCode(key) );
			
			if(typeof $this[0].selectionStart != 'undefined' && typeof $this[0].selectionEnd != 'undefined' && $this[0].selectionStart != $this[0].selectionEnd){
				val = parseInt(	$this.val().substr(0, $this[0].selectionStart -1)	+ String.fromCharCode(key) + $this.val().substr($this[0].selectionEnd, $this.val().length )	);	  
			}
			
			var overwrite;
			
			if(!val>0){
				overwrite = 1;
				val = 1;
			}
			else if(val > maxRows){
				overwrite = maxRows;
				val = maxRows;
			}
			if(overwrite){
				$(e.target).val(overwrite);
				e.preventDefault();
			}
			return true;
		}
	};
	

	self.init();

	/* exposed functionality */
	return {
		"setData":function(records, rows, start){
			self.setData()
		},
	};
};