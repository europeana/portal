<!-- logo -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="logo_tag" value='h1' />
<c:if test="${'full-doc.html' == model.pageName}"><c:set var="logo_tag" value="div" /></c:if>
<${logo_tag} id="logo">
	<a href="/" title="<spring:message code="AltLogoEuropeana_t" />">
		<img src="//sp/img/europeana-logo-${model.imageLocale}.png"
		 alt="<spring:message code="AltLogoEuropeana_t" />" width="206" height="123" />
	</a>
</${logo_tag}>
<!-- /logo -->