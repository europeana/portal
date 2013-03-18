<div id="query-full">
	<%@ include file="/WEB-INF/jsp/default/_common/query/form.jsp" %>
	
	<%--"did you mean" suggestion. nb: how to handle for embedded widget - might be best to have the backend create the url	--%>
	<c:if test="${not model.embedded} && ${model.showDidYouMean}">
		<spring:message code="Didyoumean_t"/>:
		<a href="/${model.portalName}/search.html?query=${model.briefBeanView.spellCheck.collatedResult}">${model.briefBeanView.spellCheck.collatedResult}</a>
	</c:if>
</div>

<div id="query-info" class="hide-cell-on-phones">
	<a class="hide-ilb-on-phones search-help europeana" href="/${model.portalName}/usingeuropeana.html"><spring:message code="rswHelp_t" /></a>
</div>