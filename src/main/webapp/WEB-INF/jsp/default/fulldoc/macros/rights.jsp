<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${model[rightsOption]}">
	<a href="${model.rightsOption.rightsUrl}" title="${model.rightsOption.rightsText}" class="item-metadata rights-badge" target="_blank" rel="license europeana:rights">
		<c:choose>
			<c:when test="${inLightbox}">
				<img src="/${branding}/images/rights/${model.rightsOption.rightsIconLightbox}" alt="${model.rightsOption.rightsText}" />
			</c:when>
			<c:otherwise>
				<img src="/${branding}/images/rights/${model.rightsOption.rightsIcon}" alt="${model.rightsOption.rightsText}" />
			</c:otherwise>
		</c:choose>
	</a>
	<c:if test="${model.rightsOption.noc}">
		<span rel="cc:useGuidelines" resource="http://www.europeana.eu/rights/pd-usage-guide/"></span>
	</c:if>
</c:if>
