<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="/${branding}/css/min/common.min.css" />

<c:if test="${!empty model.announceMsg}">
	<link rel="stylesheet" href="/${branding}/css/min/announce.min.css" />
</c:if>

<c:choose>

	<c:when test="${model.pageName == 'contact.html'}">
		<link rel="stylesheet" href="/${branding}/css/min/contact.min.css" />
	</c:when>

	<c:when test="${model.pageName == 'europeana-providers.html'}">
		<link rel="stylesheet" href="/${branding}/css/min/providers.min.css" />
		<c:if test="${!empty model.leftContent}">
			<link rel="stylesheet" href="/${branding}/css/min/staticpages.min.css" />
		</c:if>
	</c:when>

	<c:when test="${model.pageName == 'embed'}">
		<link rel="stylesheet" href="/${branding}/css/min/embed.min.css" />
	</c:when>

	<c:when test="${model.pageName == 'forgotPassword.html'}">
		<link rel="stylesheet" href="/${branding}/css/min/login.min.css" />
	</c:when>

	<c:when test="${model.pageName == 'full-doc.html'}">
		<link rel="stylesheet" href="/${branding}/css/min/fulldoc.min.css" />
	</c:when>

	<c:when test="${model.pageName == 'index.html'}">
		<link rel="stylesheet" href="/${branding}/css/min/index.min.css" />
	</c:when>

	<c:when test="${model.pageName == 'login.html'}">
		<link rel="stylesheet" href="/${branding}/css/min/login.min.css" />
	</c:when>

	<c:when test="${model.pageName == 'myeuropeana.html'}">
		<link rel="stylesheet" href="/${branding}/css/min/myeuropeana.min.css" />
	</c:when>

	<c:when test="${model.pageName == 'register.html'}">
		<link rel="stylesheet" href="/${branding}/css/min/register.min.css" />
	</c:when>

	<c:when test="${model.pageName == 'register-success.html'}">
		<link rel="stylesheet" href="/${branding}/css/min/login.min.css" />
	</c:when>

	<c:when test="${model.pageName == 'search.html'}">
		<link rel="stylesheet" href="/${branding}/css/min/search.min.css" />
		<c:if test="${model.embedded}">
			<link rel="stylesheet" href="/${branding}/css/min/search-embed.min.css" />
		</c:if>
	</c:when>

	<c:when test="${model.pageName == 'staticpage.html'}">
		<link rel="stylesheet" href="/${branding}/css/min/staticpages.min.css" />
	</c:when>
	
	
	<c:when test="${model.pageName == 'api/registration.html'}">
		<link rel="stylesheet" href="/${branding}/css/min/myeuropeana.min.css" />
		<link rel="stylesheet" href="/${branding}/css/min/login.min.css" />
		<link rel="stylesheet" href="/${branding}/css/min/register.min.css" />
	</c:when>
	
	<c:when test="${model.pageName == 'widget/editor.html'}">
		<link rel="stylesheet" href="/${branding}/css/sidebar-facets.css" />
		<link rel="stylesheet" href="/${branding}/css/eu-accordion-tabs.css" />
		<link rel="stylesheet" href="/${branding}/js/com/mediaelement/build/mediaelementplayer.css" />
	</c:when>

	
</c:choose>


<!--[if IE 8]><link rel="stylesheet" href="/${branding}/css/min/ie8.min.css" /><![endif]-->
<!--[if IE 9]><link rel="stylesheet" href="/${branding}/css/min/ie9.min.css" /><![endif]-->

<!-- responsive image breakpoints -->

<style>

	@media all and (min-width: 20em){
		.euresponsive {
			width: 0px;
		}
	}

	@media all and (min-width:30em){
		.euresponsive {
			width: 1px;
		}
	}

	@media all and (min-width:47em){
		.euresponsive {
			width: 2px;
		}
	}

	@media all and (min-width:49em){
		.euresponsive {
			width: 3px;
		}
	}

</style>

	
<!--[if IE]><link rel="stylesheet" href="/${branding}/css/min/ie.min.css" /><![endif]-->
<!--[if lte IE 7]><link rel="stylesheet" href="/${branding}/css/min/ie7.min.css" /><![endif]-->
<!--[if lte IE 8]><link rel="stylesheet" href="/${branding}/css/min/ie8.min.css" /><![endif]-->
<noscript><link rel="stylesheet" href="/${branding}/css/min/noscript.min.css" /></noscript>