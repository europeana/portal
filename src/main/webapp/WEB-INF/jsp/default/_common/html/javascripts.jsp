<%@ include file="/WEB-INF/jsp/default/_common/html/no-js.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/variables/variables-javascript.jsp" %>

<c:if test="${empty param.noJs}">
	<script src="/${branding}/js/eu/europeana/bootstrap/bootstrap.js">
	</script>
</c:if>
