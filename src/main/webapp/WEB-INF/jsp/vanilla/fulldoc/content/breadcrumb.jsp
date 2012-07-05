<!-- breadcrumb -->
<c:choose>
  <c:when test='${not fn:startsWith(query, "europeana_uri:")}'>
    <li><spring:message code='MatchesFor_t' />:</li>
    <c:forEach items="${model.fullBeanView.docIdWindowPager.breadcrumbs}" var="crumb">
      <c:choose>
        <c:when test="${crumb.last}">
          <li><a href="/${model.portalName}/search.html?${crumb.href}">${crumb.display}</a></li>
        </c:when>
        <c:otherwise>
          <li>${crumb.display}</li>
        </c:otherwise>
      </c:choose>
    </c:forEach>
  </c:when>
  <c:otherwise>
    <li>
      <spring:message code="ViewingRelatedItems_t" />
      <a href="${model.document.id}" rel="nofollow">
        <c:choose>
          <c:when test="${model.useCache}">
            <img src="${model.cacheUrl}uri=${model.thumbnailUrl}&amp;size=BRIEF_DOC&amp;type=${model.document.type}" alt="${model.document.title}" height="25" />
          </c:when>
          <c:otherwise>
            <img src="${model.thumbnailUrl}" alt="${model.document.title}" height="25" />
          </c:otherwise>
        </c:choose>
      </a>
    </li>
  </c:otherwise>
</c:choose>
<!-- /breadcrumb -->

