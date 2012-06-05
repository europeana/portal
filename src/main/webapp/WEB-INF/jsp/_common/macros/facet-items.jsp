<!-- facet-items -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:forEach var="facet_item" items="${facet.fields}">

	<c:set var="classAttr" value=''/>
	<%-- 
	<c:if test="${!empty facet_item.remove}" >
		<c:set var="classAttr" value=' class="active"'/>
	</c:if>
	--%>

	<li>
		<h4>
			<%-- a  href="/${model.portalName}/${model.pageName}?query=${model.query}${facet_item.url}" 
			title="${facet_item.value}" --%>
			<a  href="/${model.portalName}/${model.pageName}?query=${model.query}${facet_item.label}"
				title="${facet_item.label}"
				${classAttr}
				rel="nofollow">
				<c:choose>
					<c:when test="${!empty facet_item.label}">
						${facet_item.label}
					</c:when>
					<c:otherwise>
						${facet_item.title}
					</c:otherwise>
				</c:choose>
				(${facet_item.count})
			</a>
		</h4>
	</li>
</c:forEach>
<!-- /facet-items -->
