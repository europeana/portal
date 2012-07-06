<ul>
	<c:if test="${not empty model.breadcrumbs}">
		<c:choose>
			<c:when test="${!empty !model.euroeanaUri}">
				<li><span><spring:message code="MatchesFor_t" />:</span></li>
				<c:set var="breadcrumbs" value="${model.breadcrumbs}" />
				<c:forEach items="${breadcrumbs}" var="crumb">
					<c:if test="${crumb.showBreadCrumb}">
						<c:choose>
							<c:when test="${crumb.isLast}">
								<li><b>${crumb.display}</b></li>
							</c:when>
							<c:otherwise>
								<li><a href="${crumb.breadCrumbUrl}" rel="nofollow">${crumb.display}</a>&#160;&gt;&#160;</li>
							</c:otherwise>
						</c:choose>
					</c:if>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<li>
					<spring:message code="ViewingRelatedItems_t" />
					<c:set var="match" value="${model.briefBeanView.matchDoc}" />
					<a id="relatedContentSourceDocLink" href="${match.id}" rel="nofollow">
						<img src="${match.thumbnail}" alt="${match.title}" height="25" />
					</a>
				</li>
			</c:otherwise>
		</c:choose>
	</c:if>
</ul>