
<div class="row" id="header" role="banner">
	<%@ include file="/WEB-INF/jsp/default/_common/header-strip.jsp" %>
	
	<c:set var="logo_tag" value="h1"/>
	<c:if test="${'full-doc.html' == model.pageName}">
		<c:set var="logo_tag" value="div" />
	</c:if>
	
	<${logo_tag} id="logo">
		<a href="/${model.portalName}/" title="<spring:message code='AltLogoEuropeana_t' />">
			<img src="/${model.portalName}/sp/img/europeana-logo-${model.imageLocale}.png" alt="<spring:message code='AltLogoEuropeana_t' />"/>
		</a>
	</${logo_tag}>

</div>

<%@ include file="/WEB-INF/jsp/default/_common/query/query.jsp" %>
