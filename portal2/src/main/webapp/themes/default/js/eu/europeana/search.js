js.utils.registerNamespace( 'eu.europeana.search' );

/* IE10 fix */
if( !(window.ActiveXObject) && "ActiveXObject" ){
	$('.submit-new-keyword').parent().css('vertical-align', 'top');	
}

eu.europeana.search = {
	
	facet_sections : [],
	init : function() {
				
		// fix firefox' habit of creating invalid form states by remembering old checked values on refresh & page back 
		$('#filter-search li ul li input:checkbox').each(function(i, ob){
			if( ob.checked && !ob.getAttribute("checked")){
				ob.checked = false;
			}
		});

		// make facet sections collapsible
		$("#filter-search>li").each(function(i, ob){

			var headingSelector		= "h3 a";
			var headingSelected		= $(ob).find(headingSelector);
			var fnGetItems			= function(){
				
				// function to get the tabbable items
				if( headingSelected.parent().next('form').length ){
					// Add keywords
					return headingSelected.parent().next('form').find('input[type!="hidden"]');
				}
				else{
					// Other facets
					return headingSelected.parent().next('ul').first().find('a');
				}							
			};
			
			var accessibility =  new EuAccessibility(
				headingSelected,
				fnGetItems
			);
			
			if($(ob).hasClass('ugc-li')){
				$(ob).bind('keypress', accessibility.keyPress);
			}
			else{
				$(ob).Collapsible(
					{
						"headingSelector"	: "h3 a",
						"bodySelector"		: "ul",
						"keyHandler"		: accessibility
					}
				);				
			}
		});
		
		$('#refine-search-form').submit(function(){
			return eu.europeana.search.checkKeywordSupplied();
		});
		
		// make facet checkboxes clickable
		$("#filter-search li input[type='checkbox']").click(function(){
			var label = $("#filter-search li label[for='" + $(this).attr('id') + "']");
			window.location = label.closest("a").attr("href");
		});

		this.setupResultSizeMenu();
		this.setupEllipsis();
		
		
		$.each($('.result-pagination'), function(i, ob){
			
			new EuPagination( $(ob),
				{
					data:{
					records: eu.europeana.vars.msg.result_count,
					rows: parseInt(eu.europeana.vars.rows),
					start: eu.europeana.vars.msg.start
					} 
				});
		});
		
		if( $('#save-search').hasClass('icon-unsaveditem')){
			$('#save-search').bind('click', this.handleSaveSearchClick );			
		}
		else{
			$('#save-search').css('cursor', 'default');
		}		

		
		// accessibility for grid
		
		$('#items .thumb-frame').bind('keypress', 
			function(e){
				if(e.keyCode == 13){
					$(e.target).find('a')[0].click();
				}
			}
		);
		
		// add this
		this.addThis();
	},
	
	setupEllipsis : function(){
		// add ellipsis
		var ellipsisObjects = [];
		$('.ellipsis').each(
				function(i, ob){
					var fixed	= $(ob).find('.fixed');
					var html	= fixed.html();
					fixed.remove();
					ellipsisObjects[ellipsisObjects.length] = new Ellipsis(
								$(ob),
								{fixed:	'<span class="fixed">' + html + '</span>'},
								function($ob){
									var imgThumb = $(ob).parent().prev();
									imgThumb.css('border-style', 'solid solid none');
									imgThumb.css('border-width', '1px 1px medium');
									$ob.css('visibility', 'visible');
								}
							);					
				}
		);
		$(window).euRsz(function(){
			for(var i=0; i<ellipsisObjects.length; i++ ){
				ellipsisObjects[i].respond();
			}
		});

	},
	
	
	setupResultSizeMenu : function(){
		var config = {
			"fn_init": function(self){
				self.setActive( $("#query-search input[name=rows]").val() );
			},
			"fn_item":function(self, selected){
				window.location.href = window.location.href.replace(/([?&])rows=\d{2}/, '$1rows=' + selected)
			}
		};
		new EuMenu( $(".nav-top .eu-menu"), config).init();
		new EuMenu( $(".nav-bottom .eu-menu"), config).init();
	},
	

	addThis : function() {
		$('.shares-link').click(function(){
			
			js.loader.loadScripts([{
				file: 'addthis' + js.min_suffix + '.js' + '?' + 'domready=1', //&async=1',
				path: eu.europeana.vars.branding + '/js/com/addthis/' + js.min_directory,
				callback : function() {

					$('.shares-link').unbind('click');
						
					var url = $('head link[rel="canonical"]').attr('href'),
					title = $('head title').html(),
					description = $('head meta[name="description"]').attr('content');
					window.addthis_config = com.addthis.createConfigObject({
						pubid : eu.europeana.vars.addthis_pubid,
						ui_language: 'en', // eu.europeana.vars.locale,
						data_ga_property: eu.europeana.vars.gaId,
						data_ga_social : true,
						data_track_clickback: true,
						ui_use_css : true,
						ui_click: true		// disable hover
					});
					
					url = $('head meta[property="og:url"]').attr('content');
					window.addthis_share = com.addthis.createShareObject({
						// nb: twitter templates will soon be deprecated, no date is given
						// @link http://www.addthis.com/help/client-api#configuration-sharing-templates
						templates: { twitter: title + ': ' + url + ' #europeana' }
					});
					
					var addThisHtml = com.addthis.getToolboxHtml({
						html_class : '',
						url : url,
						title : title,
						description : description,
						services : {
							compact : {}
						},
						link_html : $('.shares-link').html()
					});

					$('.shares-link').html(
						addThisHtml
					);
					
					function addthisReady(evt) {
						try{
					        var oEvent = document.createEvent('HTMLEvents');
					        oEvent.initEvent('click', true, true);
					        $('.addthis_button')[0].dispatchEvent(oEvent);

						}
						catch(e){
							//alert("error [a] " + e);
						}
					}

					com.addthis.init( null, true, false,
						function(){
							addthis.addEventListener('addthis.ready', addthisReady);
						}		
					);
				}
			}]);
		});
		
		//if(js.debug){
			//alert("navigator.userAgent " + navigator.userAgent + "\n\ntest1 = " + (navigator.userAgent.match(/iPhone/i)) + "\ntest2" + navigator.userAgent.match(/CriOS/i)  );
			//if (navigator.userAgent.match(/OS 5(_\d)+ like Mac OS X/i)){
		//}
		if(  (navigator.userAgent.match(/OS 5(_\d)+ like Mac OS X/i) || navigator.userAgent.match(/OS 6(_\d)+ like Mac OS X/i)  ) && ! navigator.userAgent.match(/CriOS/i) ){
			$('.shares-link').click();			
		}

		/*
		if( navigator.userAgent.match(/iPhone/i) && ! navigator.userAgent.match(/CriOS/i) ){
			alert("click!");
		}
		*/

	},
	
	
	checkKeywordSupplied : function(){
		if($('#newKeyword').val().length>0){
			return true;
		}
		else{
			$('#newKeyword').addClass('error-border');
			return false;
		}	
	},
	
	handleSaveSearchClick : function( e ) {
		
		e.preventDefault();
		
		var ajax_feedback = {
			
			saved_searches_count : 0,
			$saved_searches : $('#saved-searches-count'),
			$saveSearch : $('#save-search'),
			success : function() {
				var html = '<span>' + eu.europeana.vars.msg.search_saved + '</span>';
				
				eu.europeana.ajax.methods.addFeedbackContent( html );
				eu.europeana.ajax.methods.showFeedbackContainer();
				
				ajax_feedback.saved_searches_count = parseInt( ajax_feedback.$saved_searches.html(), 10 );
				ajax_feedback.$saved_searches.html( ajax_feedback.saved_searches_count + 1 );
				ajax_feedback.$saveSearch.removeClass('icon-unsaveditem').addClass('icon-saveditem');
				ajax_feedback.$saveSearch.children('.save-label').html(eu.europeana.vars.msg.search_saved);
				ajax_feedback.$saveSearch.css('cursor', 'default');
				ajax_feedback.$saveSearch.unbind('click');
			},
			
			failure : function() {	
				var html = '<span id="save-search-feedback" class="error">' + eu.europeana.vars.msg.search_save_failed + '</span>';
				
				eu.europeana.ajax.methods.addFeedbackContent(html);
				eu.europeana.ajax.methods.showFeedbackContainer();
			}
		},
		ajax_data = {
			className : "SavedSearch",
			query : $('#query-to-save').val(),
			queryString : $('#query-string-to-save').val()
		};
		eu.europeana.ajax.methods.user_panel( 'save', ajax_data, ajax_feedback );
	}

};

