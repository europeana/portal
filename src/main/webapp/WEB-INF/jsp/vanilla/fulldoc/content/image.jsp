<@compress single_line=true>
<#if model.imageRef??>
  
  
  
<!--[if lte IE 7]>
<style type="text/css">

</style>
<![endif]-->

  
  <#assign urlRefIdImg  = "urlRefIsShownAtImg"/>  
  <#if model.urlRefIsShownBy>
    <#assign urlRefIdImg = "urlRefIsShownByImg"/>
  </#if>


<a id="${urlRefIdImg}"
  
  
  <#if lightboxRef??>
    <a id="lightbox_href" href="/${model.portalName}/redirect.html?shownAt=${model.urlRef?url('utf-8')}&amp;provider=${model.document.europeanaDataProvider[0]?url('utf-8')}&amp;id=${model.document.id}" target="_blank">
      <img class="trigger" src="${model.thumbnailUrl}" alt="${model.pageTitle}"/>
    </a>
    <div class="trigger bold view">
      <span class="trigger read" title="<@spring.message 'view_t'/>" rel="#lightbox"><@spring.message 'view_t'/></span>
    </div>
  <#else>
  
    <a href="${model.urlRef}"
      class=" image ajax"
      target="_blank"
      rel="nofollow"
      resource="${model.urlRef}"
      id="${urlRefIdImg}"
      title="${model.pageTitle}">
      
      <img src="${model.thumbnailUrl}" alt="${model.pageTitle}" data-type="${model.document.europeanaType?lower_case}" id="item-image" class="trigger"/>
        
      <span about="${model.document.id}" rel="foaf:depiction">
        <span rel="foaf:thumbnail" resource="${model.thumbnailUrl}"></span>
      </span>
    </a>



    <#if (model.europeanaIsShownBy?? && model.europeanaIsShownAt?? && model.europeanaIsShownBy[0] != model.europeanaIsShownAt[0]) 
    
    || model.europeanaIsShownBy?? && 
    
    (   model.document.europeanaIsShownBy[0]?ends_with("jpeg")
      ||  model.document.europeanaIsShownBy[0]?ends_with("jpg")
      ||  model.document.europeanaIsShownBy[0]?ends_with("gif")
      ||  model.document.europeanaIsShownBy[0]?ends_with("png")
      
      ||  model.document.europeanaIsShownBy[0]?ends_with("mp3")
      ||  model.document.europeanaIsShownBy[0]?ends_with("mp4")
      ||  model.document.europeanaIsShownBy[0]?ends_with("avi")
      ||  model.document.europeanaIsShownBy[0]?ends_with("wmv")
      ||  model.document.europeanaIsShownBy[0]?ends_with("txt")
      ||  model.document.europeanaIsShownBy[0]?ends_with("pdf")
       
    )>
    
        <#assign shownBy = model.document.europeanaIsShownBy[0]/>
        <#if model.document.europeanaType?lower_case=='text'>
        <div class="trigger read">
          <a id="urlRefIsShownByPlay" href="${shownBy}" rel="nofollow" target="_blank">       
            <span class="trigger bold read"   title="<@spring.message 'read_t'/>"   rel="nofollow"><@spring.message 'read_t'/></span>
          </a>
        </div>
        
      <#elseif model.document.europeanaType?lower_case=='video' || model.document.europeanaType?lower_case=='sound'>

        <div class="trigger play">
          <a id="urlRefIsShownByPlay" href="${shownBy}" rel="nofollow" target="_blank">       
            <span class="trigger bold play"   title="<@spring.message 'play_t'/>"   rel="nofollow"><@spring.message 'play_t'/></span>
          </a>
        </div>
      
      <#elseif model.document.europeanaType?lower_case=='image'>
        
          <div class="trigger view">
          <a id="urlRefIsShownByPlay" href="${shownBy}" rel="nofollow" target="_blank">       
            <span class="trigger bold view" title="<@spring.message 'view_t'/>"   rel="nofollow"><@spring.message 'view_t'/></span>
          </a>
        </div>
        
        </#if>
        
        <#if model.document.europeanaType?lower_case=='3d'>
          <div class="trigger view">
          <a id="urlRefIsShownByPlay" href="${shownBy}" rel="nofollow" target="_blank">       
            <span class="trigger bold view" title="<@spring.message 'view_t'/>"   rel="nofollow"><@spring.message 'view_t'/></span>
          </a>
        </div>        
        </#if>
    </#if>  
  </#if>
</#if>
  
  
  
  <#--div style="float:right;"-->
      
  
    <#if model.document.europeanaType?lower_case=='3d'>
      <div class="object-type threeD"></div> 
    <#else>
      <div class="object-type ${model.document.europeanaType?lower_case}"></div>
    </#if>
  
    <#if model.document.userGeneratedContent>
      <div class="item-ugc" title="<@spring.message 'UserCreatedContent_t'/>"></div>
    </#if>
  
  <#--/div-->

</@compress>