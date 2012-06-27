<c:if test="${!empty model.briefBeanView && !empty model.briefBeanView.pagination.presentationQuery.queryForPresentation}">
	<ul>
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
		<%-- previous arrow --%>
		<c:if test="${!empty model.briefBeanView.pagination.previous}">
			<li class="page-nr">
				<a href="${model.previousPageUrl}" class="pagination-previous" title="<spring:message code="AltPreviousPage_t" />"></a>
			</li>
		</c:if>
		<%-- next arrow --%>
		<c:if test="${!empty model.briefBeanView.pagination.next}">
			<li class="page-nr">
				<a href="${model.nextPageUrl}" class="pagination-next" title="<spring:message code="AltNextPage_t" />"></a>
			</li>
		</c:if>
	</ul>
</c:if>