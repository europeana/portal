/**
 *  ajax.js
 *
 *  @package	eu.europeana
 *  @author		dan entous <contact@gmtpluosone.com>
 *  @created	2011-07-12 09:42 GMT +1
 *  @version	2011-07-12 10:42 GMT +1
 */

/**
 * 	establish an object for common ajax methods with
 * 	private methods and properties
 * 
 *  @package	eu.europeana
 *  @author		dan entous <contact@gmtplusone.com>
 */
js.utils.registerNamespace( 'eu.europeana.ajax' );

eu.europeana.ajax = function( options ) {
	
	var default_options = {
			
			feedback_container : '#ajax-feedback',
			feedback_container_html : '<div id="ajax-feedback"></div>',
			feedback_container_add_to : '.container',
			feedback_delay_default : 700,
			feedback_delay_max : 7000,
			feedback_delay_incrementor : 50
			
		},
		
		properties = {
			
			ajax_url_prefix : eu.europeana.vars.homeUrl + '/'
			
		};
	
	
	this.options = jQuery.extend( true, {}, default_options, options );
	
	
	this.init = function() {
		
		this.addFeedbackContainer();
		properties.initialized = true;
		
	};
	
	
	/**
	 *	@param action {string}
	 *	save | remove
	 *
	 *	@param ajax_data {object}
	 *	{ "name" : "value" } of the parameters to be sent with the ajax post
	 *
	 *	@param ajax_feedback {object}
	 *	{ success : function() {}, failure : function() {} }
	 *	functions to be called upon success/failure of the ajax call
	 */
	eu.europeana.ajax.prototype.user_panel = function( action, ajax_data, ajax_feedback ) {
		
		var self = this,
			ajax_url = properties.ajax_url_prefix;
		
		if ( !properties.initialized ) { this.init(); }
		
		if ( !ajax_data || 'object' != typeof ajax_data ) {
			
			js.console.error('ajax_data was invalid');
			return;
			
		}
		
	    
	    
		switch ( action ) {
		
			case 'save' :
				ajax_url += 'save.ajax';
				break;
				
				
			case 'remove' :
				ajax_url += 'remove.ajax';
				break;
				
			
			default :
				js.console.error('ajax action was invalid');
				return;
				break;
		
		}
		
		
	    jQuery.ajax({
	    	
	       type: 'POST',
	       url: ajax_url,
	       data: ajax_data,
	       dataType : 'json',
	       
	       success: function( msg ) {
	    	   
	    	   self.feedback( 'success', ajax_data, ajax_feedback, msg );	    	   
	           
	       },
	       
	       error: function( msg ) {
	    	   
	    	   self.feedback( 'failed', ajax_data, ajax_feedback, msg );
	    	   
	       }
	       
	     });
	    
	};
	
	
	eu.europeana.ajax.prototype.feedback = function( ajax_status, ajax_data, ajax_feedback, msg ) {
		
		
		if ( 'success' == ajax_status && msg.reply.success ) {
		
			ajax_feedback.success(msg);
			
		} else {
			
			ajax_feedback.failure();
			
		}
		
	};
	
	
	eu.europeana.ajax.prototype.addFeedbackContainer = function() {
		
		if ( jQuery( this.options.feedback_container ).length < 1 ) {
			
			jQuery( this.options.feedback_container_add_to ).prepend( this.options.feedback_container_html );
			
		}
		
	};
	
	
	eu.europeana.ajax.prototype.addFeedbackContent = function( content ) {
		
		jQuery( this.options.feedback_container ).html( content );
		
	};
	
	
	eu.europeana.ajax.prototype.getFeedbackContainerDelay = function() {
		var container_html = jQuery( this.options.feedback_container ).html(),
			container_string_length = container_html.length,
			delay = this.options.feedback_delay_incrementor * container_string_length + this.options.feedback_delay_default;
		
		if ( delay > this.options.feedback_delay_max ) { delay = this.options.feedback_delay_max; }
		
		return parseInt( delay, 10 );
		
	};
	
	
	eu.europeana.ajax.prototype.showFeedbackContainer = function() {
		
		jQuery( this.options.feedback_container )
			.stop(true)
			.fadeIn()
			.delay( this.getFeedbackContainerDelay() )
			.fadeOut();
		
	};
	
};