<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:if test="${model.returnTo == 'SEARCH_HTML' && model.fullBeanView.docIdWindowPager.previous}">
  <li>
    <a href="/${model.portalName}${model.fullBeanView.docIdWindowPager.previousFullDocUrl}" title="<spring:message code="Previous_t" />" class="pagination-previous"><spring:message code="Previous_t" /></a>
  </li>
</c:if>
