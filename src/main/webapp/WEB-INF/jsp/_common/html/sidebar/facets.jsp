<!-- facets -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%-- c:if test="${!empty model.briefBeanView}" --%>
<%-- <c:if test="${!empty model.briefBeanView.facetQueryLinks}"> --%>
	<h2><spring:message code="FilterYourSearch_t" />:</h2>
	
	<ul id="filter-search">
		<%-- c:forEach var="facet" items="${model.briefBeanView.facetQueryLinks}"
		<c:forEach var="facet" items="${model.briefBeanView.facetQueryLinks}">
			<!-- facet.name: ${facet.name} -->
			<%@ include file="/WEB-INF/jsp/_common/macros/facet-sections.jsp" %>
			<!-- /facet.name: ${facet.name} -->
		</c:forEach>
		--%>

		<li class="ugc-li">
			<h3>
				<a class="facet-ugc 
					<c:choose>
						<c:when test="${model.UGCFilter}">ugc-unchecked</c:when>
						<c:otherwise>ugc-checked</c:otherwise>
					</c:choose>
					" rel="nofollow" href="${model.UGCUrl}">
						<spring:message code="IncludeUGC_t"/>
				</a>
			</h3>
		</li>
	</ul>
<%-- </c:if> --%>
<!-- /facets -->
