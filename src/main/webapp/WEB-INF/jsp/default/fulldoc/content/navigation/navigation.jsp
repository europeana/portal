<!-- navigation -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:choose>
	<c:when test="${true || !empty model.fullBeanView.docIdWindowPager}">
		<ul id="navigation" class="navigation notranslate hide-on-phones">
	
			<%-- return --%>
			
			<c:if test="${true || model.fullBeanView.docIdWindowPager.returnToResults && !model.embedded}">
				<li>
					<a href="${model.fullBeanView.docIdWindowPager.pagerReturnToPreviousPageUrl}" rel="nofollow"><spring:message code="ReturnToSearchResults_t" /></a>
				</li>
			</c:if>
					
			<%-- next --%>
			
			<c:if test='${true || model.returnTo == "SEARCH_HTML" && model.fullBeanView.docIdWindowPager.next}'>
				<li>
					<a href="/${model.portalName}${model.fullBeanView.docIdWindowPager.nextFullDocUrl}" title="<spring:message code="Next_t" />" class="pagination-next"><spring:message code="Next_t" />&nbsp;&nbsp;&gt;</a>
				</li>
			</c:if>
			
			<%-- previous --%>
			
			<c:if test='${true || model.returnTo == "SEARCH_HTML" && model.fullBeanView.docIdWindowPager.previous}'>
				<li>
					<a href="/${model.portalName}${model.fullBeanView.docIdWindowPager.previousFullDocUrl}" title="<spring:message code="Previous_t" />" class="pagination-previous">&lt;&nbsp;&nbsp;<spring:message code="Previous_t" /></a>
				</li>
			</c:if>
		</ul>
	</c:when>
</c:choose>
<!-- /navigation -->
