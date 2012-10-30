<!-- image -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%-- Determine whether or not the search widget is active and set target as appropriate --%>
<c:set var="targetArg" value="" />
<c:if test="${model.embedded}">
	<c:set var="targetArg" value='target="_blank"' />
</c:if>

<%--
	<c:set var="imgBaseUrl" value="http://europeanastatic.eu/api/image?type=TEXT&size=BRIEF_DOC&uri=" />
 --%>
 
<c:if test="${!empty cell.thumbnail}">
	<a href="${fn:replace(cell.fullDocUrl, '"', '&quot;')}&rows=${model.rows}" title="${fn:escapeXml(cell.title[0])}" ${targetArg} rel="nofollow">
		<img class="thumbnail" src='${cell.thumbnail}' alt="${cell.title[0]}" data-type="${fn:toLowerCase(cell.type)}" />
	</a>
</c:if>
<!-- /image -->
