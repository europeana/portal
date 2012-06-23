<!-- debug-css -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="/${branding}/css/html.css" />
<link rel="stylesheet" href="/${branding}/css/common.css" />
<link rel="stylesheet" href="/${branding}/css/ajax.css" />
<link rel="stylesheet" href="/${branding}/css/header.css" />
<link rel="stylesheet" href="/${branding}/css/menu-main.css" />
<link rel="stylesheet" href="/${branding}/css/menu-right.css" />
<link rel="stylesheet" href="/${branding}/css/menu-footer.css" />
<link rel="stylesheet" href="/${branding}/css/menu-styling.css" />
<link rel="stylesheet" href="/${branding}/css/footer.css" />

<c:if test="${!empty model.announceMsg}">
	<link rel="stylesheet" href="/${branding}/css/announce.css" />
</c:if>

<c:choose>
	<c:when test="${model.pageName == 'contact.html'}">
		<link rel="stylesheet" href="/${branding}/css/contact.css" />
	</c:when>

	<c:when test="${model.pageName == 'europeana-providers.html'}">
		<link rel="stylesheet" href="/${branding}/css/providers.css" />
		<c:if test="${!empty model.leftContent}">
			<link rel="stylesheet" href="/${branding}/css/staticpages.css" />
		</c:if>
	</c:when>

	<c:when test="${model.pageName == 'embed'}">
		<link rel="stylesheet" href="/${branding}/css/embed.css" />
	</c:when>

	<c:when test="${model.pageName == 'forgotPassword.html'}">
		<link rel="stylesheet" href="/${branding}/css/login.css" />
	</c:when>

	<c:when test="${model.pageName == 'full-doc.html'}">
		<link rel="stylesheet" href="/${branding}/css/carousel.css" />
		<link rel="stylesheet" href="/${branding}/css/fulldoc.css" />
		<link rel="stylesheet" href="/${branding}/css/lightbox.css" />
		<link rel="stylesheet" href="/${branding}/css/fulldoc-sidebar-right.css" />
		<link rel="stylesheet" href="/${branding}/css/fulldoc-ess.css" />
		<link rel="stylesheet" href="/${branding}/css/fulldoc-excerpt.css" />
		<link rel="stylesheet" href="/${branding}/css/fulldoc-explore-further.css" />
		<link rel="stylesheet" href="/${branding}/css/tabbed-navigation.css" />
	</c:when>

	<c:when test="${model.pageName == 'index.html'}">
		<link rel="stylesheet" href="/${branding}/css/index.css" />
		<link rel="stylesheet" href="/${branding}/css/carousel.css" />
		<link rel="stylesheet" href="/${branding}/css/touts.css" />
	</c:when>

	<c:when test="${model.pageName == 'login.html'}">
		<link rel="stylesheet" href="/${branding}/css/login.css" />
	</c:when>

	<c:when test="${model.pageName == 'myeuropeana.html'}">
		<link rel="stylesheet" href="/${branding}/css/myeuropeana.css" />
		<link rel="stylesheet" href="/${branding}/css/tabbed-navigation.css" />
	</c:when>

	<c:when test="${model.pageName == 'register.html'}">
		<link rel="stylesheet" href="/${branding}/css/register.css" />
	</c:when>

	<c:when test="${model.pageName == 'register-success.html'}">
		<link rel="stylesheet" href="/${branding}/css/login.css" />
	</c:when>

	<c:when test="${model.pageName == 'search.html'}">
		<link rel="stylesheet" href="/${branding}/css/search.css" />
		<link rel="stylesheet" href="/${branding}/css/search-navigation.css" />
		<link rel="stylesheet" href="/${branding}/css/search-icons.css" />
		<link rel="stylesheet" href="/${branding}/css/search-pagination.css" />
		<link rel="stylesheet" href="/${branding}/css/sidebar-facets.css" />
		<link rel="stylesheet" href="/${branding}/css/sidebar-legend.css" />
		<c:if test="${model.embedded}">
			<link rel="stylesheet" href="/${branding}/css/search-embed.css" />
		</c:if>
	</c:when>

	<c:when test="${model.pageName == 'staticpage.html'}">
		<link rel="stylesheet" href="/${branding}/css/staticpages.css" />
	</c:when>

	<c:when test="${model.pageName == 'map.html'}">
		<link rel="stylesheet" href="/${branding}/css/search-navigation.css" />
		<link rel="stylesheet" href="/${branding}/css/search-icons.css" />
		<link rel="stylesheet" href="/${branding}/css/search-pagination.css" />
		<link rel="stylesheet" href="/${branding}/css/map.css" />
		<link rel="stylesheet" href="/${branding}/js/simile-widgets/timeplot-1.1/timeplot-bundle.css" />
		<link rel="stylesheet" href="/${branding}/js/simile-widgets/ajax-2.2.2/styles/graphics-modified.css" />
		<link rel="stylesheet" href="/${branding}/js/simile-widgets/timeplot-1.1/timeplot-bundle.css" />
		<link rel="stylesheet" href="/${branding}/js/sti/e4D-javascript/Sti.css" />
		<%--
			could not lazy load, so put it here for the moment
			@link http://www.google.nl/search?aq=f&gcx=w&sourceid=chrome&ie=UTF-8&q=G_PHYSICAL_MAP+is+not+defined
		--%>
		<%-- <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;sensor=false&amp;key=${model.googleMapsId}"></script> --%>
	</c:when>

	<c:when test="${model.pageName == 'timeline.html'}">
		<link rel="stylesheet" href="/${branding}/css/search-navigation.css" />
		<link rel="stylesheet" href="/${branding}/css/search-icons.css" />
		<link rel="stylesheet" href="/${branding}/css/search-pagination.css" />
		<link rel="stylesheet" href="/${branding}/js/simile-widgets/ajax-2.2.2/styles/graphics-modified.css" />
		<link rel="stylesheet" href="/${branding}/js/simile-widgets/timeline-2.3.1/timeline-bundle-modified.css" />
		<!--[if IE6]><link rel="stylesheet" href="/${branding}/js/simile-widgets/ajax-2.2.2/styles/graphics-ie6.css" /><![endif]-->
	</c:when>
</c:choose>

<!--[if IE]><link rel="stylesheet" href="/${branding}/css/ie.css" /><![endif]-->
<!--[if lte IE 7]><link rel="stylesheet" href="/${branding}/css/ie7.css" /><![endif]-->
<!--[if lte IE 8]><link rel="stylesheet" href="/${branding}/css/ie8.css" /><![endif]-->
<noscript><link rel="stylesheet" href="/${branding}/css/noscript.css" /></noscript>
<!-- debug-css -->
