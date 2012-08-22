<!-- results: ${model.hasResults} -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div id="search-results">
  <c:choose>
    <c:when test="${model.hasResults}">
      <%@ include file="/WEB-INF/jsp/devel/_common/html/navigation/search-results-navigation.jsp"%>
      <%@ include file="/WEB-INF/jsp/devel/search/content/results/grid.jsp"%>
      <%@ include file="/WEB-INF/jsp/devel/_common/html/navigation/search-results-navigation.jsp"%>
    </c:when>
    <c:otherwise>
      <c:choose>
        <c:when test="${model.briefBeanView != null}">
          <h2><spring:message code="NoItemsFound_t" /></h2>
        </c:when>
        <c:otherwise>
          <h2><spring:message code="InvalidQuery_t" /></h2>
        </c:otherwise>
      </c:choose>
    </c:otherwise>
  </c:choose>
</div>
<!-- /results -->
