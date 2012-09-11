<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:if test="${!empty model.user}">
  <%-- a css class holder for the <div id="query> so that it can accommodate for the spacing issue --%>
  <c:set var="menu_user_exists" value=' class="menu-user-exists"' />
  <div id="user-bar" class="notranslate">
    <spring:message code="LoggedInAs_t" />: <b>${fn:escapeXml(model.user.userName)}</b>

    <%-- c:if test="${model.user[savedItems]}" --%>
      | <spring:message code="SavedItems_t" />:
      <a href="/${model.portalName}/myeuropeana.html#saved-items" id="saved-items-count">${fn:length(model.user.savedItems)}</a>
    <%-- /c:if --%>

    <%-- c:if test="${model.user[savedSearches]}" --%>
      | <spring:message code="SavedSearches_t" />:
      <a href="/${model.portalName}/myeuropeana.html#saved-searches" id="saved-searches-count">${fn:length(model.user.savedSearches)}</a>
    <%-- /c:if --%>

    <%-- c:if test="${model.user[socialTags]}" --%>
      | <spring:message code="SavedTags_t" />:
      <a href="/${model.portalName}/myeuropeana.html#saved-tags" id="saved-tags-count">${fn:length(model.user.socialTags)}</a>
    <%-- /c:if --%>
    | <a href="/${model.portalName}/logout.html"><spring:message code="LogOut_t" /></a>
  </div>
</c:if>
