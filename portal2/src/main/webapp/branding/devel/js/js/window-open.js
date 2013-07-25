/**
 *  window-open.js
 *
 *  @package	js
 *  @author		dan entous <contact@pennlinepublishing.com>
 *  @created	2011-03-30 17:31 GMT +1
 *  @version	2011-07-25 15:30 GMT +1
 */

/**
 *  @package	js
 *  @author		dan entous <contact@pennlinepublishing.com>
 */

js.open = {
	
	//options : {},
		
	default_options : {
		
		/**
		 *	Optional.
		 *	Specifies the URL of the page to open. If no URL is specified, a new window with about:blank is opened
		 */
		url : '',
		
		
		/**
		 *	Optional.
		 *	Specifies the target attribute or the name of the window. The following values are supported:
		 *
		 *	_blank - URL is loaded into a new window. This is default
		 *	_parent - URL is loaded into the parent frame
		 *	_self - URL replaces the current page
		 *	_top - URL replaces any framesets that may be loaded
		 *	name - The name of the window
		 */
		name : '',
		
		
		/**
		 *	Optional
		 *	 A comma-separated list of items. The following values are supported:
		 */
		specs : {
			
			/**
			 *	channelmode=yes|no|1|0
			 *	Whether or not to display the window in theater mode. Default is no. IE only
			 */
			channelmode : null,
			
			/**
			 *	directories=yes|no|1|0
			 *	Whether or not to add directory buttons. Default is yes. IE only
			 */
			directories : null,
			
			/**
			 *	fullscreen=yes|no|1|0
			 *	Whether or not to display the browser in full-screen mode. Default is no. A window in full-screen mode must also be in theater mode. IE only
			 */
			fullscreen : null,
			
			/**
			 *	height=pixels
			 *	The height of the window. Min. value is 100
			 */
			height : null,
			
			/**
			 *	left=pixels
			 *	The left position of the window
			 */
			left : null,
			
			/**
			 *	location=yes|no|1|0
			 *	Whether or not to display the address field. Default is yes
			 */
			location : null,
			
			/**
			 *	menubar=yes|no|1|0
			 *	Whether or not to display the menu bar. Default is yes
			 */
			menubar : null,
			
			/**
			 *	resizable=yes|no|1|0
			 *	Whether or not the window is resizable. Default is yes
			 */
			resizeable : null,
			
			/**
			 *	scrollbars=yes|no|1|0
			 *	Whether or not to display scroll bars. Default is yes
			 */
			scrollbars : null,
			
			/**
			 *	status=yes|no|1|0
			 *	Whether or not to add a status bar. Default is yes
			 */
			status : null,
			
			/**
			 *	titlebar=yes|no|1|0
			 *	Whether or not to display the title bar. Ignored unless the calling application is an HTML Application or a trusted dialog box. Default is yes
			 */
			titlebar : null,
			
			/**
			 *	toolbar=yes|no|1|0
			 *	Whether or not to display the browser toolbar. Default is yes
			 */
			toolbar : null,
			
			/**
			 *	top=pixels
			 *	The top position of the window. IE only
			 */
			top : null,
			
			/**
			 *	width=pixels
			 *	The width of the window. Min. value is 100
			 */
			width : null
			
		},
		
		
		/**
		 *	Optional.
		 *	Specifies whether the URL creates a new entry or replaces the current entry in the history list. The following values are supported:
		 *	true - URL replaces the current document in the history list
		 *	false - URL creates a new entry in the history list
		 */
		replace : false
	
	},
	
	
	openWindow : function( options ) {
		
		this.options = jQuery.extend( true, {}, this.default_options, options );
		window.open ( this.options.url, this.options.name, eval( "'" + this.getSpecs() + "'" ) );
		
	},
	
	
	getSpecs : function() {
		
		var i = 0,
			specs = '';
		
		for ( i in this.options.specs ) {
			
			if ( this.options.specs[i] !== null ) {
				
				specs += i + '=' + this.options.specs[i] + ',';
				
			}
			
		}
		
		if ( specs.length > 1 ) {
			
			specs = specs.slice( 0, -1 );
			
		}
		
		return specs;
		
	}
	
};