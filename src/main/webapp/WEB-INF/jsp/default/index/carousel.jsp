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

	<c:if test='${not empty model.carouselItems}'>
	
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
			
			//carouselData[0].image = "http://www.europeana.eu/portal/sp/img/ca_teylers.jpg";
		//	carouselData[0].big = "http://upload.wikimedia.org/wikipedia/commons/7/72/Pleiades_Spitzer_big.jpg";
		</script>
		
		
		<div class="six columns">
			<div id="carousel-1">
			</div>
		</div>
		
		<div class="six columns hide-on-phones">
			<div id="carousel-1-blurb">
				<span class="italic-heading">Europe's Digital Library</span>
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

