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
	
	var PREVIEW_TAB_INDEX = 3;
	
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
		
		var searchPrefix = $('input[name=rd-search-type]:checked').val();
		searchPrefix = typeof searchPrefix == 'undefined' ? '' : searchPrefix;
		
		if(self.initialisedTabs[1]){
			var query = searchPrefix + $('.widget-configuration .default_query').val();
			if(query){
				result += param() + 'query=' + query;			
			}
			
			$('ul.types a input').add('ul.copyrights a input').add('ul.languages a input').each(function(i, ob){
				if($(ob).prop('checked')){
					//var name = $(ob).next('span').html().replace(/ *\([^)]*\) */g, "");
					//var name = $(ob).attr('title').replace(/ *\([^)]*\) */g, "");
					var name = $(ob).attr('title').replace(/\"/g, "");
					result += name;
				}
			});			

			var cleanName = function(name){
				return name.replace(/ *\([^)]*\) */g, "").replace(/\s/g, '\+').replace(/\%20/g, '\+');
			};
			
			try{			
				$('.providers>li').each(function(i, ob){
					var provider      = $(ob);
					var providerInput = provider.children('a').children('input');
					
					if(providerInput.prop('checked')){
						var name = providerInput.next('span').html();
						
						// console.log("checked PROVIDER name is " + name);
						
						result += param() + 'qf=' + 'PROVIDER' + ':{' + cleanName(name) + '}';
						//result += param() + 'qf=' + ( $(ob).parent().hasClass('data-provider') ? 'DATA_PROVIDER' : 'PROVIDER') + ':{' + name + '}';
					}
					else{
						provider.find('.data-providers>li').each(function(j, dp){
							var dataProvider      = $(dp);
							var dataProviderInput = dataProvider.children('a').children('input');
	
							if(dataProviderInput.prop('checked')){
								var name          = dataProviderInput.next('span').html();
	
								console.log("checked DATA PROVIDER name is " + name);
	
								result += param() + 'qf=' + 'DATA_PROVIDER' + ':{' + cleanName(name) + '}';
	
							}
							
						});
					}
				});
			}
			catch(e){console.log(e);}
		}
		
		result += param() + 'withResults=' + getWithResults();
		result += param() + 'theme=' + getTheme();

		console.log('output() returns ' + result);

		return result;
	};
	
	var setCopy = function(copyIn){
		var copy = '&lt;script type="text/javascript" src="' + (copyIn ? copyIn : output()) + '"&gt;&lt;/script&gt;';
		$('#output').html(copy);
		selectElementContents($('#output')[0]);
	}
	
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
		
		var headingSelector	= "h3 a .change";
		var headingSelected	= ob.find(headingSelector);
		var fnGetItems		= function(){
			return headingSelected.closest('.facet-header').children('ul').find('a');
		};
		
		var accessibility = new EuAccessibility(
			headingSelected,
			fnGetItems
		);

		ob.Collapsible(
			{
				"expandedClass"         : "icon-arrow-7-right",
				"collapsedClass"        : "icon-arrow-6-right",
				"headingSelector"       : headingSelector,
				"bodySelector"          : ".hide-til-opened",
				"keyHandler"            : accessibility
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

	var getSelectedImageIndex = function(){
		return parseInt( $('.theme-select img.active').attr('data-index') );
	};
	
	var getTheme = function(){
		var selIndex = getSelectedImageIndex();
		return (selIndex == 2 || selIndex == 4) ? 'dark' : '';
	};
	
	var getWithResults = function(){
		var selIndex = getSelectedImageIndex();
		return ( selIndex == 3 || selIndex == 4) ? false : true;
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
			var inputs  = content.find('input.mandatory');
			
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
		var progress = self.tabs.getOpenTabId() == "tab-4" ? 100 : ((noTabs - disabled.length -1) / noTabs) * 100;
		
		$('.progress .bar').css('width', progress + '%');
		
		var openIdex = self.tabs.getOpenTabIndex();
		/*
		if( openIdex > 0){
			self.cmp.find('a.previous').removeClass('disabled');
		}
		else{
			self.cmp.find('a.previous').addClass('disabled');			
		}
		if( (openIdex == 0) ){
			$('.widget-configuration input.previous').addClass('disabled');
		}
		else{
			$('.widget-configuration input.previous').removeClass('disabled');
		}
		*/

		
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
				if(tabIndex != PREVIEW_TAB_INDEX){ // last tab can be reinitialised
					return;
				}
			}
			self.initialisedTabs[tabIndex] = true;
			
			if(tabIndex == 1){			// query / providers / types / copyrights / languages
				
				$('.default_query').focus();
				
				var change = function(e){
					var cb           = e.target ? $(e.target) : $(e);
					var checked      = cb.prop('checked');
					var groupChecked = false;
					
					if(cb.attr('type')=='radio'){
						groupChecked = cb.attr('id') == 'rd-search-type-all' ? false : true; 
					}
					else{
						$.each( cb.closest('.facet-header').find('.hide-til-opened').find('input'), function(i, ob){
							if( $(ob).is(':checked') ){
								groupChecked = true;
								return true;
							}
						});						
					}
					
					var labels = cb.closest('.facet-header').find('.facet-section');
					
					/*
					if(groupChecked){
						labels.find('.unmodified').css('display', 'none');
						labels.find('.modified')  .css('display', 'inline-block');
					}
					else{
						labels.find('.unmodified').css('display', 'inline-block');
						labels.find('.modified')  .css('display', 'none');
					}
					*/
				};
				
				// make facet sections collapsible
				
				$("#tab-2 #filter-search>li.facet-header").each(function(i, ob){
					createCollapsibleSection(i, ob);
				});

				
				$("ul.search-types input").add("ul.types input").add("ul.copyrights input").add("ul.languages input").change(change).click(function(e){
					e.stopPropagation();
				})
				$("ul.types input").add("ul.copyrights input").add("ul.languages input").prop('checked', false);

				$('ul.search-types a').add('ul.types a').add("ul.copyrights a").add("ul.languages a").click(function(e){
					var cb = $(this).find('input'); 
					cb.prop('checked', !cb.prop('checked'));
					change(cb);
					e.preventDefault();
				});

				
				$('button.clear-types').click(function(){
					$('ul.types').find('input').prop('checked', false);
					/*
					$('ul.types').prev('h3').find('.modified')  .css('display', 'none');
					$('ul.types').prev('h3').find('.unmodified').css('display', 'inline-block');
					*/

				});
				
				$('button.clear-copyrights').click(function(){
					$('ul.copyrights').find('input').prop('checked', false);
					/*
					$('ul.copyrights').prev('h3').find('.modified')  .css('display', 'none');
					$('ul.copyrights').prev('h3').find('.unmodified').css('display', 'inline-block');
					*/
				});
				
				$('button.clear-languages').click(function(){
					$('ul.languages').find('input').prop('checked', false);
					/*
					$('ul.languages').prev('h3').find('.modified')  .css('display', 'none');
					$('ul.languages').prev('h3').find('.unmodified').css('display', 'inline-block');
					*/
				});


				// providers
				
				eu_europeana_providers = {
					addLinks : function(cb){
						
						// parent checkboxes
						var checked = cb.prop('checked');
						var updateParentCheckboxes = function(cb, checked){
							
							var parentProvider = cb.closest('.data-providers').prev('a').find('input[type=checkbox]');
	
							if(!checked && parentProvider.prop('checked')){ // remove parent check if the set was complete
								parentProvider.prop('checked', false);
							}
							else{  // restore parent check if this data-provider completes the set
								var siblingProviders = cb.closest('.data-providers').find('input[type=checkbox]');
								var allSiblingsChecked = true;
								siblingProviders.each(function(i, ob){
									if(!$(ob).prop('checked')){
										allSiblingsChecked = false;
										return false; /* break */
									}
								});
								if(allSiblingsChecked){
									parentProvider.prop('checked', true);
								}
							}
						}
						updateParentCheckboxes(cb, checked);
						
						// choices 
						
						$('.choices').css('display', 'none');
						$('.choices').html('');
						
						var show = false;
						var setModified = function(){
							return;
							//labels.find('.modified')  .css('display', 'inline-block');
							//labels.find('.unmodified').css('display', 'none');
						}
						var setUnmodified = function(){
							return;
							//labels.find('.modified')  .css('display', 'none');
							//labels.find('.unmodified').css('display', 'inline-block');							
						}
						
						
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
									updateParentCheckboxes( ob, false );
									removeLink.remove();
									if(!$('.choices').html().length){
										$('.choices').css('display', 'none');
										setUnmodified();
									}
								});
							}
						});
						
						
						var labels = $('.choices').closest('.facet-header').find('.facet-section');
						

						if(show){
							$('.choices').css('display', 'block');
							setModified();
						}
						else{
							setUnmodified();
						}
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
							eu_europeana_providers.addLinks(cb);
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
							eu_europeana_providers.addLinks(cb);							
						});
						
						
						// filter objects
						var filterObjects = [];
						
						var FilterObject = function(el, doDebug){
							
							var self		= this;
							self.el			= el;
							self.item		= el.closest('li');
							self.subItems	= [];
							self.sub		= self.item.find('ul');
			            	if(self.sub){
			            		self.sub.find('li').each(function(j, subItem){
			            			subItem = $(subItem);
			            			var subText = subItem.find('span').html().toUpperCase();
			            			self.subItems[self.subItems.length] = {"t" : subText, "e" : subItem };
			            		});
			            	}

							self.text		= el.html().toUpperCase();
							
							return {
								"test" : function(s){
									var re			= new RegExp(s.toUpperCase() + '[A-Za-z\\d\\s]*');
					            	var childMatch	= false;
					            	if(self.sub){
					            		self.sub.find('li').each(function(j, subItem){
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
					            	}
									if(childMatch || re.test(self.text)){
										self.item.show();
									}
									else{
										self.item.hide();										
									}
								}
							};
							
						};
						
			    		$('#wizard-tabs .icon-arrow-2-after span')
			    		.add('#wizard-tabs .no-children')
			    		.each(function(i, ob){
			    			filterObjects[filterObjects.length] = new FilterObject( $(ob), i==0 );
			    		});
			    		
						
					   	$('#content-provider-filter-filter').keyup(function(e){
					    	var val =  $(this).val().toUpperCase();
					   		$.each(filterObjects, function(i, ob){
					   			ob.test(val);
					   		});
					   	});
					}
				};
				eu_europeana_providers.init();
			}
			if(tabIndex == 2){
				$('.theme-select img').click(function(){
					$('.theme-select img').removeClass('active');
					$(this).addClass('active');
				});
			}
			if(tabIndex == PREVIEW_TAB_INDEX){

				// this tab re-inits

				var src = output();
				
				$('.preview-window').html('');
				$('.preview-window').append('<input  type="hidden" id="widget-url-ref" value="' + src + '" />');
				$('.preview-window').append('<script type="text/javascript" src="' + src + '"></script>');					

				/* dynamically added scripts have no readable src attribute, so we have to provide the #widget-url-ref fall back  */
				/*
				var doPreview = function(){
					searchWidget.setWithResults( getWithResults() );
					searchWidget.setTheme($('#themeDark').prop('checked') ? "dark" : false);
			
					var src = output();
					//$("#widget-url-ref").attr('src', src)
					setCopy(src);
				};
				*/
				//$('#withResults').add('#withoutResults').add('#themeClassic').add('#themeDark').change(function(e){
				//	doPreview();
				//});
			}
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
						setCopy();
					}
				},
				false,
				disabledClick
		);
	
		setDisabledTabs();
		
		self.cmp.find('input[type!=radio]:not(.next)').val('');
		self.cmp.find('#rd-search-type-all').prop('checked', true);
		
		
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
		
		$('.widget-configuration a.previous').click(function(e){
			e.preventDefault();
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
