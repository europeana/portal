<!-- original-context -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="eu" tagdir="/WEB-INF/tags" %>

<%-- <#if model.urlRefMms> --%>

<c:set var="href_attributes" value=""/>
<c:if test="${not empty model['urlRef']}">
	<c:set var="href_attributes">target="_blank" rel="nofollow rdfs:seeAlso cc:attributionURL cc:morePermissions" resource="${model.urlRef}"</c:set>
</c:if>

<c:set var="classes" value="icon-external-right europeana"/>
<c:if test="${not empty model['urlRef']}">
	<c:set var="classes">${classes} underline external item-metadata</c:set>
</c:if>

<c:set var="urlRefId" value="urlRefIsShownAt" />
<c:if test="${model.urlRefIsShownBy}">
	<c:set var="urlRefId" value="urlRefIsShownBy" />
</c:if>

<c:set var="provider">
	<c:choose>
		<c:when test="${not empty model.document['dataProvider'][0]}">${model.document['dataProvider'][0]}</c:when>
		<c:otherwise>${model.shownAtProvider}</c:otherwise>
	</c:choose>
</c:set>

<c:if test="${not empty model['urlRef']}">
	<div class="clear"><spring:message code="ViewItemAt_t" /></div>
	<a about="${model.document.cannonicalUrl}" id="${urlRefId}" class="${classes}" href="/${model.portalName}/redirect.html?shownAt=<eu:encode url="${model.urlRef}" />&amp;provider=${fn:escapeXml(provider)}&amp;id=${model.document.about}" ${href_attributes}>${provider}</a>
</c:if>