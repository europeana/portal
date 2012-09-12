<!-- content -->
<div id="content" class="row">
	<div class="twelve columns">
		<div class="row">
			<div class="twelve columns">
				<%@ include file="/WEB-INF/jsp/default/fulldoc/content/navigation/navigation.jsp" %>
			</div>
		</div>

		<div class="row">
			<div class="three columns">
				<%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-left/sidebar-left.jsp" %>
			</div>
			<div class="nine columns" id="main-fulldoc-area">
				<%-- Translation link --%>
				<div class="translate-box">
					<a href="" id="translate-item" class="bold"><span class="iconD hide-ilb-on-phones"></span><spring:message code="TranslateDetails_t" /><span class="iconP show-ilb-on-phones"></span></a>
				</div>
				
				<%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/full-excerpt.jsp" %>
				
			</div>	
		</div>
		
		
		<div class="row">
			<div class="twelve columns">
			
			
			
			<%--
			 --%>
	
			
			
				<c:if test="${!empty model.moreLikeThis}">
				
					<script type="text/javascript">
						var carousel2Data = [];
						<c:forEach items="${model.moreLikeThis}" var="doc">
						
							carousel2Data[carousel2Data.length] = {
								image:			decodeURI("${doc.thumbnail}").replace(/&amp;/g, '&'),
								title:			"${fn:join(doc.title, ' ') }"
							};
							
						</c:forEach>
						//alert(carousel2Data.length + "\n\n" +  JSON.stringify(carousel2Data));				
					</script>
					
					
<%--					
					
<c:if test="${!empty model.fullBeanView.relatedItems}">
	<h3><a href="#similar-content"><spring:message code="SimilarContent_t" /></a></h3>
	<div id="similar-content" class="carousel">
		<div class="carousel-container" about="${model.document.id}">
			<ul>
				<c:forEach items="${model.fullBeanView.relatedItems}" var="doc">
					<li>
						<c:set var="relItemQuery" value='europeana_uri:"${model.document.id}"' />
							<img src="${doc.thumbnail}" alt="${doc.title}" width="70" data-type="${fn:toLowerCase(doc.type)}" />

					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</c:if>

--%>
			

<div id="explore-further">


	<h3><a href="#similar-content"><spring:message code="SimilarContent_t" /></a></h3>
	<div id="similar-content">

						<%--
						<div id="carousel-2-header" class="europeana-header">
						</div>
						--%>
						<div id="carousel-2"  about="${model.document.id}" class="europeana-carousel">
						</div>
						<%--
						<div id="carousel-2-footer" class="europeana-footer">
						</div>
						--%>



	</div>
</div>					
					
					
					


					
				</c:if>
				
				
				
				
				
			</div>	
		</div>
	</div>
</div>
<!-- end content -->

