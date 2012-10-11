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


<div class="row carousel-row">

	<c:if test='${!empty model.carouselItems}'>
	
	    <script type="text/javascript">
	        var carouselData = [];   
 
	
			<c:forEach var="item" items="${model.carouselItems}">

				carouselData[carouselData.length] = {
					image:			"/${model.portalName}<spring:message code='${item.imgUrl}'/>",
					title:			"<spring:message code='${item.anchorTitle}'/>",
					description:	"<spring:message code='${item.description}'/>",
					link:			"${item.url}"
				};
				
				<%-- NOT USED 
					${item.anchorTarget}
					${item.imgWidth}
					${item.imgHeight}					
				--%>
				
			</c:forEach>
			
/* fix for home
 
	        carouselData = [];   

			carouselData[0] = {
					image:			"http://upload.wikimedia.org/wikipedia/commons/5/5f/Chicago_Downtown_Panorama.jpg",
					title:			"Chicago Downtown Panorama",
					description:	"Description of image",
					link:			"http://www.google.co.uk"
			};
 				
			carouselData[1] = {
					image:			"http://www.fromparis.com/panoramas_quicktime_vr/louvre_museum_pyramid/louvre_museum_pyramid.jpg",
					title:			"The Louvre in Paris",
					description:	"Description of image",
					link:			"http://www.google.co.uk"
			};
 				
			carouselData[0] = {
					image:			"http://1.bp.blogspot.com/-msOcdMpdrcI/TgSQMuMU8CI/AAAAAAAAAm4/qcK5Alsvrqo/s1600/sunrise-panorama-v2.jpg",
					title:			"Sunrise",
					description:	"Description of image",
					link:			"http://www.google.co.uk"
			};
 				
			carouselData[0] = {
					image:			"http://parkerlab.bio.uci.edu/pictures/photography%20pictures/2008_12_19_select/Untitled_Panorama1.jpg",
					title:			"Rocks Reflected in a Lake",
					description:	"Description of image",
					link:			"http://www.google.co.uk"
			};
*/

		</script>
		

			
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
				

		
	</c:if>	

</div>

