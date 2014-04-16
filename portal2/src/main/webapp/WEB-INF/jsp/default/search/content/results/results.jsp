<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div id="search-results">
	<c:choose>
		<c:when test="${model.hasResults}">
			<c:set var="position_class" value="nav-top"/>
			<%--
			<%@ include file="/WEB-INF/jsp/default/search/content/results/doYouMean.jsp" %>
			--%>
			<c:if test="${empty isSearchWidget}">
				<div class="search-results-navigation notranslate">
					<%@ include file="/WEB-INF/jsp/default/_common/html/navigation/pagination.jsp" %>
				</div>
			</c:if>

			<%@ include file="/WEB-INF/jsp/default/search/content/results/grid.jsp" %>

			<c:set var="position_class" value="nav-bottom"/>
			<div class="search-results-navigation notranslate">
				<%@ include file="/WEB-INF/jsp/default/_common/html/navigation/pagination.jsp" %>
			</div>
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
