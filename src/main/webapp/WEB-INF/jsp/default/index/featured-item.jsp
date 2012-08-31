<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div class="six columns">
	<h4 id="collapse-header-2">
		<span class="left"><spring:message code="featured-content-title_t" /></span>
		<span class="collapse-icon"></span>
		<a class="feed-link" href="http://pinterest.europeana.eu.feed" target="_blank" title="RSS Feed"></a>
	</h4>

	<div class="collapse-content">
		
		<c:if test="${!empty model.featuredItem}">
		
		
			<div class="row featured-content">
				<div class="five columns">
					<h3 class="show-on-phones">
						<a	href=	"/${model.portalName}<spring:message code="${model.featuredItem.anchorUrl}" />"
							title=	"<spring:message code="${model.featuredItem.anchorTitle}" />"
							target=	"<spring:message code="${model.featuredItem.anchorTarget}" />">
								<spring:message code="${model.featuredItem.heading}" />
						</a>
					</h3>
					<div class="featured-frame">
						<a	href=	"/${model.portalName}<spring:message code="${model.featuredItem.anchorUrl}" />"
							title=	"<spring:message code="${model.featuredItem.anchorTitle}" />"
							target=	"<spring:message code="${model.featuredItem.anchorTarget}" />"
							class=	"image">
							<img	src=	"/${model.portalName}<spring:message code="${model.featuredItem.imgUrl}" />"
									alt=	"<spring:message code="${model.featuredItem.imgAlt}" />"/>
						</a>
					</div>
				</div>
				
				<div class="seven columns">
					<h3 class="hide-on-phones featured-text">
						<a	href=	"/${model.portalName}<spring:message code="${model.featuredItem.anchorUrl}" />"
							title=	"<spring:message code="${model.featuredItem.anchorTitle}" />"
							target=	"<spring:message code="${model.featuredItem.anchorTarget}" />">
								<spring:message code="${model.featuredItem.heading}" />
						</a>
					</h3>
					<p class="featured-text">
						<spring:message code="${model.featuredItem.p}" />
					</p>
				</div>			
			</div>
		</c:if>

		
<%--
		
		    <script type="text/javascript">
	            var carousel2Data = [];
				<c:forEach var="item" items="${model.featuredItems}">
					carousel2Data[carousel2Data.length] = {
						thumb:			"/${model.portalName}<spring:message code='${item.imgUrl}'/>",
						title:			"<spring:message code='${item.anchorTitle}'/>",
						description:	"<spring:message code='${item.p}'/>",
						link:			"/${model.portalName}<spring:message code='${item.anchorUrl}'/>"
					};
				</c:forEach>
			</script>
				
			
			<div id="carousel-2-header" class="europeana-header">
			</div>
			<div id="carousel-2" class="europeana-carousel">
			</div>
			<div id="carousel-2-footer" class="europeana-footer">
			</div>
 --%>
			
		
				
	</div>
</div>

<%-- 

View more latest content from our partners...
 - featured-partner-view_text_t (in JSP: viewText)
 - notranslate_featured-partner-view_link_t (in JSP: viewLink)
 - notranslate_featured-partner-view_target_t (in JSP: viewTarget)

Browse the content of all our partners...
 - featured-partner-browse_text_t (in JSP: browseText)
 - notranslate_featured-partner-browse_link_t (in JSP: browseLink)
 - notranslate_featured-partner-browse_target_t (in JSP: browseTarget)

Visit partners website...
 - featured-partner-visit_text_t (in JSP: visitText)
 - notranslate_featured-partner-visit_link_t (in JSP: visitLink)
 - notranslate_featured-partner-visit_target_t (in JSP: visitTarget)
--%>

<div class="six columns" id="section-featured-partner">

	
<%-- 

	<h4><spring:message code="featured-partner-view_text_t" /></h4>
--%>

	<c:if test="${!empty model.featuredPartner}">
	
		<div class="row">
			<div class="twelve columns">
				<h4 id="section-header-featured-partner"><spring:message code="featured-partner-title_t" />!!!</h4>
			</div>
		</div>
		
		<div class="row featured-content">
			<div class="five columns">
				<h3 class="show-on-phones">
					<a	href=	"<spring:message	code="${model.featuredPartner.anchorUrl}" />"
						title=	"<spring:message	code="${model.featuredPartner.anchorTitle}" />"
						target=	"<spring:message	code="${model.featuredPartner.anchorTarget}" />">
							<spring:message code="${model.featuredPartner.heading}" />
					</a>
				</h3>
				<div class="featured-frame">
					<a	href=	"<spring:message code="${model.featuredPartner.anchorUrl}" />"
						title=	"<spring:message code="${model.featuredPartner.anchorTitle}" />"
						target=	"<spring:message code="${model.featuredPartner.anchorTarget}" />"
						class="image">
						<img	src=	"<spring:message	code="${model.featuredPartner.imgUrl}" />"
								alt=	"<spring:message	code="${model.featuredPartner.imgAlt}" />"/>
					</a>
				</div>
			</div>
			
			<div class="seven columns">
				<h3 class="hide-on-phones featured-text">
					<a	href=	"<spring:message code="${model.featuredPartner.anchorUrl}" />"
						title=	"<spring:message code="${model.featuredPartner.anchorTitle}" />"
						target=	"<spring:message code="${model.featuredPartner.anchorTarget}" />">
							<spring:message code="${model.featuredPartner.heading}" />
					</a>
				</h3>
				<p class="featured-text">
					<spring:message code="${model.featuredPartner.p}" />
				</p>
			</div>			
		</div>
		
	</c:if>
	
</div>


<%--

<c:if test='${not empty model.featuredItem}'>
  <h2><spring:message code='${model.featuredItem.h2}'/></h2>
  <a href="/${model.portalName}<spring:message code='${model.featuredItem.anchorUrl}'/>" title="<spring:message code='${model.featuredItem.anchorTitle}'/>" target="<spring:message code='${model.featuredItem.anchorTarget}'/>"><img src="/${model.portalName}<spring:message code='${model.featuredItem.imgUrl}'/>" alt="<spring:message code='${model.featuredItem.imgAlt}'/>" width="<spring:message code='${model.featuredItem.imgWidth}'/>" height="<spring:message code='${model.featuredItem.imgHeight}'/>"/></a>
  <h3><a href="/${model.portalName}<spring:message code='${model.featuredItem.anchorUrl}'/>" title="<spring:message code='${model.featuredItem.anchorTitle}'/>" target="<spring:message code='${model.featuredItem.anchorTarget}'/>"><spring:message code='${model.featuredItem.h3}'/></a></h3>
  <p><spring:message code='${model.featuredItem.p}'/></p>
</c:if>

 --%>