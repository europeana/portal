<ul>

	<c:set var="row_counter" value="1" />
	<c:set var="cell_counter" value="1" />
	<c:set var="results" value="${model.briefBeanView.briefBeans}" />
  
	<c:forEach var="cell" items="${results}">
  
		<!-- ${cell} -->
		<c:set var="row_counter" value="1" />
		<%-- c:set var="title" value="${fn:substring(cell.title[0], 0, titleMaxLength)}" /--%>
		<c:set var="title" value="${cell.title[0]}" />
		<c:set var="icon_class" value=" type-${fn:toLowerCase(cell.type)}" />
    
    <%--
			<c:if test="${!empty cell.parent && cell.parent > 4}">
				<c:set var="icon_class" value=" parent" />
			</c:if>
    --%>
    
		<c:set var="li_class" value="" />
		<c:if test="${cell_counter == 4}">
			<c:set var="li_class" value=" last" />
		</c:if>
		<c:if test="${row_counter == 4}">
			<c:set var="li_class" value="${li_class + ' bottom'}" />
		</c:if>
    
		<%-- item in grid --%>
		<li>
			<%@ include file="/WEB-INF/jsp/default/search/content/results/grid/image.jsp" %>
			<%@ include file="/WEB-INF/jsp/default/search/content/results/grid/title.jsp" %>
			<%@ include file="/WEB-INF/jsp/default/search/content/results/grid/additional-info.jsp" %>
		</li>
    
		<c:set var="cell_counter" value="${cell_counter + 1}" />
		<c:if test="${cell_counter > 4}">
			<c:set var="cell_counter" value="1" />
		</c:if>

		<c:set var="row_counter" value="${row_counter + 1}" />
		<c:if test="${row_counter > 3}">
			<c:set var="row_counter" value="1" />
		</c:if>
    
	</c:forEach>
  
</ul>