<%@ include file="/WEB-INF/jsp/default/_common/include.jsp"%>

<%@ include file="/WEB-INF/jsp/default/_common/html/doctype.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/variables/variables.jsp" %>

<%@ include file="/WEB-INF/jsp/default/_common/html/open-html.jsp" %>
<div class="container">
  <%@ include file="/WEB-INF/jsp/default/_common/header.jsp" %>
  <div class="row">
    <p>by dates - by users - by types</p>
    <h3>By dates</h3>
    <c:forEach items="${model.dateStatistics}" var="stat">
      ${stat.key}: ${stat.value}<br>
    </c:forEach>

    <h3>By type</h3>
    <c:forEach items="${model.typeStatistics}" var="item">
      ${item.value.recordType}:  ${item.value.count}<br>
    </c:forEach>

    <h3>By users</h3>
    <c:forEach items="${model.userStatistics}" var="stat">
      ${stat.key}: ${stat.value}<br>
    </c:forEach>

  </div>
  <%@ include file="/WEB-INF/jsp/default/_common/footer.jsp" %>
</div>
<%@ include file="/WEB-INF/jsp/default/_common/html/javascripts.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/html/close-html.jsp" %>

