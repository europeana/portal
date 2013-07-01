/**
 * @author: Andy MacLean
 * 
 * To use:
 * 
 * $(document).ready(function(){
 *   searchWidget.load();
 * })
 * 
 * 
 * Script dependencies:
 * 
 * jquery                   (TODO: detect if available / load if available)
 * js/js/utils.js           (TODO: more the directory to compile to all file)
 * search-widget-all.min.js (derived from minify)
 * 
 * */

var EuWidgetWizard = function(cmpIn, options){
	
	var self          = this;
	self.cmp          = cmpIn;
	self.options      = options;
	self.tabs         = false;
	self.disabledTabs = [];//[1, 2, 3, 4];

	/* Flash a red border on the inputs that have to be filled before the next tab can open */
	var disabledClick = function(){
		var active = $('#' + self.tabs.getOpenTabId());
		$.each(active.find('input.mandatory').add(active.find('select.mandatory')) , function(){
			var input = $(this); 
			if(!input.val()){
				input.addClass('error-border');
				setTimeout(function(){
					input.removeClass('error-border');
				}, 1500);
			}
		});
	};
	
	/* Disable tabs that have unfilled inputs */
	var setDisabledTabs = function(){
		var disabled = [];
		var allTabs  = self.tabs.getTabs();
		var noTabs   = allTabs.length;
		
		$.each(allTabs, function(i, tab){
		
			var content = tab.getTabContent();
			var inputs  = content.find('input.mandatory').add(content.find('select.mandatory'));
			
			inputs.each(function(j, field){
				if($(field).val().length == 0 ){
					if(i+1 <= noTabs){
						disabled.push(i+1);		
						console.log("disable tab " + (i+1))
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
		
		// tab 3 search menu
		new EuMenu( 
			$("#search-menu-widget"),
			{
				"fn_item": function(self){
				},

				"fn_init": function(self){
					/*
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
					*/
				},
				
				"fn_submit":function(self){
					/*
					var active	= self.cmp.find(".item.active a").attr("class");
					var input	= $('#query-input');
					input.val( (typeof active == "undefined" ? "" : active) + input.val());
					*/
				}
			
			}
		).init();
			
		
	};
	
	
	var init = function(){
		
		self.tabs = new AccordionTabs(
				self.cmp,
				function(){
				},
				false,
				disabledClick
		);
	
		setDisabledTabs();
		
		self.cmp.find('input:not(.next)').val('');
		self.cmp.find('select').val('');
		
		
		self.cmp.find('input.mandatory').keyup(function(e){
			setDisabledTabs();
		});
		self.cmp.find('select').change(function(e){
			setDisabledTabs();
		});

		self.cmp.find('input.next').addClass('disabled');
		
		$.each(self.cmp.find('input.next'), function(i, btn){
			$(btn).click(function(){
				if( $(this).hasClass('disabled')){
					disabledClick();
					return;
				}
				self.tabs.openTabAtIndex(i+1);
			});
		});
		
		
		eu_europeana_providers = {
			
			addLinks : function(){
				// remove old
				$('.choices').html('');
				
				$('.data-providers').find('input').each(function(i, ob){
					ob = $(ob);
					if(ob.prop('checked')){
						var removeLink = $('<div class="removeLink icon-remove">').appendTo('.choices');
						var text = ob.next('span').html();
						
						removeLink.attr("title", text);
						removeLink.html('<span>&nbsp;' + text + '</span>');
						removeLink.click(function(){
							ob.prop('checked', false);
							removeLink.remove();
						});
					}
				});
			},
			
			init: function(){
				
				// checkboxes and collapsibility
				
				var change = function(e){
					var cb       = e.target ? $(e.target) : $(e);
					var checked  = cb.prop('checked');
					var subBoxes = cb.parent().next('ul').find('li a input');
					$.each(subBoxes, function(i, ob){
						$(ob).prop('checked', checked);
					});
					eu_europeana_providers.addLinks();
				};
				
				$('.icon-arrow-2-after>input').add('.data-providers input').change(change).click(function(e){
					e.stopPropagation();
				}).prop('checked', false);
				
				$('.icon-arrow-2-after').click(function(e){
					var innerList = $(this).parent().find('ul');
					innerList.is(':visible') ? innerList.hide('slow') : innerList.show('slow');
					e.preventDefault();
				});
			  
				$('.data-providers a').click(function(){
					var cb = $(this).find('input'); 
					cb.prop('checked', !cb.prop('checked'));
					change(cb);
				});
				
				// filter
				
			   	$('#content-provider-filter-filter').keyup( function(e){
			    	var val =  $(this).val().toUpperCase();
			    	if(val.length > 0){
			    		var re = new RegExp('^' + val + '[A-Za-z\\d\\s]*');
			            $('#wizard-tabs .outer-list .icon-arrow-2-after span').add('#wizard-tabs .no-children').each(function(i, ob){
			            	var text = $(ob).html().toUpperCase();
			            	var item = $(ob).closest('li');
			            	
			            	if(re.test(text)){
			            		console.log("match: " + text );
			            		item.show();
			            	}
			            	else{
			            		item.hide();
			            	}
			            });    		
			    	}
			    	else{
			            $('.icon-arrow-2-after span').add('.no-children').closest('li').show();    		
			    	}

			    } );
				
				$('.outer-list').show();
			}
		};
		eu_europeana_providers.init();
		
		//new EuMultiSelect( $('[name=content-provider]') ).init();
	};
	
	
	/* exposed functionality */
	
	return {
		"init" : function(){
			$(document).ready(function(){
				init();
			});
		}
	};
};
