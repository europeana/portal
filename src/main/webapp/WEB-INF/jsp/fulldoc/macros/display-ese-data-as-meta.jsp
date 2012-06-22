<#--
 * display-ese-data-as-meta.ftl
 *
 * This macro was built in order to render ese ( Europeana Semantic Elements ) meta data fields
 * in the html context for search engine optimization ( ? ) using the <meta> tag
 * or to display them in <p> blocks for diagnostic purposes.
 *
 * The default behavior is to render the ese meta data in <meta> tags. The 
 * diagnostic mode is triggered by adding the query string &format=labels to the url 
 *
 *
 * @param metaDataFields array - the meta data collection produced by the model
 * @param showFieldName bool - toggles diagnostic mode vs <meta> output
 * 
 * @author unknown
 * @author Dan Entous
 * @modified 2011-06-08 08.25 GMT+1
 -->

<#macro displayEseDataAsMeta metaDataFields showFieldName>
	
	<#list metaDataFields as metaDataField>
	
		<#list metaDataField.fieldValues as value>
		
       		<#if showFieldName>
	    		
		    	<p class="notranslate">
		    	
		    		<b>${metaDataField.fieldName}</b> = ${value.valueXML}
		    		
		    	</p>
       		
       		<#else>
       		
            	<meta about="${model.document.id}" property="${metaDataField.property}" content="${value.valueXML}"/> 
	        	
	        </#if>
	        
    	</#list>
    	
    </#list>
    
</#macro>