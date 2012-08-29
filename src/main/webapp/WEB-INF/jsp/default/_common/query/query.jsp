
<div class="row" xstyle="text-align:center;">

	<div id="query"${menu_user_exists} class="column centered" xstyle="background-color:#ffe;">
	
		<h3 class="show-on-phones" aria-hidden="true"><spring:message code="query_heading_t"/></h3>
		
		<%@ include file="/WEB-INF/jsp/default/_common/query/form.jsp" %>
		<%@ include file="/WEB-INF/jsp/default/_common/query/refinement.jsp"%>
		
		<%--"did you mean" suggestion. nb: how to handle for embedded widget - might be best to have the backend create the url	--%>
		
		<c:if test="${not model.embedded} && ${model.showDidYouMean}">
			<spring:message code="Didyoumean_t"/>:
			<a href="/${model.portalName}/search.html?query=${model.briefBeanView.spellCheck.collatedResult}">${model.briefBeanView.spellCheck.collatedResult}</a>
		</c:if>
	</div>
	
</div>