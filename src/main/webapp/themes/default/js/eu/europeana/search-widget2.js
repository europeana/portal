/* Andy MacLean
 * 
 * This file gets loaded by including search-widget-all.js on a remote machine.
 * 
 * It loads the markup and styling and wires up the controls (todo)
 * 
 * */

// todo: check for jquery and if not present append to the head 
//  this needs to be loaded remotely before before mega file


searchWidget = {

	// get markup from portal
		
	load : function(){
		$.ajax({
			"url" : "http://localhost:8081/portal/template.html?id=search",
			"type" : "GET",
			"crossDomain" : true,
			"dataType" : "script"
		});
	},

	// load style / initialise events / set state

	init : function(htmlData) {
		$('.search-widget-container').append(htmlData.markup);

		this.setupResultSizeMenu();
		this.setupEllipsis();
		this.setupPageJump();

		// load style
		$('head').append('<link rel="stylesheet" href="http://localhost:8081/portal/themes/default/css/min/search-widget-all.css" type="text/css" />');
		
		
		// use the widget
		
		var url = "http://test.portal2.eanadev.org/api/v2/search.json?wskey=api2demo&query=paris&qf=PROVIDER%3A%22Judaica+Europeana%22&qf=DATA_PROVIDER%3AAkadem&start=49&rows=12&profile=portal&callback=searchWidget.showRes";

		$('#search').click(function(){
			$.ajax({
				"url" : url,
				"type" : "GET",
				"crossDomain" : true,
				"dataType" : "script"
			});
		});
	},

	
	showRes:function(data){
	
		$(data.facets).each(function(i, ob){
			alert(i);
			$('body').append(JSON.stringify(ob));
			$('body').append('<br>');
		});
	},
	
	
	
	// todo
	setupPageJump : function(){
		//$('.jump-to-page').bind('submit', 				this.jumpToPageSubmit );
		//$('.jump-to-page #start-page').bind('keypress',	this.validateJumpToPage);
	},
	
	// todo
	jumpToPageSubmit : function( e ){
		//var $jumpToPage	= $(this).parent();
		//var rows		= parseInt($jumpToPage.find("input[name=rows]").val());
		//var pageNum		= parseInt($jumpToPage.find("#start-page").val());
		//var newStart	= 1 + ((pageNum-1)*rows);
		//window.location.href = eu.europeana.search.urlAlterParam("start", newStart);
		//return false; // stop submit
	},
	
	// todo / to check
	validateJumpToPage : function(e){
		if(e.ctrlKey || e.metaKey || e.keyCode == 9){
			// ctrl or cmd or tab
			return;
		}
		
		var $this		= $(this);
		var $jumpToPage	= $(this).parent();
		var key			= window.event ? e.keyCode : e.which;
		var maxRows		= parseInt($jumpToPage.find("#max-rows").val());

		if([8, 46, 37, 38, 39, 40].indexOf(e.keyCode)>-1){
			/* delete, backspace, left, up, right, down */
			return true;
		}
		else if(e.keyCode == 13){
			/* return */
			var currVal = $jumpToPage.find("#start-page").val();
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
		
	},

	//  todo / to check
	setupEllipsis : function(){
		// add ellipsis
		var ellipsisObjects = [];
		$('.ellipsis').each(
				function(i, ob){
					var fixed	= $(ob).find('.fixed');
					var html	= fixed.html();
					fixed.remove();
					ellipsisObjects[ellipsisObjects.length] = new Ellipsis(
								$(ob),
								{fixed:	'<span class="fixed">' + html + '</span>'},
								function($ob){
									var imgThumb = $(ob).parent().prev();
									imgThumb.css('border-style', 'solid solid none');
									imgThumb.css('border-width', '1px 1px medium');
									$ob.css('visibility', 'visible');
								}
							);					
				}
		);
		$(window).euRsz(function(){
			for(var i=0; i<ellipsisObjects.length; i++ ){
				ellipsisObjects[i].respond();
			}
		});

	},

	// todo 
	setupResultSizeMenu : function(){
		var config = {
			"fn_init": function(self){
				self.setActive( $("#query-search input[name=rows]").val() );
			},
			"fn_item":function(self, selected){
				window.location.href = eu.europeana.search.urlAlterParam("rows", selected);
			}
		};
		
		var menuTop = new EuMenu( $(".nav-top .eu-menu"), config);
		var menuBottom = new EuMenu( $(".nav-bottom .eu-menu"), config);
		menuTop.init();
		menuBottom.init();
	},
	
	// todo
	checkKeywordSupplied : function(){
		if($('#newKeyword').val().length>0){
			return true;
		}
		else{
			$('#newKeyword').addClass('error-border');
			return false;
		}	
	}
	
	
};

$(document).ready(function(){		
	searchWidget.load();
});


