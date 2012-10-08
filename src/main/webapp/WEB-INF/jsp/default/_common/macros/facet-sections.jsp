
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="facettype" value="${fn:toLowerCase(facet.type)}" />
<c:choose>
	<c:when test="${facettype == 'type'}">
		<c:set var="classAttr" value="facet-section icon-arrow-6 facet-media active" />
	</c:when>
	<c:when test="${facet.selected}">
		<c:set var="classAttr" value="facet-section icon-arrow-6 active" />
	</c:when>
	<c:otherwise>
		<c:set var="classAttr" value="facet-section icon-arrow-6" />
	</c:otherwise>
</c:choose>


<li>
	<h3>
		<a href="" class="${classAttr}" rel="nofollow">
			<c:choose>
				<c:when test="${facettype == 'country'}"		><spring:message code="ByCountry_t" /></c:when>
				<c:when test="${facettype == 'completeness'}"	>By Record Size</c:when>
				<c:when test="${facettype == 'language'}"		><spring:message code="ByLanguage_t" /></c:when>
		        <c:when test="${facettype == 'provider'}"		><spring:message code="ByProvider_t" /></c:when>
				<c:when test="${facettype == 'rights'}"			><spring:message code="byCopyright_t" /></c:when>
				<c:when test="${facettype == 'type'}"			><spring:message code="ByMediatype_t" /></c:when>
				<c:when test="${facettype == 'year'}"			><spring:message code="Bydate_t" /></c:when>
			</c:choose>
		</a>
	</h3>
	<ul>
		<c:if test="${facettype == 'rights'}">
			<li id="rights-info"><spring:message code="rightsNotice_t" /></li>
		</c:if>
		<%@ include file="/WEB-INF/jsp/default/_common/macros/facet-items.jsp" %>
	</ul>
</li>
