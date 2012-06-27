<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<form method="post" action="${language_menu_action}" id="language-selector">
	<select id="embeddedlang" name="embeddedlang" title="<spring:message code="ChooseLanguage_t" />">
		<option value="Choose language" selected="selected"><spring:message code="ChooseLanguage_t" /></option>
		<c:forEach items="${model.portalLanguages}" var="language">
			<option value="${language.getLanguageCode()}">${language.getLanguageName()}</option>
		</c:forEach>
	</select>
	<noscript><input type="submit" /></noscript>
</form>
