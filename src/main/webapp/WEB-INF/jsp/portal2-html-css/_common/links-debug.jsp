<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- debug-css -->
<link rel="stylesheet" href="/${theme_assets}/css/html.css" />
<link rel="stylesheet" href="/${theme_assets}/css/common.css" />
<link rel="stylesheet" href="/${theme_assets}/css/ajax.css" />
<link rel="stylesheet" href="/${theme_assets}/css/header.css" />
<link rel="stylesheet" href="/${theme_assets}/css/menu-main.css" />
<link rel="stylesheet" href="/${theme_assets}/css/menu-right.css" />
<link rel="stylesheet" href="/${theme_assets}/css/menu-styling.css" />
<link rel="stylesheet" href="/${theme_assets}/css/responsive-grid.css" />
<link rel="stylesheet" href="/${theme_assets}/css/eu-menu.css" />
<link rel="stylesheet" href="/${theme_assets}/css/ellipsis.css" />
<link rel="stylesheet" href="/${theme_assets}/css/europeana-font-icons.css" />
<link rel="stylesheet" href="/${theme_assets}/css/europeana-font-face.css" />
<!-- responsive image breakpoints -->
<style>
@media all and (min-width: 20em){
	.euresponsive {
		width: 1px;
	}
}

@media all and (min-width:30em){
	.euresponsive {
		width: 2px;
	}
}

@media all and (min-width:47em){
	.euresponsive {
		width: 3px;
	}
}

@media all and (min-width:49em){
	.euresponsive {
		width: 4px;
	}
}

.euresponsive-logo {
	width: 1px;
}

