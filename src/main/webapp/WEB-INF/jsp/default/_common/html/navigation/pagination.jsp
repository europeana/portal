<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${!empty model.briefBeanView && !empty model.briefBeanView.pagination.presentationQuery.queryForPresentation}">

	<div>
		<ul class="result-pagination">
			
			<%-- first arrow --%>
			<c:if test="${!model.briefBeanView.pagination.first}">
				<li>
					<a href="${model.firstPageUrl}" title="<spring:message code="AltFirstPage_t" />">&lt; &lt; &nbsp;</a>
				</li>
			</c:if>
			
			<%-- previous arrow --%>
			<c:if test="${model.briefBeanView.pagination.previous}">
				<li>
					<a href="${model.previousPageUrl}" title="<spring:message code="AltPreviousPage_t" />"> &lt; &nbsp; </a>
				</li>
			</c:if>
			
			<li class="page-nr">
				<form method="get" action="${query_action}?" id="jump-to-page">
					<input type="number" id="start-page" value="${model.pageNumber}" min="1" max="${model.numberOfPages}" /> of ${model.numberOfPages}
					<input type="hidden" name="start"	id="start" />
					<input type="hidden" name="rows"	id="rows" value="${model.rows}" />
					<input type="hidden" name="query"	value="${model.query}"/>
					<input type="hidden" id="max-rows"	value="${model.numberOfPages}" />				
					<input type="submit" style="position: absolute; left: -9999px; width: 1px; height: 1px;"/>
				</form>
			</li>
			
			<%-- next arrow --%>
			<c:if test="${model.briefBeanView.pagination.next}">
				<li>
					<a href="${model.nextPageUrl}" title="<spring:message code="AltNextPage_t" />"> &nbsp; &gt; </a>
				</li>
			</c:if>
			
			<%-- last arrow --%>
			<c:if test="${!model.briefBeanView.pagination.last}">
				<li>
					<a href="${model.lastPageUrl}" title="<spring:message code="AltLastPage_t" />">&nbsp; &gt; &gt;</a>
				</li>
			</c:if>
		</ul>
		
		<div style="clear:both;"></div>
		
	</div>

</c:if>
