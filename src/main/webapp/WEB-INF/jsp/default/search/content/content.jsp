<!-- content -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="content">
	<%@ include file="/WEB-INF/jsp/default/_common/html/navigation/breadcrumb.jsp" %>
	<%@ include file="/WEB-INF/jsp/default/_common/html/sidebar/sidebar.jsp" %>
	
	<c:choose>
		<c:when test="${view == 'tlv'}">
			<%@ include file="/WEB-INF/jsp/default/timeline/content/timeline.jsp" %>
		</c:when>
		<c:when test="${view == 'map'}">
			<%@ include file="/WEB-INF/jsp/default/map/content/map.jsp" %>
		</c:when>
		<c:otherwise>
			<%@ include file="/WEB-INF/jsp/default/search/content/results/results.jsp" %>
		</c:otherwise>
	</c:choose>
	<div class="clear"></div>
</div>
<!-- /content -->
