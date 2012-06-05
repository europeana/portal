/**
 *  loader.js
 *
 *  @package	js
 *  @author		dan entous <contact@pennlinepublishing.com>
 *  @created	2011-03-30 16:13 GMT +1
 *  @version	2011-07-05 17:44 GMT +1
 */

/**
 *  @package	js
 *  @author		dan entous <contact@pennlinepublishing.com>
 */

js.loader = {
	
	loader_status : {},
	check_interval : 50,
	load_attempt_limit : 150,
	
	/**
	 *
	 *	@param {array} scripts
	 *	an array of script objects to be loaded
	 *
	 *	var scripts = [{
	 *		file: 'script-filname.js',
	 *		path: '/path/to/script/',
	 *		dom_location : document.getElementsByTagName('body')[0], [optional]
	 *		callback : eu.europeana.timeline.handleOnLoad [optional]
	 *	}]	
	 */
	loadScripts : function( scripts ) {
		
		var i;
		
		for ( i in scripts ) {
			
			scripts[i].name = scripts[i].name || scripts[i].file;
			
			this.loader_status[ scripts[i].name ] = {
				
				load_attempts : 0,
				placed_in_dom : false,
				script_loaded : false,
				interval : null
				
			};
			
			this.checkScriptDependencies( scripts[i] );
			
		}
		
	},
	
	
	/**
	 *	send scripts through the loadScripts method
	 */
	loadScript : function( script ) {
		
		if ( !script || this.loader_status[ script.name ].placed_in_dom ) { return; }
		
		var src,
			script_to_load,
			dom_location = 
				script.dom_location
				|| document.getElementsByTagName('body')[0];
		
		script_to_load = document.createElement('script');
		script_to_load.src = script.path + script.file;
		script_to_load.onload = function() { js.loader.updateLoaderStatus( script ); };
		script_to_load.onreadystatechange = function () { if ( script_to_load.readyState == 'loaded' || script_to_load.readyState == 'complete' ) { js.loader.updateLoaderStatus( script ); } }; 
		script_to_load.onerror = function() { js.loader.reportLoadError( script ); };
			
		if ( js.debug ) { console.log( 'load : ' + script.file + ', ' + script.path ); }
		
		if ( dom_location.appendChild( script_to_load ) ) {
			
			this.loader_status[ script.name ].placed_in_dom = true;
			
		}
		
	},
	
	
	checkScriptDependencies : function( script ) {
		
		if ( !script ) { return; }
		
		var i,
			interval,
			dependencies_fulfilled = true;
			
		if ( script.dependencies && script.dependencies.length > 0 ) {
			
			for ( i in script.dependencies ) {
			
				if ( !this.loader_status[ script.dependencies[i] ]
					 || !this.loader_status[ script.dependencies[i] ].script_loaded ) {
					
					this.loader_status[ script.name ].load_attempts += 1;
					dependencies_fulfilled = false;
					
					if ( this.load_attempt_limit <= this.loader_status[ script.name ].load_attempts ) {
						
						clearInterval( this.loader_status[ script.name ].interval );
						console.error(
							'tried to load script : ' + script.dependencies[i] + 
							' ' + this.load_attempt_limit + ' times without success'
						);
						break;
						
					}

					if ( !this.loader_status[ script.name ].interval ) {
						
						interval =
							setInterval(
								function() { js.loader.checkScriptDependencies( script ); },
								this.check_interval
							);
						
						this.loader_status[ script.name ].interval = interval;
							
					}
					
					break;
					
				}
				
			}
			
		}
		
		if ( dependencies_fulfilled ) {
			
			this.loadScript( script );
			
		}
		
	},
	
	
	updateLoaderStatus : function( script ) {
		
		if ( !script ) { return; }
		
		if ( this.loader_status[ script.name ].interval ) {
			
			if ( js.debug ) { console.log( 'loaded : ' + script.name ); }
			clearInterval( this.loader_status[ script.name ].interval );
			this.loader_status[ script.name ].interval = null;
			
		}
		
		this.loader_status[ script.name ].script_loaded = true;
		if ( script.callback ) { script.callback(); }
		
	},
	
	
	reportLoadError : function( script ) {
		
		console.error( 'could not load : ' + script.path + script.file );
		
	}
	
};