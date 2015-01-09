<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div id="items">
	<%-- Determine whether or not the search widget is active and set target as appropriate --%>
	<c:set var="targetArg" value="" />
	<c:if test="${model.embedded}">
		<c:set var="targetArg" value='target="_blank"' />
	</c:if>

	<c:forEach items="${model.briefBeanView.briefBeans}" var="cell" varStatus="x"><div class="li"><%-- whitespace breaks layout - keep li elements together tight --%>
		<div class="thumb-frame">
			<c:set var="title" value="${cell.titleBidi[0]}" />
			<c:set var="icon_class" value="icon-${fn:toLowerCase(cell.type)}" />
			<%@ include file="/WEB-INF/jsp/default/search/content/results/grid/image.jsp" %>
		</div>

		<%-- comments within the ellipsis div cause problems with the functionality! --%>
		<c:set var="titleBidi" value="" />
		<c:if test="${!empty cell.titleBidi}">
			<c:set var="titleBidi"> title="${title}"</c:set>
		</c:if>
        <c:set var="req" value="${pageContext.request}" />
		<c:set var="searchUrl" value="${fn:replace(req.requestURL, fn:substring(req.requestURI, 0, fn:length(req.requestURI)), req.contextPath)}" />
		<a href="${searchUrl}${cell.fullDocUrl}&rows=${model.rows}" ${targetArg} ${titleBidi}>
			<div class="ellipsis">
				<c:choose>
					<c:when test="${!empty cell.titleBidi}">${title}</c:when>
					<c:otherwise></c:otherwise>
				</c:choose>
				<span class="fixed"><span aria-hidden="true" class="${icon_class}"></span></span>
			</div>
		</a>

		<%--@ include file="/WEB-INF/jsp/default/search/content/results/grid/additional-info.jsp" --%>
	</div></c:forEach>
</div>
