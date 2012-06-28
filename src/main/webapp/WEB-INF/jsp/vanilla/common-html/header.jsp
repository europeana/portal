<c:set var="logo_tag" value="h1"/>
<c:if test="${'full-doc.html' == model.pageName}"><c:set var="logo_tag" value="div" /></c:if>
<${logo_tag}>
  <a href="/${model.portalName}/" title="<spring:message code='AltLogoEuropeana_t' />">
    <img src="/${model.portalName}/sp/img/europeana-logo-${model.imageLocale}.png" alt="<spring:message code='AltLogoEuropeana_t' />" width="206" height="123"/>
  </a>
</${logo_tag}>
<%--@ include file="/WEB-INF/jsp/vanilla/menus/main.jsp" --%>
<%--@ include file="/WEB-INF/jsp/vanilla/menus/user.jsp" --%>
<%--@ include file="/WEB-INF/jsp/vanilla/query/query.jsp" --%>