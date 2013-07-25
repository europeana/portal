/**
 *  type.js
 *
 *  @package	js
 *  @author		dan entous <contact@pennlinepublishing.com>
 *  @created	2011-03-30 17:31 GMT +1
 *  @version	2011-06-24 07:51 GMT +1
 */

/**
 *  @package	js
 *  @author		dan entous <contact@pennlinepublishing.com>
 */
js.type = {
		
	registerNamespace : function( ns ) {
        
	    var ns_parts = ns.split( '.' ),
	    	root = window,
	    	fcn = '',
            i,
            ii;
	    
	    for ( i = 0, ii = ns_parts.length; i < ii; i++ ) {
	        
	        fcn += ns_parts[i];
	        
	        if ( typeof root[ ns_parts[i] ] == 'undefined' ) {
	            
	            root[ ns_parts[i] ] = {};
	            
	        } else if ( i == ii - 1 ) {
	            
	            throw new Error( fcn + ' already exists' );
	            
	        }
	        
	        root = root[ ns_parts[i] ];
	        fcn += '.';
	        
	    }
	    
	}
	
	

};

Function.prototype.method = function( name, func ) {
	
	if ( !this.prototype[name] ) {
		
		this.prototype[name] = func;
		return this;
		
	}
	
};