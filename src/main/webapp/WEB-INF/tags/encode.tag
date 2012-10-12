<%@ tag trimDirectiveWhitespaces="true" %>
<%-- This tag creates an encoded URL escaping special characters --%>

<%-- parameters --%>
<%@ attribute name="url" required="true" %>

<%-- tag libs --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:url var="encoded" value=""><c:param name="output" value="${url}" /></c:url>
<c:set var="encoded" value="${fn:substringAfter(encoded, '=')}" />
${encoded}