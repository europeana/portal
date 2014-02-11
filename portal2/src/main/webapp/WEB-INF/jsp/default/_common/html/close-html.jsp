<div class="overlaid-content">
	<div class="iframe-wrap"></div>
</div>
<c:if test="${!empty model.debug && model.debug}">
<div id="browser-language-query">
	<c:set var="locale_language">language_${model.locale}_t</c:set>
	<label>model.locale</label>: ${model.locale}<br/>
	<label>model.browserLocale</label>: ${model.browserLocale}<br/>
	<label>locale language</label>: <spring:message code="${locale_language}" /><br/>
	<label>front-end derived sentence</label>: <spring:message code="browser_language_preference1_t" />&nbsp;<spring:message code="${locale_language}" />. <spring:message code="browser_language_preference2_t" />&nbsp;<spring:message code="${locale_language}" />? <a href="#yes" id="browser-language-change-yes"><spring:message code="yes_t" /></a> / <a href="#no" id="browser-language-change-no"><spring:message code="no_t" /></a><br/>
	<label>model.localeMessage</label>: ${model.localeMessage}
</div>
</c:if>
</body>
</html>
