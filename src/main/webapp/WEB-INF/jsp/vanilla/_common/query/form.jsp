<%--
  query action
--%>
<c:set var="query_action" value="/${model.portalName}/search.html"/>
<c:if test="'map.html' == ${model.pageName}">
  <c:set var="query_value" value="/${model.portalName}/map.html"/>
</c:if>
<c:if test="'timeline.html' == ${model.pageName}">
  <c:set var="query_value" value="/${model.portalName}/timeline.html"/>
</c:if>
<%--
  query value
--%>
<c:set var="query_value" value=""/>
<c:if test="${not empty model.query}">
  <c:set var="query_value" value="${model.query}"/>
</c:if>
<%--
  form
--%>
<form action="${query_action}" method="get">
	<fieldset>
		<input type="text" name="query" title="<spring:message code='SearchTerm_t'/>" value="${query_value}" maxlength="175"/>
		<input type="submit" class="submit-button" value="<spring:message code='Search_t'/>"/>
		<%--
		  map search link
		--%>
		<c:if test="${model.debug} && 'map.html' == ${model.pageName}">
		  <input type="checkbox" id="box_search"/>
		  <label for="box_search"><spring:message code='MapBoxedSearch_t'/></label>
		</c:if>
		<%--
		  embedded search
		--%>
		<c:if test="${model.embedded}">
			<input type="hidden" name="embedded" value="${model.embeddedString}"/>
			<input type="hidden" name="embeddedBgColor" value="${model.embeddedBgColor}"/>
			<input type="hidden" name="embeddedForeColor" value="${model.embeddedForeColor}"/>
			<input type="hidden" name="embeddedLogo" value="${model.embeddedLogo}"/>
			<input type="hidden" name="rswUserId" value="${model.rswUserId}"/>
			<input type="hidden" name="rswDefqry" value="${model.rswDefqry}"/>
			<input type="hidden" name="lang" value="${model.locale}"/>
		</c:if>
	</fieldset>
  <%--
    additional feature links for the search box
  --%>
  <c:if test="${not model.embedded}">
    <%--
      refine search link
    --%>
    <spring:message code='RefineYourSearch_t'/>
    <%--
      save search link
    --%>
    <c:if test="${!empty model.user} && 'search.html' == ${model.pageName}">
      <spring:message code='SaveToMyEuropeana_t'/>
      <c:if test="${model.briefBeanView}">
        <input type="text" value="${model.briefBeanView.pagination.presentationQuery.queryToSave}"/>
      </c:if>
      <c:if test="${model.query}">
        <input type="text" value="${model.query}"/>
      </c:if>
    </c:if>
    <%--
      help link
    --%>
    <a href="/${model.portalName}/usingeuropeana.html"><spring:message code='rswHelp_t'/></a>
  </c:if>
</form>
