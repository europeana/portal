<div id="header-strip">
	<span>
		<a href="/${model.portalName}/" target="<spring:message code="notranslate_menu-main-1_a_target_t"/>" title="<spring:message code="menu-main-1_a_title_t"/>"><spring:message code="menu-main-1_a_text_t"/></a>
		
		<a href="<spring:message code="notranslate_menu-main-6_a_url_t"/>" target="<spring:message code="notranslate_menu-main-6_a_target_t"/>" title="<spring:message code="menu-main-6_a_title_t"/>"><spring:message code="menu-main-6_a_text_t"/></a>
	</span>

	<%-- Choose a language --%>
	<%@ include file="/WEB-INF/jsp/default/_common/menus/language.jsp" %>	
</div>
