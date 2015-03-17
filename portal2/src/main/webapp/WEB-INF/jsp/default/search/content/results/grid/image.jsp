<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 
<c:if test="${!empty cell.thumbnail}">
<c:set var="req" value="${pageContext.request}" />
<%--
<c:set var="searchUrl" value="${fn:replace(req.requestURL, fn:substring(req.requestURI, 0, fn:length(req.requestURI)), req.contextPath)}" />
 --%>
 <c:set var="searchUrl" value="${homeUrlNS}" />
		
	<a href="${searchUrl}${fn:replace(cell.fullDocUrl, '"', '&quot;')}&rows=${model.rows}"
		title="${cell.titleBidi[0]}"
		${targetArg}>
		<img class="thumbnail" src="${cell.thumbnail}" alt="${cell.title[0]}" data-type="${fn:toLowerCase(cell.type)}" />
	</a>
</c:if>

