<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags" %>

<%-- Now based on page context variable rightsToParse to allow this string to be used in JSON data for the lightbox carousel --%>

<c:set var="rightsFound" value="false"/>

<c:if test="${!empty rightsToParse}">
  


  <a href="${rightsToParse.relativeUrl}" title="${rightsToParse.rightsText}" class="item-metadata rights-badge" target="_blank" rel="xhv:license http://www.europeana.eu/schemas/edm/rights">
    <c:set var="rightsIcons" value="${fn:split(rightsToParse.rightsIcon, ' ')}" />
    <c:forEach items="${rightsIcons}" var="rightsIcon">
      <span title="${rightsToParse.rightsText}" class="rights-icon ${rightsIcon}"></span>
    </c:forEach>
    <span class="rights-text">${rightsToParse.rightsText}</span>
    <c:if test="${rightsToParse.rightsShowExternalIcon}">
      <span class="icon-external-right"></span>
    </c:if>
    <c:if test="${!empty rightsToParse.expiryDate}">
      <c:set var="fmtPage" value="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/context/timeFmtExpires.jsp"/>
	  <span  class="rights-text">expires <jsp:include page="${fmtPage}" flush="true"><jsp:param name="date" value="${rightsToParse.expiryDate}" /></jsp:include></span>
    </c:if>
  </a>
  <c:if test="${rightsToParse.noc}">
    <span rel="cc:useGuidelines" resource="http://www.europeana.eu/rights/pd-usage-guide/"></span>
  </c:if>
	
</c:if>

    
    
    
    
	  
