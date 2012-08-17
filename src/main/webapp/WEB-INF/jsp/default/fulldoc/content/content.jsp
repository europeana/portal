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
	</div>
</div>
<!-- end content -->
