<!-- content -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="content">
  <%@ include file="/WEB-INF/jsp/devel/_common/content/messages.jsp"%>
  <%@ include file="/WEB-INF/jsp/devel/_common/html/navigation/breadcrumb.jsp"%>
  <%@ include file="/WEB-INF/jsp/devel/_common/html/sidebar/sidebar.jsp"%>

  <c:choose>
    <c:when test="${view == 'tlv'}">
      <%@ include file="/WEB-INF/jsp/devel/timeline/content/timeline.jsp"%>
    </c:when>
    <c:when test="${view == 'map'}">
      <%@ include file="/WEB-INF/jsp/devel/map/content/map.jsp"%>
    </c:when>
    <c:otherwise>
      <%@ include file="/WEB-INF/jsp/devel/search/content/results/results.jsp"%>
    </c:otherwise>
  </c:choose>
  <div class="clear"></div>
</div>
<!-- /content -->
