<if model.user??>
<div>

  <h1>user menu</h1>
	<springmessage 'LoggedInAs_t' />: <b>{model.user.userName?html}</b>
	
	<if model.user.savedItems?exists>
		
		| <springmessage 'SavedItems_t' />:		
		<a href="/{model.portalName}/myeuropeana.html#saved-items" id="saved-items-count">{model.user.savedItems?size}</a>
		
	</if>
	
	<if model.user.savedSearches?exists>
	
		| <springmessage 'SavedSearches_t' />:		
		<a href="/{model.portalName}/myeuropeana.html#saved-searches" id="saved-searches-count">{model.user.savedSearches?size}</a>
		
	</if>
	
	<if model.user.socialTags?exists>
	
		| <springmessage 'SavedTags_t' />:
		<a href="/{model.portalName}/myeuropeana.html#saved-tags" id="saved-tags-count">{model.user.socialTags?size}</a>
		
	</if>
	
	| <a href="/{model.portalName}/logout.html"><springmessage 'LogOut_t' /></a>
	
</div>
</if>