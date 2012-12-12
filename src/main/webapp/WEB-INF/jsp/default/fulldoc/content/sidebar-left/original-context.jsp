<!-- original-context -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="eu" tagdir="/WEB-INF/tags" %>

<%-- <#if model.urlRefMms> --%>

<c:set var="href_attributes" value=""/>
<c:if test="${not empty model['urlRef']}">
	<c:set var="href_attributes">class="underline external item-metadata" target="_blank" rel="nofollow rdfs:seeAlso cc:attributionURL cc:morePermissions" resource="${model.urlRef}"</c:set>
</c:if>

<c:set var="urlRefId" value="urlRefIsShownAt" />
<c:set var="urlRefField" value="edm:isShownAt" />
<c:if test="${model.urlRefIsShownBy}">
  <c:set var="urlRefId" value="urlRefIsShownBy" />
  <c:set var="urlRefField" value="edm:isShownBy" />
</c:if>

<c:set var="property"><eu:semanticAttributes field="${urlRefField}" schemaOrgMapping="${model.schemaOrgMapping}"></eu:semanticAttributes></c:set>

<c:if test="${not empty model['urlRef']}">

	<div class="clear"><spring:message code="ViewItemAt_t" /></div>

	<a id="${urlRefId}" class="icon-external-right europeana" href="/${model.portalName}/redirect.html?shownAt=<eu:encode url="${model.urlRef}" />&amp;provider=${model.document.dataProvider[0]}&amp;id=${model.document.about}" ${href_attributes} ${" "} ${property}>
		<c:choose>
			<c:when test="${not empty model.document['dataProvider'][0]}">
				  ${model.document['dataProvider'][0]}
			</c:when>
			<c:otherwise>
				  ${model.shownAtProvider}
			</c:otherwise>
		</c:choose>
	</a>
</c:if>
