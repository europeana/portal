<%@ include file="/WEB-INF/jsp/default/_common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/variables/variables.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/html-open.jsp" %>

<c:if test="${!model.embeddedConsole}">
  <%@ include file="/WEB-INF/jsp/default/_common/header.jsp" %>
</c:if>

<script type="text/javascript">
<!--
var apiconsole = {
	selectedPanel: '${model.function}',
	showHeaderText: '<spring:message code="apiconsole_show_headers_t" />',
	hideHeaderText: '<spring:message code="apiconsole_hide_headers_t" />'
};
//-->
</script>

  <div class="row" id="api-console">
    <div id="api-form" class="three columns">
      <div class="api-form-inner">
        <h2><spring:message code="apiconsole_function_t" />:</h2>
        <p class="section global-radios">
          <c:forEach items="${model.supportedFunctions}" var="function">
            <input type="radio" name="function" id="api-function-${function}" value="${function}" <c:if test="${model.function == function}">checked="checked"</c:if> />
            <label for="api-function-${function}">${function}</label>
          </c:forEach>
        </p>

        <fieldset id="search-panel">
          <legend><spring:message code="apiconsole_search_parameters_t" /></legend>

          <form>
            <input type="hidden" name="function" value="search" />
            <input type="hidden" name="embedded" value="${model.embeddedConsole}" />

            <label for="api-query"><spring:message code="apiconsole_query_t" />:</label><br/>
            <c:set var="searchQuery" value="" />
            <c:if test="${model.function == 'search'}">
              <c:set var="searchQuery" value="${fn:escapeXml(model.query)}" />
            </c:if>
            <input type="text" id="api-query" name="query" value="${searchQuery}" /><br/>

            <label for="api-qf"><spring:message code="apiconsole_refinements_t" />:</label><br/>
            <c:forEach items="${model.refinements}" var="qf">
              <input type="text" id="api-qf" name="qf" value="${fn:escapeXml(qf)}" /><br/>
            </c:forEach>
            <input type="text" id="api-qf" name="qf" value="${qf}" />
            <br/>
            <br/>

            <p class="section">
              <label for="api-latlong"><spring:message code="apiconsole_spatial_t" />:</label><br/>
              <span class="api-latlong"><spring:message code="apiconsole_lat_t" /></span>
              <input type="text" id="api-latMin" name="latMin" value="${model.latMin}" class="api-latlong"/> &mdash;
              <input type="text" id="api-latMax" name="latMax" value="${model.latMax}" class="api-latlong" /><br/>
              <span class="api-latlong"><spring:message code="apiconsole_long_t" /></span>
              <input type="text" id="api-longMin" name="longMin" value="${model.longMin}" class="api-latlong" /> &mdash;
              <input type="text" id="api-longMax" name="longMax" value="${model.longMax}" class="api-latlong" /><br/>
              <span class="api-help"><spring:message code="apiconsole_spatial_help_t" /></span>
            </p>

            <p class="section">
              <label for="api-latlong"><spring:message code="apiconsole_temporal_t" />:</label><br/>
              <span class="api-latlong"><spring:message code="apiconsole_year_t" /></span>
              <input type="text" id="api-yearMin" name="yearMin" value="${model.yearMin}" class="api-latlong"/> &mdash;
              <input type="text" id="api-yearMax" name="yearMax" value="${model.yearMax}" class="api-latlong" /><br/>
              <span class="api-help"><spring:message code="apiconsole_temporal_help_t" /></span>
            </p>

            <p>
              <label for="api-reusability"><spring:message code="apiconsole_reusability_t" />:</label><br/>
              <c:forEach items="${model.supportedReusabilityValues}" var="reusability">
                <input type="checkbox" name="reusability" id="api-reusability-${reusability.key}" value="${reusability.key}" 
                  <c:if test="${!empty model.reusability[reusability.key] && model.reusability[reusability.key] == true}">checked="checked"</c:if> />
                <label for="api-reusability-${reusability.key}"><spring:message code="${reusability.value}" /></label><br/>
              </c:forEach>
            </p>

            <p>
              <label for="api-profile"><spring:message code="apiconsole_profile_t" />:</label><br/>
              <c:forEach items="${model.defaultSearchProfiles}" var="profile">
                <input type="checkbox" name="profile" id="api-profile-${profile.key}" value="${profile.key}" <c:if test="${profile.value == true}">checked="checked"</c:if> />
                <label for="api-profile-${profile.key}">${profile.key}</label><br/>
              </c:forEach>
            </p>

            <p class="section">
              <label for="api-rows"><spring:message code="apiconsole_rows_t" />:</label><br/>
              <c:forEach items="${model.defaultRows}" var="rows">
                <input type="radio" name="rows" id="api-rows-${rows}" value="${rows}" <c:if test="${model.rows == rows}">checked="checked"</c:if> />
                <label for="api-rows-${rows}">${rows}</label>
              </c:forEach>
            </p>

            <p class="section">
              <label for="api-start"><spring:message code="apiconsole_start_t" />:</label><br/>
              <input type="text" name="start" value="${model.start}" />
            </p>

            <p class="section">
              <label for="api-callback"><spring:message code="apiconsole_callback_t" />:</label><br/>
              <input type="text" name="callback" value="${model.callback}" />
            </p>

            <div id="submit-wrapper">
              <input type="submit" value="get result" class="deans-button-1 europeana-button-1 api-submit">
            </div>
          </form>
        </fieldset>

        <fieldset id="record-panel">
          <legend><spring:message code="apiconsole_record_parameters_t" /></legend>

          <form>
            <input type="hidden" name="function" value="record" />
            <input type="hidden" name="embedded" value="${model.embeddedConsole}" />

            <%--
            <label for="api-collectionId">Collection id:</label><br/>
            <input type="text" id="api-collectionId" name="collectionId" value="${model.collectionId}" /><br/>
           --%>

            <label for="api-profile"><spring:message code="apiconsole_profile_t" />:</label><br/>
            <c:forEach items="${model.defaultObjectProfiles}" var="profile">
              <%-- option value="${profile}" <c:if test="${model.profile == profile}">selected="selected"</c:if>>${profile}</option --%>
              <input type="checkbox" name="profile" id="api-profile-${profile.key}" value="${profile.key}" <c:if test="${profile.value == true}">checked="checked"</c:if> />
              <label for="api-profile-${profile.key}">${profile.key}</label><br/>
            </c:forEach>

            <label for="api-recordId"><spring:message code="apiconsole_record_id_t" />:</label><br/>
            <input type="text" id="api-recordId" name="recordId" value="${model.recordId}" /><br/>

            <p class="section">
              <label for="api-callback"><spring:message code="apiconsole_callback_t" />:</label><br/>
              <input type="text" name="callback" value="${model.callback}" />
            </p>

            <div id="submit-wrapper">
              <input type="submit" value="get result" class="deans-button-1 europeana-button-1 api-submit">
            </div>

          </form>
        </fieldset>

        <fieldset id="suggestions-panel">
          <legend><spring:message code="apiconsole_suggestions_parameters_t" /></legend>

          <form>
            <input type="hidden" name="function" value="suggestions" />
            <input type="hidden" name="embedded" value="${model.embeddedConsole}" />

            <label for="api-query"><spring:message code="apiconsole_query_t" />:</label><br/>

            <c:set var="suggestionsQuery" value="" />
            <c:if test="${model.function == 'suggestions'}">
              <c:set var="suggestionsQuery" value="${fn:escapeXml(model.query)}" />
            </c:if>
            <input type="text" id="api-query" name="query" value="${suggestionsQuery}" /><br/>

            <p class="section">
              <label for="api-suggestions-rows"><spring:message code="apiconsole_rows_t" />:</label><br/>
              <c:forEach items="${model.defaultRows}" var="rows">
                <input type="radio" name="rows" id="api-suggestions-rows-${rows}" value="${rows}" <c:if test="${model.rows == rows}">checked="checked"</c:if> />
                <label for="api-suggestions-rows-${rows}">${rows}</label>
              </c:forEach>
            </p>

            <label for="api-phrases"><spring:message code="apiconsole_phrases_t" />:</label><br/>
            <input type="checkbox" id="api-phrases" name="phrases" <c:if test="${model.phrases}">checked="checked"</c:if> /><br/>

            <p class="section">
              <label for="api-callback"><spring:message code="apiconsole_callback_t" />:</label><br/>
              <input type="text" name="callback" value="${model.callback}" />
            </p>

            <div id="submit-wrapper">
              <input type="submit" value="get result" class="deans-button-1 europeana-button-1 api-submit">
            </div>
          </form>
        </fieldset>
      </div>
    </div>

    <div id="api-result" class="nine columns">
      <div class="api-result-inner">
        <div id="request-url-title"><h2><spring:message code="apiconsole_request_url_t" /></h2></div>
        <div id="request-url">${model.apiUrl}</div>
        <div id="request-status-title"><h2><spring:message code="apiconsole_http_status_code_t" /></h2></div>
        <div id="request-status">${model.httpStatusCode}</div>
        <div id="show-http-headers"><a href="#"><spring:message code="apiconsole_show_headers_t" /></a></div>
        <div class="request-http-headers-title"><h2><spring:message code="apiconsole_request_headers_t" /></h2></div>
        <div class="request-http-headers">
          <c:forEach items="${model.requestHeaders}" var="entry">${entry.key}: ${entry.value}<br/></c:forEach>
        </div>
        <div class="request-http-headers-title"><h2><spring:message code="apiconsole_response_headers_t" /></h2></div>
        <div class="request-http-headers">
          <c:forEach items="${model.responseHeaders}" var="entry">${entry.key}: ${entry.value}<br/></c:forEach>
        </div>
        <div id="response-title"><h2><spring:message code="apiconsole_response_t" /></h2></div>
        <div id="response">${model.jsonString}</div>
      </div>
    </div>
  </div>
<c:if test="${!model.embeddedConsole}">
  <%@ include file="/WEB-INF/jsp/default/_common/footer.jsp" %>
</c:if>

<%@ include file="/WEB-INF/jsp/default/_common/html/javascripts.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/html/close-html.jsp" %>