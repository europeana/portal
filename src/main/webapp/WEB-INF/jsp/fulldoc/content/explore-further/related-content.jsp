<#if model.fullBeanView.parent?? || model.fullBeanView.children??>

	<h3><a href="#related-content"><@spring.message 'RelatedContent_t'/></a></h3>
	
	<div id="related-content" class="carousel">
	
		<#if model.fullBeanView.parent??>
		
			<#-- EXAMPLE CODE FOR PARENT BREADCRUMB -->
			
			<#list model.fullBeanView.parents as parent>
			
				${parent.title}&nbsp;&gt;
				
			</#list>
			
			${model.document.title}
			
			<#-- END OF EXAMPLE CODE FOR PARENT BREADCRUMB -->
			
			<a	href="${model.fullBeanView.parent.fullDocUrl}"
				title="${model.fullBeanView.parent.title}"
				class="parent"
				rel="nofollow">
				
				<#if model.useCache>
	
					<img class="thumb"
						 src="${model.fullBeanView.parent.thumbnail}"
						 alt="${model.fullBeanView.parent.title}"
						 data-type="${model.fullBeanView.parent.type?lower_case}"/>
					
				<#else>
				
					<img class="thumb"
						 src="${model.fullBeanView.parent.thumbnail}"
						 alt="${model.fullBeanView.parent.title}"
						 data-type="${model.fullBeanView.parent.type?lower_case}"/>
				
				</#if>
				
				${model.fullBeanView.parent.title}
				
			</a>
		
		</#if>
			
		<div class="carousel-container" about="${model.document.id}">
			
			<#if model.fullBeanView.children??>
			
				<ul>
				
					<#list model.fullBeanView.children as child>
							
						<li>
							
							<#assign relItemQuery = 'europeana_uri:"${model.document.id}"' />
							
							<a  href='/${model.portalName}/${child.id?replace("http://www.europeana.eu/resolve/", "")}.html?query=${relItemQuery?url('UTF-8')}&amp;startPage=1&amp;pageId=brd'
								rel="rdfs:seeAlso" resource="${child.id}"
								title="${child.title}">
								<img src="${child.thumbnail}" alt="${child.title}" width="70" data-type="${child.type?lower_case}"/>
							</a>
							
						</li>
						
					</#list>
				
				</ul>
			
			</#if>
			
		</div>
		
	</div>

</#if>