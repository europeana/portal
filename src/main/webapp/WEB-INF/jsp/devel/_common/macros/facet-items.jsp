<!-- facet-items -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:forEach var="facet_item" items="${facet.links}">
	<c:set var="classAttr" value="" />
	<c:if test="${!empty facet_item.remove}" >
		<c:set var="classAttr" value=' class="active"' />
	</c:if>

	<c:set var="label">
		<c:choose>
			<c:when test="${!empty facet_item.value}">
				${facet_item.value}
			</c:when>
			<c:otherwise>
				${facet_item.title}
			</c:otherwise>
		</c:choose>
	</c:set>

	<li>
		<h4>
			<a  href="/${model.portalName}/${model.pageName}?query=${model.query}${facet_item.url}"
				title="${facet_item.value}" ${classAttr} rel="nofollow">${label} (${facet_item.count})</a>
		</h4>
	</li>
</c:forEach>
<!-- /facet-items -->
