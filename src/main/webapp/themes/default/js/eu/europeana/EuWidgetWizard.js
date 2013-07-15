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
 * 
 * */

var EuWidgetWizard = function(cmpIn, options){
	
	var self             = this;
	self.cmp             = cmpIn;
	self.options         = options;
	
	var PREVIEW_TAB_INDEX = 4;
	
	self.tabs            = false;
	self.disabledTabs    = [];
    self.initialisedTabs = {};
	
    
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
	
	var output = function(){
		var result = searchWidgetUrl;
		var param = function(){
			return (result.indexOf('?')>-1) ? '&' : '?';
		};
		
		var searchPrefix = '';
		if(self.searchMenu){
			searchPrefix = self.searchMenu.getActive();
		}
		
		if(self.initialisedTabs[1]){
			var query = $('.widget-configuration .default_query').val();
			if(query){
				result += param() + 'query=' + (searchPrefix ? searchPrefix : '') + query;			
			}
			
			$('ul.types a input').add('ul.copyrights a input').add('ul.languages a input').each(function(i, ob){
				if($(ob).prop('checked')){
					//var name = $(ob).next('span').html().replace(/ *\([^)]*\) */g, "");
					//var name = $(ob).attr('title').replace(/ *\([^)]*\) */g, "");
					var name = $(ob).attr('title').replace(/\"/g, "");
					result += name;
				}
			});

			
		}		
		if(self.initialisedTabs[2]){
			$('.data-providers>li>a>input').add('.providers>li>a>input').each(function(i, ob){
				if($(ob).prop('checked')){
					
					console.log("checked name is " + $(ob).next('span').html());
					
					var name = $(ob).next('span').html().replace(/ *\([^)]*\) */g, "").replace(/\s/g, '\+').replace(/\%20/g, '\+');
					
					result += param() + 'qf=' + ( $(ob).parent().hasClass('data-provider') ? 'DATA_PROVIDER' : 'PROVIDER') + ':{' + name + '}';
					//result += param() + 'qf=' + ( $(ob).parent().hasClass('data-provider') ? 'DATA_PROVIDER' : 'PROVIDER') + ':&quot;' + name + '&quot;';
					//result += param() + 'qf=' + ( $(ob).parent().hasClass('data-provider') ? 'DATA_PROVIDER' : 'PROVIDER') + ':' + name;

					
				}
			});			
		}

		if(self.initialisedTabs[3]){
		
			$('ul.portal-languages a input').each(function(i, ob){
				if($(ob).prop('checked')){
					var lang = $(ob).next('span').attr('class');
					result += param() + 'lang=' + lang;
				}
			});

		}

		if(self.initialisedTabs[4]){
			var resultBehaviour = $('#withResults').prop('checked');
			result += param() + 'withResults=' + withResults;
		}
		
		return result;
		
	};
	
	var selectElementContents = function(el) {
	    var range;
	    if (window.getSelection && document.createRange) {
	        range = document.createRange();
	        var sel = window.getSelection();
	        if(typeof sel != 'undefined' && sel != null){
		        range.selectNodeContents(el);
		        try{
		        	sel.removeAllRanges();
		        }catch(e){}
		        try{
		        	sel.addRange(range);
		        }catch(e){}
	        }
	    }
	    else if (document.body && document.body.createTextRange) {
	    	try{
		        range = document.body.createTextRange();
		        range.moveToElementText(el);
		        range.select();
	    	}
	    	catch(e){}
	    }
	};
	
	var createCollapsibleSection = function(i, ob){
		ob = $(ob);
		
		var headingSelector	= "h3 a";
		var headingSelected	= ob.find(headingSelector);
		var fnGetItems		= function(){
			return headingSelected.closest('.facet-header').next('li').find('ul').find('a');
		};
		
		var accessibility = new EuAccessibility(
			headingSelected,
			fnGetItems
		);
		
		ob.Collapsible(
			{
				"headingSelector"		: "h3 a",
				"bodySelector"			: "ul",
				"keyHandler"			: accessibility
			}
		);
		
		headingSelected.click(function(){
			var btn = $(this).closest('.facet-header').find('button');
			if($(this).hasClass('active')){
				btn.css('display', 'block');
			}
			else{
				btn.css('display', 'none');
			}
		});	
	};
	
	
	/* Disable tabs that have unfilled inputs */
	var setDisabledTabs = function(){
		if(!self.tabs){
			return;
		}
		var disabled = [];
		var allTabs  = self.tabs.getTabs();
		var noTabs   = allTabs.length;
		
		$.each(allTabs, function(i, tab){
		
			var content = tab.getTabContent();
			var inputs  = content.find('input.mandatory');//.add(content.find('select.mandatory'));
			
			inputs.each(function(j, field){
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
		var progress = self.tabs.getOpenTabId() == "tab-5" ? 100 : ((noTabs - disabled.length -1) / noTabs) * 100;
		
		$('.progress .bar').css('width', progress + '%');
		
		var openIdex = self.tabs.getOpenTabIndex();
		if( openIdex > 0){
			self.cmp.find('input.previous').removeClass('disabled');
		}
		else{
			self.cmp.find('input.previous').addClass('disabled');			
		}

		
		if( (openIdex == 0) ){
			$('.widget-configuration input.previous').addClass('disabled');
		}
		else{
			$('.widget-configuration input.previous').removeClass('disabled');
		}
		
		if( (openIdex+1) >= self.tabs.getTabs().length){
			$('.widget-configuration input.next').addClass('disabled');
		}
		else{
			$('.widget-configuration input.next').removeClass('disabled');
		}
		
		
	};
	
	
	var init = function(){

		// individual tab initialisation
		
		var initTab = function(tabIndex){
			
			if(self.initialisedTabs[tabIndex]){
				if(tabIndex != 4){
					return;
				}
			}
			
			if(tabIndex == 1){			// query / types / copyrights / languages
				
				self.searchMenu = new EuMenu( $(".search-what-menu"), {} );
				self.searchMenu.init();
				
				var change = function(e){
					var cb       = e.target ? $(e.target) : $(e);
					var checked  = cb.prop('checked');
					console.log( (checked ? "Add" : "Remove") + cb.attr('title') );
				};
				
				// make facet sections collapsible
				
				$("#tab-2 #filter-search>li.facet-header").each(function(i, ob){
					createCollapsibleSection(i, ob);
				});

				
				$("ul.types input").add("ul.copyrights input").add("ul.languages input").change(change).click(function(e){
					e.stopPropagation();
				}).prop('checked', false);

				$('ul.types a').add("ul.copyrights a").add("ul.languages a").click(function(e){
					var cb = $(this).find('input'); 
					cb.prop('checked', !cb.prop('checked'));
					change(cb);
					e.preventDefault();
				});

				$('button.clear-types').click(function(){
					$('ul.types').find('input').prop('checked', false);
				});
				
				$('button.clear-copyrights').click(function(){
					$('ul.copyrights').find('input').prop('checked', false);
				});
				
				$('button.clear-languages').click(function(){
					$('ul.languages').find('input').prop('checked', false);
				});
			}

			if(tabIndex == 2){				// providers
				
				eu_europeana_providers = {
					addLinks : function(){
						
						$('.choices').css('display', 'none');
						$('.choices').html('');
						
						var show = false;
						
						$('.data-providers').find('input').each(function(i, ob){
							ob = $(ob);
							if(ob.prop('checked')){
								show = true;
								var removeLink = $('<span class="remove-link icon-remove small">').appendTo('.choices');
								var text = ob.next('span').html();
								
								removeLink.attr("title", text);
								removeLink.html('<span>&nbsp;' + text + '</span>');
								removeLink.click(function(){
									ob.prop('checked', false);
									removeLink.remove();
								});
							}
						});
						
						if(show){
							$('.choices').css('display', 'block');
						}
					},
					
					init: function(){
						
// checkboxes and collapsibility
						
						var change = function(e){
console.log("change event START");							
							var cb       = e.target ? $(e.target) : $(e);
							var checked  = cb.prop('checked');
							var subBoxes = cb.parent().next('ul').find('li a input');
							$.each(subBoxes, function(i, ob){
								$(ob).prop('checked', checked);
							});
							eu_europeana_providers.addLinks();
console.log("change event END");							
						};
						
						$('.icon-arrow-2-after>input').add('.data-providers input').change(change).click(function(e){
							e.stopPropagation();
						}).prop('checked', false);
						
						$('.icon-arrow-2-after').click(function(e){
							var innerList = $(this).parent().find('ul');
							innerList.is(':visible') ? innerList.hide('slow') : innerList.show('slow');							
							e.preventDefault();
						});
					  
						$('.data-providers a').click(function(e){
							var cb = $(this).find('input'); 
							cb.prop('checked', !cb.prop('checked'));
							//change(cb);
							eu_europeana_providers.addLinks();							
						});
						
						// filter
						
					   	$('#content-provider-filter-filter').keyup( function(e){
					    	var val =  $(this).val().toUpperCase();
					    	if(val.length > 0){
					    		//var re = new RegExp('^' + val + '[A-Za-z\\d\\s]*');
					    		var re = new RegExp(val + '[A-Za-z\\d\\s]*');
					    		
					    		$('#wizard-tabs .icon-arrow-2-after span')
					    		.add('#wizard-tabs .no-children')
					    		.each(function(i, ob){
					            	var text		= $(ob).html().toUpperCase();
					            	var item		= $(ob).closest('li');
					            	var sub			= item.find('ul');
					            	var childMatch	= false;
					            	
					            	sub.find('li').each(function(j, subItem){
					            		subItem = $(subItem);
					            		var subText = subItem.find('span').html().toUpperCase();
					            		if(re.test(subText)){
					            			subItem.show();
					            			childMatch = true;
					            		}
					            		else{
					            			subItem.hide();					            			
					            		}
					            	});
					            	

					            	if(re.test(text)){
					            		item.show();
					            	}
					            	else{
					            		if(childMatch){
					            			// open.... 
					            			$(ob).closest('li').show();
					            			if(sub.css('display')=="none"){
					            				$(ob).click();					            				
					            			}
					            		}
					            		else{
					            			$(ob).closest('li').hide();
					            		}
					            	}
					            });    		
					    	}
					    	else{
					            $('.icon-arrow-2-after span').add('.no-children').closest('li').show();    		
					    	}

					    } );						
					   	
					}
				};
				eu_europeana_providers.init();
				

			}
			if(tabIndex == 3){
				// TODO: generic checkbox change
				var change = function(e){
					var cb       = e.target ? $(e.target) : $(e);
					var checked  = cb.prop('checked');
				};
				
				$("ul.portal-languages input").change(change).click(function(e){
					e.stopPropagation();
				}).prop('checked', false);
				
				$('ul.portal-languages a').click(function(e){
					var cb = $(this).find('input'); 
					cb.prop('checked', !cb.prop('checked'));
					change(cb);
				});

			}
			if(tabIndex == PREVIEW_TAB_INDEX){
				
				/* dynamically added scripts have no readable src attribute, so we have to provide the #widget-url-ref fall back  */
				var src = output();
				$('.preview-window').html('');
				$('.preview-window').append('<input  type="hidden" id="widget-url-ref" value="' + src + '" />');
				$('.preview-window').append('<script type="text/javascript" src="' + src + '"></script>');

				/*
				$('.update-preview').click(function(e){					
				});
				*/
			}
			self.initialisedTabs[tabIndex] = true;
		};  // end initTab()
		
		self.tabs = new AccordionTabs(
				self.cmp,
				function(i){
					initTab(i);
					setTimeout(function(){
						$('#step' + (i+1)).val("Yes2014"); // satisfy mandatory condition
						setDisabledTabs();
					}, 1);
					
					if(i == PREVIEW_TAB_INDEX){
						var result = '&lt;script type="text/javascript" src="' + output() + '"&gt;&lt;/script&gt;';
						$('#output').html(result);
						selectElementContents($('#output')[0]);
					}
				},
				false,
				disabledClick
		);
	
		setDisabledTabs();
		
		self.cmp.find('input:not(.next):not(.previous)').val('');
		self.cmp.find('select').val('');
		
		self.cmp.find('input.mandatory').keyup(function(e){
			setDisabledTabs();
		});
		
		self.cmp.find('select').change(function(e){
			setDisabledTabs();
		});

		$('.widget-configuration input.next').addClass('disabled');
		
		$('.widget-configuration input.next').click(function(){
			if( $(this).hasClass('disabled')){
				disabledClick();
				return;
			}
			self.tabs.openTabAtIndex(self.tabs.getOpenTabIndex()+1);
		});
		
		$('.widget-configuration input.previous').click(function(){
			if( $(this).hasClass('disabled')){
				return;
			}
			self.tabs.openTabAtIndex(self.tabs.getOpenTabIndex()-1);
		});
		
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
