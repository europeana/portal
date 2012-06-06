<!-- grid: ${model.results} -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<ul id="items">
	<c:set var="row_counter" value="1" />
	<c:set var="cell_counter" value="1" />
	<c:forEach var="cell" items="${model.results}">
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

		<li class="item${li_class}${icon_class}">
			<%@ include file="/WEB-INF/jsp/search/content/results/grid/image.jsp" %>
			<%@ include file="/WEB-INF/jsp/search/content/results/grid/title.jsp" %>
			<%@ include file="/WEB-INF/jsp/search/content/results/grid/additional-info.jsp" %>
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
<!-- /grid -->
