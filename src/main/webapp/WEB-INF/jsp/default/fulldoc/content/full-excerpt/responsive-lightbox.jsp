<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>

<div class="simple_overlay" id="lightbox">
	<div class="content-wrap">
	
		<span id="nav-prev" class="icon-arrow-4"></span>
		<span id="nav-next" class="icon-arrow-2"></span>

		<img id="lightbox_image" class="content-image" style="display:none" src=""/>
		
		<div class="info">
			<div class="hide_show_meta">
				<a class="showMeta"><spring:message code="see_details_t"/></a>
				<a class="hideMeta" style="display:none;"><spring:message code="hide_details_t"/></a>
			</div>
			<div class="info-open" style="display:none;">			
				<div class="info-top">
				
					<c:if test="${!empty model.objectTitle}">
						<div class="item-metadata">
							<strong><spring:message code="dc_title_t"/>:</strong> ${model.objectTitle}
						</div>
					</c:if>
					
					<c:if test="${!empty model.fieldsLightbox && fn:length(model.fieldsLightbox) > 0}">
						<europeana:displayEseDataAsHtml listCollection="${model.fieldsLightbox}" wrapper="div" ugc="false" ess="false" />
					</c:if>
					
					<div class="action-link shares-link">
						<span class="icon-share" title="<spring:message code="Share_item_link_alt_t" />">
							<span class="action-title" title="<spring:message code="Share_item_link_alt_t" />"><spring:message code="Share_item_link_t" /></span>
						</span>
					</div>
					
				</div>
					
				<div class="info-bottom">
					<div class="original-context">
						<%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-left/original-context.jsp" %>
					</div>
					<div class="lightbox-rights" id="rights-collapsed">
						<%@ include file="/WEB-INF/jsp/default/fulldoc/macros/rights.jsp" %>
					</div>
				
				</div>
				
			</div>

		</div>
	</div>
</div>
