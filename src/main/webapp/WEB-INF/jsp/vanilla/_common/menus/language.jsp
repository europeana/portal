
<c:choose>
  <c:when test="${not empty model.currentUrl}">
    <c:set var="language_menu_action" value="${model.currentUrl}"/>
  </c:when>
  <c:otherwise>
    <c:set var="language_menu_action" value="${model.portalName}/search.html"/>
  </c:otherwise>
</c:choose>
<li>
  <form action="${language_menu_action}" method="post">
    <select name="embeddedlang" title="<spring:message code='ChooseLanguage_t' />">
      <option value="<spring:message code='ChooseLanguage_t'/>" selected><spring:message code='ChooseLanguage_t'/></option>
      <c:forEach items="${model.portalLanguages}" var="language">
        <option value="${language.languageCode}">${language.languageName}</option>
      </c:forEach>
    </select>
    <input type="submit"/>
  </form>
</li>
