<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
 * msg-property-limiter.ftl
 *
 * This macro was built in order to limit the string length of a messages.properties tag
 *
 * The macro receives the messages.properties tag and the string limiter count and then
 * returns the resulting string
 *
 * @param msg_property_tag string - the messages.properties tag
 * @param length_limit int - the string length limit
 * @return new_string string - the resulting string
 * 
 * @author dan entous
 * @version 2011-06-17 12.33 GMT+1
 --%>
<%-- #macro msgPropertyLimiter msg_property_tag length_limit --%>
<%-- TODO: it is the best ethod to receive parameters? --%>
<c:set var="msg_property_tag"><%=request.getParameter("msg_property_tag")%></c:set>
<c:set var="length_limit"><%=request.getParameter("length_limit")%></c:set>

<c:set var="msg_property_string"><spring:message code="${msg_property_tag}" /></c:set>
<!-- $msg_property_string -->
<c:choose>
	<c:when test="${fn:length(msg_property_string) > length_limit}">
		<c:set var="new_string" value='${fn:substring(msg_property_string, 0, length_limit) + "..." }'/>
	</c:when>
	<c:otherwise>
		<c:set var="new_string" value="${msg_property_string}" />
	</c:otherwise>
</c:choose>
${new_string}
