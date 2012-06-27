<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>

	<%-- @ include file="/WEB-INF/jsp/default/_common/html/navigation/breadcrumb.jsp" --%>
	<%-- @ include file="/WEB-INF/jsp/default/_common/html/sidebar/sidebar.jsp" --%>
	
	<c:choose>
		<c:when test="${view == 'tlv'}">
			<%@ include file="/WEB-INF/jsp/vanilla/timeline/content/timeline.jsp" %>
		</c:when>
		<c:when test="${view == 'map'}">
			<%@ include file="/WEB-INF/jsp/vanilla/map/content/map.jsp" %>
		</c:when>
		<c:otherwise>
			<%@ include file="/WEB-INF/jsp/vanilla/search/results/results.jsp" %>
		</c:otherwise>
	</c:choose>
  
</div>