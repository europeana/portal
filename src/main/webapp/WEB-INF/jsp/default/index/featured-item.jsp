<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="six columns">
	<h3 id="collapse-header-2">
		<span class="left collapse-header-text"><spring:message code="featured-content-title_t" /></span>
		<span class="collapse-icon"></span>
		<a class="feed-link" href="http://pinterest.europeana.eu.feed" target="_blank" title="RSS Feed"></a>
	</h3>
 
	<div class="collapse-content">
	
	
		<c:if test="${!empty model.featuredItem}">
		
			<div class="fi-block-spacer">
	
				<h4 class="show-on-phones">
					<a	href	= "/${model.portalName}<spring:message code="${model.featuredItem.anchorUrl}" />"
						title	= "<spring:message code="${model.featuredItem.anchorTitle}" />"
						target	= "<spring:message code="${model.featuredItem.anchorTarget}" />"
						class	= "europeana">
						<spring:message code="${model.featuredItem.heading}" />	
					</a>
				</h4>
			
		
				<a	href=	"/${model.portalName}<spring:message code="${model.featuredItem.anchorUrl}" />"
					title=	"<spring:message code="${model.featuredItem.anchorTitle}" />"
					target=	"<spring:message code="${model.featuredItem.anchorTarget}" />"
					class=	"image">
					<img	src=	"/${model.portalName}<spring:message code="${model.featuredItem.imgUrl}" />"
							alt=	"<spring:message code="${model.featuredItem.imgAlt}" />"
						
							/>
				</a>

				<h4 class="hide-on-phones">
					<a	href	= "/${model.portalName}<spring:message code="${model.featuredItem.anchorUrl}" />"
						title	= "<spring:message code="${model.featuredItem.anchorTitle}" />"
						target	= "<spring:message code="${model.featuredItem.anchorTarget}" />"
						class	= "europeana">
						<spring:message code="${model.featuredItem.heading}" />	
					</a>
				</h4>
								
				<spring:message code="${model.featuredItem.p}" />
			</div>

		</c:if>

	</div>
</div>



<div class="six columns" id="section-featured-partner">


	<c:if test="${!empty model.featuredPartner}">
	
		<!-- collapsible header  -->
		<div class="row">
			<div class="twelve columns" id="featured-partner-header-wrapper">
				<div id="partner-section-heading" class="fi-block-spacer">
					<h3 id="section-header-featured-partner" class="collapse-header-text"><spring:message code="featured-partner-title_t" /></h3>
				</div>
			</div>
		</div>
		
		<div class="fi-block-spacer">

			<h4 class="show-on-phones">
				<a	href	= "/${model.portalName}<spring:message	code="${model.featuredPartner.anchorUrl}" />"
					title	= "<spring:message	code="${model.featuredPartner.anchorTitle}" />"
					target	= "<spring:message	code="${model.featuredPartner.anchorTarget}" />"
					class	= "europeana">
					<spring:message code="${model.featuredPartner.heading}" />	
				</a>
			</h4>
		
	
			<a	href=	"/${model.portalName}<spring:message code="${model.featuredPartner.anchorUrl}" />"
				title=	"<spring:message code="${model.featuredPartner.anchorTitle}" />"
				target=	"<spring:message code="${model.featuredPartner.anchorTarget}" />"
				class=	"image">
				<img	src=	"/${model.portalName}<spring:message code="${model.featuredPartner.imgUrl}" />"
						alt=	"<spring:message code="${model.featuredPartner.imgAlt}" />"
					
						/>
			</a>

			<h4 class="hide-on-phones">
				<a	href	= "/${model.portalName}<spring:message	code="${model.featuredPartner.anchorUrl}" />"
					title	= "<spring:message	code="${model.featuredPartner.anchorTitle}" />"
					target	= "<spring:message	code="${model.featuredPartner.anchorTarget}" />"
					class	= "europeana">
					<spring:message code="${model.featuredPartner.heading}" />	
				</a>
			</h4>
							
			<spring:message code="${model.featuredPartner.p}" />
			
			<ul class="featured-partner-links featured-text">
				<li>
					<a	href	= "<spring:message code="${model.featuredPartner.visitLink}"  />"
						target	= "<spring:message code="notranslate_featured-partner-visit_target_t" />"
						rel		= "nofollow"
						class	= "icon-external-right europeana">
						<spring:message code="featured-partner-visit_text_t"   />&nbsp;
						<spring:message code="featured-partner-visit_name_t"   />
					</a>
				</li>

				<%--
				<li>
					<a	href	= "/${model.portalName}/<spring:message code="notranslate_featured-partner-view_link_t" />"
						target	= "<spring:message code="notranslate_featured-partner-view_target_t" />"
						class	= "europeana">
						<spring:message code="featured-partner-view_text_t" />
					</a>
				</li>
				 --%>

			</ul>

		</div>
		
	</c:if>
	
</div>

