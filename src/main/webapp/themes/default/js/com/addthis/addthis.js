/**
 *
 *	@author		dan entous <contact@gmtpluosone.com>
 *	@version	2011-10-20 08:26 GMT +1
 *	
 *	@link http://www.addthis.com/help/client-api
 *	@link http://www.addthis.com/help/client-api#rendering-decoration-classes
 *
 */
js.utils.registerNamespace( 'com.addthis' );

com.addthis = {	
	
	path : 'http://s7.addthis.com/js/250/',
	js_file : 'addthis_widget.js',
	
	
	/**
	 *	@param {string} pubid
	 *	AddThis Profile ID
	 *
	 *	@param {boolean} domready
	 *	If defined, the AddThis script will assume it's been added to the page after
	 *	the DOM is ready. Useful when dynamically adding AddThis to a page after the
	 *	latter has finished loading.
	 *	
	 *	@param {boolean} async
	 *	If defined, only core assets will be loaded. Once you're ready to load the
	 *	rest of the assets, call the function addthis.init()
	 */
	init : function( pubid, domready, async ) {
		
		pubid = pubid || null;
		domready = domready || false;
		async = async || false;
		
		js.loader.loadScripts([{
			
			file : this.getFilename( pubid, domready, async ),
			path : this.path,
			callback : com.addthis.loaded
			
		}]);
		
	},
	
	
	loaded : function() {
		
		addthis.addEventListener('addthis.ready', com.addthis.ready);
		js.console.log('addthis.ready listener added');
		addthis.addEventListener('addthis.menu.share', com.addthis.shareEvent);
		js.console.log('addthis.menu.share listener added 1');
		
	},
	
	
	ready : function() {
		
		js.console.log('addthis is ready');
		
	},
    
    
    /**
	 *	To allow the simplest possible case without JavaScript at all we support
	 *	passing in parameters to the AddThis JavaScript URL via its hash, otherwise
	 *	known as a fragment identifier. (In the URL http://www.example.com/foo#bar,
	 *	"#bar" is the hash.)
	 *	
	 *	@param {string} pubid
	 *	AddThis Profile ID
	 *
	 *	@param {boolean} domready
	 *	If defined, the AddThis script will assume it's been added to the page after
	 *	the DOM is ready. Useful when dynamically adding AddThis to a page after the
	 *	latter has finished loading.
	 *	
	 *	@param {boolean} async
	 *	If defined, only core assets will be loaded. Once you're ready to load the
	 *	rest of the assets, call the function addthis.init()
	 */
	getFilename : function( pubid, domready, async ) {
		
		var i,
            ii = arguments.length,
            extra_parameters = '',
            filename =	this.js_file;
        
        for ( i = 0; i < ii; i += 1 ) {
            
            if ( extra_parameters.length > 0 && arguments[i] ) {
            	
            	extra_parameters += '&';
            	
            }
            
            switch ( i ) {
                
                case 0 : extra_parameters += ( arguments[i] ) ? 'pubid=' + arguments[i] : ''; break;
                case 1 : extra_parameters += ( arguments[i] ) ? 'domready=1' : ''; break;
                case 2 : extra_parameters += ( arguments[i] ) ? 'async=1' : ''; break;
                
            }
            
        }
        
        if ( extra_parameters.length > 0 ) {
            
            filename += '#' + extra_parameters;
            
        }
		
		return filename;
		
	},
	
    
	/**
	 *	By default, AddThis will use the URL and title of the page being viewed.
	 *	You can however specify a different URL and Page Title for AddThis to use instead.
	 *
	 *	@param {object} options
	 *		html_class : {string}
	 *		url : {url}
	 *		title : {string}
	 *		description : {string}
	 *		services : {object}
	 */
	getToolboxHtml : function( options ) {
		
		options.html_class = options.html_class || 'addthis_toolbox addthis_default_style';
		
		var html =
			'<div class="' + options.html_class + '"' +
				( options.url ? ' addthis:url="' + options.url + '"' : '' ) +
				( options.title ? ' addthis:title="' + options.title + '"' : '' ) +
				( options.description ? ' addthis:description="' + options.description + '"' : '' ) +
			'>' +
				
				( ( options.services ) ? this.getButtons( options.services ) : '' ) +
				'<div style="clear:both;"></div>' +
				
			'</div>';
		
		return html;
		
	},
	
	

	
	getToolboxHtml_ANDY : function( options ) {
		
		options.html_class = options.html_class || 'addthis_toolbox addthis_default_style';
		
		
		var html =
			
			'<span '								+
			'style="width:100%;" '					+
			'title="get the title" '				+
			'class="' + options.html_class + '"'	+

			( options.url			? ' addthis:url="'			+ options.url + '"' : '' ) +
			( options.title			? ' addthis:title="'		+ options.title + '"' : '' ) +
			( options.description	? ' addthis:description="'	+ options.description + '"' : '' ) +
			
			'>' + 
			
			( ( options.services )	? this.getButtons( options.services, options.link_html ) : '' ) +
			
			'</span>';	
		
		return html;
		
	},
	
	
	/**
	 *	Renders a normal AddThis button at an anchor tag. If the tag has no image
	 *	in it, we load our default image. If the tag has an image in it, that image
	 *	is used as for the button graphic.
	 *
	 *	@param { dom reference, css selector string | array of } target
	 *	@param {addthis configuration object} config_object
	 *	@param {addthis sharing object} sharing_object
	 */
	addButton : function( target, config_object, sharing_object ) {
		
		config_object = this.createConfigObject( config_object );
		sharing_object = this.createShareObject( sharing_object );
		addthis.button( target, config_object, sharing_object );
		
	},
	
	
	/**
	 *	Renders an AddThis toolbox inside the given div. In normal operation, all
	 *	appropriately-classed anchors are rendered automatically through link
	 *	decoration, described below. But when building a toolbox dynamically using
	 *	JavaScript, after the page has finished loading, you can use this function
	 *	to force AddThis to update any newly decorated links. (If the div has no
	 *	appropriately-classed anchors inside it, nothing happens.)
	 *	
	 *	@param { dom reference, css selector string | array of } target
	 *	@param {addthis configuration object} config_object
	 *	@param {addthis sharing object} sharing_object
	 */
	addToolbox : function( target, config_object, sharing_object ) {
		
		config_object = this.createConfigObject( config_object );
		sharing_object = this.createShareObject( sharing_object );
		addthis.toolbox( target, config_object, sharing_object );
		
	},
	
	
	/**
	 *	Renders an AddThis share counter at the specified tag.
	 *	
	 *	@param { dom reference, css selector string | array of } target
	 *	@param { addthis configuration object } config_object
	 *	@param { addthis sharing object } sharing_object
	 */
	addCounter : function( target, config_object, sharing_object ) {
		
		config_object = this.createConfigObject( config_object );
		sharing_object = this.createShareObject( sharing_object );
		addthis.counter( target, config_object, sharing_object );
		
	},
	
	
	getButtons : function( services, link_html ) {
		
		var i = '',
			button_html = '';
		
		for ( i in services ) {
			
			if ( services.hasOwnProperty(i) ) {
				
				if ( i.search(/facebook/) > -1 ) {
					
					button_html += this.getFacebookButton( i, services[i] );
					
				} else if ( i.search(/google/) > -1 ) {
					
					button_html += this.getGoogleButton( i, services[i] );
					
				} else if ( i.search(/counter/) > -1 ) {
				
					button_html += this.getAddThisCounter( i, services[i] );
					
				} else if ( i.search(/tweet/) > -1 ) {
					
					button_html += this.getTweetButton( i, services[i] );
					
				} else {

					
					button_html +=
						'<a class="addthis_button">' +
						( services[i]['text'] ? services[i]['text'] : '' ) +
						(link_html ? link_html : '') + 
						'</a>';

					/*
	 					button_html +=
						'<a class="addthis_button_' + i + '">' +
						( services[i]['text'] ? services[i]['text'] : '' ) +
						'</a>';

					 */
				}
				
			}
			
		}
		return button_html;
		
	},
	
	
	/**
	 *	@link http://www.addthis.com/help/custom-buttons#facebook-like
	 *	fb:like:layout = button_count | box_count | standard
	 *	fb:action = recommend
	 *
	 *	@link http://www.addthis.com/help/custom-buttons#facebook-send
	*/
	getFacebookButton : function( service, options ) {
		
		var button_html = '';
		
		
		/**
		 *	TODO: could validate options
		 */
		switch ( service ) {
			
			/**
			 *	@link http://www.addthis.com/help/custom-buttons#facebook-like
			 *	@link http://developers.facebook.com/docs/reference/plugins/like/
			 *
			 *	href - the URL to like. The XFBML version defaults to the current page.
			 *	send - specifies whether to include a Send button with the Like button. This only works with the XFBML version.
			 *	layout - there are three options.
			 *		standard - displays social text to the right of the button and friends' profile photos below. Minimum width: 225 pixels. Default width: 450 pixels. Height: 35 pixels (without photos) or 80 pixels (with photos).
			 *		button_count - displays the total number of likes to the right of the button. Minimum width: 90 pixels. Default width: 90 pixels. Height: 20 pixels.
			 *		box_count - displays the total number of likes above the button. Minimum width: 55 pixels. Default width: 55 pixels. Height: 65 pixels.
			 *		< 1,6 km vdsl
			 *		adsl 2 plus
			 *
			 *	show_faces - specifies whether to display profile photos below the button (standard layout only)
			 *	width - the width of the Like button.
			 *	action - the verb to display on the button. Options: 'like', 'recommend'
			 *	font - the font to display in the button. Options: 'arial', 'lucida grande', 'segoe ui', 'tahoma', 'trebuchet ms', 'verdana'
			 *	colorscheme - the color scheme for the like button. Options: 'light', 'dark'
			 *	ref - a label for tracking referrals; must be less than 50 characters and can contain alphanumeric characters and some punctuation (currently +/=-.:_). The ref attribute causes two parameters to be added to the referrer URL when a user clicks a link from a stream story about a Like action:
			 *		fb_ref - the ref parameter
			 *		fb_source - the stream type ('home', 'profile', 'search', 'other') in which the click occurred and the story type ('oneline' or 'multiline'), concatenated with an underscore.
			 *
			 *
			 *	index page - all counts
			 *	europeana app id 4949752878
			 */
			case 'facebook_like' :
				
				button_html +=
					'<a class="addthis_button_' + service + '"' +
						( options['href'] ? ' fb:like:href="' + options['href'] + '"' : '' ) +
						( options['layout'] ? ' fb:like:layout="' + options['layout'] + '"' : '' ) +
						( options['width'] ? ' fb:like:width="' + options['width'] + '"' : '' ) +
						( options['show_faces'] ? ' fb:like:show-faces="' + options['show_faces'] + '"' : '' ) +
						( options['action'] ? ' fb:like:action="' + options['action'] + '"' : '' ) +
						( options['colorscheme'] ? ' fb:like:colorscheme="' + options['colorscheme'] + '"' : '' ) +
						( options['font'] ? ' fb:like:font="' + options['font'] + '"' : '' ) +
					'>' +
						( options['text'] ? options['text'] : '' ) +
					'</a>';
				break;
			
			
			/**
			 *	@link http://www.addthis.com/features/facebook-send-button
			 */
			case 'facebook_send' :
				
				button_html +=
					'<a class="addthis_button_' + service + '"' +
					'>' +
						( options['text'] ? options['text'] : '' ) +
					'</a>';
				break;
			
				
			default :
				
				js.console.error( '[' + service + '] is not a recognized Facebook button property' );
				break;
			
		}
		
		
		return button_html;
		
	},
	
	
	/**
	 *	g:plusone:size = medium | tall
	 *	g:plusone:count = true | false
	 *	g:plusone:annotation = bubble | inline | none
	 *	
	 *	@param service
	 *	@param options
	 *	@returns {String}
	 *
	 *	@link http://www.addthis.com/help/custom-buttons#google-plus1
	 */
	getGoogleButton : function( service, options ) {
		
		var button_html = '';		
		
		button_html +=
			'<a class="addthis_button_' + service + '"' +
				( options['size'] ? ' g:plusone:size="' + options['size'] + '"' : '' ) +
				( options['count'] ? ' g:plusone:count="' + options['count'] + '"' : '' ) +
				( options['annotation'] ? ' g:plusone:annotation="' + options['annotation'] + '"' : '' ) +
			'>' +
				( options['text'] ? options['text'] : '' ) +
			'</a>';
		
		return button_html;
		
	},
	
	
	/**
	 *	tw:count = vertical
	 *	tw:via = addthis ( use your twitter name )
	 *	tw:text = text you want to tweet
	 *		nb: option value for this field is tw_text, text is used for the <a> tag
	 *	tw:related = twitter related you want to add
	 *	tw:hashtag - a guess at what might become supported
	 *		nb: this is not supported, its a guess at what be support in the future
	 *	
	 *	@param service
	 *	@param options
	 *	@returns {String}
	 *
	 *	@link http://www.addthis.com/help/custom-buttons#tweet-button
	 */
	getTweetButton : function( service, options ) {
		
		var button_html = '';
		
		button_html +=
			'<a class="addthis_button_' + service + '"' +
				( options['count'] ? ' tw:count="' + options['count'] + '"' : '' ) +
				( options['via'] ? ' tw:via="' + options['via'] + '"' : '' ) +
				( options['tw_text'] ? ' tw:text="' + options['tw_text'] + '"' : '' ) +
				( options['related'] ? ' tw:related="' + options['related'] + '"' : '' ) +
			'>' +
				( options['text'] ? options['text'] : '' ) +
			'</a>';
		
		return button_html;
		
	},
	
	
	/**
	 * 
	 * @param service
	 * @param options
	 * @returns {String}
	 */
	getAddThisCounter : function( service, options ) {
		
		var button_html = '';
		
		button_html += 
			'<a class="addthis_counter' +
				( options['style'] ? ' ' + options['style'] : '' ) + 
			'">' +
			'</a>';
		
		return button_html;
		
		
	},
	
	/**
	*	We support two types of configuration objects: one for configuring our UI tools,
	*	and one for specifying what you're sharing.
	*
	*	UI Configuration
	*	The same object format can be used for specifying global or instance configuration.
	*	To specify global configuration, the special name addthis_config must be used.
	*
	*	@link http://www.addthis.com/help/client-api
	*
	*	@returns {boolean | object}
	*	returns boolean false if supplied properties are not members of addthis_config
	*	or an addthis_config object merged with the supplied properties; any properties
	*	left as undefined are left out of the returned object
	*/
	createConfigObject : function( properties ) {
		
		var addthis_config = {
			
		   /**
			*	@var {string} pubid
			*	
			*	Your AddThis Profile ID. Always global to a page.
			*/
		   pubid : undefined,
		   
		   
		   /**
			*	@var {string (csv)} services_exclude
			*	@link http://www.addthis.com/services/list
			*	
			*	Services to exclude from all menus. For example, setting this to
			*	'facebook,myspace' would hide Facebook and MySpace on all our menus.
			*	Always global.
			*/
		   services_exclude : undefined,
		   
		   
		   /**
			*	@var {string (csv)} services_expanded
			*	@link http://www.addthis.com/services/list
			*
			*	Services to use in the compact menu. For example, setting this to '
			*	print,email,favorites' would result in only those three services appearing.
			*	Always global.
			*	
			*	default: We regularly optimize the default list based on our data.
			*/
		   services_compact : undefined,
		   
		   
		   /**
			*	@var {string (csv)} services_expanded
			*	@link http://www.addthis.com/services/list
			*
			*	Services to use in the expanded menu. Useful if very few services are desired
			*	-- specifying a long list via services_exclude could be tiresome, and wouldn't
			*	catch a new service added later. For example, setting this to
			*	'bebo,misterwong,netvibes' would result in only those three services appearing
			*	in the expanded menu. Always global.
			*
			*	Specify your own AddThis bookmarking service like so: {name: "My Service",
			*	url: "http://share.example.com?url={{URL}}&title={{TITLE}}",
			*	icon: "http://example.com/icon.jpg"}. All three fields must be present for
			*	each custom service. Always global. Sample usage.
			*	
			*	default : all the services AddThis offers
			*/
		   services_expanded : undefined,
		   
		   
		   /**
			*	@var {array} services_custom
			*	@link http://www.addthis.com/help/custom-services
			*
			*	Analytics will appear in your account under the service's base domain name
			*	(no subdomains--"example.com" in this case), and the first service specified
			*	will appear in the compact menu unless services_compact is specified. Only one
			*	service per domain is accepted. All custom services specified automatically get
			*	added to the expanded menu.
			*
			*	To include a custom service with services_expanded or services_compact, refer to
			*	it by the service's full domain name as in the analytics console. (In this case,
			*	"share.example.com".) 
			*/
		   services_custom : undefined,
		   
		   
		   /**
			*	@var {boolean} ui_click
			*
			*	If true, the compact menu will never appear upon mousing over the regular button.
			*	Instead, it will be revealed upon clicking the button.
			*	
			*	default : false
			*/
		   ui_click : undefined,
		   
		   
		   /**
			*	@var {integer} ui_delay
			*
			*	Delay, in milliseconds, before compact menu appears when mousing over a
			*	regular button. Capped at 500 ms.
			*	
			*	default : 0
			*/
		   ui_delay : undefined,
		   
		   
		   /**
			*	@var {integer} ui_hover_direction
			*
			*	Normally, we show the compact menu in the direction of the user's browser
			*	that has the most space (i.e., a button at the bottom of the page will pop up,
			*	and vice versa). You can override this behavior with this setting: 1 to force
			*	the menu to appear "up", -1 to force the menu to appear "down".
			*
			*	default : 0
			*/
		   ui_hover_direction : undefined,
		   
		   
		   /**
			*	@var {boolean} ui_open_windows
			*
			*	If true, all shares will open in a new pop-up window instead of a new tab or
			*	regular browser window.
			*
			*	default : false
			*/
		   ui_open_windows : undefined,
		   
		   
		   /**
			*	@var {string} ui_language
			*
			*	For forcing the menu to use a particular language, specified via ISO code.
			*	For example, setting this to "sv" would show the menu in Swedish. Note:
			*	Regardless of the number of times it's specified, only one language is
			*	supported per page.
			*
			*	default : user's browser
			*/
		   ui_language : undefined,
		   
		   
		   /**
			*	@var {integer} ui_offset_top
			*
			*	Number of pixels to offset the top of the compact menu from its parent element
			*
			*	default : 0
			*/
		   ui_offset_top : undefined,
		   
		   
		   /**
			*	@var {integer} ui_offset_left
			*
			*	Number of pixels to offset the left of the compact menu from its parent element
			*
			*	default : 0
			*/
		   ui_offset_left : undefined,
		   
		   
		   /**
			*	@var {hex string} ui_header_color
			*
			*	Color of the compact and extended menus' header foregrounds. For example,
			*	"#FFF" would make the text white.
			*/
		   ui_header_color : undefined,
		   
		   
		   /**
			*	@var {hex string} ui_header_background
			*
			*	Color of the compact and extended menus' header backgrounds. For example,
			*	"#000" would make the text appear on a black background.
			*/
		   ui_header_background : undefined,
		   
		   
		   /**
			*	@var {string} ui_cobrand
			*
			*	Additional branding message to be rendered in the upper-right-hand corner
			*	of the menus. Should be less than 15 characters in most cases to render properly.
			*/
		   ui_cobrand : undefined,
		   
		   
		   /**
			*	@var {boolean} ui_use_css
			*
			*	If false, we will not load our standard CSS file, allowing you to style everything
			*	yourself without incurring the cost of an additonal load. Always global; must be
			*	defined in a page-level global variable.
			*
			*	default : true
			*/
		   ui_use_css : undefined,
		   
		   
		   /**
			*	@var {boolean} ui_use_addressbook
			*
			*	If true, the user will be able import their contacts from popular webmail services
			*	when using AddThis's email sharing.
			*
			*	default : false
			*/
		   ui_use_addressbook : undefined,
		   
		   
		   /**
			*	@var {boolean} ui_508_compliant
			*
			*	If true, clicking the AddThis button will open a new window to a page that is keyboard
			*	navigable.
			*/
		   ui_508_compliant : undefined,
		   
		   
		   /**
			*	@var {boolean} data_track_clickbackaddthis_congi
			*
			*	Set to true to allow us to append a variable to your URLs upon sharing. We'll use this
			*	to track how many people come back to your content via links shared with AddThis. Highly
			*	recommended. Always global.
			*
			*	default : false
			*/
		   data_track_clickback : undefined,
		   
		   
		   /**
			*	@var {object | string} data_ga_tracker
			*	@var http://www.addthis.com/help/google-analytics-integration
			*
			*	Google Analytics tracking object, or the name of a global variable that references it.
			*	If set, we'll send AddThis tracking events to Google, so you can have integrated reporting.
			*	Sample usage.
			*
			*	default : null
			*/
		   data_ga_tracker : undefined,
		   
		   
		   /**
			*	@var {string} data_ga_property
			*	@link http://www.addthis.com/help/google-analytics-integration
			*
			*	Once Google analytics is installed on your pages, you can send AddThis shares to your Google
			*	Analytics reports as custom events in the category addthis by adding the following configuration
			*	lines to your existing AddThis code. You'll need your GA property ID (generally a serial number
			*	of the form UA-xxxxxx-x).
			*/
		   data_ga_property : undefined,
		   
		   
		   /**
			*	@var {boolean} data_ga_social
			*	
			*	If you're using the latest version of GA, you can take advantage of Google's new social interaction
			*	analytics. You don't even have to write _trackSocial hooks. Just opt in with data_ga_social
			*
			*	When using the modern social methodology, we track the network (e.g. "facebook"), the social action
			*	(e.g. "share"), and the target (e.g., http://example.com/a/cool/blog).
			*/
		   data_ga_social : undefined
		   
		   
	   };
	   
	   if ( !js.utils.objectHasProperties( addthis_config, properties ) ) {
		   
		   return false;
		   
	   }
	   
	   return js.utils.extend( true, {}, addthis_config, properties );
	   
   },
   
   
   /**
	*	We support two types of configuration objects: one for configuring our UI tools,
	*	and one for specifying what you're sharing.
	*
	*	Sharing Configuration
	*
	*	The same object format can be used for specifying global or instance configuration. To specify
	*	global configuration, the special name addthis_share must be used.
	*
	*	@returns {boolean | object}
	*	returns boolean false if supplied properties are not members of addthis_config
	*	or an addthis_config object merged with the supplied properties; any properties
	*	left as undefined are not part of the returned object
	*
	*	@link http://www.addthis.com/help/client-api
	*/
	createShareObject : function( properties ) {
		
		var addthis_share = {
			
			/**
			 *	@var {url} url
			 *	@link http://www.addthis.com/help/url-and-title
			 *
			 *	URL to use if not the current page. This is helpful when you have an AddThis button on
			 *	multiple articles on the same page.
			 *
			 *	default : window URL
			*/
			url : undefined,
			
			
			/**
			 *	@var {string} title
			 *	@link http://www.addthis.com/help/url-and-title
			 *
			 *	This is an alternate title.
			 *
			 *	default : window title
			*/
			title : undefined,
			
			
			/**
			 *	@var {string} description
			 *	@link http://www.addthis.com/help/url-and-title
			 *
			 *	description of shared object
			*/
			description : undefined,
			
			/**
			 *	@var {string} swfurl
			 *	@link http://www.addthis.com/help/client-api#embedded-content
			 *
			 *	URL of a Flash object to share, along with the link
			*/
			swfurl : undefined,
			
			
			/**
			 *	@var {integer} width
			 *	@link http://www.addthis.com/help/client-api#embedded-content
			 *
			 *	ideal width of any provided Flash object
			*/
			width : undefined,
			
			
			/**
			 *	@var {integer} height
			 *	@link http://www.addthis.com/help/client-api#embedded-content
			 *
			 *	ideal height of any provide Flash object
			*/
			height : undefined,
			
			
			/**
			 *	@var {string} email_template
			 *	@link http://www.addthis.com/help/email-templates
			 *
			 *	name of template to use for emailed shares
			*/
			email_template : undefined,
			
			
			/**
			 *	@var {object} email_vars
			 *	@link http://www.addthis.com/help/email-templates
			 *
			 *	associative array mapping custom email variables to values 
			*/
			email_vars : undefined,
			
			
			/**
			 *	@var {object} templates
			 *	@link http://www.addthis.com/help/client-api#configuration-sharing-templates
			 *
			 *	associative array mapping services to post templates
			*/
			templates : undefined		   
		
		};
	   
		if ( !js.utils.objectHasProperties( addthis_share, properties ) ) {
		
			return false;
		
		}
	   
	   return js.utils.extend( true, {}, addthis_share, properties );
	   
   }
	
};
