<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags" %>

<c:if test="${!empty model.rightsOption}">
  <a href="${model.rightsOption.relativeUrl}" title="${model.rightsOption.rightsText}" class="item-metadata rights-badge" target="_blank" rel="xhv:license http://www.europeana.eu/schemas/edm/rights">
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
  </a>
  <c:if test="${model.rightsOption.noc}">
    <span rel="cc:useGuidelines" resource="http://www.europeana.eu/rights/pd-usage-guide/"></span>
  </c:if>
</c:if>