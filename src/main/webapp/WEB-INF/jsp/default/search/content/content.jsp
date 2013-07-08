<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row" id="content">
	<c:choose>
		<c:when test="${empty param.showFacets || param.showFacets=='true'}">
			<div class="nine columns push-three">
				<%@ include file="/WEB-INF/jsp/default/search/content/results/results.jsp" %>
			</div>
			<div class="three columns pull-nine">
				<%@ include file="/WEB-INF/jsp/default/_common/html/sidebar/sidebar.jsp" %>
			</div>
		</c:when>
		<c:otherwise>
			<div class="twelve columns">
				<%@ include file="/WEB-INF/jsp/default/search/content/results/results.jsp" %>
			</div>
		</c:otherwise>
	</c:choose>
</div>
