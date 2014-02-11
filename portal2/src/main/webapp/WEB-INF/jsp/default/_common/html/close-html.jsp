<div class="overlaid-content">
	<div class="iframe-wrap"></div>
</div>
<c:if test="${!empty model.browserLocale && !empty model.debug && model.debug}">
<p id="browser-language-query">
	<c:set var="browser_locale">language_${model.browserLocale}_t</c:set>
	<spring:message code="browser_language_preference1_t" />&nbsp;<spring:message code="${browser_locale}" />; <spring:message code="browser_language_preference2_t" />&nbsp;<spring:message code="${browser_locale}" />? <a href="#yes" class="browser-language-change"><spring:message code="yes_t" /></a> / <a href="#no" class="browser-language-change"><spring:message code="no_t" /></a><br/>
	${model.localeMessage}&nbsp;<a href="#yes" class="browser-language-change"><spring:message code="yes_t" /></a> / <a href="#no" class="browser-language-change"><spring:message code="no_t" /></a>
</p>
</c:if>
</body>
</html>
