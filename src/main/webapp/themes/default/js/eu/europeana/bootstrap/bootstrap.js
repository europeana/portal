/**
 *	bootstrap.js
 *
 *	@author		dan entous <contact@gmtpluosone.com>
 *	@author		andy maclean <andyjmaclean@gmail.com>
 *	@version	2012-07-12
 */
'use strict';


var europeana_bootstrap = function(){
	
	if ( !window.eu ) { throw new Error( 'window.eu was not defined before bootstrap' ); }
	if ( !window.js ) { throw new Error( 'window.js was not defined before bootstrap' ); }

	
	var loadedScripts = {};
	
	// loads the loader which then loads the scripts
	function loadScripts(scripts){
		
		var script = document.createElement('script');
		script.src = eu.europeana.vars.branding + '/js/js/' + js.min_directory + 'loader' + js.min_suffix + '.js' + js.cache_helper;
		
		if ( 'onload' in document || 'addEventListener' in window ) {
			script.onload = function(){
				js.loader.loadScripts( scripts );
			};
		}
		else if ( 'onreadystatechange' in document ) {	
			script.onreadystatechange = function () {
				if ( script.readyState == 'loaded' || script.readyState == 'complete' ) {
					js.loader.loadScripts( scripts );
				}
			};
		}
		if(!loadedScripts[script.src]){
			loadedScripts[script.src] = 1;
			document.getElementsByTagName('body')[0].appendChild( script );		
		}
		else{
			js.loader.loadScripts( scripts );
		}
	}
	
	
	// array of scripts needed for all pages
	var scripts = [
	  {	name  : 'jquery',			file : 'jquery-1.8.1.min.js',													path : eu.europeana.vars.branding + '/js/jquery/min/'	},

	  { name : 'jquery-ui',			file : 'jquery-ui-1.9.0.custom.min.js',											path : eu.europeana.vars.branding + '/js/jquery/min/',															dependencies : [ 'jquery' ]	},
	  
//	  {	name : 'utils',				file : 'utils' + js.min_suffix + '.js' + js.cache_helper,						path : eu.europeana.vars.branding + '/js/js/' + js.min_directory,											dependencies : [ 'jquery-ui' ]	},
	  {	name : 'utils',				file : 'utils' + js.min_suffix + '.js' + js.cache_helper,						path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,											dependencies : [ 'jquery-ui' ]	},
	  
	  
	  {	name : 'analytics',			file : 'analytics' + js.min_suffix + '.js' + js.cache_helper,					path : eu.europeana.vars.branding + '/js/com/google/analytics/' + js.min_directory,							dependencies : [ 'utils' ]	},
	  {	name : 'ajax',				file : 'ajax' + js.min_suffix + '.js' + js.cache_helper,						path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,									dependencies : [ 'utils' ]	},
	  
	  {
		  name : 'EuAccessibility',
		  file : 'EuAccessibility' + js.min_suffix + '.js' + js.cache_helper,
		  path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory		  
	  },
	  
	  {
		  name : 'EuMenu',
		  file : 'EuMenu' + js.min_suffix + '.js' + js.cache_helper,
		  path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
		  dependencies : [ 'utils', 'EuAccessibility' ]
	  },

	  {	name : 'header',			file : 'header' + js.min_suffix + '.js' + js.cache_helper,						path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
		dependencies : [ 'ajax', 'utils' ],
		callback : function(){
			eu.europeana.header.init();
		}},
		
	  {	name : 'orientation-fix',	file : 'ios-orientationchange-fix' + js.min_suffix + '.js' + js.cache_helper,	path : eu.europeana.vars.branding + '/js/scottjehl-iOS-Orientationchange-Fix-99c9c99/' + js.min_directory	},
	  
	  {
			name : 'touchswipe',
			file : 'touch-swipe' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/jquery/' + js.min_directory,
			dependencies : [ 'utils', 'jquery' ]
	  },
	  {
		  name : 'ellipsis',
		  file : 'ellipsis' + js.min_suffix + '.js' + js.cache_helper,
		  path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
		  dependencies : [ 'utils' ]
	  },
	  
	  {
		  	name: 'euresponsive',
			file: 'euresponsive' + js.min_suffix + '.js' + js.cache_helper,
			path: eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'jquery', 'utils' ]
	  }
	  
	  
   	];
	
	if(eu.europeana.vars.page_name == 'api/console.html'){
		loadScripts(scripts);
	}

	if(eu.europeana.vars.page_name == 'api/registration.html'){
		loadScripts(scripts);
	}

	if(eu.europeana.vars.page_name == 'widget/editor.html'){
		
		scripts.push({
			name : 'AccordionTabs',
			file : 'accordion-tabs2' + js.min_suffix + '.js' + js.cache_helper,
			dependencies : ['jquery'],
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
		});
		  
		scripts.push({
			file : 'EuWidgetWizard' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : ['AccordionTabs'],
			callback : function(){
				new EuWidgetWizard(
					$('#wizard-tabs'),
					{}
				).init();
			}
		});
		
		loadScripts(scripts);
	}
	
	if(eu.europeana.vars.page_name == 'contact.html'){
		scripts.push({
			name : 'contact',
			file : 'contact' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
		loadScripts(scripts);
	}
	else if(eu.europeana.vars.page_name == 'embed'){
		scripts.push({
			name : 'embed',
			file : 'embed' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
		loadScripts(scripts);
	}
	else if(eu.europeana.vars.page_name == 'europeana-providers.html'){
		scripts.push({
			file : 'providers' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'jquery', 'utils' ],
			callback : function(){
				eu_europeana_providers.init();
			}
		});
		loadScripts(scripts);
	}
	else if(eu.europeana.vars.page_name == 'exception.html'){
		loadScripts(scripts);
	}
	else if(eu.europeana.vars.page_name == 'forgotPassword.html'){
		scripts.push({
			file : 'forgot-password' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
		loadScripts(scripts);
	}
	else if(eu.europeana.vars.page_name == 'full-doc.html'){
		/*
		scripts.push({
			name : 'sharethis',
			file : 'buttons.js',
			path : '//w.sharethis.com/button/'
		});
		*/
		
		scripts.push({
			name : 'galleria',
			file : 'galleria-1.2.8' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/galleria/',
			dependencies : [ 'jquery', 'touchswipe'  ]
		});

		scripts.push({
			name : 'fulldoc',
			file : 'fulldoc' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils', 'galleria', 'EuMenu' ]
		});


		loadScripts(scripts);

	}
	else if(eu.europeana.vars.page_name == 'index.html'){
		/*
		scripts.push({
			name : 'sharethis',
			file : 'buttons.js',
			path : '//w.sharethis.com/button/'
		});
		*/
		
		scripts.push({
			name : 'collapsible',
			file : 'collapsible' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils', 'EuAccessibility' ]
		});

		scripts.push({
			name : 'galleria',
			file : 'galleria-1.2.8' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/galleria/',			
			dependencies : [ 'jquery', 'touchswipe'  ]
		});

		scripts.push({
			name : 'index',
			file : 'index' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : ['collapsible', 'utils', 'galleria', 'EuMenu']
		});
				
		loadScripts(scripts);
	}
	else if(eu.europeana.vars.page_name == 'login.html'){
		
		scripts.push({
			file : 'login' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
		
		loadScripts(scripts);
	}
	else if(eu.europeana.vars.page_name == 'map.html'){
		/* IE versions 7 and 8 don't support the canvas object, so we add the google code here to make it work */
		if(navigator.userAgent.indexOf("MSIE 7") >-1 || navigator.userAgent.indexOf("MSIE 8") >-1){
			scripts.push({
				name : 'excanvas',
				file : 'excanvas.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/sti/e4D-javascript/lib/'
			});
			scripts.push({
				name : 'map',
				file : 'map' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'utils', 'excanvas' ]
			});
		}
		else{				
			scripts.push({
				name : 'map',
				file : 'map' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'utils' ]
			});
		}
		loadScripts(scripts);
	}		
	else if(eu.europeana.vars.page_name == 'myeuropeana.html'){
		scripts.push({
			name : 'myeuropeana',
			file : 'myeuropeana' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
		
		loadScripts(scripts);
	}
	else if(eu.europeana.vars.page_name == 'register.html'){
		loadScripts(scripts);
	}
	else if(eu.europeana.vars.page_name == 'register-success.html'){	// change password success
		scripts.push({
			name : 'register-success',
			file : 'register-success' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory
		});
		
		loadScripts(scripts);
	}
	else if(eu.europeana.vars.page_name == 'search.html'){
		/*
		scripts.push({
			name : 'sharethis',
			file : 'buttons.js',
			path : '//w.sharethis.com/button/'
		});
		*/
		
		scripts.push({
			name : 'collapsible',
			file : 'collapsible' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils', 'EuAccessibility' ]
		});
		
		scripts.push({
			name : 'EuPagination',
			file : 'EuPagination' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory
		});
		
		scripts.push({
			file : 'search' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'ajax', 'utils', 'collapsible', 'ellipsis', 'EuMenu', 'EuPagination' ],
			callback: function(){
				eu.europeana.search.init();
			}
		});

		loadScripts(scripts);
	}
	else if(eu.europeana.vars.page_name == 'staticpage.html'){
		//scripts.push({
			//file : 'addthis' + js.min_suffix + '.js' + js.cache_helper,
			//path : eu.europeana.vars.branding + '/js/com/addthis/' + js.min_directory,
			//dependencies : [ 'utils' ]
		//});
		loadScripts(scripts);
	}
	else if(eu.europeana.vars.page_name == 'timeline.html'){
		scripts.push({
			file : 'timeline' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
		loadScripts(scripts);
	}
	else if(eu.europeana.vars.page_name == 'search-widget.html'){
		scripts.push({
			file : 'search-widget' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
		loadScripts(scripts);
	}	
}();

// end conditional load object

