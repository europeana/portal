<!-- content -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="row">
	
	<div class="nine columns push-three">
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
	</div>

	<div class="three columns pull-nine">
		<%@ include file="/WEB-INF/jsp/default/_common/html/sidebar/sidebar.jsp" %>	
	</div>

	
</div>
<!-- /content -->
