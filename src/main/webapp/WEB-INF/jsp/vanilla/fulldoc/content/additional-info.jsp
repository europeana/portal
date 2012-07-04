<#if model.fieldsAdditional?? && ( model.fieldsAdditional?size > 0 )>

  <@displayEseDataAsHtml model.fieldsAdditional 'div'  model.document.userGeneratedContent true/>
  
</#if>