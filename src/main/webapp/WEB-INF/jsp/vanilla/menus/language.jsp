<form method="post" action="{language_menu_action?html}">
	<select title="<spring:message code='ChooseLanguage_t' />">
		<option value="Choose language" selected="selected"><spring:message code='ChooseLanguage_t' /></option>
			<list model.portalLanguages as language>
				<option value="{language.getLanguageCode()}">{language.getLanguageName()}</option>
			</list>
	</select>
	<noscript><input type="submit" /></noscript>
</form>
