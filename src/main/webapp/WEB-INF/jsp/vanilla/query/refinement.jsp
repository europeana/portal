	<form id="refine-search-form" method="get" action="${query_action}">
	    
        <fieldset>
	        <#assign className></#assign> 
	        <#if model.debug?? && model.debug && 'map.html' = model.pageName>
		        <#assign className> map-query</#assign>
			</#if>

        	<input type="text" name="rq" id="rq" value="" class="${className}" title="<@spring.message 'RefineYourSearch_t'/>">
			<input type="hidden" name="query" <#if model.query??>value="${model.query?html}"</#if>/>
			<input type="submit" class="submit-button" value="<@spring.message 'RefineYourSearch_t' />" />
			
			
			<#if model.debug?? && model.debug && 'map.html' = model.pageName>
				<input type="checkbox" id="box_search_refine"/>
				<label for="box_search_refine"><@spring.message 'MapBoxedSearch_t'/></label>
			</#if>
			
			<a href="" id="close-refine-search" rel="nofollow">Hide Refine Search</a>
			
			
	        <#--
				@willem jan
				2011-09-21 13.46 GMT +1
				if a qf value has " in it they should be converted to &quo; so the html passes validation
				also what are the differences between all of these qf values and are they all needed?
				
				existing query terms
			-->
			
				<#if model.refinements??>
					
					<#list model.refinements as refinement>
						
						<input type="hidden" name="qf" value="${refinement?html}"/>
						
					</#list>
				
				</#if>
				
				<#if model.providersForInclusion??>
				
					<#list model.providersForInclusion as providerForInclusion>
			         		
			     		<input type="hidden" name="qf" value="${providerForInclusion?html}" />
			     		
					</#list>
					
				</#if>
				
				
				<#if model.facetsForInclusion??>
				
					<#list model.facetsForInclusion as facetsForInclusion>
			         		
			     		<input type="hidden" name="qf" value="${facetsForInclusion?html}" />
			     		
					</#list>
					
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
        
    </form>