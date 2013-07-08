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
	self.tabs            = false;
	self.disabledTabs    = [];//[1, 2, 3, 4];
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
	
	var output = function(preview){
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
			$('.data-providers a input').each(function(i, ob){
				if($(ob).prop('checked')){
					var name = $(ob).next('span').html().replace(/ *\([^)]*\) */g, "");
					result += param() + 'qf=PROVIDER:' + (name.indexOf(' ') > 0) ? '' + name.replace(/\s/g, '+') + '' : name;
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
		
		if(preview){
			return result;
		}
		else{
			result = '&lt;script type="text/javascript" src="' + result + '"&gt;&lt;/script&gt;';
			$('#output').html(result);			
		}
		
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
					  
						$('.data-providers a').click(function(e){
							var cb = $(this).find('input'); 
							cb.prop('checked', !cb.prop('checked'));
							change(cb);
							
						});
						
						// filter
						
					   	$('#content-provider-filter-filter').keyup( function(e){
					    	var val =  $(this).val().toUpperCase();
					    	if(val.length > 0){
					    		//var re = new RegExp('^' + val + '[A-Za-z\\d\\s]*');
					    		var re = new RegExp(val + '[A-Za-z\\d\\s]*');
					    		
					            $('#wizard-tabs .outer-list .icon-arrow-2-after span').add('#wizard-tabs .no-children').each(function(i, ob){
					            	var text = $(ob).html().toUpperCase();
					            	var item = $(ob).closest('li');
					            	re.test(text) ? item.show() : item.hide();
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
			if(tabIndex == 4){
				
				/* dynamically added scripts have no readable src attribute, so we have to provide the #widget-url-ref fall back  */
				var src = output(true);
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
						$('#step' + (i+1)).val("ANDY");
						setDisabledTabs();
					}, 1);
					
					output();
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
