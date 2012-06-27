<#if model.user??>

	<#-- a css class holder for the <div id="query> so that it can accommodate for the spacing issue -->
	<#assign menu_user_exists = ' class="menu-user-exists"'/>
	
	<div id="user-bar" class="notranslate">
	
		<@spring.message 'LoggedInAs_t' />: <b>${model.user.userName?html}</b>
		
		<#if model.user.savedItems?exists>
			
			| <@spring.message 'SavedItems_t' />: 
			
			<#-- onclick="$.cookie('ui-tabs-3', '1', { expires: 1 });" -->
			<a href="/${model.portalName}/myeuropeana.html#saved-items" id="saved-items-count">${model.user.savedItems?size}</a>
			
		</#if>
		
		<#if model.user.savedSearches?exists>
		
			| <@spring.message 'SavedSearches_t' />:
			
			<#--onclick="$.cookie('ui-tabs-3', '2', { expires: 1 });"-->
			<a href="/${model.portalName}/myeuropeana.html#saved-searches" id="saved-searches-count">${model.user.savedSearches?size}</a>
			
		</#if>
		
		<#if model.user.socialTags?exists>
		
			| <@spring.message 'SavedTags_t' />:
			
			<#-- onclick="$.cookie('ui-tabs-3', '3', { expires: 1 });"-->
			<a href="/${model.portalName}/myeuropeana.html#saved-tags" id="saved-tags-count">${model.user.socialTags?size}</a>
			
		</#if>
		
		| <a href="/${model.portalName}/logout.html"><@spring.message 'LogOut_t' /></a>
		
	</div>
	
</#if>
