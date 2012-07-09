<div id="content">

  <div id="explanation">
  
    <@spring.message 'MyEuropeanaExplain_t' />
  
  </div>

  <div id="login-boxes">
        
        <#include '/myeuropeana/content/login/login-form.ftl'/>
    <#include '/myeuropeana/content/login/register-form.ftl'/>
    <#include '/myeuropeana/content/login/request-password-form.ftl'/>
    
    <div id="login-response">
    
      <#if model.errorMessage??>
        <b class="error">${model.errorMessage}</b>
      </#if>
      
      <#if model.forgotSuccess>
        <@spring.message 'AnEmailHasBeenSentTo_t' />: <b>${model.email}</b>.
        <@spring.message 'PleaseFollowTheLinkProvided_t' />.  <!-- TODO change message -->
          </#if>
          
          
          <#if model.failureForgotFormat>
        <b class="error"><@spring.message 'Error_t' />!</b>
        <@spring.message 'EmailFormatError_t' />.             
          </#if>
      
      
      <#if model.failureForgotDoesntExist>          
        <b class="error"><@spring.message 'Error_t' />!</b>
        <@spring.message 'EmailDoesntExist_t' /></span><br/>
          </#if>
          
           <#if model.success>
        <@spring.message 'AnEmailHasBeenSentTo_t' />: <b>${model.email}</b>.
        <@spring.message 'PleaseFollowTheLinkProvided_t' />.
        </#if>
        
        <#if model.failureFormat>
        <b class="error"><@spring.message 'Error_t' />!</b> <@spring.message 'EmailFormatError_t' />.
        </#if>
        
        <#if model.failureExists>
        <b class="error"><@spring.message 'Error_t' />!</b> <@spring.message 'EmailAlreadyRegistered_t' />.
        </#if>
        
    </div>
    
  </div>
  
</div>