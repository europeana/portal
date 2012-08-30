<!-- grid: ${model.results} -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<ul id="items">
	
	<c:set var="results" value="${model.briefBeanView.briefDocs}" />

	<!-- whitespace breaks layout, so the li elements need to run against one another -->
	<c:forEach var="cell" items="${results}"><li>
	
		<div class="thumb-frame">
		
			<c:set var="title" value="${cell.title[0]}" />
			<c:set var="icon_class" value="icon-${fn:toLowerCase(cell.type)}" />
			
			<%@ include file="/WEB-INF/jsp/default/search/content/results/grid/image.jsp" %>
			
		</div>
		<%@ include file="/WEB-INF/jsp/default/search/content/results/grid/title.jsp" %>
		
		
		<h2 class="title">
			<c:if test="${!empty cell.title}">
				${title}
			</c:if>
		</h2>
			<span class="${icon_class}"></span>
		<!-- /title -->
		
		
		<%@ include file="/WEB-INF/jsp/default/search/content/results/grid/additional-info.jsp" %>
				
	</li></c:forEach>
</ul>
<!-- /grid -->
