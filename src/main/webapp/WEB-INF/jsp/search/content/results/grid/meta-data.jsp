<div class="meta-data${li_class}">
	
	
	<#if cell.title?? & cell.title != "">	
		
		<h4>${title}</h4>
		
	</#if>
	
	
	<a href="${cell.fullDocUrl}" title="${cell.title}" style="float: left; margin-right: 10px;"><img src="${cell.thumbnail}"/></a>
	
	
	<#if cell.creator?? & cell.creator != "">
	
		<span class="creator"><@stringLimiter "${cell.creatorXML}" providerNameMaxLength/></span><br/>
		
	</#if>
	
	
	<#if cell.dataProvider?? & cell.dataProvider != "">
						
		<span class="data-provider"><@stringLimiter "${cell.dataProvider}" providerMaxLength /></span><br/>
		
	</#if>
	
	
	<#if cell.provider?? & cell.provider != "">
						
		<span class="provider"><@stringLimiter "${cell.provider}" providerMaxLength /></span><br/>
			
	</#if>
	
	
	<#if cell.year != "" & cell.year != "0000">
						
		<span class="year">${cell.year}</span>
			
	</#if>
	
	
</div>