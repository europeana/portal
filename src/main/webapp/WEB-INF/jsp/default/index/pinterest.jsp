
<%-- 

	<c:if test='${not empty model.pinterestItems}'>
	    <script type="text/javascript">
            var carousel3Data = [];
			<c:forEach var="item" items="${model.pinterestItems}">
				carousel3Data[carousel3Data.length] = {
					thumb:			"/${model.portalName}<spring:message code='${item.imgUrl}'/>",
					title:			"<spring:message code='${item.anchorTitle}'/>",
					description:	"<spring:message code='${item.p}'/>",
					link:			"${item.anchorUrl}"
				};
			</c:forEach>
		</script>
		<div id="carousel-3" class="europeana-carousel">
		</div>
	</c:if>	

--%>

<%--
<h2><spring:message code='recent_pinterest_activities_t'/>&nbsp;<a href="http://www.pinterest.com/europeana" target="_blank"><img src="/${model.portalName}/themes/common/images/logos/Pinterest_Logo.png" width="70" height="18" alt="Follow Me on Pinterest" /></a></h2>
<div id="pinterest" class="rssFeed"></div>
 --%>
