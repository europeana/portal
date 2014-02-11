<div class="overlaid-content">
	<div class="iframe-wrap"></div>
</div>
<c:if test="${!empty model.debug && model.debug}">
<p id="browser-language-query">
	<c:set var="browser_locale">language_${model.browserLocale}_t</c:set>
	<spring:message code="browser_language_preference1_t" />&nbsp;<spring:message code="${browser_locale}" />; <spring:message code="browser_language_preference2_t" />&nbsp;<spring:message code="${browser_locale}" />? <a href="#yes" id="browser-language-change-yes"><spring:message code="yes_t" /></a> / <a href="#no" id="browser-language-change-no"><spring:message code="no_t" /></a><br/>
	${model.localeMessage}
</p>
</c:if>
</body>
</html>
