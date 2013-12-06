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
	
    
	var cleanName = function(name){
		return  name.replace(/"/g, '\\\"').replace(/ *\([^)]*\) */g, "").replace(/\s/g, '\+').replace(/\%20/g, '\+');
	};
		
    var showSpinner = function(){
    	$('.PROVIDER').add('.COUNTRY').add('.TYPE').add('.RIGHTS').add('.LANGUAGE').add('.choices').append('<div class="wizard-overlay">');
    	
    	$.each($('.wizard-overlay'), function(i, ob){
    		$(ob).css('top', $(ob).parent().scrollTop() + 'px'); 
    	});
    };
    
    var hideSpinner = function(){
    	$('.PROVIDER').add('.COUNTRY').add('.TYPE').add('.RIGHTS').add('.LANGUAGE').add('.choices').find('.wizard-overlay').remove();
    };
    
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

		
		// Facets
		
		if(self.initialisedTabs[1]){
			
			// non-provider facets
			
			var query = searchPrefix + $('.widget-configuration .default_query').val();
			if(query){
				result += param() + 'query=' + query;			
			}
			
			$('ul.TYPE a input').add('ul.COUNTRY a input').add('ul.RIGHTS a input').add('ul.LANGUAGE a input').each(function(i, ob){
				if($(ob).prop('checked')){
					//var name = $(ob).next('span').html().replace(/ *\([^)]*\) */g, "");
					//var name = $(ob).attr('title').replace(/ *\([^)]*\) */g, "");
					var name = $(ob).attr('title').replace(/\"/g, "");
					result += name;
				}
			});

			// Providers
			
			var debug = '';
			
			$('.PROVIDER>li').each(function(i, ob){
				var provider      = $(ob);
				var providerInput = provider.children('a').children('input');
				
				// a checked provider includes all child data_providers
				
				var name			= providerInput.next('label').html();
				var providerParam	= 'qf=' + 'PROVIDER' + ':{' + cleanName(name) + '}';

				if(providerInput.prop('checked')){
					result += param() + providerParam;
				}
				else{
					var subtractUrl = '';
					var resultFragment = '';
					
					provider.find('.DATA_PROVIDER>li').each(function(j, dp){
						var dataProvider      = $(dp);
						var dataProviderInput = dataProvider.children('a').children('input');
						var name              = dataProviderInput.next('label').html();

						if(dataProviderInput.prop('checked')){
							//result += param() + 'qf=' + 'DATA_PROVIDER' + ':{' + cleanName(name) + '}';
							resultFragment += param() + 'qf=' + 'DATA_PROVIDER' + ':{' + cleanName(name) + '}';
						}
						else{
							subtractUrl += '&qf=-' + 'DATA_PROVIDER' + ':{' + cleanName(name) + '}';
						}
					});
					
					// which is shorter?  Use that!
					result += resultFragment.length < (providerParam.length + subtractUrl.length) ? resultFragment : providerParam + subtractUrl;
				}
			});

				
		}
		
		result += param() + 'withResults=' + getWithResults();
		result += param() + 'theme=' + getTheme();

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
			
			if(tabIndex == 1){			// query / provider / types / copyrights / languages
				
				$('.default_query').focus();
				
				/*  */
				
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
					var callingFacet = cb.closest('ul').attr('class');
					if(callingFacet.indexOf(' ') > -1){
						callingFacet = callingFacet.split(' ')[0];
					}
					updateAvailableFacets(callingFacet);
				};
				
				// make facet sections collapsible
				
				$("#tab-2 #filter-search>li.facet-header").each(function(i, ob){
					createCollapsibleSection(i, ob);
				});

				
				$("ul.search-types input").add("ul.TYPE input").add("ul.RIGHTS input").add("ul.COUNTRY input").add("ul.LANGUAGE input").change(change).click(function(e){
					e.stopPropagation();
				})
				$("ul.TYPE input").add("ul.RIGHTS input").add("ul.LANGUAGE input").prop('checked', false);

				$('ul.search-types a').add('ul.TYPE a').add("ul.RIGHTS a").add("ul.LANGUAGE a").click(function(e){
					var cb = $(this).find('input'); 
					cb.prop('checked', !cb.prop('checked'));
					change(cb);
					e.preventDefault();
				});

				
				$('button.clear-types').click(function(){
					$('ul.TYPE').find('input').prop('checked', false);
					updateAvailableFacets('TYPE');
					
					/*
					$('ul.TYPE').prev('h3').find('.modified')  .css('display', 'none');
					$('ul.TYPE').prev('h3').find('.unmodified').css('display', 'inline-block');
					*/

				});
				
				$('button.clear-countries').click(function(){
					$('ul.COUNTRY').find('input').prop('checked', false);
					updateAvailableFacets('COUNTRY');
					
					/*
					$('ul.COUNTRY').prev('h3').find('.modified')  .css('display', 'none');
					$('ul.COUNTRY').prev('h3').find('.unmodified').css('display', 'inline-block');
					 */
					
				});
				
				$('button.clear-copyrights').click(function(){
					$('ul.RIGHTS').find('input').prop('checked', false);
					updateAvailableFacets('RIGHTS');
					
					/*
					$('ul.RIGHTS').prev('h3').find('.modified')  .css('display', 'none');
					$('ul.RIGHTS').prev('h3').find('.unmodified').css('display', 'inline-block');
					*/
				});
				
				$('button.clear-languages').click(function(){					
					$('ul.LANGUAGE').find('input').prop('checked', false);
					updateAvailableFacets('LANGUAGE');
					
					/*
					$('ul.LANGUAGE').prev('h3').find('.modified')  .css('display', 'none');
					$('ul.LANGUAGE').prev('h3').find('.unmodified').css('display', 'inline-block');
					*/
				});


				// PROVIDER
				
				eu_europeana_providers = {
					addLinks : function(cb){
						
						// parent checkboxes
						var checked = cb.prop('checked');
						var updateParentCheckboxes = function(cb, checked){
							
							var parentProvider = cb.closest('.DATA_PROVIDER').prev('a').find('input[type=checkbox]');
	
							if(!checked && parentProvider.prop('checked')){ // remove parent check if the set was complete
								parentProvider.prop('checked', false);
							}
							else{  // restore parent check if this data-provider completes the set
								var siblingProviders = cb.closest('.DATA_PROVIDER').find('input[type=checkbox]');
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
						
						
						$('.DATA_PROVIDER').find('input').each(function(i, ob){
							ob = $(ob);
							if(ob.prop('checked')){
								show = true;
								var removeLink = $('<span class="remove-link icon-remove small">').appendTo('.choices');
								var text = ob.next('label').html();
								
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
									updateAvailableFacets('DATA_PROVIDER');
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
							
							var callingFacet = cb.closest('ul').attr('class');
							if(callingFacet.indexOf(' ') > -1){
								callingFacet = callingFacet.split(' ')[0];
							}
							updateAvailableFacets(callingFacet);
						};
						
						$('.icon-arrow-2-after>input').add('.DATA_PROVIDER input').change(change).click(function(e){
							e.stopPropagation();
						}).prop('checked', false);
						
						$('.icon-arrow-2-after').click(function(e){
							var innerList = $(this).parent().find('ul');
							innerList.is(':visible') ? innerList.hide('slow') : innerList.show('slow');							
							e.preventDefault();
						});
					  
						$('.DATA_PROVIDER a').click(function(e){
							var cb = $(this).find('input'); 
							cb.prop('checked', !cb.prop('checked'));
							eu_europeana_providers.addLinks(cb);
							
							updateAvailableFacets('DATA_PROVIDER');
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
			            			var subText = subItem.find('label').html().toUpperCase();
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
					            			var subText = subItem.find('label').html().toUpperCase();
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
		
		$('.tab-number').removeClass('hidden');
	};
	
	var updateAvailableFacets = function(chosenFacet){
		
		showSpinner();
		
		// construct query
		
		var query = "";
		try{			
			$('.PROVIDER>li').each(function(i, ob){
				var provider       = $(ob);
				var providerInput  = provider.children('a').children('input');
				
				var name           = providerInput.next('label').html();
				var providerParam  = '&qf=PROVIDER:"' + cleanName(name) + '"';


				
				if(providerInput.prop('checked')){
					//var name = providerInput.next('label').html();
					//query += '&qf=PROVIDER:"' + cleanName(name) + '"';
					query += providerParam
				}
				else{
					//alert('NOT CHECKED ' + providerParam)
					var subtractUrl    = '';
					var resultFragment = '';
					
					provider.find('.DATA_PROVIDER>li').each(function(j, dp){
						var dataProvider      = $(dp);
						var dataProviderInput = dataProvider.children('a').children('input');
						var name              = dataProviderInput.next('label').html();

						if(dataProviderInput.prop('checked')){
							//query += '&qf=' + 'DATA_PROVIDER:"' + cleanName(name) + '"';
							resultFragment += '&qf=DATA_PROVIDER:"' + cleanName(name) + '"';
						}
						else{
							subtractUrl += '&qf=-DATA_PROVIDER:"' + cleanName(name) + '"';							
						}
						
					});
					
					//alert(resultFragment + '\n\n - v- \n\n' + subtractUrl);
					// which is shorter?  Use that!
					query += resultFragment.length < (providerParam.length + subtractUrl.length) ? resultFragment : providerParam + subtractUrl;
				}
				

			});
			
			$('ul.TYPE a input').add('ul.COUNTRY a input').add('ul.RIGHTS a input').add('ul.LANGUAGE a input').each(function(i, ob){
				if($(ob).prop('checked')){
					if($(ob).attr('title')){
						query += $(ob).attr('title').replace(/\"/g, "");						
					}
					else{
						console.log('missing title for ' + $(ob).next('label').html() );
					}
				}
			});

		}
		catch(e){
			alert("Error in updateAvailableFacets: " + e);
		}
		
		var fnSuccess = function(data){
        	// countries

        	var countryOps = $('ul.COUNTRY li');
        	countryOps.find('a').hide();

        	// copyrights
        	
        	var copyrightOps = $('ul.RIGHTS li');
        	copyrightOps.find('a').hide();
        	
        	// providers
        	
        	var providerOps = $('ul.PROVIDER li');
        	
        	if(chosenFacet != 'DATA_PROVIDER' && chosenFacet != 'PROVIDER'){	        		
        		providerOps.find('a').hide();
        	}
        	
        	
        	// data providers
        	var dataProviderOps = $('ul.DATA_PROVIDER li');
        	if(chosenFacet != 'DATA_PROVIDER' && chosenFacet != 'PROVIDER'){	        		
        		dataProviderOps.find('a').hide();
        	}
        	
        	// types
        	
        	var typeOps = $('ul.TYPE li');
        	typeOps.find('a').hide();
        	
        	// languages
        	
        	var langOps = $('ul.LANGUAGE li');	        	
        	langOps.find('a').hide();
        	
        	$.each(data.facets, function(i, facet){
        		
    		 	var ops  = $('ul.' + facet.name);
    		    var regX = /\(\d*\)/g;
    		    
    		 	if(facet.name == 'RIGHTS'){
    		 		
        			$.each(facet.fields, function(j, field){
        				
        				//console.log("RIGHTS field.label " + field.label);
        				
        				 // ERROR:
        				 //
        				 //1) ADD FACET:
        				 //		CC BY-SA (1173821) 
        				 //
        				 //2) ADD FACET
        				 //		SOUND (484303)
        				 //
        				 //NOTE THAT THE ORIGINAL FACET VANISHES.
        				 //
        				 //
        				 //CAUSE:
        				 //
        				 //THE '*' QUERY RETURNS SPECIFICS ( /3.0/, /2.0/ ETC) WHICH DO NOT MATCH THE '*' LABEL
        				
        				//copyrightOps.find('a[title^="&qf=RIGHTS:' + field.label.replace(/\"/g, '&quot;') + '*"]').show();
        				var item  = copyrightOps.find('a[title^="&qf=RIGHTS:' + field.label.replace(/\"/g, '&quot;') + '"]');
        				var label = $(item).find('label');
        				item.show();
    					if(label.length ){
    						label.html( label.html().replace(regX, '(' + field.count + ')') );        						
    					}
        			});
    		 		
    		  	}
    		  	else{
    		  	
    		  		$.each(facet.fields, function(j, field){
    		  			var item  = ops.find('a[title="' + field.label + '"]');
    		  			var label = $(item).find('>label');

    					item.show();
    					
    					if(label.length ){
    						label.html( label.html().replace(regX, '(' + field.count + ')') );        						
    					}
    					
    				});
    		  	
    		  	}
    		 	
        	});
        	hideSpinner();
		};
		
		var postUrl = js.debug ?  "http://test.portal2.eanadev.org/api/v2/search.json?wskey=api2demo&query=*:*&profile=facets,params" : "http://www.europeana.eu/api/v2/search.json?wskey=api2demo&query=*:*&profile=facets,params";
		
		try{
			// IE8 & 9 only Cross domain JSON GET request
		    if ('XDomainRequest' in window && window.XDomainRequest !== null) {
		        var xdr = new XDomainRequest(); // Use Microsoft XDR
		        xdr.open('post', postUrl);
		        xdr.onprogress = function () {};
		        xdr.onload = function () {
		            var dom  = new ActiveXObject('Microsoft.XMLDOM'),
		                jsonRes = $.parseJSON(xdr.responseText);
		            dom.async = false;
		            if (jsonRes == null || typeof (jsonRes) == 'undefined') {
		            	jsonRes = $.parseJSON(data.firstChild.textContent);
		            }
		            fnSuccess(jsonRes);
		        };
		        xdr.onerror = function() {
		        	console.log('xdr fail');
		            _result = false;  
		        };
		        xdr.timeout = 0;
		        xdr.send();
		    } 
		    else{
				$.ajax({
					"url":				postUrl + query,
			        "dataType":			"json", 
			        "crossDomain":		true,
			        "type":				"POST",
			        "fail":function(){
			        	alert('fail');
			    		hideSpinner();
			        },
			        "success":function(data){
			        	fnSuccess(data);
			        }
			    });		    	
		    }
		}		
		catch(e){
			console.log("AJAX ERROR " + e);
		}
	};
	
	
	/* exposed functionality */
	
	return {
		init : function(){
			$(document).ready(function(){
				init();
			});
		}
	};
};
