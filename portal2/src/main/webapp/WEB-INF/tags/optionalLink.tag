<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="item" required="true" rtexprvalue="true" %>
<%@ attribute name="label" required="false" rtexprvalue="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>
<%--
 * Parameter:
 * @item: the item
 --%>

<c:choose>
  <c:when test="${fn:startsWith(item, 'http://')}">
    <c:choose>
      <c:when test="${!empty label}">
        <a href="#${fn:replace(fn:replace(item, 'http://', ''), '/', '-')}">${label}</a>
      </c:when>
      <c:otherwise>
        <a href="${item}" class="icon-external-right">${item}</a>
      </c:otherwise>
    </c:choose>
  </c:when>
  <c:otherwise>${item}</c:otherwise>
</c:choose>
