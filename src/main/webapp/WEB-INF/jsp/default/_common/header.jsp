<c:set var="logo_tag" value="h1"/>
<c:if test="${'full-doc.html' == model.pageName}">
	<c:set var="logo_tag" value="div" />
</c:if>

<${logo_tag}>
<a href="/${model.portalName}/" title="<spring:message code='AltLogoEuropeana_t' />">
<img src="/${model.portalName}/sp/img/europeana-logo-${model.imageLocale}.png" alt="<spring:message code='AltLogoEuropeana_t' />" width="206" height="123"/>
</a>
</${logo_tag}>

<%-- Choose a language --%>
<%@ include file="/WEB-INF/jsp/vanilla/_common/menus/language.jsp" %>

<%-- <%@ include file="/WEB-INF/jsp/default/_common/menus/user.jsp" %> --%>

<%@ include file="/WEB-INF/jsp/default/_common/query/query.jsp" %>
