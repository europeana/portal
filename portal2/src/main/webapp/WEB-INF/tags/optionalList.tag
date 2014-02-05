<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="list" required="true" type="java.lang.String[]" rtexprvalue="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>
<%--
 --%>
<c:choose>
  <c:when test="${fn:length(list) == 1}">
    <europeana:optionalLink item="${list[0]}" />
  </c:when>
  <c:otherwise>
    <ul>
      <c:forEach items="${list}" var="item" varStatus="st">
        <li>
          <europeana:optionalLink item="${item}" />
        </li>
      </c:forEach>
    </ul>
  </c:otherwise>
</c:choose>
