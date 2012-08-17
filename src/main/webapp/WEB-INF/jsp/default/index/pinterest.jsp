
 

	<c:if test='${not empty model.pinterestItems}'>
	    <script type="text/javascript">
            var carousel3Data = [];
			<c:forEach var="item" items="${model.pinterestItems}">
				carousel3Data[carousel3Data.length] = {
					//thumb:			('${item.images[0].src}').length == 0 ? 'http://media-cache-ec3.pinterest.com/upload/176484879118638505_jmQ9FkIX_b.jpg' : '/${model.portalName}${item.images[0].src}',
					//thumb:			'/${model.portalName}${item.images[0].src}',
					thumb:			'http://media-cache-ec3.pinterest.com/upload/176484879118638505_jmQ9FkIX_b.jpg',
					title:			'${item.title}',
					description:	'${item.description}',
					link:			'${item.link}'
				};
			</c:forEach>
//			alert(JSON.stringify(carousel3Data));
		</script>
		<div id="carousel-3" class="europeana-carousel">
		</div>
	</c:if>	



<%--
<h2><spring:message code='recent_pinterest_activities_t'/>&nbsp;<a href="http://www.pinterest.com/europeana" target="_blank"><img src="/${model.portalName}/themes/common/images/logos/Pinterest_Logo.png" width="70" height="18" alt="Follow Me on Pinterest" /></a></h2>
<div id="pinterest" class="rssFeed"></div>
 --%>
