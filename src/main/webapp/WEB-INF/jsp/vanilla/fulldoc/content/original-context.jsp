<#-- <#if model.urlRefMms> -->
<#assign href_attributes>class="underline external item-metadata" target="_blank" rel="nofollow rdfs:seeAlso cc:attributionURL cc:morePermissions" resource="${model.urlRef}"</#assign>

<div class="clear"><@spring.message 'ViewItemAt_t'/></div>

<#assign urlRefId = "urlRefIsShownAt"/>

<#if model.urlRefIsShownBy>

  <#assign urlRefId = "urlRefIsShownBy"/>

</#if>

<a id="${urlRefId}" href="/${model.portalName}/redirect.html?shownAt=${model.urlRef?url('utf-8')}&amp;provider=${model.document.europeanaDataProvider[0]?url('utf-8')}&amp;id=${model.document.id}" ${href_attributes}> 

     ${model.shownAtProvider}
  
</a>