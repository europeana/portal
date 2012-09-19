/**
 *	bootstrap.js
 *
 *	@author		dan entous <contact@gmtpluosone.com>
 *	@author		andy maclean <andyjmaclean@gmail.com>
 *	@version	2012-07-12
 */
'use strict';


// Andy: proposed structure for conditional loading
// TODO: track which scripts are loaded so we don't attempt to reload the same stuff
// TODO: doesn't loader already track what has already been loaded?

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
//	  {								file : 'jquery-1.4.4.min.js',													path : eu.europeana.vars.branding + '/js/jquery/'	},
	  {	name  : 'jquery',			file : 'jquery-1.8.1.min.js',													path : eu.europeana.vars.branding + '/js/jquery/'	},
	  { 							file : 'jquery-ui-1.8.2.custom.min.js',											path : eu.europeana.vars.branding + '/js/jquery/',															dependencies : [ 'jquery' ]	},
	  {	name : 'utils',				file : 'utils' + js.min_suffix + '.js' + js.cache_helper,						path : eu.europeana.vars.branding + '/js/js/' + js.min_directory,											dependencies : [ 'jquery-ui-1.8.2.custom.min.js' ]	},
	  {	name : 'analytics',			file : 'analytics' + js.min_suffix + '.js' + js.cache_helper,					path : eu.europeana.vars.branding + '/js/com/google/analytics/' + js.min_directory,							dependencies : [ 'utils' ]	},
	  {	name : 'ajax',				file : 'ajax' + js.min_suffix + '.js' + js.cache_helper,						path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,									dependencies : [ 'utils' ]	},
	  
	  {
		  name : 'EuMenu',
		  file : 'EuMenu' + js.min_suffix + '.js' + js.cache_helper,
		  path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
		  dependencies : [ 'utils' ]
	  },
	  
	  {	name : 'header',			file : 'header' + js.min_suffix + '.js' + js.cache_helper,						path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
		dependencies : [ 'ajax' ],
		callback : function(){
			eu.europeana.header.init();
		}},
		
	  {	name : 'orientation-fix',	file : 'ios-orientationchange-fix' + js.min_suffix + '.js' + js.cache_helper,	path : eu.europeana.vars.branding + '/js/scottjehl-iOS-Orientationchange-Fix-99c9c99/' + js.min_directory	},
	  
	  {
			name : 'touchswipe',
			file : 'touch-swipe' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/jquery/' + js.min_directory,
			dependencies : [ 'utils' ]
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
			path: eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory
	  }
	  
	  
   	];

	
	// functionality common to fulldoc, index and search 
	var common = function(){
		return {
			
		};
	}();

	if(eu.europeana.vars.page_name == 'contact.html'){
		scripts.push({
			name : 'contact',
			file : 'contact' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
	}
	else if(eu.europeana.vars.page_name == 'embed'){
		scripts.push({
			name : 'embed',
			file : 'embed' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
	}
	else if(eu.europeana.vars.page_name == 'europeana-providers.html'){
		scripts.push({
			file : 'providers' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
	}
	else if(eu.europeana.vars.page_name == 'forgotPassword.html'){
		scripts.push({
			file : 'forgot-password' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
	}
	else if(eu.europeana.vars.page_name == 'full-doc.html'){

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
			dependencies : [ 'utils', 'galleria' ]
		});


		loadScripts(scripts);

		return{
			"common":common
		};
	}
	else if(eu.europeana.vars.page_name == 'index.html'){
		
		scripts.push({
			name : 'collapsible',
			file : 'collapsible' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});

		scripts.push({
			name : 'galleria',
			file : 'galleria-1.2.8' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/galleria/',
			dependencies : [ 'jquery' ]
		});

		scripts.push({
			name : 'index',
			file : 'index' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : ['collapsible', 'utils', 'galleria']
		});
				
		loadScripts(scripts);
		
		var index = function(){
			return {
				testIndex:function(){
					console.log("called testIndex");
				}
			};
		}();
		
		return{
			"common":common,
			"index":index
		};

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
	else if(eu.europeana.vars.page_name == 'register-success.html'){	// change password success
		scripts.push({
			file : 'register-success' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
		
		loadScripts(scripts);
	}
	else if(eu.europeana.vars.page_name == 'search.html'){
		
		scripts.push({
			name : 'collapsible',
			file : 'collapsible' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
		
		scripts.push({
			file : 'search' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils', 'collapsible' ]
		});

		loadScripts(scripts);

		var search = function(){
			return {
				
			};
		}();
		
		return{
			"common":common,
			"search":search
		};
	}
	else if(eu.europeana.vars.page_name == 'staticpage.html'){
		scripts.push({
			file : 'addthis' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/com/addthis/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
	}
	else if(eu.europeana.vars.page_name == 'timeline.html'){
		scripts.push({
			file : 'timeline' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
	}
	else if(eu.europeana.vars.page_name == 'search-widget.html'){
		scripts.push({
			file : 'search-widget' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		});
	}	
	
	

	// expose functions
	//alert("shouldn't see this if on index, search or fulldoc....");
	return{
		/*
		"fulldoc":fulldoc,
		"index":index,
		"search":search,
		"common":common
		*/
	};
}();

// end conditional load object

