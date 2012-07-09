<h3><a  href="#saved-items"><spring:message code='SavedItems_t'/></a></h3>
<c:choose>
  <c:when test="${fn:length(model.user.savedItems) < 1 }">
    <spring:message code='NoSavedItems_t'/>
  </c:when>
<c:otherwise>
<%-- Iterate over Saved Items --%>
<c:forEach items="${model.user.savedItems}" var="item">
  <%-- Item Image --%>
    <%-- Determine fallback image for this item --%>
	    <c:set var='imgtoshow' value="item-image.gif"/>
	    <c:set var='docType' value="${item.docType}"/>
<#switch item.docType>
<#case "IMAGE">
<#assign imgtoshow = "item-image.gif"/>
<#break/>
<#case "VIDEO">
<#assign imgtoshow = "item-video.gif"/>
<#break/>  
<#case "SOUND">
<#assign imgtoshow = "item-sound.gif"/>
<#break/>  
<#case "TEXT">
<#assign imgtoshow = "item-text.gif"/>
<#break/>
<#case "_3D">
<#assign imgtoshow = "item-3d.gif"/>
<#assign docType = "3d"/>
<#break/>
</#switch>
<%-- Place Image --%>
        
          <a href='/${model.portalName}/${item.getEuropeanaUri()?replace("http://www.europeana.eu/resolve/", "")}.html?bt=savedItem' title="${item.title}">
          
            <#if item.europeanaObject??>
            
                  <img src="${model.cacheUrl}uri=${item.europeanaObject?url('utf-8')}&size=BRIEF_DOC&type=${docType}" alt="${item.title}"/>
                  
            <#else/>
            
              <img src="/${branding}/images/icons/item-types/${imgtoshow}" alt="${item.title}"/>
              
            </#if>
            
          </a>
      
      
      <#--
        Item Info
      -->
        
        <#--
          !item.europeanaId.orphan
        -->
        
          <a href='/${model.portalName}/${item.getEuropeanaUri()?replace("http://www.europeana.eu/resolve/", "")}.html?bt=savedItem' title="${item.title}" class="bold"><@stringLimiter "${item.title}" "50"/></a><br/>
          <spring:message code='Creator_t'/>: <em>${item.author}</em><br/>
          <spring:message code='DateSaved_t'/>: <em>${item.dateSaved?datetime}</em><br/>
      

      <#--
        Delete Saved Item button
      -->
      
        <button id="${item.id?string("0")}" class="remove-saved-item submit-button"><spring:message code='Delete_t'/></button>
</c:forEach>    
</c:otherwise>
</c:choose>