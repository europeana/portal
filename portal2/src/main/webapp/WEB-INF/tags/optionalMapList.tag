<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="map" required="true" type="java.util.Map" rtexprvalue="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>
<%--
  * param:
  * @map
  *   The Map<String, String>
 --%>
<c:choose>
  <c:when test="${fn:length(map) == 0}"></c:when>
  <c:when test="${fn:length(map) == 1}">
    <c:forEach items="${map}" var="item" varStatus="st">
      <europeana:optionalLink item="${item.key}" label="${item.value}" />
    </c:forEach>
  </c:when>
  <c:otherwise>
    <ul>
      <c:forEach items="${map}" var="item" varStatus="st">
        <li>
          <europeana:optionalLink item="${item.key}" label="${item.value}" />
        </li>
      </c:forEach>
    </ul>
  </c:otherwise>
</c:choose>
