<c:set var="logo_tag" value="h1"/>
<c:if test="${'full-doc.html' == model.pageName}">
	<c:set var="logo_tag" value="div" />
</c:if>


<div id="header-strip">
	<span>
		<a href="/${model.portalName}/" target="<spring:message code="notranslate_menu-main-1_a_target_t"/>" title="<spring:message code="menu-main-1_a_title_t"/>"><spring:message code="menu-main-1_a_text_t"/></a>
		
		<a href="<spring:message code="notranslate_menu-main-6_a_url_t"/>" target="<spring:message code="notranslate_menu-main-6_a_target_t"/>" title="<spring:message code="menu-main-6_a_title_t"/>"><spring:message code="menu-main-6_a_text_t"/></a>
	</span>

	<%-- Choose a language --%>
	<%@ include file="/WEB-INF/jsp/default/_common/menus/language.jsp" %>	
</div>


<${logo_tag} id="logo">
<a href="/${model.portalName}/" title="<spring:message code='AltLogoEuropeana_t' />">
<img src="/${model.portalName}/sp/img/europeana-logo-${model.imageLocale}.png" alt="<spring:message code='AltLogoEuropeana_t' />" width="206" height="123"/>
</a>
</${logo_tag}>

<%-- <%@ include file="/WEB-INF/jsp/default/_common/menus/user.jsp" %> --%>

<%@ include file="/WEB-INF/jsp/default/_common/query/query.jsp" %>
