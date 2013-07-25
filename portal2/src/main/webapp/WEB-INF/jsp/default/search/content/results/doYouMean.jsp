<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:if test="${model.briefBeanView.spellCheck.correctlySpelled}">
  <c:out value="Do you mean" />
  <c:forEach items="${model.briefBeanView.spellCheck.suggestions}" var="suggestion" varStatus="suggestionStatus">
    <c:if test="${!suggestionStatus.first}">&mdash;</c:if>
    <c:url value="/search.html" var="url">
      <c:param name="rows" value="${model.rows}" />
      <c:param name="query" value="${suggestion.label}" />
      <%--
        <c:forEach items="${model.refinements}" var="refinement">
          <c:param name="qf" value="${refinement}" />
        </c:forEach>
      --%>
    </c:url>
    <c:out value=" " /><a href="${url}">${suggestion.label}</a> <c:out value="(${suggestion.count} hits)" />
  </c:forEach>
  <c:out value="?" />
</c:if>