<%@ include file="/WEB-INF/jsp/default/_common/header-strip.jsp" %>

<div id="header" role="banner" class="row">
	<div id="logo-and-search">
		<div id="logo">
			<c:set var="logoWrapperTag" value="h1"/>
			<c:if test="${model.pageName == 'full-doc.html'}">
				<c:set var="logoWrapperTag" value="div"/>
			</c:if>

			<c:set var="logoClass" value="logo"/>
			<c:set var="logoBg" value=""/>

			<c:choose>
				<c:when test="${model.pageName == 'staticpage.html' && model.tc}">
					<c:set var="logoClass" value="logo-t-and-c"/>
				</c:when>
				<c:otherwise>
					<c:set var="logoBg" value="europeana-logo-${model.locale}.png"/>
				</c:otherwise>
			</c:choose>
			
			<${logoWrapperTag} title="<spring:message code="AltLogoEuropeana_t" />">
				<a	href="/${model.portalName}/"
					title="<spring:message code="AltLogoEuropeana_t" />">
					
					<span	class="${logoClass}"
							<c:if test="${!empty logoBg}">
							style="background-image:url('/${model.portalName}/sp/img/${logoBg}')"
							</c:if>
					></span>
				</a>
			</${logoWrapperTag}>
			
			
			<script type="text/javascript">
				var completionTranslations = {};
				completionTranslations['Title']			= "<spring:message code="FieldedSearchTitle_t" />";
				completionTranslations['Place']			= "<spring:message code="FieldedSearchWhere_t" />";
				completionTranslations['Time/Period']	= "<spring:message code="FieldedSearchWhen_t" />";
				completionTranslations['Subject']		= "<spring:message code="FieldedSearchWhat_t" />";
				completionTranslations['Creator']		= "<spring:message code="FieldedSearchWho_t" />";
	
				var completionClasses = {};
				completionClasses['Title']			= "title:";
				completionClasses['Place']			= "where:";
				completionClasses['Time/Period']	= "when:";
				completionClasses['Subject']		= "what:";
				completionClasses['Creator']		= "who:";
			</script>
		</div>


		<c:if test="${!(model.pageName == 'staticpage.html' && model.tc)}">
			<%@ include file="/WEB-INF/jsp/default/_common/query/query.jsp" %>
		</c:if>
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
