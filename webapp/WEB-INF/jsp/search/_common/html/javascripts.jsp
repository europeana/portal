<#include '/_common/html/no-js.ftl'/>
<#include '/_common/variables/variables-javascript.ftl'/>

<#if model.minify?? && model.minify>

	<script src="/${branding}/js/eu/europeana/bootstrap/min/bootstrap.min.js"></script>
	
<#else/>

	<script src="/${branding}/js/eu/europeana/bootstrap/bootstrap.js?${.now?long?c}"></script>
	<#--script src="/${branding}/js/eu/europeana/bootstrap/bootstrap-debug.js"></script-->
	
</#if>