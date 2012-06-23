<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!-- query ${model.query} -->
<div id="query"${menu_user_exists}>
	<h3><spring:message code="query_heading_t" /></h3>
	<%@ include file="/WEB-INF/jsp/default/_common/html/query/query-form.jsp" %>
	<%@ include file="/WEB-INF/jsp/default/_common/html/query/refinement-form.jsp" %>

	<%--
		did you mean suggestion
		nb: how to handle for embedded widget - might be best to have the backend create the url
	--%>
	<c:if test="${empty model.embedded && !empty model.showDidYouMean}">
		<div id="additional-feedback">
			<spring:message code="Didyoumean_t" />:
			<a href="/${model.portalName}/search.html?query=${model.briefBeanView.spellCheck.collatedResult}">${model.briefBeanView.spellCheck.collatedResult}</a>
		</div>
	</c:if>
</div>
<!-- /query -->