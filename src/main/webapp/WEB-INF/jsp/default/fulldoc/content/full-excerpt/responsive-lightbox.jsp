<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>


<div id="lightbox-proxy"><div class="lightbox_content_wrap">


		<a id="nav-prev" class="icon-arrow-4" onClick="eu.europeana.fulldoc.lightboxOb.prev();" ></a>
		<a id="nav-next" class="icon-arrow-2" onClick="eu.europeana.fulldoc.lightboxOb.next();" ></a>
		<a id="zoomIn" onClick="eu.europeana.fulldoc.lightboxOb.zoom();"></a>
		
		<img
		id="lightbox_image"
		/><div id="lightbox_info">
			<ul>
				<li class="hide_show_meta">
					<a class="showMeta"><spring:message code="see_details_t"/></a>
					<a class="hideMeta"><spring:message code="hide_details_t"/></a>
				</li>
				
				<c:if test="${!empty model.objectTitle}">
					<li class="title"><strong><spring:message code="dc_title_t"/>:</strong></li>
				</c:if>

			</ul>
		</div></div><div class="close"></div></div>


<%--
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
						<europeana:displayEseDataAsHtml listCollection="${model.fieldsLightbox}" wrapper="div" ugc="false" ess="false"/>
					</c:if>
					<div class="original-context">
						<%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-left/original-context.jsp" %>
					</div>
				</div>
				<div class="info-bottom">
					<div class="action-link shares-link">
						<span class="icon-share" title="<spring:message code="Share_item_link_alt_t" />">
							<span class="action-title" title="<spring:message code="Share_item_link_alt_t" />"><spring:message code="Share_item_link_t" /></span>
						</span>
					</div>
					<div class="lightbox-rights" id="rights-collapsed">
            <c:set var="inLightbox" scope="request" value="true"/>
						<%@ include file="/WEB-INF/jsp/default/fulldoc/macros/rights.jsp" %>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
--%>