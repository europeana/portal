<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<h3><a  href="#saved-items"><spring:message code='SavedItems_t'/></a></h3>
<c:choose>
  <c:when test="${fn:length(model.user.savedItems) < 1 }">
    <spring:message code='NoSavedItems_t'/>
  </c:when>
<c:otherwise>
<%-- Iterate over Saved Items --%>
<c:forEach items="${model.user.savedItems}" var="item">
  <%-- Item Image --%>
    <%-- Determine fallback image for this item --%>
      <c:set var='imgtoshow' value="item-image.gif"/>
      <c:set var='docType' value="${item.docType}"/>
      <c:choose>
        <c:when test="${item.docType == 'IMAGE'}">
          <c:set var='imgtoshow' value="item-image.gif"/>
        </c:when>
        <c:when test="${item.docType == 'VIDEO'}">
          <c:set var='imgtoshow' value="item-video.gif"/>
        </c:when>
        <c:when test="${item.docType == 'SOUND'}">
          <c:set var='imgtoshow' value="item-sound.gif"/>
        </c:when>
        <c:when test="${item.docType == 'TEXT'}">
          <c:set var='imgtoshow' value="item-text.gif"/>
        </c:when>
        <c:when test="${item.docType == '_3D'}">
          <c:set var='imgtoshow' value="item-3d.gif"/>
          <c:set var='docType' value="3d"/>
        </c:when>
      </c:choose>
  <%-- Place Image --%>
  <a href='/${model.portalName}/${fn:replace(item.europeanaUri, "http://www.europeana.eu/resolve/", "")}.html?bt=savedItem' title="${item.title}">
  <c:choose>
    <c:when test="${item.europeanaObject}">
      <img src="${model.cacheUrl}uri=${fn:escapeXml(item.europeanaObject)}&size=BRIEF_DOC&type=${docType}" alt="${item.title}"/>
    </c:when>
    <c:otherwise>
      <img src="/${branding}/images/icons/item-types/${imgtoshow}" alt="${item.title}"/>
    </c:otherwise>
  </c:choose>
  </a>
  <%-- Item Info --%>
  <a href='/${model.portalName}/${fn:replace(item.europeanaUri, "http://www.europeana.eu/resolve/", "")}.html?bt=savedItem' title="${item.title}"><@stringLimiter "${item.title}" "50"/></a> <spring:message code='Creator_t'/>: <em>${item.author} <spring:message code='DateSaved_t'/>: ${item.dateSaved?datetime}
  <%-- Delete Saved Item button --%>
  <button><spring:message code='Delete_t'/></button>
</c:forEach>    
</c:otherwise>
</c:choose>