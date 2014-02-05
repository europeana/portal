<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="item" required="true" rtexprvalue="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>
<%--
 * Parameter:
 * @item: the item
 --%>
<c:choose>
  <c:when test="${fn:startsWith(item, 'http://')}"><a href="${item}">${item}</a></c:when>
  <c:otherwise>${item}</c:otherwise>
</c:choose>
