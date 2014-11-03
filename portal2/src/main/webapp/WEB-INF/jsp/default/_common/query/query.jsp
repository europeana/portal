


<div id="query-full" <c:if test="${!empty model.queryTranslationLinks}">class="with-translations"</c:if> >

	<c:choose>
		<c:when test="${empty isSearchWidget}">
			<%@ include file="/WEB-INF/jsp/default/_common/query/form.jsp" %>
		</c:when>
		<c:otherwise>
			<%@ include file="/WEB-INF/jsp/default/_common/query/form-sw.jsp" %>
		</c:otherwise>
	</c:choose>

	<%--"did you mean" suggestion. nb: how to handle for embedded widget - might be best to have the backend create the url	--%>
	<%--
	<c:if test="${not model.embedded} && ${model.showDidYouMean}">
		<spring:message code="Didyoumean_t"/>:
		<a href="/search.html?query=${model.briefBeanView.spellCheck.collatedResult}">${model.briefBeanView.spellCheck.collatedResult}</a>
	</c:if>
	--%>
</div>

<c:if test="${empty isSearchWidget}">
	<div id="query-info" class="hide-cell-on-phones <c:if test="${!empty model.queryTranslationLinks}">with-translations</c:if>">

		<c:set var="helpHref" value="/usingeuropeana.html"/>
		<c:choose>
			<c:when test="${model.pageName == 'login.html' || model.pageName == 'myeuropeana/index'}">
				<c:set var="helpHref" value="/usingeuropeana_myeuropeana.html"/>
			</c:when>
			<c:when test="${model.pageName == 'full-doc.html' || model.pageName == 'search.html'}">
				<c:set var="helpHref" value="/usingeuropeana_results.html"/>
			</c:when>
		</c:choose>

		<a id="help-link" class="hide-ilb-on-phones search-help europeana" href="${helpHref}"><spring:message code="rswHelp_t" /></a>
	</div>
</c:if>
