<link rel="shortcut icon" href="/${model.portalName}/favicon.ico"/>
<link rel="search" type="application/opensearchdescription+xml"  href="http://api.europeana.eu/api/opensearch.xml" title="Europeana Search"/>

<#if model.minify?? && model.minify>
	
	<#include '/_common/html/css/production-css.ftl'/>
	
<#else/>

	<#include '/_common/html/css/debug-css.ftl'/>
	
</#if>


<#if model.headerContent??>${model.headerContent}</#if>
