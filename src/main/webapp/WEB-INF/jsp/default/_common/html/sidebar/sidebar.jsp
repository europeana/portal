<!-- sidebar -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="facets-actions" class="sidebar">
	
	
	
	
<!-- facets -->
<c:if test="${!empty model.briefBeanView.facetQueryLinks}">
	<h2><spring:message code="FilterYourSearch_t" />:</h2>
	
	<ul id="filter-search">
		<c:forEach var="facet" items="${model.briefBeanView.facetQueryLinks}">
			<%@ include file="/WEB-INF/jsp/default/_common/macros/facet-sections.jsp" %>
		</c:forEach>

		<li class="ugc-li">
			<h3>
				<c:set var="ugcClassName">
					<c:choose>
						<c:when test="${model.UGCFilter}">ugc-unchecked</c:when>
						<c:otherwise>ugc-checked</c:otherwise>
					</c:choose>
				</c:set>
				<a class="facet-ugc ${ugcClassName}" rel="nofollow" href="${model.UGCUrl}">
					<spring:message code="IncludeUGC_t" />
				</a>
			</h3>
		</li>
	</ul>
</c:if>
<!-- /facets -->
	
	
	
	
	<!-- legend -->
<h2 id="legend"><spring:message code="Legend_t" />:</h2>
HELLO
<div id="legend-icons">
	<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:IMAGE" class="image" title="<spring:message code="Image_t" />"><spring:message code="Image_t" /></a>
	<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:VIDEO" class="video" title="<spring:message code="Video_t" />"><spring:message code="Video_t" /></a>
	<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:TEXT" class="text" title="<spring:message code="Text_t" />"><spring:message code="Text_t" /></a>
	<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:SOUND" class="sound" title="<spring:message code="Sound_t" />"><spring:message code="Sound_t" /></a>
	<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:3D" class="threeD" title="<spring:message code="3D_t" />"><spring:message code="3D_t" /></a>
</div>
<!-- end legend -->
	
	
	
	
</div>
<!-- /sidebar -->
