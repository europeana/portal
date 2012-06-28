<div id="content">
	<%@ include file="/WEB-INF/jsp/devel/_common/html/menus/sidebar.jsp" %>
	<c:choose>
		<c:when test="${!empty model.bodyContent}">
			${model.bodyContent}
		</c:when>
		<c:otherwise>
			<h2>Unable to find content for this page.</h2>
		</c:otherwise>
	</c:choose>
</div>