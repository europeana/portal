<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="view" value="" />
<c:set var="query" value="" />
<c:set var="langActionUrl" value="" />
<c:set var="MyEuropeanaCurrent" value="" />
<c:set var="CommunitiesCurrent" value="" />
<c:set var="PartnersCurrent" value="" />
<c:set var="AboutUsCurrent" value="" />
<c:set var="ThoughtLabCurrent" value="" />

<c:set var="branding" value="${model.portalName}/themes/${model.theme}" />

<c:if test="${!empty model.locale}"><c:set var="locale" value="${model.locale}" /></c:if>

<c:if test="${!empty RequestParameters.view}"><c:set var="view" value="${RequestParameters.view}" /></c:if>
<c:if test="${!empty RequestParameters.query}"><c:set var="query" value="${RequestParameters.query}" /></c:if>
<c:if test="${model.pageName == 'search.html'}"><c:set var="langActionUrl" value="${model.viewUrlTable}" /></c:if>

<%-- menus --%>
<c:set var="menu1_active" value="" />
<c:set var="menu2_active" value="" />
<c:set var="menu3_active" value="" />
<c:set var="menu4_active" value="" />
<c:set var="menu5_active" value="" />
<c:set var="menu6_active" value="" />

<c:if test="${'index.html' == model.pageName}"><c:set var="menu1_active" value="active " /></c:if>
<c:if test="${'timeline.html' == model.pageName || 'map.html' == model.pageName}"><c:set var="menu2_active" value="active " /></c:if>
<c:if test="${'login.html' == model.pageName || 'myeuropeana/index' == model.pageName}"><c:set var="menu6_active" value="active " /></c:if>

<%-- search navigation icons --%>
<c:set var="gridview_active" value="" />
<c:set var="timeline_active" value="" />
<c:set var="map_active" value="" />

<c:if test="${'search.html' == model.pageName}"><c:set var="gridview_active" value="active " /></c:if>
<c:if test="${'timeline.html' == model.pageName}"><c:set var="timeline_active" value="active " /></c:if>
<c:if test="${'map.html' == model.pageName}"><c:set var="map_active" value="active " /></c:if>

<%-- a css class holder for the <div id="query> so that it can accommodate for the spacing issue --%>
<c:set var="menu_user_exists" value="" />

<c:set var="query_action" value="/${model.portalName}/search.html" />
<c:if test="${'timeline.html' == model.pageName}"><c:set var="query_action" value="/${model.portalName}/timeline.html" /></c:if>
<c:if test="${'map.html' == model.pageName}"><c:set var="query_action" value="/${model.portalName}/map.html" /></c:if>

<c:set var="language_menu_action" value="${model.currentUrl}" />
<c:if test="${empty model.currentUrl}"><c:set var="language_menu_action" value="${model.portalName}/search.html" /></c:if>

<c:set var="header_class" value=' class="notranslate"' />
<c:if test="${empty model.embedded && !empty model.user}"><c:set var="header_class" value=' class="notranslate user-bar-added"' /></c:if>

<c:set var="titleMaxLength" value="40" />
<c:set var="providerMaxLength" value="25" />
<c:set var="providerNameMaxLength" value="25" />
