<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<div id="content" class="row">

	<div class="twelve columns">
	
		<div class="row">
			<%@ include file="/WEB-INF/jsp/default/fulldoc/content/navigation/navigation.jsp" %>
		</div>

		<div class="row">

			<div class="three-cols-fulldoc-sidebar">
				<%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-left/sidebar-left.jsp" %>
			</div>


			<div class="nine-cols-fulldoc" id="main-fulldoc-area">
			
				<div class="row">
				
					<div class="nine-cols-fulldoc">
			
						<%-- Main data area --%>				
						<%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/full-excerpt.jsp" %>
					
					</div>
				
					<div id="sidebar-right-wrapper" class="three-cols-fulldoc-sidebar">
					
						<%-- Sidebar right --%>
						<div class="sidebar-right">
							<%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/see-also.jspf" %>
						</div>
						
					</div>
				
				</div>

			</div>
			
		</div>
		
		<c:if test="${!empty model.moreLikeThis}">

			<div class="row">
				<div class="twelve columns">

					<script type="text/javascript">
					
						<%-- Translation data for lightbox triggers: map lightboxble type to message 
						
						--%>
						var triggerLabels = {
								'image'		:	'<spring:message code="view_t"		/>',
								'play'		:	'<spring:message code="play_t"		/>',	// todo - base the label on the lightboxable type
								'download'	:	'<spring:message code="download_t"	/>',	// todo
								'read'		:	'<spring:message code="read_t"		/>'		// todo
						};
					
						<%-- data for carousel --%>
						var carousel2Data = [];
						<c:forEach items="${model.moreLikeThis}" var="doc">
						
							carousel2Data[carousel2Data.length] = {
								image:			decodeURI("${doc.thumbnail}").replace(/&amp;/g, '&'),
								title:			'${fn:join(doc.title, ' ') }'.replace(/\"/g, '&quot;'),
								link:			'/${model.portalName}/${doc.fullDocUrl}'
							};
							
						</c:forEach>
						
					</script>
					
					<%-- markup for carousel --%>
					<div id="explore-further">
						<h3><a href="#similar-content"><spring:message code="SimilarContent_t" /></a></h3>

						<%-- SEO friendly html rendering of images used as an image dimension measuring utility: has to live outside of the initially hidden #similar-content div --%>

						<div id="carousel-2-img-measure">
							<c:forEach var="similar" items="${model.moreLikeThis}">
								<img	src			= "${ fn:replace(  fn:escapeXml(similar.thumbnail), '&amp;', '&')}"
										alt			= "${fn:join(similar.title, ' ') }"
										title		= "${fn:join(similar.title, ' ') }"
										data-type	= "${fn:toLowerCase(similar.type)}"
										class		= "no-show"/>
							</c:forEach>
						</div>

						<div id="similar-content">

							<div id="carousel-2" about="${model.document.id}" class="europeana-carousel"></div>
							
						</div>
					</div>

				</div>	
			</div>
		</c:if>
	</div>
</div>



