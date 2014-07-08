<div class="overlaid-content">
	<div class="iframe-wrap"></div>
</div>
<c:if test="${model.doTranslation}">
<p id="browser-language-query">
	<c:set var="browser_locale">language_${model.browserLanguage}_t</c:set>
	<spring:message code="browser_language_preference1_t" />&nbsp;<spring:message code="${browser_locale}" />; <spring:message code="browser_language_preference2_t" />&nbsp;<spring:message code="${browser_locale}" />? <a href="#yes" class="browser-language-change"><spring:message code="yes_t" /></a> / <a href="#no" class="browser-language-change"><spring:message code="no_t" /></a><br/>
	${model.localeMessages[0]}&nbsp;<a href="#yes" class="browser-language-change">${model.localeMessages[1]}</a> / <a href="#no" class="browser-language-change">${model.localeMessages[2]}</a>
</p>
</c:if>
</body>
</html>
