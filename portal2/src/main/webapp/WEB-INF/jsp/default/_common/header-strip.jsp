<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:choose>
	<c:when test="${!empty model.debug && model.debug}">
		<div id="header-strip" class="row">
			<c:choose>
				<%-- not logged in --%>
				<c:when test="${empty model.user}">

					<%-- logo --%>
					<a	href="/${model.portalName}/"
						target="<spring:message code="notranslate_main_menu_home_a_target_t" />"
						title="<spring:message code="main_menu_home_a_title_t" />"
						id="main-menu-logo"></a>

					<%-- home --%>
					<a	href="/${model.portalName}/"
						target="<spring:message code="notranslate_main_menu_home_a_target_t" />"
						title="<spring:message code="main_menu_home_a_title_t" />"
						class="white left"><spring:message code="main_menu_home_a_text_t" /></a>

					<%-- search --%>
					<a	href="/${model.portalName}/<spring:message code="notranslate_main_menu_search_a_url_t" />"
						target="<spring:message	code="notranslate_main_menu_search_a_target_t"/>"
						title="<spring:message code="main_menu_search_a_title_t" />"
						class="white left"><spring:message code="main_menu_search_a_text_t" /></a>

					<%-- exhibitions --%>
					<a	href="<spring:message code="notranslate_main_menu_exhibitions_a_url_t" />"
						target="<spring:message	code="notranslate_main_menu_exhibitions_a_target_t"/>"
						title="<spring:message code="main_menu_exhibitions_a_title_t" />"
						class="white left"><spring:message code="main_menu_exhibitions_a_text_t" /></a>

					<%-- blog --%>
					<a	href="<spring:message code="notranslate_main_menu_blog_a_url_t" />"
						target="<spring:message	code="notranslate_main_menu_blog_a_target_t"/>"
						title="<spring:message code="main_menu_blog_a_title_t" />"
						class="white left"><spring:message code="main_menu_blog_a_text_t" /></a>

					<%-- my europeana --%>
					<a	href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t" />"
						target="<spring:message	code="notranslate_main_menu_myeuropeana_a_target_t"/>"
						title="<spring:message code="main_menu_myeuropeana_a_title_t" />"
						class="white left"><spring:message code="main_menu_myeuropeana_a_text_t" /></a>

				</c:when>

				<%-- logged in --%>
				<c:otherwise>
					<span class="white">
						<spring:message code="LoggedInAs_t" />:
						<a	href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t" />#user-information"
							target="<spring:message code="notranslate_main_menu_myeuropeana_a_target_t" />"
							title="<spring:message code="main_menu_myeuropeana_a_title_t" />"><b>${fn:escapeXml(model.user.userName)}</b></a>

						<c:if test="${!empty model.user.savedItems}">
							&nbsp;|&nbsp;
							<spring:message code="SavedItems_t" />:
							<a id="saved-items-count" href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t" />#saved-items">${fn:length(model.user.savedItems)}</a>
						</c:if>

						<c:if test="${!empty model.user.savedSearches}">
							&nbsp;|&nbsp;
							<spring:message code="SavedSearches_t" />:
							<a id="saved-searches-count" href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t" />#saved-searches">${fn:length(model.user.savedSearches)}</a>
						</c:if>

						<c:if test="${!empty model.user.socialTags}">
							&nbsp;|&nbsp;
							<spring:message code="SavedTags_t" />:
							<a id="saved-tags-count" href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t" />#saved-tags">${fn:length(model.user.socialTags)}</a>
						</c:if>

						&nbsp;|&nbsp;
						<a href="/${model.portalName}/logout.html"><spring:message code="LogOut_t" /></a>
					</span>
				</c:otherwise>
			</c:choose>

			<%-- cog wheel --%>
			<a	href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t" />"
					target="<spring:message	code="notranslate_main_menu_myeuropeana_a_target_t"/>"
					title="<spring:message code="main_menu_myeuropeana_a_title_t" />"
					id="cog"><spring:message code="main_menu_myeuropeana_a_text_t" /></a>
		
		</div>
	</c:when>

	<c:otherwise>
		<div id="header-strip" class="row">
			<div>
				<span class="hide-on-phones">
					<c:choose>
						<c:when test="${empty model.user}">
							<%-- [Home] [My Europeana] --%>
							<a	href="/${model.portalName}/"
								target="<spring:message code="notranslate_main_menu_home_a_target_t" />"
								title="<spring:message code="main_menu_home_a_title_t" />"
								class="white left"><spring:message code="main_menu_home_a_text_t" /></a>
							<a	href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t" />"
								target="<spring:message	code="notranslate_main_menu_myeuropeana_a_target_t"/>"
								title="<spring:message code="main_menu_myeuropeana_a_title_t" />"
								class="white left"><spring:message code="main_menu_myeuropeana_a_text_t" /></a>
						</c:when>

						<c:otherwise>
							<span class="white">
								<spring:message code="LoggedInAs_t" />:
								<a	href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t" />#user-information"
									target="<spring:message code="notranslate_main_menu_myeuropeana_a_target_t" />"
									title="<spring:message code="main_menu_myeuropeana_a_title_t" />"><b>${fn:escapeXml(model.user.userName)}</b></a>

								<c:if test="${!empty model.user.savedItems}">
									&nbsp;|&nbsp;
									<spring:message code="SavedItems_t" />:
									<a id="saved-items-count" href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t" />#saved-items">${fn:length(model.user.savedItems)}</a>
								</c:if>

								<c:if test="${!empty model.user.savedSearches}">
									&nbsp;|&nbsp;
									<spring:message code="SavedSearches_t" />:
									<a id="saved-searches-count" href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t" />#saved-searches">${fn:length(model.user.savedSearches)}</a>
								</c:if>

								<c:if test="${!empty model.user.socialTags}">
									&nbsp;|&nbsp;
									<spring:message code="SavedTags_t" />:
									<a id="saved-tags-count" href="/${model.portalName}/<spring:message code="notranslate_main_menu_myeuropeana_a_url_t" />#saved-tags">${fn:length(model.user.socialTags)}</a>
								</c:if>

								&nbsp;|&nbsp;

								<a href="/${model.portalName}/logout.html"><spring:message code="LogOut_t" /></a>

							</span>
						</c:otherwise>
					</c:choose>
				</span>

				<%-- Choose a language --%>
				<%@ include file="/WEB-INF/jsp/default/_common/menus/language.jsp" %>
			</div>
		</div>
	</c:otherwise>
</c:choose>
