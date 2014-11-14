<%@ include file="/WEB-INF/jsp/default/_common/html/no-js.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/variables/variables-javascript.jsp" %>

<c:if test="${empty param.noJs}">
	<c:choose>
		<c:when test="${!empty model.debug && !model.debug}">
			<script src="/themes/default/js/eu/europeana/bootstrap/min/bootstrap.min.js"></script>
		</c:when>
		<c:otherwise>
			<script src="/themes/default/js/eu/europeana/bootstrap/bootstrap.js"></script>
		</c:otherwise>
	</c:choose>
</c:if>
