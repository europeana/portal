<%@ tag trimDirectiveWhitespaces="true" %>
<%-- parameters --%>
<%@ attribute name="theStr" required="true" rtexprvalue="true" %>
<%@ attribute name="size" required="true" rtexprvalue="true" %>
<%-- included tag libs --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="newStr" value="${theStr}" />
<c:if test="${fn:length(newStr) > size}">
  <c:set var="newStr" value="${fn:substring(theStr, 0,size)}..." />
</c:if>
${newStr}