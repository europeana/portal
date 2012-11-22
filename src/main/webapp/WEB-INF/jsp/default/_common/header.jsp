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
		
		<script type="text/javascript">
			var completionTranslations = {};
			completionTranslations['Title']			= "<spring:message code='FieldedSearchTitle_t' />";
			completionTranslations['Place']			= "<spring:message code='FieldedSearchWhere_t' />";
			completionTranslations['Time/Period']	= "<spring:message code='FieldedSearchWhen_t' />";
			completionTranslations['Subject']		= "<spring:message code='FieldedSearchWhat_t' />";
			completionTranslations['Creator']		= "<spring:message code='FieldedSearchWho_t' />";			
			
			var completionClasses = {};
			completionClasses['Title']			= "title:";
			completionClasses['Place']			= "where:";
			completionClasses['Time/Period']	= "when:";
			completionClasses['Subject']		= "what:";
			completionClasses['Creator']		= "who:";
		</script>
		
		<%@ include file="/WEB-INF/jsp/default/_common/query/query.jsp" %>
		
	</div>

<c:set var="sharethis" value="
<div id='shareicons'>
	<span class='st_facebook_large' displayText='Facebook'></span>
	<span class='st_twitter_large' displayText='Tweet'></span>
	<span class='st_email_large' displayText='Email'></span>
	<span class='st_sharethis_large' displayText='ShareThis'></span>
</div>
"/>
<c:choose>
<c:when test='${model.pageName == "index.html"}'>
	<c:out value="${sharethis}" escapeXml="false" />
</c:when>
<c:when test='${model.pageName == "search.html"}'>
	<c:out value="${sharethis}" escapeXml="false" />
</c:when>
<c:when test='${model.pageName == "fulldoc.html"}'>
	<c:out value="${sharethis}" escapeXml="false" />
</c:when>
</c:choose>
	
</div>
