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


<div id="carousel-1-wrapper" class="row">

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
		
		<div class="six columns hide-on-phones">
			<div id="carousel-1-blurb">
				<h2>Europe's Digital Library</h2>
				<p>
					In Europeana you can search more than 24 million records of paintings, artifacts, books, music, films and more made available by Europe's libraries, archives and museums.
				</p>			
				<p>
					You can also explore online exhibitions and follow the latest......
				</p>		
			</div>
		</div>
		
	</c:if>	
</div>

