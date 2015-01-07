js.utils.registerNamespace('eu.europeana.admin');

eu.europeana.admin = {
	total: 0,

	init: function() {
		$("td.apikey").each(function(index, domEle){
			var apiKey = $(this, "code").text();
			if (eu.europeana.admin.total == 0) {
				$('#status-message').html('Updating usage statistics...');
			}
			eu.europeana.admin.total++;
			$.ajax({
				url: '/admin/limitInfo.json',
				data: {'apiKey': apiKey},
				dataType: 'json',
				beforeSend: function (jqXHR, settings) {
					jqXHR.url = settings.url;
				},
				success: function(data, textStatus, jqXHR){
					$('#' + apiKey + ' td.actual').html(data.actual).removeClass('blue');
					// $('#' + apiKey + ' td.actual');
					$('#' + apiKey + ' td.total').html(data.total).removeClass('blue');
					// $('#' + apiKey + ' td.total');
					eu.europeana.admin.total--;
					if (eu.europeana.admin.total == 0) {
						$('#status-message').html('Usage statistics updated').fadeOut(2000);
					}
				}
			});
		});
	}
};