<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


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
		
		<c:if test="${!empty model.moreLikeThis}">

			<div class="row">
				<div class="twelve columns">

					<%-- data for carousel --%>
					<script type="text/javascript">
						var carousel2Data = [];
						<c:forEach items="${model.moreLikeThis}" var="doc">
						
							carousel2Data[carousel2Data.length] = {
								image:			decodeURI("${doc.thumbnail}").replace(/&amp;/g, '&'),
								title:			"${fn:join(doc.title, ' ') }"
							};
							
						</c:forEach>
					</script>
					
					<%-- markup for carousel --%>
					<div id="explore-further">
						<h3><a href="#similar-content"><spring:message code="SimilarContent_t" /></a></h3>
						<div id="similar-content">
						
							<%--
							<div id="carousel-2-header" class="europeana-header"></div>
							--%>
							
							<div id="carousel-2" about="${model.document.id}" class="europeana-carousel">

								<c:forEach var="similar" items="${model.moreLikeThis}">
									<img	src="${ fn:replace(  fn:escapeXml(similar.thumbnail), '&amp;', '&')}"
											data-type="${fn:toLowerCase(similar.type)}"
									/>
								</c:forEach>
							
							</div>
							
							<%--
							<div id="carousel-2-footer" class="europeana-footer"></div>
							--%>
							
						</div>
					</div>

				</div>	
			</div>
		</c:if>
	</div>
</div>



