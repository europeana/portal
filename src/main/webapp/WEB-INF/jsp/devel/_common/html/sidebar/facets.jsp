<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!-- facets -->
<c:if test="${!empty model.briefBeanView.facetQueryLinks}">
  <h2><spring:message code="FilterYourSearch_t" />:</h2>

  <ul id="filter-search">
    <c:forEach var="facet" items="${model.briefBeanView.facetQueryLinks}">
      <%@ include file="/WEB-INF/jsp/devel/_common/macros/facet-sections.jsp"%>
    </c:forEach>

    <li class="ugc-li">
      <h3>
        <c:set var="ugcClassName">
          <c:choose>
            <c:when test="${model.UGCFilter}">ugc-unchecked</c:when>
            <c:otherwise>ugc-checked</c:otherwise>
          </c:choose>
        </c:set>
        <a class="facet-ugc ${ugcClassName}" rel="nofollow" href="${model.UGCUrl}"> <spring:message code="IncludeUGC_t" /></a>
      </h3>
    </li>
  </ul>
</c:if>
<!-- /facets -->
