<div class="tout">
		
	<#if model.featuredItem??>
	
		<h2><@spring.message '${model.featuredItem.h2}'/></h2>
		<a href="/${model.portalName}<@spring.message '${model.featuredItem.anchorUrl}'/>" title="<@spring.message '${model.featuredItem.anchorTitle}'/>" target="<@spring.message '${model.featuredItem.anchorTarget}'/>" class="image"><img src="/${model.portalName}<@spring.message '${model.featuredItem.imgUrl}'/>" alt="<@spring.message '${model.featuredItem.imgAlt}'/>" width="<@spring.message '${model.featuredItem.imgWidth}'/>" height="<@spring.message '${model.featuredItem.imgHeight}'/>"/></a>
		<h3><a href="/${model.portalName}<@spring.message '${model.featuredItem.anchorUrl}'/>" title="<@spring.message '${model.featuredItem.anchorTitle}'/>" target="<@spring.message '${model.featuredItem.anchorTarget}'/>"><@spring.message '${model.featuredItem.h3}'/></a></h3>
		<p><@msgPropertyLimiter '${model.featuredItem.p}' '${featured_item_snipet_limit}' /></p>
		
	</#if>

</div>
