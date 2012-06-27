<div {menu_user_exists}>

    <h3><spring.message 'query_heading_t'/></h3>
		<%--@ include file="/WEB-INF/jsp/vanilla/query/form.jsp" --%>
    <%--@ include file="/WEB-INF/jsp/vanilla/query/refinement.jsp" --%>
	
	<#--
		did you mean suggestion
		nb: how to handle for embedded widget - might be best to have the backend create the url
	-->
		
		<if !model.embedded && model.showDidYouMean>
		
			<div id="additional-feedback">
				
				<spring.message 'Didyoumean_t'/>:
				<a href="/{model.portalName}/search.html?query={model.briefBeanView.spellCheck.collatedResult}">{model.briefBeanView.spellCheck.collatedResult}</a>
				
			</div>
			
		</if>
	
</div>