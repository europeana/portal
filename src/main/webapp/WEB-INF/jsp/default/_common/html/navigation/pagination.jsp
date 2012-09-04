<!-- pagination -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:if test="${!empty model.briefBeanView && !empty model.briefBeanView.pagination.presentationQuery.queryForPresentation}">
	<ul class="navigation-pagination">
	


	
		<%-- total nr of results --%>
		<li class="page-nr">
			<spring:message code="Results_t" />&nbsp;${model.briefBeanView.pagination.start} - ${model.briefBeanView.pagination.lastViewableRecord}&nbsp;<spring:message code="Of_t" />&nbsp;${model.briefBeanView.pagination.numFound}
		</li>



		<%-- current page nr --%>
		<li class="page-nr">
			<spring:message code="Page_t" />:
		</li>

		<c:forEach var="link" items="${model.briefBeanView.pagination.pageLinks}">
			<c:choose>
				<c:when test="${!empty link.linked}">
					<li class="page-nr">
						<a href="${link.url}" title="<spring:message code="Page_t" /> ${link.display}">${link.display}</a>
					</li>
				</c:when>
				<c:otherwise>
					<li class="page-nr">
						<a href="" class="selected" title="<spring:message code="Page_t" /> ${link.display}">${link.display}</a>
					</li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		
		<%-- first arrow --%>
		<c:if test="${!model.briefBeanView.pagination.first}">
			<li class="page-nr">
				<a href="${model.firstPageUrl}" class="pagination-previous" title="<spring:message code="AltFirstPage_t" />"></a>
			</li>
		</c:if>
		
		<%-- previous arrow --%>
		<c:if test="${!empty model.briefBeanView.pagination.previous}">
			<li class="page-nr">
				<a href="${model.previousPageUrl}" class="pagination-previous" title="<spring:message code="AltPreviousPage_t" />"></a>
			</li>
		</c:if>
		
		
		<li class="page-nr">
			<form method="get" action="${query_action}?" id="jump-to-page">
				<input type="number" id="start-page" value="${model.pageNumber}"/> of ${model.numberOfPages}
				<input type="hidden" name="start"	id="start" />
				<input type="hidden" name="rows"	id="rows" value="${model.rows}" />
				<input type="hidden" name="query"	value="${model.query}"/>
				<input type="hidden" id="max-rows"	value="${model.numberOfPages}" />				
				<input type="submit" style="position: absolute; left: -9999px; width: 1px; height: 1px;"/>
			</form>
			
			
		</li>
		
		<%-- next arrow --%>
		<c:if test="${!empty model.briefBeanView.pagination.next}">
			<li class="page-nr">
				<a href="${model.nextPageUrl}" class="pagination-next" title="<spring:message code="AltNextPage_t" />"></a>
			</li>
		</c:if>
		<%-- last arrow --%>
		<c:if test="${!model.briefBeanView.pagination.last}">
			<li class="page-nr">
				<a href="${model.lastPageUrl}" class="pagination-next" title="<spring:message code="AltLastPage_t" />"></a>
			</li>
		</c:if>
	</ul>
</c:if>
<!-- pagination -->