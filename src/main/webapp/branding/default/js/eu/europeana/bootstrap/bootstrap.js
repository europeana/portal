/**
 *	bootstrap.js
 *
 *	@author		dan entous <contact@gmtpluosone.com>
 *	@author		andy maclean <andyjmaclean@gmail.com>
 *	@version	2012-07-12
 */
'use strict';


// Andy: proposed structure for conditional loading

var europeana_bootstrap = function(){
	
	// loads the loader which then loads the scripts
	function loadScripts(scripts){
		var script = document.createElement('script');
		script.src = eu.europeana.vars.branding + '/js/js/' + js.min_directory + 'loader' + js.min_suffix + '.js' + js.cache_helper;
		if ( 'onload' in document || 'addEventListener' in window ) {
			script.onload = function() { js.loader.loadScripts( scripts ); };
		}
		else if ( 'onreadystatechange' in document ) {	
			script.onreadystatechange = function () {
				if ( script.readyState == 'loaded' || script.readyState == 'complete' ) {
					js.loader.loadScripts( scripts );
				}
			};
		}	
		document.getElementsByTagName('body')[0].appendChild( script );		
	}

	// function common to index, search and fulldoc
	function loadResultSizer(callback){
		loadScripts(
			[{
				name : 'result-size',
				file : 'result-size' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory
				//,dependencies : [ 'jquery-1.4.4.min.js' ]
				,callback : callback
			}]
		);
	}
	
	// functionality for index.jsp
	var index = function(){
		return {
			loadResultSizer:function(callback){
				loadResultSizer(callback);
			},
			b:function(){
				alert("called b");
			}
		};
	}();

	// functionality for fulldoc.jsp 
	var fulldoc = function(){
		return {
			loadResultSizer:function(){
				loadResultSizer();
			},
			b:function(){
			}
		};
	}();

	// functionality for search.jsp 
	var search = function(){
		return {
			loadResultSizer:function(){
				loadResultSizer();
			}
		};
	}();


	// return functions
	
	return{
		x:function(){},
		y:function(){},
		"fulldoc":fulldoc,
		"index":index,
		"search":search
	};
}();

// end conditional load object




(function() {
	
	var body = document.getElementsByTagName('body')[0],
		script,
		scripts;
	
		if ( !window.eu ) { throw new Error( 'window.eu was not defined before bootstrap' ); }
		if ( !window.js ) { throw new Error( 'window.js was not defined before bootstrap' ); }
	

	/**
	 *	development scripts, non-minified when possible
	 *	core scripts for all pages
	 */		
	scripts = [

   		{
			file : 'jquery-1.4.4.min.js',
			path : eu.europeana.vars.branding + '/js/jquery/'
		},

		{
			file : 'jquery-ui-1.8.2.custom.min.js',
			path : eu.europeana.vars.branding + '/js/jquery/',
			dependencies : [ 'jquery-1.4.4.min.js' ]
		},
		
		{
			name : 'utils',
			file : 'utils' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/js/' + js.min_directory,
			dependencies : [ 'jquery-ui-1.8.2.custom.min.js' ]
		},
		
		{
			name : 'analytics',
			file : 'analytics' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/com/google/analytics/' + js.min_directory,
			dependencies : [ 'utils' ]
		},
		
		{
			name : 'ajax',
			file : 'ajax' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'utils' ]
		},
		
		{
			name : 'header',
			file : 'header' + js.min_suffix + '.js' + js.cache_helper,
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'ajax' ]
		}
		
	];
	
	switch ( eu.europeana.vars.page_name ) {
			
		case 'contact.html' :
			
			scripts.push({
				name : 'contact',
				file : 'contact' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'utils' ]
			});
			
			break;
			
			
		case 'embed' :
			
			scripts.push({
				name : 'embed',
				file : 'embed' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'utils' ]
			});
			
			break;
		
		
		case 'europeana-providers.html' :
			
			scripts.push({
				file : 'providers' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'utils' ]
			});
			
			break;
			
		
		case 'forgotPassword.html' :
			
			scripts.push({
				file : 'forgot-password' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'utils' ]
			});
			
			break;
			
			
		case 'full-doc.html' :
			scripts.push({
				name : 'fulldoc',
				file : 'fulldoc' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'utils' ]
			});
			
			break;
		
			
		case 'index.html' :
			//alert("bootstrap to load index.js");
			scripts.push({
				name : 'index',
				file : 'index' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'utils' ]
			});
			
			break;
			
			
		case 'login.html' :
			
			scripts.push({
				file : 'login' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'utils' ]
			});
			
			break;
		

		case 'map.html' :
			
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
			
			break;
			
			
		case 'myeuropeana.html' :
			scripts.push({
				name : 'myeuropeana',
				file : 'myeuropeana' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'utils' ]
			});
			
			break;
		
		
		case 'register-success.html' : // change password success
			
			scripts.push({
				file : 'register-success' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'utils' ]
			});
			
			break;
			
			
		case 'search.html' :
			
			scripts.push({
				file : 'search' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'utils' ]
			});
			
			scripts.push({
				name : 'result-size',
				file : 'result-size' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'jquery-1.4.4.min.js' ]
			});

			break;
			
			
		case 'staticpage.html' :
			
			scripts.push({
				file : 'addthis' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/com/addthis/' + js.min_directory,
				dependencies : [ 'utils' ]
			});
			
			break;
			
			
		case 'timeline.html' :
			
			scripts.push({
				file : 'timeline' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'utils' ]
			});
			
			break;
			
		case 'search-widget.html' :
			scripts.push({
				file : 'search-widget' + js.min_suffix + '.js' + js.cache_helper,
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'utils' ]
			});
			
			break;
			
	}

	script = document.createElement('script');
	script.src = eu.europeana.vars.branding + '/js/js/' + js.min_directory + 'loader' + js.min_suffix + '.js' + js.cache_helper;
	
	if ( 'onload' in document || 'addEventListener' in window ) {
		
		script.onload = function() { js.loader.loadScripts( scripts ); };
		
	} else if ( 'onreadystatechange' in document ) {
		
		script.onreadystatechange = function () {
			
			if ( script.readyState == 'loaded' || script.readyState == 'complete' ) {
			
				js.loader.loadScripts( scripts );
				
			}
			
		};
		
	}	
	
	body.appendChild( script );
	
})();

