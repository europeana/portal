<if model.user??>
	<spring:message code="LoggedInAs_t" />: <b>{model.user.userName?html}</b>

	<if model.user.savedItems?exists>
	| <spring:message code="SavedItems_t" />:
		<a href="/{model.portalName}/myeuropeana.html#saved-items">{model.user.savedItems?size}</a>
	</if>
	<if model.user.savedSearches?exists>
	| <spring:message code="SavedSearches_t" />:
	<a href="/{model.portalName}/myeuropeana.html#saved-searches">{model.user.savedSearches?size}</a>
	</if>
	<if model.user.socialTags?exists>
	| <spring:message code="SavedTags_t" />:
	<a href="/{model.portalName}/myeuropeana.html#saved-tags">{model.user.socialTags?size}</a>
	</if>
	| <a href="/{model.portalName}/logout.html"><spring:message code="LogOut_t" /></a>
</if>
