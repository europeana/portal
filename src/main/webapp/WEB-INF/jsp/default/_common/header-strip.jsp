<div id="header-strip">
	<span class="hide-on-phones">
		<a href="/${model.portalName}/" target="<spring:message code="notranslate_main_menu_home_a_target_t"/>" title="<spring:message code="main_menu_home_a_title_t"/>"><spring:message code="main_menu_home_a_text_t"/></a>		
		<a href="<spring:message code="notranslate_main_menu_myeuropeana_a_url_t"/>" target="<spring:message code="notranslate_main_menu_myeuropeana_a_target_t"/>" title="<spring:message code="main_menu_myeuropeana_a_title_t"/>"><spring:message code="main_menu_myeuropeana_a_text_t"/></a>
	</span>

	<%-- mobile menu links --%>
	<div class="menu show-on-phones"></div>
	
	<%-- Choose a language --%>
	<%@ include file="/WEB-INF/jsp/default/_common/menus/language.jsp" %>
	
</div>
