<div id="content">
  <%@ include file="/WEB-INF/jsp/devel/_common/content/messages.jsp"%>
  <%@ include file="/WEB-INF/jsp/devel/_common/html/menus/sidebar.jsp"%>
  <c:choose>
    <c:when test="${!empty model.bodyContent}">
      ${model.bodyContent}
    </c:when>
    
    <c:when test="${!empty model.leftContent}">
    <h1>THIS IS THEW LEFTY CONTYENTNT</h1>
      ${model.leftContent}
    </c:when>
    <c:otherwise>
      <h2>Unable to find content for this page.</h2>
    </c:otherwise>
  </c:choose>
</div>