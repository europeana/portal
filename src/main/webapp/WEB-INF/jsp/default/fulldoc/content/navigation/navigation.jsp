<!-- navigation -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<ul id="navigation" class="navigation notranslate">
<c:choose>
	<c:when test="${!empty model.fullBeanView.docIdWindowPager}">

		<%-- return --%>
		
		<c:if test="{model.fullBeanView.docIdWindowPager.returnToResults && !model.embedded}">
			<li>
				<a href="${model.fullBeanView.docIdWindowPager.pagerReturnToPreviousPageUrl}" rel="nofollow"><spring:message code="ReturnToSearchResults_t" /></a>
			</li>
		</c:if>
				
		<%-- next --%>
		
		<c:if test='{model.returnTo == "BD" && model.fullBeanView.docIdWindowPager.next}'>
			<li>
				<a href="${model.fullBeanView.docIdWindowPager.nextFullDocUrl}" title="<spring:message code="Next_t" />" class="pagination-next"><spring:message code="Next_t" /></a>
			</li>
		</c:if>
		
		<%-- previous --%>
		
		<c:if test='{model.returnTo == "BD" && model.fullBeanView.docIdWindowPager.previous}'>
			<li>
				<a href="${model.fullBeanView.docIdWindowPager.previousFullDocUrl}" title="<spring:message code="Previous_t" />" class="pagination-next"><spring:message code="Previous_t" /></a>
			</li>
		</c:if>

	</c:when>
	<c:otherwise>
		<li>&nbsp;</li>
	</c:otherwise>
</c:choose>
</ul>
<!-- /navigation -->
