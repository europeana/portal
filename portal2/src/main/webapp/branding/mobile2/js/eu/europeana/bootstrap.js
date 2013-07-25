(function() {
	
	'use strict';
	
	var body = document.getElementsByTagName('body')[0],
		script,
		scripts;
	
		if ( !window.eu ) { throw new Error( 'window.eu was not defined before bootstrap' ); }
		if ( !window.js ) { throw new Error( 'window.js was not defined before bootstrap' ); }

	
	scripts = [
   		{
			file : 'jquery-1.4.4.min.js',
			path : eu.europeana.vars.branding + '/js/jquery/'
		},
		
		{
			name : 'type',
			file : 'type.js',
			path : eu.europeana.vars.branding + '/js/js/',
			dependencies : [ 'jquery-1.4.4.min.js' ]
		},
		
		{
			file : 'utilities' + js.min_suffix + '.js',
			path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
			dependencies : [ 'type' ]
		}
		
	];

	switch ( eu.europeana.vars.page_name ) {
		
		case 'index.html' :
			
			scripts.push({
				file : 'utilities' + js.min_suffix + '.js',
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'type' ]
			});
			
			break;
		
		case 'contact.html' :
			
			scripts.push({
				file : 'utilities' + js.min_suffix + '.js',
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'type' ]
			});
			
			break;
			
		case 'full-doc.html' :
			
			scripts.push({
				file : 'utilities' + js.min_suffix + '.js',
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'type' ]
			});
			
			break;
			
		case 'search.html' :
			
			scripts.push({
				name : 'toggle-elements',
				file : 'toggle-elements' + js.min_suffix + '.js',
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'type' ]
			});
			
			scripts.push({
				file : 'results' + js.min_suffix + '.js',
				path : eu.europeana.vars.branding + '/js/eu/europeana/' + js.min_directory,
				dependencies : [ 'toggle-elements' ]
			});
			
			break;
			
		default:
			//alert("bootstrap can't find " + eu.europeana.vars.page_name + ", js.min_directory = " + js.min_directory);
		
	}
	
	script = document.createElement('script');	
	script.src = eu.europeana.vars.branding + '/js/js/' + js.min_directory + 'loader' + js.min_suffix + '.js';
	script.onload = function() { js.loader.loadScripts( scripts ); };
	script.onreadystatechange = function () { if (script.readyState == 'loaded' || script.readyState == 'complete') { js.loader.loadScripts( scripts ); } }; 
	body.appendChild( script );
	
}());