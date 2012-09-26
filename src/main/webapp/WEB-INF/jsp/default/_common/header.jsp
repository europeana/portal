<%@ include file="/WEB-INF/jsp/default/_common/header-strip.jsp" %>

<div id="header" role="banner" class="row">

	<div id="logo-and-search">
	
		<div id="logo">
			
			<h1 title="<spring:message code='AltLogoEuropeana_t' />">
			
				<a href="/${model.portalName}/" title="<spring:message code='AltLogoEuropeana_t' />">
				
					<span class="responsive-logo" alt="<spring:message code='AltLogoEuropeana_t' />"></span>

				</a>
				
			</h1>

		</div>
		
		<%@ include file="/WEB-INF/jsp/default/_common/query/query.jsp" %>
		
	</div>

</div>
