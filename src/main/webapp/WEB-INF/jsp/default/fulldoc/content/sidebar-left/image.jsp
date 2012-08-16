<!-- image -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<c:if test="${!empty model.imageRef}">

	<%-- Andy
	
	New vars:
		urlRefIdImg (= urlRefIsShownAtImg by default or urlRefIsShownByImg if available)
	Model vars:
		urlRefIsShownAtImg
		urlRefIsShownByImg
		lightboxRef
		
	--%>

	<c:set var="urlRefIdImg" value="urlRefIsShownAtImg" />
	<c:if test="${model.urlRefIsShownBy}">
		<c:set var="urlRefIdImg" value="urlRefIsShownByImg"/>
		<a id="${urlRefIdImg}"></a>
	</c:if>

<c:choose>


<%-- Andy hack 
	<c:when test="${lightboxRef}"> 
--%>
	<c:when test="${lightboxRef || true}"> 
<%-- Andy hack end--%>


		<%-- Andy hack
			<a id="lightbox_href" href="/${model.portalName}/redirect.html?shownAt=${model.urlRef}&amp;provider=${model.document.europeanaDataProvider[0]}&amp;id=${model.document.id}" target="_blank">
		--%> 
		<a id="lightbox_href" href="/${model.portalName}/" target="_blank">
		<%-- Andy hack end--%>
			<img class="trigger" src="${model.thumbnailUrl}" alt="${model.pageTitle}"/>
		</a>
		<div class="trigger bold view">
			<span class="trigger read" title="<spring:message code="view_t" />" rel="#lightbox"><spring:message code="view_t" /></span>
		</div>
	</c:when>

	<c:otherwise>
		<a href="${model.urlRef}" class=" image ajax" target="_blank" rel="nofollow" resource="${model.urlRef}" id="${urlRefIdImg}" title="${model.pageTitle}">
			<img src="${model.thumbnailUrl}" alt="${model.pageTitle}" data-type="${fn:toLowerCase(model.document.type)}" id="item-image" class="trigger"/>
			<span about="${model.document.id}" rel="foaf:depiction">
				<span rel="foaf:thumbnail" resource="${model.thumbnailUrl}"></span>
			</span>
		</a>

		<c:if test='${(model[edmIsShownBy] && model[edmIsShownAt] && model.edmIsShownBy[0] != model.edmIsShownAt[0]) 
				|| (model[edmIsShownBy] && 
				(		fn:endsWith(model.document.edmIsShownBy[0], "jpeg")
					|| fn:endsWith(model.document.edmIsShownBy[0], "jpg")
					|| fn:endsWith(model.document.edmIsShownBy[0], "gif")
					|| fn:endsWith(model.document.edmIsShownBy[0], "png")
					|| fn:endsWith(model.document.edmIsShownBy[0], "mp3")
					|| fn:endsWith(model.document.edmIsShownBy[0], "mp4")
					|| fn:endsWith(model.document.edmIsShownBy[0], "avi")
					|| fn:endsWith(model.document.edmIsShownBy[0], "wmv")
					|| fn:endsWith(model.document.edmIsShownBy[0], "txt")
					|| fn:endsWith(model.document.edmIsShownBy[0], "pdf")
				))
				
				 || true
				
				}'>
				
			<c:set var="shownBy" value="${model.document.edmIsShownBy[0]}" />
			<c:choose>
				<c:when test="${fn:toLowerCase(model.document.edmType) == 'text'}">
					<div class="trigger read">
						<a id="urlRefIsShownByPlay" href="${shownBy}" rel="nofollow" target="_blank">
							<span class="trigger bold read" title="<spring:message code="read_t" />" rel="nofollow"><spring:message code="read_t" /></span>
						</a>
					</div>
				</c:when>

				<c:when test="${fn:toLowerCase(model.document.edmType) == 'video' || fn:toLowerCase(model.document.type) == 'sound'}">
					<div class="trigger play">
						<a id="urlRefIsShownByPlay" href="${shownBy}" rel="nofollow" target="_blank">
							<span class="trigger bold play" title="<spring:message code="play_t" />" rel="nofollow"><spring:message code="play_t" /></span>
						</a>
					</div>
				</c:when>
				
				<c:when test="${fn:toLowerCase(model.document.edmType) == 'image'}">
					<div class="trigger view">
						<a id="urlRefIsShownByPlay" href="${shownBy}" rel="nofollow" target="_blank">
							<span class="trigger bold view" title="<spring:message code="view_t" />" rel="nofollow"><spring:message code="view_t" /></span>
						</a>
					</div>
				</c:when>
			</c:choose>
			
			<c:if test="${fn:toLowerCase(model.document.edmType) == '3d'}">
				<div class="trigger view">
					<a id="urlRefIsShownByPlay" href="${shownBy}" rel="nofollow" target="_blank">
						<span class="trigger bold view" title="<spring:message code="view_t" />" rel="nofollow"><spring:message code="view_t" /></span>
					</a>
				</div>
			</c:if>
		</c:if>
	</c:otherwise>
</c:choose>
</c:if>

<%--div style="float:right;"--%>
<c:choose>
	<c:when test="${fn:toLowerCase(model.document.type) == '3d'}">
		<div class="object-type threeD"></div>
	</c:when>
	<c:otherwise>
		<div class="object-type ${fn:toLowerCase(model.document.type)}"></div>
	</c:otherwise>
</c:choose>

<c:if test="${model.document.userGeneratedContent}">
	<div class="item-ugc" title="<spring:message code="UserCreatedContent_t" />"></div>
</c:if>

<%--/div--%>
<!-- /image -->
