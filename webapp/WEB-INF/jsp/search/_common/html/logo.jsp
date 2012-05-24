<#assign logo_tag = 'h1'/>
<#if 'full-doc.html' == '${model.pageName}'><#assign logo_tag = 'div'/></#if>

<${logo_tag} id="logo">

	
	<a href="/${model.portalName}/" title="<@spring.message 'AltLogoEuropeanaHeader_t'/>">
	
		<img src="/${model.portalName}/sp/img/europeana-logo-${model.imageLocale}.png" alt="<@spring.message 'AltLogoEuropeanaHeader_t'/>" width="206" height="123"/>
		
	</a>
	
</${logo_tag}>
