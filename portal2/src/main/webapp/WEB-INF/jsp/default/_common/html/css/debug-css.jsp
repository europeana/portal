<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${empty param.noCss}">

	<link rel="stylesheet" href="${branding}/css/html.css" />
	<link rel="stylesheet" href="${branding}/css/common.css" />
	<link rel="stylesheet" href="${branding}/css/ajax.css" />
	<link rel="stylesheet" href="${branding}/css/header.css" />
	<link rel="stylesheet" href="${branding}/css/menu-main.css" />
	<link rel="stylesheet" href="${branding}/css/responsive-grid.css" />

	<link rel="stylesheet" href="${branding}/css/eu-menu.css" />
	<link rel="stylesheet" href="${branding}/css/ellipsis.css" />

	<link rel="stylesheet" href="${branding}/css/europeana-font-icons.css" />
	<link rel="stylesheet" href="${branding}/css/europeana-font-face.css" />

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

	<c:if test="${!empty model.announceMsg}">
		<link rel="stylesheet" href="${branding}/css/announce.css" />
	</c:if>

	<c:choose>
		<c:when test="${model.pageName == 'contact.html'}">
			<link rel="stylesheet" href="${branding}/css/contact.css" />
		</c:when>

		<c:when test="${model.pageName == 'europeana-providers.html'}">
			<link rel="stylesheet" href="${branding}/css/providers.css" />
			<c:if test="${!empty model.leftContent}">
				<link rel="stylesheet" href="${branding}/css/staticpages.css" />
			</c:if>
		</c:when>

		<c:when test="${model.pageName == 'embed'}">
			<link rel="stylesheet" href="${branding}/css/embed.css" />
		</c:when>

		<c:when test="${model.pageName == 'widget/editor.html'}">
			<link rel="stylesheet" href="${branding}/css/common.css" />
			<link rel="stylesheet" href="${branding}/css/sidebar-facets.css" />
			<link rel="stylesheet" href="${branding}/css/eu-accordion-tabs.css" />
			<link rel="stylesheet" href="${branding}/js/com/mediaelement/build/mediaelementplayer.css" />
		</c:when>

		<c:when test="${model.pageName == 'forgotPassword.html'}">
			<link rel="stylesheet" href="${branding}/css/login.css" />
		</c:when>

		<c:when test="${model.pageName == 'full-doc.html'}">
			<link rel="stylesheet" href="${branding}/css/fulldoc.css" />
			<link rel="stylesheet" href="${branding}/css/eu-carousel.css" />
			<link rel="stylesheet" href="${branding}/css/lightbox.css" />

			<!-- accordion-tabs before fulldoc-ess -->
			<link rel="stylesheet" href="${branding}/css/eu-accordion-tabs.css" />
			<link rel="stylesheet" href="${branding}/css/fulldoc-ess.css" />

			<%--
			<link rel="stylesheet" href="${branding}/css/fulldoc-explore-further.css" />
			<link rel="stylesheet" href="${branding}/css/tabbed-navigation.css" />
			--%>
		</c:when>

		<c:when test="${model.pageName == 'index.html'}">
			<link rel="stylesheet" href="${branding}/css/eu-carousel.css" />
			<link rel="stylesheet" href="${branding}/css/index.css" />
		</c:when>

		<c:when test="${model.pageName == 'login.html'}">
			<link rel="stylesheet" href="${branding}/css/login.css" />
		</c:when>

		<c:when test="${model.pageName == 'api/console.html'}">
			<link rel="stylesheet" href="${branding}/css/api-console.css" />
			<c:if test="${model.embeddedConsole}">
				<link rel="stylesheet" href="${branding}/css/api-console-embedded.css" />
			</c:if>
		</c:when>

		<c:when test="${model.pageName == 'api/registration.html'}">
			<link rel="stylesheet" href="${branding}/css/register.css" />
			<link rel="stylesheet" href="${branding}/css/myeuropeana.css" />
			<link rel="stylesheet" href="${branding}/css/login.css" />
		</c:when>

		<c:when test="${model.pageName == 'myeuropeana.html'}">
			<link rel="stylesheet" href="${branding}/css/myeuropeana.css" />
			<link rel="stylesheet" href="${branding}/css/eu-accordion-tabs.css" />
		</c:when>

		<c:when test="${model.pageName == 'myeuropeana/index'}">
			<link rel="stylesheet" href="${branding}/css/login.css" />
			<link rel="stylesheet" href="${branding}/css/myeuropeana-index.css" />
		</c:when>

		<c:when test="${model.pageName == 'register.html'}">
			<link rel="stylesheet" href="${branding}/css/register.css" />
		</c:when>

		<c:when test="${model.pageName == 'register-success.html'}">
			<link rel="stylesheet" href="${branding}/css/login.css" />
		</c:when>

		<c:when test="${model.pageName == 'search.html'}">
			<link rel="stylesheet" href="${branding}/css/search.css" />
			<link rel="stylesheet" href="${branding}/css/search-pagination.css" />
			<link rel="stylesheet" href="${branding}/css/sidebar-facets.css" />
			<c:if test="${model.embedded}">
				<link rel="stylesheet" href="${branding}/css/search-embed.css" />
			</c:if>
		</c:when>

		<c:when test="${model.pageName == 'staticpage.html'}">
			<link rel="stylesheet" href="${branding}/css/staticpages.css" />
		</c:when>
	</c:choose>

	<!--[if IE 8]><link rel="stylesheet" href="${branding}/css/ie8.css" /><![endif]-->
	<!--[if IE 9]><link rel="stylesheet" href="${branding}/css/ie9.css" /><![endif]-->
	<noscript><link rel="stylesheet" href="${branding}/css/noscript.css" /></noscript>

</c:if>
