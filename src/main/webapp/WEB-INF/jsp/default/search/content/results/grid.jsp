<!-- grid: ${model.results} -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<ul id="items">
	
	<c:set var="results" value="${model.briefBeanView.briefDocs}" />

	<c:forEach var="cell" items="${results}">
		
		<c:set var="title" value="${cell.title[0]}" />
		<c:set var="icon_class" value="icon-${fn:toLowerCase(cell.type)}" />
		
		<li>
			<%@ include file="/WEB-INF/jsp/default/search/content/results/grid/image.jsp" %>
			<%@ include file="/WEB-INF/jsp/default/search/content/results/grid/title.jsp" %>
			<%@ include file="/WEB-INF/jsp/default/search/content/results/grid/additional-info.jsp" %>
			<span class="${icon_class}"></span>
		</li>

	</c:forEach>
</ul>
<!-- /grid -->
