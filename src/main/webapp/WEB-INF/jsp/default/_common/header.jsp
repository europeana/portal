

<div id="header" role="banner" class="row">
<%@ include file="/WEB-INF/jsp/default/_common/header-strip.jsp" %>

	<div style="display:table; width:100%;">
	
		<div id="logo">
			
			<c:set var="logo_tag" value="h1"/>
			<c:if test="${'full-doc.html' == model.pageName}">
				<c:set var="logo_tag" value="div" />
			</c:if>
			
			<${logo_tag} id="_logo" xstyle="display:inline-block; max-width:90%; ">
	
				<a href="/${model.portalName}/" title="<spring:message code='AltLogoEuropeana_t' />">
					<img style="vertical-align:middle;"  src="/${model.portalName}/sp/img/europeana-logo-${model.imageLocale}.png" alt="<spring:message code='AltLogoEuropeana_t' />"/>
				</a>
				
			</${logo_tag}>
		</div>
		
		<%@ include file="/WEB-INF/jsp/default/_common/query/query.jsp" %>
		
	</div>

</div>



<%--

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


<%@ include file="/WEB-INF/jsp/default/_common/query/query.jsp" %>

</div>



--%>