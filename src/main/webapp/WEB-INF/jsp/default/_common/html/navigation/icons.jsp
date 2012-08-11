<!-- icons -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<ul class="navigation-icons">
	<%-- grid view --%>
	<c:if test="${!empty model.viewUrlTable}">
		<li>
			<a href="${model.viewUrlTable}" title="<spring:message code="AltGridView_t" />" rel="nofollow" class="${gridview_active}grid-view"></a>
		</li>
	</c:if>

	<%-- timeline --%>
	<c:if test="${!empty model.viewUrlTimeline}">
		<li class="timeline_icon">
			<a href="${model.viewUrlTimeline}" title="<spring:message code="AltTimelineView_t" />" rel="nofollow" class="${timeline_active}timeline"></a>
		</li>
	</c:if>

	<%-- map --%>
	<li class="mapview_icon">
		<a href="${model.viewUrlMap}" title="<spring:message code="AltMapView_t" />" rel="nofollow" class="${map_active}mapview"></a>
	</li>

	<%-- 
		wikipedia
		2011-07-21
		removing until wikipedia is available
	--%>
	<%--
		<li>
			<a href="" title="<spring:message code="AltWikipediaView_t" />" rel="nofollow" class="wikipedia"></a>
		</li>
	--%>
</ul>
<!-- /icons -->
