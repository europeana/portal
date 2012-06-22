<#if model.fullBeanView.relatedItems??>

	<h3><a href="#similar-content"><@spring.message 'SimilarContent_t'/></a></h3>
	
	<div id="similar-content" class="carousel">
		
		<div class="carousel-container" about="${model.document.id}">
		
			<ul>
			
				<#list model.fullBeanView.relatedItems as doc>
						
					<li>
						
						<#assign relItemQuery = 'europeana_uri:"${model.document.id}"' />
						
						<a href="${doc.fullDocUrl}" rel="rdfs:seeAlso" resource="${doc.id}" title="${doc.title}">
						
							<img src="${doc.thumbnail}" alt="${doc.title}" width="70" data-type="${doc.type?lower_case}"/>
							
						</a>
						
					</li>
					
				</#list>
			
			</ul>
			
		</div>
		
	</div>
	
</#if>
