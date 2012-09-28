<%@ tag trimDirectiveWhitespaces="true" %>
<%-- parameters --%>
<%@ attribute name="text" required="true" rtexprvalue="true" %>
<%-- taglib --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%-- content --%>
<%-- fn:replace(text, "\\", "\\\\") --%>
<c:set var="text" value='${fn:replace(text, "\\\\", "\\\\\\\\")}' />
<%-- fn:replace(text, "/", "\\/") --%>
<%-- c:set var="text" value='${fn:replace(text, "/", "\\\\/")}' / --%>
<%-- fn:replace(text, "\"", "\\\"") --%>
${fn:replace(fn:replace(text, "/", "\\/"), '"', '\\"')}
