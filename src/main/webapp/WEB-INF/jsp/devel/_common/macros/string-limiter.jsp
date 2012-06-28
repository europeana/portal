<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%-- #macro stringLimiter theStr size --%>
<%-- 
<c:set var="theStr"><%= request.getParameter("theStr") %></c:set>
<c:set var="size"><%= request.getParameter("size") %></c:set>
 --%>
<c:set var="newStr" value="${theStr}" />
<c:if test="${fn:length(newStr) > size}">
	<c:set var="newStr" value="${fn:substring(theStr, 0,size)}..." />
</c:if>
${newStr}
