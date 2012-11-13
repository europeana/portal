<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/WEB-INF/jsp/_common/tag-libraries.jsp" %>


	<c:choose>
	
	
	
	 	<c:when test="${not empty param.blog}">
 		{
 			"markup":"
 				<c:choose>
					<c:when test='${not empty model.feedEntries}'>
						<c:forEach items="${model.feedEntries}" var="entry" varStatus="status">
							<c:if test="${status.index < 2}">
		
								<div class='six columns'>
		
									<div class='fi-block-spacer'>
							
										<h4 class='show-on-phones'>
											<a	href=	'${entry.link}'
												title=	'${entry.title}'
												target=	'_self'
												class=	'europeana'>
												${entry.title}
											</a>
										</h4>
								
										<a	href=	'${entry.link}'
											title=	'${entry.title}'
											target=	'_self'
											class=	'image'>
											<img class='responsive' src='/${model.portalName}${entry.images[0].responsiveFileNames['_1']}' alt='${entry.title}' />
										</a>
					
										<h4 class='hide-on-phones'>
											<a	href=	'${entry.link}'
												title=	'${entry.title}'
												target=	'_self'
												class=	'europeana'>
												${entry.title}
											</a>
										</h4>
		
										<p class='featured-text'>
											${entry.description}
										</p>
						
									</div>
											
								</div>
							</c:if>
						</c:forEach>
					</c:when>
					<c:otherwise>
					  <!--
					    fallback content in case the blog feed is not working
					    or if the portal server blog feed cache is cleared after a server restart
					  -->
					  <h3><a href='<spring:message code="notranslate_blog-item-1_a_url"/>' target='<spring:message code="notranslate_blog-item-1_a_target"/>'><spring:message code="notranslate_blog-item-1_h3"/></a></h3> 
					  <p><spring:message code='notranslate_blog-item-1_p'/></p>
					  <h3><a href='<@spring.message "notranslate_blog-item-2_a_url"/>' target='<spring:message code="notranslate_blog-item-2_a_target"/>'><spring:message code="notranslate_blog-item-2_h3"/></a></h3> 
					  <p><spring:message code="notranslate_blog-item-2_p"/></p>  
					  <h3><a href='<spring:message code="notranslate_blog-item-3_a_url"/>' target='<spring:message code="notranslate_blog-item-3_a_target"/>'><spring:message code="notranslate_blog-item-3_h3"/></a></h3> 
					  <p><spring:message code="notranslate_blog-item-3_p"/></p>
					</c:otherwise>
				</c:choose> 			
 			"
 		}
 	</c:when>
 	
 	<c:when test="${not empty param.featuredItem}">
 		{
 			"markup":"

		 		<c:if test="${!empty model.featuredItem}">
				
					<div class='fi-block-spacer'>
			
						<h4 class='show-on-phones'>
							<a	href	= '/${model.portalName}<spring:message code="${model.featuredItem.anchorUrl}" />'
								title	= '<spring:message code="${model.featuredItem.anchorTitle}" />'
								target	= '<spring:message code="${model.featuredItem.anchorTarget}" />'
								class	= 'europeana'>
								<spring:message code="${model.featuredItem.heading}" />	
							</a>
						</h4>
					
						<a	href=	'/${model.portalName}<spring:message code="${model.featuredItem.anchorUrl}" />'
							title=	'<spring:message code="${model.featuredItem.anchorTitle}" />'
							target=	'<spring:message code="${model.featuredItem.anchorTarget}" />'
							class=	'image'>
							<img	src=	'/${model.portalName}<spring:message code="${model.featuredItem.imgUrl}" />'
									alt=	'<spring:message code="${model.featuredItem.imgAlt}" />'
								
									/>
						</a>
		
						<h4 class='hide-on-phones'>
							<a	href	= '/${model.portalName}<spring:message code="${model.featuredItem.anchorUrl}" />'
								title	= '<spring:message code="${model.featuredItem.anchorTitle}" />'
								target	= '<spring:message code="${model.featuredItem.anchorTarget}" />'
								class	= 'europeana'>
								<spring:message code="${model.featuredItem.heading}" />	
							</a>
						</h4>
										
						<spring:message code="${model.featuredItem.p}" />
					</div>
		
				</c:if>
			",
			"markup2" : "
				<div class='six columns' id='section-featured-partner'>
				
					<c:if test="${!empty model.featuredPartner}">
					
						<!-- collapsible header  -->
						<div class='row'>
							<div class='twelve columns' id='featured-partner-header-wrapper'>
								<div id='partner-section-heading' class='fi-block-spacer'>
									<h3 id='section-header-featured-partner' class='collapse-header-text'><spring:message code='featured-partner-title_t' /></h3>
								</div>
							</div>
						</div>
						
						<div class='fi-block-spacer'>
				
							<h4 class='show-on-phones'>
								<a	href	= '/${model.portalName}<spring:message	code="${model.featuredPartner.anchorUrl}" />'
									title	= '<spring:message	code="${model.featuredPartner.anchorTitle}" />'
									target	= '<spring:message	code="${model.featuredPartner.anchorTarget}" />'
									class	= 'europeana'>
									<spring:message code="${model.featuredPartner.heading}" />	
								</a>
							</h4>
						
					
							<a	href=	'/${model.portalName}<spring:message code="${model.featuredPartner.anchorUrl}" />'
								title=	'<spring:message code="${model.featuredPartner.anchorTitle}" />'
								target=	'<spring:message code="${model.featuredPartner.anchorTarget}" />'
								class=	'image'>
								<img	src=	'/${model.portalName}<spring:message code="${model.featuredPartner.imgUrl}" />'
										alt=	'<spring:message code="${model.featuredPartner.imgAlt}" />'
									
										/>
							</a>
				
							<h4 class='hide-on-phones'>
								<a	href	= '/${model.portalName}<spring:message	code="${model.featuredPartner.anchorUrl}" />'
									title	= '<spring:message	code="${model.featuredPartner.anchorTitle}" />'
									target	= '<spring:message	code="${model.featuredPartner.anchorTarget}" />'
									class	= 'europeana'>
									<spring:message code="${model.featuredPartner.heading}" />
								</a>
							</h4>
											
							<spring:message code="${model.featuredPartner.p}" />
							
							<ul class='featured-partner-links featured-text'>
								<li>
									<a	href	= '<spring:message code="${model.featuredPartner.visitLink}"  />'
										target	= '<spring:message code="notranslate_featured-partner-visit_target_t" />'
										rel		= 'nofollow'
										class	= 'icon-external-right europeana'>
										<spring:message code="featured-partner-visit_text_t"   />&nbsp;
										<spring:message code="featured-partner-visit_name_t"   />
									</a>
								</li>
							</ul>
				
						</div>
						
					</c:if>
					
				</div>			
			"
		}
 	</c:when>
 	
 	
 	
 	<c:when test="${not empty param.pinterest}">
 	
 		{
 			"markup":"
				<c:if test="${not empty model.pinterestItems}">
					<div id='carousel-3' class='europeana-carousel'>
						<c:forEach var="item" items="${model.pinterestItems}">
							<a href='${item.link}' class='hidden'>
								<img	src		= '${item.images[0].src}'
										alt		= '${fn:escapeXml(item.plainDescription)}'
										title	= '${fn:escapeXml(item.plainDescription)}'
								/>	
							</a>
						</c:forEach>
					</div>
				</c:if>
			",
			
 			"data":{

	            "carousel3Data" : [
				<c:forEach var="item" items="${model.pinterestItems}" varStatus="status">
					{
						"thumb":		"${item.images[0].src}",
						"title":		"${fn:escapeXml(item.plainDescription)}",
						"link":			"${item.link}",
						"linkTarget":	"_new"
					}
					<c:if test="${not status.last}">,</c:if>
				</c:forEach>
	            ]
 			
 			},
 			"javascripts":[],
 			"css":[]
 		}
 		
 	</c:when>
 	
 	<c:otherwise>

		<%--Andy: added this for conditional load--%>
		<%@ include file="/WEB-INF/jsp/default/_common/variables/variables.jsp" %>
		<%@ include file="/WEB-INF/jsp/default/_common/html-open.jsp" %>
		
		<div class="container">
			<%@ include file="/WEB-INF/jsp/default/_common/header.jsp" %>
			<%@ include file="/WEB-INF/jsp/default/index/content.jsp" %>
			<%@ include file="/WEB-INF/jsp/default/_common/footer.jsp" %>
		</div>
		
		<%@ include file="/WEB-INF/jsp/default/_common/scripts-body.jsp" %>
		<%@ include file="/WEB-INF/jsp/default/_common/html-close.jsp" %>
 	
 	</c:otherwise>
 
</c:choose>


