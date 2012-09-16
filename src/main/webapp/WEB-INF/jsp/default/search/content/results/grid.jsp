<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="items">
	
	<c:set var="results" value="${model.briefBeanView.briefDocs}" />

	<c:forEach var="cell" items="${results}"><div class="li"><!-- whitespace breaks layout - keep li elements together tight -->
	
		<div class="thumb-frame">
		
			<c:set var="title" value="${cell.title[0]}" />
			<c:set var="icon_class" value="icon-${fn:toLowerCase(cell.type)}" />
			
			<%@ include file="/WEB-INF/jsp/default/search/content/results/grid/image.jsp" %>
			
		</div>

		<!-- comments within the ellipsis div cause problems with the functionality! -->
		<div class="ellipsis">

				<c:choose>
					<c:when test="${!empty cell.title}">
						${title}
					</c:when>
					<c:otherwise>
						 &nbsp;
					</c:otherwise>
				</c:choose>				

			<span class="fixed"><span aria-hidden="true" class="${icon_class}"></span></span>
		</div>

				
		<%--@ include file="/WEB-INF/jsp/default/search/content/results/grid/additional-info.jsp" --%>
		
	</div></c:forEach>
</div>
