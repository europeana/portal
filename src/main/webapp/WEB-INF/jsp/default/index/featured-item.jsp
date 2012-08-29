<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div class="six columns">
	<h4 id="collapse-header-2">
		<span class="left"><spring:message code="featured-content-title_t" /></span>
		<span class="collapse-icon"></span>
		<a class="feed-link" href="http://pinterest.europeana.eu.feed" target="_blank" title="RSS Feed"></a>
	</h4>

	<div class="collapse-content">
		
		<c:if test="${!empty model.featuredItem}">
			<a href="/${model.portalName}<spring:message code="${model.featuredItem.anchorUrl}" />" title="<spring:message code="${model.featuredItem.anchorTitle}" />" target="<spring:message code="${model.featuredItem.anchorTarget}" />" class="image"><img src="/${model.portalName}<spring:message code="${model.featuredItem.imgUrl}" />" alt="<spring:message code="${model.featuredItem.imgAlt}" />" width="<spring:message code="${model.featuredItem.imgWidth}" />" height="<spring:message code="${model.featuredItem.imgHeight}" />"/></a>
			<h3><a href="/${model.portalName}<spring:message code="${model.featuredItem.anchorUrl}" />" title="<spring:message code="${model.featuredItem.anchorTitle}" />" target="<spring:message code="${model.featuredItem.anchorTarget}" />"><spring:message code="${model.featuredItem.h3}" /></a></h3>
			<p>
				<%-- @msgPropertyLimiter '${model.featuredItem.p}' '${featured_item_snipet_limit}' / --%>
			</p>
			<!-- ${model.featuredItem.p} -->
			<!-- ${featured_item_snipet_limit} -->
			<p><jsp:include page="/WEB-INF/jsp/devel/_common/macros/msg-property-limiter.jsp">
					<jsp:param name="msg_property_tag" value="${model.featuredItem.p}" />
					<jsp:param name="length_limit" value="${featured_item_snipet_limit}" />
				</jsp:include></p>
		</c:if>


		
<%--
		
		    <script type="text/javascript">
	            var carousel2Data = [];
				<c:forEach var="item" items="${model.featuredItems}">
					carousel2Data[carousel2Data.length] = {
						thumb:			"/${model.portalName}<spring:message code='${item.imgUrl}'/>",
						title:			"<spring:message code='${item.anchorTitle}'/>",
						description:	"<spring:message code='${item.p}'/>",
						link:			"/${model.portalName}<spring:message code='${item.anchorUrl}'/>"
					};
				</c:forEach>
			</script>
				
			
			<div id="carousel-2-header" class="europeana-header">
			</div>
			<div id="carousel-2" class="europeana-carousel">
			</div>
			<div id="carousel-2-footer" class="europeana-footer">
			</div>
 --%>
			
		
				
	</div>
</div>


<div class="six columns" id="section-featured-partner">
	<h4><spring:message code="featured-partner-title_t" /></h4>
	
		<c:if test="${!empty model.featuredItem}">
		<a href="/${model.portalName}<spring:message code="${model.featuredItem.anchorUrl}" />" title="<spring:message code="${model.featuredItem.anchorTitle}" />" target="<spring:message code="${model.featuredItem.anchorTarget}" />" class="image"><img src="/${model.portalName}<spring:message code="${model.featuredItem.imgUrl}" />" alt="<spring:message code="${model.featuredItem.imgAlt}" />" width="<spring:message code="${model.featuredItem.imgWidth}" />" height="<spring:message code="${model.featuredItem.imgHeight}" />"/></a>
		<h3><a href="/${model.portalName}<spring:message code="${model.featuredItem.anchorUrl}" />" title="<spring:message code="${model.featuredItem.anchorTitle}" />" target="<spring:message code="${model.featuredItem.anchorTarget}" />"><spring:message code="${model.featuredItem.h3}" /></a></h3>
		<p>
			<%-- @msgPropertyLimiter '${model.featuredItem.p}' '${featured_item_snipet_limit}' / --%>
		</p>
		<!-- ${model.featuredItem.p} -->
		<!-- ${featured_item_snipet_limit} -->
		<p><jsp:include page="/WEB-INF/jsp/devel/_common/macros/msg-property-limiter.jsp">
				<jsp:param name="msg_property_tag" value="${model.featuredItem.p}" />
				<jsp:param name="length_limit" value="${featured_item_snipet_limit}" />
			</jsp:include></p>
	</c:if>

</div>



<%--

<c:if test='${not empty model.featuredItem}'>
  <h2><spring:message code='${model.featuredItem.h2}'/></h2>
  <a href="/${model.portalName}<spring:message code='${model.featuredItem.anchorUrl}'/>" title="<spring:message code='${model.featuredItem.anchorTitle}'/>" target="<spring:message code='${model.featuredItem.anchorTarget}'/>"><img src="/${model.portalName}<spring:message code='${model.featuredItem.imgUrl}'/>" alt="<spring:message code='${model.featuredItem.imgAlt}'/>" width="<spring:message code='${model.featuredItem.imgWidth}'/>" height="<spring:message code='${model.featuredItem.imgHeight}'/>"/></a>
  <h3><a href="/${model.portalName}<spring:message code='${model.featuredItem.anchorUrl}'/>" title="<spring:message code='${model.featuredItem.anchorTitle}'/>" target="<spring:message code='${model.featuredItem.anchorTarget}'/>"><spring:message code='${model.featuredItem.h3}'/></a></h3>
  <p><spring:message code='${model.featuredItem.p}'/></p>
</c:if>

 --%>