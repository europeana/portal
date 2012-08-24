
<div class="six columns">
	<h4 id="collapse-header">Featured content<span class="collapse-icon"></span></h4>

	<div class="collapse-content">
		<c:if test='${not empty model.featuredItems}'>
		    <script type="text/javascript">
	            var carousel2Data = [];
				<c:forEach var="item" items="${model.featuredItems}">
					carousel2Data[carousel2Data.length] = {
						thumb:			"/${model.portalName}<spring:message code='${item.imgUrl}'/>",
						title:			"<spring:message code='${item.anchorTitle}'/>",
						description:	"<spring:message code='${item.p}'/>",
						link:			"/${model.portalName}<spring:message code='${item.anchorUrl}'/>"
					};
				</c:forEach>
			</script>
				
			
			<div id="carousel-2-header" class="europeana-header">
			</div>
			<div id="carousel-2" class="europeana-carousel">
			</div>
			<div id="carousel-2-footer" class="europeana-footer">
			</div>
			
		</c:if>		
	</div>
</div>


<div class="six columns" id="section-featured-partner">
	<h4>Featured partner</h4>
	
</div>



<%--

<c:if test='${not empty model.featuredItem}'>
  <h2><spring:message code='${model.featuredItem.h2}'/></h2>
  <a href="/${model.portalName}<spring:message code='${model.featuredItem.anchorUrl}'/>" title="<spring:message code='${model.featuredItem.anchorTitle}'/>" target="<spring:message code='${model.featuredItem.anchorTarget}'/>"><img src="/${model.portalName}<spring:message code='${model.featuredItem.imgUrl}'/>" alt="<spring:message code='${model.featuredItem.imgAlt}'/>" width="<spring:message code='${model.featuredItem.imgWidth}'/>" height="<spring:message code='${model.featuredItem.imgHeight}'/>"/></a>
  <h3><a href="/${model.portalName}<spring:message code='${model.featuredItem.anchorUrl}'/>" title="<spring:message code='${model.featuredItem.anchorTitle}'/>" target="<spring:message code='${model.featuredItem.anchorTarget}'/>"><spring:message code='${model.featuredItem.h3}'/></a></h3>
  <p><spring:message code='${model.featuredItem.p}'/></p>
</c:if>

 --%>