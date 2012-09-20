<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<%--
list would be a collection of carousel items coming from the messages.properties file based on tags with actual content,
e.g., total possible is 12 items, yet they may only fill 7 items, thus the collection should only contain the 7
        
marcoms can provide a local european.eu link within the portal or an external http:// link,
this if statement is currently used to determine if the item.anchorUrl should have /${model.portalName}/ appended
or not, something similar could be determined in the backend instead
<#if !"${url}"?contains('http://')> <#assign url>/${model.portalName}/${url}</#assign> </#if>

x = ordinal nr
${item.url} = a local/external interpretation of the notranslate_carousel-item-x-a_url
${item.anchorUrl} = notranslate_carousel-item-x-a_url
${item.anchorTitle} = carousel-item-x-a_title
${item.anchorTarget} = notranslate_carousel-item-x_a_target

${item.imgUrl} = notranslate_carousel-item-x_img_url
${item.imgAlt} = carousel-item-x_img_alt
${item.imgWidth} = notranslate_carousel-item-x_img_width
${item.imgHeight} = notranslate_carousel-item-x_img_height
--%>


<div class="row">

	<c:if test='${!empty model.carouselItems}'>
	
	    <script type="text/javascript">
	            var carouselData = [];   
	
			<c:forEach var="item" items="${model.carouselItems}">
			
				carouselData[carouselData.length] = {
					image:			"/${model.portalName}<spring:message code='${item.imgUrl}'/>",
					title:			"<spring:message code='${item.anchorTitle}'/>",
					description:	"<spring:message code='${item.imgAlt}'/>",
					link:			"${item.url}"
				};
				
				<%-- NOT USED 
					${item.anchorTarget}
					${item.imgWidth}
					${item.imgHeight}					
				--%>
			</c:forEach>
			
		</script>
		
		
		<div class="six columns">

			<div class="carousel-spacer">
			
				<div id="carousel-1">
				
					<c:forEach var="item" items="${model.carouselItems}">
						<a href="${item.url}">
							<img	src		= "/${model.portalName}<spring:message code='${item.imgUrl}'/>"
									title	= "<spring:message code='${item.anchorTitle}'/>"
									alt		= "<spring:message code='${item.imgAlt}'/>"
									class	= "hidden"
							/>
						</a>
					</c:forEach>
								
				</div>
				
			</div>		
			
		</div>
		
		
		<div class="six columns hide-on-phones">
		
			<div class="blurb-spacer">
		
				<h2>Europe's Heritage - Collected for You!</h2>
				
				<p>
				Explore more than 20 million items from a range of Europe's leading galleries, libraries, archives and museums.
				
				From The Girl with the Pearl Earring to Newton's Laws of Motion, from the music of Mozart to the TV-news of times gone by, it's all here!
				
				Start your Europeana adventure now and find something amazing! 				
				</p>
				
				
			</div>
				
		</div>
		
	</c:if>	
</div>

