<!-- original-context -->
<%-- <c:if test="${model.urlRefMms}"> --%>
<c:set value="href_attributes" value="target='_blank' rel='nofollow rdfs:seeAlso cc:attributionURL cc:morePermissions' resource='${model.urlRef}'"/>
<c:set var="urlRefId" value="urlRefIsShownAt"/>
<spring:message code='ViewItemAt_t'/>
<c:if test="${model.urlRefIsShownBy}">
  <c:set var="urlRefId" value="urlRefIsShownBy"/>
</c:if>
<a href="/${model.portalName}/redirect.html?shownAt=${model.urlRef}&amp;provider=${model.document.europeanaDataProvider[0]}&amp;id=${model.document.id}" ${href_attributes}>${model.shownAtProvider}</a>
<!-- /original-context -->
