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
					image:				"/${model.portalName}${item.responsiveImages['_1']}",
					title:				"<spring:message code="${item.anchorTitle}" />",
					description:		"<spring:message code="${item.description}" />",
					linkDescription:	"<spring:message code="${item.linkDescription}" />",
					europeanaLink:		"${item.url}",
					external:			"<spring:message code="${item.anchorTarget}" />",
					alt:				"<spring:message code="${item.imgAlt}" />"
				};

				<c:if test='${!empty item.translatableUrls}'>
					<c:forEach var="lang" items="${item.translatableUrls}" varStatus="status">
						<c:if test="${model.locale == lang.key}" >
							carouselData[carouselData.length-1].europeanaLink = "${lang.value}";
						</c:if>
					</c:forEach>
				</c:if>
				
			</c:forEach>
		</script>

		<div id="carousel-1">
			<c:forEach var="item" items="${model.carouselItems}">
				<a href="${item.url}">
					<img
							src		= "/${model.portalName}${item.responsiveImages['_1']}"
							title	= "<spring:message code="${item.anchorTitle}" />"
							alt		= "<spring:message code="${item.imgAlt}" />"
							class	= "hidden"
					/>
				</a>
			</c:forEach>
		</div>
		<div id="carousel-1-external-info"></div>
	</c:if>

</div>

