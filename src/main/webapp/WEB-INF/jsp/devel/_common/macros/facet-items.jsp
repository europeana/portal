<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!-- facet: ${facet} -->
<c:forEach items="${facet.links}" var="facet_item">
  <c:set var="classAttr" value="" />
  <c:if test="${facet_item.remove}">
    <c:set var="classAttr" value=' class="active"' />
  </c:if>

  <c:set var="label">
    <c:choose>
      <c:when test="${!empty facet_item.title}">${facet_item.title}</c:when>
      <c:otherwise>${facet_item.value}</c:otherwise>
    </c:choose>
  </c:set>

  <li><h4><a href="/${model.portalName}/${model.pageName}?query=${model.query}${fn:escapeXml(facet_item.url)}" title="${fn:escapeXml(facet_item.value)}" ${classAttr} rel="nofollow">${label} (${facet_item.count})</a></h4></li>
</c:forEach>
