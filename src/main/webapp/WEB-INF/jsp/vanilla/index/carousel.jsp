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
<ul>
<c:if test='${not empty model.carouselItems}'>
  <c:set var="item" value="${model.carouselItems[0]}"/>
	<li><a href="${item.url}" title="<spring:message code='${item.anchorTitle}'/>" target="<spring:message code='${item.anchorTarget}'/>"><img src="/${model.portalName}<spring:message code='${item.imgUrl}'/>" alt="<spring:message code='${item.imgAlt}'/>" width="<spring:message code='${item.imgWidth}'/>" height="<spring:message code='${item.imgHeight}'/>"/></a></li>
</ul>
</c:if>
