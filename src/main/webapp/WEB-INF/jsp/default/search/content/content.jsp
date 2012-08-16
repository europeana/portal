<!-- content -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="content" class="row">
	<div class="twelve columns">
		<%@ include file="/WEB-INF/jsp/default/_common/html/navigation/breadcrumb.jsp" %>
	</div>
</div>
<div class="row">
	<div class="three columns">
		<%@ include file="/WEB-INF/jsp/default/_common/html/sidebar/sidebar.jsp" %>	
	</div>
	
	<div class="nine columns">
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
	
</div>
<!-- /content -->
