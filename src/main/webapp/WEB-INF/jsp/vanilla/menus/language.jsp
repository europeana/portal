<li>
  <form method="post" action="${language_menu_action}">
    <select title="<spring:message code='ChooseLanguage_t' />">
      <option value="Choose language" selected="selected"><spring:message code='ChooseLanguage_t' /></option>
      <c:forEach items="${model.portalLanguages}" var="language">
        <option value="${language.getLanguageCode()}">${language.getLanguageName()}</option>
      </c:forEach>
    </select>
    <noscript><input type="submit" /></noscript>
  </form>
</li>
