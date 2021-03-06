<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>

<script type="text/javascript">
	window.lightboxMsg = ['<spring:message code="enter_fullscreen_t"/>', '<spring:message code="exit_fullscreen_t"/>'];
</script>

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

			<li class="bottom">
			 	<div class="action-link shares-link">
					<span class="icon-share" title="<spring:message code="Share_item_link_alt_t" />"><span class="action-title" title="<spring:message code="Share_item_link_alt_t" />"><spring:message code="Share_item_link_t" /></span></span>
				</div>
			</li>

		</ul>
	</div></div><a class="close"></a></div>

