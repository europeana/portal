<h3 id="explore"><spring:message code="ExploreFurther_t" />!</h3>

<div id="explore-further">
	
	<%-- Similar content --%>

	<c:if test="${!empty model.fullBeanView.relatedItems}">
		<h3><a href="#similar-content"><spring:message code="SimilarContent_t" /></a></h3>
		<div id="similar-content" class="carousel">
			<div class="carousel-container" about="${model.document.id}">
				<ul>
					<c:forEach items="${model.fullBeanView.relatedItems}" var="doc">
						<li>
							<c:set var="relItemQuery" value='europeana_uri:"${model.document.id}"' />
							<a href="${doc.fullDocUrl}?theme=${model.theme}" rel="rdfs:seeAlso" resource="${doc.id}" title="${doc.title}">


								<%-- Andy: this outputs all the thumbnails of all the related items, but maybe we only want the 1st thumbnail.... --%>
								
								<c:forEach items="${doc.edmObject}" var="thumbnail">
									<img src="${thumbnail}" alt="${doc.title}" width="70" data-type="${fn:toLowerCase(doc.type)}" />
								</c:forEach>
				
								<%-- Andy: original ESE thumbnail code --%>
								<%-- <img src="${doc.thumbnail}" alt="${doc.title}" width="70" data-type="${fn:toLowerCase(doc.type)}" /> --%>
								
							</a>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</c:if>
 
	<%-- Related content --%>
	
	<c:if test="${!empty model.fullBeanView.parents || !empty model.fullBeanView.children}">
		<h3><a href="#related-content"><spring:message code="RelatedContent_t" /></a></h3>
		<div id="related-content" class="carousel">
			<c:if test="${model.fullBeanView.parent}">
			
				
				<c:forEach items="${model.fullBeanView.parents}" var="parent">
					${parent.title}&nbsp;&gt;
				</c:forEach>
				${model.document.title}
				
				
				<a href="${model.fullBeanView.parent.fullDocUrl}?theme=${model.theme}" title="${model.fullBeanView.parent.title}" class="parent" rel="nofollow">
					<c:choose>
						<c:when test="${model.useCache}">
							<img class="thumb"
								 src="${model.fullBeanView.parent.thumbnail}"
								 alt="${model.fullBeanView.parent.title}"
								 data-type="${fn:toLowerCase(model.fullBeanView.parent.type)}"/>
						</c:when>
						<c:otherwise>
							<img class="thumb"
								 src="${model.fullBeanView.parent.thumbnail}"
								 alt="${model.fullBeanView.parent.title}"
								 data-type="${fn:toLowerCase(model.fullBeanView.parent.type)}" />
						</c:otherwise>
					</c:choose>
					${model.fullBeanView.parent.title}
				</a>
			</c:if>
			
			<div class="carousel-container" about="${model.document.id}">
				<c:if test="${model.fullBeanView.children}">
					<ul>
						<c:forEach items="${model.fullBeanView.children}" var="child">
							<li>
								<c:set var="relItemQuery" value='europeana_uri:"${model.document.id}"' />
								<a  href='/${model.portalName}/${fn:replace(child.id, "http://www.europeana.eu/resolve/", "")}.html?query=${relItemQuery}&amp;startPage=1&amp;pageId=brd'
									rel="rdfs:seeAlso" resource="${child.id}"
									title="${child.title}">
									
									<img src="${child.thumbnail}" alt="${child.title}" width="70" data-type="${fn:toLowerCase(child.type)}"/>
								</a>
							</li>
						</c:forEach>
					</ul>
				</c:if>
			</div>
		</div>
	</c:if>


	<%-- <#include '/fulldoc/content/explore-further/map.ftl' /> --%>

	<h3><a href="#mapview"><spring:message code="AltMapView_t" /></a></h3>

	<div id="mapview">
		<div id="e4DContainer">
			<div id="e4DContainer-msg">
				<span><spring:message code="loading_t" /></span>
			</div>
		</div>
	</div>
	
</div>
