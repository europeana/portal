<!-- query-form -->
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<form id="query-search" method="get" action="${query_action}">
	<fieldset>
		<c:choose>
			<c:when test="${model.debug && model.pageName == 'map.html'}">
				<c:set var="className" value="map-query" />
			</c:when>
			<c:otherwise>
				<c:set var="className" value="" />
			</c:otherwise>
		</c:choose>
		<c:set var="inputTitle"><spring:message code="SearchTerm_t" /></c:set>

		<input type="text" name="query" id="query-input" class="${className}" title="${inputTitle}"
		<c:if test="${!empty model.query}">value="${model.query}"</c:if> maxlength="175" />
		<input type="submit" class="submit-button" value="<spring:message code="Search_t" />" />

		<%-- save search link --%>
		<c:if test="${model.debug && model.pageName == 'map.html'}">
			<input type="checkbox" id="box_search" />
			<label for="box_search"><spring:message code="MapBoxedSearch_t" /></label>
		</c:if>

		<c:if test="${model.embedded}">
			<input type="hidden" name="embedded" 			value="${model.embeddedString}" />
			<input type="hidden" name="embeddedBgColor" 	value="${model.embeddedBgColor}" />
			<input type="hidden" name="embeddedForeColor" 	value="${model.embeddedForeColor}" />
			<input type="hidden" name="embeddedLogo"	 	value="${model.embeddedLogo}" />
			<input type="hidden" name="rswUserId"	 		value="${model.rswUserId}" />
			<input type="hidden" name="rswDefqry"			value="${model.rswDefqry}" />
			<input type="hidden" name="lang"				value="${model.locale}" />
		</c:if>
	</fieldset>

	<%-- additional feature links for the search box --%>
	<c:if test="${!model.embedded}">
		<%-- refine search link --%>
		<c:set var="refinedEnabled" value=" disabled" />
		<c:if test="${!empty model.enableRefinedSearch && model.enableRefinedSearch}">
			<c:set var="refinedEnabled" value="" />
		</c:if>
		<a href="" id="refine-search" class="nofollow${refinedEnabled}"><spring:message code="RefineYourSearch_t" /></a>

		<%-- save search link --%>
		<c:if test="${!empty model.user && model.pageName == 'search.html'}">
			<a href="" id="save-search" rel="nofollow"><spring:message code="SaveToMyEuropeana_t" /></a>
			<c:if test="${!empty model.briefBeanView}">
				<input type="hidden" id="query-to-save" value="${model.briefBeanView.pagination.presentationQuery.queryToSave}" />
			</c:if>
			<c:if test="${!empty model.query}">
				<input type="hidden" id="query-string-to-save" value="${model.query}" />
			</c:if>
		</c:if>

		<%-- help link --%>
		<a href="/${model.portalName}/usingeuropeana.html" id="search-help" class="${className}"><spring:message code="rswHelp_t" /></a>
	</c:if>
</form>
<!-- /query-form -->