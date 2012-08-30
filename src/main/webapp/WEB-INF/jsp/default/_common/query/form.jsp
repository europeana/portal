<%-- query action --%>

<c:set var="query_action" value="/${model.portalName}/search.html"/>
<c:if test="'map.html' == ${model.pageName}">
	<c:set var="query_value" value="/${model.portalName}/map.html"/>
</c:if>
<c:if test="'timeline.html' == ${model.pageName}">
	<c:set var="query_value" value="/${model.portalName}/timeline.html"/>
</c:if>

<%-- query value --%>

<c:set var="query_value" value=""/>
<c:if test="${not empty model.query}">
	<c:set var="query_value" value="${model.query}"/>
</c:if>

<%-- form --%>

	<form id="query-search" action="${query_action}" method="get">
		
			<div class="twelve columns search-wrapper">
				
				<!-- fieldset-->
				<%-- search options --%>
				<div class="search-menu" aria-hidden="true">
					<span class="hide-ilb-on-phones menu-label">Search...</span>
					<span class="icon-arrow-3"></span>
					<ul>
						<li class="item">	<a href="" class=""			>All fields</a></li>
						<li class="item">	<a href="" class="title:"	>Title</a></li>
						<li class="item">	<a href="" class="who:"		>Who</a></li>
						<li class="item">	<a href="" class="what:"	>What</a></li>
						<li class="item">	<a href="" class="when:"	>When</a></li>
						<li class="item">	<a href="" class="where:"	>Where</a></li>
					</ul>
				<!-- no whitespace allowed between end of div and input -->

				
				<!--  
				</div><input
					type="text" name="query" role="search" id="query-input" title="<spring:message code='SearchTerm_t'/>" value="${query_value}" maxlength="175" placeholder="<spring:message code="query_heading_t"/>"
				/><input
					id="submit-query" type="submit" class="submit-button" value="<spring:message code='Search_t'/>"
				/><a class="hide-on-phones search-help" href="/${model.portalName}/usingeuropeana.html"><spring:message code='rswHelp_t'/></a>
					<a class="show-on-phones search-help" href="/${model.portalName}/usingeuropeana.html">?</a>
				-->


				</div><input
					type="text" name="query" role="search" id="query-input" title="<spring:message code='SearchTerm_t'/>" value="${query_value}" maxlength="175" placeholder="<spring:message code="query_heading_t"/>"
				/><div
					id="submit-query" type="submit" class="submit-button" 
				>
				
					<input value="<spring:message code='Search_t'/>" type="submit"/>
					
					<a class="show-ilb-on-phones search-help" href="/${model.portalName}/usingeuropeana.html">?</a>
					
				</div>
				<a class="hide-ilb-on-phones search-help" href="/${model.portalName}/usingeuropeana.html"><spring:message code='rswHelp_t'/></a>
					
				<!--/fieldset-->
			</div>
			
			
			<%-- map search link --%>
	
			<c:if test="${model.debug} && 'map.html' == ${model.pageName}">
				<input type="checkbox" id="box_search"/>
				<label for="box_search"><spring:message code='MapBoxedSearch_t'/></label>
			</c:if>
	
			<%-- embedded search --%>
	
			<c:if test="${model.embedded}">
				<input type="hidden" name="embedded" value="${model.embeddedString}"/>
				<input type="hidden" name="embeddedBgColor" value="${model.embeddedBgColor}"/>
				<input type="hidden" name="embeddedForeColor" value="${model.embeddedForeColor}"/>
				<input type="hidden" name="embeddedLogo" value="${model.embeddedLogo}"/>
				<input type="hidden" name="rswUserId" value="${model.rswUserId}"/>
				<input type="hidden" name="rswDefqry" value="${model.rswDefqry}"/>
				<input type="hidden" name="lang" value="${model.locale}"/>
			</c:if>
		
		
		<%-- additional feature links for the search box --%>
		<c:if test="${not model.embedded}">
			<%-- refine search link --%>
	 		<c:set var="refinedEnabled" value=" disabled" />
			<c:if test="${!empty model.enableRefinedSearch && model.enableRefinedSearch}">
				<c:set var="refinedEnabled" value="" />
			</c:if>
	
			<a href="" id="refine-search" class="nofollow${refinedEnabled}"><spring:message code="RefineYourSearch_t" /></a>
	
			<%-- save search link --%>
	 	
			<c:if test="${model.user} && 'search.html' == ${model.pageName}">
				<spring:message code='SaveToMyEuropeana_t'/>
				<c:if test="${model.briefBeanView}">
					<input type="text" value="${model.briefBeanView.pagination.presentationQuery.queryToSave}"/>
				</c:if>
				<c:if test="${model.query}">
					<input type="text" value="${model.query}"/>
				</c:if>
			 </c:if>
	
		</c:if>
	</form>
