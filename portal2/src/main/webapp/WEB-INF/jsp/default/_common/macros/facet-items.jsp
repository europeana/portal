<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:forEach items="${facet.links}" var="facet_item">

	<c:set var="checkedValue" value="" />
	<c:if test="${facet_item.remove}" >
		<c:set var="checkedValue" value='checked="checked"' />
	</c:if>

	<c:set var="label">
		<c:choose>
			<c:when test="${!empty facet_item.valueCode}"><spring:message code="${facet_item.valueCode}" /></c:when>
			<c:when test="${!empty facet_item.title}">${facet_item.title}</c:when>
			<c:otherwise>${facet_item.value}</c:otherwise>
		</c:choose>
	</c:set>

	<li>
		<h4>
			<input type="checkbox" ${checkedValue} id="cb-${fn:escapeXml(facet_item.value)}" name="cb-${label}"/>
			<c:set var="qHref">${fn:replace(model.query, '"', '&quot;')}${fn:replace(facet_item.url, '"', '&quot;')}${rowsParam}</c:set>
			<a href="/${model.portalName}/${model.pageName}?query=${fn:replace(qHref, '#', '%23')}"
				title="${fn:escapeXml(facet_item.value)}" rel="nofollow">
				<label for="cb-${fn:escapeXml(facet_item.value)}" style="display:inline">${label} (${facet_item.count}) </label>
			</a>
		</h4>
	</li>
</c:forEach>