<%--@ include file="/WEB-INF/jsp/devel/_common/html/menus/sidebar.jsp" --%>
<c:choose>
<c:when test="${!empty model.bodyContent}">
${model.bodyContent}
</c:when>
<c:otherwise>
<h2><spring:message code="no_content_t"/></h2>
</c:otherwise>
</c:choose>