<c:if test="${empty isSearchWidget}">
	<%@ include file="/WEB-INF/jsp/default/_common/header-strip.jsp" %>
</c:if>

<div id="header" role="banner" class="row">
	<%-- we used to use the presence of the "hamburger" for javascript to detect we were on phones.
		 that broke when we moved to the cog.
		 this offscreen-div together with the styling defined in header.css and the phoneTest fn defined in utils replaces that
	--%>
	<div class="phone-test"></div>

	<div id="logo-and-search">
		<c:if test="${empty isSearchWidget}">
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
					<a	href="${homeUrl}"
						title="<spring:message code="AltLogoEuropeana_t" />">
						
						<span class="${logoClass}">&nbsp;</span>
						 <%-- localised logos not implemented yet
						<c:if test="${!empty logoBg}">
							<style type="text/css">
								.${logoWrapperTag} .${logoClass} { background-image:url(themes/default/images/europeana-logo-2.png); }
							</style>
						</c:if>
						--%>
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
		</c:if>

		<c:if test="${!(model.pageName == 'staticpage.html' && model.tc)}">
			<%@ include file="/WEB-INF/jsp/default/_common/query/query.jsp" %>
		</c:if>
	</div>

<c:if test="${empty isSearchWidget}">
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
</c:if>
</div>
