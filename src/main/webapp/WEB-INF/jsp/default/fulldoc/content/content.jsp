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
					<a href="" id="translate-item" class="toggle-menu-icon bold"><spring:message code="TranslateDetails_t" /></a>
				</div>
				
				<%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/full-excerpt.jsp" %>
				
			</div>	
		</div>
		
		
		<div class="row">
			<div class="twelve columns">
				<c:if test="${!empty model.moreLikeThis}">
					<script type="text/javascript">
						var carousel2Data = [];
						<c:forEach items="${model.moreLikeThis}" var="doc">
							carousel2Data[0] = {
								image:			decodeURI("${doc.thumbnail}").replace(/&amp;/g, '&'),
								title:			"${doc.title}"
							};
							carousel2Data[1] = {
								image:			decodeURI("${doc.thumbnail}").replace(/&amp;/g, '&'),
								title:			"${doc.title}"
							};
							carousel2Data[2] = {
									image:			decodeURI("${doc.thumbnail}").replace(/&amp;/g, '&'),
									title:			"${doc.title}"
								};
							carousel2Data[3] = {
									image:			decodeURI("${doc.thumbnail}").replace(/&amp;/g, '&'),
									title:			"${doc.title}"
								};
							carousel2Data[4] = {
									image:			decodeURI("${doc.thumbnail}").replace(/&amp;/g, '&'),
									title:			"${doc.title}"
								};
							carousel2Data[5] = {
									image:			decodeURI("${doc.thumbnail}").replace(/&amp;/g, '&'),
									title:			"${doc.title}"
								};
						</c:forEach>
						// alert(JSON.stringify(carousel2Data));
					</script>
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
				</c:if>
			</div>	
		</div>
	</div>
</div>
<!-- end content -->

