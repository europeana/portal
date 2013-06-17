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

	searchUrl : 'http://test.portal2.eanadev.org/api/v2/search.json?wskey=api2demo',
	q : false,
	container : false,

	// get markup from portal
	load : function(){
		$.ajax({
			"url" : "http://localhost:8081/portal/template.html?id=search",
			"type" : "GET",
			"crossDomain" : true,
			"dataType" : "script"
		});
	},

	// load style / initialise events / set state - called by load callback
	init : function(htmlData) {
		this.container = $('.search-widget-container');
		this.container.append(htmlData.markup);

		// work out percentage width window/container
		/*
		var wBody = $('body').width();
		var wThis = this.container.width();
		var pct = wThis / wBody;
		alert("pct = " + pct);
		this.container.css('zoom', '2');
		this.container.css('-moz-transform', 'scale(2)');
		*/
		
//		file:///home/maclean/workspace/europeana/new-search-widget.html
			
		
		this.setupQuery();
		this.setupMenus();
		this.setupEllipsis();
		this.setupPageJump();

		// load style
		$('head').append('<link rel="stylesheet" href="http://localhost:8081/portal/themes/default/css/min/search-widget-all.css" type="text/css" />');

		// use the widget
		$('#search').click(function(){
			var url = buildUrl();
			if(url){
				$.ajax({
					"url" : url,
					"type" : "GET",
					"crossDomain" : true,
					"dataType" : "script"
				});
			}
			else{
				this.q.addClass('error-border');
			}
		});
	},

	buildUrl : function() {
		console.log("buildUrl");
		var term = this.q.val();
		if(!term){
			return '';
		}
		
		var url = searchWidget.searchUrl + '&query=' + term;

		// "&qf=PROVIDER%3A%22Judaica+Europeana%22&qf=DATA_PROVIDER%3AAkadem&start=49&rows=12&profile=portal&callback=searchWidget.showRes";
		/*
		 &qf=PROVIDER%3A%22Judaica+Europeana%22
		 &qf=DATA_PROVIDER%3AAkadem
		 &start=49
		 &rows=12
		 &profile=portal
		 &callback=searchWidget.showRes";
		*/
	},

	showRes: function(data){
		$(data.facets).each(function(i, ob){
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

	setupQuery : function(){
		this.q = $('#query-input');
		
		this.q.blur(function(){
			$(this).removeClass('glow');
		}).focus(function(){
			$(this).addClass('glow');	
		});
		
		//return;
		var submitCell = this.container.find('.submit-cell');
		var submitCellButton = submitCell.find('button');
		var menuCell = this.container.find('.menu-cell');
		var searchMenu = this.container.find('#search-menu');

		//alert(searchMenu.width());

		
		submitCell.css("width", submitCellButton.outerWidth(true) + "px"); 
		menuCell.css("width", searchMenu.width() + "px");//searchMenu.outerWidth(false) + "px");
		submitCellButton.css("border-left",	"solid 1px #4C7ECF");	// do this after the resize to stop 1px gap in FF
		
		

		//alert(searchMenu.width());

	},
	
	// todo 
	setupMenus : function(){
		
		// search 
		this.searchMenu = new EuMenu( 
			$("#search-menu"),
			{
				"fn_item": function(self){},
				"fn_init": function(self){
					var input		= $('#query-input');
					var searchTerm	= input.attr("valueForBackButton").replace("*:*", "");
					self.cmp.find(".item a").each(function(i, ob){
						var searchType = $(ob).attr("class");
						if(searchTerm.indexOf(searchType) == 0){
						 	self.setLabel(searchType);
							input.val( searchTerm.substr( searchType.length, searchTerm.length) );
							self.setActive(searchType);
						}
					});
				},
				"fn_submit":function(self){
					var active	= self.cmp.find(".item.active a").attr("class");
					var input	= $('#query-input');
					input.val( (typeof active == "undefined" ? "" : active) + input.val());
				}
			}
		);
		this.searchMenu.init();
			
		// result size 
		var config = {
			"fn_init": function(self){
				self.setActive( $("#query-search input[name=rows]").val() );
			},
			"fn_item":function(self, selected){
				window.location.href = eu.europeana.search.urlAlterParam("rows", selected);
			}
		};
		
		new EuMenu( $(".nav-top .eu-menu"), config).init();
		new EuMenu( $(".nav-bottom .eu-menu"), config).init();
		
		// menu closing
		
		$(this.container).click( function(){
			$('.eu-menu' ).removeClass("active");
		});

	}
	
};

$(document).ready(function(){		
	searchWidget.load();
});