@media all and (min-width:70em){
	.euresponsive-logo {
		width: 2px;
	}
}
</style>
<!-- end responsive image breakpoints --><c:if test="${!empty model.announceMsg}">
<link rel="stylesheet" href="/${theme_assets}/css/announce.css" />
</c:if>
<c:choose>
<c:when test="${model.pageName == 'contact.html'}">
<link rel="stylesheet" href="/${theme_assets}/css/contact.css" />
</c:when>
<c:when test="${model.pageName == 'europeana-providers.html'}">
<link rel="stylesheet" href="/${theme_assets}/css/providers.css" />
<c:if test="${!empty model.leftContent}">
<link rel="stylesheet" href="/${theme_assets}/css/staticpages.css" />
</c:if>
</c:when>
<c:when test="${model.pageName == 'embed'}">
<link rel="stylesheet" href="/${theme_assets}/css/embed.css" />
</c:when>
<c:when test="${model.pageName == 'forgotPassword.html'}">
<link rel="stylesheet" href="/${theme_assets}/css/login.css" />
</c:when>
<c:when test="${model.pageName == 'full-doc.html'}">
<link rel="stylesheet" href="/${theme_assets}/css/carousel.css" />
<link rel="stylesheet" href="/${theme_assets}/css/fulldoc.css" />
<link rel="stylesheet" href="/${theme_assets}/css/lightbox.css" />
<link rel="stylesheet" href="/${theme_assets}/css/fulldoc-ess.css" />
<link rel="stylesheet" href="/${theme_assets}/css/fulldoc-excerpt.css" />
<link rel="stylesheet" href="/${theme_assets}/css/fulldoc-explore-further.css" />
<link rel="stylesheet" href="/${theme_assets}/css/tabbed-navigation.css" />
</c:when>
<c:when test="${model.pageName == 'index.html'}">
<link rel="stylesheet" href="/${theme_assets}/css/index.css" />
<link rel="stylesheet" href="/${theme_assets}/js/galleria/themes/europeanax/galleria.europeanax.css" />
</c:when>
<c:when test="${model.pageName == 'login.html'}">
<link rel="stylesheet" href="/${theme_assets}/css/login.css" />
</c:when>
<c:when test="${model.pageName == 'myeuropeana.html'}">
<link rel="stylesheet" href="/${theme_assets}/css/myeuropeana.css" />
<link rel="stylesheet" href="/${theme_assets}/css/tabbed-navigation.css" />
</c:when>
<c:when test="${model.pageName == 'register.html'}">
<link rel="stylesheet" href="/${theme_assets}/css/register.css" />
</c:when>
<c:when test="${model.pageName == 'register-success.html'}">
<link rel="stylesheet" href="/${theme_assets}/css/login.css" />
</c:when>
<c:when test="${model.pageName == 'search.html'}">
<link rel="stylesheet" href="/${theme_assets}/css/search.css" />
<link rel="stylesheet" href="/${theme_assets}/css/search-navigation.css" />
<link rel="stylesheet" href="/${theme_assets}/css/search-icons.css" />
<link rel="stylesheet" href="/${theme_assets}/css/search-pagination.css" />
<link rel="stylesheet" href="/${theme_assets}/css/sidebar-facets.css" />
<link rel="stylesheet" href="/${theme_assets}/css/sidebar-legend.css" />
<c:if test="${model.embedded}">
<link rel="stylesheet" href="/${theme_assets}/css/search-embed.css" />
</c:if>
</c:when>
<c:when test="${model.pageName == 'staticpage.html'}">
<link rel="stylesheet" href="/${theme_assets}/css/staticpages.css" />
</c:when>
<c:when test="${model.pageName == 'map.html'}">
<link rel="stylesheet" href="/${theme_assets}/css/search-navigation.css" />
<link rel="stylesheet" href="/${theme_assets}/css/search-icons.css" />
<link rel="stylesheet" href="/${theme_assets}/css/search-pagination.css" />
<link rel="stylesheet" href="/${theme_assets}/css/map.css" />
<link rel="stylesheet" href="/${theme_assets}/js/simile-widgets/timeplot-1.1/timeplot-bundle.css" />
<link rel="stylesheet" href="/${theme_assets}/js/simile-widgets/ajax-2.2.2/styles/graphics-modified.css" />
<link rel="stylesheet" href="/${theme_assets}/js/simile-widgets/timeplot-1.1/timeplot-bundle.css" />
<link rel="stylesheet" href="/${theme_assets}/js/sti/e4D-javascript/Sti.css" />
<%--
	could not lazy load, so put it here for the moment
	@link http://www.google.nl/search?aq=f&gcx=w&sourceid=chrome&ie=UTF-8&q=G_PHYSICAL_MAP+is+not+defined
--%>
<%-- <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;sensor=false&amp;key=${model.googleMapsId}"></script> --%>
</c:when>
<c:when test="${model.pageName == 'timeline.html'}">
<link rel="stylesheet" href="/${theme_assets}/css/search-navigation.css" />
<link rel="stylesheet" href="/${theme_assets}/css/search-icons.css" />
<link rel="stylesheet" href="/${theme_assets}/css/search-pagination.css" />
<link rel="stylesheet" href="/${theme_assets}/js/simile-widgets/ajax-2.2.2/styles/graphics-modified.css" />
<link rel="stylesheet" href="/${theme_assets}/js/simile-widgets/timeline-2.3.1/timeline-bundle-modified.css" />
<!--[if IE6]><link rel="stylesheet" href="/${theme_assets}/js/simile-widgets/ajax-2.2.2/styles/graphics-ie6.css" /><![endif]-->
</c:when>
</c:choose><!--[if IE]><link rel="stylesheet" href="/${theme_assets}/css/ie.css" /><![endif]-->
<!--[if lte IE 7]><link rel="stylesheet" href="/${theme_assets}/css/ie7.css" /><![endif]-->
<!--[if lte IE 8]><link rel="stylesheet" href="/${theme_assets}/css/ie8.css" /><![endif]-->
<noscript><link rel="stylesheet" href="/${theme_assets}/css/noscript.css" /></noscript>
<!-- debug-css -->