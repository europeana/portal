<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="header-strip" class="row">
	<div>
		<span class="hide-on-phones">

			<c:set var="nextTabIndex" value="2"/>

			<c:choose>
				<c:when test="${empty model.user}">

					<%-- [Home] [My Europeana] --%>
				
					<a	tabIndex="${nextTabIndex}"
						href="/${model.portalName}/"
						target="<spring:message code="notranslate_main_menu_home_a_target_t"/>"
						title="<spring:message code="main_menu_home_a_title_t"/>"
						class="white left xtab"><spring:message code="main_menu_home_a_text_t"/></a>
					
					<c:set var="nextTabIndex" value="${nextTabIndex+1}"/>
					
					<a	tabIndex="${nextTabIndex}"
						href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t"/>"
						target="<spring:message	code="notranslate_main_menu_myeuropeana_a_target_t"/>"
						title="<spring:message code="main_menu_myeuropeana_a_title_t"/>"
						class="white left xtab"><spring:message code="main_menu_myeuropeana_a_text_t"/></a>
						
					<c:set var="nextTabIndex" value="${nextTabIndex+1}"/>
						
				</c:when>
				<c:otherwise>
					<span class="white">
						<spring:message code="LoggedInAs_t"/>:
						<a	tabIndex="${nextTabIndex}"
							href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t"/>#user-information"
							target="<spring:message code="notranslate_main_menu_myeuropeana_a_target_t"/>"
							title="<spring:message code="main_menu_myeuropeana_a_title_t"/>"><b>${fn:escapeXml(model.user.userName)}</b></a>
						
						<c:set var="nextTabIndex" value="${nextTabIndex+1}"/>
						
						<c:if test="${!empty model.user.savedItems}">
							&nbsp;|&nbsp;
							<spring:message code="SavedItems_t"/>:
							<a tabIndex="${nextTabIndex}" id="saved-items-count" href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t"/>#saved-items">${fn:length(model.user.savedItems)}</a>
							
							<c:set var="nextTabIndex" value="${nextTabIndex+1}"/>
						</c:if>
						
						<c:if test="${!empty model.user.savedSearches}">
							&nbsp;|&nbsp;
							<spring:message code="SavedSearches_t"/>:
							<a tabIndex="${nextTabIndex}" id="saved-searches-count" href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t"/>#saved-searches">${fn:length(model.user.savedSearches)}</a>
							
							<c:set var="nextTabIndex" value="${nextTabIndex+1}"/>
						</c:if>

						<c:if test="${!empty model.user.savedSearches}">
							&nbsp;|&nbsp;
							<spring:message code="SavedTags_t"/>:
							<a tabIndex="${nextTabIndex}" id="saved-tags-count" href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t"/>#saved-tags">${fn:length(model.user.socialTags)}</a>
							
							<c:set var="nextTabIndex" value="${nextTabIndex+1}"/>
						</c:if>

						&nbsp;|&nbsp;
						
						<a tabIndex="${nextTabIndex}" href="/${model.portalName}/logout.html"><spring:message code="LogOut_t"/></a>
						
						<c:set var="nextTabIndex" value="${nextTabIndex+1}"/>
						
					</span>
				</c:otherwise>
			</c:choose>
		</span>

		<%-- Choose a language --%>
		<%@ include file="/WEB-INF/jsp/default/_common/menus/language.jsp" %>
	</div>
</div>
