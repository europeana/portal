<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:choose>
  <c:when test="${not empty model.currentUrl}">
    <c:set var="language_menu_action" value="${model.currentUrl}"/>
  </c:when>
  <c:otherwise>
    <c:set var="language_menu_action" value="${model.portalName}/search.html"/>
  </c:otherwise>
</c:choose>

<form action="${language_menu_action}" method="POST" id="language-selector" style="display:table-cell; vertical-align:middle;">
	<div class="eu-menu no-highlight white" id="lang-menu">
		<span class="menu-label hide-on-phones"></span>
		<span class="icon-arrow-3 open-menu hide-on-phones"></span>
		
		<span class="focus-access" tabindex="${nextTabIndex}"></span>
		<c:set var="nextTabIndex" value="${nextTabIndex+1}"/>
		
		<span class="icon-mobilemenu show-on-phones" id="mobile-menu"></span>

		<ul title="<spring:message code='ChooseLanguage_t' />">
	
			<c:if test="${!model.embedded}">
				<li class="item icon-home show-on-phones">
					<a class="treat-as-link" target="<spring:message code="notranslate_main_menu_home_a_target_t"/>" href="/${model.portalName}/"><spring:message code="main_menu_home_a_text_t"/></a>

				</li>
				<li class="item icon-logo show-on-phones">
					<a class="treat-as-link" target="<spring:message code="notranslate_main_menu_myeuropeana_a_target_t"/>" href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t"/>"><spring:message code="main_menu_myeuropeana_a_text_t"/></a>

				</li>
				<li class="item icon-info show-on-phones">
					<a class="treat-as-link" href="/${model.portalName}/usingeuropeana.html"><spring:message code='rswHelp_t'/></a>

				</li>
			</c:if>

			<li class="item">
				<a class="choose"><spring:message code='ChooseLanguage_t'/></a>
			</li>

			<c:forEach items="${model.portalLanguages}" var="language">
				<li class="item" tabIndex="${nextTabIndex}"><a class="${language.languageCode}">${language.languageName}</a></li>
				<c:set var="nextTabIndex" value="${nextTabIndex+1}"/>
			</c:forEach>
		</ul>
		<input type="hidden" name="lang" value="" style="margin:0;padding:0;"/>
	</div>
</form>