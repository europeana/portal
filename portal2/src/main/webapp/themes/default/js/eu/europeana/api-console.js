js.utils.registerNamespace('eu.europeana.apiconsole');

eu.europeana.apiconsole = {
	status: 'hidden',
	panels: ['record', 'search', 'suggestions'],

	init: function() {
		// initialize panels
		for (i in eu.europeana.apiconsole.panels) {
			var id = '#' + eu.europeana.apiconsole.panels[i] + '-panel';
			if (eu.europeana.apiconsole.panels[i] == apiconsole.selectedPanel) {
				$(id).show();
			} else {
				$(id).hide();
			}
		}

		$("#show-http-headers a").click(function(){
			$(".request-http-headers-title").toggle('slow');
			$(".request-http-headers").toggle('slow');
			if (eu.europeana.apiconsole.status == 'hidden') {
				$("#show-http-headers a").text(apiconsole.hideHeaderText);
				eu.europeana.apiconsole.status = 'shown';
			} else {
				$("#show-http-headers a").text(apiconsole.showHeaderText);
				eu.europeana.apiconsole.status = 'hidden';
			}
		});

		$('input[name=function]').click(function(){
			var selectedPanel = $(this).val();
			for (i in eu.europeana.apiconsole.panels) {
				var id = '#' + eu.europeana.apiconsole.panels[i] + '-panel';
				if (eu.europeana.apiconsole.panels[i] == selectedPanel) {
					$(id).show();
				} else {
					$(id).hide();
				}
			}
		});
		
	},
};

var updateParent = function() {
	var target = parent.postMessage ? parent : (parent.document.postMessage ? parent.document : undefined);
	var resHeight = $('#api-result').outerHeight( true );
	var formHeight = $('#api-form').outerHeight( true );
	var height = Math.max(resHeight, formHeight);
	target.postMessage((height + 10) + 'px', '*');
};

$(document).ready(function(){
	updateParent();
});

$(window).euRsz(function(){
	updateParent();
});
