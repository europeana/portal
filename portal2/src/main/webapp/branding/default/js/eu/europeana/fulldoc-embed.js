js.utils.registerNamespace( 'eu.europeana.embed' );

eu.europeana.embed = {
	
	init : function() {
		
		jQuery('#portrait, #landscape').bind( 'click', this.submitForm );
		jQuery('#color, #lines').bind( 'change', this.submitForm );
		
	},
	
	submitForm : function( e ) {
		
		jQuery('#content-embed').submit();
		js.console.log('submit');
	}
	
};

eu.europeana.embed.init();