<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${!empty model.messages}">
  <ul id="messages">
    <c:forEach items="${model.messages}" var="message">
      <li>${message}</li>
    </c:forEach>
  </ul>
</c:if>