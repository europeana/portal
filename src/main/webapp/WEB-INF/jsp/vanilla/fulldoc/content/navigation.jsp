<ul>
<c:choose>
  <c:when test="${!empty model.fullBeanView.docIdWindowPager}">
  <c:if test="{model.fullBeanView.docIdWindowPager.returnToResults && !model.embedded}">
    <li><a href="${model.fullBeanView.docIdWindowPager.pagerReturnToPreviousPageUrl}" rel="nofollow"><spring:message code="ReturnToSearchResults_t" /></a></li>
  </c:if>
  <c:if test='{model.returnTo == "BD" && model.fullBeanView.docIdWindowPager.next}'>
    <li><a href="${model.fullBeanView.docIdWindowPager.nextFullDocUrl}" title="<spring:message code="Next_t" />" ><spring:message code="Next_t" /></a></li>
  </c:if>
  <c:if test='{model.returnTo == "BD" && model.fullBeanView.docIdWindowPager.previous}'>
    <li><a href="${model.fullBeanView.docIdWindowPager.previousFullDocUrl}" title="<spring:message code="Previous_t" />" ><spring:message code="Previous_t" /></a></li>
  </c:if>
  </c:when>
  <c:otherwise>
    <li>&nbsp;</li>
  </c:otherwise>
</c:choose>
</ul>
