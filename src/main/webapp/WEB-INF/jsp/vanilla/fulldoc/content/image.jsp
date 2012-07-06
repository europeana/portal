<!-- image -->
<c:if test="${model.imageRef}">
	
	<c:set var="urlRefIdImg"  value="urlRefIsShownAtImg"/>  
	<c:if test="${model.urlRefIsShownBy}">
  	<c:set var="urlRefIdImg" value="urlRefIsShownByImg"/>
  </c:if>

  <%-- <a id="${urlRefIdImg}" --%>
  
  <c:choose>
    <c:when test="${lightboxRef}">
		  <a href="/${model.portalName}/redirect.html?shownAt=${model.urlRef}&amp;provider=${model.document.europeanaDataProvider[0]}&amp;id=${model.document.id}" target="_blank"><img src="${model.thumbnailUrl}" alt="${model.pageTitle}"/></a>
      <div><span title="<spring:message code='view_t'/>" rel="#lightbox"><spring:message code="view_t"/></span></div>
    </c:when>
		<c:otherwise>
      <a href="${model.urlRef}" target="_blank" rel="nofollow" resource="${model.urlRef}" title="${model.pageTitle}"><img src="${model.thumbnailUrl}" alt="${model.pageTitle}" data-type="${model.document.europeanaType}"/><span about="${model.document.id}" rel="foaf:depiction"><span rel="foaf:thumbnail" resource="${model.thumbnailUrl}"></span></span></a>
			<%--
			<if ( model.europeanaIsShownBy??	&& model.europeanaIsShownAt??	&& model.europeanaIsShownBy[0] != model.europeanaIsShownAt[0] )
				|| model.europeanaIsShownBy?? && (model.document.europeanaIsShownBy[0]?ends_with("jpeg")
				||  model.document.europeanaIsShownBy[0]?ends_with("jpg")
				||  model.document.europeanaIsShownBy[0]?ends_with("gif")
				||  model.document.europeanaIsShownBy[0]?ends_with("png")
				||  model.document.europeanaIsShownBy[0]?ends_with("mp3")
				||  model.document.europeanaIsShownBy[0]?ends_with("mp4")
				||  model.document.europeanaIsShownBy[0]?ends_with("avi")
				||  model.document.europeanaIsShownBy[0]?ends_with("wmv")
				||  model.document.europeanaIsShownBy[0]?ends_with("txt")
				||  model.document.europeanaIsShownBy[0]?ends_with("pdf")>
      --%>
				<c:set var="shownBy" value="${model.document.europeanaIsShownBy[0]}"/>
				
				<c:choose>
				  <c:when test="${model.document.europeanaType =='TEXT'}">
            <div><a href="${shownBy}" rel="nofollow" target="_blank"><span title="<spring:message code='read_t'/>" rel="nofollow"><spring:message code='read_t'/></span></a></div>
          </c:when>
          <c:when test="${model.document.europeanaType == 'VIDEO'} || ${model.document.europeanaType == 'SOUND'}">
            <div><a href="${shownBy}" rel="nofollow" target="_blank"><span title="<spring:message code='play_t'/>" rel="nofollow"><spring:message code='play_t'/></span></a></div>
          </c:when>
          <c:when test="${model.document.europeanaType == 'IMAGE'}">	  
            <div><a href="${shownBy}" rel="nofollow" target="_blank"><span title="<spring:message code='view_t'/>" rel="nofollow"><spring:message code='view_t'/></span></a></div>
          </c:when>
          <c:when test="${model.document.europeanaType}">
				    <div><a href="${shownBy}" rel="nofollow" target="_blank"><span title="<spring:message code='view_t'/>" rel="nofollow"><spring:message code='view_t'/></span></a></div>
          </c:when>
        </c:choose>
				
      <%-- </if> --%>
      
    </c:otherwise>
    
   </c:choose>   
  
	<c:choose>
		<c:when test="${model.document.europeanaType == '3D'}">
	    <div>3d</div> 
		</c:when>
		<c:otherwise>
	    <div>object-type ${model.document.europeanaType}</div>
		</c:otherwise>
	</c:choose>
  
	<c:if test="${model.document.userGeneratedContent}">
	  <div title="<spring:message code='UserCreatedContent_t'/>"><spring:message code='UserCreatedContent_t'/></div>
	</c:if>
	
</c:if>
<!-- /image -->
