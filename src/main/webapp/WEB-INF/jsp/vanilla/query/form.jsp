<form id="query-search" method="get" action="${query_action}">
    
    <fieldset>
        <#assign className></#assign> 
        <#if model.debug?? && model.debug && 'map.html' = model.pageName>
	        <#assign className> map-query</#assign>
		</#if>
    
        <input type="text" name="query" id="query-input" class="${className}" title="<@spring.message 'SearchTerm_t'/>" <#if model.query??>value="${model.query?html}"</#if> maxlength="175"/>
        
        <input type="submit" class="submit-button" value="<@spring.message 'Search_t' />"/>
        
        		
			<#--
				save search link
			-->
			
			<#if model.debug?? && model.debug && 'map.html' = model.pageName>
				<input type="checkbox" id="box_search"/>
				<label for="box_search"><@spring.message 'MapBoxedSearch_t'/></label>
			</#if>
				
			<#if model.embedded>
				<input type="hidden" name="embedded" 			value="${model.embeddedString?html}" />
				<input type="hidden" name="embeddedBgColor" 	value="${model.embeddedBgColor?html}" />
				<input type="hidden" name="embeddedForeColor" 	value="${model.embeddedForeColor?html}" />
				<input type="hidden" name="embeddedLogo"	 	value="${model.embeddedLogo?html}" />
				<input type="hidden" name="rswUserId"	 		value="${model.rswUserId?html}" />
				<input type="hidden" name="rswDefqry"			value="${model.rswDefqry?html}" />
				<input type="hidden" name="lang"				value="${model.locale?html}"/>
			</#if>
			
	</fieldset>
    
    
	<#--
		additional feature links for the search box
	-->
	
		<#if !model.embedded>
        
			<#--
				refine search link
			-->

				<#assign refinedEnabled=" disabled">
							
				<#if model.enableRefinedSearch?? && model.enableRefinedSearch>
					<#assign refinedEnabled="">
				</#if>
			
				<a href="" id="refine-search" class="nofollow${refinedEnabled}"><@spring.message 'RefineYourSearch_t'/></a>
					
		
		
			<#--
				save search link
			-->
			
				<#if model.user?? && 'search.html' = model.pageName>
				
					<a href="" id="save-search" rel="nofollow"><@spring.message 'SaveToMyEuropeana_t'/></a>
					<#if model.briefBeanView??><input type="hidden" id="query-to-save" value="${model.briefBeanView.pagination.presentationQuery.queryToSave}"/></#if>
					<#if model.query??><input type="hidden" id="query-string-to-save" value="${model.query?url("utf-8")?js_string}"/></#if>
				
				</#if>
			
			
			<#--
				help link
			-->
			
				<a href="/${model.portalName}/usingeuropeana.html" id="search-help" class="${className}"><@spring.message 'rswHelp_t'/></a>
        		
        </#if>
    
</form>