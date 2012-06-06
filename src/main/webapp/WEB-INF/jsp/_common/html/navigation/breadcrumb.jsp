<!-- breadcrumb -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<ul id="breadcrumb" class="notranslate">
	<c:if test="${!empty model.breadcrumbs}">
		<c:choose>
			<c:when test="${!empty !model.euroeanaUri}">
				<li><span class="bold black"><spring:message code="MatchesFor_t" />:</span></li>
				<c:forEach items="${model.breadcrumbs}" var="crumb">
					<li><a href="${crumb.href}" rel="nofollow">${crumb.display}</a>&#160;&gt;&#160;</li>
<%--
					<c:if test="${!empty crumb.showBreadCrumb}">
						<c:choose>
							<c:when test="${!empty crumb_has_next}">
								<li><a href="${crumb.breadCrumbUrl}" rel="nofollow">${crumb.display}</a>&#160;&gt;&#160;</li>
							</c:when>
							<c:otherwise>
								<li><b>${crumb.display}</b></li>
							</c:otherwise>
						</c:choose>
					</c:if>
--%>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<li>
					<spring:message code="ViewingRelatedItems_t" />
					<c:set var="match" value="${model.briefBeanView.matchDoc}" />
					<a id="relatedContentSourceDocLink" href="${match.id}" rel="nofollow">
						<img src="${match.thumbnail}" alt="${match.title}" height="25"/>
					</a>
				</li>
			</c:otherwise>
		</c:choose>
	</c:if>
</ul>
<!-- /breadcrumb -->