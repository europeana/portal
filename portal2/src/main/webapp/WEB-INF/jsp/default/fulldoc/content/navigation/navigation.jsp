<!-- navigation -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:choose>
	<c:when test="${true || !empty model.fullBeanView.docIdWindowPager}">
		<c:set var="navClass" value=" hidden"/>
		<c:if test="${!empty model.fullBeanView.docIdWindowPager.returnToResults 
					&& model.fullBeanView.docIdWindowPager.pagerReturnToPreviousPageUrl != null}">
			<c:set var="navClass" value=""/>
		</c:if>

		<ul id="navigation" class="navigation notranslate hide-on-phones ${navClass}">
			<%-- return to search results --%>
			<c:if test="${!empty model.fullBeanView.docIdWindowPager.returnToResults 
				&& model.fullBeanView.docIdWindowPager.pagerReturnToPreviousPageUrl != null
				&& !model.embedded}">
				<li>
					<a class="europeana" href="../..${model.fullBeanView.docIdWindowPager.pagerReturnToPreviousPageUrl}" rel="nofollow"><spring:message code="ReturnToSearchResults_t" /></a>
				</li>
			</c:if>

			<%-- next --%>
			<c:if test='${model.returnTo == "SEARCH_HTML" && model.fullBeanView.docIdWindowPager.next}'>
				<li>
					<a href="../..${model.fullBeanView.docIdWindowPager.nextFullDocUrl}" title="<spring:message code="Next_t" />" class="pagination-next europeana"><spring:message code="Next_t" />&nbsp;&nbsp;&gt;</a>
				</li>
			</c:if>

			<%-- previous --%>
			<c:if test='${model.returnTo == "SEARCH_HTML" && model.fullBeanView.docIdWindowPager.previous}'>
				<li>
					<a href="../..${model.fullBeanView.docIdWindowPager.previousFullDocUrl}" title="<spring:message code="Previous_t" />" class="pagination-previous europeana">&lt;&nbsp;&nbsp;<spring:message code="Previous_t" /></a>
				</li>
			</c:if>
		</ul>
	</c:when>
</c:choose>
<!-- /navigation -->
