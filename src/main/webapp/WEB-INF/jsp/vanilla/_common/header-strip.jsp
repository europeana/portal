<div id="header-strip" class="row">
<div>
<span class="hide-on-phones"><a href="/${model.portalName}/" target="<spring:message code="notranslate_main_menu_home_a_target_t"/>" title="<spring:message code="main_menu_home_a_title_t"/>" class="white left"><spring:message code="main_menu_home_a_text_t"/></a>
<c:choose>
<c:when test="${!empty model.user}">
<span class="white"><spring:message code="LoggedInAs_t"/>: <a href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t"/>#user-information" target="<spring:message code="notranslate_main_menu_myeuropeana_a_target_t"/>" title="<spring:message code="main_menu_myeuropeana_a_title_t"/>"><b>${fn:escapeXml(model.user.userName)}</b></a>
<c:if test="${!empty model.user.savedItems}">
&nbsp;|&nbsp;
<spring:message code="SavedItems_t"/>:
<a id="saved-items-count" href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t"/>#saved-items">${fn:length(model.user.savedItems)}</a>
</c:if>
<c:if test="${!empty model.user.savedSearches}">
&nbsp;|&nbsp;
<spring:message code="SavedSearches_t"/>:
<a id="saved-searches-count" href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t"/>#saved-searches">${fn:length(model.user.savedSearches)}</a>
</c:if>
<c:if test="${!empty model.user.savedSearches}">
&nbsp;|&nbsp;
<spring:message code="SavedTags_t"/>:
<a id="saved-tags-count" href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t"/>#saved-tags">${fn:length(model.user.socialTags)}</a>
</c:if>
&nbsp;|&nbsp;
<a href="/${model.portalName}/logout.html"><spring:message code="LogOut_t"/></a></span>
</c:when>
<c:otherwise>
<%-- uncomment this to restore myeuropeana link --%>
<a href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t"/>" target="<spring:message code="notranslate_main_menu_myeuropeana_a_target_t"/>" title="<spring:message code="main_menu_myeuropeana_a_title_t"/>" class="white left"><spring:message code="main_menu_myeuropeana_a_text_t"/></a>
</c:otherwise>		
</c:choose>	
</span>
<%-- mobile menu links --%>
<div id="mobile-menu" class="eu-menu icon-mobilemenu show-on-phones" aria-hidden="true">
<ul>
<li class="item icon-home"><a class="/${model.portalName}/" target="<spring:message code="notranslate_main_menu_home_a_target_t"/>" href="/${model.portalName}/"><spring:message code="main_menu_home_a_text_t"/></a></li>
<li class="item icon-logo"><a class="<spring:message code="notranslate_main_menu_myeuropeana_a_url_t"/>" target="<spring:message code="notranslate_main_menu_myeuropeana_a_target_t"/>" href="<spring:message code="notranslate_main_menu_myeuropeana_a_url_t"/>"><spring:message code="main_menu_myeuropeana_a_text_t"/></a></li>
<%-- language options --%>
<li class="item lang"><spring:message code='ChooseLanguage_t' /></li>
<c:forEach items="${model.portalLanguages}" var="language">
<li class="item lang"><a class="${language.languageCode}">${language.languageName}</a></li>
</c:forEach>
</ul>
</div>
<%-- Choose a language --%>
<%@ include file="/WEB-INF/jsp/vanilla/_common/menus/language.jsp" %>
</div>
</div>