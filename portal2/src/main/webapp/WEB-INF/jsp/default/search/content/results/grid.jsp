<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="items">

	<c:set var="results" value="${model.briefBeanView.briefBeans}" />
	
	<%-- Determine whether or not the search widget is active and set target as appropriate --%>
	
	<c:set var="targetArg" value="" />
	<c:if test="${model.embedded}">
		<c:set var="targetArg" value='target="_blank"' />
	</c:if>

	<c:forEach var="cell" items="${results}" varStatus="x"><div class="li"><!-- whitespace breaks layout - keep li elements together tight -->
	
		<div class="thumb-frame">
		
			<c:set var="title" value="${cell.titleBidi[0]}" />
			<c:set var="icon_class" value="icon-${fn:toLowerCase(cell.type)}" />
			
			<%@ include file="/WEB-INF/jsp/default/search/content/results/grid/image.jsp" %>
			
		</div>

		<!-- comments within the ellipsis div cause problems with the functionality! -->
		<a href="${fn:replace(cell.fullDocUrl, '"', '&quot;')}&rows=${model.rows}"
			<c:if test="${!empty cell.titleBidi}">
				title="${title}"
			</c:if>
			 ${targetArg}
			 rel="nofollow">
			<div class="ellipsis">
				<c:choose>
					<c:when test="${!empty cell.titleBidi}">${title}</c:when>
					<c:otherwise></c:otherwise>
				</c:choose>
				<span class="fixed"><span aria-hidden="true" class="${icon_class}"></span></span>
			</div>
		</a>

				
		<%--@ include file="/WEB-INF/jsp/default/search/content/results/grid/additional-info.jsp" --%>
		
	</div></c:forEach>
</div>
