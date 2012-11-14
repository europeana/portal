<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<c:set var="inLightbox" scope="request" value="false"/>  

<c:if test="${!empty model.rightsOption}">

  <a
  	href="${model.edmRights}"
    title="${model.rightsOption.rightsText}"
    class="item-metadata rights-badge" target="_blank"
    rel="license europeana:rights">
    <c:choose>
      <c:when test="${inLightbox}">
        <img
          src="/${branding}/images/rights/${model.rightsOption.rightsIcon}"
          alt="${model.rightsOption.rightsText}" />
      </c:when>
      <c:otherwise>
      
		<c:set var="rightsIcons" value="${fn:split(model.rightsOption.rightsIcon, ' ')}" />

		<c:forEach items="${rightsIcons}" var="rightsIcon">
	      	<span title="${model.rightsOption.rightsText}" class="rights-icon ${rightsIcon}"></span>
		</c:forEach>
		
		<span class="rights-text">
			<c:set var="rightsText" value="${fn:replace(model.rightsOption.rightsText, ' ', '_')}" />
			<c:set var="rightsText" value="${fn:replace(rightsText, '-', '_')}" />
			<spring:message code="rights_${rightsText}" />
		</span>
		
		<c:if test="${model.rightsOption.rightsShowExternalIcon}">
			<span class="icon-external-right"></span>
		</c:if>
		
      </c:otherwise>
    </c:choose>
  </a>
  <c:if test="${model.rightsOption.noc}">
    <span rel="cc:useGuidelines" resource="http://www.europeana.eu/rights/pd-usage-guide/"></span>
  </c:if>
</c:if>
