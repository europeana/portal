<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>



<!--Include div for lightbox -->
<div class="simple_overlay" id="lightbox">
	<div class="content-wrap">
	
		<span id="nav-prev" class="icon-arrow-4"></span>
		<span id="nav-next" class="icon-arrow-2"></span>
		<span id="nav-prev-zoomed" class="icon-arrow-4"></span>
		<span id="nav-next-zoomed" class="icon-arrow-2"></span>
	
		<!--	Note: the anchor style is used to size the media player (see eu.europeana.lightbox.playerOps for more info)
				the same width attributes need copied into the div element to keep the lightbox centred in IE7.								 
		-->
		
		<div class="playerDiv" style="display:block;width:0px;height:0px;">
		
			<%--a	href="/portal/.media?europeanaUri=${model.document.id}&recordId=${model.recordId}&lightboxRef=${lightboxRef}"
				style="display:block;width:500px;height:350px;"
				id="player"></a--%>
				
			<a href="${lightboxRef}" style="display:block; width:500px; height:350px;" id="player"></a>
		</div>

		<img id="lightbox_image" class="content-image" style="display:none" src=""/>
		
		<div class="lightbox-rights" id="rights-collapsed">
			<%@ include file="/WEB-INF/jsp/default/fulldoc/macros/rights.jsp" %>
		</div>
		<div class="info">
			<div class="hide_show_meta">
				<a class="showMeta"><spring:message code="see_details_t"/></a>
				<a class="hideMeta" style="display:none;"><spring:message code="hide_details_t"/></a>
			</div>
			<div class="info-open" style="display:none;">
				<c:if test="${!empty model.objectTitle}">
					<div class="item-metadata">
						<strong><spring:message code="dc_title_t"/>:</strong> ${model.objectTitle}
					</div>
				</c:if>
				<c:if test="${!empty model.fieldsLightbox && fn:length(model.fieldsLightbox) > 0}">
					<europeana:displayEseDataAsHtml listCollection="${model.fieldsLightbox}" wrapper="div" ugc="false" ess="false" />
				</c:if>
				<div class="original-context">
					<%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-left/original-context.jsp" %>
				</div>
			</div>
			
			
			<!-- div class="spacer"></div-->
			
			
			<%--js target div--%>
			<div id="lightbox-addthis"></div>
			<div class="direct-download">
			</div>
		</div>
	</div>
</div>
